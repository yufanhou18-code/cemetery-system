package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemetery.domain.entity.DeceasedInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 逝者信息Mapper接口
 */
@Mapper
public interface DeceasedInfoMapper extends BaseMapper<DeceasedInfo> {

    /**
     * 根据墓位ID查询逝者列表
     * @param tombId 墓位ID
     * @return 逝者列表
     */
    List<DeceasedInfo> selectByTombId(@Param("tombId") Long tombId);

    /**
     * 根据逝者姓名模糊查询
     * @param name 逝者姓名
     * @return 逝者列表
     */
    List<DeceasedInfo> selectByNameLike(@Param("name") String name);

    /**
     * 根据身份证号查询
     * @param idCard 身份证号
     * @return 逝者信息
     */
    DeceasedInfo selectByIdCard(@Param("idCard") String idCard);
}
