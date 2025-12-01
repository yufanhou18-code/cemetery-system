package com.cemetery.web.task;

import com.cemetery.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 支付超时检查定时任务
 * 类比：就像一个定时闹钟，每隔一段时间自动检查有没有过期的支付订单
 */
@Component
public class PaymentTimeoutTask {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentTimeoutTask.class);
    
    private final PaymentService paymentService;
    
    public PaymentTimeoutTask(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    /**
     * 每5分钟检查一次支付超时
     * cron表达式：每5分钟的第0秒执行
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkTimeout() {
        log.info("定时任务：开始检查支付超时");
        try {
            paymentService.checkPaymentTimeout();
        } catch (Exception e) {
            log.error("支付超时检查任务异常", e);
        }
    }
}
