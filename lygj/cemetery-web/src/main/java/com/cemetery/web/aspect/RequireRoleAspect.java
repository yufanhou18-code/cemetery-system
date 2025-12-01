package com.cemetery.web.aspect;

import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.web.utils.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限校验切面
 * 处理@RequireRole注解
 */
@Aspect
@Component
public class RequireRoleAspect {

    @Around("@annotation(com.cemetery.common.annotation.RequireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取注解
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        if (requireRole == null) {
            return joinPoint.proceed();
        }
        
        // 获取需要的角色
        String[] roles = requireRole.value();
        RequireRole.Logical logical = requireRole.logical();
        
        // 检查权限
        boolean hasPermission;
        if (logical == RequireRole.Logical.AND) {
            hasPermission = SecurityUtils.hasAllRoles(roles);
        } else {
            hasPermission = SecurityUtils.hasAnyRole(roles);
        }
        
        if (!hasPermission) {
            throw new BusinessException("权限不足，无法访问该资源");
        }
        
        // 继续执行方法
        return joinPoint.proceed();
    }
}
