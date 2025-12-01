package com.cemetery.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务商信息VO
 */
@Data
public class ServiceProviderVO {

    private Long id;

    private String providerNo;

    private Long userId;

    private String providerName;

    private Integer providerType;

    private String businessLicense;

    private String legalPerson;

    private String contactPerson;

    private String contactPhone;

    private String contactEmail;

    private String address;

    private String province;

    private String city;

    private String district;

    private String serviceScope;

    private String businessHours;

    private String description;

    private String logo;

    private String images;

    private String certificateImages;

    private BigDecimal rating;

    private Integer serviceCount;

    private Integer complaintCount;

    private Integer status;

    private Integer auditStatus;

    private Date auditTime;

    private String auditRemark;

    private Date joinDate;

    private Date contractStartDate;

    private Date contractEndDate;

    private Integer isRecommended;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    private String remark;

    // ========== 扩展字段：统计信息 ==========

    /**
     * 服务项目数量
     */
    private Integer serviceItemCount;

    /**
     * 反馈数量
     */
    private Integer feedbackCount;

    /**
     * 好评数量（评分>=4分）
     */
    private Integer goodReviewCount;

    /**
     * 好评率
     */
    private BigDecimal goodReviewRate;

    /**
     * 平均响应时间（小时）
     */
    private Integer avgResponseTime;

    /**
     * 本月服务次数
     */
    private Integer monthServiceCount;

    /**
     * 本月收入
     */
    private BigDecimal monthRevenue;
}
