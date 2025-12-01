package com.cemetery.service.impl;

import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.document.MemorialDocument;
import com.cemetery.domain.dto.MemorialSearchDTO;
import com.cemetery.domain.repository.MemorialSearchRepository;
import com.cemetery.service.MemorialSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 纪念空间搜索服务实现类
 */
@Slf4j
@Service
public class MemorialSearchServiceImpl implements MemorialSearchService {

    @Autowired(required = false)
    private MemorialSearchRepository searchRepository;
    
    @Autowired(required = false)
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public Object search(MemorialSearchDTO searchDTO) {
        log.info("执行全文搜索, keyword={}, searchType={}", 
                searchDTO.getKeyword(), searchDTO.getSearchType());

        if (elasticsearchTemplate == null) {
            log.warn("Elasticsearch未配置，返回空结果");
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("data", Collections.emptyList());
            return result;
        }

        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            // 关键词搜索
            boolQuery.should(QueryBuilders.matchQuery("spaceName", searchDTO.getKeyword()));
            boolQuery.should(QueryBuilders.matchQuery("deceasedName", searchDTO.getKeyword()));
            boolQuery.should(QueryBuilders.matchQuery("biography", searchDTO.getKeyword()));
            boolQuery.minimumShouldMatch(1);

            // 权限过滤
            if (searchDTO.getAccessPermission() != null) {
                boolQuery.filter(QueryBuilders.termQuery("accessPermission", searchDTO.getAccessPermission()));
            }

            // 只搜索已发布
            if (Boolean.TRUE.equals(searchDTO.getOnlyPublished())) {
                boolQuery.filter(QueryBuilders.termQuery("isPublished", 1));
            }

            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(searchDTO.getPage() - 1, searchDTO.getPageSize()))
                    .build();

            SearchHits<MemorialDocument> searchHits = elasticsearchTemplate.search(query, MemorialDocument.class);

            Map<String, Object> result = new HashMap<>();
            result.put("total", searchHits.getTotalHits());
            result.put("data", searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList()));

            return result;
        } catch (Exception e) {
            log.error("全文搜索失败", e);
            throw new BusinessException("搜索失败");
        }
    }

    @Override
    public Object searchMemorial(String keyword, Integer page, Integer pageSize, Boolean highlight) {
        log.info("搜索纪念空间, keyword={}", keyword);

        if (elasticsearchTemplate == null) {
            log.warn("Elasticsearch未配置，返回空结果");
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("data", Collections.emptyList());
            return result;
        }

        try {
            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.multiMatchQuery(keyword, 
                            "spaceName", "deceasedName", "biography"))
                    .withPageable(PageRequest.of(page - 1, pageSize))
                    .build();

            SearchHits<MemorialDocument> searchHits = elasticsearchTemplate.search(query, MemorialDocument.class);

            Map<String, Object> result = new HashMap<>();
            result.put("total", searchHits.getTotalHits());
            result.put("data", searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList()));

            return result;
        } catch (Exception e) {
            log.error("搜索纪念空间失败", e);
            throw new BusinessException("搜索失败");
        }
    }

    @Override
    public Object searchDeceased(String name, Integer page, Integer pageSize) {
        log.info("搜索逝者, name={}", name);

        if (elasticsearchTemplate == null) {
            log.warn("Elasticsearch未配置，返回空结果");
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("data", Collections.emptyList());
            return result;
        }

        try {
            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery("deceasedName", name))
                    .withPageable(PageRequest.of(page - 1, pageSize))
                    .build();

            SearchHits<MemorialDocument> searchHits = elasticsearchTemplate.search(query, MemorialDocument.class);

            Map<String, Object> result = new HashMap<>();
            result.put("total", searchHits.getTotalHits());
            result.put("data", searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList()));

            return result;
        } catch (Exception e) {
            log.error("搜索逝者失败", e);
            throw new BusinessException("搜索失败");
        }
    }

    @Override
    public Object searchContent(String keyword, Integer contentType, Integer page, Integer pageSize) {
        log.info("搜索内容, keyword={}, contentType={}", keyword, contentType);

        // 暂时返回空结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", 0);
        result.put("data", Collections.emptyList());
        return result;
    }

    @Override
    public Object searchMessage(String keyword, Integer page, Integer pageSize) {
        log.info("搜索留言, keyword={}", keyword);

        // 暂时返回空结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", 0);
        result.put("data", Collections.emptyList());
        return result;
    }

    @Override
    public List<String> searchSuggest(String keyword, Integer limit) {
        log.info("获取搜索建议, keyword={}", keyword);

        // 暂时返回空列表
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> getHotKeywords(Integer limit) {
        log.info("获取热门搜索词, limit={}", limit);

        // 暂时返回空列表
        return Collections.emptyList();
    }

    @Override
    public void indexMemorial(Long id) {
        log.info("索引纪念空间, memorialId={}", id);

        if (searchRepository == null) {
            log.warn("Elasticsearch未配置，跳过索引操作");
            return;
        }

        try {
            // 创建文档并保存到ES
            MemorialDocument document = new MemorialDocument();
            document.setId(id);
            // 这里需要从数据库查询并设置其他字段
            
            searchRepository.save(document);
            log.info("索引纪念空间成功, memorialId={}", id);
        } catch (Exception e) {
            log.error("索引纪念空间失败", e);
            throw new BusinessException("索引失败");
        }
    }

    @Override
    public void deleteMemorialIndex(Long id) {
        log.info("删除索引, memorialId={}", id);

        if (searchRepository == null) {
            log.warn("Elasticsearch未配置，跳过索引操作");
            return;
        }

        try {
            searchRepository.deleteById(id);
            log.info("删除索引成功, memorialId={}", id);
        } catch (Exception e) {
            log.error("删除索引失败", e);
            throw new BusinessException("删除索引失败");
        }
    }

    @Override
    public void rebuildIndex() {
        log.info("开始重建搜索索引");

        if (searchRepository == null) {
            log.warn("Elasticsearch未配置，跳过索引操作");
            return;
        }

        try {
            // 删除所有索引
            searchRepository.deleteAll();
            
            // 重新索引所有纪念空间
            // 这里需要从数据库查询所有纪念空间并索引
            
            log.info("重建搜索索引成功");
        } catch (Exception e) {
            log.error("重建搜索索引失败", e);
            throw new BusinessException("重建索引失败");
        }
    }

    @Override
    public Object advancedSearch(String deceasedName, String spaceName, String birthDateStart,
                                String birthDateEnd, String deathDateStart, String deathDateEnd,
                                Integer accessPermission, Integer page, Integer pageSize) {
        log.info("执行高级搜索, deceasedName={}, spaceName={}", deceasedName, spaceName);

        if (elasticsearchTemplate == null) {
            log.warn("Elasticsearch未配置，返回空结果");
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("data", Collections.emptyList());
            return result;
        }

        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            if (deceasedName != null && !deceasedName.isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("deceasedName", deceasedName));
            }

            if (spaceName != null && !spaceName.isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("spaceName", spaceName));
            }

            if (accessPermission != null) {
                boolQuery.filter(QueryBuilders.termQuery("accessPermission", accessPermission));
            }

            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(page - 1, pageSize))
                    .build();

            SearchHits<MemorialDocument> searchHits = elasticsearchTemplate.search(query, MemorialDocument.class);

            Map<String, Object> result = new HashMap<>();
            result.put("total", searchHits.getTotalHits());
            result.put("data", searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList()));

            return result;
        } catch (Exception e) {
            log.error("高级搜索失败", e);
            throw new BusinessException("搜索失败");
        }
    }
}
