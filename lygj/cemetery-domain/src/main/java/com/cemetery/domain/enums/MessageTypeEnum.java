package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 留言类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
    
    NORMAL(1, "普通留言"),
    MEMORIAL_DAY(2, "祭日留言"),
    ANNIVERSARY(3, "纪念日留言");

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static MessageTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MessageTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
