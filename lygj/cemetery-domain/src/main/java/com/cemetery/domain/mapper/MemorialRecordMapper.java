package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemetery.domain.entity.MemorialRecord;
import com.cemetery.domain.enums.MemorialTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 祭扫记录Mapper接口
 */
@Mapper
public interface MemorialRecordMapper extends BaseMapper<MemorialRecord> {

    /**
     * 根据用户ID查询祭扫记录
     * @param userId 用户ID
     * @return 祭扫记录列表
     */
    List<MemorialRecord> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据逝者ID查询祭扫记录
     * @param deceasedId 逝者ID
     * @return 祭扫记录列表
     */
    List<MemorialRecord> selectByDeceasedId(@Param("deceasedId") Long deceasedId);

    /**
     * 根据墓位ID查询祭扫记录
     * @param tombId 墓位ID
     * @return 祭扫记录列表
     */
    List<MemorialRecord> selectByTombId(@Param("tombId") Long tombId);

    /**
     * 根据祭扫类型查询
     * @param memorialType 祭扫类型
     * @return 祭扫记录列表
     */
    List<MemorialRecord> selectByMemorialType(@Param("memorialType") MemorialTypeEnum memorialType);

    /**
     * 查询指定日期范围的祭扫记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 祭扫记录列表
     */
    List<MemorialRecord> selectByDateRange(@Param("startDate") Date startDate, 
                                            @Param("endDate") Date endDate);

    /**
     * 查询用户在指定墓位的祭扫记录
     * @param userId 用户ID
     * @param tombId 墓位ID
     * @return 祭扫记录列表
     */
    List<MemorialRecord> selectByUserAndTomb(@Param("userId") Long userId, 
                                              @Param("tombId") Long tombId);
}
