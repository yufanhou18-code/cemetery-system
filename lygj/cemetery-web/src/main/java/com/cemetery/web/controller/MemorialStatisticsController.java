package com.cemetery.web.controller;

import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.service.MemorialStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计报表接口
 */
@Slf4j
@RestController
@RequestMapping("/api/memorial/statistics")
@RequiredArgsConstructor
@Api(tags = "统计报表服务")
public class MemorialStatisticsController {

    private final MemorialStatisticsService statisticsService;

    @GetMapping("/overview")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "整体概况统计", notes = "获取纪念空间整体运营数据概况")
    public Result<Map<String, Object>> getOverview() {
        log.info("获取整体概况统计");
        
        Map<String, Object> overview = statisticsService.getOverview();
        return Result.success(overview);
    }

    @GetMapping("/memorial/{id}")
    @ApiOperation(value = "单个纪念空间统计", notes = "获取指定纪念空间的详细统计数据")
    public Result<Map<String, Object>> getMemorialStatistics(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long id) {
        log.info("获取纪念空间统计, memorialId={}", id);
        
        Map<String, Object> statistics = statisticsService.getMemorialStatistics(id);
        return Result.success(statistics);
    }

    @GetMapping("/visit-trend")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "访问趋势统计", notes = "统计指定时间范围内的访问量趋势")
    public Result<List<Map<String, Object>>> getVisitTrend(
            @ApiParam(value = "开始日期", required = true, example = "2024-01-01") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", required = true, example = "2024-01-31") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @ApiParam(value = "纪念空间ID（不传则统计全部）") @RequestParam(required = false) Long memorialId) {
        log.info("获取访问趋势统计, startDate={}, endDate={}, memorialId={}", 
                startDate, endDate, memorialId);
        
        List<Map<String, Object>> visitTrend = statisticsService.getVisitTrend(startDate, endDate, memorialId);
        return Result.success(visitTrend);
    }

    @GetMapping("/interaction-trend")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "互动趋势统计", notes = "统计指定时间范围内的互动（蜡烛/鲜花/香）趋势")
    public Result<List<Map<String, Object>>> getInteractionTrend(
            @ApiParam(value = "开始日期", required = true, example = "2024-01-01") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", required = true, example = "2024-01-31") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @ApiParam(value = "纪念空间ID（不传则统计全部）") @RequestParam(required = false) Long memorialId) {
        log.info("获取互动趋势统计, startDate={}, endDate={}, memorialId={}", 
                startDate, endDate, memorialId);
        
        List<Map<String, Object>> interactionTrend = statisticsService.getInteractionTrend(startDate, endDate, memorialId);
        return Result.success(interactionTrend);
    }

    @GetMapping("/content-type-distribution")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "内容类型分布统计", notes = "统计各类型内容的数量和占比")
    public Result<List<Map<String, Object>>> getContentTypeDistribution(
            @ApiParam(value = "纪念空间ID（不传则统计全部）") @RequestParam(required = false) Long memorialId) {
        log.info("获取内容类型分布统计, memorialId={}", memorialId);
        
        List<Map<String, Object>> distribution = statisticsService.getContentTypeDistribution(memorialId);
        return Result.success(distribution);
    }

    @GetMapping("/message-statistics")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "留言统计", notes = "统计留言的数量和趋势")
    public Result<Map<String, Object>> getMessageStatistics(
            @ApiParam(value = "开始日期", required = true, example = "2024-01-01") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", required = true, example = "2024-01-31") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @ApiParam(value = "纪念空间ID（不传则统计全部）") @RequestParam(required = false) Long memorialId) {
        log.info("获取留言统计, startDate={}, endDate={}, memorialId={}", 
                startDate, endDate, memorialId);
        
        Map<String, Object> messageStatistics = statisticsService.getMessageStatistics(startDate, endDate, memorialId);
        return Result.success(messageStatistics);
    }

    @GetMapping("/top-memorials")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "热门纪念空间排行", notes = "按访问量、互动量等维度排序")
    public Result<List<Map<String, Object>>> getTopMemorials(
            @ApiParam(value = "排序维度：visitCount-访问量，interactionCount-互动量，messageCount-留言数", 
                      defaultValue = "visitCount") 
            @RequestParam(defaultValue = "visitCount") String orderBy,
            @ApiParam(value = "返回数量", defaultValue = "10") 
            @RequestParam(defaultValue = "10") Integer limit,
            @ApiParam(value = "开始日期", example = "2024-01-01") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", example = "2024-01-31") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("获取热门纪念空间排行, orderBy={}, limit={}", orderBy, limit);
        
        List<Map<String, Object>> topMemorials = statisticsService.getTopMemorials(orderBy, limit, startDate, endDate);
        return Result.success(topMemorials);
    }

    @GetMapping("/user-activity")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "用户活跃度统计", notes = "统计用户的活跃度和行为分布")
    public Result<Map<String, Object>> getUserActivity(
            @ApiParam(value = "开始日期", required = true, example = "2024-01-01") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", required = true, example = "2024-01-31") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("获取用户活跃度统计, startDate={}, endDate={}", startDate, endDate);
        
        Map<String, Object> userActivity = statisticsService.getUserActivity(startDate, endDate);
        return Result.success(userActivity);
    }

    @GetMapping("/storage-usage")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "存储空间使用统计", notes = "统计存储空间的使用情况")
    public Result<Map<String, Object>> getStorageUsage(
            @ApiParam(value = "纪念空间ID（不传则统计全部）") @RequestParam(required = false) Long memorialId) {
        log.info("获取存储空间使用统计, memorialId={}", memorialId);
        
        Map<String, Object> storageUsage = statisticsService.getStorageUsage(memorialId);
        return Result.success(storageUsage);
    }

    @GetMapping("/export")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "导出统计报表", notes = "导出Excel格式的统计报表")
    public Result<String> exportStatistics(
            @ApiParam(value = "报表类型：overview-概况，visit-访问，interaction-互动，message-留言", 
                      required = true) 
            @RequestParam String reportType,
            @ApiParam(value = "开始日期", example = "2024-01-01") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", example = "2024-01-31") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @ApiParam(value = "纪念空间ID") @RequestParam(required = false) Long memorialId) {
        log.info("导出统计报表, reportType={}, startDate={}, endDate={}", reportType, startDate, endDate);
        
        String fileUrl = statisticsService.exportStatistics(reportType, startDate, endDate, memorialId);
        return Result.success(fileUrl);
    }

    @GetMapping("/real-time")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "实时统计数据", notes = "获取实时的访问和互动数据")
    public Result<Map<String, Object>> getRealTimeStatistics() {
        log.info("获取实时统计数据");
        
        Map<String, Object> realTimeStats = statisticsService.getRealTimeStatistics();
        return Result.success(realTimeStats);
    }

    @GetMapping("/memorial/{id}/report")
    @ApiOperation(value = "纪念空间数据报告", notes = "生成指定纪念空间的完整数据报告")
    public Result<Map<String, Object>> getMemorialReport(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long id,
            @ApiParam(value = "开始日期", example = "2024-01-01") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", example = "2024-01-31") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("生成纪念空间数据报告, memorialId={}, startDate={}, endDate={}", id, startDate, endDate);
        
        Map<String, Object> report = statisticsService.getMemorialReport(id, startDate, endDate);
        return Result.success(report);
    }
}
