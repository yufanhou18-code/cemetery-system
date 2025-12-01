package com.cemetery.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务反馈VO
 */
@Data
public class ServiceFeedbackVO {

    private Long id;

    private String feedbackNo;

    private Long orderId;

    private Long providerId;

    private String providerName;

    private Long serviceId;

    private String serviceName;

    private Long userId;

    private String userName;

    private Integer feedbackType;

    private BigDecimal rating;

    private BigDecimal serviceRating;

    private BigDecimal qualityRating;

    private BigDecimal speedRating;

    private String content;

    private String images;

    private Integer isAnonymous;

    private Integer isTop;

    private Integer likeCount;

    private String replyContent;

    private Date replyTime;

    private Long replyBy;

    private String replyByName;

    private Integer status;

    private Integer auditStatus;

    private Date auditTime;

    private String auditRemark;

    private Date handleTime;

    private String handleResult;

    private Date feedbackTime;

    private Date createTime;

    private Date updateTime;

    private String remark;

    // ========== 扩展字段：关联信息 ==========

    /**
     * 服务商Logo
     */
    private String providerLogo;

    /**
     * 服务项目封面图
     */
    private String serviceCoverImage;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 订单编号
     */
    private String orderNo;

    // ========== 扩展字段：状态信息 ==========

    /**
     * 是否已点赞（当前用户）
     */
    private Boolean isLiked;

    /**
     * 是否可以编辑（当前用户）
     */
    private Boolean canEdit;

    /**
     * 是否可以删除（当前用户）
     */
    private Boolean canDelete;

    /**
     * 审核人姓名
     */
    private String auditByName;

    /**
     * 处理人姓名
     */
    private String handleByName;
}
