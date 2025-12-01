package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.UserDTO;
import com.cemetery.domain.enums.UserStatusEnum;
import com.cemetery.domain.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 创建用户
     * @param userDTO 用户DTO
     * @return 用户ID
     */
    Long createUser(UserDTO userDTO);

    /**
     * 更新用户
     * @param userDTO 用户DTO
     */
    void updateUser(UserDTO userDTO);

    /**
     * 删除用户
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户VO
     */
    UserVO getUserById(Long userId);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户VO
     */
    UserVO getUserByUsername(String username);

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户VO
     */
    UserVO getUserByPhone(String phone);

    /**
     * 分页查询用户
     * @param pageQueryDTO 分页查询参数
     * @return 用户分页列表
     */
    Page<UserVO> pageUsers(PageQueryDTO pageQueryDTO);

    /**
     * 启用/禁用用户
     * @param userId 用户ID
     * @param status 状态
     */
    void changeUserStatus(Long userId, UserStatusEnum status);

    /**
     * 重置密码
     * @param userId 用户ID
     * @param newPassword 新密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<UserVO> listAllUsers();
}
