package com.cemetery.domain.vo;

import com.cemetery.domain.enums.AccessPermissionEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数字纪念空间VO
 */
@Data
public class DigitalMemorialVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String spaceNo;
    private Long tombId;
    private Long deceasedId;
    private String spaceName;
    private String biography;
    private String lifeAchievements;
    private String familyWords;
    private AccessPermissionEnum accessPermission;
    private String backgroundTheme;
    private String backgroundColor;
    private String backgroundImage;
    private Integer musicEnabled;
    private String musicUrl;
    private String musicName;
    private Integer candleEnabled;
    private Integer flowerEnabled;
    private Integer incenseEnabled;
    private Integer messageEnabled;
    private Integer messageAudit;
    private Integer visitCount;
    private Integer candleCount;
    private Integer flowerCount;
    private Integer incenseCount;
    private Integer messageCount;
    private Integer isPublished;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    private String qrCode;
    private String shareUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String remark;

    // 关联信息
    private String tombNo;
    private String deceasedName;
    private String deceasedPhoto;
}
