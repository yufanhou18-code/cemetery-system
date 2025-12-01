package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ServiceOrderDTO;
import com.cemetery.domain.enums.OrderStatusEnum;
import com.cemetery.domain.vo.ServiceOrderVO;
import com.cemetery.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 订单管理控制器
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping
    public Result<Long> createOrder(@Validated @RequestBody ServiceOrderDTO orderDTO) {
        Long orderId = orderService.createOrder(orderDTO);
        return Result.success("订单创建成功", orderId);
    }

    @ApiOperation("更新订单")
    @PutMapping
    public Result<Void> updateOrder(@Validated @RequestBody ServiceOrderDTO orderDTO) {
        orderService.updateOrder(orderDTO);
        return Result.success();
    }

    @ApiOperation("取消订单")
    @PostMapping("/{orderId}/cancel")
    public Result<Void> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String cancelReason) {
        orderService.cancelOrder(orderId, cancelReason);
        return Result.success();
    }

    @ApiOperation("根据ID查询订单")
    @GetMapping("/{orderId}")
    public Result<ServiceOrderVO> getOrderById(@PathVariable Long orderId) {
        ServiceOrderVO orderVO = orderService.getOrderById(orderId);
        return Result.success(orderVO);
    }

    @ApiOperation("分页查询订单列表")
    @PostMapping("/page")
    public Result<Page<ServiceOrderVO>> pageOrders(@RequestBody PageQueryDTO pageQueryDTO) {
        Page<ServiceOrderVO> page = orderService.pageOrders(pageQueryDTO);
        return Result.success(page);
    }

    @ApiOperation("根据订单编号查询")
    @GetMapping("/order-no/{orderNo}")
    public Result<ServiceOrderVO> getOrderByOrderNo(@PathVariable String orderNo) {
        ServiceOrderVO orderVO = orderService.getOrderByOrderNo(orderNo);
        return Result.success(orderVO);
    }

    @ApiOperation("根据用户ID查询订单列表")
    @GetMapping("/user/{userId}")
    public Result<List<ServiceOrderVO>> getOrdersByUserId(@PathVariable Long userId) {
        List<ServiceOrderVO> orders = orderService.getOrdersByUserId(userId);
        return Result.success(orders);
    }

    @ApiOperation("根据墓位ID查询订单列表")
    @GetMapping("/tomb/{tombId}")
    public Result<List<ServiceOrderVO>> getOrdersByTombId(@PathVariable Long tombId) {
        List<ServiceOrderVO> orders = orderService.getOrdersByTombId(tombId);
        return Result.success(orders);
    }

    @ApiOperation("更新订单状态")
    @PostMapping("/{orderId}/status")
    public Result<Void> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Integer status) {
        OrderStatusEnum statusEnum = OrderStatusEnum.values()[status];
        orderService.changeOrderStatus(orderId, statusEnum);
        return Result.success();
    }

    @ApiOperation("分配服务人员")
    @PostMapping("/{orderId}/assign-staff")
    public Result<Void> assignStaff(
            @PathVariable Long orderId,
            @RequestParam Long staffId) {
        orderService.assignStaff(orderId, staffId);
        return Result.success();
    }

    @ApiOperation("开始服务")
    @PostMapping("/{orderId}/start-service")
    public Result<Void> startService(@PathVariable Long orderId) {
        orderService.startService(orderId);
        return Result.success();
    }

    @ApiOperation("完成服务")
    @PostMapping("/{orderId}/complete-service")
    public Result<Void> completeService(@PathVariable Long orderId) {
        orderService.completeService(orderId);
        return Result.success();
    }

    @ApiOperation("查询待支付订单")
    @GetMapping("/pending-payment")
    public Result<List<ServiceOrderVO>> getPendingPaymentOrders() {
        List<ServiceOrderVO> orders = orderService.getPendingPaymentOrders();
        return Result.success(orders);
    }

    @ApiOperation("根据预约日期查询订单")
    @GetMapping("/appointment-date")
    public Result<List<ServiceOrderVO>> getOrdersByAppointmentDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date appointmentDate) {
        List<ServiceOrderVO> orders = orderService.getOrdersByAppointmentDate(appointmentDate);
        return Result.success(orders);
    }
}
