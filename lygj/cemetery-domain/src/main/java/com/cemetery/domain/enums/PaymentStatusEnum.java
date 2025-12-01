package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 支付状态枚举
 */
@Getter
public enum PaymentStatusEnum {
    
    PENDING(1, "待支付"),
    PROCESSING(2, "支付中"),
    SUCCESS(3, "支付成功"),
    FAILED(4, "支付失败"),
    REFUNDED(5, "已退款");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    PaymentStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
