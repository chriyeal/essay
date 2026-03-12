-- 补充缺失的字段 SQL 脚本
-- 在 Navicat 中执行此脚本

USE ry;

-- 添加 learning_goals 字段
ALTER TABLE study_plan ADD COLUMN learning_goals varchar(500) DEFAULT '' COMMENT '学习目标' AFTER description;

-- 添加 total_tasks 字段
ALTER TABLE study_plan ADD COLUMN total_tasks int(11) DEFAULT 0 COMMENT '总任务数' AFTER progress;

-- 添加 completed_tasks 字段
ALTER TABLE study_plan ADD COLUMN completed_tasks int(11) DEFAULT 0 COMMENT '已完成任务数' AFTER total_tasks;

-- 添加 is_template 字段
ALTER TABLE study_plan ADD COLUMN is_template tinyint(1) DEFAULT 0 COMMENT '是否为模板 (0 否 1 是)' AFTER status;

SELECT '✅ 所有缺失字段已添加！' AS result;
