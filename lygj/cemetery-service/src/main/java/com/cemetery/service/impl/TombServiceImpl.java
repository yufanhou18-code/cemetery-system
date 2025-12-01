package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.TombLocationDTO;
import com.cemetery.domain.entity.TombLocation;
import com.cemetery.domain.enums.TombStatusEnum;
import com.cemetery.domain.enums.TombTypeEnum;
import com.cemetery.domain.mapper.TombLocationMapper;
import com.cemetery.domain.vo.TombLocationVO;
import com.cemetery.service.TombService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 墓位服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TombServiceImpl implements TombService {

    private final TombLocationMapper tombLocationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTomb(TombLocationDTO tombLocationDTO) {
        // 校验墓位编号唯一性
        if (StringUtils.isNotBlank(tombLocationDTO.getTombNo())) {
            TombLocation existTomb = tombLocationMapper.selectByTombNo(tombLocationDTO.getTombNo());
            if (existTomb != null) {
                throw new BusinessException("墓位编号已存在");
            }
        }

        // 转换并保存
        TombLocation tombLocation = EntityConverter.INSTANCE.toTombLocation(tombLocationDTO);
        tombLocationMapper.insert(tombLocation);
        log.info("创建墓位成功, tombId={}, tombNo={}", tombLocation.getId(), tombLocation.getTombNo());
        return tombLocation.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTomb(TombLocationDTO tombLocationDTO) {
        if (tombLocationDTO.getId() == null) {
            throw new BusinessException("墓位ID不能为空");
        }

        TombLocation existTomb = tombLocationMapper.selectById(tombLocationDTO.getId());
        if (existTomb == null) {
            throw new BusinessException("墓位不存在");
        }

        // 校验墓位编号唯一性（排除自己）
        if (StringUtils.isNotBlank(tombLocationDTO.getTombNo()) 
                && !tombLocationDTO.getTombNo().equals(existTomb.getTombNo())) {
            TombLocation tomb = tombLocationMapper.selectByTombNo(tombLocationDTO.getTombNo());
            if (tomb != null) {
                throw new BusinessException("墓位编号已存在");
            }
        }

        // 转换并更新
        TombLocation tombLocation = EntityConverter.INSTANCE.toTombLocation(tombLocationDTO);
        tombLocationMapper.updateById(tombLocation);
        log.info("更新墓位成功, tombId={}", tombLocation.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTomb(Long tombId) {
        if (tombId == null) {
            throw new BusinessException("墓位ID不能为空");
        }

        TombLocation tombLocation = tombLocationMapper.selectById(tombId);
        if (tombLocation == null) {
            throw new BusinessException("墓位不存在");
        }

        // 已售墓位不能删除
        if (TombStatusEnum.SOLD.equals(tombLocation.getStatus())) {
            throw new BusinessException("已售墓位不能删除");
        }

        // 逻辑删除
        tombLocationMapper.deleteById(tombId);
        log.info("删除墓位成功, tombId={}", tombId);
    }

    @Override
    public TombLocationVO getTombById(Long tombId) {
        if (tombId == null) {
            throw new BusinessException("墓位ID不能为空");
        }

        TombLocation tombLocation = tombLocationMapper.selectById(tombId);
        if (tombLocation == null) {
            throw new BusinessException("墓位不存在");
        }

        return EntityConverter.INSTANCE.toTombLocationVO(tombLocation);
    }

    @Override
    public TombLocationVO getTombByTombNo(String tombNo) {
        if (StringUtils.isBlank(tombNo)) {
            throw new BusinessException("墓位编号不能为空");
        }

        TombLocation tombLocation = tombLocationMapper.selectByTombNo(tombNo);
        return tombLocation != null ? EntityConverter.INSTANCE.toTombLocationVO(tombLocation) : null;
    }

    @Override
    public Page<TombLocationVO> pageTombs(PageQueryDTO pageQueryDTO) {
        Page<TombLocation> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        LambdaQueryWrapper<TombLocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(TombLocation::getAreaCode, TombLocation::getRowNum, TombLocation::getColNum);
        
        Page<TombLocation> tombPage = tombLocationMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<TombLocationVO> voPage = new Page<>(tombPage.getCurrent(), tombPage.getSize(), tombPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toTombLocationVOList(tombPage.getRecords()));
        
        return voPage;
    }

    @Override
    public List<TombLocationVO> getTombsByArea(String areaCode) {
        if (StringUtils.isBlank(areaCode)) {
            throw new BusinessException("区域编码不能为空");
        }

        List<TombLocation> tombList = tombLocationMapper.selectByAreaCode(areaCode);
        return EntityConverter.INSTANCE.toTombLocationVOList(tombList);
    }

    @Override
    public List<TombLocationVO> getTombsByStatus(TombStatusEnum status) {
        if (status == null) {
            throw new BusinessException("墓位状态不能为空");
        }

        List<TombLocation> tombList = tombLocationMapper.selectByStatus(status);
        return EntityConverter.INSTANCE.toTombLocationVOList(tombList);
    }

    @Override
    public List<TombLocationVO> getAvailableTombs() {
        List<TombLocation> tombList = tombLocationMapper.selectAvailableTombs();
        return EntityConverter.INSTANCE.toTombLocationVOList(tombList);
    }

    @Override
    public List<TombLocationVO> getTombsByType(TombTypeEnum tombType) {
        if (tombType == null) {
            throw new BusinessException("墓位类型不能为空");
        }

        List<TombLocation> tombList = tombLocationMapper.selectByTombType(tombType);
        return EntityConverter.INSTANCE.toTombLocationVOList(tombList);
    }

    @Override
    public List<TombLocationVO> getTombsByOwner(Long ownerId) {
        if (ownerId == null) {
            throw new BusinessException("所有者ID不能为空");
        }

        List<TombLocation> tombList = tombLocationMapper.selectByOwnerId(ownerId);
        return EntityConverter.INSTANCE.toTombLocationVOList(tombList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeTombStatus(Long tombId, TombStatusEnum status) {
        if (tombId == null) {
            throw new BusinessException("墓位ID不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }

        TombLocation tombLocation = tombLocationMapper.selectById(tombId);
        if (tombLocation == null) {
            throw new BusinessException("墓位不存在");
        }

        tombLocation.setStatus(status);
        tombLocationMapper.updateById(tombLocation);
        log.info("修改墓位状态成功, tombId={}, status={}", tombId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTombToOwner(Long tombId, Long ownerId) {
        if (tombId == null) {
            throw new BusinessException("墓位ID不能为空");
        }
        if (ownerId == null) {
            throw new BusinessException("所有者ID不能为空");
        }

        TombLocation tombLocation = tombLocationMapper.selectById(tombId);
        if (tombLocation == null) {
            throw new BusinessException("墓位不存在");
        }

        // 只有空闲墓位可以分配
        if (!TombStatusEnum.AVAILABLE.equals(tombLocation.getStatus())) {
            throw new BusinessException("只有空闲墓位可以分配");
        }

        tombLocation.setOwnerId(ownerId);
        tombLocation.setStatus(TombStatusEnum.SOLD);
        tombLocationMapper.updateById(tombLocation);
        log.info("分配墓位成功, tombId={}, ownerId={}", tombId, ownerId);
    }
}
