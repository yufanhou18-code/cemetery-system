package com.cemetery.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cemetery.domain.enums.OrderStatusEnum;
import com.cemetery.domain.enums.ServiceTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务订单实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_order")
public class ServiceOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号（唯一）
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 服务类型（1-墓位购买，2-祭扫预约，3-代客祭扫，4-鲜花购买，5-管理费续费，6-墓碑维护，7-其他服务）
     */
    @TableField("service_type")
    private ServiceTypeEnum serviceType;

    /**
     * 墓位ID（关联tomb_location）
     */
    @TableField("tomb_id")
    private Long tombId;

    /**
     * 下单用户ID（关联sys_user）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 家属ID（关联family_member）
     */
    @TableField("family_id")
    private Long familyId;

    /**
     * 服务名称
     */
    @TableField("service_name")
    private String serviceName;

    /**
     * 服务描述
     */
    @TableField("service_desc")
    private String serviceDesc;

    /**
     * 订单总金额（元）
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 优惠金额（元）
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    /**
     * 实付金额（元）
     */
    @TableField("actual_amount")
    private BigDecimal actualAmount;

    /**
     * 订单状态（1-待支付，2-已支付，3-服务中，4-已完成，5-已取消，6-已退款）
     */
    @TableField("status")
    private OrderStatusEnum status;

    /**
     * 预约服务日期
     */
    @TableField("appointment_date")
    private Date appointmentDate;

    /**
     * 预约时段
     */
    @TableField("appointment_time")
    private String appointmentTime;

    /**
     * 联系人姓名
     */
    @TableField("contact_name")
    private String contactName;

    /**
     * 联系人电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 服务人员ID（关联sys_user）
     */
    @TableField("service_staff_id")
    private Long serviceStaffId;

    /**
     * 服务开始时间
     */
    @TableField("service_start_time")
    private Date serviceStartTime;

    /**
     * 服务结束时间
     */
    @TableField("service_end_time")
    private Date serviceEndTime;

    /**
     * 取消原因
     */
    @TableField("cancel_reason")
    private String cancelReason;

    /**
     * 取消时间
     */
    @TableField("cancel_time")
    private Date cancelTime;
}
