package com.cemetery.domain.dto;

import com.cemetery.domain.enums.OrderStatusEnum;
import com.cemetery.domain.enums.ServiceTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务订单DTO
 */
@Data
public class ServiceOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 服务类型
     */
    @NotNull(message = "服务类型不能为空")
    private ServiceTypeEnum serviceType;

    /**
     * 墓位ID
     */
    private Long tombId;

    /**
     * 下单用户ID
     */
    private Long userId;

    /**
     * 家属ID
     */
    private Long familyId;

    /**
     * 服务名称
     */
    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    /**
     * 服务描述
     */
    private String serviceDesc;

    /**
     * 订单总金额
     */
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    @NotNull(message = "实付金额不能为空")
    private BigDecimal actualAmount;

    /**
     * 订单状态
     */
    private OrderStatusEnum status;

    /**
     * 预约服务日期
     */
    private Date appointmentDate;

    /**
     * 预约时段
     */
    private String appointmentTime;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 服务人员ID
     */
    private Long serviceStaffId;

    /**
     * 备注
     */
    private String remark;
}
