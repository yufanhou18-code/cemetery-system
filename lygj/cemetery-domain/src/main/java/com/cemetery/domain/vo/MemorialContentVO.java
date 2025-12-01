package com.cemetery.domain.vo;

import com.cemetery.domain.enums.AuditStatusEnum;
import com.cemetery.domain.enums.ContentTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 纪念内容VO
 */
@Data
public class MemorialContentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memorialId;
    private ContentTypeEnum contentType;
    private String title;
    private String description;
    private String contentUrl;
    private String contentText;
    private String thumbnailUrl;
    private Long fileSize;
    private Integer duration;
    private Integer width;
    private Integer height;
    private Integer sortOrder;
    private AuditStatusEnum auditStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    private Long auditBy;
    private String auditRemark;
    private Integer isFeatured;
    private Integer viewCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;

    private Long uploadBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String remark;

    // 关联信息
    private String uploadUserName;
    private String auditUserName;
    private String memorialSpaceName;
}
