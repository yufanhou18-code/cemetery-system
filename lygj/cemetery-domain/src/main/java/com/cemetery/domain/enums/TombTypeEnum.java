package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 墓位类型枚举
 */
@Getter
public enum TombTypeEnum {
    
    SINGLE(1, "单穴"),
    DOUBLE(2, "双穴"),
    FAMILY(3, "家族墓"),
    WALL(4, "壁葬"),
    TREE(5, "树葬");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    TombTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
