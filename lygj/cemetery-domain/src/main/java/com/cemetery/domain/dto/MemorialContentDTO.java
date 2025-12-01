package com.cemetery.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 纪念内容DTO
 */
@Data
@ApiModel("纪念内容传输对象")
public class MemorialContentDTO {

    @ApiModelProperty(value = "内容ID（更新时必填）", example = "10001")
    private Long id;

    @ApiModelProperty(value = "纪念空间ID", required = true, example = "10001")
    @NotNull(message = "纪念空间ID不能为空")
    private Long memorialId;

    @ApiModelProperty(value = "内容类型：1-文本，2-图片，3-视频，4-音频，5-文档", required = true, example = "2")
    @NotNull(message = "内容类型不能为空")
    @Min(value = 1, message = "内容类型最小值为1")
    @Max(value = 5, message = "内容类型最大值为5")
    private Integer contentType;

    @ApiModelProperty(value = "内容标题", example = "珍贵回忆")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    @ApiModelProperty(value = "内容描述", example = "1985年春节全家福")
    @Size(max = 1000, message = "描述长度不能超过1000个字符")
    private String description;

    @ApiModelProperty(value = "内容地址（图片/视频/音频/文档的URL）", example = "http://xxx.com/photo.jpg")
    @Size(max = 1000, message = "内容地址长度不能超过1000个字符")
    private String contentUrl;

    @ApiModelProperty(value = "文本内容（当contentType=1时使用）")
    @Size(max = 10000, message = "文本内容长度不能超过10000个字符")
    private String contentText;

    @ApiModelProperty(value = "缩略图地址", example = "http://xxx.com/photo_thumb.jpg")
    @Size(max = 500, message = "缩略图地址长度不能超过500个字符")
    private String thumbnailUrl;

    @ApiModelProperty(value = "文件大小（字节）", example = "1048576")
    @Min(value = 0, message = "文件大小不能为负数")
    private Long fileSize;

    @ApiModelProperty(value = "时长（秒，适用于视频/音频）", example = "120")
    @Min(value = 0, message = "时长不能为负数")
    private Integer duration;

    @ApiModelProperty(value = "宽度（像素，适用于图片/视频）", example = "1920")
    @Min(value = 0, message = "宽度不能为负数")
    private Integer width;

    @ApiModelProperty(value = "高度（像素，适用于图片/视频）", example = "1080")
    @Min(value = 0, message = "高度不能为负数")
    private Integer height;

    @ApiModelProperty(value = "排序序号", example = "1")
    @Min(value = 0, message = "排序序号不能为负数")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否精选展示：0-否，1-是", example = "0")
    private Integer isFeatured;

    @ApiModelProperty(value = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
