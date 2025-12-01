package com.cemetery.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务反馈DTO
 */
@Data
public class ServiceFeedbackDTO {

    private Long id;

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "服务商ID不能为空")
    private Long providerId;

    private Long serviceId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private Integer feedbackType;

    @NotNull(message = "评分不能为空")
    private BigDecimal rating;

    private BigDecimal serviceRating;

    private BigDecimal qualityRating;

    private BigDecimal speedRating;

    @NotBlank(message = "反馈内容不能为空")
    private String content;

    private String images;

    private Integer isAnonymous;

    private Integer isTop;

    private Integer likeCount;

    private String replyContent;

    private Date replyTime;

    private Long replyBy;

    private Integer status;

    private Integer auditStatus;

    private Date auditTime;

    private Long auditBy;

    private String auditRemark;

    private Date handleTime;

    private Long handleBy;

    private String handleResult;

    private Date feedbackTime;

    private String remark;
}
