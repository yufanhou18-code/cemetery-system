package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.DeceasedInfoDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.entity.DeceasedInfo;
import com.cemetery.domain.entity.TombLocation;
import com.cemetery.domain.mapper.DeceasedInfoMapper;
import com.cemetery.domain.mapper.TombLocationMapper;
import com.cemetery.domain.vo.DeceasedInfoVO;
import com.cemetery.service.DeceasedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 逝者信息服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeceasedServiceImpl implements DeceasedService {

    private final DeceasedInfoMapper deceasedInfoMapper;
    private final TombLocationMapper tombLocationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDeceased(DeceasedInfoDTO deceasedInfoDTO) {
        // 验证墓位是否存在
        if (deceasedInfoDTO.getTombId() != null) {
            TombLocation tombLocation = tombLocationMapper.selectById(deceasedInfoDTO.getTombId());
            if (tombLocation == null) {
                throw new BusinessException("墓位不存在");
            }
        }

        // 验证身份证号唯一性
        if (StringUtils.isNotBlank(deceasedInfoDTO.getIdCard())) {
            DeceasedInfo exist = deceasedInfoMapper.selectByIdCard(deceasedInfoDTO.getIdCard());
            if (exist != null) {
                throw new BusinessException("身份证号已存在");
            }
        }

        // 转换并保存
        DeceasedInfo deceasedInfo = EntityConverter.INSTANCE.toDeceasedInfo(deceasedInfoDTO);
        deceasedInfoMapper.insert(deceasedInfo);
        log.info("创建逝者信息成功, deceasedId={}, name={}", deceasedInfo.getId(), deceasedInfo.getDeceasedName());
        return deceasedInfo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeceased(DeceasedInfoDTO deceasedInfoDTO) {
        if (deceasedInfoDTO.getId() == null) {
            throw new BusinessException("逝者ID不能为空");
        }

        DeceasedInfo existDeceased = deceasedInfoMapper.selectById(deceasedInfoDTO.getId());
        if (existDeceased == null) {
            throw new BusinessException("逝者信息不存在");
        }

        // 验证墓位是否存在
        if (deceasedInfoDTO.getTombId() != null) {
            TombLocation tombLocation = tombLocationMapper.selectById(deceasedInfoDTO.getTombId());
            if (tombLocation == null) {
                throw new BusinessException("墓位不存在");
            }
        }

        // 验证身份证号唯一性（排除自己）
        if (StringUtils.isNotBlank(deceasedInfoDTO.getIdCard()) 
                && !deceasedInfoDTO.getIdCard().equals(existDeceased.getIdCard())) {
            DeceasedInfo exist = deceasedInfoMapper.selectByIdCard(deceasedInfoDTO.getIdCard());
            if (exist != null) {
                throw new BusinessException("身份证号已存在");
            }
        }

        // 转换并更新
        DeceasedInfo deceasedInfo = EntityConverter.INSTANCE.toDeceasedInfo(deceasedInfoDTO);
        deceasedInfoMapper.updateById(deceasedInfo);
        log.info("更新逝者信息成功, deceasedId={}", deceasedInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeceased(Long deceasedId) {
        if (deceasedId == null) {
            throw new BusinessException("逝者ID不能为空");
        }

        DeceasedInfo deceasedInfo = deceasedInfoMapper.selectById(deceasedId);
        if (deceasedInfo == null) {
            throw new BusinessException("逝者信息不存在");
        }

        // 逻辑删除
        deceasedInfoMapper.deleteById(deceasedId);
        log.info("删除逝者信息成功, deceasedId={}", deceasedId);
    }

    @Override
    public DeceasedInfoVO getDeceasedById(Long deceasedId) {
        if (deceasedId == null) {
            throw new BusinessException("逝者ID不能为空");
        }

        DeceasedInfo deceasedInfo = deceasedInfoMapper.selectById(deceasedId);
        if (deceasedInfo == null) {
            throw new BusinessException("逝者信息不存在");
        }

        return EntityConverter.INSTANCE.toDeceasedInfoVO(deceasedInfo);
    }

    @Override
    public List<DeceasedInfoVO> getDeceasedByTombId(Long tombId) {
        if (tombId == null) {
            throw new BusinessException("墓位ID不能为空");
        }

        List<DeceasedInfo> deceasedList = deceasedInfoMapper.selectByTombId(tombId);
        return EntityConverter.INSTANCE.toDeceasedInfoVOList(deceasedList);
    }

    @Override
    public List<DeceasedInfoVO> searchDeceasedByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new BusinessException("姓名不能为空");
        }

        List<DeceasedInfo> deceasedList = deceasedInfoMapper.selectByNameLike(name);
        return EntityConverter.INSTANCE.toDeceasedInfoVOList(deceasedList);
    }

    @Override
    public DeceasedInfoVO getDeceasedByIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            throw new BusinessException("身份证号不能为空");
        }

        DeceasedInfo deceasedInfo = deceasedInfoMapper.selectByIdCard(idCard);
        return deceasedInfo != null ? EntityConverter.INSTANCE.toDeceasedInfoVO(deceasedInfo) : null;
    }

    @Override
    public Page<DeceasedInfoVO> pageDeceased(PageQueryDTO pageQueryDTO) {
        Page<DeceasedInfo> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        LambdaQueryWrapper<DeceasedInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DeceasedInfo::getDeathDate);
        
        Page<DeceasedInfo> deceasedPage = deceasedInfoMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<DeceasedInfoVO> voPage = new Page<>(deceasedPage.getCurrent(), deceasedPage.getSize(), deceasedPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toDeceasedInfoVOList(deceasedPage.getRecords()));
        
        return voPage;
    }
}
