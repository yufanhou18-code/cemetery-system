-- ============================================
-- 服务商管理模块 - 数据库扩展脚本
-- 版本: 1.0.0
-- 创建日期: 2025-11-26
-- 功能：服务商信息、服务项目、服务反馈管理
-- ============================================

USE cemetery_db;

-- 设置会话字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- ============================================
-- 1. 服务商信息表（service_provider）
-- 功能：管理墓园合作的服务商基本信息
-- ============================================
CREATE TABLE IF NOT EXISTS `service_provider` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '服务商ID',
  `provider_no` VARCHAR(50) NOT NULL COMMENT '服务商编号（唯一标识）',
  `user_id` BIGINT(20) NOT NULL COMMENT '关联用户ID（关联sys_user，user_type=2）',
  `provider_name` VARCHAR(100) NOT NULL COMMENT '服务商名称',
  `provider_type` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '服务商类型（1-个人，2-企业）',
  `business_license` VARCHAR(50) DEFAULT NULL COMMENT '营业执照号（企业类型必填）',
  `legal_person` VARCHAR(50) DEFAULT NULL COMMENT '法人代表',
  `contact_person` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '服务商地址',
  `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
  `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
  `district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
  `service_scope` VARCHAR(500) DEFAULT NULL COMMENT '服务范围（多个用逗号分隔）',
  `business_hours` VARCHAR(100) DEFAULT NULL COMMENT '营业时间（如：周一至周日 8:00-18:00）',
  `description` TEXT COMMENT '服务商简介',
  `logo` VARCHAR(500) DEFAULT NULL COMMENT '服务商Logo地址',
  `images` TEXT COMMENT '服务商图片（多个用逗号分隔）',
  `certificate_images` TEXT COMMENT '资质证书图片（多个用逗号分隔）',
  `rating` DECIMAL(3,2) DEFAULT 5.00 COMMENT '综合评分（0-5分）',
  `service_count` INT(11) DEFAULT 0 COMMENT '累计服务次数',
  `complaint_count` INT(11) DEFAULT 0 COMMENT '投诉次数',
  `status` TINYINT(1) DEFAULT 1 COMMENT '服务商状态（0-禁用，1-正常，2-审核中，3-审核拒绝）',
  `audit_status` TINYINT(1) DEFAULT 1 COMMENT '审核状态（1-待审核，2-审核通过，3-审核拒绝）',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `audit_by` BIGINT(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
  `join_date` DATE DEFAULT NULL COMMENT '入驻日期',
  `contract_start_date` DATE DEFAULT NULL COMMENT '合同开始日期',
  `contract_end_date` DATE DEFAULT NULL COMMENT '合同结束日期',
  `is_recommended` TINYINT(1) DEFAULT 0 COMMENT '是否推荐（0-否，1-是）',
  `sort_order` INT(11) DEFAULT 0 COMMENT '排序序号（数字越小越靠前）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_provider_no` (`provider_no`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_provider_name` (`provider_name`),
  KEY `idx_provider_type` (`provider_type`),
  KEY `idx_status` (`status`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_rating` (`rating` DESC),
  KEY `idx_city` (`province`, `city`),
  KEY `idx_is_recommended` (`is_recommended`, `sort_order`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_provider_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='服务商信息表';

-- ============================================
-- 2. 服务项目表（provider_service）
-- 功能：管理服务商提供的各类服务项目
-- ============================================
CREATE TABLE IF NOT EXISTS `provider_service` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '服务项目ID',
  `provider_id` BIGINT(20) NOT NULL COMMENT '服务商ID（关联service_provider）',
  `service_no` VARCHAR(50) NOT NULL COMMENT '服务项目编号',
  `service_name` VARCHAR(100) NOT NULL COMMENT '服务项目名称',
  `service_category` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '服务分类（1-祭扫服务，2-鲜花供品，3-墓碑维护，4-法事服务，5-接送服务，6-其他服务）',
  `service_type` TINYINT(1) DEFAULT 1 COMMENT '服务类型（1-代理服务，2-商品销售）',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '服务价格（元）',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价（元，用于显示折扣）',
  `price_unit` VARCHAR(20) DEFAULT '次' COMMENT '价格单位（次、小时、天、个等）',
  `description` TEXT COMMENT '服务详情描述',
  `service_content` TEXT COMMENT '服务内容（详细说明）',
  `service_process` TEXT COMMENT '服务流程',
  `duration` INT(11) DEFAULT NULL COMMENT '服务时长（分钟）',
  `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片',
  `images` TEXT COMMENT '服务图片（多个用逗号分隔）',
  `video_url` VARCHAR(500) DEFAULT NULL COMMENT '视频介绍地址',
  `stock` INT(11) DEFAULT -1 COMMENT '库存数量（-1表示不限库存）',
  `sales_count` INT(11) DEFAULT 0 COMMENT '销售数量',
  `view_count` INT(11) DEFAULT 0 COMMENT '浏览次数',
  `rating` DECIMAL(3,2) DEFAULT 5.00 COMMENT '服务评分（0-5分）',
  `review_count` INT(11) DEFAULT 0 COMMENT '评价数量',
  `status` TINYINT(1) DEFAULT 1 COMMENT '服务状态（0-下架，1-上架，2-售罄）',
  `is_hot` TINYINT(1) DEFAULT 0 COMMENT '是否热门（0-否，1-是）',
  `is_new` TINYINT(1) DEFAULT 0 COMMENT '是否新品（0-否，1-是）',
  `is_recommended` TINYINT(1) DEFAULT 0 COMMENT '是否推荐（0-否，1-是）',
  `sort_order` INT(11) DEFAULT 0 COMMENT '排序序号（数字越小越靠前）',
  `publish_time` DATETIME DEFAULT NULL COMMENT '上架时间',
  `offline_time` DATETIME DEFAULT NULL COMMENT '下架时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_service_no` (`service_no`),
  KEY `idx_provider_id` (`provider_id`, `status`),
  KEY `idx_service_name` (`service_name`),
  KEY `idx_service_category` (`service_category`),
  KEY `idx_price` (`price`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating` DESC),
  KEY `idx_sales_count` (`sales_count` DESC),
  KEY `idx_hot_new_recommend` (`is_hot`, `is_new`, `is_recommended`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_service_provider` FOREIGN KEY (`provider_id`) REFERENCES `service_provider` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='服务项目表';

-- 为服务描述字段创建全文索引（支持中文检索）
ALTER TABLE `provider_service` 
  ADD FULLTEXT INDEX `ft_service_desc` (`service_name`, `description`, `service_content`) WITH PARSER ngram;

-- ============================================
-- 3. 服务反馈表（service_feedback）
-- 功能：管理用户对服务商及服务项目的评价和反馈
-- ============================================
CREATE TABLE IF NOT EXISTS `service_feedback` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `feedback_no` VARCHAR(50) NOT NULL COMMENT '反馈编号',
  `order_id` BIGINT(20) NOT NULL COMMENT '订单ID（关联service_order）',
  `provider_id` BIGINT(20) NOT NULL COMMENT '服务商ID（关联service_provider）',
  `service_id` BIGINT(20) DEFAULT NULL COMMENT '服务项目ID（关联provider_service）',
  `user_id` BIGINT(20) NOT NULL COMMENT '评价用户ID（关联sys_user）',
  `feedback_type` TINYINT(1) DEFAULT 1 COMMENT '反馈类型（1-服务评价，2-投诉建议）',
  `rating` DECIMAL(3,2) NOT NULL DEFAULT 5.00 COMMENT '综合评分（0-5分）',
  `service_rating` DECIMAL(3,2) DEFAULT NULL COMMENT '服务态度评分',
  `quality_rating` DECIMAL(3,2) DEFAULT NULL COMMENT '服务质量评分',
  `speed_rating` DECIMAL(3,2) DEFAULT NULL COMMENT '响应速度评分',
  `content` TEXT NOT NULL COMMENT '反馈内容',
  `images` TEXT COMMENT '反馈图片（多个用逗号分隔）',
  `is_anonymous` TINYINT(1) DEFAULT 0 COMMENT '是否匿名（0-否，1-是）',
  `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶（0-否，1-是）',
  `like_count` INT(11) DEFAULT 0 COMMENT '点赞数',
  `reply_content` TEXT COMMENT '服务商回复内容',
  `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
  `reply_by` BIGINT(20) DEFAULT NULL COMMENT '回复人ID',
  `status` TINYINT(1) DEFAULT 1 COMMENT '反馈状态（1-待处理，2-已回复，3-已解决，4-已关闭）',
  `audit_status` TINYINT(1) DEFAULT 1 COMMENT '审核状态（1-待审核，2-审核通过，3-审核拒绝）',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `audit_by` BIGINT(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
  `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  `handle_by` BIGINT(20) DEFAULT NULL COMMENT '处理人ID',
  `handle_result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
  `feedback_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '反馈时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_feedback_no` (`feedback_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_provider_id` (`provider_id`, `rating`),
  KEY `idx_service_id` (`service_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_feedback_type` (`feedback_type`),
  KEY `idx_status` (`status`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_rating` (`rating` DESC),
  KEY `idx_feedback_time` (`feedback_time` DESC),
  KEY `idx_is_top` (`is_top`, `like_count` DESC),
  CONSTRAINT `fk_feedback_order` FOREIGN KEY (`order_id`) REFERENCES `service_order` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_feedback_provider` FOREIGN KEY (`provider_id`) REFERENCES `service_provider` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_feedback_service` FOREIGN KEY (`service_id`) REFERENCES `provider_service` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_feedback_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='服务反馈表';

-- 为反馈内容创建全文索引（支持中文检索）
ALTER TABLE `service_feedback` 
  ADD FULLTEXT INDEX `ft_feedback_content` (`content`, `reply_content`) WITH PARSER ngram;

-- ============================================
-- 创建视图：服务商统计视图
-- ============================================
CREATE OR REPLACE VIEW `v_provider_statistics` AS
SELECT 
    sp.id AS provider_id,
    sp.provider_no,
    sp.provider_name,
    sp.rating,
    sp.service_count,
    sp.complaint_count,
    COUNT(DISTINCT ps.id) AS service_item_count,
    COALESCE(SUM(ps.sales_count), 0) AS total_sales,
    COUNT(DISTINCT sf.id) AS feedback_count,
    COALESCE(AVG(sf.rating), 0) AS avg_feedback_rating,
    COUNT(DISTINCT CASE WHEN sf.feedback_type = 2 THEN sf.id END) AS complaint_feedback_count
FROM service_provider sp
LEFT JOIN provider_service ps ON sp.id = ps.provider_id AND ps.deleted = 0
LEFT JOIN service_feedback sf ON sp.id = sf.provider_id AND sf.deleted = 0 AND sf.audit_status = 2
WHERE sp.deleted = 0
GROUP BY sp.id, sp.provider_no, sp.provider_name, sp.rating, sp.service_count, sp.complaint_count;

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入测试服务商（假设已有user_id=1001的服务商用户）
INSERT INTO `service_provider` 
(`provider_no`, `user_id`, `provider_name`, `provider_type`, `contact_person`, `contact_phone`, `contact_email`, 
 `address`, `province`, `city`, `district`, `service_scope`, `business_hours`, `description`, 
 `rating`, `status`, `audit_status`, `join_date`, `is_recommended`, `sort_order`)
VALUES
('SP202511260001', 1001, '福缘祭扫服务中心', 2, '张经理', '13800138001', 'fuyuan@example.com',
 '北京市朝阳区陵园路88号', '北京市', '北京市', '朝阳区', '祭扫服务,鲜花供品,墓碑维护', '周一至周日 8:00-18:00',
 '专业提供墓园祭扫、鲜花配送、墓碑维护等一站式服务，服务团队经验丰富，态度诚恳。', 
 4.85, 1, 2, '2023-01-01', 1, 1),
('SP202511260002', 1002, '心诚鲜花配送', 1, '李女士', '13900139002', 'xincheng@example.com',
 '北京市海淀区花卉市场东街56号', '北京市', '北京市', '海淀区', '鲜花供品', '周一至周日 7:00-20:00',
 '专业鲜花配送服务，提供各类祭祀用花，品质保证，准时送达。',
 4.92, 1, 2, '2023-03-15', 1, 2),
('SP202511260003', 1003, '慎终追远法事服务', 2, '王师傅', '13700137003', 'shenzong@example.com',
 '北京市丰台区寺庙街12号', '北京市', '北京市', '丰台区', '法事服务', '周一至周日 8:00-17:00',
 '提供专业佛事、道教法事服务，经验丰富的法师团队，仪式庄重。',
 4.78, 1, 2, '2023-06-01', 0, 3);

-- 插入测试服务项目
INSERT INTO `provider_service`
(`provider_id`, `service_no`, `service_name`, `service_category`, `service_type`, `price`, `original_price`, `price_unit`,
 `description`, `service_content`, `duration`, `stock`, `status`, `is_hot`, `is_recommended`, `publish_time`)
VALUES
(10000, 'SVC202511260001', '代客祭扫基础套餐', 1, 1, 200.00, 280.00, '次',
 '包含墓碑清洁、敬献鲜花、上香祭拜等基础服务', 
 '1. 墓碑清洁擦拭\n2. 敬献一束鲜花（菊花或百合）\n3. 上香祭拜\n4. 拍照录像记录\n5. 微信实时反馈',
 60, -1, 1, 1, 1, '2024-01-01 00:00:00'),
(10000, 'SVC202511260002', '代客祭扫豪华套餐', 1, 1, 500.00, 680.00, '次',
 '包含墓碑清洁、豪华鲜花、供品、法事诵经等全套服务',
 '1. 墓碑深度清洁维护\n2. 敬献豪华花篮\n3. 供奉水果供品\n4. 上香祭拜\n5. 法事诵经（10分钟）\n6. 高清视频录制\n7. 微信实时反馈',
 120, -1, 1, 1, 1, '2024-01-01 00:00:00'),
(10001, 'SVC202511260003', '白菊花束', 2, 2, 80.00, 100.00, '束',
 '新鲜白菊花束，适合祭祀使用',
 '精选优质白菊花，数量20-30朵，配精美包装',
 NULL, 100, 1, 0, 0, '2024-02-01 00:00:00'),
(10001, 'SVC202511260004', '百合花篮', 2, 2, 180.00, 220.00, '个',
 '高档百合花篮，表达哀思',
 '进口百合配时令鲜花，精美花篮装饰',
 NULL, 50, 1, 1, 1, '2024-02-01 00:00:00'),
(10002, 'SVC202511260005', '佛教超度法事', 4, 1, 800.00, 1000.00, '场',
 '专业佛教超度仪式，祈福消灾',
 '由资深法师主持，完整佛教超度仪轨，时长约90分钟',
 90, -1, 1, 0, 1, '2024-03-01 00:00:00');

-- 插入测试反馈（假设已有订单ID=100001）
INSERT INTO `service_feedback`
(`feedback_no`, `order_id`, `provider_id`, `service_id`, `user_id`, `feedback_type`, 
 `rating`, `service_rating`, `quality_rating`, `speed_rating`, `content`, 
 `status`, `audit_status`, `feedback_time`)
VALUES
('FB202511260001', 100001, 10000, 10000, 1000, 1, 
 5.00, 5.00, 5.00, 5.00, '服务非常专业，工作人员态度很好，墓碑清洁得很干净，鲜花也很新鲜，非常满意！',
 2, 2, '2024-11-01 10:30:00'),
('FB202511260002', 100002, 10001, 10003, 1001, 1,
 4.50, 4.50, 5.00, 4.00, '花很新鲜，包装精美，就是送达时间比预约晚了一点，整体还是很满意的。',
 2, 2, '2024-11-05 15:20:00');

-- ============================================
-- 创建索引优化建议
-- ============================================
-- 以下索引已在表创建时添加，此处仅作说明：
-- 1. 服务商表：按状态、评分、推荐标记查询的组合索引
-- 2. 服务项目表：按服务商ID和状态的组合索引，按销量和评分的降序索引
-- 3. 服务反馈表：按服务商ID和评分的组合索引，按反馈时间降序索引
-- 4. 全文索引：用于服务项目和反馈内容的中文全文检索
