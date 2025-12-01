package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ProviderServiceDTO;
import com.cemetery.domain.dto.ProviderServiceQueryDTO;
import com.cemetery.domain.entity.ProviderService;
import com.cemetery.domain.entity.ServiceProvider;
import com.cemetery.domain.mapper.ProviderServiceMapper;
import com.cemetery.domain.mapper.ServiceProviderMapper;
import com.cemetery.domain.vo.ProviderServiceVO;
import com.cemetery.service.IProviderServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务项目管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderServiceServiceImpl implements IProviderServiceService {

    private final ProviderServiceMapper providerServiceMapper;
    private final ServiceProviderMapper serviceProviderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createService(ProviderServiceDTO serviceDTO) {
        log.info("开始创建服务项目, serviceName={}, providerId={}", 
                serviceDTO.getServiceName(), serviceDTO.getProviderId());

        // 校验服务商是否存在
        if (serviceDTO.getProviderId() != null) {
            ServiceProvider provider = serviceProviderMapper.selectById(serviceDTO.getProviderId());
            if (provider == null) {
                log.error("服务商不存在, providerId={}", serviceDTO.getProviderId());
                throw new BusinessException("服务商不存在");
            }
        }

        // 生成服务编号
        String serviceNo = generateServiceNo();

        // 转换并保存
        ProviderService providerService = EntityConverter.INSTANCE.toProviderService(serviceDTO);
        providerService.setServiceNo(serviceNo);
        providerService.setStatus(0); // 待上架
        providerService.setSalesCount(0);
        providerService.setViewCount(0);
        providerService.setIsRecommended(0);

        providerServiceMapper.insert(providerService);
        log.info("创建服务项目成功, serviceId={}, serviceNo={}", 
                providerService.getId(), serviceNo);

        return providerService.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateService(ProviderServiceDTO serviceDTO) {
        if (serviceDTO.getId() == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        ProviderService existService = providerServiceMapper.selectById(serviceDTO.getId());
        if (existService == null) {
            throw new BusinessException("服务项目不存在");
        }

        // 转换并更新
        ProviderService providerService = EntityConverter.INSTANCE.toProviderService(serviceDTO);
        
        providerServiceMapper.updateById(providerService);
        log.info("更新服务项目成功, serviceId={}", providerService.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteService(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        ProviderService providerService = providerServiceMapper.selectById(serviceId);
        if (providerService == null) {
            throw new BusinessException("服务项目不存在");
        }

        // 逻辑删除
        providerServiceMapper.deleteById(serviceId);
        log.info("删除服务项目成功, serviceId={}", serviceId);
    }

    @Override
    public ProviderServiceVO getServiceDetail(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        ProviderService providerService = providerServiceMapper.selectById(serviceId);
        if (providerService == null) {
            throw new BusinessException("服务项目不存在");
        }

        return EntityConverter.INSTANCE.toProviderServiceVO(providerService);
    }

    @Override
    public ProviderServiceVO getServiceByNo(String serviceNo) {
        if (StringUtils.isBlank(serviceNo)) {
            throw new BusinessException("服务编号不能为空");
        }

        ProviderService providerService = providerServiceMapper.selectByServiceNo(serviceNo);
        if (providerService == null) {
            return null;
        }

        return EntityConverter.INSTANCE.toProviderServiceVO(providerService);
    }

    @Override
    public Page<ProviderServiceVO> pageServices(ProviderServiceQueryDTO queryDTO) {
        Page<ProviderService> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        LambdaQueryWrapper<ProviderService> wrapper = new LambdaQueryWrapper<>();
        
        // 构建查询条件
        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            wrapper.like(ProviderService::getServiceName, queryDTO.getKeyword());
        }
        if (queryDTO.getProviderId() != null) {
            wrapper.eq(ProviderService::getProviderId, queryDTO.getProviderId());
        }
        if (queryDTO.getServiceType() != null) {
            wrapper.eq(ProviderService::getServiceType, queryDTO.getServiceType());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(ProviderService::getStatus, queryDTO.getStatus());
        }
        if (queryDTO.getIsRecommended() != null) {
            wrapper.eq(ProviderService::getIsRecommended, queryDTO.getIsRecommended());
        }
        
        wrapper.orderByDesc(ProviderService::getCreateTime);

        Page<ProviderService> servicePage = providerServiceMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<ProviderServiceVO> voPage = new Page<>(servicePage.getCurrent(), 
                servicePage.getSize(), servicePage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toProviderServiceVOList(servicePage.getRecords()));

        return voPage;
    }

    @Override
    public List<ProviderServiceVO> getServicesByProvider(Long providerId) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        LambdaQueryWrapper<ProviderService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProviderService::getProviderId, providerId);
        List<ProviderService> services = providerServiceMapper.selectList(wrapper);
        return EntityConverter.INSTANCE.toProviderServiceVOList(services);
    }

    @Override
    public Page<ProviderServiceVO> searchServices(String keyword, Integer serviceType, PageQueryDTO pageQueryDTO) {
        Page<ProviderService> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        IPage<ProviderService> servicePage = providerServiceMapper.searchServices(page, keyword, serviceType, null, null);

        // 转换为VO
        Page<ProviderServiceVO> voPage = new Page<>(servicePage.getCurrent(), 
                servicePage.getSize(), servicePage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toProviderServiceVOList(servicePage.getRecords()));

        return voPage;
    }

    @Override
    public List<ProviderServiceVO> getPopularServices(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        List<ProviderService> services = providerServiceMapper.selectHotServices(limit);
        return EntityConverter.INSTANCE.toProviderServiceVOList(services);
    }

    @Override
    public List<ProviderServiceVO> getRecommendedServices(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        List<ProviderService> services = providerServiceMapper.selectRecommendedServices(limit);
        return EntityConverter.INSTANCE.toProviderServiceVOList(services);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishService(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        ProviderService providerService = providerServiceMapper.selectById(serviceId);
        if (providerService == null) {
            throw new BusinessException("服务项目不存在");
        }

        providerService.setStatus(1); // 已上架
        providerServiceMapper.updateById(providerService);
        log.info("上架服务项目成功, serviceId={}", serviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpublishService(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        ProviderService providerService = providerServiceMapper.selectById(serviceId);
        if (providerService == null) {
            throw new BusinessException("服务项目不存在");
        }

        providerService.setStatus(0); // 待上架
        providerServiceMapper.updateById(providerService);
        log.info("下架服务项目成功, serviceId={}", serviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementSalesCount(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        int rows = providerServiceMapper.incrementSalesCount(serviceId, 1);
        if (rows > 0) {
            log.info("增加销售数量成功, serviceId={}", serviceId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        int rows = providerServiceMapper.incrementViewCount(serviceId);
        if (rows > 0) {
            log.info("增加浏览次数成功, serviceId={}", serviceId);
        }
    }

    @Override
    public Map<String, Object> getServiceStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总服务项目数
        Long totalCount = providerServiceMapper.selectCount(null);
        statistics.put("totalCount", totalCount);

        // 已上架数
        LambdaQueryWrapper<ProviderService> publishedWrapper = new LambdaQueryWrapper<>();
        publishedWrapper.eq(ProviderService::getStatus, 1);
        Long publishedCount = providerServiceMapper.selectCount(publishedWrapper);
        statistics.put("publishedCount", publishedCount);

        // 推荐服务数
        LambdaQueryWrapper<ProviderService> recommendedWrapper = new LambdaQueryWrapper<>();
        recommendedWrapper.eq(ProviderService::getIsRecommended, 1);
        Long recommendedCount = providerServiceMapper.selectCount(recommendedWrapper);
        statistics.put("recommendedCount", recommendedCount);

        log.info("获取服务项目统计信息成功, statistics={}", statistics);
        return statistics;
    }

    /**
     * 生成服务编号
     */
    private String generateServiceNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "SV" + timestamp + random;
    }
}
