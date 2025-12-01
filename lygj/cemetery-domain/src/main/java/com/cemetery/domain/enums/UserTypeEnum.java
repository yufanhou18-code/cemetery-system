package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户类型枚举
 */
@Getter
public enum UserTypeEnum {
    
    ADMIN(1, "管理员"),
    SERVICE(2, "服务商"),
    FAMILY(3, "家属");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    UserTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
