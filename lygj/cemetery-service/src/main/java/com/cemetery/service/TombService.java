package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.TombLocationDTO;
import com.cemetery.domain.enums.TombStatusEnum;
import com.cemetery.domain.enums.TombTypeEnum;
import com.cemetery.domain.vo.TombLocationVO;

import java.util.List;

/**
 * 墓位服务接口
 */
public interface TombService {

    /**
     * 创建墓位
     * @param tombLocationDTO 墓位DTO
     * @return 墓位ID
     */
    Long createTomb(TombLocationDTO tombLocationDTO);

    /**
     * 更新墓位
     * @param tombLocationDTO 墓位DTO
     */
    void updateTomb(TombLocationDTO tombLocationDTO);

    /**
     * 删除墓位
     * @param tombId 墓位ID
     */
    void deleteTomb(Long tombId);

    /**
     * 根据ID查询墓位
     * @param tombId 墓位ID
     * @return 墓位VO
     */
    TombLocationVO getTombById(Long tombId);

    /**
     * 根据墓位编号查询
     * @param tombNo 墓位编号
     * @return 墓位VO
     */
    TombLocationVO getTombByTombNo(String tombNo);

    /**
     * 分页查询墓位
     * @param pageQueryDTO 分页查询参数
     * @return 墓位分页列表
     */
    Page<TombLocationVO> pageTombs(PageQueryDTO pageQueryDTO);

    /**
     * 根据区域查询墓位
     * @param areaCode 区域编码
     * @return 墓位列表
     */
    List<TombLocationVO> getTombsByArea(String areaCode);

    /**
     * 根据状态查询墓位
     * @param status 墓位状态
     * @return 墓位列表
     */
    List<TombLocationVO> getTombsByStatus(TombStatusEnum status);

    /**
     * 查询空闲墓位
     * @return 空闲墓位列表
     */
    List<TombLocationVO> getAvailableTombs();

    /**
     * 根据墓位类型查询
     * @param tombType 墓位类型
     * @return 墓位列表
     */
    List<TombLocationVO> getTombsByType(TombTypeEnum tombType);

    /**
     * 根据所有者查询墓位
     * @param ownerId 所有者ID
     * @return 墓位列表
     */
    List<TombLocationVO> getTombsByOwner(Long ownerId);

    /**
     * 修改墓位状态
     * @param tombId 墓位ID
     * @param status 状态
     */
    void changeTombStatus(Long tombId, TombStatusEnum status);

    /**
     * 分配墓位给家属
     * @param tombId 墓位ID
     * @param ownerId 家属ID
     */
    void assignTombToOwner(Long tombId, Long ownerId);
}
