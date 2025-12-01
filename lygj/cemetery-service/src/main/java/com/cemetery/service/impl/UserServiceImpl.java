package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.UserDTO;
import com.cemetery.domain.entity.User;
import com.cemetery.domain.enums.UserStatusEnum;
import com.cemetery.domain.mapper.UserMapper;
import com.cemetery.domain.vo.UserVO;
import com.cemetery.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserDTO userDTO) {
        // 校验用户名唯一性
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 校验手机号唯一性
        if (StringUtils.isNotBlank(userDTO.getPhone())) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, userDTO.getPhone());
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("手机号已被使用");
            }
        }

        // 转换并保存
        User user = EntityConverter.INSTANCE.toUser(userDTO);
        
        // 密码加密
        if (StringUtils.isNotBlank(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            // 默认密码123456
            user.setPassword(passwordEncoder.encode("123456"));
        }

        userMapper.insert(user);
        log.info("创建用户成功, userId={}, username={}", user.getId(), user.getUsername());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }

        User existUser = userMapper.selectById(userDTO.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 校验用户名唯一性（排除自己）
        if (StringUtils.isNotBlank(userDTO.getUsername()) 
                && !userDTO.getUsername().equals(existUser.getUsername())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, userDTO.getUsername());
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("用户名已存在");
            }
        }

        // 转换并更新（不更新密码）
        User user = EntityConverter.INSTANCE.toUser(userDTO);
        user.setPassword(null); // 不更新密码
        
        userMapper.updateById(user);
        log.info("更新用户成功, userId={}", user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 逻辑删除
        userMapper.deleteById(userId);
        log.info("删除用户成功, userId={}", userId);
    }

    @Override
    public UserVO getUserById(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return EntityConverter.INSTANCE.toUserVO(user);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BusinessException("用户名不能为空");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        return user != null ? EntityConverter.INSTANCE.toUserVO(user) : null;
    }

    @Override
    public UserVO getUserByPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(wrapper);

        return user != null ? EntityConverter.INSTANCE.toUserVO(user) : null;
    }

    @Override
    public Page<UserVO> pageUsers(PageQueryDTO pageQueryDTO) {
        Page<User> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> userPage = userMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toUserVOList(userPage.getRecords()));
        
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUserStatus(Long userId, UserStatusEnum status) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setStatus(status);
        userMapper.updateById(user);
        log.info("修改用户状态成功, userId={}, status={}", userId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String newPassword) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            throw new BusinessException("新密码不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
        log.info("重置密码成功, userId={}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (StringUtils.isBlank(oldPassword)) {
            throw new BusinessException("旧密码不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            throw new BusinessException("新密码不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
        log.info("修改密码成功, userId={}", userId);
    }

    @Override
    public List<UserVO> listAllUsers() {
        List<User> userList = userMapper.selectList(null);
        return EntityConverter.INSTANCE.toUserVOList(userList);
    }
}
