package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.entity.ServiceFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ServiceFeedbackMapper extends BaseMapper<ServiceFeedback> {
    
    ServiceFeedback selectByFeedbackNo(@Param("feedbackNo") String feedbackNo);
    
    IPage<ServiceFeedback> selectByProviderId(
            Page<ServiceFeedback> page,
            @Param("providerId") Long providerId,
            @Param("status") Integer status
    );
    
    IPage<ServiceFeedback> selectByServiceId(
            Page<ServiceFeedback> page,
            @Param("serviceId") Long serviceId,
            @Param("status") Integer status
    );
    
    IPage<ServiceFeedback> selectByUserId(Page<ServiceFeedback> page, @Param("userId") Long userId);
    
    List<ServiceFeedback> selectTopFeedbacks(@Param("providerId") Long providerId, @Param("limit") Integer limit);
    
    int incrementLikeCount(@Param("id") Long id);
    
    BigDecimal calculateAverageRating(@Param("providerId") Long providerId);
    
    Long countFeedbacksByProvider(@Param("providerId") Long providerId, @Param("feedbackType") Integer feedbackType);
    
    List<Map<String, Object>> getFeedbackStatistics(@Param("providerId") Long providerId);
}
