package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemetery.domain.entity.ServiceOrder;
import com.cemetery.domain.enums.OrderStatusEnum;
import com.cemetery.domain.enums.ServiceTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 服务订单Mapper接口
 */
@Mapper
public interface ServiceOrderMapper extends BaseMapper<ServiceOrder> {

    /**
     * 根据订单编号查询
     * @param orderNo 订单编号
     * @return 订单信息
     */
    ServiceOrder selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<ServiceOrder> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据墓位ID查询订单列表
     * @param tombId 墓位ID
     * @return 订单列表
     */
    List<ServiceOrder> selectByTombId(@Param("tombId") Long tombId);

    /**
     * 根据订单状态查询
     * @param status 订单状态
     * @return 订单列表
     */
    List<ServiceOrder> selectByStatus(@Param("status") OrderStatusEnum status);

    /**
     * 根据服务类型查询
     * @param serviceType 服务类型
     * @return 订单列表
     */
    List<ServiceOrder> selectByServiceType(@Param("serviceType") ServiceTypeEnum serviceType);

    /**
     * 根据预约日期查询订单
     * @param appointmentDate 预约日期
     * @return 订单列表
     */
    List<ServiceOrder> selectByAppointmentDate(@Param("appointmentDate") Date appointmentDate);

    /**
     * 查询用户指定状态的订单
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<ServiceOrder> selectByUserIdAndStatus(@Param("userId") Long userId, 
                                                 @Param("status") OrderStatusEnum status);

    /**
     * 查询待支付订单
     * @return 待支付订单列表
     */
    List<ServiceOrder> selectPendingPaymentOrders();

    /**
     * 根据服务人员ID查询订单
     * @param staffId 服务人员ID
     * @return 订单列表
     */
    List<ServiceOrder> selectByServiceStaffId(@Param("staffId") Long staffId);
}
