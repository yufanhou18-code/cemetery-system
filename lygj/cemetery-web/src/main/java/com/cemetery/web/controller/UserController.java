package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.UserDTO;
import com.cemetery.domain.vo.UserVO;
import com.cemetery.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("创建用户")
    @PostMapping
    public Result<Long> createUser(@Validated @RequestBody UserDTO userDTO) {
        Long userId = userService.createUser(userDTO);
        return Result.success("用户创建成功", userId);
    }

    @ApiOperation("更新用户")
    @PutMapping
    public Result<Void> updateUser(@Validated @RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return Result.success();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return Result.success();
    }

    @ApiOperation("根据ID查询用户")
    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable Long userId) {
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    @ApiOperation("分页查询用户列表")
    @PostMapping("/page")
    public Result<Page<UserVO>> pageUsers(@RequestBody PageQueryDTO pageQueryDTO) {
        Page<UserVO> page = userService.pageUsers(pageQueryDTO);
        return Result.success(page);
    }

    @ApiOperation("修改密码")
    @PostMapping("/{userId}/change-password")
    public Result<Void> changePassword(
            @PathVariable Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @ApiOperation("重置密码")
    @PostMapping("/{userId}/reset-password")
    public Result<Void> resetPassword(
            @PathVariable Long userId,
            @RequestParam String newPassword) {
        userService.resetPassword(userId, newPassword);
        return Result.success();
    }

    @ApiOperation("启用用户")
    @PostMapping("/{userId}/enable")
    public Result<Void> enableUser(@PathVariable Long userId) {
        userService.changeUserStatus(userId, com.cemetery.domain.enums.UserStatusEnum.NORMAL);
        return Result.success();
    }

    @ApiOperation("禁用用户")
    @PostMapping("/{userId}/disable")
    public Result<Void> disableUser(@PathVariable Long userId) {
        userService.changeUserStatus(userId, com.cemetery.domain.enums.UserStatusEnum.DISABLED);
        return Result.success();
    }

    @ApiOperation("根据用户名查询用户")
    @GetMapping("/username/{username}")
    public Result<UserVO> getUserByUsername(@PathVariable String username) {
        UserVO userVO = userService.getUserByUsername(username);
        return Result.success(userVO);
    }

    @ApiOperation("根据手机号查询用户")
    @GetMapping("/phone/{phone}")
    public Result<UserVO> getUserByPhone(@PathVariable String phone) {
        UserVO userVO = userService.getUserByPhone(phone);
        return Result.success(userVO);
    }
}
