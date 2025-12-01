package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.MemorialContentDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.vo.MemorialContentVO;

import java.util.List;

/**
 * 纪念内容服务接口
 */
public interface MemorialContentService {

    /**
     * 添加纪念内容
     */
    Long addContent(MemorialContentDTO contentDTO);

    /**
     * 更新纪念内容
     */
    void updateContent(MemorialContentDTO contentDTO);

    /**
     * 删除纪念内容
     */
    void deleteContent(Long id);

    /**
     * 获取内容详情
     */
    MemorialContentVO getContentById(Long id);

    /**
     * 获取纪念空间的所有内容
     */
    List<MemorialContentVO> getMemorialContents(Long memorialId, Integer contentType);

    /**
     * 分页查询纪念空间内容
     */
    Page<MemorialContentVO> pageMemorialContents(Long memorialId, Integer contentType, PageQueryDTO pageQueryDTO);

    /**
     * 获取精选内容
     */
    List<MemorialContentVO> getFeaturedContents(Long memorialId, Integer limit);

    /**
     * 设置为精选内容
     */
    void featureContent(Long id);

    /**
     * 取消精选内容
     */
    void unfeatureContent(Long id);

    /**
     * 调整内容排序
     */
    void updateSortOrder(Long id, Integer sortOrder);

    /**
     * 获取内容统计信息
     */
    Object getContentStatistics(Long memorialId);

    /**
     * 批量删除内容
     */
    void batchDeleteContents(List<Long> ids);
}
