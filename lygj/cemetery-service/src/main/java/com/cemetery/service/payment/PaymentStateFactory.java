package com.cemetery.service.payment;

import com.cemetery.domain.enums.PaymentStatusEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付状态工厂（工厂模式 + 单例模式）
 * 类比：像一个状态对象的仓库，每种状态只创建一次，需要时直接取用
 */
public class PaymentStateFactory {
    
    /**
     * 状态缓存池（单例）
     */
    private static final Map<PaymentStatusEnum, PaymentState> STATE_POOL = new HashMap<>();
    
    static {
        // 初始化所有状态对象
        STATE_POOL.put(PaymentStatusEnum.PENDING, new PendingPaymentState());
        STATE_POOL.put(PaymentStatusEnum.PROCESSING, new ProcessingPaymentState());
        STATE_POOL.put(PaymentStatusEnum.SUCCESS, new SuccessPaymentState());
        STATE_POOL.put(PaymentStatusEnum.FAILED, new FailedPaymentState());
        STATE_POOL.put(PaymentStatusEnum.REFUNDED, new RefundedPaymentState());
    }
    
    /**
     * 获取状态对象
     * @param status 支付状态枚举
     * @return 状态对象
     */
    public static PaymentState getState(PaymentStatusEnum status) {
        PaymentState state = STATE_POOL.get(status);
        if (state == null) {
            throw new IllegalArgumentException("不支持的支付状态: " + status);
        }
        return state;
    }
}
