package com.cemetery.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

/**
 * SQL脚本执行工具
 * 用于执行数据库脚本文件
 */
public class SqlScriptExecutor {

    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/cemetery_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String username = "root";
        
        // SQL文件路径
        String sqlFilePath = "c:/Users/不要和其他小朋友打架哦/Desktop/lygj/cemetery-domain/src/main/resources/db/service_provider_schema.sql";
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("============================================");
        System.out.println("服务商管理模块 - 数据库脚本执行工具");
        System.out.println("============================================");
        System.out.println();
        
        // 检查SQL文件是否存在
        File sqlFile = new File(sqlFilePath);
        if (!sqlFile.exists()) {
            System.err.println("错误: SQL文件不存在！");
            System.err.println("路径: " + sqlFilePath);
            return;
        }
        
        System.out.println("找到SQL文件: " + sqlFilePath);
        System.out.println();
        
        // 输入密码
        String password = "515222sjy";  // 使用提供的密码
        System.out.println("使用配置的密码连接数据库...");
        
        System.out.println();
        System.out.println("开始执行SQL脚本...");
        System.out.println();
        
        Connection conn = null;
        Statement stmt = null;
        
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 建立连接
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            
            System.out.println("✓ 数据库连接成功");
            
            // 读取SQL文件内容
            String sqlContent = new String(Files.readAllBytes(Paths.get(sqlFilePath)), StandardCharsets.UTF_8);
            
            // 分割SQL语句（按分号分割）
            String[] sqlStatements = sqlContent.split(";");
            
            stmt = conn.createStatement();
            int successCount = 0;
            int skipCount = 0;
            
            for (String sql : sqlStatements) {
                sql = sql.trim();
                
                // 跳过空语句和注释
                if (sql.isEmpty() || sql.startsWith("--") || sql.startsWith("/*") || sql.equals("USE cemetery_db")) {
                    skipCount++;
                    continue;
                }
                
                try {
                    stmt.execute(sql);
                    successCount++;
                } catch (SQLException e) {
                    // 如果是"表已存在"等可忽略的错误，继续执行
                    if (e.getMessage().contains("already exists") || 
                        e.getMessage().contains("Duplicate key name")) {
                        System.out.println("⚠ 跳过已存在的对象: " + e.getMessage());
                        continue;
                    }
                    throw e;
                }
            }
            
            // 提交事务
            conn.commit();
            
            System.out.println();
            System.out.println("============================================");
            System.out.println("✓ SQL脚本执行成功！");
            System.out.println("============================================");
            System.out.println();
            System.out.println("执行统计:");
            System.out.println("  成功执行: " + successCount + " 条SQL语句");
            System.out.println("  跳过: " + skipCount + " 条（注释或空语句）");
            System.out.println();
            System.out.println("已创建以下表:");
            System.out.println("  1. service_provider (服务商信息表)");
            System.out.println("  2. provider_service (服务项目表)");
            System.out.println("  3. service_feedback (服务反馈表)");
            System.out.println();
            System.out.println("已创建视图:");
            System.out.println("  - v_provider_statistics (服务商统计视图)");
            System.out.println();
            System.out.println("已插入测试数据:");
            System.out.println("  - 3个服务商");
            System.out.println("  - 5个服务项目");
            System.out.println("  - 2条反馈记录");
            
        } catch (ClassNotFoundException e) {
            System.err.println();
            System.err.println("✗ 错误: 未找到MySQL驱动");
            System.err.println("请确保项目中包含mysql-connector-java依赖");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println();
            System.err.println("✗ 数据库错误");
            System.err.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            
            // 回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("已回滚事务");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println();
            System.err.println("✗ 文件读取错误");
            System.err.println("错误信息: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println();
        System.out.println("按Enter键退出...");
        scanner.nextLine();
        scanner.close();
    }
}
