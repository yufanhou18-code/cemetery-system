package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.dto.DigitalMemorialDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.entity.DeceasedInfo;
import com.cemetery.domain.entity.DigitalMemorial;
import com.cemetery.domain.entity.TombLocation;
import com.cemetery.domain.enums.AccessPermissionEnum;
import com.cemetery.domain.mapper.DeceasedInfoMapper;
import com.cemetery.domain.mapper.DigitalMemorialMapper;
import com.cemetery.domain.mapper.TombLocationMapper;
import com.cemetery.domain.vo.DigitalMemorialVO;
import com.cemetery.service.MemorialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 纪念空间管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemorialServiceImpl implements MemorialService {

    private final DigitalMemorialMapper memorialMapper;
    private final TombLocationMapper tombLocationMapper;
    private final DeceasedInfoMapper deceasedInfoMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMemorial(DigitalMemorialDTO memorialDTO) {
        log.info("开始创建纪念空间, tombId={}, deceasedId={}", 
                memorialDTO.getTombId(), memorialDTO.getDeceasedId());

        try {
            // 校验墓位是否存在
            TombLocation tombLocation = tombLocationMapper.selectById(memorialDTO.getTombId());
            if (tombLocation == null) {
                log.error("墓位不存在, tombId={}", memorialDTO.getTombId());
                throw new BusinessException("墓位不存在，请检查墓位ID");
            }
            log.debug("墓位校验通过, tombId={}", memorialDTO.getTombId());

            // 校验逝者是否存在
            DeceasedInfo deceasedInfo = deceasedInfoMapper.selectById(memorialDTO.getDeceasedId());
            if (deceasedInfo == null) {
                log.error("逝者信息不存在, deceasedId={}", memorialDTO.getDeceasedId());
                throw new BusinessException("逝者信息不存在，请检查逝者ID");
            }
            log.debug("逝者信息校验通过, deceasedId={}", memorialDTO.getDeceasedId());

            // 校验逝者是否已有纪念空间
            DigitalMemorial existMemorial = memorialMapper.selectByDeceasedId(memorialDTO.getDeceasedId());
            if (existMemorial != null) {
                log.error("该逝者已有纪念空间, deceasedId={}, existMemorialId={}", 
                        memorialDTO.getDeceasedId(), existMemorial.getId());
                throw new BusinessException("该逝者已有纪念空间，无法重复创建");
            }
            log.debug("逝者纪念空间唯一性校验通过");

            // 生成纪念空间编号
            String spaceNo = generateSpaceNo();
            log.debug("生成纪念空间编号: {}", spaceNo);

            // 转换并保存
            DigitalMemorial memorial = new DigitalMemorial();
            BeanUtils.copyProperties(memorialDTO, memorial);
            memorial.setSpaceNo(spaceNo);

            // 处理密码加密
            if (AccessPermissionEnum.PASSWORD_PROTECTED.equals(memorial.getAccessPermission())) {
                if (StringUtils.isBlank(memorialDTO.getAccessPassword())) {
                    throw new BusinessException("密码访问模式必须设置访问密码");
                }
                memorial.setAccessPassword(passwordEncoder.encode(memorialDTO.getAccessPassword()));
                log.debug("访问密码已加密");
            }

            // 初始化统计字段
            memorial.setVisitCount(0);
            memorial.setCandleCount(0);
            memorial.setFlowerCount(0);
            memorial.setIncenseCount(0);
            memorial.setMessageCount(0);
            memorial.setIsPublished(0);
            
            // 手动设置create_time（分区表必需）
            Date now = new Date();
            if (memorial.getCreateTime() == null) {
                memorial.setCreateTime(now);
            }
            if (memorial.getUpdateTime() == null) {
                memorial.setUpdateTime(now);
            }
            log.debug("准备插入数据库, createTime={}", memorial.getCreateTime());

            memorialMapper.insert(memorial);
            log.info("创建纪念空间成功, memorialId={}, spaceNo={}", memorial.getId(), spaceNo);

            return memorial.getId();
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("创建纪念空间失败, deceasedId={}, error={}", 
                    memorialDTO.getDeceasedId(), e.getMessage(), e);
            throw new BusinessException("创建纪念空间失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMemorial(DigitalMemorialDTO memorialDTO) {
        if (memorialDTO.getId() == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        log.info("开始更新纪念空间, memorialId={}", memorialDTO.getId());

        DigitalMemorial existMemorial = memorialMapper.selectById(memorialDTO.getId());
        if (existMemorial == null) {
            throw new BusinessException("纪念空间不存在");
        }

        // 转换并更新
        DigitalMemorial memorial = new DigitalMemorial();
        BeanUtils.copyProperties(memorialDTO, memorial);

        // 处理密码更新
        if (AccessPermissionEnum.PASSWORD_PROTECTED.equals(memorial.getAccessPermission())) {
            if (StringUtils.isNotBlank(memorialDTO.getAccessPassword())) {
                memorial.setAccessPassword(passwordEncoder.encode(memorialDTO.getAccessPassword()));
            } else {
                memorial.setAccessPassword(existMemorial.getAccessPassword());
            }
        } else {
            memorial.setAccessPassword(null);
        }

        memorialMapper.updateById(memorial);
        log.info("更新纪念空间成功, memorialId={}", memorial.getId());
    }

    @Override
    public DigitalMemorialVO getMemorialDetail(Long memorialId) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        DigitalMemorial memorial = memorialMapper.selectMemorialWithDetails(memorialId);
        if (memorial == null) {
            throw new BusinessException("纪念空间不存在");
        }

        return convertToVO(memorial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMemorial(Long memorialId) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        log.info("开始删除纪念空间, memorialId={}", memorialId);

        DigitalMemorial memorial = memorialMapper.selectById(memorialId);
        if (memorial == null) {
            throw new BusinessException("纪念空间不存在");
        }

        // 逻辑删除
        memorialMapper.deleteById(memorialId);
        log.info("删除纪念空间成功, memorialId={}", memorialId);
    }

    @Override
    public DigitalMemorialVO getMemorialBySpaceNo(String spaceNo) {
        if (StringUtils.isBlank(spaceNo)) {
            throw new BusinessException("纪念空间编号不能为空");
        }

        DigitalMemorial memorial = memorialMapper.selectBySpaceNo(spaceNo);
        return memorial != null ? convertToVO(memorial) : null;
    }

    @Override
    public DigitalMemorialVO getMemorialByDeceasedId(Long deceasedId) {
        if (deceasedId == null) {
            throw new BusinessException("逝者ID不能为空");
        }

        DigitalMemorial memorial = memorialMapper.selectByDeceasedId(deceasedId);
        return memorial != null ? convertToVO(memorial) : null;
    }

    @Override
    public DigitalMemorialVO getMemorialByTombId(Long tombId) {
        if (tombId == null) {
            throw new BusinessException("墓位ID不能为空");
        }

        DigitalMemorial memorial = memorialMapper.selectByTombId(tombId);
        return memorial != null ? convertToVO(memorial) : null;
    }

    @Override
    public Page<DigitalMemorialVO> pagePublishedMemorials(PageQueryDTO pageQueryDTO) {
        Page<DigitalMemorial> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        IPage<DigitalMemorial> memorialPage = memorialMapper.selectPublishedMemorials(page);

        // 转换为VO
        Page<DigitalMemorialVO> voPage = new Page<>(memorialPage.getCurrent(), 
                memorialPage.getSize(), memorialPage.getTotal());
        voPage.setRecords(memorialPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishMemorial(Long memorialId) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        log.info("发布纪念空间, memorialId={}", memorialId);

        DigitalMemorial memorial = memorialMapper.selectById(memorialId);
        if (memorial == null) {
            throw new BusinessException("纪念空间不存在");
        }

        memorial.setIsPublished(1);
        memorial.setPublishTime(new Date());
        memorialMapper.updateById(memorial);

        log.info("发布纪念空间成功, memorialId={}", memorialId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpublishMemorial(Long memorialId) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        log.info("取消发布纪念空间, memorialId={}", memorialId);

        DigitalMemorial memorial = memorialMapper.selectById(memorialId);
        if (memorial == null) {
            throw new BusinessException("纪念空间不存在");
        }

        memorial.setIsPublished(0);
        memorialMapper.updateById(memorial);

        log.info("取消发布纪念空间成功, memorialId={}", memorialId);
    }

    @Override
    public void incrementVisitCount(Long memorialId) {
        memorialMapper.incrementVisitCount(memorialId);
    }

    @Override
    public void incrementCandleCount(Long memorialId) {
        memorialMapper.incrementCandleCount(memorialId);
    }

    @Override
    public void incrementFlowerCount(Long memorialId) {
        memorialMapper.incrementFlowerCount(memorialId);
    }

    @Override
    public void incrementIncenseCount(Long memorialId) {
        memorialMapper.incrementIncenseCount(memorialId);
    }

    @Override
    public List<DigitalMemorialVO> getPopularMemorials(Integer limit) {
        Page<DigitalMemorial> page = new Page<>(1, limit != null ? limit : 10);
        IPage<DigitalMemorial> memorialPage = memorialMapper.selectPopularMemorials(page, limit);

        return memorialPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DigitalMemorialVO> getExpiringMemorials(Integer days) {
        if (days == null || days <= 0) {
            days = 30;
        }

        List<DigitalMemorial> memorials = memorialMapper.selectExpiringMemorials(days);
        return memorials.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean verifyAccess(Long memorialId, String password) {
        DigitalMemorial memorial = memorialMapper.selectById(memorialId);
        if (memorial == null) {
            return false;
        }

        AccessPermissionEnum permission = memorial.getAccessPermission();
        
        // 公开访问
        if (AccessPermissionEnum.PUBLIC.equals(permission)) {
            return true;
        }

        // 密码访问
        if (AccessPermissionEnum.PASSWORD_PROTECTED.equals(permission)) {
            if (StringUtils.isBlank(password)) {
                return false;
            }
            return passwordEncoder.matches(password, memorial.getAccessPassword());
        }

        // 家属可见和完全私密需要在Controller层进行用户身份验证
        return false;
    }

    @Override
    public Map<String, Object> getMemorialStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总数统计
        statistics.put("totalCount", memorialMapper.countMemorials(null));
        statistics.put("publishedCount", memorialMapper.countMemorials(1));
        statistics.put("unpublishedCount", memorialMapper.countMemorials(0));

        // 按访问权限统计
        List<Map<String, Object>> permissionStats = memorialMapper.countByAccessPermission();
        statistics.put("permissionStats", permissionStats);

        return statistics;
    }

    /**
     * 生成纪念空间编号
     */
    private String generateSpaceNo() {
        // 格式：DM + 年月日 + 4位序号
        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefix = "DM" + date;
        
        // 查询当天已有的最大序号
        LambdaQueryWrapper<DigitalMemorial> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(DigitalMemorial::getSpaceNo, prefix)
                .orderByDesc(DigitalMemorial::getSpaceNo)
                .last("LIMIT 1");
        
        DigitalMemorial lastMemorial = memorialMapper.selectOne(wrapper);
        
        int nextSeq = 1;
        if (lastMemorial != null && lastMemorial.getSpaceNo() != null) {
            String lastNo = lastMemorial.getSpaceNo().substring(prefix.length());
            nextSeq = Integer.parseInt(lastNo) + 1;
        }
        
        return prefix + String.format("%04d", nextSeq);
    }

    /**
     * 转换为VO
     */
    private DigitalMemorialVO convertToVO(DigitalMemorial memorial) {
        if (memorial == null) {
            return null;
        }

        DigitalMemorialVO vo = new DigitalMemorialVO();
        BeanUtils.copyProperties(memorial, vo);

        // 关联信息
        if (memorial.getTombLocation() != null) {
            vo.setTombNo(memorial.getTombLocation().getTombNo());
        }
        if (memorial.getDeceasedInfo() != null) {
            vo.setDeceasedName(memorial.getDeceasedInfo().getDeceasedName());
            vo.setDeceasedPhoto(memorial.getDeceasedInfo().getPhoto());
        }

        return vo;
    }
}
