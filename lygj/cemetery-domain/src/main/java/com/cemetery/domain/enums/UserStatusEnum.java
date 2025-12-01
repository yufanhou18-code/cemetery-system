package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatusEnum {
    
    DISABLED(0, "禁用"),
    NORMAL(1, "正常");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    UserStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
