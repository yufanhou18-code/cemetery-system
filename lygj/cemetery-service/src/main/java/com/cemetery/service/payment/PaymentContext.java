package com.cemetery.service.payment;

import com.cemetery.domain.entity.PaymentRecord;
import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.Data;

/**
 * 支付上下文（状态机上下文）
 * 类比：就像快递单，记录了当前状态和所有相关信息
 */
@Data
public class PaymentContext {
    
    /**
     * 支付记录
     */
    private PaymentRecord paymentRecord;
    
    /**
     * 当前状态
     */
    private PaymentState currentState;
    
    /**
     * 创建时间戳
     */
    private Long createTimestamp;
    
    /**
     * 支付超时时间（毫秒），默认30分钟
     */
    private Long paymentTimeout = 30 * 60 * 1000L;
    
    public PaymentContext(PaymentRecord paymentRecord) {
        this.paymentRecord = paymentRecord;
        this.createTimestamp = System.currentTimeMillis();
        // 根据当前支付状态初始化状态对象
        this.currentState = PaymentStateFactory.getState(paymentRecord.getPaymentStatus());
    }
    
    /**
     * 切换状态
     * @param newState 新状态
     */
    public void setState(PaymentState newState) {
        if (currentState.canTransitionTo(newState.getStatus())) {
            this.currentState = newState;
            this.paymentRecord.setPaymentStatus(newState.getStatus());
        } else {
            throw new IllegalStateException(
                String.format("不允许从状态 %s 转换到 %s", 
                    currentState.getStatus().getDescription(), 
                    newState.getStatus().getDescription())
            );
        }
    }
    
    /**
     * 检查是否超时
     * @return 是否超时
     */
    public boolean isTimeout() {
        return System.currentTimeMillis() - createTimestamp > paymentTimeout;
    }
    
    /**
     * 获取当前状态枚举
     * @return 支付状态
     */
    public PaymentStatusEnum getCurrentStatus() {
        return currentState.getStatus();
    }
}
