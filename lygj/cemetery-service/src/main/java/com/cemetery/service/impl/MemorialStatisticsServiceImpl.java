package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.entity.DigitalMemorial;
import com.cemetery.domain.entity.MemorialContent;
import com.cemetery.domain.entity.MemorialMessage;
import com.cemetery.domain.enums.ContentTypeEnum;
import com.cemetery.domain.enums.MessageTypeEnum;
import com.cemetery.domain.mapper.DigitalMemorialMapper;
import com.cemetery.domain.mapper.MemorialContentMapper;
import com.cemetery.domain.mapper.MemorialMessageMapper;
import com.cemetery.service.MemorialStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计报表服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemorialStatisticsServiceImpl implements MemorialStatisticsService {

    private final DigitalMemorialMapper memorialMapper;
    private final MemorialContentMapper contentMapper;
    private final MemorialMessageMapper messageMapper;

    @Override
    public Map<String, Object> getOverview() {
        log.info("获取整体概况统计");

        Map<String, Object> overview = new HashMap<>();

        // 总纪念空间数
        long totalMemorials = memorialMapper.selectCount(null);
        overview.put("totalMemorials", totalMemorials);

        // 已发布数量
        LambdaQueryWrapper<DigitalMemorial> publishedWrapper = new LambdaQueryWrapper<>();
        publishedWrapper.eq(DigitalMemorial::getIsPublished, 1);
        long publishedMemorials = memorialMapper.selectCount(publishedWrapper);
        overview.put("publishedMemorials", publishedMemorials);

        // 总访问量
        List<DigitalMemorial> memorials = memorialMapper.selectList(null);
        int totalVisits = memorials.stream()
                .mapToInt(m -> m.getVisitCount() != null ? m.getVisitCount() : 0)
                .sum();
        overview.put("totalVisits", totalVisits);

        // 总留言数
        long totalMessages = messageMapper.selectCount(null);
        overview.put("totalMessages", totalMessages);

        // 总内容数
        long totalContents = contentMapper.selectCount(null);
        overview.put("totalContents", totalContents);

        // 总互动数（蜡烛+鲜花+香）
        int totalInteractions = memorials.stream()
                .mapToInt(m -> (m.getCandleCount() != null ? m.getCandleCount() : 0)
                        + (m.getFlowerCount() != null ? m.getFlowerCount() : 0)
                        + (m.getIncenseCount() != null ? m.getIncenseCount() : 0))
                .sum();
        overview.put("totalInteractions", totalInteractions);

        return overview;
    }

    @Override
    public Map<String, Object> getMemorialStatistics(Long memorialId) {
        log.info("获取纪念空间统计, memorialId={}", memorialId);

        DigitalMemorial memorial = memorialMapper.selectById(memorialId);
        if (memorial == null) {
            throw new BusinessException("纪念空间不存在");
        }

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("visitCount", memorial.getVisitCount());
        statistics.put("candleCount", memorial.getCandleCount());
        statistics.put("flowerCount", memorial.getFlowerCount());
        statistics.put("incenseCount", memorial.getIncenseCount());
        statistics.put("messageCount", memorial.getMessageCount());

        // 内容统计
        LambdaQueryWrapper<MemorialContent> contentWrapper = new LambdaQueryWrapper<>();
        contentWrapper.eq(MemorialContent::getMemorialId, memorialId);
        long contentCount = contentMapper.selectCount(contentWrapper);
        statistics.put("contentCount", contentCount);

        // 留言统计
        LambdaQueryWrapper<MemorialMessage> messageWrapper = new LambdaQueryWrapper<>();
        messageWrapper.eq(MemorialMessage::getMemorialId, memorialId);
        long actualMessageCount = messageMapper.selectCount(messageWrapper);
        statistics.put("actualMessageCount", actualMessageCount);

        return statistics;
    }

    @Override
    public List<Map<String, Object>> getVisitTrend(LocalDate startDate, LocalDate endDate, Long memorialId) {
        log.info("获取访问趋势统计, startDate={}, endDate={}, memorialId={}", 
                startDate, endDate, memorialId);

        // 暂时返回模拟数据
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate current = startDate;
        
        while (!current.isAfter(endDate)) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", current.toString());
            dayData.put("visitCount", new Random().nextInt(100));
            trend.add(dayData);
            
            current = current.plusDays(1);
        }

        return trend;
    }

    @Override
    public List<Map<String, Object>> getInteractionTrend(LocalDate startDate, LocalDate endDate, Long memorialId) {
        log.info("获取互动趋势统计, startDate={}, endDate={}, memorialId={}", 
                startDate, endDate, memorialId);

        // 暂时返回模拟数据
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate current = startDate;
        
        while (!current.isAfter(endDate)) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", current.toString());
            dayData.put("candleCount", new Random().nextInt(50));
            dayData.put("flowerCount", new Random().nextInt(30));
            dayData.put("incenseCount", new Random().nextInt(20));
            trend.add(dayData);
            
            current = current.plusDays(1);
        }

        return trend;
    }

    @Override
    public List<Map<String, Object>> getContentTypeDistribution(Long memorialId) {
        log.info("获取内容类型分布统计, memorialId={}", memorialId);

        LambdaQueryWrapper<MemorialContent> wrapper = new LambdaQueryWrapper<>();
        if (memorialId != null) {
            wrapper.eq(MemorialContent::getMemorialId, memorialId);
        }

        List<MemorialContent> contents = contentMapper.selectList(wrapper);
        
        Map<ContentTypeEnum, Long> typeCountMap = contents.stream()
                .collect(Collectors.groupingBy(MemorialContent::getContentType, Collectors.counting()));

        List<Map<String, Object>> distribution = new ArrayList<>();
        
        for (Map.Entry<ContentTypeEnum, Long> entry : typeCountMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("contentType", entry.getKey().getCode());
            item.put("typeName", entry.getKey().getDesc());
            item.put("count", entry.getValue());
            item.put("percentage", entry.getValue() * 100.0 / contents.size());
            distribution.add(item);
        }

        return distribution;
    }

    @Override
    public Map<String, Object> getMessageStatistics(LocalDate startDate, LocalDate endDate, Long memorialId) {
        log.info("获取留言统计, startDate={}, endDate={}, memorialId={}", 
                startDate, endDate, memorialId);

        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        if (memorialId != null) {
            wrapper.eq(MemorialMessage::getMemorialId, memorialId);
        }

        List<MemorialMessage> messages = messageMapper.selectList(wrapper);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCount", messages.size());

        // 按类型统计
        Map<MessageTypeEnum, Long> typeCount = messages.stream()
                .collect(Collectors.groupingBy(MemorialMessage::getMessageType, Collectors.counting()));
        
        // 转换为Integer的Map
        Map<Integer, Long> typeCountMap = new HashMap<>();
        for (Map.Entry<MessageTypeEnum, Long> entry : typeCount.entrySet()) {
            typeCountMap.put(entry.getKey().getCode(), entry.getValue());
        }
        statistics.put("typeCount", typeCountMap);

        return statistics;
    }

    @Override
    public List<Map<String, Object>> getTopMemorials(String orderBy, Integer limit, 
                                                     LocalDate startDate, LocalDate endDate) {
        log.info("获取热门纪念空间排行, orderBy={}, limit={}", orderBy, limit);

        List<DigitalMemorial> memorials = memorialMapper.selectList(null);

        // 根据排序字段排序
        Comparator<DigitalMemorial> comparator;
        switch (orderBy) {
            case "interactionCount":
                comparator = Comparator.comparing(m -> 
                    (m.getCandleCount() != null ? m.getCandleCount() : 0) +
                    (m.getFlowerCount() != null ? m.getFlowerCount() : 0) +
                    (m.getIncenseCount() != null ? m.getIncenseCount() : 0), 
                    Comparator.reverseOrder());
                break;
            case "messageCount":
                comparator = Comparator.comparing(m -> 
                    m.getMessageCount() != null ? m.getMessageCount() : 0, 
                    Comparator.reverseOrder());
                break;
            default: // visitCount
                comparator = Comparator.comparing(m -> 
                    m.getVisitCount() != null ? m.getVisitCount() : 0, 
                    Comparator.reverseOrder());
        }

        return memorials.stream()
                .sorted(comparator)
                .limit(limit)
                .map(m -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", m.getId());
                    item.put("spaceName", m.getSpaceName());
                    item.put("visitCount", m.getVisitCount());
                    item.put("messageCount", m.getMessageCount());
                    item.put("interactionCount", 
                        (m.getCandleCount() != null ? m.getCandleCount() : 0) +
                        (m.getFlowerCount() != null ? m.getFlowerCount() : 0) +
                        (m.getIncenseCount() != null ? m.getIncenseCount() : 0));
                    return item;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getUserActivity(LocalDate startDate, LocalDate endDate) {
        log.info("获取用户活跃度统计, startDate={}, endDate={}", startDate, endDate);

        // 暂时返回模拟数据
        Map<String, Object> activity = new HashMap<>();
        activity.put("activeUsers", 1000);
        activity.put("newUsers", 150);
        activity.put("totalActions", 5000);

        return activity;
    }

    @Override
    public Map<String, Object> getStorageUsage(Long memorialId) {
        log.info("获取存储空间使用统计, memorialId={}", memorialId);

        LambdaQueryWrapper<MemorialContent> wrapper = new LambdaQueryWrapper<>();
        if (memorialId != null) {
            wrapper.eq(MemorialContent::getMemorialId, memorialId);
        }

        List<MemorialContent> contents = contentMapper.selectList(wrapper);

        Map<String, Object> usage = new HashMap<>();
        
        long totalSize = contents.stream()
                .filter(c -> c.getFileSize() != null)
                .mapToLong(MemorialContent::getFileSize)
                .sum();
        usage.put("totalSize", totalSize);

        // 按类型统计大小
        Map<ContentTypeEnum, Long> typeSizeMap = contents.stream()
                .filter(c -> c.getFileSize() != null)
                .collect(Collectors.groupingBy(
                    MemorialContent::getContentType, 
                    Collectors.summingLong(MemorialContent::getFileSize)));
        
        usage.put("imageSize", typeSizeMap.getOrDefault(ContentTypeEnum.IMAGE, 0L));
        usage.put("videoSize", typeSizeMap.getOrDefault(ContentTypeEnum.VIDEO, 0L));
        usage.put("audioSize", typeSizeMap.getOrDefault(ContentTypeEnum.AUDIO, 0L));
        usage.put("documentSize", typeSizeMap.getOrDefault(ContentTypeEnum.DOCUMENT, 0L));

        return usage;
    }

    @Override
    public String exportStatistics(String reportType, LocalDate startDate, LocalDate endDate, Long memorialId) {
        log.info("导出统计报表, reportType={}, startDate={}, endDate={}", 
                reportType, startDate, endDate);

        // 暂时返回模拟URL
        return "http://example.com/export/" + reportType + ".xlsx";
    }

    @Override
    public Map<String, Object> getRealTimeStatistics() {
        log.info("获取实时统计数据");

        Map<String, Object> stats = new HashMap<>();
        stats.put("onlineUsers", new Random().nextInt(100));
        stats.put("todayVisits", new Random().nextInt(1000));
        stats.put("todayMessages", new Random().nextInt(50));

        return stats;
    }

    @Override
    public Map<String, Object> getMemorialReport(Long memorialId, LocalDate startDate, LocalDate endDate) {
        log.info("生成纪念空间数据报告, memorialId={}, startDate={}, endDate={}", 
                memorialId, startDate, endDate);

        Map<String, Object> report = new HashMap<>();
        
        // 基本信息
        report.put("basicInfo", getMemorialStatistics(memorialId));
        
        // 访问趋势
        if (startDate != null && endDate != null) {
            report.put("visitTrend", getVisitTrend(startDate, endDate, memorialId));
            report.put("interactionTrend", getInteractionTrend(startDate, endDate, memorialId));
        }
        
        // 内容分布
        report.put("contentDistribution", getContentTypeDistribution(memorialId));
        
        // 存储使用
        report.put("storageUsage", getStorageUsage(memorialId));

        return report;
    }
}
