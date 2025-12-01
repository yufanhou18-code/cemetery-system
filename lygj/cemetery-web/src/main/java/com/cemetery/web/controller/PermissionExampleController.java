package com.cemetery.web.controller;

import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.web.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限使用示例控制器
 * 演示各种权限控制方式
 */
@Api(tags = "权限使用示例")
@RestController
@RequestMapping("/api/example")
public class PermissionExampleController {

    @ApiOperation("使用@RequireRole注解 - 仅管理员")
    @GetMapping("/annotation-admin")
    @RequireRole("ADMIN")
    public Result<String> annotationAdminExample() {
        return Result.success("使用@RequireRole注解验证成功");
    }

    @ApiOperation("使用@RequireRole注解 - 管理员或家属")
    @GetMapping("/annotation-multi")
    @RequireRole(value = {"ADMIN", "FAMILY"}, logical = RequireRole.Logical.OR)
    public Result<String> annotationMultiExample() {
        return Result.success("使用@RequireRole注解（多角色OR）验证成功");
    }

    @ApiOperation("使用SecurityUtils工具类 - 编程式权限判断")
    @GetMapping("/programmatic")
    public Result<String> programmaticExample() {
        // 获取当前用户信息
        Long userId = SecurityUtils.getCurrentUserId();
        String username = SecurityUtils.getCurrentUsername();
        
        // 判断权限
        if (SecurityUtils.isAdmin()) {
            return Result.success("当前用户是管理员，userId=" + userId + ", username=" + username);
        } else if (SecurityUtils.isFamily()) {
            return Result.success("当前用户是家属，userId=" + userId + ", username=" + username);
        } else if (SecurityUtils.isProvider()) {
            return Result.success("当前用户是服务商，userId=" + userId + ", username=" + username);
        } else {
            return Result.success("当前用户角色未知");
        }
    }

    @ApiOperation("业务逻辑中的权限判断示例")
    @GetMapping("/business-logic")
    public Result<String> businessLogicExample() {
        Long userId = SecurityUtils.getCurrentUserId();
        
        // 示例：根据不同角色返回不同的数据权限
        if (SecurityUtils.isAdmin()) {
            // 管理员可以查看所有数据
            return Result.success("管理员：可查看所有数据");
        } else if (SecurityUtils.isFamily()) {
            // 家属只能查看自己相关的数据
            return Result.success("家属（userId=" + userId + "）：只能查看自己的墓位和订单");
        } else if (SecurityUtils.isProvider()) {
            // 服务商只能查看分配给自己的订单
            return Result.success("服务商（userId=" + userId + "）：只能查看分配的订单");
        }
        
        return Result.success("未知角色");
    }

    @ApiOperation("复杂权限判断示例")
    @GetMapping("/complex-permission")
    public Result<String> complexPermissionExample() {
        // 示例：需要管理员或者是特定的服务商
        if (SecurityUtils.hasRole("ADMIN")) {
            return Result.success("管理员可以访问");
        }
        
        if (SecurityUtils.hasRole("PROVIDER")) {
            Long userId = SecurityUtils.getCurrentUserId();
            // 可以在这里添加更复杂的业务逻辑判断
            // 例如：检查该服务商是否有权限处理某个订单
            return Result.success("服务商（userId=" + userId + "）需要额外的业务权限验证");
        }
        
        return Result.error("权限不足");
    }
}
