package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 访问权限枚举
 */
@Getter
@AllArgsConstructor
public enum AccessPermissionEnum {
    
    PUBLIC(1, "公开"),
    FAMILY_ONLY(2, "家属可见"),
    PASSWORD_PROTECTED(3, "密码访问"),
    PRIVATE(4, "完全私密");

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static AccessPermissionEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AccessPermissionEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
