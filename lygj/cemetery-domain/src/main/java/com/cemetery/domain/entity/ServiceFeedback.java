package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务反馈实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_feedback")
public class ServiceFeedback extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("feedback_no")
    private String feedbackNo;

    @TableField("order_id")
    private Long orderId;

    @TableField("provider_id")
    private Long providerId;

    @TableField("service_id")
    private Long serviceId;

    @TableField("user_id")
    private Long userId;

    @TableField("feedback_type")
    private Integer feedbackType;

    @TableField("rating")
    private BigDecimal rating;

    @TableField("service_rating")
    private BigDecimal serviceRating;

    @TableField("quality_rating")
    private BigDecimal qualityRating;

    @TableField("speed_rating")
    private BigDecimal speedRating;

    @TableField("content")
    private String content;

    @TableField("images")
    private String images;

    @TableField("is_anonymous")
    private Integer isAnonymous;

    @TableField("is_top")
    private Integer isTop;

    @TableField("like_count")
    private Integer likeCount;

    @TableField("reply_content")
    private String replyContent;

    @TableField("reply_time")
    private Date replyTime;

    @TableField("reply_by")
    private Long replyBy;

    @TableField("status")
    private Integer status;

    @TableField("audit_status")
    private Integer auditStatus;

    @TableField("audit_time")
    private Date auditTime;

    @TableField("audit_by")
    private Long auditBy;

    @TableField("audit_remark")
    private String auditRemark;

    @TableField("handle_time")
    private Date handleTime;

    @TableField("handle_by")
    private Long handleBy;

    @TableField("handle_result")
    private String handleResult;

    @TableField("feedback_time")
    private Date feedbackTime;
}
