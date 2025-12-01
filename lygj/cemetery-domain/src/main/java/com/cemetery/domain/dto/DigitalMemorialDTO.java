package com.cemetery.domain.dto;

import com.cemetery.domain.enums.AccessPermissionEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数字纪念空间DTO
 */
@Data
public class DigitalMemorialDTO {

    /**
     * 纪念空间ID（更新时必填）
     */
    private Long id;

    /**
     * 纪念空间编号
     */
    private String spaceNo;

    /**
     * 关联墓位ID
     */
    @NotNull(message = "墓位ID不能为空")
    private Long tombId;

    /**
     * 逝者ID
     */
    @NotNull(message = "逝者ID不能为空")
    private Long deceasedId;

    /**
     * 纪念空间名称
     */
    @NotBlank(message = "纪念空间名称不能为空")
    private String spaceName;

    /**
     * 生平介绍
     */
    private String biography;

    /**
     * 生平成就
     */
    private String lifeAchievements;

    /**
     * 家属寄语
     */
    private String familyWords;

    /**
     * 访问权限
     */
    private AccessPermissionEnum accessPermission;

    /**
     * 访问密码（仅当accessPermission=PASSWORD_PROTECTED时需要）
     */
    private String accessPassword;

    /**
     * 背景主题
     */
    private String backgroundTheme;

    /**
     * 背景颜色
     */
    private String backgroundColor;

    /**
     * 背景图片地址
     */
    private String backgroundImage;

    /**
     * 是否启用背景音乐
     */
    private Integer musicEnabled;

    /**
     * 背景音乐地址
     */
    private String musicUrl;

    /**
     * 背景音乐名称
     */
    private String musicName;

    /**
     * 是否显示电子蜡烛
     */
    private Integer candleEnabled;

    /**
     * 是否显示电子献花
     */
    private Integer flowerEnabled;

    /**
     * 是否显示电子上香
     */
    private Integer incenseEnabled;

    /**
     * 是否允许留言
     */
    private Integer messageEnabled;

    /**
     * 留言是否需要审核
     */
    private Integer messageAudit;

    /**
     * 是否已发布
     */
    private Integer isPublished;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 到期时间
     */
    private Date expireTime;

    /**
     * 备注
     */
    private String remark;
}
