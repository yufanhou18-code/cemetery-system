package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.GenderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 逝者信息实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("deceased_info")
public class DeceasedInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 逝者姓名
     */
    @TableField("deceased_name")
    private String deceasedName;

    /**
     * 性别（0-女，1-男）
     */
    @TableField("gender")
    private GenderEnum gender;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    private Date birthDate;

    /**
     * 去世日期
     */
    @TableField("death_date")
    private Date deathDate;

    /**
     * 享年
     */
    @TableField("age")
    private Integer age;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 国籍
     */
    @TableField("nationality")
    private String nationality;

    /**
     * 籍贯
     */
    @TableField("native_place")
    private String nativePlace;

    /**
     * 生前职业
     */
    @TableField("occupation")
    private String occupation;

    /**
     * 墓位ID（关联tomb_location）
     */
    @TableField("tomb_id")
    private Long tombId;

    /**
     * 安葬日期
     */
    @TableField("burial_date")
    private Date burialDate;

    /**
     * 墓志铭
     */
    @TableField("epitaph")
    private String epitaph;

    /**
     * 遗像地址
     */
    @TableField("photo")
    private String photo;

    /**
     * 生平简介
     */
    @TableField("life_story")
    private String lifeStory;
}
