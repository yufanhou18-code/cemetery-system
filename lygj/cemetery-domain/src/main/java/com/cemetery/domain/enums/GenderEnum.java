package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
public enum GenderEnum {
    
    FEMALE(0, "女"),
    MALE(1, "男");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    GenderEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
