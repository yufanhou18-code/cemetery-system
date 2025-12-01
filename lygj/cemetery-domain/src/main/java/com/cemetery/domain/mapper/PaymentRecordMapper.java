package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemetery.domain.entity.PaymentRecord;
import com.cemetery.domain.enums.PaymentStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付记录Mapper接口
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 根据支付流水号查询
     * @param paymentNo 支付流水号
     * @return 支付记录
     */
    PaymentRecord selectByPaymentNo(@Param("paymentNo") String paymentNo);

    /**
     * 根据订单ID查询支付记录
     * @param orderId 订单ID
     * @return 支付记录列表
     */
    List<PaymentRecord> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单编号查询
     * @param orderNo 订单编号
     * @return 支付记录列表
     */
    List<PaymentRecord> selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据用户ID查询支付记录
     * @param userId 用户ID
     * @return 支付记录列表
     */
    List<PaymentRecord> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据支付状态查询
     * @param status 支付状态
     * @return 支付记录列表
     */
    List<PaymentRecord> selectByStatus(@Param("status") PaymentStatusEnum status);

    /**
     * 根据第三方交易号查询
     * @param transactionId 第三方交易号
     * @return 支付记录
     */
    PaymentRecord selectByTransactionId(@Param("transactionId") String transactionId);

    /**
     * 查询待支付记录
     * @return 待支付记录列表
     */
    List<PaymentRecord> selectPendingPayments();

    /**
     * 查询用户指定状态的支付记录
     * @param userId 用户ID
     * @param status 支付状态
     * @return 支付记录列表
     */
    List<PaymentRecord> selectByUserIdAndStatus(@Param("userId") Long userId, 
                                                  @Param("status") PaymentStatusEnum status);
}
