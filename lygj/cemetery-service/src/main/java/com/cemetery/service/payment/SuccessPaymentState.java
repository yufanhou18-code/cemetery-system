package com.cemetery.service.payment;

import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 支付成功状态
 * 类比：钱已经到账，交易完成
 */
@Slf4j
public class SuccessPaymentState extends AbstractPaymentState {
    
    @Override
    public PaymentStatusEnum getStatus() {
        return PaymentStatusEnum.SUCCESS;
    }
    
    @Override
    protected List<PaymentStatusEnum> getAllowedTransitions() {
        // 支付成功 -> 已退款
        return Arrays.asList(PaymentStatusEnum.REFUNDED);
    }
    
    @Override
    public void applyRefund(PaymentContext context, String refundReason) {
        log.info("申请退款，支付流水号: {}, 退款原因: {}", 
            context.getPaymentRecord().getPaymentNo(), refundReason);
        
        // 设置退款信息
        context.getPaymentRecord().setRefundReason(refundReason);
        context.getPaymentRecord().setRefundAmount(context.getPaymentRecord().getPaymentAmount());
        context.getPaymentRecord().setRefundTime(new Date());
        
        // 转换到退款状态
        context.setState(PaymentStateFactory.getState(PaymentStatusEnum.REFUNDED));
    }
}
