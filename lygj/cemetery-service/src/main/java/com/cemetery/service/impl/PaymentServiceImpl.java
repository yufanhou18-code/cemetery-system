package com.cemetery.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.PaymentRecordDTO;
import com.cemetery.domain.entity.PaymentRecord;
import com.cemetery.domain.entity.ServiceOrder;
import com.cemetery.domain.enums.OrderStatusEnum;
import com.cemetery.domain.enums.PaymentStatusEnum;
import com.cemetery.domain.mapper.PaymentRecordMapper;
import com.cemetery.domain.mapper.ServiceOrderMapper;
import com.cemetery.domain.vo.PaymentRecordVO;
import com.cemetery.service.PaymentService;
import com.cemetery.service.payment.PaymentContext;
import com.cemetery.service.payment.PaymentProcessor;
import com.cemetery.service.payment.PaymentStateFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 支付服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRecordMapper paymentRecordMapper;
    private final ServiceOrderMapper serviceOrderMapper;
    private final PaymentProcessor paymentProcessor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPaymentRecord(PaymentRecordDTO paymentRecordDTO) {
        // 验证订单是否存在
        ServiceOrder serviceOrder = serviceOrderMapper.selectById(paymentRecordDTO.getOrderId());
        if (serviceOrder == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (!OrderStatusEnum.PENDING_PAYMENT.equals(serviceOrder.getStatus())) {
            throw new BusinessException("订单状态不正确，不能创建支付记录");
        }

        // 生成支付流水号
        String paymentNo = generatePaymentNo();

        // 转换并保存
        PaymentRecord paymentRecord = EntityConverter.INSTANCE.toPaymentRecord(paymentRecordDTO);
        paymentRecord.setPaymentNo(paymentNo);
        paymentRecord.setOrderNo(serviceOrder.getOrderNo());
        paymentRecord.setUserId(serviceOrder.getUserId());
        paymentRecord.setPaymentStatus(PaymentStatusEnum.PENDING);

        paymentRecordMapper.insert(paymentRecord);
        log.info("创建支付记录成功, paymentId={}, paymentNo={}", paymentRecord.getId(), paymentNo);
        return paymentRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentSuccess(String paymentNo, String transactionId) {
        if (StringUtils.isBlank(paymentNo)) {
            throw new BusinessException("支付流水号不能为空");
        }

        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        // 更新支付记录
        paymentRecord.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        paymentRecord.setTransactionId(transactionId);
        paymentRecord.setPaymentTime(new Date());
        paymentRecordMapper.updateById(paymentRecord);

        // 更新订单状态
        ServiceOrder serviceOrder = serviceOrderMapper.selectById(paymentRecord.getOrderId());
        if (serviceOrder != null) {
            serviceOrder.setStatus(OrderStatusEnum.PAID);
            serviceOrderMapper.updateById(serviceOrder);
        }

        log.info("处理支付成功, paymentNo={}, transactionId={}", paymentNo, transactionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentFailed(String paymentNo, String failReason) {
        if (StringUtils.isBlank(paymentNo)) {
            throw new BusinessException("支付流水号不能为空");
        }

        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        paymentRecord.setPaymentStatus(PaymentStatusEnum.FAILED);
        paymentRecord.setRemark(failReason);
        paymentRecordMapper.updateById(paymentRecord);

        log.info("处理支付失败, paymentNo={}, reason={}", paymentNo, failReason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRefund(String paymentNo, String refundReason) {
        if (StringUtils.isBlank(paymentNo)) {
            throw new BusinessException("支付流水号不能为空");
        }

        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        // 使用状态机处理退款
        PaymentContext context = new PaymentContext(paymentRecord);
        context.getCurrentState().applyRefund(context, refundReason);
        
        // 更新数据库
        paymentRecord.setPaymentStatus(context.getCurrentStatus());
        paymentRecordMapper.updateById(paymentRecord);
        
        // 更新订单状态为已退款
        updateOrderStatus(paymentRecord.getOrderId(), OrderStatusEnum.REFUNDED);

        log.info("申请退款成功, paymentNo={}, reason={}", paymentNo, refundReason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleRefundSuccess(String paymentNo) {
        if (StringUtils.isBlank(paymentNo)) {
            throw new BusinessException("支付流水号不能为空");
        }

        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        paymentRecord.setPaymentStatus(PaymentStatusEnum.REFUNDED);
        paymentRecord.setRefundTime(new Date());
        paymentRecordMapper.updateById(paymentRecord);

        // 更新订单状态
        ServiceOrder serviceOrder = serviceOrderMapper.selectById(paymentRecord.getOrderId());
        if (serviceOrder != null) {
            serviceOrder.setStatus(OrderStatusEnum.REFUNDED);
            serviceOrderMapper.updateById(serviceOrder);
        }

        log.info("处理退款成功, paymentNo={}", paymentNo);
    }

    @Override
    public PaymentRecordVO getPaymentByPaymentNo(String paymentNo) {
        if (StringUtils.isBlank(paymentNo)) {
            throw new BusinessException("支付流水号不能为空");
        }

        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        return paymentRecord != null ? EntityConverter.INSTANCE.toPaymentRecordVO(paymentRecord) : null;
    }

    @Override
    public List<PaymentRecordVO> getPaymentsByOrderId(Long orderId) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }

        List<PaymentRecord> paymentList = paymentRecordMapper.selectByOrderId(orderId);
        return EntityConverter.INSTANCE.toPaymentRecordVOList(paymentList);
    }

    @Override
    public List<PaymentRecordVO> getPaymentsByOrderNo(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException("订单编号不能为空");
        }

        List<PaymentRecord> paymentList = paymentRecordMapper.selectByOrderNo(orderNo);
        return EntityConverter.INSTANCE.toPaymentRecordVOList(paymentList);
    }

    @Override
    public List<PaymentRecordVO> getPaymentsByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        List<PaymentRecord> paymentList = paymentRecordMapper.selectByUserId(userId);
        return EntityConverter.INSTANCE.toPaymentRecordVOList(paymentList);
    }

    @Override
    public Page<PaymentRecordVO> pagePayments(PageQueryDTO pageQueryDTO) {
        Page<PaymentRecord> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PaymentRecord::getCreateTime);
        
        Page<PaymentRecord> paymentPage = paymentRecordMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<PaymentRecordVO> voPage = new Page<>(paymentPage.getCurrent(), paymentPage.getSize(), paymentPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toPaymentRecordVOList(paymentPage.getRecords()));
        
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePaymentStatus(String paymentNo, PaymentStatusEnum status) {
        if (StringUtils.isBlank(paymentNo)) {
            throw new BusinessException("支付流水号不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }

        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        paymentRecord.setPaymentStatus(status);
        paymentRecordMapper.updateById(paymentRecord);
        log.info("修改支付状态成功, paymentNo={}, status={}", paymentNo, status);
    }

    @Override
    public List<PaymentRecordVO> getPendingPayments() {
        List<PaymentRecord> paymentList = paymentRecordMapper.selectPendingPayments();
        return EntityConverter.INSTANCE.toPaymentRecordVOList(paymentList);
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void processPaymentAsync(String paymentNo) {
        log.info("异步处理支付开始, paymentNo={}", paymentNo);
        
        // 查询支付记录
        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        if (paymentRecord == null) {
            log.error("支付记录不存在, paymentNo={}", paymentNo);
            return;
        }
        
        // 创建支付上下文（状态机）
        PaymentContext context = new PaymentContext(paymentRecord);
        
        try {
            // 调用状态机处理支付
            context.getCurrentState().processPayment(context);
            
            // 更新数据库状态为支付中
            paymentRecord.setPaymentStatus(context.getCurrentStatus());
            paymentRecordMapper.updateById(paymentRecord);
            
            // 调用支付处理器（模拟第三方支付）
            PaymentProcessor.PaymentResult result = paymentProcessor.processPayment(
                paymentNo, 
                paymentRecord.getPaymentAmount().toString()
            );
            
            // 处理支付结果回调
            handlePaymentCallback(
                paymentNo, 
                result.isSuccess(), 
                result.getTransactionId(), 
                result.getFailReason()
            );
            
        } catch (Exception e) {
            log.error("异步支付处理异常, paymentNo={}", paymentNo, e);
            // 处理异常，标记为失败
            handlePaymentCallback(paymentNo, false, null, "系统异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentCallback(String paymentNo, boolean success, String transactionId, String failReason) {
        log.info("处理支付回调, paymentNo={}, success={}", paymentNo, success);
        
        // 查询支付记录
        PaymentRecord paymentRecord = paymentRecordMapper.selectByPaymentNo(paymentNo);
        if (paymentRecord == null) {
            log.error("支付记录不存在, paymentNo={}", paymentNo);
            return;
        }
        
        // 创建支付上下文
        PaymentContext context = new PaymentContext(paymentRecord);
        
        if (success) {
            // 调用状态机处理成功回调
            context.getCurrentState().onPaymentSuccess(context, transactionId);
            
            // 更新订单状态为已支付
            updateOrderStatus(paymentRecord.getOrderId(), OrderStatusEnum.PAID);
            
        } else {
            // 调用状态机处理失败回调
            context.getCurrentState().onPaymentFailed(context, failReason);
        }
        
        // 更新数据库
        paymentRecord.setPaymentStatus(context.getCurrentStatus());
        paymentRecordMapper.updateById(paymentRecord);
        
        log.info("支付回调处理完成, paymentNo={}, finalStatus={}", 
            paymentNo, context.getCurrentStatus().getDescription());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkPaymentTimeout() {
        log.info("开始检查支付超时");
        
        // 查询待支付和支付中的记录
        List<PaymentRecord> pendingPayments = paymentRecordMapper.selectPendingPayments();
        List<PaymentRecord> processingPayments = paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getPaymentStatus, PaymentStatusEnum.PROCESSING)
        );
        
        // 合并列表
        pendingPayments.addAll(processingPayments);
        
        int timeoutCount = 0;
        for (PaymentRecord paymentRecord : pendingPayments) {
            try {
                PaymentContext context = new PaymentContext(paymentRecord);
                
                // 检查超时
                context.getCurrentState().checkTimeout(context);
                
                // 如果状态发生变化，更新数据库
                if (!context.getCurrentStatus().equals(paymentRecord.getPaymentStatus())) {
                    paymentRecord.setPaymentStatus(context.getCurrentStatus());
                    paymentRecordMapper.updateById(paymentRecord);
                    timeoutCount++;
                    log.warn("支付超时处理, paymentNo={}, status={}", 
                        paymentRecord.getPaymentNo(), context.getCurrentStatus().getDescription());
                }
                
            } catch (Exception e) {
                log.error("检查支付超时异常, paymentNo={}", paymentRecord.getPaymentNo(), e);
            }
        }
        
        log.info("支付超时检查完成, 处理数量={}", timeoutCount);
    }

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 订单状态
     */
    private void updateOrderStatus(Long orderId, OrderStatusEnum status) {
        ServiceOrder serviceOrder = serviceOrderMapper.selectById(orderId);
        if (serviceOrder != null) {
            serviceOrder.setStatus(status);
            serviceOrderMapper.updateById(serviceOrder);
            log.info("更新订单状态, orderId={}, status={}", orderId, status.getDescription());
        }
    }

    /**
     * 生成支付流水号
     * 格式：PAY + yyyyMMddHHmmss + 6位随机数
     */
    private String generatePaymentNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%06d", (int)(Math.random() * 1000000));
        return "PAY" + timestamp + random;
    }
}
