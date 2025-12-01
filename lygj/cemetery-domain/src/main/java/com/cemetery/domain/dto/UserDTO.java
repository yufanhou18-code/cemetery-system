package com.cemetery.domain.dto;

import com.cemetery.domain.enums.GenderEnum;
import com.cemetery.domain.enums.UserStatusEnum;
import com.cemetery.domain.enums.UserTypeEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户DTO
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名（登录账号）
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别
     */
    private GenderEnum gender;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;

    /**
     * 账号状态
     */
    private UserStatusEnum status;

    /**
     * 备注
     */
    private String remark;
}
