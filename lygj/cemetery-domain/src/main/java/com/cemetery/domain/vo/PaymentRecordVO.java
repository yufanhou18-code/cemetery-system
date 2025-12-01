package com.cemetery.domain.vo;

import com.cemetery.domain.enums.PaymentMethodEnum;
import com.cemetery.domain.enums.PaymentStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录VO
 */
@Data
public class PaymentRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 支付流水号
     */
    private String paymentNo;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付方式
     */
    private PaymentMethodEnum paymentMethod;

    /**
     * 支付渠道
     */
    private String paymentChannel;

    /**
     * 第三方支付交易号
     */
    private String transactionId;

    /**
     * 支付状态
     */
    private PaymentStatusEnum paymentStatus;

    /**
     * 支付完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paymentTime;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 付款账号
     */
    private String payerAccount;

    /**
     * 收款账号
     */
    private String payeeAccount;

    /**
     * 支付通知时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date notifyTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
}
