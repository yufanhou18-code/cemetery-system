package com.cemetery.domain.dto;

import com.cemetery.domain.enums.GenderEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 逝者信息DTO
 */
@Data
public class DeceasedInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 逝者ID
     */
    private Long id;

    /**
     * 逝者姓名
     */
    @NotBlank(message = "逝者姓名不能为空")
    private String deceasedName;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    private GenderEnum gender;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 去世日期
     */
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
    @NotNull(message = "墓位ID不能为空")
    private Long tombId;

    /**
     * 安葬日期
     */
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
     * 备注
     */
    private String remark;
}
