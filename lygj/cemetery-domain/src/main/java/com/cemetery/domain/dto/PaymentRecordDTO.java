package com.cemetery.domain.dto;

import com.cemetery.domain.enums.PaymentMethodEnum;
import com.cemetery.domain.enums.PaymentStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付记录DTO
 */
@Data
public class PaymentRecordDTO implements Serializable {

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
    @NotNull(message = "订单ID不能为空")
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
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    private BigDecimal paymentAmount;

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
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
     * 付款账号
     */
    private String payerAccount;

    /**
     * 收款账号
     */
    private String payeeAccount;

    /**
     * 备注
     */
    private String remark;
}
