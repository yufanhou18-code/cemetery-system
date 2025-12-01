package com.cemetery.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

/**
 * 支付处理器（模拟第三方支付）
 * 类比：像支付宝、微信支付的处理系统，负责实际的支付扣款
 */
@Slf4j
@Component
public class PaymentProcessor {
    
    private final Random random = new Random();
    
    /**
     * 模拟支付处理（90%成功率）
     * @param paymentNo 支付流水号
     * @param amount 支付金额
     * @return 支付结果
     */
    public PaymentResult processPayment(String paymentNo, String amount) {
        log.info("开始模拟支付处理，支付流水号: {}, 金额: {}", paymentNo, amount);
        
        try {
            // 模拟网络延迟
            Thread.sleep(1000 + random.nextInt(2000)); // 1-3秒随机延迟
            
            // 90%成功率
            boolean success = random.nextInt(100) < 90;
            
            if (success) {
                String transactionId = generateTransactionId();
                log.info("支付处理成功，支付流水号: {}, 第三方交易号: {}", paymentNo, transactionId);
                return PaymentResult.success(transactionId);
            } else {
                String failReason = getRandomFailReason();
                log.warn("支付处理失败，支付流水号: {}, 失败原因: {}", paymentNo, failReason);
                return PaymentResult.failed(failReason);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("支付处理被中断，支付流水号: {}", paymentNo, e);
            return PaymentResult.failed("支付处理被中断");
        } catch (Exception e) {
            log.error("支付处理异常，支付流水号: {}", paymentNo, e);
            return PaymentResult.failed("系统异常: " + e.getMessage());
        }
    }
    
    /**
     * 生成第三方交易号
     * @return 交易号
     */
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 获取随机失败原因
     * @return 失败原因
     */
    private String getRandomFailReason() {
        String[] reasons = {
            "余额不足",
            "银行卡状态异常",
            "支付密码错误",
            "交易限额超限",
            "网络超时",
            "银行系统维护中"
        };
        return reasons[random.nextInt(reasons.length)];
    }
    
    /**
     * 支付结果
     */
    @lombok.Data
    public static class PaymentResult {
        private boolean success;
        private String transactionId;
        private String failReason;
        
        public static PaymentResult success(String transactionId) {
            PaymentResult result = new PaymentResult();
            result.setSuccess(true);
            result.setTransactionId(transactionId);
            return result;
        }
        
        public static PaymentResult failed(String failReason) {
            PaymentResult result = new PaymentResult();
            result.setSuccess(false);
            result.setFailReason(failReason);
            return result;
        }
    }
}
