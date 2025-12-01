package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.PaymentMethodEnum;
import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment_record")
public class PaymentRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 支付流水号（唯一）
     */
    @TableField("payment_no")
    private String paymentNo;

    /**
     * 订单ID（关联service_order）
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 支付用户ID（关联sys_user）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 支付金额（元）
     */
    @TableField("payment_amount")
    private BigDecimal paymentAmount;

    /**
     * 支付方式（1-微信支付，2-支付宝，3-现金，4-刷卡，5-转账）
     */
    @TableField("payment_method")
    private PaymentMethodEnum paymentMethod;

    /**
     * 支付渠道（如：WX_JSAPI、ALIPAY_APP）
     */
    @TableField("payment_channel")
    private String paymentChannel;

    /**
     * 第三方支付交易号
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 支付状态（1-待支付，2-支付中，3-支付成功，4-支付失败，5-已退款）
     */
    @TableField("payment_status")
    private PaymentStatusEnum paymentStatus;

    /**
     * 支付完成时间
     */
    @TableField("payment_time")
    private Date paymentTime;

    /**
     * 退款金额（元）
     */
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    /**
     * 退款时间
     */
    @TableField("refund_time")
    private Date refundTime;

    /**
     * 退款原因
     */
    @TableField("refund_reason")
    private String refundReason;

    /**
     * 付款账号
     */
    @TableField("payer_account")
    private String payerAccount;

    /**
     * 收款账号
     */
    @TableField("payee_account")
    private String payeeAccount;

    /**
     * 支付通知时间
     */
    @TableField("notify_time")
    private Date notifyTime;

    /**
     * 支付回调数据
     */
    @TableField("notify_data")
    private String notifyData;
}
