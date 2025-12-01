package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemetery.domain.entity.FamilyMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 家属关系Mapper接口
 */
@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {

    /**
     * 根据用户ID查询家属关系列表
     * @param userId 用户ID
     * @return 家属关系列表
     */
    List<FamilyMember> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据逝者ID查询家属列表
     * @param deceasedId 逝者ID
     * @return 家属列表
     */
    List<FamilyMember> selectByDeceasedId(@Param("deceasedId") Long deceasedId);

    /**
     * 查询逝者的主要联系人
     * @param deceasedId 逝者ID
     * @return 主要联系人
     */
    FamilyMember selectPrimaryContact(@Param("deceasedId") Long deceasedId);

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return 家属列表
     */
    List<FamilyMember> selectByPhone(@Param("phone") String phone);
}
