package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.PaymentRecordDTO;
import com.cemetery.domain.enums.PaymentStatusEnum;
import com.cemetery.domain.vo.PaymentRecordVO;

import java.util.List;

/**
 * 支付服务接口
 */
public interface PaymentService {

    /**
     * 创建支付记录
     * @param paymentRecordDTO 支付记录DTO
     * @return 支付记录ID
     */
    Long createPaymentRecord(PaymentRecordDTO paymentRecordDTO);

    /**
     * 处理支付成功
     * @param paymentNo 支付流水号
     * @param transactionId 第三方交易号
     */
    void handlePaymentSuccess(String paymentNo, String transactionId);

    /**
     * 处理支付失败
     * @param paymentNo 支付流水号
     * @param failReason 失败原因
     */
    void handlePaymentFailed(String paymentNo, String failReason);

    /**
     * 申请退款
     * @param paymentNo 支付流水号
     * @param refundReason 退款原因
     */
    void applyRefund(String paymentNo, String refundReason);

    /**
     * 处理退款成功
     * @param paymentNo 支付流水号
     */
    void handleRefundSuccess(String paymentNo);

    /**
     * 根据支付流水号查询
     * @param paymentNo 支付流水号
     * @return 支付记录VO
     */
    PaymentRecordVO getPaymentByPaymentNo(String paymentNo);

    /**
     * 根据订单ID查询支付记录
     * @param orderId 订单ID
     * @return 支付记录列表
     */
    List<PaymentRecordVO> getPaymentsByOrderId(Long orderId);

    /**
     * 根据订单编号查询支付记录
     * @param orderNo 订单编号
     * @return 支付记录列表
     */
    List<PaymentRecordVO> getPaymentsByOrderNo(String orderNo);

    /**
     * 根据用户ID查询支付记录
     * @param userId 用户ID
     * @return 支付记录列表
     */
    List<PaymentRecordVO> getPaymentsByUserId(Long userId);

    /**
     * 分页查询支付记录
     * @param pageQueryDTO 分页查询参数
     * @return 支付记录分页列表
     */
    Page<PaymentRecordVO> pagePayments(PageQueryDTO pageQueryDTO);

    /**
     * 修改支付状态
     * @param paymentNo 支付流水号
     * @param status 状态
     */
    void changePaymentStatus(String paymentNo, PaymentStatusEnum status);

    /**
     * 查询待支付记录
     * @return 待支付记录列表
     */
    List<PaymentRecordVO> getPendingPayments();

    /**
     * 异步处理支付（使用状态机）
     * @param paymentNo 支付流水号
     */
    void processPaymentAsync(String paymentNo);

    /**
     * 处理支付回调
     * @param paymentNo 支付流水号
     * @param success 是否成功
     * @param transactionId 第三方交易号
     * @param failReason 失败原因
     */
    void handlePaymentCallback(String paymentNo, boolean success, String transactionId, String failReason);

    /**
     * 检查支付超时
     */
    void checkPaymentTimeout();
}
