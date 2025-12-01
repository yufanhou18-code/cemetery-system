package com.cemetery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.cemetery.common.config.CacheConfig;
import com.cemetery.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 缓存服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final CacheConfig cacheConfig;

    private RBloomFilter<String> bloomFilter;

    @PostConstruct
    public void init() {
        // 初始化布隆过滤器
        if (cacheConfig.getBloomFilterEnabled()) {
            bloomFilter = redissonClient.getBloomFilter("memorial:bloom:filter");
            bloomFilter.tryInit(
                cacheConfig.getBloomFilterExpectedInsertions(),
                cacheConfig.getBloomFilterFpp()
            );
            log.info("布隆过滤器初始化成功");
        }

        // 缓存预热
        if (cacheConfig.getPreloadEnabled()) {
            preloadCache();
        }
    }

    @Override
    public void set(String key, Object value) {
        if (!cacheConfig.getEnabled()) {
            return;
        }
        set(key, value, cacheConfig.getDefaultExpire(), TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        if (!cacheConfig.getEnabled()) {
            return;
        }

        try {
            String fullKey = buildFullKey(key);
            redisTemplate.opsForValue().set(fullKey, value, timeout, unit);
            
            // 添加到布隆过滤器
            if (cacheConfig.getBloomFilterEnabled() && bloomFilter != null) {
                bloomFilter.add(fullKey);
            }
            
            log.debug("缓存设置成功: key={}, timeout={} {}", fullKey, timeout, unit);
        } catch (Exception e) {
            log.error("缓存设置失败: key={}", key, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (!cacheConfig.getEnabled()) {
            return null;
        }

        try {
            String fullKey = buildFullKey(key);
            
            // 布隆过滤器检查
            if (cacheConfig.getBloomFilterEnabled() && bloomFilter != null) {
                if (!bloomFilter.contains(fullKey)) {
                    log.debug("布隆过滤器判定不存在: key={}", fullKey);
                    return null;
                }
            }
            
            return (T) redisTemplate.opsForValue().get(fullKey);
        } catch (Exception e) {
            log.error("缓存获取失败: key={}", key, e);
            return null;
        }
    }

    @Override
    public <T> T get(String key, Supplier<T> loader, long timeout, TimeUnit unit) {
        if (!cacheConfig.getEnabled()) {
            return loader.get();
        }

        T value = get(key);
        if (value != null) {
            log.debug("缓存命中: key={}", key);
            return value;
        }

        // 缓存未命中，从数据库加载
        log.debug("缓存未命中，从数据库加载: key={}", key);
        value = loader.get();
        
        if (value != null) {
            set(key, value, timeout, unit);
        } else {
            // 防止缓存穿透：缓存空对象（短时间）
            set(key, new Object(), 60, TimeUnit.SECONDS);
        }
        
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> multiGet(Collection<String> keys) {
        if (!cacheConfig.getEnabled() || keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            List<String> fullKeys = keys.stream()
                    .map(this::buildFullKey)
                    .collect(Collectors.toList());
            
            List<Object> values = redisTemplate.opsForValue().multiGet(fullKeys);
            
            if (values == null) {
                return new ArrayList<>();
            }
            
            return values.stream()
                    .filter(Objects::nonNull)
                    .map(v -> (T) v)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("批量获取缓存失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void delete(String key) {
        if (!cacheConfig.getEnabled()) {
            return;
        }

        try {
            String fullKey = buildFullKey(key);
            redisTemplate.delete(fullKey);
            log.debug("缓存删除成功: key={}", fullKey);
        } catch (Exception e) {
            log.error("缓存删除失败: key={}", key, e);
        }
    }

    @Override
    public void deleteMulti(Collection<String> keys) {
        if (!cacheConfig.getEnabled() || keys == null || keys.isEmpty()) {
            return;
        }

        try {
            List<String> fullKeys = keys.stream()
                    .map(this::buildFullKey)
                    .collect(Collectors.toList());
            
            redisTemplate.delete(fullKeys);
            log.debug("批量删除缓存成功: count={}", fullKeys.size());
        } catch (Exception e) {
            log.error("批量删除缓存失败", e);
        }
    }

    @Override
    public void deletePattern(String pattern) {
        if (!cacheConfig.getEnabled()) {
            return;
        }

        try {
            String fullPattern = buildFullKey(pattern);
            Set<String> keys = redisTemplate.keys(fullPattern);
            
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("模式删除缓存成功: pattern={}, count={}", fullPattern, keys.size());
            }
        } catch (Exception e) {
            log.error("模式删除缓存失败: pattern={}", pattern, e);
        }
    }

    @Override
    public boolean exists(String key) {
        if (!cacheConfig.getEnabled()) {
            return false;
        }

        try {
            String fullKey = buildFullKey(key);
            return Boolean.TRUE.equals(redisTemplate.hasKey(fullKey));
        } catch (Exception e) {
            log.error("检查缓存存在性失败: key={}", key, e);
            return false;
        }
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        if (!cacheConfig.getEnabled()) {
            return;
        }

        try {
            String fullKey = buildFullKey(key);
            redisTemplate.expire(fullKey, timeout, unit);
        } catch (Exception e) {
            log.error("设置缓存过期时间失败: key={}", key, e);
        }
    }

    @Override
    public Long increment(String key, long delta) {
        if (!cacheConfig.getEnabled()) {
            return 0L;
        }

        try {
            String fullKey = buildFullKey(key);
            return redisTemplate.opsForValue().increment(fullKey, delta);
        } catch (Exception e) {
            log.error("缓存递增失败: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public Long decrement(String key, long delta) {
        if (!cacheConfig.getEnabled()) {
            return 0L;
        }

        try {
            String fullKey = buildFullKey(key);
            return redisTemplate.opsForValue().decrement(fullKey, delta);
        } catch (Exception e) {
            log.error("缓存递减失败: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        if (!cacheConfig.getEnabled()) {
            return;
        }

        try {
            String fullKey = buildFullKey(key);
            redisTemplate.opsForHash().put(fullKey, hashKey, value);
        } catch (Exception e) {
            log.error("Hash缓存设置失败: key={}, hashKey={}", key, hashKey, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String hashKey) {
        if (!cacheConfig.getEnabled()) {
            return null;
        }

        try {
            String fullKey = buildFullKey(key);
            return (T) redisTemplate.opsForHash().get(fullKey, hashKey);
        } catch (Exception e) {
            log.error("Hash缓存获取失败: key={}, hashKey={}", key, hashKey, e);
            return null;
        }
    }

    @Override
    public void hSetAll(String key, Map<String, Object> map) {
        if (!cacheConfig.getEnabled() || map == null || map.isEmpty()) {
            return;
        }

        try {
            String fullKey = buildFullKey(key);
            redisTemplate.opsForHash().putAll(fullKey, map);
        } catch (Exception e) {
            log.error("Hash批量设置失败: key={}", key, e);
        }
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        if (!cacheConfig.getEnabled()) {
            return new HashMap<>();
        }

        try {
            String fullKey = buildFullKey(key);
            return redisTemplate.opsForHash().entries(fullKey);
        } catch (Exception e) {
            log.error("Hash获取所有失败: key={}", key, e);
            return new HashMap<>();
        }
    }

    @Override
    public void hDelete(String key, Object... hashKeys) {
        if (!cacheConfig.getEnabled() || hashKeys == null || hashKeys.length == 0) {
            return;
        }

        try {
            String fullKey = buildFullKey(key);
            redisTemplate.opsForHash().delete(fullKey, hashKeys);
        } catch (Exception e) {
            log.error("Hash删除失败: key={}", key, e);
        }
    }

    @Override
    public Long sAdd(String key, Object... values) {
        if (!cacheConfig.getEnabled() || values == null || values.length == 0) {
            return 0L;
        }

        try {
            String fullKey = buildFullKey(key);
            return redisTemplate.opsForSet().add(fullKey, values);
        } catch (Exception e) {
            log.error("Set添加失败: key={}", key, e);
            return 0L;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sMembers(String key) {
        if (!cacheConfig.getEnabled()) {
            return new HashSet<>();
        }

        try {
            String fullKey = buildFullKey(key);
            Set<Object> members = redisTemplate.opsForSet().members(fullKey);
            
            if (members == null) {
                return new HashSet<>();
            }
            
            return members.stream()
                    .map(m -> (T) m)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Set获取所有成员失败: key={}", key, e);
            return new HashSet<>();
        }
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        if (!cacheConfig.getEnabled()) {
            return false;
        }

        try {
            String fullKey = buildFullKey(key);
            return redisTemplate.opsForSet().isMember(fullKey, value);
        } catch (Exception e) {
            log.error("Set判断成员失败: key={}", key, e);
            return false;
        }
    }

    @Override
    public Long sRemove(String key, Object... values) {
        if (!cacheConfig.getEnabled() || values == null || values.length == 0) {
            return 0L;
        }

        try {
            String fullKey = buildFullKey(key);
            return redisTemplate.opsForSet().remove(fullKey, values);
        } catch (Exception e) {
            log.error("Set删除失败: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public void zAdd(String key, Object value, double score) {
        if (!cacheConfig.getEnabled()) {
            return;
        }

        try {
            String fullKey = buildFullKey(key);
            redisTemplate.opsForZSet().add(fullKey, value, score);
        } catch (Exception e) {
            log.error("ZSet添加失败: key={}", key, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zRange(String key, long start, long end) {
        if (!cacheConfig.getEnabled()) {
            return new LinkedHashSet<>();
        }

        try {
            String fullKey = buildFullKey(key);
            Set<Object> range = redisTemplate.opsForZSet().range(fullKey, start, end);
            
            if (range == null) {
                return new LinkedHashSet<>();
            }
            
            return range.stream()
                    .map(r -> (T) r)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (Exception e) {
            log.error("ZSet范围获取失败: key={}", key, e);
            return new LinkedHashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zRangeByScore(String key, double min, double max) {
        if (!cacheConfig.getEnabled()) {
            return new LinkedHashSet<>();
        }

        try {
            String fullKey = buildFullKey(key);
            Set<Object> range = redisTemplate.opsForZSet().rangeByScore(fullKey, min, max);
            
            if (range == null) {
                return new LinkedHashSet<>();
            }
            
            return range.stream()
                    .map(r -> (T) r)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (Exception e) {
            log.error("ZSet按分数范围获取失败: key={}", key, e);
            return new LinkedHashSet<>();
        }
    }

    @Override
    public Long zRemove(String key, Object... values) {
        if (!cacheConfig.getEnabled() || values == null || values.length == 0) {
            return 0L;
        }

        try {
            String fullKey = buildFullKey(key);
            return redisTemplate.opsForZSet().remove(fullKey, values);
        } catch (Exception e) {
            log.error("ZSet删除失败: key={}", key, e);
            return 0L;
        }
    }

    @Override
    public String buildKey(String... parts) {
        if (parts == null || parts.length == 0) {
            return cacheConfig.getKeyPrefix();
        }
        
        return cacheConfig.getKeyPrefix() + String.join(":", parts);
    }

    /**
     * 构建完整的缓存键
     */
    private String buildFullKey(String key) {
        if (StrUtil.isBlank(key)) {
            return cacheConfig.getKeyPrefix();
        }
        
        // 如果已经包含前缀，直接返回
        if (key.startsWith(cacheConfig.getKeyPrefix())) {
            return key;
        }
        
        return cacheConfig.getKeyPrefix() + key;
    }

    @Override
    public void preloadCache() {
        if (!cacheConfig.getPreloadEnabled()) {
            return;
        }

        log.info("开始缓存预热...");
        
        try {
            // 这里可以预加载热点数据
            // 例如：热门纪念空间、统计数据等
            
            log.info("缓存预热完成");
        } catch (Exception e) {
            log.error("缓存预热失败", e);
        }
    }

    @Override
    public void clearAllCache() {
        if (!cacheConfig.getEnabled()) {
            return;
        }

        try {
            Set<String> keys = redisTemplate.keys(cacheConfig.getKeyPrefix() + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.warn("清空所有缓存成功, count={}", keys.size());
            }
        } catch (Exception e) {
            log.error("清空缓存失败", e);
        }
    }
}
