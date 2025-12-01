package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.AuditStatusEnum;
import com.cemetery.domain.enums.MessageTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 追思留言实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("memorial_message")
public class MemorialMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 纪念空间ID（关联digital_memorial）
     */
    @TableField("memorial_id")
    private Long memorialId;

    /**
     * 留言用户ID（关联sys_user，匿名留言时为NULL）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 留言人昵称
     */
    @TableField("author_name")
    private String authorName;

    /**
     * 留言人头像
     */
    @TableField("author_avatar")
    private String authorAvatar;

    /**
     * 与逝者关系（子女、配偶、父母、兄弟姐妹、朋友、同事等）
     */
    @TableField("relationship")
    private String relationship;

    /**
     * 留言内容
     */
    @TableField("message_content")
    private String messageContent;

    /**
     * 留言类型（1-普通留言，2-祭日留言，3-纪念日留言）
     */
    @TableField("message_type")
    private MessageTypeEnum messageType;

    /**
     * 留言配图（多个用逗号分隔）
     */
    @TableField("images")
    private String images;

    /**
     * 是否匿名（0-否，1-是）
     */
    @TableField("is_anonymous")
    private Integer isAnonymous;

    /**
     * 是否置顶（0-否，1-是）
     */
    @TableField("is_pinned")
    private Integer isPinned;

    /**
     * 审核状态（1-待审核，2-审核通过，3-审核拒绝）
     */
    @TableField("audit_status")
    private AuditStatusEnum auditStatus;

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
     * 点赞次数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 回复次数
     */
    @TableField("reply_count")
    private Integer replyCount;

    /**
     * 父留言ID（用于留言回复）
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 回复给哪条留言
     */
    @TableField("reply_to_id")
    private Long replyToId;

    /**
     * 回复给谁
     */
    @TableField("reply_to_name")
    private String replyToName;

    /**
     * 留言位置-省份
     */
    @TableField("location_province")
    private String locationProvince;

    /**
     * 留言位置-城市
     */
    @TableField("location_city")
    private String locationCity;

    /**
     * 留言位置信息
     */
    @TableField("location_info")
    private String locationInfo;

    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 设备信息
     */
    @TableField("device_info")
    private String deviceInfo;

    /**
     * 留言时间
     */
    @TableField("message_time")
    private Date messageTime;

    // ========== 关联对象（非数据库字段） ==========

    /**
     * 关联的纪念空间信息
     */
    @TableField(exist = false)
    private DigitalMemorial digitalMemorial;

    /**
     * 留言用户名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 审核人名称
     */
    @TableField(exist = false)
    private String auditUserName;

    /**
     * 父留言信息
     */
    @TableField(exist = false)
    private MemorialMessage parentMessage;

    /**
     * 是否已点赞（当前登录用户）
     */
    @TableField(exist = false)
    private Boolean isLiked;
}
