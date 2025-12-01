package com.cemetery.service.payment;

import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 支付处理中状态
 * 类比：钱已经从账户扣除，正在转账过程中
 */
@Slf4j
public class ProcessingPaymentState extends AbstractPaymentState {
    
    @Override
    public PaymentStatusEnum getStatus() {
        return PaymentStatusEnum.PROCESSING;
    }
    
    @Override
    protected List<PaymentStatusEnum> getAllowedTransitions() {
        // 支付中 -> 支付成功、支付失败
        return Arrays.asList(PaymentStatusEnum.SUCCESS, PaymentStatusEnum.FAILED);
    }
    
    @Override
    public void onPaymentSuccess(PaymentContext context, String transactionId) {
        log.info("支付成功回调，支付流水号: {}, 第三方交易号: {}", 
            context.getPaymentRecord().getPaymentNo(), transactionId);
        
        // 更新支付记录
        context.getPaymentRecord().setTransactionId(transactionId);
        context.getPaymentRecord().setPaymentTime(new Date());
        context.getPaymentRecord().setNotifyTime(new Date());
        
        // 转换到成功状态
        context.setState(PaymentStateFactory.getState(PaymentStatusEnum.SUCCESS));
    }
    
    @Override
    public void onPaymentFailed(PaymentContext context, String failReason) {
        log.warn("支付失败回调，支付流水号: {}, 失败原因: {}", 
            context.getPaymentRecord().getPaymentNo(), failReason);
        
        // 记录失败原因到回调数据
        context.getPaymentRecord().setNotifyData(failReason);
        context.getPaymentRecord().setNotifyTime(new Date());
        
        // 转换到失败状态
        context.setState(PaymentStateFactory.getState(PaymentStatusEnum.FAILED));
    }
    
    @Override
    public void checkTimeout(PaymentContext context) {
        if (context.isTimeout()) {
            log.warn("支付处理超时，支付流水号: {}", context.getPaymentRecord().getPaymentNo());
            // 超时后转为失败状态
            onPaymentFailed(context, "支付处理超时");
        }
    }
}
