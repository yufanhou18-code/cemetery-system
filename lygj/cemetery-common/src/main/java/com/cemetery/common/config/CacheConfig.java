package com.cemetery.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "cache.redis")
public class CacheConfig {

    /**
     * 默认过期时间（秒）
     */
    private Long defaultExpire = 3600L;

    /**
     * 热点数据过期时间（秒）
     */
    private Long hotDataExpire = 7200L;

    /**
     * 列表数据过期时间（秒）
     */
    private Long listExpire = 1800L;

    /**
     * 详情数据过期时间（秒）
     */
    private Long detailExpire = 3600L;

    /**
     * 统计数据过期时间（秒）
     */
    private Long statisticsExpire = 300L;

    /**
     * 缓存key前缀
     */
    private String keyPrefix = "cemetery:";

    /**
     * 是否启用缓存
     */
    private Boolean enabled = true;

    /**
     * 是否启用缓存预热
     */
    private Boolean preloadEnabled = true;

    /**
     * 热点数据访问阈值（访问次数）
     */
    private Integer hotDataThreshold = 100;

    /**
     * 是否启用布隆过滤器（防止缓存穿透）
     */
    private Boolean bloomFilterEnabled = true;

    /**
     * 布隆过滤器预期元素数量
     */
    private Long bloomFilterExpectedInsertions = 100000L;

    /**
     * 布隆过滤器误判率
     */
    private Double bloomFilterFpp = 0.01;

    /**
     * 缓存更新策略：lazy(懒加载), active(主动更新)
     */
    private String updateStrategy = "lazy";

    /**
     * 是否启用多级缓存
     */
    private Boolean multiLevelEnabled = false;
}
