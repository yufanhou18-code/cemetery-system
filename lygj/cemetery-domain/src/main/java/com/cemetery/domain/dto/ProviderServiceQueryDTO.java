package com.cemetery.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 服务项目查询DTO
 */
@Data
public class ProviderServiceQueryDTO extends PageQueryDTO {

    private Long providerId;

    private String keyword;

    private Integer serviceCategory;

    private Integer serviceType;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer status;

    private Integer isHot;

    private Integer isNew;

    private Integer isRecommended;
}
