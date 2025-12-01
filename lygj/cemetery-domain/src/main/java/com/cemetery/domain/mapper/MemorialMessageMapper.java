package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.entity.MemorialMessage;
import com.cemetery.domain.enums.AuditStatusEnum;
import com.cemetery.domain.enums.MessageTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 追思留言Mapper接口
 */
@Mapper
public interface MemorialMessageMapper extends BaseMapper<MemorialMessage> {

    /**
     * 根据纪念空间ID查询留言列表（分页）
     * @param page 分页参数
     * @param memorialId 纪念空间ID
     * @param auditStatus 审核状态（可选）
     * @return 分页结果
     */
    IPage<MemorialMessage> selectByMemorialId(
            Page<MemorialMessage> page,
            @Param("memorialId") Long memorialId,
            @Param("auditStatus") AuditStatusEnum auditStatus
    );

    /**
     * 查询置顶留言
     * @param memorialId 纪念空间ID
     * @return 置顶留言列表
     */
    List<MemorialMessage> selectPinnedMessages(@Param("memorialId") Long memorialId);

    /**
     * 根据用户ID查询留言
     * @param page 分页参数
     * @param userId 用户ID
     * @return 分页结果
     */
    IPage<MemorialMessage> selectByUserId(
            Page<MemorialMessage> page,
            @Param("userId") Long userId
    );

    /**
     * 查询子留言（回复）
     * @param parentId 父留言ID
     * @return 子留言列表
     */
    List<MemorialMessage> selectRepliesByParentId(@Param("parentId") Long parentId);

    /**
     * 根据审核状态查询留言
     * @param page 分页参数
     * @param auditStatus 审核状态
     * @return 分页结果
     */
    IPage<MemorialMessage> selectByAuditStatus(
            Page<MemorialMessage> page,
            @Param("auditStatus") AuditStatusEnum auditStatus
    );

    /**
     * 根据留言类型查询
     * @param page 分页参数
     * @param memorialId 纪念空间ID
     * @param messageType 留言类型
     * @return 分页结果
     */
    IPage<MemorialMessage> selectByMessageType(
            Page<MemorialMessage> page,
            @Param("memorialId") Long memorialId,
            @Param("messageType") MessageTypeEnum messageType
    );

    /**
     * 搜索留言（支持内容模糊搜索）
     * @param page 分页参数
     * @param keyword 关键词
     * @param memorialId 纪念空间ID（可选）
     * @param auditStatus 审核状态（可选）
     * @return 分页结果
     */
    IPage<MemorialMessage> searchMessages(
            Page<MemorialMessage> page,
            @Param("keyword") String keyword,
            @Param("memorialId") Long memorialId,
            @Param("auditStatus") AuditStatusEnum auditStatus
    );

    /**
     * 查询热门留言（按点赞数排序）
     * @param page 分页参数
     * @param memorialId 纪念空间ID
     * @param limit 数量限制
     * @return 分页结果
     */
    IPage<MemorialMessage> selectPopularMessages(
            Page<MemorialMessage> page,
            @Param("memorialId") Long memorialId,
            @Param("limit") Integer limit
    );

    /**
     * 查询最新留言
     * @param page 分页参数
     * @param memorialId 纪念空间ID（可选）
     * @param limit 数量限制
     * @return 分页结果
     */
    IPage<MemorialMessage> selectRecentMessages(
            Page<MemorialMessage> page,
            @Param("memorialId") Long memorialId,
            @Param("limit") Integer limit
    );

    /**
     * 更新点赞次数
     * @param id 留言ID
     * @param increment 增量（可正可负）
     * @return 影响行数
     */
    int updateLikeCount(
            @Param("id") Long id,
            @Param("increment") Integer increment
    );

    /**
     * 更新回复次数
     * @param id 留言ID
     * @param increment 增量（可正可负）
     * @return 影响行数
     */
    int updateReplyCount(
            @Param("id") Long id,
            @Param("increment") Integer increment
    );

    /**
     * 统计纪念空间的留言数量
     * @param memorialId 纪念空间ID
     * @param auditStatus 审核状态（可选）
     * @return 数量
     */
    Long countByMemorialId(
            @Param("memorialId") Long memorialId,
            @Param("auditStatus") AuditStatusEnum auditStatus
    );

    /**
     * 统计用户的留言数量
     * @param userId 用户ID
     * @return 数量
     */
    Long countByUserId(@Param("userId") Long userId);

    /**
     * 统计待审核留言数量
     * @return 数量
     */
    Long countPendingAudit();

    /**
     * 统计各留言类型的数量
     * @param memorialId 纪念空间ID（可选）
     * @return 统计结果（key: 留言类型, value: 数量）
     */
    List<Map<String, Object>> countByMessageType(@Param("memorialId") Long memorialId);

    /**
     * 统计留言时间分布
     * @param memorialId 纪念空间ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
    List<Map<String, Object>> countByDateRange(
            @Param("memorialId") Long memorialId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    /**
     * 查询留言详情（包含关联信息）
     * @param id 留言ID
     * @return 留言详情
     */
    MemorialMessage selectMessageWithDetails(@Param("id") Long id);

    /**
     * 批量审核留言
     * @param messageIds 留言ID列表
     * @param auditStatus 审核状态
     * @param auditBy 审核人ID
     * @param auditRemark 审核备注
     * @return 影响行数
     */
    int batchAudit(
            @Param("messageIds") List<Long> messageIds,
            @Param("auditStatus") AuditStatusEnum auditStatus,
            @Param("auditBy") Long auditBy,
            @Param("auditRemark") String auditRemark
    );

    /**
     * 检查用户是否对留言点赞
     * @param messageId 留言ID
     * @param userId 用户ID
     * @return 是否点赞
     */
    Boolean checkUserLiked(
            @Param("messageId") Long messageId,
            @Param("userId") Long userId
    );
}
