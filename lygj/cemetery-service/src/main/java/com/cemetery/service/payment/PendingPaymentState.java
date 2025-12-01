package com.cemetery.service.payment;

import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * 待支付状态
 * 类比：商品放入购物车，等待付款
 */
@Slf4j
public class PendingPaymentState extends AbstractPaymentState {
    
    @Override
    public PaymentStatusEnum getStatus() {
        return PaymentStatusEnum.PENDING;
    }
    
    @Override
    protected List<PaymentStatusEnum> getAllowedTransitions() {
        // 待支付 -> 支付中、支付失败
        return Arrays.asList(PaymentStatusEnum.PROCESSING, PaymentStatusEnum.FAILED);
    }
    
    @Override
    public void processPayment(PaymentContext context) {
        log.info("开始处理支付，支付流水号: {}", context.getPaymentRecord().getPaymentNo());
        // 转换到支付中状态
        context.setState(PaymentStateFactory.getState(PaymentStatusEnum.PROCESSING));
    }
    
    @Override
    public void checkTimeout(PaymentContext context) {
        if (context.isTimeout()) {
            log.warn("支付超时，支付流水号: {}", context.getPaymentRecord().getPaymentNo());
            // 超时后转为失败状态
            context.setState(PaymentStateFactory.getState(PaymentStatusEnum.FAILED));
        }
    }
}
