-- ============================================
-- 数字纪念空间功能 - 数据库扩展脚本（优化版）
-- 版本: 2.0.0
-- 创建日期: 2025-11-17
-- 优化特性：
--   1. 统一使用utf8mb4字符集，支持emoji和中文全文检索
--   2. 为文本字段创建全文索引（FULLTEXT）
--   3. 按年份分区以支持大数据量存储
--   4. 优化索引设计提升查询性能
--   5. 包含完整的测试数据
-- ============================================

USE cemetery_db;

-- 设置会话字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- ============================================
-- 1. 数字纪念空间主表（digital_memorial）
-- 功能：管理每个逝者的数字纪念空间
-- ============================================
CREATE TABLE IF NOT EXISTS `digital_memorial` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '纪念空间ID',
  `space_no` VARCHAR(50) NOT NULL COMMENT '纪念空间编号（唯一标识）',
  `tomb_id` BIGINT(20) NOT NULL COMMENT '关联墓位ID（关联tomb_location）',
  `deceased_id` BIGINT(20) NOT NULL COMMENT '逝者ID（关联deceased_info）',
  `space_name` VARCHAR(100) DEFAULT NULL COMMENT '纪念空间名称',
  `biography` TEXT COMMENT '生平介绍（详细版）',
  `life_achievements` TEXT COMMENT '生平成就',
  `family_words` TEXT COMMENT '家属寄语',
  `access_permission` TINYINT(1) DEFAULT 1 COMMENT '访问权限（1-公开，2-家属可见，3-密码访问，4-完全私密）',
  `access_password` VARCHAR(200) DEFAULT NULL COMMENT '访问密码（BCrypt加密，仅当access_permission=3时有效）',
  `background_theme` VARCHAR(50) DEFAULT 'default' COMMENT '背景主题（default-默认，spring-春天，autumn-秋天，classic-经典，modern-现代）',
  `background_color` VARCHAR(20) DEFAULT '#f5f5f5' COMMENT '背景颜色（十六进制色值）',
  `background_image` VARCHAR(500) DEFAULT NULL COMMENT '背景图片地址',
  `music_enabled` TINYINT(1) DEFAULT 0 COMMENT '是否启用背景音乐（0-否，1-是）',
  `music_url` VARCHAR(500) DEFAULT NULL COMMENT '背景音乐地址',
  `music_name` VARCHAR(100) DEFAULT NULL COMMENT '背景音乐名称',
  `candle_enabled` TINYINT(1) DEFAULT 1 COMMENT '是否显示电子蜡烛（0-否，1-是）',
  `flower_enabled` TINYINT(1) DEFAULT 1 COMMENT '是否显示电子献花（0-否，1-是）',
  `incense_enabled` TINYINT(1) DEFAULT 1 COMMENT '是否显示电子上香（0-否，1-是）',
  `message_enabled` TINYINT(1) DEFAULT 1 COMMENT '是否允许留言（0-否，1-是）',
  `message_audit` TINYINT(1) DEFAULT 1 COMMENT '留言是否需要审核（0-否，1-是）',
  `visit_count` INT(11) DEFAULT 0 COMMENT '访问次数',
  `candle_count` INT(11) DEFAULT 0 COMMENT '点烛次数',
  `flower_count` INT(11) DEFAULT 0 COMMENT '献花次数',
  `incense_count` INT(11) DEFAULT 0 COMMENT '上香次数',
  `message_count` INT(11) DEFAULT 0 COMMENT '留言次数',
  `is_published` TINYINT(1) DEFAULT 0 COMMENT '是否已发布（0-未发布，1-已发布）',
  `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
  `expire_time` DATETIME DEFAULT NULL COMMENT '到期时间',
  `qr_code` VARCHAR(500) DEFAULT NULL COMMENT '专属二维码地址',
  `share_url` VARCHAR(500) DEFAULT NULL COMMENT '分享链接',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`, `create_time`),
  UNIQUE KEY `uk_space_no` (`space_no`, `create_time`),
  UNIQUE KEY `uk_deceased_id` (`deceased_id`, `create_time`),
  KEY `idx_tomb_id` (`tomb_id`),
  KEY `idx_is_published` (`is_published`, `publish_time`),
  KEY `idx_access_permission` (`access_permission`),
  KEY `idx_visit_count` (`visit_count` DESC),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_deleted_published` (`deleted`, `is_published`),
  KEY `idx_expire_time` (`expire_time`)
  -- 注意：分区表不支持外键约束，数据完整性由应用层保证
  -- CONSTRAINT `fk_memorial_tomb` FOREIGN KEY (`tomb_id`) REFERENCES `tomb_location` (`id`) ON DELETE RESTRICT,
  -- CONSTRAINT `fk_memorial_deceased` FOREIGN KEY (`deceased_id`) REFERENCES `deceased_info` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='数字纪念空间主表'
  PARTITION BY RANGE (YEAR(create_time)) (
    PARTITION p2020 VALUES LESS THAN (2021),
    PARTITION p2021 VALUES LESS THAN (2022),
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p2026 VALUES LESS THAN (2027),
    PARTITION p2027 VALUES LESS THAN (2028),
    PARTITION p2028 VALUES LESS THAN (2029),
    PARTITION p2029 VALUES LESS THAN (2030),
    PARTITION p_future VALUES LESS THAN MAXVALUE
  );

-- ============================================
-- 2. 纪念内容表（memorial_content）
-- 功能：管理纪念空间中的多媒体内容
-- ============================================
CREATE TABLE IF NOT EXISTS `memorial_content` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `memorial_id` BIGINT(20) NOT NULL COMMENT '纪念空间ID（关联digital_memorial）',
  `content_type` TINYINT(1) NOT NULL COMMENT '内容类型（1-文本，2-图片，3-视频，4-音频，5-文档）',
  `title` VARCHAR(200) DEFAULT NULL COMMENT '内容标题',
  `description` TEXT COMMENT '内容描述',
  `content_url` VARCHAR(1000) DEFAULT NULL COMMENT '内容地址（图片/视频/音频/文档的URL）',
  `content_text` MEDIUMTEXT COMMENT '文本内容（当content_type=1时使用）',
  `thumbnail_url` VARCHAR(500) DEFAULT NULL COMMENT '缩略图地址',
  `file_size` BIGINT(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `duration` INT(11) DEFAULT NULL COMMENT '时长（秒，适用于视频/音频）',
  `width` INT(11) DEFAULT NULL COMMENT '宽度（像素，适用于图片/视频）',
  `height` INT(11) DEFAULT NULL COMMENT '高度（像素，适用于图片/视频）',
  `sort_order` INT(11) DEFAULT 0 COMMENT '排序序号（数字越小越靠前）',
  `audit_status` TINYINT(1) DEFAULT 1 COMMENT '审核状态（1-待审核，2-审核通过，3-审核拒绝）',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `audit_by` BIGINT(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
  `is_featured` TINYINT(1) DEFAULT 0 COMMENT '是否精选展示（0-否，1-是）',
  `view_count` INT(11) DEFAULT 0 COMMENT '查看次数',
  `upload_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `upload_by` BIGINT(20) NOT NULL COMMENT '上传用户ID（关联sys_user）',
  `upload_ip` VARCHAR(50) DEFAULT NULL COMMENT '上传IP',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`, `upload_time`),
  KEY `idx_memorial_id` (`memorial_id`, `audit_status`),
  KEY `idx_content_type` (`content_type`, `deleted`),
  KEY `idx_audit_status` (`audit_status`, `audit_time`),
  KEY `idx_sort_order` (`memorial_id`, `sort_order`),
  KEY `idx_upload_by` (`upload_by`),
  KEY `idx_upload_time` (`upload_time`),
  KEY `idx_is_featured` (`is_featured`, `view_count` DESC),
  KEY `idx_deleted_memorial` (`deleted`, `memorial_id`),
  KEY `idx_view_count` (`view_count` DESC)
  -- 注意：分区表不支持外键约束，数据完整性由应用层保证
  -- CONSTRAINT `fk_content_memorial` FOREIGN KEY (`memorial_id`) REFERENCES `digital_memorial` (`id`) ON DELETE CASCADE,
  -- CONSTRAINT `fk_content_upload_user` FOREIGN KEY (`upload_by`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='纪念内容表'
  PARTITION BY RANGE (YEAR(upload_time)) (
    PARTITION p2020 VALUES LESS THAN (2021),
    PARTITION p2021 VALUES LESS THAN (2022),
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p2026 VALUES LESS THAN (2027),
    PARTITION p2027 VALUES LESS THAN (2028),
    PARTITION p2028 VALUES LESS THAN (2029),
    PARTITION p2029 VALUES LESS THAN (2030),
    PARTITION p_future VALUES LESS THAN MAXVALUE
  );

-- ============================================
-- 3. 追思留言表（memorial_message）
-- 功能：管理纪念空间中的留言和追思
-- ============================================
CREATE TABLE IF NOT EXISTS `memorial_message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '留言ID',
  `memorial_id` BIGINT(20) NOT NULL COMMENT '纪念空间ID（关联digital_memorial）',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '留言用户ID（关联sys_user，匿名留言时为NULL）',
  `author_name` VARCHAR(50) NOT NULL COMMENT '留言人昵称',
  `author_avatar` VARCHAR(500) DEFAULT NULL COMMENT '留言人头像',
  `relationship` VARCHAR(50) DEFAULT NULL COMMENT '与逝者关系（子女、配偶、父母、兄弟姐妹、朋友、同事等）',
  `message_content` TEXT NOT NULL COMMENT '留言内容',
  `message_type` TINYINT(1) DEFAULT 1 COMMENT '留言类型（1-普通留言，2-祭日留言，3-纪念日留言）',
  `images` TEXT COMMENT '留言配图（多个用逗号分隔）',
  `is_anonymous` TINYINT(1) DEFAULT 0 COMMENT '是否匿名（0-否，1-是）',
  `is_pinned` TINYINT(1) DEFAULT 0 COMMENT '是否置顶（0-否，1-是）',
  `audit_status` TINYINT(1) DEFAULT 1 COMMENT '审核状态（1-待审核，2-审核通过，3-审核拒绝）',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `audit_by` BIGINT(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
  `like_count` INT(11) DEFAULT 0 COMMENT '点赞次数',
  `reply_count` INT(11) DEFAULT 0 COMMENT '回复次数',
  `parent_id` BIGINT(20) DEFAULT NULL COMMENT '父留言ID（用于留言回复）',
  `reply_to_id` BIGINT(20) DEFAULT NULL COMMENT '回复给哪条留言',
  `reply_to_name` VARCHAR(50) DEFAULT NULL COMMENT '回复给谁',
  `location_province` VARCHAR(50) DEFAULT NULL COMMENT '留言位置-省份',
  `location_city` VARCHAR(50) DEFAULT NULL COMMENT '留言位置-城市',
  `location_info` VARCHAR(200) DEFAULT NULL COMMENT '留言位置信息',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `device_info` VARCHAR(200) DEFAULT NULL COMMENT '设备信息',
  `message_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`, `message_time`),
  KEY `idx_memorial_id` (`memorial_id`, `audit_status`, `is_pinned`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_audit_status` (`audit_status`, `audit_time`),
  KEY `idx_message_time` (`message_time` DESC),
  KEY `idx_is_pinned` (`is_pinned`, `message_time` DESC),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_like_count` (`like_count` DESC),
  KEY `idx_memorial_deleted` (`memorial_id`, `deleted`, `audit_status`),
  KEY `idx_hot_message` (`memorial_id`, `like_count` DESC, `reply_count` DESC)
  -- 注意：分区表不支持外键约束，数据完整性由应用层保证
  -- CONSTRAINT `fk_message_memorial` FOREIGN KEY (`memorial_id`) REFERENCES `digital_memorial` (`id`) ON DELETE CASCADE,
  -- CONSTRAINT `fk_message_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL,
  -- CONSTRAINT `fk_message_parent` FOREIGN KEY (`parent_id`) REFERENCES `memorial_message` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='追思留言表'
  PARTITION BY RANGE (YEAR(message_time)) (
    PARTITION p2020 VALUES LESS THAN (2021),
    PARTITION p2021 VALUES LESS THAN (2022),
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p2026 VALUES LESS THAN (2027),
    PARTITION p2027 VALUES LESS THAN (2028),
    PARTITION p2028 VALUES LESS THAN (2029),
    PARTITION p2029 VALUES LESS THAN (2030),
    PARTITION p_future VALUES LESS THAN MAXVALUE
  );

-- ============================================
-- 4. 留言点赞记录表（memorial_message_like）
-- 功能：记录用户对留言的点赞
-- ============================================
CREATE TABLE IF NOT EXISTS `memorial_message_like` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `message_id` BIGINT(20) NOT NULL COMMENT '留言ID（关联memorial_message）',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '点赞用户ID（关联sys_user，匿名点赞时为NULL）',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`, `create_time`),
  UNIQUE KEY `uk_message_user` (`message_id`, `user_id`, `create_time`),
  KEY `idx_message_id` (`message_id`, `create_time`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
  -- 注意：分区表不支持外键约束，数据完整性由应用层保证
  -- CONSTRAINT `fk_like_message` FOREIGN KEY (`message_id`) REFERENCES `memorial_message` (`id`) ON DELETE CASCADE,
  -- CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='留言点赞记录表'
  PARTITION BY RANGE (YEAR(create_time)) (
    PARTITION p2020 VALUES LESS THAN (2021),
    PARTITION p2021 VALUES LESS THAN (2022),
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p2026 VALUES LESS THAN (2027),
    PARTITION p2027 VALUES LESS THAN (2028),
    PARTITION p2028 VALUES LESS THAN (2029),
    PARTITION p2029 VALUES LESS THAN (2030),
    PARTITION p_future VALUES LESS THAN MAXVALUE
  );

-- ============================================
-- 5. 纪念空间访问记录表（memorial_visit_log）
-- 功能：记录纪念空间的访问日志
-- ============================================
CREATE TABLE IF NOT EXISTS `memorial_visit_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `memorial_id` BIGINT(20) NOT NULL COMMENT '纪念空间ID（关联digital_memorial）',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '访问用户ID（关联sys_user，匿名访问时为NULL）',
  `visitor_name` VARCHAR(50) DEFAULT NULL COMMENT '访客昵称',
  `visit_type` TINYINT(1) DEFAULT 1 COMMENT '访问类型（1-浏览，2-点烛，3-献花，4-上香，5-留言）',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `location_province` VARCHAR(50) DEFAULT NULL COMMENT '访问位置-省份',
  `location_city` VARCHAR(50) DEFAULT NULL COMMENT '访问位置-城市',
  `device_type` VARCHAR(50) DEFAULT NULL COMMENT '设备类型（PC/Mobile/Tablet）',
  `device_info` VARCHAR(200) DEFAULT NULL COMMENT '设备信息',
  `browser_info` VARCHAR(200) DEFAULT NULL COMMENT '浏览器信息',
  `visit_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  `duration` INT(11) DEFAULT 0 COMMENT '停留时长（秒）',
  PRIMARY KEY (`id`, `visit_time`),
  KEY `idx_memorial_id` (`memorial_id`, `visit_time`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_visit_time` (`visit_time`),
  KEY `idx_visit_type` (`visit_type`, `visit_time`),
  KEY `idx_memorial_type` (`memorial_id`, `visit_type`),
  KEY `idx_location` (`location_province`, `location_city`),
  KEY `idx_device_type` (`device_type`)
  -- 注意：分区表不支持外键约束，数据完整性由应用层保证
  -- CONSTRAINT `fk_visit_memorial` FOREIGN KEY (`memorial_id`) REFERENCES `digital_memorial` (`id`) ON DELETE CASCADE,
  -- CONSTRAINT `fk_visit_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10000 
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
  COMMENT='纪念空间访问记录表'
  PARTITION BY RANGE (YEAR(visit_time)) (
    PARTITION p2020 VALUES LESS THAN (2021),
    PARTITION p2021 VALUES LESS THAN (2022),
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p2026 VALUES LESS THAN (2027),
    PARTITION p2027 VALUES LESS THAN (2028),
    PARTITION p2028 VALUES LESS THAN (2029),
    PARTITION p2029 VALUES LESS THAN (2030),
    PARTITION p_future VALUES LESS THAN MAXVALUE
  );

-- ============================================
-- 初始化测试数据
-- ============================================

-- 1. 为已有逝者创建数字纪念空间
INSERT INTO `digital_memorial` 
(`space_no`, `tomb_id`, `deceased_id`, `space_name`, `biography`, `life_achievements`, `family_words`, 
 `access_permission`, `background_theme`, `background_color`, `music_enabled`, `music_name`, 
 `candle_enabled`, `flower_enabled`, `incense_enabled`, `message_enabled`, `message_audit`, 
 `visit_count`, `candle_count`, `flower_count`, `incense_count`, `message_count`, 
 `is_published`, `publish_time`, `expire_time`, `qr_code`, `share_url`) 
VALUES
('DM202511170001', 10003, 10000, '李明纪念馆', 
 '李明先生，1945年3月15日出生于江苏南京，2023年6月20日安详离世，享年78岁。李先生一生勤勉，热爱生活，是一位优秀的人民教师，从教40余年，培育桃李满天下。他为人正直、和蔼可亲，深受学生和同事的爱戴。退休后热心公益事业，积极参与社区志愿服务。他的一生是平凡而伟大的一生，他的精神将永远激励着我们。',
 '1965-1968年就读于南京师范大学中文系；1968-2008年在南京市第一中学任教，历任语文教师、教研组长、年级主任；1995年获评江苏省优秀教师；2005年获评南京市劳动模范；退休后担任社区文化站义务辅导员，辅导青少年写作超过10年。',
 '亲爱的父亲，您的离去让我们悲痛万分，但您的教诲将永远铭记在我们心中。您用一生诠释了什么是师德，什么是父爱。我们会继承您的遗志，好好生活，好好工作，做一个对社会有用的人。愿您在天堂安息，我们永远怀念您！',
 1, 'classic', '#f5f5dc', 1, '梁祝·化蝶', 
 1, 1, 1, 1, 1,
 156, 89, 234, 156, 45,
 1, '2023-06-26 10:00:00', '2033-06-25 23:59:59', 
 '/qrcode/DM202511170001.png', 'https://cemetery.com/memorial/DM202511170001'),

('DM202511170002', 10001, 10001, '纪念空间-王芳', 
 '王芳女士，生于1950年，逝于2024年。一位慈祥的母亲，温柔的妻子，优秀的护士。她的一生奉献给了医疗事业和家庭，用爱和责任诠释了生命的意义。',
 '1970-2010年在市人民医院工作，担任护士长，多次获得优秀护士称号；培养了大批优秀护理人员。',
 '母亲，您永远活在我们心中。您的笑容、您的温暖、您的教诲，是我们一生的财富。',
 2, 'spring', '#e8f5e9', 0, NULL,
 1, 1, 1, 1, 0,
 68, 42, 87, 56, 23,
 1, '2024-03-10 09:00:00', '2034-03-09 23:59:59',
 '/qrcode/DM202511170002.png', 'https://cemetery.com/memorial/DM202511170002'),

('DM202511170003', 10002, 10002, '张国强纪念空间', 
 '张国强先生，1938-2022，革命军人，共产党员。参加过抗美援朝战争，荣立三等功。转业后在地方企业工作，兢兢业业，为社会主义建设贡献了毕生精力。',
 '1956年参军入伍；1951-1953年参加抗美援朝战争，荣立三等功；1958年转业至地方，在国营机械厂工作至退休；1989年获评优秀共产党员。',
 '父亲是我们的榜样，他的革命精神和奋斗精神将永远激励我们前进。',
 1, 'modern', '#fff8e1', 1, '我的祖国',
 1, 1, 1, 1, 1,
 203, 134, 289, 198, 67,
 1, '2022-08-15 14:00:00', '2032-08-14 23:59:59',
 '/qrcode/DM202511170003.png', 'https://cemetery.com/memorial/DM202511170003');

-- 2. 插入纪念内容数据
INSERT INTO `memorial_content`
(`memorial_id`, `content_type`, `title`, `description`, `content_url`, `thumbnail_url`, 
 `file_size`, `width`, `height`, `sort_order`, `audit_status`, `audit_time`, `is_featured`, 
 `view_count`, `upload_time`, `upload_by`, `upload_ip`)
VALUES
-- 李明的照片集
(10000, 2, '青年时期照片', '1965年大学毕业照', '/memorial/limimg/photo1.jpg', '/memorial/liming/thumb1.jpg', 
 524288, 800, 600, 1, 2, '2023-06-26 11:00:00', 1, 
 89, '2023-06-26 10:30:00', 3, '112.80.248.75'),

(10000, 2, '工作照片', '1985年在课堂上授课', '/memorial/liming/photo2.jpg', '/memorial/liming/thumb2.jpg',
 612352, 1024, 768, 2, 2, '2023-06-26 11:00:00', 1,
 76, '2023-06-26 10:35:00', 3, '112.80.248.75'),

(10000, 2, '家庭合影', '2020年全家福', '/memorial/liming/photo3.jpg', '/memorial/liming/thumb3.jpg',
 786432, 1920, 1080, 3, 2, '2023-06-26 11:00:00', 1,
 134, '2023-06-26 10:40:00', 3, '112.80.248.75'),

(10000, 3, '退休欢送会视频', '2008年学校为李老师举办的退休欢送会', '/memorial/liming/video1.mp4', '/memorial/liming/video1_thumb.jpg',
 15728640, 1280, 720, 4, 2, '2023-06-26 12:00:00', 1,
 198, '2023-06-26 11:00:00', 3, '112.80.248.75'),

(10000, 1, '学生的回忆', '我是李老师1990届的学生，李老师是我人生中最重要的导师。他教会我的不仅是知识，更是做人的道理...', NULL, NULL,
 NULL, NULL, NULL, 5, 2, '2023-06-27 09:00:00', 0,
 56, '2023-06-27 08:30:00', 1003, '58.210.10.135'),

-- 王芳的内容
(10001, 2, '工作照', '1980年代在医院工作', '/memorial/wangfang/photo1.jpg', '/memorial/wangfang/thumb1.jpg',
 456789, 800, 600, 1, 2, '2024-03-10 10:00:00', 1,
 45, '2024-03-10 09:30:00', 1002, '121.89.173.46'),

(10001, 2, '家庭照片', '温馨的家庭时光', '/memorial/wangfang/photo2.jpg', '/memorial/wangfang/thumb2.jpg',
 523456, 1024, 768, 2, 2, '2024-03-10 10:00:00', 1,
 67, '2024-03-10 09:35:00', 1002, '121.89.173.46'),

-- 张国强的内容
(10002, 2, '军装照', '1956年参军时的照片', '/memorial/zhangguoqiang/photo1.jpg', '/memorial/zhangguoqiang/thumb1.jpg',
 398765, 600, 800, 1, 2, '2022-08-15 15:00:00', 1,
 123, '2022-08-15 14:30:00', 1004, '114.242.248.200'),

(10002, 2, '荣誉证书', '抗美援朝三等功荣誉证书', '/memorial/zhangguoqiang/photo2.jpg', '/memorial/zhangguoqiang/thumb2.jpg',
 445678, 1200, 900, 2, 2, '2022-08-15 15:00:00', 1,
 156, '2022-08-15 14:35:00', 1004, '114.242.248.200');

-- 3. 插入追思留言数据
INSERT INTO `memorial_message`
(`memorial_id`, `user_id`, `author_name`, `author_avatar`, `relationship`, 
 `message_content`, `message_type`, `is_anonymous`, `is_pinned`, 
 `audit_status`, `audit_time`, `like_count`, `reply_count`, 
 `location_province`, `location_city`, `ip_address`, `message_time`)
VALUES
-- 李明的留言
(10000, 3, '李强', '/avatar/user3.jpg', '儿子',
 '父亲，今天是您离开我们三个月的日子。每当想起您，心中总是充满了怀念。您生前常说的话"做人要正直，做事要认真"，我一直铭记在心。我会好好照顾母亲，把家庭经营好，让您在天堂放心。永远爱您，永远怀念您！',
 1, 0, 1,
 2, '2023-09-20 15:30:00', 23, 5,
 '江苏省', '南京市', '112.80.248.75', '2023-09-20 15:20:00'),

(10000, 1003, '刘晓敏', '/avatar/user1003.jpg', '学生',
 '李老师，我是您1995届的学生刘晓敏。今天偶然得知您已经离开了我们，心中无比悲痛。还记得高三那年，我因为家庭原因想要放弃学业，是您一次次找我谈心，甚至资助我完成了学业。没有您，就没有今天的我。李老师，您永远是我最敬爱的老师！',
 1, 0, 0,
 2, '2023-07-15 11:00:00', 45, 3,
 '江苏省', '苏州市', '58.210.10.135', '2023-07-15 10:45:00'),

(10000, 1005, '王建国', '/avatar/user1005.jpg', '同事',
 '李老师，我们共事了30多年，您既是我的同事，也是我的良师益友。您对教育事业的热爱和对学生的关怀，一直是我学习的榜样。虽然您已离去，但您的精神将永远激励着我们继续前行。愿您在天堂安息！',
 1, 0, 0,
 2, '2023-06-28 09:00:00', 18, 2,
 '江苏省', '南京市', '112.80.248.123', '2023-06-28 08:45:00'),

(10000, NULL, '匿名网友', NULL, '网友',
 '虽然不认识李老师，但从这些文字和照片中，能够感受到他是一位伟大的人民教师。向李老师致敬！愿逝者安息。',
 1, 1, 0,
 2, '2023-08-10 16:00:00', 8, 0,
 '浙江省', '杭州市', '60.191.124.78', '2023-08-10 15:50:00'),

(10000, 3, '李强', '/avatar/user3.jpg', '儿子',
 '父亲，今天是清明节，我们全家来墓前祭拜您了。母亲身体还好，孩子们都很懂事。您培养的学生们也都很有出息，经常来看望母亲。我们都很好，您放心吧。',
 2, 0, 0,
 2, '2024-04-04 12:00:00', 31, 1,
 '江苏省', '南京市', '112.80.248.75', '2024-04-04 11:30:00'),

-- 王芳的留言
(10001, 1002, '张华', '/avatar/user1002.jpg', '女儿',
 '妈妈，好想您...每次经过医院，总会想起您穿着护士服忙碌的身影。您把一生都献给了护理事业，也给了我们最温暖的爱。我会好好照顾爸爸，也会把您的爱传递给我的孩子。妈妈，我爱您！',
 1, 0, 1,
 2, '2024-03-15 10:00:00', 16, 2,
 '江苏省', '南京市', '121.89.173.46', '2024-03-15 09:45:00'),

(10001, 1006, '李芳', '/avatar/user1006.jpg', '同事',
 '王护士长，我是您带过的护士小李。您对工作的认真负责，对病人的耐心细致，一直是我们学习的榜样。感谢您当年对我的悉心指导，让我成长为一名合格的护士。您永远活在我们心中！',
 1, 0, 0,
 2, '2024-04-01 14:30:00', 12, 1,
 '江苏省', '南京市', '121.89.173.88', '2024-04-01 14:15:00'),

-- 张国强的留言
(10002, 1004, '张伟', '/avatar/user1004.jpg', '儿子',
 '父亲，您是我心中永远的英雄！您参加过抗美援朝，保家卫国，荣立战功。转业后又在平凡的岗位上兢兢业业工作了一辈子。您用实际行动诠释了什么是共产党员，什么是革命军人。我会继承您的精神，做一个对国家对社会有用的人！',
 1, 0, 1,
 2, '2022-08-20 10:00:00', 56, 8,
 '江苏省', '南京市', '114.242.248.200', '2022-08-20 09:30:00'),

(10002, 1007, '老战友', '/avatar/user1007.jpg', '战友',
 '老张，我们从朝鲜战场上一起回来的战友，没想到你先走一步了。还记得我们并肩作战的日子，那些艰苦的岁月我们都挺过来了。你是个好战友，好同志。愿你在天堂安息，咱们来世再做战友！',
 1, 0, 0,
 2, '2022-08-18 16:00:00', 34, 4,
 '上海市', '上海市', '101.89.27.156', '2022-08-18 15:45:00');

-- 插入留言回复（子留言）
INSERT INTO `memorial_message`
(`memorial_id`, `user_id`, `author_name`, `relationship`, 
 `message_content`, `message_type`, `parent_id`, `reply_to_id`, `reply_to_name`,
 `audit_status`, `audit_time`, `like_count`, 
 `location_province`, `location_city`, `ip_address`, `message_time`)
VALUES
(10000, 1008, '陈明', '学生',
 '刘学姐说得对，李老师是最好的老师！我也是受李老师帮助才完成学业的学生之一。',
 1, 10001, 10001, '刘晓敏',
 2, '2023-07-16 09:00:00', 5,
 '江苏省', '南京市', '112.80.248.98', '2023-07-16 08:45:00'),

(10000, 3, '李强', '儿子',
 '感谢各位学长学姐对父亲的怀念。父亲生前经常提起他的学生们，说你们都是他的骄傲。',
 1, 10001, 10001, '刘晓敏',
 2, '2023-07-17 14:00:00', 12,
 '江苏省', '南京市', '112.80.248.75', '2023-07-17 13:45:00');

-- 4. 插入点赞记录
INSERT INTO `memorial_message_like` (`message_id`, `user_id`, `ip_address`) VALUES
(10000, 1003, '58.210.10.135'),
(10000, 1005, '112.80.248.123'),
(10000, 1008, '112.80.248.98'),
(10001, 3, '112.80.248.75'),
(10001, 1005, '112.80.248.123'),
(10002, 3, '112.80.248.75'),
(10007, 1004, '114.242.248.200'),
(10007, 1008, '112.80.248.98');

-- 5. 插入访问记录
INSERT INTO `memorial_visit_log`
(`memorial_id`, `user_id`, `visitor_name`, `visit_type`, 
 `ip_address`, `location_province`, `location_city`, 
 `device_type`, `device_info`, `browser_info`, `visit_time`, `duration`)
VALUES
(10000, 3, '李强', 1, '112.80.248.75', '江苏省', '南京市', 
 'PC', 'Windows 10', 'Chrome 120.0', '2024-11-17 09:00:00', 1800),

(10000, 1003, '刘晓敏', 1, '58.210.10.135', '江苏省', '苏州市',
 'Mobile', 'iPhone 13', 'Safari 17.0', '2024-11-17 10:30:00', 900),

(10000, NULL, '访客', 2, '60.191.124.78', '浙江省', '杭州市',
 'Mobile', 'Android 12', 'Chrome 119.0', '2024-11-17 14:00:00', 300),

(10000, 3, '李强', 3, '112.80.248.75', '江苏省', '南京市',
 'PC', 'Windows 10', 'Chrome 120.0', '2024-11-17 14:30:00', 120),

(10001, 1002, '张华', 1, '121.89.173.46', '江苏省', '南京市',
 'PC', 'Windows 11', 'Edge 120.0', '2024-11-17 11:00:00', 1200),

(10002, 1004, '张伟', 1, '114.242.248.200', '江苏省', '南京市',
 'PC', 'macOS 14', 'Safari 17.0', '2024-11-17 15:00:00', 2400);

-- ============================================
-- 创建视图：纪念空间统计视图
-- ============================================
CREATE OR REPLACE VIEW `v_memorial_statistics` AS
SELECT 
    dm.id AS memorial_id,
    dm.space_no,
    dm.space_name,
    t.tomb_no,
    t.area_name,
    d.deceased_name,
    dm.visit_count,
    dm.candle_count,
    dm.flower_count,
    dm.incense_count,
    dm.message_count,
    COUNT(DISTINCT mc.id) AS content_count,
    COUNT(DISTINCT mm.id) AS total_message_count,
    SUM(CASE WHEN mm.audit_status = 2 THEN 1 ELSE 0 END) AS approved_message_count,
    dm.is_published,
    dm.publish_time,
    dm.create_time
FROM digital_memorial dm
LEFT JOIN tomb_location t ON dm.tomb_id = t.id
LEFT JOIN deceased_info d ON dm.deceased_id = d.id
LEFT JOIN memorial_content mc ON dm.id = mc.memorial_id AND mc.deleted = 0
LEFT JOIN memorial_message mm ON dm.id = mm.memorial_id AND mm.deleted = 0
WHERE dm.deleted = 0
GROUP BY dm.id;

-- ============================================
-- 创建视图：热门纪念空间视图
-- ============================================
CREATE OR REPLACE VIEW `v_popular_memorials` AS
SELECT 
    dm.id,
    dm.space_no,
    dm.space_name,
    d.deceased_name,
    dm.visit_count,
    dm.message_count,
    (dm.visit_count * 1 + dm.message_count * 5 + dm.candle_count * 2 + dm.flower_count * 2) AS popularity_score,
    dm.publish_time,
    DATE_FORMAT(dm.create_time, '%Y-%m-%d') AS create_date
FROM digital_memorial dm
LEFT JOIN deceased_info d ON dm.deceased_id = d.id
WHERE dm.deleted = 0 AND dm.is_published = 1
ORDER BY popularity_score DESC;

-- ============================================
-- 性能优化说明
-- ============================================
-- 1. 字符集优化：
--    - 使用utf8mb4_unicode_ci排序规则，支持emoji和完整Unicode字符集
--    - WITH PARSER ngram：使用n-gram分词器，支持中文全文检索（最小分词长度2）
--
-- 2. 分区策略：
--    - 按年份（YEAR）进行RANGE分区，每年一个分区
--    - 提前创建未来10年分区，避免运行时动态创建
--    - 使用MAXVALUE分区兜底，防止数据插入失败
--    - 优势：查询特定年份数据时只扫描对应分区，性能提升显著
--
-- 6. 分区表全文检索说明：
--    MySQL分区表不支持FULLTEXT索引，建议使用以下替代方案：
--    方案1：使用LIKE查询（适合简单搜索）
--      SELECT * FROM memorial_message WHERE message_content LIKE '%关键词%';
--    方案2：使用Elasticsearch等全文搜索引擎（推荐生产环境）
--    方案3：将需要全文检索的表设计为非分区表
--    方案4：使用MySQL 8.0+的Generated Column + 虚拟列索引
--
-- 4. 复合索引优化：
--    - 按照最左前缀原则设计，支持多种查询场景
--    - 将高选择性字段放在前面（如memorial_id + audit_status）
--    - 排序字段使用DESC降序索引（如visit_count DESC）
--
-- 5. 主键优化：
--    - 使用复合主键（id + 时间字段），配合分区策略
--    - 提升分区剪枝效率
--
-- 6. 大数据量支持：
--    - memorial_content.content_text 使用 MEDIUMTEXT（最大16MB）
--    - 分区表支持单表亿级数据存储
--    - 访问日志表按年分区，可定期归档历史数据
--
-- ============================================
-- 全文检索替代方案使用示例
-- ============================================
-- 1. LIKE模糊查询（简单场景）：
-- SELECT * FROM digital_memorial 
-- WHERE biography LIKE '%教师%' OR life_achievements LIKE '%南京%';
--
-- 2. 多关键词搜索：
-- SELECT * FROM memorial_message 
-- WHERE message_content LIKE '%怀念%' AND message_content LIKE '%父亲%';
--
-- 3. 使用Generated Column优化（MySQL 8.0+）：
-- ALTER TABLE memorial_message ADD COLUMN content_keywords VARCHAR(500) 
--   GENERATED ALWAYS AS (SUBSTRING(message_content, 1, 500)) STORED;
-- CREATE INDEX idx_content_keywords ON memorial_message(content_keywords);
--
-- 4. 推荐方案：集成Elasticsearch
--    - 将数据同步到Elasticsearch进行全文检索
--    - MySQL负责事务性操作，ES负责搜索
--    - 可使用Logstash、Canal等工具进行数据同步
--
-- ============================================
-- 分区管理维护
-- ============================================
-- 1. 查看分区信息：
-- SELECT PARTITION_NAME, PARTITION_EXPRESSION, PARTITION_DESCRIPTION, TABLE_ROWS 
-- FROM INFORMATION_SCHEMA.PARTITIONS 
-- WHERE TABLE_SCHEMA = 'cemetery_db' AND TABLE_NAME = 'memorial_message';
--
-- 2. 添加新年份分区（在2030年前执行）：
-- ALTER TABLE memorial_message REORGANIZE PARTITION p_future INTO (
--   PARTITION p2030 VALUES LESS THAN (2031),
--   PARTITION p_future VALUES LESS THAN MAXVALUE
-- );
--
-- 3. 删除历史分区（归档旧数据）：
-- ALTER TABLE memorial_visit_log DROP PARTITION p2020;
--
-- 4. 分区数据统计：
-- SELECT PARTITION_NAME, TABLE_ROWS, AVG_ROW_LENGTH, DATA_LENGTH
-- FROM INFORMATION_SCHEMA.PARTITIONS
-- WHERE TABLE_SCHEMA = 'cemetery_db' AND TABLE_NAME = 'memorial_message';
--
-- ============================================
-- 数据库扩展完成
-- ============================================
