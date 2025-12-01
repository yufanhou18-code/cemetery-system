package com.cemetery.service.payment;

import com.cemetery.domain.entity.PaymentRecord;
import com.cemetery.domain.enums.PaymentStatusEnum;

/**
 * 支付状态接口（状态模式）
 * 类比：就像订单的不同阶段（待付款、配送中、已完成），每个阶段能做的操作不同
 */
public interface PaymentState {
    
    /**
     * 获取当前状态
     * @return 支付状态枚举
     */
    PaymentStatusEnum getStatus();
    
    /**
     * 处理支付
     * @param context 支付上下文
     */
    void processPayment(PaymentContext context);
    
    /**
     * 支付成功回调
     * @param context 支付上下文
     * @param transactionId 第三方交易号
     */
    void onPaymentSuccess(PaymentContext context, String transactionId);
    
    /**
     * 支付失败回调
     * @param context 支付上下文
     * @param failReason 失败原因
     */
    void onPaymentFailed(PaymentContext context, String failReason);
    
    /**
     * 申请退款
     * @param context 支付上下文
     * @param refundReason 退款原因
     */
    void applyRefund(PaymentContext context, String refundReason);
    
    /**
     * 检查超时
     * @param context 支付上下文
     */
    void checkTimeout(PaymentContext context);
    
    /**
     * 验证状态转换是否合法
     * @param targetStatus 目标状态
     * @return 是否可以转换
     */
    boolean canTransitionTo(PaymentStatusEnum targetStatus);
}
