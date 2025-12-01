package com.cemetery.service.payment;

import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * 已退款状态
 * 类比：钱已经退回账户，交易取消
 */
@Slf4j
public class RefundedPaymentState extends AbstractPaymentState {
    
    @Override
    public PaymentStatusEnum getStatus() {
        return PaymentStatusEnum.REFUNDED;
    }
    
    @Override
    protected List<PaymentStatusEnum> getAllowedTransitions() {
        // 退款状态是终态，不允许转换
        return Collections.emptyList();
    }
}
