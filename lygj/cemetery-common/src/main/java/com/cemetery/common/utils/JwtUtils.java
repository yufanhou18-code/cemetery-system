package com.cemetery.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和解析JWT令牌
 */
public class JwtUtils {

    /**
     * 密钥（实际使用中应该从配置文件读取）
     */
    private static final String SECRET = "cemetery-management-system-secret-key-for-jwt-token-generation";

    /**
     * 过期时间（12小时，单位：毫秒）
     */
    private static final long EXPIRATION = 12 * 60 * 60 * 1000;

    /**
     * 生成密钥
     */
    private static Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成JWT令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT令牌
     */
    public static String createToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims);
    }

    /**
     * 生成JWT令牌（包含角色信息）
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param userType 用户类型
     * @return JWT令牌
     */
    public static String createToken(Long userId, String username, Integer userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("userType", userType);
        // 根据用户类型设置角色
        String role = mapUserTypeToRole(userType);
        claims.put("role", role);
        return createToken(claims);
    }

    /**
     * 映射用户类型到角色
     *
     * @param userType 用户类型
     * @return 角色名称
     */
    private static String mapUserTypeToRole(Integer userType) {
        if (userType == null) {
            return "ROLE_FAMILY";
        }
        switch (userType) {
            case 1:
                return "ROLE_ADMIN";
            case 2:
                return "ROLE_PROVIDER";
            case 3:
                return "ROLE_FAMILY";
            default:
                return "ROLE_FAMILY";
        }
    }

    /**
     * 生成JWT令牌
     *
     * @param claims 自定义声明
     * @return JWT令牌
     */
    public static String createToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌
     * @return Claims对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public static String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    /**
     * 从令牌中获取用户类型
     *
     * @param token JWT令牌
     * @return 用户类型
     */
    public static Integer getUserType(String token) {
        Claims claims = parseToken(token);
        return claims.get("userType", Integer.class);
    }

    /**
     * 从令牌中获取角色
     *
     * @param token JWT令牌
     * @return 角色名称
     */
    public static String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    /**
     * 验证令牌是否过期
     *
     * @param token JWT令牌
     * @return true-已过期，false-未过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证令牌
     *
     * @param token JWT令牌
     * @return true-有效，false-无效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
