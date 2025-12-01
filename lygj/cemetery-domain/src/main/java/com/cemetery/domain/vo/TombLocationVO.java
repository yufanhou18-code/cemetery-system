package com.cemetery.domain.vo;

import com.cemetery.domain.enums.TombStatusEnum;
import com.cemetery.domain.enums.TombTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 墓位VO
 */
@Data
public class TombLocationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 墓位ID
     */
    private Long id;

    /**
     * 墓位编号
     */
    private String tombNo;

    /**
     * 墓位名称
     */
    private String tombName;

    /**
     * 墓位类型
     */
    private TombTypeEnum tombType;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 排号
     */
    private Integer rowNum;

    /**
     * 位号
     */
    private Integer colNum;

    /**
     * 墓位价格
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date purchaseDate;

    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryDate;

    /**
     * 所属家属ID
     */
    private Long ownerId;

    /**
     * 所属家属姓名
     */
    private String ownerName;

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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
}
