package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 公告类型枚举
 */
@Getter
public enum NoticeTypeEnum {
    
    SYSTEM(1, "系统公告"),
    ACTIVITY(2, "活动通知"),
    POLICY(3, "政策公告"),
    TIP(4, "温馨提示");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    NoticeTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
