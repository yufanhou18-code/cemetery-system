package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.DigitalMemorialDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.vo.DigitalMemorialVO;

import java.util.List;
import java.util.Map;

/**
 * 纪念空间管理服务接口
 */
public interface MemorialService {

    /**
     * 创建纪念空间并初始化基础内容
     * @param memorialDTO 纪念空间DTO
     * @return 纪念空间ID
     */
    Long createMemorial(DigitalMemorialDTO memorialDTO);

    /**
     * 更新纪念空间信息和权限设置
     * @param memorialDTO 纪念空间DTO
     */
    void updateMemorial(DigitalMemorialDTO memorialDTO);

    /**
     * 获取纪念空间完整详情
     * @param memorialId 纪念空间ID
     * @return 纪念空间详情
     */
    DigitalMemorialVO getMemorialDetail(Long memorialId);

    /**
     * 软删除纪念空间
     * @param memorialId 纪念空间ID
     */
    void deleteMemorial(Long memorialId);

    /**
     * 根据纪念空间编号查询
     * @param spaceNo 纪念空间编号
     * @return 纪念空间信息
     */
    DigitalMemorialVO getMemorialBySpaceNo(String spaceNo);

    /**
     * 根据逝者ID查询纪念空间
     * @param deceasedId 逝者ID
     * @return 纪念空间信息
     */
    DigitalMemorialVO getMemorialByDeceasedId(Long deceasedId);

    /**
     * 根据墓位ID查询纪念空间
     * @param tombId 墓位ID
     * @return 纪念空间信息
     */
    DigitalMemorialVO getMemorialByTombId(Long tombId);

    /**
     * 分页查询已发布的纪念空间列表
     * @param pageQueryDTO 分页参数
     * @return 分页结果
     */
    Page<DigitalMemorialVO> pagePublishedMemorials(PageQueryDTO pageQueryDTO);

    /**
     * 发布纪念空间
     * @param memorialId 纪念空间ID
     */
    void publishMemorial(Long memorialId);

    /**
     * 取消发布纪念空间
     * @param memorialId 纪念空间ID
     */
    void unpublishMemorial(Long memorialId);

    /**
     * 更新纪念空间访问次数
     * @param memorialId 纪念空间ID
     */
    void incrementVisitCount(Long memorialId);

    /**
     * 更新点烛次数
     * @param memorialId 纪念空间ID
     */
    void incrementCandleCount(Long memorialId);

    /**
     * 更新献花次数
     * @param memorialId 纪念空间ID
     */
    void incrementFlowerCount(Long memorialId);

    /**
     * 更新上香次数
     * @param memorialId 纪念空间ID
     */
    void incrementIncenseCount(Long memorialId);

    /**
     * 查询热门纪念空间
     * @param limit 数量限制
     * @return 纪念空间列表
     */
    List<DigitalMemorialVO> getPopularMemorials(Integer limit);

    /**
     * 查询即将过期的纪念空间
     * @param days 多少天内过期
     * @return 纪念空间列表
     */
    List<DigitalMemorialVO> getExpiringMemorials(Integer days);

    /**
     * 验证访问权限
     * @param memorialId 纪念空间ID
     * @param password 访问密码（可选）
     * @return 是否有权限访问
     */
    boolean verifyAccess(Long memorialId, String password);

    /**
     * 获取纪念空间统计信息
     * @return 统计数据
     */
    Map<String, Object> getMemorialStatistics();
}
