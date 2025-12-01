package com.cemetery.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务商信息DTO
 */
@Data
public class ServiceProviderDTO {

    private Long id;

    @NotBlank(message = "服务商名称不能为空")
    private String providerName;

    @NotNull(message = "服务商类型不能为空")
    private Integer providerType;

    private String businessLicense;

    private String legalPerson;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactPerson;

    @NotBlank(message = "联系电话不能为空")
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

    private Long auditBy;

    private String auditRemark;

    private Date joinDate;

    private Date contractStartDate;

    private Date contractEndDate;

    private Integer isRecommended;

    private Integer sortOrder;

    private String remark;
}
