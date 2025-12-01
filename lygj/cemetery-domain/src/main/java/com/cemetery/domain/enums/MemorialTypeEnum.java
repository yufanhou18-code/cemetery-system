package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 祭扫类型枚举
 */
@Getter
public enum MemorialTypeEnum {
    
    ONSITE(1, "现场祭扫"),
    ONLINE(2, "在线祭扫"),
    PROXY(3, "代客祭扫");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    MemorialTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
