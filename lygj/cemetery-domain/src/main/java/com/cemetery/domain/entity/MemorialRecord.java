package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.MemorialTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 祭扫记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("memorial_record")
public class MemorialRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（关联sys_user）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 逝者ID（关联deceased_info）
     */
    @TableField("deceased_id")
    private Long deceasedId;

    /**
     * 墓位ID（关联tomb_location）
     */
    @TableField("tomb_id")
    private Long tombId;

    /**
     * 祭扫类型（1-现场祭扫，2-在线祭扫，3-代客祭扫）
     */
    @TableField("memorial_type")
    private MemorialTypeEnum memorialType;

    /**
     * 祭扫时间
     */
    @TableField("memorial_date")
    private Date memorialDate;

    /**
     * 祭扫留言内容
     */
    @TableField("content")
    private String content;

    /**
     * 祭扫照片（多个用逗号分隔）
     */
    @TableField("images")
    private String images;

    /**
     * 供品清单
     */
    @TableField("offerings")
    private String offerings;
}
