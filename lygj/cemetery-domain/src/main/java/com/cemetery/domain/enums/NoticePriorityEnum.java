package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 公告优先级枚举
 */
@Getter
public enum NoticePriorityEnum {
    
    NORMAL(0, "普通"),
    IMPORTANT(1, "重要"),
    URGENT(2, "紧急");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    NoticePriorityEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
