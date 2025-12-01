package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.entity.ProviderService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProviderServiceMapper extends BaseMapper<ProviderService> {
    
    ProviderService selectByServiceNo(@Param("serviceNo") String serviceNo);
    
    IPage<ProviderService> selectByProviderId(
            Page<ProviderService> page,
            @Param("providerId") Long providerId,
            @Param("status") Integer status
    );
    
    IPage<ProviderService> searchServices(
            Page<ProviderService> page,
            @Param("keyword") String keyword,
            @Param("category") Integer category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
    
    List<ProviderService> selectHotServices(@Param("limit") Integer limit);
    
    List<ProviderService> selectNewServices(@Param("limit") Integer limit);
    
    List<ProviderService> selectRecommendedServices(@Param("limit") Integer limit);
    
    int incrementViewCount(@Param("id") Long id);
    
    int incrementSalesCount(@Param("id") Long id, @Param("quantity") Integer quantity);
    
    int updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);
    
    Long countServicesByProvider(@Param("providerId") Long providerId);
}
