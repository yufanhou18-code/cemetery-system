package com.cemetery.web.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * 权限工具类
 * 用于在代码中进行权限判断
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前登录用户名
     *
     * @return 用户名
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() != null) {
            return authentication.getDetails().toString();
        }
        return null;
    }

    /**
     * 获取当前用户的权限列表
     *
     * @return 权限列表
     */
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities();
        }
        return null;
    }

    /**
     * 判断当前用户是否拥有指定角色
     *
     * @param role 角色名称（不需要ROLE_前缀）
     * @return true-拥有，false-不拥有
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(roleWithPrefix));
    }

    /**
     * 判断当前用户是否拥有任意一个指定角色
     *
     * @param roles 角色名称列表（不需要ROLE_前缀）
     * @return true-拥有任意一个，false-都不拥有
     */
    public static boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前用户是否拥有所有指定角色
     *
     * @param roles 角色名称列表（不需要ROLE_前缀）
     * @return true-拥有所有，false-不拥有所有
     */
    public static boolean hasAllRoles(String... roles) {
        for (String role : roles) {
            if (!hasRole(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为管理员
     *
     * @return true-是管理员，false-不是管理员
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * 判断是否为家属用户
     *
     * @return true-是家属，false-不是家属
     */
    public static boolean isFamily() {
        return hasRole("FAMILY");
    }

    /**
     * 判断是否为服务商
     *
     * @return true-是服务商，false-不是服务商
     */
    public static boolean isProvider() {
        return hasRole("PROVIDER");
    }

    /**
     * 获取当前认证对象
     *
     * @return Authentication对象
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断是否已认证
     *
     * @return true-已认证，false-未认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }
}
