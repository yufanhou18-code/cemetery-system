package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ServiceFeedbackDTO;
import com.cemetery.domain.dto.ServiceFeedbackQueryDTO;
import com.cemetery.domain.vo.ServiceFeedbackVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 服务反馈管理服务接口
 */
public interface IServiceFeedbackService {

    /**
     * 创建服务反馈
     *
     * @param feedbackDTO 反馈信息
     * @return 反馈ID
     */
    Long createFeedback(ServiceFeedbackDTO feedbackDTO);

    /**
     * 更新服务反馈
     *
     * @param feedbackDTO 反馈信息
     */
    void updateFeedback(ServiceFeedbackDTO feedbackDTO);

    /**
     * 删除服务反馈
     *
     * @param feedbackId 反馈ID
     */
    void deleteFeedback(Long feedbackId);

    /**
     * 获取反馈详情
     *
     * @param feedbackId 反馈ID
     * @return 反馈详情
     */
    ServiceFeedbackVO getFeedbackDetail(Long feedbackId);

    /**
     * 分页查询反馈
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<ServiceFeedbackVO> pageFeedbacks(ServiceFeedbackQueryDTO queryDTO);

    /**
     * 获取服务商的所有反馈
     *
     * @param providerId 服务商ID
     * @return 反馈列表
     */
    List<ServiceFeedbackVO> getFeedbacksByProvider(Long providerId);

    /**
     * 获取服务项目的所有反馈
     *
     * @param serviceId 服务项目ID
     * @return 反馈列表
     */
    List<ServiceFeedbackVO> getFeedbacksByService(Long serviceId);

    /**
     * 获取用户的所有反馈
     *
     * @param userId 用户ID
     * @return 反馈列表
     */
    List<ServiceFeedbackVO> getFeedbacksByUser(Long userId);

    /**
     * 回复反馈
     *
     * @param feedbackId 反馈ID
     * @param replyContent 回复内容
     */
    void replyFeedback(Long feedbackId, String replyContent);

    /**
     * 审核反馈
     *
     * @param feedbackId 反馈ID
     * @param approved 是否通过
     * @param auditRemark 审核备注
     */
    void auditFeedback(Long feedbackId, Boolean approved, String auditRemark);

    /**
     * 标记为精选反馈
     *
     * @param feedbackId 反馈ID
     */
    void markAsFeatured(Long feedbackId);

    /**
     * 取消精选反馈
     *
     * @param feedbackId 反馈ID
     */
    void unmarkAsFeatured(Long feedbackId);

    /**
     * 增加点赞数
     *
     * @param feedbackId 反馈ID
     */
    void incrementLikeCount(Long feedbackId);

    /**
     * 获取精选反馈
     *
     * @param limit 返回数量
     * @return 精选反馈列表
     */
    List<ServiceFeedbackVO> getFeaturedFeedbacks(Integer limit);

    /**
     * 计算服务商平均评分
     *
     * @param providerId 服务商ID
     * @return 平均评分
     */
    BigDecimal calculateProviderAverageRating(Long providerId);

    /**
     * 计算服务项目平均评分
     *
     * @param serviceId 服务项目ID
     * @return 平均评分
     */
    BigDecimal calculateServiceAverageRating(Long serviceId);

    /**
     * 获取反馈统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getFeedbackStatistics();
}
