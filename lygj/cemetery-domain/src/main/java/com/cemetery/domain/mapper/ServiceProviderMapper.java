package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.entity.ServiceProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ServiceProviderMapper extends BaseMapper<ServiceProvider> {
    
    ServiceProvider selectByProviderNo(@Param("providerNo") String providerNo);
    
    ServiceProvider selectByUserId(@Param("userId") Long userId);
    
    IPage<ServiceProvider> searchProviders(
            Page<ServiceProvider> page,
            @Param("keyword") String keyword,
            @Param("status") Integer status,
            @Param("auditStatus") Integer auditStatus,
            @Param("isRecommended") Integer isRecommended
    );
    
    List<ServiceProvider> selectRecommendedProviders(@Param("limit") Integer limit);
    
    List<ServiceProvider> selectByCity(@Param("province") String province, @Param("city") String city);
    
    int updateRating(@Param("id") Long id, @Param("rating") BigDecimal rating);
    
    int incrementServiceCount(@Param("id") Long id);
    
    int incrementComplaintCount(@Param("id") Long id);
    
    Long countProviders(@Param("status") Integer status, @Param("auditStatus") Integer auditStatus);
    
    List<Map<String, Object>> getProviderStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
