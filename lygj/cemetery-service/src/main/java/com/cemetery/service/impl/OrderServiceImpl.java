package com.cemetery.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ServiceOrderDTO;
import com.cemetery.domain.entity.ServiceOrder;
import com.cemetery.domain.enums.OrderStatusEnum;
import com.cemetery.domain.mapper.ServiceOrderMapper;
import com.cemetery.domain.vo.ServiceOrderVO;
import com.cemetery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ServiceOrderMapper serviceOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(ServiceOrderDTO serviceOrderDTO) {
        // 生成订单编号
        String orderNo = generateOrderNo();
        
        // 转换并保存
        ServiceOrder serviceOrder = EntityConverter.INSTANCE.toServiceOrder(serviceOrderDTO);
        serviceOrder.setOrderNo(orderNo);
        serviceOrder.setStatus(OrderStatusEnum.PENDING_PAYMENT);
        
        serviceOrderMapper.insert(serviceOrder);
        log.info("创建订单成功, orderId={}, orderNo={}", serviceOrder.getId(), orderNo);
        return serviceOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(ServiceOrderDTO serviceOrderDTO) {
        if (serviceOrderDTO.getId() == null) {
            throw new BusinessException("订单ID不能为空");
        }

        ServiceOrder existOrder = serviceOrderMapper.selectById(serviceOrderDTO.getId());
        if (existOrder == null) {
            throw new BusinessException("订单不存在");
        }

        // 已完成或已取消的订单不能修改
        if (OrderStatusEnum.COMPLETED.equals(existOrder.getStatus()) 
                || OrderStatusEnum.CANCELLED.equals(existOrder.getStatus())) {
            throw new BusinessException("订单已完成或已取消，不能修改");
        }

        // 转换并更新
        ServiceOrder serviceOrder = EntityConverter.INSTANCE.toServiceOrder(serviceOrderDTO);
        serviceOrder.setOrderNo(existOrder.getOrderNo()); // 不更新订单编号
        
        serviceOrderMapper.updateById(serviceOrder);
        log.info("更新订单成功, orderId={}", serviceOrder.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, String cancelReason) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }

        ServiceOrder serviceOrder = serviceOrderMapper.selectById(orderId);
        if (serviceOrder == null) {
            throw new BusinessException("订单不存在");
        }

        // 只有待支付和已支付的订单可以取消
        if (!OrderStatusEnum.PENDING_PAYMENT.equals(serviceOrder.getStatus()) 
                && !OrderStatusEnum.PAID.equals(serviceOrder.getStatus())) {
            throw new BusinessException("当前状态的订单不能取消");
        }

        serviceOrder.setStatus(OrderStatusEnum.CANCELLED);
        serviceOrder.setCancelReason(cancelReason);
        serviceOrder.setCancelTime(new Date());
        
        serviceOrderMapper.updateById(serviceOrder);
        log.info("取消订单成功, orderId={}, reason={}", orderId, cancelReason);
    }

    @Override
    public ServiceOrderVO getOrderById(Long orderId) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }

        ServiceOrder serviceOrder = serviceOrderMapper.selectById(orderId);
        if (serviceOrder == null) {
            throw new BusinessException("订单不存在");
        }

        return EntityConverter.INSTANCE.toServiceOrderVO(serviceOrder);
    }

    @Override
    public ServiceOrderVO getOrderByOrderNo(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException("订单编号不能为空");
        }

        ServiceOrder serviceOrder = serviceOrderMapper.selectByOrderNo(orderNo);
        return serviceOrder != null ? EntityConverter.INSTANCE.toServiceOrderVO(serviceOrder) : null;
    }

    @Override
    public List<ServiceOrderVO> getOrdersByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        List<ServiceOrder> orderList = serviceOrderMapper.selectByUserId(userId);
        return EntityConverter.INSTANCE.toServiceOrderVOList(orderList);
    }

    @Override
    public List<ServiceOrderVO> getOrdersByTombId(Long tombId) {
        if (tombId == null) {
            throw new BusinessException("墓位ID不能为空");
        }

        List<ServiceOrder> orderList = serviceOrderMapper.selectByTombId(tombId);
        return EntityConverter.INSTANCE.toServiceOrderVOList(orderList);
    }

    @Override
    public Page<ServiceOrderVO> pageOrders(PageQueryDTO pageQueryDTO) {
        Page<ServiceOrder> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        LambdaQueryWrapper<ServiceOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ServiceOrder::getCreateTime);
        
        Page<ServiceOrder> orderPage = serviceOrderMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<ServiceOrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toServiceOrderVOList(orderPage.getRecords()));
        
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeOrderStatus(Long orderId, OrderStatusEnum status) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }

        ServiceOrder serviceOrder = serviceOrderMapper.selectById(orderId);
        if (serviceOrder == null) {
            throw new BusinessException("订单不存在");
        }

        serviceOrder.setStatus(status);
        serviceOrderMapper.updateById(serviceOrder);
        log.info("修改订单状态成功, orderId={}, status={}", orderId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignStaff(Long orderId, Long staffId) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }
        if (staffId == null) {
            throw new BusinessException("服务人员ID不能为空");
        }

        ServiceOrder serviceOrder = serviceOrderMapper.selectById(orderId);
        if (serviceOrder == null) {
            throw new BusinessException("订单不存在");
        }

        serviceOrder.setServiceStaffId(staffId);
        serviceOrderMapper.updateById(serviceOrder);
        log.info("分配服务人员成功, orderId={}, staffId={}", orderId, staffId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startService(Long orderId) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }

        ServiceOrder serviceOrder = serviceOrderMapper.selectById(orderId);
        if (serviceOrder == null) {
            throw new BusinessException("订单不存在");
        }

        if (!OrderStatusEnum.PAID.equals(serviceOrder.getStatus())) {
            throw new BusinessException("只有已支付的订单可以开始服务");
        }

        serviceOrder.setStatus(OrderStatusEnum.IN_SERVICE);
        serviceOrder.setServiceStartTime(new Date());
        serviceOrderMapper.updateById(serviceOrder);
        log.info("开始服务成功, orderId={}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeService(Long orderId) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }

        ServiceOrder serviceOrder = serviceOrderMapper.selectById(orderId);
        if (serviceOrder == null) {
            throw new BusinessException("订单不存在");
        }

        if (!OrderStatusEnum.IN_SERVICE.equals(serviceOrder.getStatus())) {
            throw new BusinessException("只有服务中的订单可以完成");
        }

        serviceOrder.setStatus(OrderStatusEnum.COMPLETED);
        serviceOrder.setServiceEndTime(new Date());
        serviceOrderMapper.updateById(serviceOrder);
        log.info("完成服务成功, orderId={}", orderId);
    }

    @Override
    public List<ServiceOrderVO> getPendingPaymentOrders() {
        List<ServiceOrder> orderList = serviceOrderMapper.selectPendingPaymentOrders();
        return EntityConverter.INSTANCE.toServiceOrderVOList(orderList);
    }

    @Override
    public List<ServiceOrderVO> getOrdersByAppointmentDate(Date appointmentDate) {
        if (appointmentDate == null) {
            throw new BusinessException("预约日期不能为空");
        }

        List<ServiceOrder> orderList = serviceOrderMapper.selectByAppointmentDate(appointmentDate);
        return EntityConverter.INSTANCE.toServiceOrderVOList(orderList);
    }

    /**
     * 生成订单编号
     * 格式：SO + yyyyMMddHHmmss + 6位随机数
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%06d", (int)(Math.random() * 1000000));
        return "SO" + timestamp + random;
    }
}
