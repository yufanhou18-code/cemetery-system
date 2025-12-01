package com.cemetery.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 服务反馈查询DTO
 */
@Data
public class ServiceFeedbackQueryDTO extends PageQueryDTO {

    private Long providerId;

    private Long serviceId;

    private Long userId;

    private Integer feedbackType;

    private Integer status;

    private Integer auditStatus;

    private Integer isTop;

    private String keyword;

    private BigDecimal minRating;

    private BigDecimal maxRating;
}
