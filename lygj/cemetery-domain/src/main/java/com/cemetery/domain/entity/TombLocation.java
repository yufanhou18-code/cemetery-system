package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.TombStatusEnum;
import com.cemetery.domain.enums.TombTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 墓位实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tomb_location")
public class TombLocation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 墓位编号（唯一标识）
     */
    @TableField("tomb_no")
    private String tombNo;

    /**
     * 墓位名称
     */
    @TableField("tomb_name")
    private String tombName;

    /**
     * 墓位类型（1-单穴，2-双穴，3-家族墓，4-壁葬，5-树葬）
     */
    @TableField("tomb_type")
    private TombTypeEnum tombType;

    /**
     * 区域编码（如：A区、B区）
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 区域名称
     */
    @TableField("area_name")
    private String areaName;

    /**
     * 排号
     */
    @TableField("row_num")
    private Integer rowNum;

    /**
     * 位号
     */
    @TableField("col_num")
    private Integer colNum;

    /**
     * 墓位价格（元）
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 年度管理费（元）
     */
    @TableField("management_fee")
    private BigDecimal managementFee;

    /**
     * 占地面积（平方米）
     */
    @TableField("area_size")
    private BigDecimal areaSize;

    /**
     * 朝向（如：坐北朝南）
     */
    @TableField("orientation")
    private String orientation;

    /**
     * 墓位状态（1-空闲，2-已售，3-预定，4-维护中，5-已过期）
     */
    @TableField("status")
    private TombStatusEnum status;

    /**
     * 购买日期
     */
    @TableField("purchase_date")
    private Date purchaseDate;

    /**
     * 到期日期
     */
    @TableField("expiry_date")
    private Date expiryDate;

    /**
     * 所属家属ID（关联sys_user）
     */
    @TableField("owner_id")
    private Long ownerId;

    /**
     * 经度（用于地图定位）
     */
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 纬度（用于地图定位）
     */
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 墓位照片（多个用逗号分隔）
     */
    @TableField("images")
    private String images;
}
