-- ============================================
-- 陵园管家智慧墓园管理系统 - 数据库初始化脚本
-- 版本: 1.0.0
-- 创建日期: 2025-01-16
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS cemetery_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE cemetery_db;

-- ============================================
-- 1. 用户表（sys_user）
-- 功能：管理系统用户信息，包括管理员、家属、服务商
-- ============================================
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名（登录账号）',
  `password` VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `gender` TINYINT(1) DEFAULT 0 COMMENT '性别（0-女，1-男）',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像地址',
  `user_type` TINYINT(1) DEFAULT 3 COMMENT '用户类型（1-管理员，2-服务商，3-家属）',
  `status` TINYINT(1) DEFAULT 1 COMMENT '账号状态（0-禁用，1-正常）',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 墓位表（tomb_location）
-- 功能：管理墓园所有墓位信息
-- ============================================
CREATE TABLE IF NOT EXISTS `tomb_location` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '墓位ID',
  `tomb_no` VARCHAR(50) NOT NULL COMMENT '墓位编号（唯一标识）',
  `tomb_name` VARCHAR(100) DEFAULT NULL COMMENT '墓位名称',
  `tomb_type` TINYINT(1) DEFAULT 1 COMMENT '墓位类型（1-单穴，2-双穴，3-家族墓，4-壁葬，5-树葬）',
  `area_code` VARCHAR(50) NOT NULL COMMENT '区域编码（如：A区、B区）',
  `area_name` VARCHAR(100) NOT NULL COMMENT '区域名称',
  `row_num` INT(11) NOT NULL COMMENT '排号',
  `col_num` INT(11) NOT NULL COMMENT '位号',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '墓位价格（元）',
  `management_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '年度管理费（元）',
  `area_size` DECIMAL(10,2) DEFAULT NULL COMMENT '占地面积（平方米）',
  `orientation` VARCHAR(20) DEFAULT NULL COMMENT '朝向（如：坐北朝南）',
  `status` TINYINT(1) DEFAULT 1 COMMENT '墓位状态（1-空闲，2-已售，3-预定，4-维护中，5-已过期）',
  `purchase_date` DATE DEFAULT NULL COMMENT '购买日期',
  `expiry_date` DATE DEFAULT NULL COMMENT '到期日期',
  `owner_id` BIGINT(20) DEFAULT NULL COMMENT '所属家属ID（关联sys_user）',
  `longitude` DECIMAL(10,6) DEFAULT NULL COMMENT '经度（用于地图定位）',
  `latitude` DECIMAL(10,6) DEFAULT NULL COMMENT '纬度（用于地图定位）',
  `images` TEXT COMMENT '墓位照片（多个用逗号分隔）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tomb_no` (`tomb_no`),
  KEY `idx_area_code` (`area_code`),
  KEY `idx_status` (`status`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_row_col` (`row_num`, `col_num`),
  KEY `idx_price` (`price`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='墓位表';

-- ============================================
-- 3. 逝者表（deceased_info）
-- 功能：记录安葬在墓位中的逝者信息
-- ============================================
CREATE TABLE IF NOT EXISTS `deceased_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '逝者ID',
  `deceased_name` VARCHAR(50) NOT NULL COMMENT '逝者姓名',
  `gender` TINYINT(1) NOT NULL COMMENT '性别（0-女，1-男）',
  `birth_date` DATE DEFAULT NULL COMMENT '出生日期',
  `death_date` DATE DEFAULT NULL COMMENT '去世日期',
  `age` INT(11) DEFAULT NULL COMMENT '享年',
  `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
  `nationality` VARCHAR(50) DEFAULT NULL COMMENT '国籍',
  `native_place` VARCHAR(100) DEFAULT NULL COMMENT '籍贯',
  `occupation` VARCHAR(100) DEFAULT NULL COMMENT '生前职业',
  `tomb_id` BIGINT(20) NOT NULL COMMENT '墓位ID（关联tomb_location）',
  `burial_date` DATE DEFAULT NULL COMMENT '安葬日期',
  `epitaph` TEXT COMMENT '墓志铭',
  `photo` VARCHAR(500) DEFAULT NULL COMMENT '遗像地址',
  `life_story` TEXT COMMENT '生平简介',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_tomb_id` (`tomb_id`),
  KEY `idx_deceased_name` (`deceased_name`),
  KEY `idx_death_date` (`death_date`),
  CONSTRAINT `fk_deceased_tomb` FOREIGN KEY (`tomb_id`) REFERENCES `tomb_location` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='逝者信息表';

-- ============================================
-- 4. 家属表（family_member）
-- 功能：管理逝者的家属关系和联系方式
-- ============================================
CREATE TABLE IF NOT EXISTS `family_member` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '家属ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID（关联sys_user）',
  `deceased_id` BIGINT(20) NOT NULL COMMENT '逝者ID（关联deceased_info）',
  `member_name` VARCHAR(50) NOT NULL COMMENT '家属姓名',
  `relationship` VARCHAR(50) NOT NULL COMMENT '与逝者关系（如：子女、配偶、父母、兄弟姐妹）',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '联系地址',
  `is_primary` TINYINT(1) DEFAULT 0 COMMENT '是否主要联系人（0-否，1-是）',
  `emergency_contact` TINYINT(1) DEFAULT 0 COMMENT '是否紧急联系人（0-否，1-是）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_deceased_id` (`deceased_id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_is_primary` (`is_primary`),
  CONSTRAINT `fk_family_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_family_deceased` FOREIGN KEY (`deceased_id`) REFERENCES `deceased_info` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='家属关系表';

-- ============================================
-- 5. 订单表（service_order）
-- 功能：管理墓园各类服务订单
-- ============================================
CREATE TABLE IF NOT EXISTS `service_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单编号（唯一）',
  `service_type` TINYINT(1) NOT NULL COMMENT '服务类型（1-墓位购买，2-祭扫预约，3-代客祭扫，4-鲜花购买，5-管理费续费，6-墓碑维护，7-其他服务）',
  `tomb_id` BIGINT(20) DEFAULT NULL COMMENT '墓位ID（关联tomb_location）',
  `user_id` BIGINT(20) NOT NULL COMMENT '下单用户ID（关联sys_user）',
  `family_id` BIGINT(20) DEFAULT NULL COMMENT '家属ID（关联family_member）',
  `service_name` VARCHAR(100) NOT NULL COMMENT '服务名称',
  `service_desc` TEXT COMMENT '服务描述',
  `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额（元）',
  `discount_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额（元）',
  `actual_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实付金额（元）',
  `status` TINYINT(1) DEFAULT 1 COMMENT '订单状态（1-待支付，2-已支付，3-服务中，4-已完成，5-已取消，6-已退款）',
  `appointment_date` DATE DEFAULT NULL COMMENT '预约服务日期',
  `appointment_time` VARCHAR(20) DEFAULT NULL COMMENT '预约时段',
  `contact_name` VARCHAR(50) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系人电话',
  `service_staff_id` BIGINT(20) DEFAULT NULL COMMENT '服务人员ID（关联sys_user）',
  `service_start_time` DATETIME DEFAULT NULL COMMENT '服务开始时间',
  `service_end_time` DATETIME DEFAULT NULL COMMENT '服务结束时间',
  `cancel_reason` VARCHAR(500) DEFAULT NULL COMMENT '取消原因',
  `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_service_type` (`service_type`),
  KEY `idx_tomb_id` (`tomb_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_appointment_date` (`appointment_date`),
  CONSTRAINT `fk_order_tomb` FOREIGN KEY (`tomb_id`) REFERENCES `tomb_location` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COMMENT='服务订单表';

-- ============================================
-- 6. 支付记录表（payment_record）
-- 功能：记录所有支付交易信息
-- ============================================
CREATE TABLE IF NOT EXISTS `payment_record` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `payment_no` VARCHAR(50) NOT NULL COMMENT '支付流水号（唯一）',
  `order_id` BIGINT(20) NOT NULL COMMENT '订单ID（关联service_order）',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单编号',
  `user_id` BIGINT(20) NOT NULL COMMENT '支付用户ID（关联sys_user）',
  `payment_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '支付金额（元）',
  `payment_method` TINYINT(1) DEFAULT 1 COMMENT '支付方式（1-微信支付，2-支付宝，3-现金，4-刷卡，5-转账）',
  `payment_channel` VARCHAR(50) DEFAULT NULL COMMENT '支付渠道（如：WX_JSAPI、ALIPAY_APP）',
  `transaction_id` VARCHAR(100) DEFAULT NULL COMMENT '第三方支付交易号',
  `payment_status` TINYINT(1) DEFAULT 1 COMMENT '支付状态（1-待支付，2-支付中，3-支付成功，4-支付失败，5-已退款）',
  `payment_time` DATETIME DEFAULT NULL COMMENT '支付完成时间',
  `refund_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '退款金额（元）',
  `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
  `refund_reason` VARCHAR(500) DEFAULT NULL COMMENT '退款原因',
  `payer_account` VARCHAR(100) DEFAULT NULL COMMENT '付款账号',
  `payee_account` VARCHAR(100) DEFAULT NULL COMMENT '收款账号',
  `notify_time` DATETIME DEFAULT NULL COMMENT '支付通知时间',
  `notify_data` TEXT COMMENT '支付回调数据',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_payment_time` (`payment_time`),
  KEY `idx_transaction_id` (`transaction_id`),
  CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `service_order` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_payment_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- ============================================
-- 7. 祭扫记录表（memorial_record）
-- 功能：记录家属的祭扫活动
-- ============================================
CREATE TABLE IF NOT EXISTS `memorial_record` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID（关联sys_user）',
  `deceased_id` BIGINT(20) NOT NULL COMMENT '逝者ID（关联deceased_info）',
  `tomb_id` BIGINT(20) NOT NULL COMMENT '墓位ID（关联tomb_location）',
  `memorial_type` TINYINT(1) DEFAULT 1 COMMENT '祭扫类型（1-现场祭扫，2-在线祭扫，3-代客祭扫）',
  `memorial_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '祭扫时间',
  `content` TEXT COMMENT '祭扫留言内容',
  `images` TEXT COMMENT '祭扫照片（多个用逗号分隔）',
  `offerings` VARCHAR(500) DEFAULT NULL COMMENT '供品清单',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_deceased_id` (`deceased_id`),
  KEY `idx_tomb_id` (`tomb_id`),
  KEY `idx_memorial_date` (`memorial_date`),
  CONSTRAINT `fk_memorial_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_memorial_deceased` FOREIGN KEY (`deceased_id`) REFERENCES `deceased_info` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_memorial_tomb` FOREIGN KEY (`tomb_id`) REFERENCES `tomb_location` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='祭扫记录表';

-- ============================================
-- 8. 公告表（cemetery_notice）
-- 功能：发布系统公告和通知
-- ============================================
CREATE TABLE IF NOT EXISTS `cemetery_notice` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
  `content` TEXT COMMENT '公告内容',
  `notice_type` TINYINT(1) DEFAULT 1 COMMENT '公告类型（1-系统公告，2-活动通知，3-政策公告，4-温馨提示）',
  `priority` TINYINT(1) DEFAULT 0 COMMENT '优先级（0-普通，1-重要，2-紧急）',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态（0-草稿，1-已发布，2-已下线）',
  `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
  `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
  `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片',
  `view_count` INT(11) DEFAULT 0 COMMENT '阅读次数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_notice_type` (`notice_type`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_priority` (`priority`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='公告通知表';

-- ============================================
-- 初始化基础数据
-- ============================================

-- 插入管理员账号（密码：123456，使用BCrypt加密）
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `real_name`, `phone`, `email`, `user_type`, `status`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU.qHQa8R1i', '系统管理员', '张三', '13800138000', 'admin@cemetery.com', 1, 1),
(2, 'service01', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU.qHQa8R1i', '服务员001', '李四', '13800138001', 'service01@cemetery.com', 2, 1),
(3, 'family01', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU.qHQa8R1i', '王先生', '王五', '13800138002', 'wang@example.com', 3, 1);

-- 插入墓位示例数据
INSERT INTO `tomb_location` (`tomb_no`, `tomb_name`, `tomb_type`, `area_code`, `area_name`, `row_num`, `col_num`, `price`, `management_fee`, `area_size`, `orientation`, `status`) VALUES
('A-01-001', '福寿园A区1排1号', 1, 'A', '福寿园A区', 1, 1, 50000.00, 500.00, 2.5, '坐北朝南', 1),
('A-01-002', '福寿园A区1排2号', 2, 'A', '福寿园A区', 1, 2, 80000.00, 800.00, 4.0, '坐北朝南', 1),
('A-01-003', '福寿园A区1排3号', 1, 'A', '福寿园A区', 1, 3, 50000.00, 500.00, 2.5, '坐北朝南', 2),
('B-01-001', '安康园B区1排1号', 3, 'B', '安康园B区', 1, 1, 150000.00, 1500.00, 8.0, '坐西朝东', 1),
('B-01-002', '安康园B区1排2号', 1, 'B', '安康园B区', 1, 2, 45000.00, 450.00, 2.0, '坐西朝东', 1);

-- 插入逝者示例数据
INSERT INTO `deceased_info` (`deceased_name`, `gender`, `birth_date`, `death_date`, `age`, `tomb_id`, `burial_date`) VALUES
('李明', 1, '1945-03-15', '2023-06-20', 78, 10003, '2023-06-25');

-- 插入家属关系数据
INSERT INTO `family_member` (`user_id`, `deceased_id`, `member_name`, `relationship`, `phone`, `is_primary`) VALUES
(3, 10000, '李强', '儿子', '13800138002', 1);

-- 插入订单示例数据
INSERT INTO `service_order` (`order_no`, `service_type`, `tomb_id`, `user_id`, `service_name`, `service_desc`, `total_amount`, `actual_amount`, `status`, `appointment_date`, `contact_name`, `contact_phone`) VALUES
('SO202501160001', 1, 10003, 3, '墓位购买', '购买福寿园A区1排3号墓位', 50000.00, 50000.00, 2, '2023-06-01', '李强', '13800138002'),
('SO202501160002', 2, 10003, 3, '清明节祭扫预约', '预约清明节祭扫服务', 0.00, 0.00, 4, '2024-04-04', '李强', '13800138002');

-- 插入支付记录数据
INSERT INTO `payment_record` (`payment_no`, `order_id`, `order_no`, `user_id`, `payment_amount`, `payment_method`, `payment_status`, `payment_time`) VALUES
('PAY202501160001', 100000, 'SO202501160001', 3, 50000.00, 1, 3, '2023-06-01 14:30:00');

-- 插入祭扫记录数据
INSERT INTO `memorial_record` (`user_id`, `deceased_id`, `tomb_id`, `memorial_type`, `memorial_date`, `content`) VALUES
(3, 10000, 10003, 1, '2024-04-04 10:00:00', '清明节祭扫，愿父亲安息');

-- 插入公告数据
INSERT INTO `cemetery_notice` (`title`, `content`, `notice_type`, `priority`, `status`, `publish_time`) VALUES
('2025年清明节祭扫预约通知', '尊敬的各位家属：2025年清明节即将来临，为保障祭扫秩序，请提前预约祭扫时间。预约时间：3月20日至4月3日。', 2, 1, 1, '2025-03-15 09:00:00'),
('墓园环境提升公告', '为提升墓园环境质量，我园将于近期对园区绿化进行升级改造，施工期间部分区域可能会有临时管制，敬请谅解。', 1, 0, 1, '2025-03-10 10:00:00'),
('春节开放时间调整', '春节期间（2月1日-2月7日）墓园开放时间调整为：8:00-16:00，请合理安排祭扫时间。', 4, 1, 1, '2025-01-20 09:00:00');

-- ============================================
-- 数据库初始化完成
-- ============================================
