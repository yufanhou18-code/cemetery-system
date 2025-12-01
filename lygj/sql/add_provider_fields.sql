-- ============================================
-- 服务商管理模块 - 添加缺失字段脚本
-- 版本: 1.0.1
-- 创建日期: 2025-11-26
-- 说明：为已存在的表添加统计字段（如果字段不存在）
-- ============================================

USE cemetery_db;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- ============================================
-- 检查并添加 service_provider 表的统计字段
-- ============================================

-- 添加 service_count 字段（累计服务次数）
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'cemetery_db' 
  AND TABLE_NAME = 'service_provider' 
  AND COLUMN_NAME = 'service_count';

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `service_provider` ADD COLUMN `service_count` INT(11) DEFAULT 0 COMMENT ''累计服务次数'' AFTER `rating`',
    'SELECT ''字段 service_count 已存在，跳过'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 complaint_count 字段（投诉次数）
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'cemetery_db' 
  AND TABLE_NAME = 'service_provider' 
  AND COLUMN_NAME = 'complaint_count';

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `service_provider` ADD COLUMN `complaint_count` INT(11) DEFAULT 0 COMMENT ''投诉次数'' AFTER `service_count`',
    'SELECT ''字段 complaint_count 已存在，跳过'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 验证字段是否添加成功
-- ============================================
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    COLUMN_TYPE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'cemetery_db'
  AND TABLE_NAME = 'service_provider'
  AND COLUMN_NAME IN ('service_count', 'complaint_count')
ORDER BY ORDINAL_POSITION;

SELECT '✓ 服务商表字段检查完成！' AS status;
