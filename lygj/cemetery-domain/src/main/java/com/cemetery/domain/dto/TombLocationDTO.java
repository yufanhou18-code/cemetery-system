package com.cemetery.domain.dto;

import com.cemetery.domain.enums.TombStatusEnum;
import com.cemetery.domain.enums.TombTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 墓位DTO
 */
@Data
public class TombLocationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 墓位ID
     */
    private Long id;

    /**
     * 墓位编号
     */
    @NotBlank(message = "墓位编号不能为空")
    private String tombNo;

    /**
     * 墓位名称
     */
    private String tombName;

    /**
     * 墓位类型
     */
    @NotNull(message = "墓位类型不能为空")
    private TombTypeEnum tombType;

    /**
     * 区域编码
     */
    @NotBlank(message = "区域编码不能为空")
    private String areaCode;

    /**
     * 区域名称
     */
    @NotBlank(message = "区域名称不能为空")
    private String areaName;

    /**
     * 排号
     */
    @NotNull(message = "排号不能为空")
    private Integer rowNum;

    /**
     * 位号
     */
    @NotNull(message = "位号不能为空")
    private Integer colNum;

    /**
     * 墓位价格
     */
    @NotNull(message = "墓位价格不能为空")
    private BigDecimal price;

    /**
     * 年度管理费
     */
    private BigDecimal managementFee;

    /**
     * 占地面积
     */
    private BigDecimal areaSize;

    /**
     * 朝向
     */
    private String orientation;

    /**
     * 墓位状态
     */
    private TombStatusEnum status;

    /**
     * 购买日期
     */
    private Date purchaseDate;

    /**
     * 到期日期
     */
    private Date expiryDate;

    /**
     * 所属家属ID
     */
    private Long ownerId;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 墓位照片
     */
    private String images;

    /**
     * 备注
     */
    private String remark;
}
