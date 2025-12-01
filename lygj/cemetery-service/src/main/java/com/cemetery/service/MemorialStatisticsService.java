package com.cemetery.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计报表服务接口
 */
public interface MemorialStatisticsService {

    /**
     * 获取整体概况统计
     */
    Map<String, Object> getOverview();

    /**
     * 获取纪念空间统计
     */
    Map<String, Object> getMemorialStatistics(Long memorialId);

    /**
     * 获取访问趋势统计
     */
    List<Map<String, Object>> getVisitTrend(LocalDate startDate, LocalDate endDate, Long memorialId);

    /**
     * 获取互动趋势统计
     */
    List<Map<String, Object>> getInteractionTrend(LocalDate startDate, LocalDate endDate, Long memorialId);

    /**
     * 获取内容类型分布统计
     */
    List<Map<String, Object>> getContentTypeDistribution(Long memorialId);

    /**
     * 获取留言统计
     */
    Map<String, Object> getMessageStatistics(LocalDate startDate, LocalDate endDate, Long memorialId);

    /**
     * 获取热门纪念空间排行
     */
    List<Map<String, Object>> getTopMemorials(String orderBy, Integer limit, LocalDate startDate, LocalDate endDate);

    /**
     * 获取用户活跃度统计
     */
    Map<String, Object> getUserActivity(LocalDate startDate, LocalDate endDate);

    /**
     * 获取存储空间使用统计
     */
    Map<String, Object> getStorageUsage(Long memorialId);

    /**
     * 导出统计报表
     */
    String exportStatistics(String reportType, LocalDate startDate, LocalDate endDate, Long memorialId);

    /**
     * 获取实时统计数据
     */
    Map<String, Object> getRealTimeStatistics();

    /**
     * 生成纪念空间数据报告
     */
    Map<String, Object> getMemorialReport(Long memorialId, LocalDate startDate, LocalDate endDate);
}
