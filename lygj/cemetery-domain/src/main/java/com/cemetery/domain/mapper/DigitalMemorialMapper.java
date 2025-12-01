package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.entity.DigitalMemorial;
import com.cemetery.domain.enums.AccessPermissionEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数字纪念空间Mapper接口
 */
@Mapper
public interface DigitalMemorialMapper extends BaseMapper<DigitalMemorial> {

    /**
     * 根据纪念空间编号查询
     * @param spaceNo 纪念空间编号
     * @return 纪念空间信息
     */
    DigitalMemorial selectBySpaceNo(@Param("spaceNo") String spaceNo);

    /**
     * 根据逝者ID查询纪念空间
     * @param deceasedId 逝者ID
     * @return 纪念空间信息
     */
    DigitalMemorial selectByDeceasedId(@Param("deceasedId") Long deceasedId);

    /**
     * 根据墓位ID查询纪念空间
     * @param tombId 墓位ID
     * @return 纪念空间信息
     */
    DigitalMemorial selectByTombId(@Param("tombId") Long tombId);

    /**
     * 查询已发布的纪念空间列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DigitalMemorial> selectPublishedMemorials(Page<DigitalMemorial> page);

    /**
     * 根据访问权限查询
     * @param accessPermission 访问权限
     * @return 纪念空间列表
     */
    List<DigitalMemorial> selectByAccessPermission(@Param("accessPermission") AccessPermissionEnum accessPermission);

    /**
     * 搜索纪念空间（支持模糊搜索）
     * @param page 分页参数
     * @param keyword 关键词（搜索空间名称、生平介绍等）
     * @param isPublished 是否已发布
     * @return 分页结果
     */
    IPage<DigitalMemorial> searchMemorials(
            Page<DigitalMemorial> page,
            @Param("keyword") String keyword,
            @Param("isPublished") Integer isPublished
    );

    /**
     * 查询热门纪念空间（按访问量排序）
     * @param page 分页参数
     * @param limit 数量限制
     * @return 分页结果
     */
    IPage<DigitalMemorial> selectPopularMemorials(
            Page<DigitalMemorial> page,
            @Param("limit") Integer limit
    );

    /**
     * 查询即将过期的纪念空间
     * @param days 多少天内过期
     * @return 纪念空间列表
     */
    List<DigitalMemorial> selectExpiringMemorials(@Param("days") Integer days);

    /**
     * 更新访问次数
     * @param id 纪念空间ID
     * @return 影响行数
     */
    int incrementVisitCount(@Param("id") Long id);

    /**
     * 更新点烛次数
     * @param id 纪念空间ID
     * @return 影响行数
     */
    int incrementCandleCount(@Param("id") Long id);

    /**
     * 更新献花次数
     * @param id 纪念空间ID
     * @return 影响行数
     */
    int incrementFlowerCount(@Param("id") Long id);

    /**
     * 更新上香次数
     * @param id 纪念空间ID
     * @return 影响行数
     */
    int incrementIncenseCount(@Param("id") Long id);

    /**
     * 更新留言次数
     * @param id 纪念空间ID
     * @return 影响行数
     */
    int incrementMessageCount(@Param("id") Long id);

    /**
     * 统计纪念空间总数
     * @param isPublished 是否已发布
     * @return 总数
     */
    Long countMemorials(@Param("isPublished") Integer isPublished);

    /**
     * 统计各访问权限的纪念空间数量
     * @return 统计结果（key: 权限类型, value: 数量）
     */
    List<Map<String, Object>> countByAccessPermission();

    /**
     * 查询纪念空间统计信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
    Map<String, Object> getMemorialStatistics(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    /**
     * 查询纪念空间详情（包含关联信息）
     * @param id 纪念空间ID
     * @return 纪念空间详情
     */
    DigitalMemorial selectMemorialWithDetails(@Param("id") Long id);
}
