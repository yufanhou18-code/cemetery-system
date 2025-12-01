-- ============================================
-- 数据库逻辑修复补丁
-- 作用：补全 schema.sql 和扩展脚本之间缺失的关联数据
-- 使用顺序：schema.sql -> 本补丁 -> service_provider_schema.sql -> digital_memorial_schema.sql
-- ============================================

USE cemetery_db;

-- --------------------------------------------
-- 1. 补全 sys_user 表缺失的用户 (ID 1000-1008)
-- 解决：服务商表(service_provider)外键报错
-- --------------------------------------------
DELETE FROM `sys_user` WHERE `id` BETWEEN 1000 AND 1008; -- 防止重复先清理

INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `user_type`, `status`) VALUES
(1000, 'user1000', '$2a$10$7JB720yubVSZvUI0rEqK', '测试用户A', 3, 1),
(1001, 'provider1001', '$2a$10$7JB720yubVSZvUI0rEqK', '福缘服务商', 2, 1),
(1002, 'provider1002', '$2a$10$7JB720yubVSZvUI0rEqK', '心诚鲜花', 2, 1),
(1003, 'provider1003', '$2a$10$7JB720yubVSZvUI0rEqK', '慎终法事', 2, 1),
(1004, 'user1004', '$2a$10$7JB720yubVSZvUI0rEqK', '张伟', 3, 1),
(1005, 'user1005', '$2a$10$7JB720yubVSZvUI0rEqK', '王建国', 3, 1),
(1006, 'user1006', '$2a$10$7JB720yubVSZvUI0rEqK', '李芳', 3, 1),
(1007, 'user1007', '$2a$10$7JB720yubVSZvUI0rEqK', '老战友', 3, 1),
(1008, 'user1008', '$2a$10$7JB720yubVSZvUI0rEqK', '陈明', 3, 1);

-- --------------------------------------------
-- 2. 补全 service_order 表缺失的订单 (ID 100001, 100002)
-- 解决：服务评价表(service_feedback)外键报错
-- --------------------------------------------
DELETE FROM `service_order` WHERE `id` IN (100001, 100002); -- 防止重复先清理

INSERT INTO `service_order` 
(`id`, `order_no`, `service_type`, `tomb_id`, `user_id`, `service_name`, `total_amount`, `actual_amount`, `status`) 
VALUES
(100001, 'SO_PATCH_001', 4, 10003, 3, '补丁订单1-鲜花', 100.00, 100.00, 4),
(100002, 'SO_PATCH_002', 4, 10003, 3, '补丁订单2-维护', 200.00, 200.00, 4);

-- --------------------------------------------
-- 3. 补全 deceased_info 表缺失的逝者 (ID 10001, 10002)
-- 解决：数字纪念馆(digital_memorial)外键报错
-- --------------------------------------------
DELETE FROM `deceased_info` WHERE `id` IN (10001, 10002); -- 防止重复先清理

INSERT INTO `deceased_info` (`id`, `deceased_name`, `gender`, `tomb_id`, `burial_date`) VALUES
(10001, '王芳', 0, 10000, '2024-03-09'),
(10002, '张国强', 1, 10000, '2022-08-14');

SELECT '✅ 数据库补丁执行完成，缺失数据已补全！' as result;