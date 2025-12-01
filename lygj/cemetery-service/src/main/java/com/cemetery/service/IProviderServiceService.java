package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ProviderServiceDTO;
import com.cemetery.domain.dto.ProviderServiceQueryDTO;
import com.cemetery.domain.vo.ProviderServiceVO;

import java.util.List;
import java.util.Map;

/**
 * 服务项目管理服务接口
 */
public interface IProviderServiceService {

    /**
     * 创建服务项目
     *
     * @param serviceDTO 服务项目信息
     * @return 服务项目ID
     */
    Long createService(ProviderServiceDTO serviceDTO);

    /**
     * 更新服务项目
     *
     * @param serviceDTO 服务项目信息
     */
    void updateService(ProviderServiceDTO serviceDTO);

    /**
     * 删除服务项目
     *
     * @param serviceId 服务项目ID
     */
    void deleteService(Long serviceId);

    /**
     * 获取服务项目详情
     *
     * @param serviceId 服务项目ID
     * @return 服务项目详情
     */
    ProviderServiceVO getServiceDetail(Long serviceId);

    /**
     * 通过服务编号获取服务项目
     *
     * @param serviceNo 服务编号
     * @return 服务项目详情
     */
    ProviderServiceVO getServiceByNo(String serviceNo);

    /**
     * 分页查询服务项目
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<ProviderServiceVO> pageServices(ProviderServiceQueryDTO queryDTO);

    /**
     * 获取服务商的所有服务项目
     *
     * @param providerId 服务商ID
     * @return 服务项目列表
     */
    List<ProviderServiceVO> getServicesByProvider(Long providerId);

    /**
     * 搜索服务项目
     *
     * @param keyword 关键词
     * @param serviceType 服务类型
     * @param pageQueryDTO 分页参数
     * @return 搜索结果
     */
    Page<ProviderServiceVO> searchServices(String keyword, Integer serviceType, PageQueryDTO pageQueryDTO);

    /**
     * 获取热门服务项目
     *
     * @param limit 返回数量
     * @return 热门服务项目列表
     */
    List<ProviderServiceVO> getPopularServices(Integer limit);

    /**
     * 获取推荐服务项目
     *
     * @param limit 返回数量
     * @return 推荐服务项目列表
     */
    List<ProviderServiceVO> getRecommendedServices(Integer limit);

    /**
     * 上架服务项目
     *
     * @param serviceId 服务项目ID
     */
    void publishService(Long serviceId);

    /**
     * 下架服务项目
     *
     * @param serviceId 服务项目ID
     */
    void unpublishService(Long serviceId);

    /**
     * 增加销售数量
     *
     * @param serviceId 服务项目ID
     */
    void incrementSalesCount(Long serviceId);

    /**
     * 增加浏览次数
     *
     * @param serviceId 服务项目ID
     */
    void incrementViewCount(Long serviceId);

    /**
     * 获取服务项目统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getServiceStatistics();
}
