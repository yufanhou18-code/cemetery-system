package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemetery.domain.entity.TombLocation;
import com.cemetery.domain.enums.TombStatusEnum;
import com.cemetery.domain.enums.TombTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 墓位Mapper接口
 */
@Mapper
public interface TombLocationMapper extends BaseMapper<TombLocation> {

    /**
     * 根据墓位编号查询
     * @param tombNo 墓位编号
     * @return 墓位信息
     */
    TombLocation selectByTombNo(@Param("tombNo") String tombNo);

    /**
     * 根据区域编码查询墓位列表
     * @param areaCode 区域编码
     * @return 墓位列表
     */
    List<TombLocation> selectByAreaCode(@Param("areaCode") String areaCode);

    /**
     * 根据墓位状态查询
     * @param status 墓位状态
     * @return 墓位列表
     */
    List<TombLocation> selectByStatus(@Param("status") TombStatusEnum status);

    /**
     * 根据墓位类型查询
     * @param tombType 墓位类型
     * @return 墓位列表
     */
    List<TombLocation> selectByTombType(@Param("tombType") TombTypeEnum tombType);

    /**
     * 根据所有者ID查询墓位列表
     * @param ownerId 所有者ID
     * @return 墓位列表
     */
    List<TombLocation> selectByOwnerId(@Param("ownerId") Long ownerId);

    /**
     * 查询空闲墓位
     * @return 空闲墓位列表
     */
    List<TombLocation> selectAvailableTombs();

    /**
     * 根据区域和状态查询墓位
     * @param areaCode 区域编码
     * @param status 墓位状态
     * @return 墓位列表
     */
    List<TombLocation> selectByAreaAndStatus(@Param("areaCode") String areaCode, 
                                               @Param("status") TombStatusEnum status);
}
