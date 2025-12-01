package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.entity.MemorialContent;
import com.cemetery.domain.enums.AuditStatusEnum;
import com.cemetery.domain.enums.ContentTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 纪念内容Mapper接口
 */
@Mapper
public interface MemorialContentMapper extends BaseMapper<MemorialContent> {

    /**
     * 根据纪念空间ID查询内容列表
     * @param memorialId 纪念空间ID
     * @return 内容列表
     */
    List<MemorialContent> selectByMemorialId(@Param("memorialId") Long memorialId);

    /**
     * 根据纪念空间ID和内容类型查询
     * @param memorialId 纪念空间ID
     * @param contentType 内容类型
     * @return 内容列表
     */
    List<MemorialContent> selectByMemorialIdAndType(
            @Param("memorialId") Long memorialId,
            @Param("contentType") ContentTypeEnum contentType
    );

    /**
     * 查询纪念空间的审核通过内容（按排序）
     * @param memorialId 纪念空间ID
     * @return 内容列表
     */
    List<MemorialContent> selectApprovedContentsByMemorialId(@Param("memorialId") Long memorialId);

    /**
     * 查询精选内容
     * @param page 分页参数
     * @param contentType 内容类型（可选）
     * @return 分页结果
     */
    IPage<MemorialContent> selectFeaturedContents(
            Page<MemorialContent> page,
            @Param("contentType") ContentTypeEnum contentType
    );

    /**
     * 根据审核状态查询内容
     * @param page 分页参数
     * @param auditStatus 审核状态
     * @return 分页结果
     */
    IPage<MemorialContent> selectByAuditStatus(
            Page<MemorialContent> page,
            @Param("auditStatus") AuditStatusEnum auditStatus
    );

    /**
     * 根据上传用户ID查询内容
     * @param page 分页参数
     * @param uploadBy 上传用户ID
     * @return 分页结果
     */
    IPage<MemorialContent> selectByUploadBy(
            Page<MemorialContent> page,
            @Param("uploadBy") Long uploadBy
    );

    /**
     * 搜索内容（支持标题、描述模糊搜索）
     * @param page 分页参数
     * @param keyword 关键词
     * @param contentType 内容类型（可选）
     * @param auditStatus 审核状态（可选）
     * @return 分页结果
     */
    IPage<MemorialContent> searchContents(
            Page<MemorialContent> page,
            @Param("keyword") String keyword,
            @Param("contentType") ContentTypeEnum contentType,
            @Param("auditStatus") AuditStatusEnum auditStatus
    );

    /**
     * 更新查看次数
     * @param id 内容ID
     * @return 影响行数
     */
    int incrementViewCount(@Param("id") Long id);

    /**
     * 批量更新排序
     * @param contentIds 内容ID列表
     * @param sortOrders 排序序号列表
     * @return 影响行数
     */
    int batchUpdateSortOrder(
            @Param("contentIds") List<Long> contentIds,
            @Param("sortOrders") List<Integer> sortOrders
    );

    /**
     * 统计纪念空间的内容数量
     * @param memorialId 纪念空间ID
     * @param contentType 内容类型（可选）
     * @return 数量
     */
    Long countByMemorialId(
            @Param("memorialId") Long memorialId,
            @Param("contentType") ContentTypeEnum contentType
    );

    /**
     * 统计各内容类型的数量
     * @param memorialId 纪念空间ID（可选）
     * @return 统计结果（key: 内容类型, value: 数量）
     */
    List<Map<String, Object>> countByContentType(@Param("memorialId") Long memorialId);

    /**
     * 统计待审核内容数量
     * @return 数量
     */
    Long countPendingAudit();

    /**
     * 查询最新上传的内容
     * @param page 分页参数
     * @param limit 数量限制
     * @return 分页结果
     */
    IPage<MemorialContent> selectRecentContents(
            Page<MemorialContent> page,
            @Param("limit") Integer limit
    );

    /**
     * 查询热门内容（按查看次数排序）
     * @param page 分页参数
     * @param contentType 内容类型（可选）
     * @return 分页结果
     */
    IPage<MemorialContent> selectPopularContents(
            Page<MemorialContent> page,
            @Param("contentType") ContentTypeEnum contentType
    );

    /**
     * 查询内容详情（包含关联信息）
     * @param id 内容ID
     * @return 内容详情
     */
    MemorialContent selectContentWithDetails(@Param("id") Long id);
}
