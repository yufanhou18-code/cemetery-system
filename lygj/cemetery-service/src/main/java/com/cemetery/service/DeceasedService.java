package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.DeceasedInfoDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.vo.DeceasedInfoVO;

import java.util.List;

/**
 * 逝者信息服务接口
 */
public interface DeceasedService {

    /**
     * 创建逝者信息
     * @param deceasedInfoDTO 逝者信息DTO
     * @return 逝者ID
     */
    Long createDeceased(DeceasedInfoDTO deceasedInfoDTO);

    /**
     * 更新逝者信息
     * @param deceasedInfoDTO 逝者信息DTO
     */
    void updateDeceased(DeceasedInfoDTO deceasedInfoDTO);

    /**
     * 删除逝者信息
     * @param deceasedId 逝者ID
     */
    void deleteDeceased(Long deceasedId);

    /**
     * 根据ID查询逝者信息
     * @param deceasedId 逝者ID
     * @return 逝者信息VO
     */
    DeceasedInfoVO getDeceasedById(Long deceasedId);

    /**
     * 根据墓位ID查询逝者列表
     * @param tombId 墓位ID
     * @return 逝者列表
     */
    List<DeceasedInfoVO> getDeceasedByTombId(Long tombId);

    /**
     * 根据姓名模糊查询逝者
     * @param name 逝者姓名
     * @return 逝者列表
     */
    List<DeceasedInfoVO> searchDeceasedByName(String name);

    /**
     * 根据身份证号查询逝者
     * @param idCard 身份证号
     * @return 逝者信息VO
     */
    DeceasedInfoVO getDeceasedByIdCard(String idCard);

    /**
     * 分页查询逝者信息
     * @param pageQueryDTO 分页查询参数
     * @return 逝者分页列表
     */
    Page<DeceasedInfoVO> pageDeceased(PageQueryDTO pageQueryDTO);
}
