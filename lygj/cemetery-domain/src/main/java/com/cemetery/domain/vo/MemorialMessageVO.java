package com.cemetery.domain.vo;

import com.cemetery.domain.enums.AuditStatusEnum;
import com.cemetery.domain.enums.MessageTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 追思留言VO
 */
@Data
public class MemorialMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memorialId;
    private Long userId;
    private String authorName;
    private String authorAvatar;
    private String relationship;
    private String messageContent;
    private MessageTypeEnum messageType;
    private String images;
    private Integer isAnonymous;
    private Integer isPinned;
    private AuditStatusEnum auditStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    private String auditRemark;
    private Integer likeCount;
    private Integer replyCount;
    private Long parentId;
    private Long replyToId;
    private String replyToName;
    private String locationProvince;
    private String locationCity;
    private String locationInfo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date messageTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String remark;

    // 关联信息
    private String memorialSpaceName;
    private Boolean isLiked;
    private List<MemorialMessageVO> replies;
}
