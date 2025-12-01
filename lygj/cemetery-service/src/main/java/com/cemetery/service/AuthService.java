package com.cemetery.service;

import com.cemetery.domain.dto.LoginDTO;
import com.cemetery.domain.vo.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户登出
     *
     * @param token 令牌
     */
    void logout(String token);

    /**
     * 刷新令牌
     *
     * @param token 旧令牌
     * @return 新令牌
     */
    String refreshToken(String token);
}
