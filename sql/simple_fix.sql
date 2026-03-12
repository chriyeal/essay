-- 简单直接的修复 SQL 脚本
-- 在 Navicat 中直接运行此脚本

USE study_system;

-- 直接添加缺失的字段（如果已存在会报错，请忽略错误继续执行）

-- 1. 添加 deadline 字段
ALTER TABLE study_plan ADD COLUMN deadline datetime DEFAULT NULL COMMENT '截止日期' AFTER end_date;

-- 2. 添加 is_completed 字段
ALTER TABLE study_plan ADD COLUMN is_completed tinyint(1) DEFAULT 0 COMMENT '是否完成 (0 未完成 1 已完成)' AFTER status;

-- 3. 添加 completed_time 字段
ALTER TABLE study_plan ADD COLUMN completed_time datetime DEFAULT NULL COMMENT '完成时间' AFTER is_completed;

-- 4. 添加 tags 字段
ALTER TABLE study_plan ADD COLUMN tags varchar(200) DEFAULT '' COMMENT '标签分类' AFTER difficulty;

-- 5. 添加 actual_hours 字段
ALTER TABLE study_plan ADD COLUMN actual_hours decimal(5,2) DEFAULT 0.00 COMMENT '实际时长 (小时)' AFTER estimated_hours;

-- 6. 添加索引
ALTER TABLE study_plan ADD INDEX idx_is_completed (is_completed);
ALTER TABLE study_plan ADD INDEX idx_deadline (deadline);

SELECT '所有字段添加完成！' AS result;
