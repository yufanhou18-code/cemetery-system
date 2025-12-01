package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 支付方式枚举
 */
@Getter
public enum PaymentMethodEnum {
    
    WECHAT(1, "微信支付"),
    ALIPAY(2, "支付宝"),
    CASH(3, "现金"),
    CARD(4, "刷卡"),
    TRANSFER(5, "转账");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    PaymentMethodEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
