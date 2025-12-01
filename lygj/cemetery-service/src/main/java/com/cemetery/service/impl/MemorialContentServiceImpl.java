package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.dto.MemorialContentDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.entity.MemorialContent;
import com.cemetery.domain.enums.ContentTypeEnum;
import com.cemetery.domain.mapper.MemorialContentMapper;
import com.cemetery.domain.vo.MemorialContentVO;
import com.cemetery.service.MemorialContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 纪念内容服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemorialContentServiceImpl implements MemorialContentService {

    private final MemorialContentMapper contentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addContent(MemorialContentDTO contentDTO) {
        log.info("添加纪念内容, memorialId={}, contentType={}", 
                contentDTO.getMemorialId(), contentDTO.getContentType());

        MemorialContent content = new MemorialContent();
        BeanUtils.copyProperties(contentDTO, content);
        
        // 设置默认值
        if (content.getSortOrder() == null) {
            content.setSortOrder(0);
        }
        if (content.getIsFeatured() == null) {
            content.setIsFeatured(0);
        }

        contentMapper.insert(content);
        log.info("添加纪念内容成功, contentId={}", content.getId());
        
        return content.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContent(MemorialContentDTO contentDTO) {
        if (contentDTO.getId() == null) {
            throw new BusinessException("内容ID不能为空");
        }

        log.info("更新纪念内容, contentId={}", contentDTO.getId());

        MemorialContent existContent = contentMapper.selectById(contentDTO.getId());
        if (existContent == null) {
            throw new BusinessException("内容不存在");
        }

        MemorialContent content = new MemorialContent();
        BeanUtils.copyProperties(contentDTO, content);

        contentMapper.updateById(content);
        log.info("更新纪念内容成功, contentId={}", content.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContent(Long id) {
        if (id == null) {
            throw new BusinessException("内容ID不能为空");
        }

        log.info("删除纪念内容, contentId={}", id);

        MemorialContent content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException("内容不存在");
        }

        contentMapper.deleteById(id);
        log.info("删除纪念内容成功, contentId={}", id);
    }

    @Override
    public MemorialContentVO getContentById(Long id) {
        if (id == null) {
            throw new BusinessException("内容ID不能为空");
        }

        MemorialContent content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException("内容不存在");
        }

        return convertToVO(content);
    }

    @Override
    public List<MemorialContentVO> getMemorialContents(Long memorialId, Integer contentType) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        LambdaQueryWrapper<MemorialContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialContent::getMemorialId, memorialId);
        
        if (contentType != null) {
            wrapper.eq(MemorialContent::getContentType, contentType);
        }
        
        wrapper.orderByAsc(MemorialContent::getSortOrder);
        wrapper.orderByDesc(MemorialContent::getCreateTime);

        List<MemorialContent> contents = contentMapper.selectList(wrapper);
        return contents.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Page<MemorialContentVO> pageMemorialContents(Long memorialId, Integer contentType, PageQueryDTO pageQueryDTO) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        Page<MemorialContent> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        LambdaQueryWrapper<MemorialContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialContent::getMemorialId, memorialId);
        
        if (contentType != null) {
            wrapper.eq(MemorialContent::getContentType, contentType);
        }
        
        wrapper.orderByAsc(MemorialContent::getSortOrder);
        wrapper.orderByDesc(MemorialContent::getCreateTime);

        Page<MemorialContent> contentPage = contentMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<MemorialContentVO> voPage = new Page<>(contentPage.getCurrent(), 
                contentPage.getSize(), contentPage.getTotal());
        voPage.setRecords(contentPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        
        return voPage;
    }

    @Override
    public List<MemorialContentVO> getFeaturedContents(Long memorialId, Integer limit) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        LambdaQueryWrapper<MemorialContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialContent::getMemorialId, memorialId);
        wrapper.eq(MemorialContent::getIsFeatured, 1);
        wrapper.orderByAsc(MemorialContent::getSortOrder);
        wrapper.orderByDesc(MemorialContent::getCreateTime);
        wrapper.last("LIMIT " + limit);

        List<MemorialContent> contents = contentMapper.selectList(wrapper);
        return contents.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void featureContent(Long id) {
        if (id == null) {
            throw new BusinessException("内容ID不能为空");
        }

        log.info("设置精选内容, contentId={}", id);

        MemorialContent content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException("内容不存在");
        }

        content.setIsFeatured(1);
        contentMapper.updateById(content);
        
        log.info("设置精选内容成功, contentId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfeatureContent(Long id) {
        if (id == null) {
            throw new BusinessException("内容ID不能为空");
        }

        log.info("取消精选内容, contentId={}", id);

        MemorialContent content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException("内容不存在");
        }

        content.setIsFeatured(0);
        contentMapper.updateById(content);
        
        log.info("取消精选内容成功, contentId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSortOrder(Long id, Integer sortOrder) {
        if (id == null) {
            throw new BusinessException("内容ID不能为空");
        }

        log.info("调整内容排序, contentId={}, sortOrder={}", id, sortOrder);

        MemorialContent content = contentMapper.selectById(id);
        if (content == null) {
            throw new BusinessException("内容不存在");
        }

        content.setSortOrder(sortOrder);
        contentMapper.updateById(content);
        
        log.info("调整内容排序成功, contentId={}", id);
    }

    @Override
    public Object getContentStatistics(Long memorialId) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        LambdaQueryWrapper<MemorialContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialContent::getMemorialId, memorialId);
        
        List<MemorialContent> contents = contentMapper.selectList(wrapper);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCount", contents.size());
        
        // 按类型统计
        Map<ContentTypeEnum, Long> typeCount = contents.stream()
                .collect(Collectors.groupingBy(MemorialContent::getContentType, Collectors.counting()));
        
        // 转换为Integer的Map
        Map<Integer, Long> typeCountMap = new HashMap<>();
        for (Map.Entry<ContentTypeEnum, Long> entry : typeCount.entrySet()) {
            typeCountMap.put(entry.getKey().getCode(), entry.getValue());
        }
        statistics.put("typeCount", typeCountMap);
        
        // 总大小
        long totalSize = contents.stream()
                .filter(c -> c.getFileSize() != null)
                .mapToLong(MemorialContent::getFileSize)
                .sum();
        statistics.put("totalSize", totalSize);
        
        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteContents(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("内容ID列表不能为空");
        }

        log.info("批量删除内容, ids={}", ids);
        
        contentMapper.deleteBatchIds(ids);
        
        log.info("批量删除内容成功, count={}", ids.size());
    }

    /**
     * 转换为VO
     */
    private MemorialContentVO convertToVO(MemorialContent content) {
        MemorialContentVO vo = new MemorialContentVO();
        BeanUtils.copyProperties(content, vo);
        return vo;
    }
}
