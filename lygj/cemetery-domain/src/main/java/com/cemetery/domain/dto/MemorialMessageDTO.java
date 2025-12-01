package com.cemetery.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 追思留言DTO
 */
@Data
@ApiModel("追思留言传输对象")
public class MemorialMessageDTO {

    @ApiModelProperty(value = "留言ID（更新时必填）", example = "10001")
    private Long id;

    @ApiModelProperty(value = "纪念空间ID", required = true, example = "10001")
    @NotNull(message = "纪念空间ID不能为空")
    private Long memorialId;

    @ApiModelProperty(value = "留言人昵称", required = true, example = "张三")
    @NotBlank(message = "留言人昵称不能为空")
    @Size(min = 2, max = 50, message = "昵称长度在2-50个字符之间")
    private String authorName;

    @ApiModelProperty(value = "留言人头像", example = "http://xxx.com/avatar.jpg")
    @Size(max = 500, message = "头像地址长度不能超过500个字符")
    private String authorAvatar;

    @ApiModelProperty(value = "与逝者关系", example = "子女")
    @Size(max = 50, message = "关系长度不能超过50个字符")
    private String relationship;

    @ApiModelProperty(value = "留言内容", required = true, example = "愿您在天堂安好")
    @NotBlank(message = "留言内容不能为空")
    @Size(min = 1, max = 2000, message = "留言内容长度在1-2000个字符之间")
    private String messageContent;

    @ApiModelProperty(value = "留言类型：1-普通留言，2-祭日留言，3-纪念日留言", example = "1")
    @Min(value = 1, message = "留言类型最小值为1")
    @Max(value = 3, message = "留言类型最大值为3")
    private Integer messageType;

    @ApiModelProperty(value = "留言配图（多个用逗号分隔）", example = "http://xxx.com/img1.jpg,http://xxx.com/img2.jpg")
    @Size(max = 2000, message = "配图地址总长度不能超过2000个字符")
    private String images;

    @ApiModelProperty(value = "是否匿名：0-否，1-是", example = "0")
    private Integer isAnonymous;

    @ApiModelProperty(value = "父留言ID（用于留言回复）", example = "10000")
    private Long parentId;

    @ApiModelProperty(value = "回复给哪条留言", example = "10000")
    private Long replyToId;

    @ApiModelProperty(value = "回复给谁", example = "李四")
    @Size(max = 50, message = "回复对象昵称长度不能超过50个字符")
    private String replyToName;

    @ApiModelProperty(value = "位置省份", example = "北京市")
    @Size(max = 50, message = "省份长度不能超过50个字符")
    private String locationProvince;

    @ApiModelProperty(value = "位置城市", example = "北京市")
    @Size(max = 50, message = "城市长度不能超过50个字符")
    private String locationCity;

    @ApiModelProperty(value = "位置详细信息", example = "朝阳区")
    @Size(max = 200, message = "位置信息长度不能超过200个字符")
    private String locationInfo;
}
