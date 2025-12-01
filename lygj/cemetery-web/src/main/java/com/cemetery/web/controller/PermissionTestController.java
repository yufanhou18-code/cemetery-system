package com.cemetery.web.controller;

import com.cemetery.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限测试控制器
 * 用于测试RBAC权限控制功能
 */
@Api(tags = "权限测试接口")
@RestController
@RequestMapping("/api")
public class PermissionTestController {

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/current-user")
    public Result<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", authentication.getPrincipal());
        data.put("username", authentication.getDetails());
        data.put("authorities", authentication.getAuthorities());
        
        return Result.success(data);
    }

    @ApiOperation("管理员专用接口")
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> adminTest() {
        return Result.success("管理员接口访问成功");
    }

    @ApiOperation("家属专用接口")
    @GetMapping("/family/test")
    @PreAuthorize("hasAnyRole('ADMIN', 'FAMILY')")
    public Result<String> familyTest() {
        return Result.success("家属接口访问成功");
    }

    @ApiOperation("服务商专用接口")
    @GetMapping("/provider/test")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROVIDER')")
    public Result<String> providerTest() {
        return Result.success("服务商接口访问成功");
    }

    @ApiOperation("公共接口（需要认证）")
    @GetMapping("/common/test")
    public Result<String> commonTest() {
        return Result.success("公共接口访问成功");
    }
}
