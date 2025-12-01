package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ServiceOrderDTO;
import com.cemetery.domain.enums.OrderStatusEnum;
import com.cemetery.domain.vo.ServiceOrderVO;

import java.util.Date;
import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     * @param serviceOrderDTO 订单DTO
     * @return 订单ID
     */
    Long createOrder(ServiceOrderDTO serviceOrderDTO);

    /**
     * 更新订单
     * @param serviceOrderDTO 订单DTO
     */
    void updateOrder(ServiceOrderDTO serviceOrderDTO);

    /**
     * 取消订单
     * @param orderId 订单ID
     * @param cancelReason 取消原因
     */
    void cancelOrder(Long orderId, String cancelReason);

    /**
     * 根据ID查询订单
     * @param orderId 订单ID
     * @return 订单VO
     */
    ServiceOrderVO getOrderById(Long orderId);

    /**
     * 根据订单编号查询
     * @param orderNo 订单编号
     * @return 订单VO
     */
    ServiceOrderVO getOrderByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<ServiceOrderVO> getOrdersByUserId(Long userId);

    /**
     * 根据墓位ID查询订单列表
     * @param tombId 墓位ID
     * @return 订单列表
     */
    List<ServiceOrderVO> getOrdersByTombId(Long tombId);

    /**
     * 分页查询订单
     * @param pageQueryDTO 分页查询参数
     * @return 订单分页列表
     */
    Page<ServiceOrderVO> pageOrders(PageQueryDTO pageQueryDTO);

    /**
     * 修改订单状态
     * @param orderId 订单ID
     * @param status 状态
     */
    void changeOrderStatus(Long orderId, OrderStatusEnum status);

    /**
     * 分配服务人员
     * @param orderId 订单ID
     * @param staffId 服务人员ID
     */
    void assignStaff(Long orderId, Long staffId);

    /**
     * 开始服务
     * @param orderId 订单ID
     */
    void startService(Long orderId);

    /**
     * 完成服务
     * @param orderId 订单ID
     */
    void completeService(Long orderId);

    /**
     * 查询待支付订单
     * @return 待支付订单列表
     */
    List<ServiceOrderVO> getPendingPaymentOrders();

    /**
     * 根据预约日期查询订单
     * @param appointmentDate 预约日期
     * @return 订单列表
     */
    List<ServiceOrderVO> getOrdersByAppointmentDate(Date appointmentDate);
}
