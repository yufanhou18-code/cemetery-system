package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 墓位状态枚举
 */
@Getter
public enum TombStatusEnum {
    
    AVAILABLE(1, "空闲"),
    SOLD(2, "已售"),
    RESERVED(3, "预定"),
    MAINTENANCE(4, "维护中"),
    EXPIRED(5, "已过期");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    TombStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
