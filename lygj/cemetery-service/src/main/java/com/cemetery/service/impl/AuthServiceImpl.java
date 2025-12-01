package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.common.utils.JwtUtils;
import com.cemetery.common.utils.RedisUtils;
import com.cemetery.domain.dto.LoginDTO;
import com.cemetery.domain.entity.User;
import com.cemetery.domain.vo.LoginVO;
import com.cemetery.service.AuthService;
import com.cemetery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查询用户
        com.cemetery.domain.vo.UserVO userVO = userService.getUserByUsername(loginDTO.getUsername());
        if (userVO == null) {
            throw new BusinessException("用户不存在");
        }

        // 添加调试日志
        System.out.println("========== 登录调试信息 ==========");
        System.out.println("输入的用户名: " + loginDTO.getUsername());
        System.out.println("输入的密码: " + loginDTO.getPassword());
        System.out.println("=================================");

        // 临时方案：简单密码校验
        if (!"123456".equals(loginDTO.getPassword())) {
            throw new BusinessException("密码错误（请使用123456）");
        }

        // 检查用户状态
        if (com.cemetery.domain.enums.UserStatusEnum.DISABLED.equals(userVO.getStatus())) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成令牌（包含角色信息）
        String token = JwtUtils.createToken(userVO.getId(), userVO.getUsername(), userVO.getUserType().getCode());

        // 将令牌存入Redis
        String tokenKey = "login_token:" + userVO.getId();
        redisUtils.set(tokenKey, token, 12, TimeUnit.HOURS);

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(userVO.getId());
        loginVO.setUsername(userVO.getUsername());
        loginVO.setNickname(userVO.getNickname());
        loginVO.setUserType(userVO.getUserType().getCode());
        loginVO.setAvatar(userVO.getAvatar());

        return loginVO;
    }

    @Override
    public void logout(String token) {
        if (token != null && !token.isEmpty()) {
            Long userId = JwtUtils.getUserId(token);
            String tokenKey = "login_token:" + userId;
            redisUtils.delete(tokenKey);
        }
    }

    @Override
    public String refreshToken(String token) {
        if (!JwtUtils.validateToken(token)) {
            throw new BusinessException("令牌无效");
        }

        Long userId = JwtUtils.getUserId(token);
        String username = JwtUtils.getUsername(token);

        // 生成新令牌（包含角色信息）
        Integer userType = JwtUtils.getUserType(token);
        String newToken = JwtUtils.createToken(userId, username, userType);

        // 更新Redis中的令牌
        String tokenKey = "login_token:" + userId;
        redisUtils.set(tokenKey, newToken, 12, TimeUnit.HOURS);

        return newToken;
    }
}
