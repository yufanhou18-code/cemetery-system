package com.cemetery.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 陵园管家系统启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.cemetery"})
@MapperScan("com.cemetery.domain.mapper")
public class CemeteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CemeteryApplication.class, args);
        System.out.println("============================================");
        System.out.println("    陵园管家系统启动成功！");
        System.out.println("    接口文档地址：http://localhost:8080/doc.html");
        System.out.println("============================================");
    }
}
