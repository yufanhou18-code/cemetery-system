package com.cemetery.service;

import com.cemetery.domain.dto.MemorialSearchDTO;

import java.util.List;
import java.util.Map;

/**
 * 纪念空间搜索服务接口
 */
public interface MemorialSearchService {

    /**
     * 全文搜索
     */
    Object search(MemorialSearchDTO searchDTO);

    /**
     * 搜索纪念空间
     */
    Object searchMemorial(String keyword, Integer page, Integer pageSize, Boolean highlight);

    /**
     * 搜索逝者
     */
    Object searchDeceased(String name, Integer page, Integer pageSize);

    /**
     * 搜索内容
     */
    Object searchContent(String keyword, Integer contentType, Integer page, Integer pageSize);

    /**
     * 搜索留言
     */
    Object searchMessage(String keyword, Integer page, Integer pageSize);

    /**
     * 搜索建议
     */
    List<String> searchSuggest(String keyword, Integer limit);

    /**
     * 获取热门搜索词
     */
    List<Map<String, Object>> getHotKeywords(Integer limit);

    /**
     * 索引纪念空间
     */
    void indexMemorial(Long id);

    /**
     * 删除索引
     */
    void deleteMemorialIndex(Long id);

    /**
     * 重建索引
     */
    void rebuildIndex();

    /**
     * 高级搜索
     */
    Object advancedSearch(String deceasedName, String spaceName, String birthDateStart, 
                         String birthDateEnd, String deathDateStart, String deathDateEnd, 
                         Integer accessPermission, Integer page, Integer pageSize);
}
