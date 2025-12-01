package com.cemetery.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存服务接口
 */
public interface CacheService {

    /**
     * 设置缓存
     *
     * @param key 键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 设置缓存并指定过期时间
     *
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取缓存
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值
     */
    <T> T get(String key);

    /**
     * 获取缓存，如果不存在则从数据库加载并缓存
     *
     * @param key 键
     * @param loader 数据加载器
     * @param timeout 过期时间
     * @param unit 时间单位
     * @param <T> 值类型
     * @return 值
     */
    <T> T get(String key, Supplier<T> loader, long timeout, TimeUnit unit);

    /**
     * 批量获取缓存
     *
     * @param keys 键列表
     * @param <T> 值类型
     * @return 值列表
     */
    <T> List<T> multiGet(Collection<String> keys);

    /**
     * 删除缓存
     *
     * @param key 键
     */
    void delete(String key);

    /**
     * 批量删除缓存
     *
     * @param keys 键列表
     */
    void deleteMulti(Collection<String> keys);

    /**
     * 删除匹配模式的所有键
     *
     * @param pattern 模式
     */
    void deletePattern(String pattern);

    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 设置键的过期时间
     *
     * @param key 键
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void expire(String key, long timeout, TimeUnit unit);

    /**
     * 递增操作
     *
     * @param key 键
     * @param delta 增量
     * @return 递增后的值
     */
    Long increment(String key, long delta);

    /**
     * 递减操作
     *
     * @param key 键
     * @param delta 减量
     * @return 递减后的值
     */
    Long decrement(String key, long delta);

    /**
     * Hash操作 - 设置
     *
     * @param key 键
     * @param hashKey Hash键
     * @param value 值
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * Hash操作 - 获取
     *
     * @param key 键
     * @param hashKey Hash键
     * @param <T> 值类型
     * @return 值
     */
    <T> T hGet(String key, String hashKey);

    /**
     * Hash操作 - 批量设置
     *
     * @param key 键
     * @param map 数据
     */
    void hSetAll(String key, Map<String, Object> map);

    /**
     * Hash操作 - 获取所有
     *
     * @param key 键
     * @return 所有数据
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * Hash操作 - 删除
     *
     * @param key 键
     * @param hashKeys Hash键
     */
    void hDelete(String key, Object... hashKeys);

    /**
     * Set操作 - 添加
     *
     * @param key 键
     * @param values 值
     * @return 添加数量
     */
    Long sAdd(String key, Object... values);

    /**
     * Set操作 - 获取所有成员
     *
     * @param key 键
     * @param <T> 成员类型
     * @return 所有成员
     */
    <T> Set<T> sMembers(String key);

    /**
     * Set操作 - 判断是否成员
     *
     * @param key 键
     * @param value 值
     * @return 是否成员
     */
    Boolean sIsMember(String key, Object value);

    /**
     * Set操作 - 删除
     *
     * @param key 键
     * @param values 值
     * @return 删除数量
     */
    Long sRemove(String key, Object... values);

    /**
     * ZSet操作 - 添加
     *
     * @param key 键
     * @param value 值
     * @param score 分数
     */
    void zAdd(String key, Object value, double score);

    /**
     * ZSet操作 - 获取指定范围
     *
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @param <T> 值类型
     * @return 值列表
     */
    <T> Set<T> zRange(String key, long start, long end);

    /**
     * ZSet操作 - 根据分数范围获取
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @param <T> 值类型
     * @return 值列表
     */
    <T> Set<T> zRangeByScore(String key, double min, double max);

    /**
     * ZSet操作 - 删除
     *
     * @param key 键
     * @param values 值
     * @return 删除数量
     */
    Long zRemove(String key, Object... values);

    /**
     * 构建缓存键
     *
     * @param parts 键的组成部分
     * @return 完整的缓存键
     */
    String buildKey(String... parts);

    /**
     * 缓存预热
     */
    void preloadCache();

    /**
     * 清空所有缓存
     */
    void clearAllCache();
}
