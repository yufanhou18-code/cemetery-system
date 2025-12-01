package com.cemetery.domain.vo;

import com.cemetery.domain.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 逝者信息VO
 */
@Data
public class DeceasedInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 逝者ID
     */
    private Long id;

    /**
     * 逝者姓名
     */
    private String deceasedName;

    /**
     * 性别
     */
    private GenderEnum gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthDate;

    /**
     * 去世日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deathDate;

    /**
     * 享年
     */
    private Integer age;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 生前职业
     */
    private String occupation;

    /**
     * 墓位ID
     */
    private Long tombId;

    /**
     * 墓位编号
     */
    private String tombNo;

    /**
     * 墓位名称
     */
    private String tombName;

    /**
     * 安葬日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date burialDate;

    /**
     * 墓志铭
     */
    private String epitaph;

    /**
     * 遗像地址
     */
    private String photo;

    /**
     * 生平简介
     */
    private String lifeStory;

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
