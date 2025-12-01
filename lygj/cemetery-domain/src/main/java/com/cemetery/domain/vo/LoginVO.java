package com.cemetery.domain.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户登录返回VO
 */
@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 头像
     */
    private String avatar;
}
