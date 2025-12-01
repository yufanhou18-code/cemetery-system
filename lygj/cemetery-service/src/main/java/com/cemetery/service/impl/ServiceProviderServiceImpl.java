package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ServiceProviderDTO;
import com.cemetery.domain.dto.ServiceProviderQueryDTO;
import com.cemetery.domain.entity.ServiceProvider;
import com.cemetery.domain.mapper.ServiceProviderMapper;
import com.cemetery.domain.mapper.UserMapper;
import com.cemetery.domain.vo.ServiceProviderVO;
import com.cemetery.service.IServiceProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务商管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceProviderServiceImpl implements IServiceProviderService {

    private final ServiceProviderMapper serviceProviderMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProvider(ServiceProviderDTO providerDTO) {
        log.info("开始创建服务商, providerName={}, contactPerson={}", 
                providerDTO.getProviderName(), providerDTO.getContactPerson());

        // 校验关联用户是否存在（如果需要关联用户功能，可在此扩展）

        // 校验联系电话唯一性
        if (StringUtils.isNotBlank(providerDTO.getContactPhone())) {
            LambdaQueryWrapper<ServiceProvider> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ServiceProvider::getContactPhone, providerDTO.getContactPhone());
            if (serviceProviderMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("该联系电话已被使用");
            }
        }

        // 生成服务商编号
        String providerNo = generateProviderNo();

        // 转换并保存
        ServiceProvider serviceProvider = EntityConverter.INSTANCE.toServiceProvider(providerDTO);
        serviceProvider.setProviderNo(providerNo);
        serviceProvider.setStatus(0); // 待审核
        serviceProvider.setRating(new BigDecimal("5.00")); // 默认5.0分
        serviceProvider.setServiceCount(0);
        serviceProvider.setComplaintCount(0);
        serviceProvider.setIsRecommended(0);

        serviceProviderMapper.insert(serviceProvider);
        log.info("创建服务商成功, providerId={}, providerNo={}", 
                serviceProvider.getId(), providerNo);

        return serviceProvider.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProvider(ServiceProviderDTO providerDTO) {
        if (providerDTO.getId() == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        ServiceProvider existProvider = serviceProviderMapper.selectById(providerDTO.getId());
        if (existProvider == null) {
            throw new BusinessException("服务商不存在");
        }

        // 校验联系电话唯一性（排除自己）
        if (StringUtils.isNotBlank(providerDTO.getContactPhone()) 
                && !providerDTO.getContactPhone().equals(existProvider.getContactPhone())) {
            LambdaQueryWrapper<ServiceProvider> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ServiceProvider::getContactPhone, providerDTO.getContactPhone());
            if (serviceProviderMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("该联系电话已被使用");
            }
        }

        // 转换并更新
        ServiceProvider serviceProvider = EntityConverter.INSTANCE.toServiceProvider(providerDTO);
        
        serviceProviderMapper.updateById(serviceProvider);
        log.info("更新服务商成功, providerId={}", serviceProvider.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProvider(Long providerId) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        ServiceProvider serviceProvider = serviceProviderMapper.selectById(providerId);
        if (serviceProvider == null) {
            throw new BusinessException("服务商不存在");
        }

        // 逻辑删除
        serviceProviderMapper.deleteById(providerId);
        log.info("删除服务商成功, providerId={}", providerId);
    }

    @Override
    public ServiceProviderVO getProviderDetail(Long providerId) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        ServiceProvider serviceProvider = serviceProviderMapper.selectById(providerId);
        if (serviceProvider == null) {
            throw new BusinessException("服务商不存在");
        }

        return EntityConverter.INSTANCE.toServiceProviderVO(serviceProvider);
    }

    @Override
    public ServiceProviderVO getProviderByNo(String providerNo) {
        if (StringUtils.isBlank(providerNo)) {
            throw new BusinessException("服务商编号不能为空");
        }

        ServiceProvider serviceProvider = serviceProviderMapper.selectByProviderNo(providerNo);
        if (serviceProvider == null) {
            return null;
        }

        return EntityConverter.INSTANCE.toServiceProviderVO(serviceProvider);
    }

    @Override
    public Page<ServiceProviderVO> pageProviders(ServiceProviderQueryDTO queryDTO) {
        Page<ServiceProvider> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        LambdaQueryWrapper<ServiceProvider> wrapper = new LambdaQueryWrapper<>();
        
        // 构建查询条件
        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            wrapper.like(ServiceProvider::getProviderName, queryDTO.getKeyword());
        }
        if (queryDTO.getProviderType() != null) {
            wrapper.eq(ServiceProvider::getProviderType, queryDTO.getProviderType());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(ServiceProvider::getStatus, queryDTO.getStatus());
        }
        if (queryDTO.getAuditStatus() != null) {
            wrapper.eq(ServiceProvider::getAuditStatus, queryDTO.getAuditStatus());
        }
        if (queryDTO.getIsRecommended() != null) {
            wrapper.eq(ServiceProvider::getIsRecommended, queryDTO.getIsRecommended());
        }
        if (StringUtils.isNotBlank(queryDTO.getServiceScope())) {
            wrapper.like(ServiceProvider::getServiceScope, queryDTO.getServiceScope());
        }
        if (StringUtils.isNotBlank(queryDTO.getProvince())) {
            wrapper.eq(ServiceProvider::getProvince, queryDTO.getProvince());
        }
        if (StringUtils.isNotBlank(queryDTO.getCity())) {
            wrapper.eq(ServiceProvider::getCity, queryDTO.getCity());
        }
        
        wrapper.orderByDesc(ServiceProvider::getCreateTime);

        Page<ServiceProvider> providerPage = serviceProviderMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<ServiceProviderVO> voPage = new Page<>(providerPage.getCurrent(), 
                providerPage.getSize(), providerPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toServiceProviderVOList(providerPage.getRecords()));

        return voPage;
    }

    @Override
    public List<ServiceProviderVO> getRecommendedProviders(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        List<ServiceProvider> providers = serviceProviderMapper.selectRecommendedProviders(limit);
        return EntityConverter.INSTANCE.toServiceProviderVOList(providers);
    }

    @Override
    public Page<ServiceProviderVO> searchProviders(String keyword, Integer status, PageQueryDTO pageQueryDTO) {
        Page<ServiceProvider> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        IPage<ServiceProvider> providerPage = serviceProviderMapper.searchProviders(page, keyword, status, null, null);

        // 转换为VO
        Page<ServiceProviderVO> voPage = new Page<>(providerPage.getCurrent(), 
                providerPage.getSize(), providerPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toServiceProviderVOList(providerPage.getRecords()));

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProvider(Long providerId, Boolean approved, String auditRemark) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }
        if (approved == null) {
            throw new BusinessException("审核结果不能为空");
        }

        ServiceProvider serviceProvider = serviceProviderMapper.selectById(providerId);
        if (serviceProvider == null) {
            throw new BusinessException("服务商不存在");
        }

        // 更新审核状态
        serviceProvider.setStatus(approved ? 1 : 2); // 1-审核通过，2-审核拒绝
        serviceProvider.setAuditRemark(auditRemark);
        
        serviceProviderMapper.updateById(serviceProvider);
        log.info("审核服务商成功, providerId={}, approved={}", providerId, approved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRating(Long providerId, BigDecimal rating) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }
        if (rating == null) {
            throw new BusinessException("评分不能为空");
        }
        if (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(new BigDecimal("5.00")) > 0) {
            throw new BusinessException("评分必须在0-5之间");
        }

        int rows = serviceProviderMapper.updateRating(providerId, rating);
        if (rows > 0) {
            log.info("更新服务商评分成功, providerId={}, rating={}", providerId, rating);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementServiceCount(Long providerId) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        ServiceProvider serviceProvider = serviceProviderMapper.selectById(providerId);
        if (serviceProvider == null) {
            throw new BusinessException("服务商不存在");
        }

        serviceProvider.setServiceCount(serviceProvider.getServiceCount() + 1);
        serviceProviderMapper.updateById(serviceProvider);
        log.info("增加服务次数成功, providerId={}, serviceCount={}", 
                providerId, serviceProvider.getServiceCount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatistics(Long providerId) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        // 这里可以根据实际业务需求更新统计信息
        // 例如：从订单、反馈等表中汇总数据
        log.info("更新服务商统计信息, providerId={}", providerId);
    }

    @Override
    public Map<String, Object> getProviderStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总服务商数
        Long totalCount = serviceProviderMapper.selectCount(null);
        statistics.put("totalCount", totalCount);

        // 待审核数
        LambdaQueryWrapper<ServiceProvider> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(ServiceProvider::getStatus, 0);
        Long pendingCount = serviceProviderMapper.selectCount(pendingWrapper);
        statistics.put("pendingCount", pendingCount);

        // 审核通过数
        LambdaQueryWrapper<ServiceProvider> approvedWrapper = new LambdaQueryWrapper<>();
        approvedWrapper.eq(ServiceProvider::getStatus, 1);
        Long approvedCount = serviceProviderMapper.selectCount(approvedWrapper);
        statistics.put("approvedCount", approvedCount);

        // 推荐服务商数
        LambdaQueryWrapper<ServiceProvider> recommendedWrapper = new LambdaQueryWrapper<>();
        recommendedWrapper.eq(ServiceProvider::getIsRecommended, 1);
        Long recommendedCount = serviceProviderMapper.selectCount(recommendedWrapper);
        statistics.put("recommendedCount", recommendedCount);

        log.info("获取服务商统计信息成功, statistics={}", statistics);
        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeProviderStatus(Long providerId, Integer status) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }

        ServiceProvider serviceProvider = serviceProviderMapper.selectById(providerId);
        if (serviceProvider == null) {
            throw new BusinessException("服务商不存在");
        }

        serviceProvider.setStatus(status);
        serviceProviderMapper.updateById(serviceProvider);
        log.info("修改服务商状态成功, providerId={}, status={}", providerId, status);
    }

    /**
     * 生成服务商编号
     */
    private String generateProviderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "SP" + timestamp + random;
    }
}
