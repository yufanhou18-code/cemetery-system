package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.PaymentRecordDTO;
import com.cemetery.domain.enums.PaymentStatusEnum;
import com.cemetery.domain.vo.PaymentRecordVO;
import com.cemetery.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付管理控制器
 */
@Api(tags = "支付管理")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @ApiOperation("创建支付记录")
    @PostMapping
    public Result<Long> createPayment(@Validated @RequestBody PaymentRecordDTO paymentDTO) {
        Long paymentId = paymentService.createPaymentRecord(paymentDTO);
        return Result.success("支付记录创建成功", paymentId);
    }

    @ApiOperation("根据支付流水号查询")
    @GetMapping("/payment-no/{paymentNo}")
    public Result<PaymentRecordVO> getPaymentByPaymentNo(@PathVariable String paymentNo) {
        PaymentRecordVO paymentVO = paymentService.getPaymentByPaymentNo(paymentNo);
        return Result.success(paymentVO);
    }

    @ApiOperation("分页查询支付记录列表")
    @PostMapping("/page")
    public Result<Page<PaymentRecordVO>> pagePayments(@RequestBody PageQueryDTO pageQueryDTO) {
        Page<PaymentRecordVO> page = paymentService.pagePayments(pageQueryDTO);
        return Result.success(page);
    }

    @ApiOperation("根据订单ID查询支付记录列表")
    @GetMapping("/order/{orderId}")
    public Result<List<PaymentRecordVO>> getPaymentsByOrderId(@PathVariable Long orderId) {
        List<PaymentRecordVO> payments = paymentService.getPaymentsByOrderId(orderId);
        return Result.success(payments);
    }

    @ApiOperation("根据订单编号查询支付记录列表")
    @GetMapping("/order-no/{orderNo}")
    public Result<List<PaymentRecordVO>> getPaymentsByOrderNo(@PathVariable String orderNo) {
        List<PaymentRecordVO> payments = paymentService.getPaymentsByOrderNo(orderNo);
        return Result.success(payments);
    }

    @ApiOperation("根据用户ID查询支付记录列表")
    @GetMapping("/user/{userId}")
    public Result<List<PaymentRecordVO>> getPaymentsByUserId(@PathVariable Long userId) {
        List<PaymentRecordVO> payments = paymentService.getPaymentsByUserId(userId);
        return Result.success(payments);
    }

    @ApiOperation("支付成功回调")
    @PostMapping("/success")
    public Result<Void> paymentSuccess(
            @RequestParam String paymentNo,
            @RequestParam String transactionId) {
        paymentService.handlePaymentSuccess(paymentNo, transactionId);
        return Result.success();
    }

    @ApiOperation("支付失败处理")
    @PostMapping("/failure")
    public Result<Void> paymentFailure(
            @RequestParam String paymentNo,
            @RequestParam String failReason) {
        paymentService.handlePaymentFailed(paymentNo, failReason);
        return Result.success();
    }

    @ApiOperation("申请退款")
    @PostMapping("/refund/apply")
    public Result<Void> applyRefund(
            @RequestParam String paymentNo,
            @RequestParam String refundReason) {
        paymentService.applyRefund(paymentNo, refundReason);
        return Result.success();
    }

    @ApiOperation("处理退款成功")
    @PostMapping("/refund/success")
    public Result<Void> handleRefundSuccess(@RequestParam String paymentNo) {
        paymentService.handleRefundSuccess(paymentNo);
        return Result.success();
    }

    @ApiOperation("修改支付状态")
    @PostMapping("/change-status")
    public Result<Void> changePaymentStatus(
            @RequestParam String paymentNo,
            @RequestParam Integer status) {
        PaymentStatusEnum statusEnum = PaymentStatusEnum.values()[status];
        paymentService.changePaymentStatus(paymentNo, statusEnum);
        return Result.success();
    }

    @ApiOperation("查询待支付记录")
    @GetMapping("/pending")
    public Result<List<PaymentRecordVO>> getPendingPayments() {
        List<PaymentRecordVO> payments = paymentService.getPendingPayments();
        return Result.success(payments);
    }

    @ApiOperation("异步处理支付（使用状态机）")
    @PostMapping("/process/{paymentNo}")
    public Result<String> processPayment(@PathVariable String paymentNo) {
        paymentService.processPaymentAsync(paymentNo);
        return Result.success("支付处理已提交，请稍后查询结果", "处理中");
    }

    @ApiOperation("模拟支付回调")
    @PostMapping("/callback")
    public Result<String> paymentCallback(
            @RequestParam String paymentNo,
            @RequestParam boolean success,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String failReason) {
        paymentService.handlePaymentCallback(paymentNo, success, transactionId, failReason);
        return Result.success("支付回调处理成功", "OK");
    }

    @ApiOperation("手动触发超时检查")
    @PostMapping("/check-timeout")
    public Result<String> checkTimeout() {
        paymentService.checkPaymentTimeout();
        return Result.success("超时检查完成", "OK");
    }
}
