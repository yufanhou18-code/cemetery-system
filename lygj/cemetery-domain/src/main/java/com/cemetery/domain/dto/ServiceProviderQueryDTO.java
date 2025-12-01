package com.cemetery.domain.dto;

import lombok.Data;

/**
 * 服务商查询DTO
 */
@Data
public class ServiceProviderQueryDTO extends PageQueryDTO {

    private String keyword;

    private Integer status;

    private Integer auditStatus;

    private Integer providerType;

    private String province;

    private String city;

    private Integer isRecommended;

    private String serviceScope;
}
