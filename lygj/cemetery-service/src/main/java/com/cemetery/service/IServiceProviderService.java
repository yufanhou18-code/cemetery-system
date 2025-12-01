package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ServiceProviderDTO;
import com.cemetery.domain.dto.ServiceProviderQueryDTO;
import com.cemetery.domain.vo.ServiceProviderVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 服务商管理服务接口
 */
public interface IServiceProviderService {

    /**
     * 创建服务商
     *
     * @param providerDTO 服务商信息
     * @return 服务商ID
     */
    Long createProvider(ServiceProviderDTO providerDTO);

    /**
     * 更新服务商信息
     *
     * @param providerDTO 服务商信息
     */
    void updateProvider(ServiceProviderDTO providerDTO);

    /**
     * 删除服务商
     *
     * @param providerId 服务商ID
     */
    void deleteProvider(Long providerId);

    /**
     * 获取服务商详情
     *
     * @param providerId 服务商ID
     * @return 服务商详情
     */
    ServiceProviderVO getProviderDetail(Long providerId);

    /**
     * 通过服务商编号获取服务商
     *
     * @param providerNo 服务商编号
     * @return 服务商详情
     */
    ServiceProviderVO getProviderByNo(String providerNo);

    /**
     * 分页查询服务商
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<ServiceProviderVO> pageProviders(ServiceProviderQueryDTO queryDTO);

    /**
     * 获取推荐服务商列表
     *
     * @param limit 返回数量
     * @return 推荐服务商列表
     */
    List<ServiceProviderVO> getRecommendedProviders(Integer limit);

    /**
     * 搜索服务商
     *
     * @param keyword 关键词
     * @param status 状态
     * @param pageQueryDTO 分页参数
     * @return 搜索结果
     */
    Page<ServiceProviderVO> searchProviders(String keyword, Integer status, PageQueryDTO pageQueryDTO);

    /**
     * 审核服务商
     *
     * @param providerId 服务商ID
     * @param approved 是否通过
     * @param auditRemark 审核备注
     */
    void auditProvider(Long providerId, Boolean approved, String auditRemark);

    /**
     * 更新服务商评分
     *
     * @param providerId 服务商ID
     * @param rating 评分
     */
    void updateRating(Long providerId, BigDecimal rating);

    /**
     * 增加服务次数
     *
     * @param providerId 服务商ID
     */
    void incrementServiceCount(Long providerId);

    /**
     * 更新服务商统计信息
     *
     * @param providerId 服务商ID
     */
    void updateStatistics(Long providerId);

    /**
     * 获取服务商统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getProviderStatistics();

    /**
     * 修改服务商状态
     *
     * @param providerId 服务商ID
     * @param status 状态
     */
    void changeProviderStatus(Long providerId, Integer status);
}
