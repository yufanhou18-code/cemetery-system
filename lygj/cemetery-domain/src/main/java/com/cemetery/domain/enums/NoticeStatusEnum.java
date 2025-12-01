package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 公告状态枚举
 */
@Getter
public enum NoticeStatusEnum {
    
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    OFFLINE(2, "已下线");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    NoticeStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
