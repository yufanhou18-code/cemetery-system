package com.cemetery.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 服务类型枚举
 */
@Getter
public enum ServiceTypeEnum {
    
    TOMB_PURCHASE(1, "墓位购买"),
    MEMORIAL_APPOINTMENT(2, "祭扫预约"),
    PROXY_MEMORIAL(3, "代客祭扫"),
    FLOWER_PURCHASE(4, "鲜花购买"),
    FEE_RENEWAL(5, "管理费续费"),
    TOMBSTONE_MAINTENANCE(6, "墓碑维护"),
    OTHER_SERVICE(7, "其他服务");
    
    @EnumValue
    private final Integer code;
    
    @JsonValue
    private final String description;
    
    ServiceTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
