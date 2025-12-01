package com.cemetery.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务项目DTO
 */
@Data
public class ProviderServiceDTO {

    private Long id;

    @NotNull(message = "服务商ID不能为空")
    private Long providerId;

    @NotBlank(message = "服务项目名称不能为空")
    private String serviceName;

    @NotNull(message = "服务分类不能为空")
    private Integer serviceCategory;

    private Integer serviceType;

    @NotNull(message = "服务价格不能为空")
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

    private String remark;
}
