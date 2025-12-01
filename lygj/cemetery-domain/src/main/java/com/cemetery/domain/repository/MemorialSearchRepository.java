package com.cemetery.domain.repository;

import com.cemetery.domain.document.MemorialDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 纪念空间搜索Repository
 */
@Repository
public interface MemorialSearchRepository extends ElasticsearchRepository<MemorialDocument, Long> {
}
