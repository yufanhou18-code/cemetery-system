package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.converter.EntityConverter;
import com.cemetery.domain.dto.ServiceFeedbackDTO;
import com.cemetery.domain.dto.ServiceFeedbackQueryDTO;
import com.cemetery.domain.entity.ProviderService;
import com.cemetery.domain.entity.ServiceFeedback;
import com.cemetery.domain.entity.ServiceProvider;
import com.cemetery.domain.entity.User;
import com.cemetery.domain.mapper.ProviderServiceMapper;
import com.cemetery.domain.mapper.ServiceFeedbackMapper;
import com.cemetery.domain.mapper.ServiceProviderMapper;
import com.cemetery.domain.mapper.UserMapper;
import com.cemetery.domain.vo.ServiceFeedbackVO;
import com.cemetery.service.IServiceFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务反馈管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceFeedbackServiceImpl implements IServiceFeedbackService {

    private final ServiceFeedbackMapper serviceFeedbackMapper;
    private final ServiceProviderMapper serviceProviderMapper;
    private final ProviderServiceMapper providerServiceMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFeedback(ServiceFeedbackDTO feedbackDTO) {
        log.info("开始创建服务反馈, providerId={}, serviceId={}, userId={}", 
                feedbackDTO.getProviderId(), feedbackDTO.getServiceId(), feedbackDTO.getUserId());

        // 校验服务商是否存在
        if (feedbackDTO.getProviderId() != null) {
            ServiceProvider provider = serviceProviderMapper.selectById(feedbackDTO.getProviderId());
            if (provider == null) {
                log.error("服务商不存在, providerId={}", feedbackDTO.getProviderId());
                throw new BusinessException("服务商不存在");
            }
        }

        // 转换并保存
        ServiceFeedback serviceFeedback = EntityConverter.INSTANCE.toServiceFeedback(feedbackDTO);
        serviceFeedback.setStatus(0); // 待审核
        serviceFeedback.setLikeCount(0);
        serviceFeedback.setIsTop(0);

        serviceFeedbackMapper.insert(serviceFeedback);
        log.info("创建服务反馈成功, feedbackId={}", serviceFeedback.getId());

        return serviceFeedback.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFeedback(ServiceFeedbackDTO feedbackDTO) {
        if (feedbackDTO.getId() == null) {
            throw new BusinessException("反馈ID不能为空");
        }

        ServiceFeedback existFeedback = serviceFeedbackMapper.selectById(feedbackDTO.getId());
        if (existFeedback == null) {
            throw new BusinessException("反馈不存在");
        }

        // 转换并更新
        ServiceFeedback serviceFeedback = EntityConverter.INSTANCE.toServiceFeedback(feedbackDTO);
        
        serviceFeedbackMapper.updateById(serviceFeedback);
        log.info("更新服务反馈成功, feedbackId={}", serviceFeedback.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFeedback(Long feedbackId) {
        if (feedbackId == null) {
            throw new BusinessException("反馈ID不能为空");
        }

        ServiceFeedback serviceFeedback = serviceFeedbackMapper.selectById(feedbackId);
        if (serviceFeedback == null) {
            throw new BusinessException("反馈不存在");
        }

        // 逻辑删除
        serviceFeedbackMapper.deleteById(feedbackId);
        log.info("删除服务反馈成功, feedbackId={}", feedbackId);
    }

    @Override
    public ServiceFeedbackVO getFeedbackDetail(Long feedbackId) {
        if (feedbackId == null) {
            throw new BusinessException("反馈ID不能为空");
        }

        ServiceFeedback serviceFeedback = serviceFeedbackMapper.selectById(feedbackId);
        if (serviceFeedback == null) {
            throw new BusinessException("反馈不存在");
        }

        return EntityConverter.INSTANCE.toServiceFeedbackVO(serviceFeedback);
    }

    @Override
    public Page<ServiceFeedbackVO> pageFeedbacks(ServiceFeedbackQueryDTO queryDTO) {
        Page<ServiceFeedback> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        LambdaQueryWrapper<ServiceFeedback> wrapper = new LambdaQueryWrapper<>();
        
        // 构建查询条件
        if (queryDTO.getProviderId() != null) {
            wrapper.eq(ServiceFeedback::getProviderId, queryDTO.getProviderId());
        }
        if (queryDTO.getServiceId() != null) {
            wrapper.eq(ServiceFeedback::getServiceId, queryDTO.getServiceId());
        }
        if (queryDTO.getUserId() != null) {
            wrapper.eq(ServiceFeedback::getUserId, queryDTO.getUserId());
        }
        if (queryDTO.getFeedbackType() != null) {
            wrapper.eq(ServiceFeedback::getFeedbackType, queryDTO.getFeedbackType());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(ServiceFeedback::getStatus, queryDTO.getStatus());
        }
        if (queryDTO.getIsTop() != null) {
            wrapper.eq(ServiceFeedback::getIsTop, queryDTO.getIsTop());
        }
        
        wrapper.orderByDesc(ServiceFeedback::getCreateTime);

        Page<ServiceFeedback> feedbackPage = serviceFeedbackMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<ServiceFeedbackVO> voPage = new Page<>(feedbackPage.getCurrent(), 
                feedbackPage.getSize(), feedbackPage.getTotal());
        voPage.setRecords(EntityConverter.INSTANCE.toServiceFeedbackVOList(feedbackPage.getRecords()));

        return voPage;
    }

    @Override
    public List<ServiceFeedbackVO> getFeedbacksByProvider(Long providerId) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        LambdaQueryWrapper<ServiceFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceFeedback::getProviderId, providerId);
        List<ServiceFeedback> feedbacks = serviceFeedbackMapper.selectList(wrapper);
        return EntityConverter.INSTANCE.toServiceFeedbackVOList(feedbacks);
    }

    @Override
    public List<ServiceFeedbackVO> getFeedbacksByService(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        LambdaQueryWrapper<ServiceFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceFeedback::getServiceId, serviceId);
        List<ServiceFeedback> feedbacks = serviceFeedbackMapper.selectList(wrapper);
        return EntityConverter.INSTANCE.toServiceFeedbackVOList(feedbacks);
    }

    @Override
    public List<ServiceFeedbackVO> getFeedbacksByUser(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        LambdaQueryWrapper<ServiceFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceFeedback::getUserId, userId);
        List<ServiceFeedback> feedbacks = serviceFeedbackMapper.selectList(wrapper);
        return EntityConverter.INSTANCE.toServiceFeedbackVOList(feedbacks);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyFeedback(Long feedbackId, String replyContent) {
        if (feedbackId == null) {
            throw new BusinessException("反馈ID不能为空");
        }
        if (StringUtils.isBlank(replyContent)) {
            throw new BusinessException("回复内容不能为空");
        }

        ServiceFeedback serviceFeedback = serviceFeedbackMapper.selectById(feedbackId);
        if (serviceFeedback == null) {
            throw new BusinessException("反馈不存在");
        }

        serviceFeedback.setReplyContent(replyContent);
        serviceFeedback.setReplyTime(new Date());
        serviceFeedbackMapper.updateById(serviceFeedback);
        log.info("回复反馈成功, feedbackId={}", feedbackId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditFeedback(Long feedbackId, Boolean approved, String auditRemark) {
        if (feedbackId == null) {
            throw new BusinessException("反馈ID不能为空");
        }
        if (approved == null) {
            throw new BusinessException("审核结果不能为空");
        }

        ServiceFeedback serviceFeedback = serviceFeedbackMapper.selectById(feedbackId);
        if (serviceFeedback == null) {
            throw new BusinessException("反馈不存在");
        }

        serviceFeedback.setStatus(approved ? 1 : 2); // 1-审核通过，2-审核拒绝
        serviceFeedback.setAuditRemark(auditRemark);
        serviceFeedbackMapper.updateById(serviceFeedback);
        log.info("审核反馈成功, feedbackId={}, approved={}", feedbackId, approved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsFeatured(Long feedbackId) {
        if (feedbackId == null) {
            throw new BusinessException("反馈ID不能为空");
        }

        ServiceFeedback serviceFeedback = serviceFeedbackMapper.selectById(feedbackId);
        if (serviceFeedback == null) {
            throw new BusinessException("反馈不存在");
        }

        serviceFeedback.setIsTop(1);
        serviceFeedbackMapper.updateById(serviceFeedback);
        log.info("标记为精选反馈成功, feedbackId={}", feedbackId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unmarkAsFeatured(Long feedbackId) {
        if (feedbackId == null) {
            throw new BusinessException("反馈ID不能为空");
        }

        ServiceFeedback serviceFeedback = serviceFeedbackMapper.selectById(feedbackId);
        if (serviceFeedback == null) {
            throw new BusinessException("反馈不存在");
        }

        serviceFeedback.setIsTop(0);
        serviceFeedbackMapper.updateById(serviceFeedback);
        log.info("取消精选反馈成功, feedbackId={}", feedbackId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementLikeCount(Long feedbackId) {
        if (feedbackId == null) {
            throw new BusinessException("反馈ID不能为空");
        }

        int rows = serviceFeedbackMapper.incrementLikeCount(feedbackId);
        if (rows > 0) {
            log.info("增加点赞数成功, feedbackId={}", feedbackId);
        }
    }

    @Override
    public List<ServiceFeedbackVO> getFeaturedFeedbacks(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        LambdaQueryWrapper<ServiceFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceFeedback::getIsTop, 1);
        wrapper.orderByDesc(ServiceFeedback::getCreateTime);
        wrapper.last("LIMIT " + limit);
        
        List<ServiceFeedback> feedbacks = serviceFeedbackMapper.selectList(wrapper);
        return EntityConverter.INSTANCE.toServiceFeedbackVOList(feedbacks);
    }

    @Override
    public BigDecimal calculateProviderAverageRating(Long providerId) {
        if (providerId == null) {
            throw new BusinessException("服务商ID不能为空");
        }

        BigDecimal avgRating = serviceFeedbackMapper.calculateAverageRating(providerId);
        return avgRating != null ? avgRating : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateServiceAverageRating(Long serviceId) {
        if (serviceId == null) {
            throw new BusinessException("服务项目ID不能为空");
        }

        // 使用与服务商相同的方法，或者自定义查询
        LambdaQueryWrapper<ServiceFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceFeedback::getServiceId, serviceId);
        wrapper.isNotNull(ServiceFeedback::getRating);
        List<ServiceFeedback> feedbacks = serviceFeedbackMapper.selectList(wrapper);
        
        if (feedbacks.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = feedbacks.stream()
                .map(ServiceFeedback::getRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.divide(new BigDecimal(feedbacks.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public Map<String, Object> getFeedbackStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总反馈数
        Long totalCount = serviceFeedbackMapper.selectCount(null);
        statistics.put("totalCount", totalCount);

        // 待审核数
        LambdaQueryWrapper<ServiceFeedback> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(ServiceFeedback::getStatus, 0);
        Long pendingCount = serviceFeedbackMapper.selectCount(pendingWrapper);
        statistics.put("pendingCount", pendingCount);

        // 已通过数
        LambdaQueryWrapper<ServiceFeedback> approvedWrapper = new LambdaQueryWrapper<>();
        approvedWrapper.eq(ServiceFeedback::getStatus, 1);
        Long approvedCount = serviceFeedbackMapper.selectCount(approvedWrapper);
        statistics.put("approvedCount", approvedCount);

        // 精选反馈数
        LambdaQueryWrapper<ServiceFeedback> featuredWrapper = new LambdaQueryWrapper<>();
        featuredWrapper.eq(ServiceFeedback::getIsTop, 1);
        Long featuredCount = serviceFeedbackMapper.selectCount(featuredWrapper);
        statistics.put("featuredCount", featuredCount);

        log.info("获取反馈统计信息成功, statistics={}", statistics);
        return statistics;
    }
}
