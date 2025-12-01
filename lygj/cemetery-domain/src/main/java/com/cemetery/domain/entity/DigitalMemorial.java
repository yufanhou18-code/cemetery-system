package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.AccessPermissionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 数字纪念空间实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("digital_memorial")
public class DigitalMemorial extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID - 使用雪花算法生成（分区表必需）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 纪念空间编号（唯一标识）
     */
    @TableField("space_no")
    private String spaceNo;

    /**
     * 关联墓位ID（关联tomb_location）
     */
    @TableField("tomb_id")
    private Long tombId;

    /**
     * 逝者ID（关联deceased_info）
     */
    @TableField("deceased_id")
    private Long deceasedId;

    /**
     * 纪念空间名称
     */
    @TableField("space_name")
    private String spaceName;

    /**
     * 生平介绍（详细版）
     */
    @TableField("biography")
    private String biography;

    /**
     * 生平成就
     */
    @TableField("life_achievements")
    private String lifeAchievements;

    /**
     * 家属寄语
     */
    @TableField("family_words")
    private String familyWords;

    /**
     * 访问权限（1-公开，2-家属可见，3-密码访问，4-完全私密）
     */
    @TableField("access_permission")
    private AccessPermissionEnum accessPermission;

    /**
     * 访问密码（BCrypt加密，仅当access_permission=3时有效）
     */
    @TableField("access_password")
    private String accessPassword;

    /**
     * 背景主题（default-默认，spring-春天，autumn-秋天，classic-经典，modern-现代）
     */
    @TableField("background_theme")
    private String backgroundTheme;

    /**
     * 背景颜色（十六进制色值）
     */
    @TableField("background_color")
    private String backgroundColor;

    /**
     * 背景图片地址
     */
    @TableField("background_image")
    private String backgroundImage;

    /**
     * 是否启用背景音乐（0-否，1-是）
     */
    @TableField("music_enabled")
    private Integer musicEnabled;

    /**
     * 背景音乐地址
     */
    @TableField("music_url")
    private String musicUrl;

    /**
     * 背景音乐名称
     */
    @TableField("music_name")
    private String musicName;

    /**
     * 是否显示电子蜡烛（0-否，1-是）
     */
    @TableField("candle_enabled")
    private Integer candleEnabled;

    /**
     * 是否显示电子献花（0-否，1-是）
     */
    @TableField("flower_enabled")
    private Integer flowerEnabled;

    /**
     * 是否显示电子上香（0-否，1-是）
     */
    @TableField("incense_enabled")
    private Integer incenseEnabled;

    /**
     * 是否允许留言（0-否，1-是）
     */
    @TableField("message_enabled")
    private Integer messageEnabled;

    /**
     * 留言是否需要审核（0-否，1-是）
     */
    @TableField("message_audit")
    private Integer messageAudit;

    /**
     * 访问次数
     */
    @TableField("visit_count")
    private Integer visitCount;

    /**
     * 点烛次数
     */
    @TableField("candle_count")
    private Integer candleCount;

    /**
     * 献花次数
     */
    @TableField("flower_count")
    private Integer flowerCount;

    /**
     * 上香次数
     */
    @TableField("incense_count")
    private Integer incenseCount;

    /**
     * 留言次数
     */
    @TableField("message_count")
    private Integer messageCount;

    /**
     * 是否已发布（0-未发布，1-已发布）
     */
    @TableField("is_published")
    private Integer isPublished;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 到期时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 专属二维码地址
     */
    @TableField("qr_code")
    private String qrCode;

    /**
     * 分享链接
     */
    @TableField("share_url")
    private String shareUrl;

    // ========== 关联对象（非数据库字段） ==========

    /**
     * 关联的墓位信息
     */
    @TableField(exist = false)
    private TombLocation tombLocation;

    /**
     * 关联的逝者信息
     */
    @TableField(exist = false)
    private DeceasedInfo deceasedInfo;
}
