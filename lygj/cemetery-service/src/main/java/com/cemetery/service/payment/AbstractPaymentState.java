package com.cemetery.service.payment;

import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 抽象支付状态（模板方法模式）
 * 类比：定义所有状态的通用行为框架
 */
@Slf4j
public abstract class AbstractPaymentState implements PaymentState {
    
    /**
     * 定义允许转换的目标状态列表
     * @return 可转换的状态列表
     */
    protected abstract List<PaymentStatusEnum> getAllowedTransitions();
    
    @Override
    public boolean canTransitionTo(PaymentStatusEnum targetStatus) {
        return getAllowedTransitions().contains(targetStatus);
    }
    
    @Override
    public void processPayment(PaymentContext context) {
        log.warn("当前状态 {} 不支持处理支付操作", getStatus().getDescription());
        throw new UnsupportedOperationException("当前状态不支持处理支付");
    }
    
    @Override
    public void onPaymentSuccess(PaymentContext context, String transactionId) {
        log.warn("当前状态 {} 不支持支付成功回调", getStatus().getDescription());
        throw new UnsupportedOperationException("当前状态不支持支付成功回调");
    }
    
    @Override
    public void onPaymentFailed(PaymentContext context, String failReason) {
        log.warn("当前状态 {} 不支持支付失败回调", getStatus().getDescription());
        throw new UnsupportedOperationException("当前状态不支持支付失败回调");
    }
    
    @Override
    public void applyRefund(PaymentContext context, String refundReason) {
        log.warn("当前状态 {} 不支持退款操作", getStatus().getDescription());
        throw new UnsupportedOperationException("当前状态不支持退款操作");
    }
    
    @Override
    public void checkTimeout(PaymentContext context) {
        // 默认实现：不做任何操作
    }
}
