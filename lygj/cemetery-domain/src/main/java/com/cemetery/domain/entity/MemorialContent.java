package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.AuditStatusEnum;
import com.cemetery.domain.enums.ContentTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 纪念内容实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("memorial_content")
public class MemorialContent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 纪念空间ID（关联digital_memorial）
     */
    @TableField("memorial_id")
    private Long memorialId;

    /**
     * 内容类型（1-文本，2-图片，3-视频，4-音频，5-文档）
     */
    @TableField("content_type")
    private ContentTypeEnum contentType;

    /**
     * 内容标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容描述
     */
    @TableField("description")
    private String description;

    /**
     * 内容地址（图片/视频/音频/文档的URL）
     */
    @TableField("content_url")
    private String contentUrl;

    /**
     * 文本内容（当content_type=1时使用）
     */
    @TableField("content_text")
    private String contentText;

    /**
     * 缩略图地址
     */
    @TableField("thumbnail_url")
    private String thumbnailUrl;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 时长（秒，适用于视频/音频）
     */
    @TableField("duration")
    private Integer duration;

    /**
     * 宽度（像素，适用于图片/视频）
     */
    @TableField("width")
    private Integer width;

    /**
     * 高度（像素，适用于图片/视频）
     */
    @TableField("height")
    private Integer height;

    /**
     * 排序序号（数字越小越靠前）
     */
    @TableField("sort_order")
    private Integer sortOrder;

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
     * 是否精选展示（0-否，1-是）
     */
    @TableField("is_featured")
    private Integer isFeatured;

    /**
     * 查看次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 上传时间
     */
    @TableField("upload_time")
    private Date uploadTime;

    /**
     * 上传用户ID（关联sys_user）
     */
    @TableField("upload_by")
    private Long uploadBy;

    /**
     * 上传IP
     */
    @TableField("upload_ip")
    private String uploadIp;

    // ========== 关联对象（非数据库字段） ==========

    /**
     * 关联的纪念空间信息
     */
    @TableField(exist = false)
    private DigitalMemorial digitalMemorial;

    /**
     * 上传用户名称
     */
    @TableField(exist = false)
    private String uploadUserName;

    /**
     * 审核人名称
     */
    @TableField(exist = false)
    private String auditUserName;
}
