package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务项目实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("provider_service")
public class ProviderService extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("provider_id")
    private Long providerId;

    @TableField("service_no")
    private String serviceNo;

    @TableField("service_name")
    private String serviceName;

    @TableField("service_category")
    private Integer serviceCategory;

    @TableField("service_type")
    private Integer serviceType;

    @TableField("price")
    private BigDecimal price;

    @TableField("original_price")
    private BigDecimal originalPrice;

    @TableField("price_unit")
    private String priceUnit;

    @TableField("description")
    private String description;

    @TableField("service_content")
    private String serviceContent;

    @TableField("service_process")
    private String serviceProcess;

    @TableField("duration")
    private Integer duration;

    @TableField("cover_image")
    private String coverImage;

    @TableField("images")
    private String images;

    @TableField("video_url")
    private String videoUrl;

    @TableField("stock")
    private Integer stock;

    @TableField("sales_count")
    private Integer salesCount;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("rating")
    private BigDecimal rating;

    @TableField("review_count")
    private Integer reviewCount;

    @TableField("status")
    private Integer status;

    @TableField("is_hot")
    private Integer isHot;

    @TableField("is_new")
    private Integer isNew;

    @TableField("is_recommended")
    private Integer isRecommended;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("publish_time")
    private Date publishTime;

    @TableField("offline_time")
    private Date offlineTime;
}
