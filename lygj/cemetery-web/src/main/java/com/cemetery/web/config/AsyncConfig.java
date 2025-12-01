package com.cemetery.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 异步任务配置
 * 类比：启用项目的异步处理能力，就像开启多个工作通道，可以同时处理多个任务
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {
    // Spring Boot 会自动配置默认的异步执行器
}
