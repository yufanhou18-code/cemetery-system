package com.cemetery.service.payment;

import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * 支付失败状态
 * 类比：付款失败，需要重新下单或选择其他支付方式
 */
@Slf4j
public class FailedPaymentState extends AbstractPaymentState {
    
    @Override
    public PaymentStatusEnum getStatus() {
        return PaymentStatusEnum.FAILED;
    }
    
    @Override
    protected List<PaymentStatusEnum> getAllowedTransitions() {
        // 失败状态是终态，不允许转换
        return Collections.emptyList();
    }
}
