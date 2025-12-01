package com.cemetery.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 搜索查询DTO
 */
@Data
@ApiModel("搜索查询参数")
public class MemorialSearchDTO {

    @ApiModelProperty(value = "搜索关键词", required = true, example = "张三")
    @NotBlank(message = "搜索关键词不能为空")
    @Size(min = 1, max = 100, message = "关键词长度在1-100个字符之间")
    private String keyword;

    @ApiModelProperty(value = "搜索类型：1-纪念空间，2-逝者，3-内容，4-留言", example = "1")
    private Integer searchType;

    @ApiModelProperty(value = "访问权限过滤：1-公开，2-家属可见", example = "1")
    private Integer accessPermission;

    @ApiModelProperty(value = "是否只搜索已发布", example = "true")
    private Boolean onlyPublished;

    @ApiModelProperty(value = "排序字段：visitCount-访问量，createTime-创建时间，score-相关度", example = "score")
    private String sortField;

    @ApiModelProperty(value = "排序方向：asc-升序，desc-降序", example = "desc")
    private String sortOrder;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "是否高亮显示", example = "true")
    private Boolean highlight;
}
