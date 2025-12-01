package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.NoticePriorityEnum;
import com.cemetery.domain.enums.NoticeStatusEnum;
import com.cemetery.domain.enums.NoticeTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 公告通知实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cemetery_notice")
public class CemeteryNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公告标题
     */
    @TableField("title")
    private String title;

    /**
     * 公告内容
     */
    @TableField("content")
    private String content;

    /**
     * 公告类型（1-系统公告，2-活动通知，3-政策公告，4-温馨提示）
     */
    @TableField("notice_type")
    private NoticeTypeEnum noticeType;

    /**
     * 优先级（0-普通，1-重要，2-紧急）
     */
    @TableField("priority")
    private NoticePriorityEnum priority;

    /**
     * 状态（0-草稿，1-已发布，2-已下线）
     */
    @TableField("status")
    private NoticeStatusEnum status;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 过期时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 封面图片
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 阅读次数
     */
    @TableField("view_count")
    private Integer viewCount;
}
