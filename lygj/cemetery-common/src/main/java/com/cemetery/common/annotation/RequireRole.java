package com.cemetery.common.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * 用于方法级别的权限控制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    
    /**
     * 需要的角色列表（不需要ROLE_前缀）
     */
    String[] value();
    
    /**
     * 逻辑关系：AND-需要所有角色，OR-需要任意一个角色
     */
    Logical logical() default Logical.OR;
    
    enum Logical {
        AND, OR
    }
}
