package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务商信息实体类
 * 
 * @author cemetery
 * @since 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_provider")
public class ServiceProvider extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 服务商编号（唯一标识）
     */
    @TableField("provider_no")
    private String providerNo;

    /**
     * 关联用户ID（关联sys_user，user_type=2）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 服务商名称
     */
    @TableField("provider_name")
    private String providerName;

    /**
     * 服务商类型（1-个人，2-企业）
     */
    @TableField("provider_type")
    private Integer providerType;

    /**
     * 营业执照号（企业类型必填）
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * 法人代表
     */
    @TableField("legal_person")
    private String legalPerson;

    /**
     * 联系人姓名
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 服务商地址
     */
    @TableField("address")
    private String address;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 区县
     */
    @TableField("district")
    private String district;

    /**
     * 服务范围（多个用逗号分隔）
     */
    @TableField("service_scope")
    private String serviceScope;

    /**
     * 营业时间
     */
    @TableField("business_hours")
    private String businessHours;

    /**
     * 服务商简介
     */
    @TableField("description")
    private String description;

    /**
     * 服务商Logo地址
     */
    @TableField("logo")
    private String logo;

    /**
     * 服务商图片（多个用逗号分隔）
     */
    @TableField("images")
    private String images;

    /**
     * 资质证书图片（多个用逗号分隔）
     */
    @TableField("certificate_images")
    private String certificateImages;

    /**
     * 综合评分（0-5分）
     */
    @TableField("rating")
    private BigDecimal rating;

    /**
     * 累计服务次数
     */
    @TableField("service_count")
    private Integer serviceCount;

    /**
     * 投诉次数
     */
    @TableField("complaint_count")
    private Integer complaintCount;

    /**
     * 服务商状态（0-禁用，1-正常，2-审核中，3-审核拒绝）
     */
    @TableField("status")
    private Integer status;

    /**
     * 审核状态（1-待审核，2-审核通过，3-审核拒绝）
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private Date auditTime;

    /**
     * 审核人ID
     */
    @TableField("audit_by")
    private Long auditBy;

    /**
     * 审核备注
     */
    @TableField("audit_remark")
    private String auditRemark;

    /**
     * 入驻日期
     */
    @TableField("join_date")
    private Date joinDate;

    /**
     * 合同开始日期
     */
    @TableField("contract_start_date")
    private Date contractStartDate;

    /**
     * 合同结束日期
     */
    @TableField("contract_end_date")
    private Date contractEndDate;

    /**
     * 是否推荐（0-否，1-是）
     */
    @TableField("is_recommended")
    private Integer isRecommended;

    /**
     * 排序序号（数字越小越靠前）
     */
    @TableField("sort_order")
    private Integer sortOrder;
}
