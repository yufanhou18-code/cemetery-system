package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内容类型枚举
 */
@Getter
@AllArgsConstructor
public enum ContentTypeEnum {
    
    TEXT(1, "文本"),
    IMAGE(2, "图片"),
    VIDEO(3, "视频"),
    AUDIO(4, "音频"),
    DOCUMENT(5, "文档");

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static ContentTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ContentTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
