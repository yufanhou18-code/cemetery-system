package com.cemetery.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务项目VO
 */
@Data
public class ProviderServiceVO {

    private Long id;

    private Long providerId;

    private String providerName;

    private String serviceNo;

    private String serviceName;

    private Integer serviceCategory;

    private Integer serviceType;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String priceUnit;

    private String description;

    private String serviceContent;

    private String serviceProcess;

    private Integer duration;

    private String coverImage;

    private String images;

    private String videoUrl;

    private Integer stock;

    private Integer salesCount;

    private Integer viewCount;

    private BigDecimal rating;

    private Integer reviewCount;

    private Integer status;

    private Integer isHot;

    private Integer isNew;

    private Integer isRecommended;

    private Integer sortOrder;

    private Date publishTime;

    private Date offlineTime;

    private Date createTime;

    private Date updateTime;

    private String remark;

    // ========== 扩展字段：服务商信息 ==========

    /**
     * 服务商评分
     */
    private BigDecimal providerRating;

    /**
     * 服务商联系电话
     */
    private String providerPhone;

    // ========== 扩展字段：统计信息 ==========

    /**
     * 好评数量（评分>=4分）
     */
    private Integer goodReviewCount;

    /**
     * 好评率
     */
    private BigDecimal goodReviewRate;

    /**
     * 收藏数
     */
    private Integer favoriteCount;

    /**
     * 分享数
     */
    private Integer shareCount;

    /**
     * 本月销量
     */
    private Integer monthSales;

    /**
     * 是否已收藏（当前用户）
     */
    private Boolean isFavorited;
}
