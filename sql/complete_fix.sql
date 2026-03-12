-- ============================================
-- 完整的数据库表结构修复脚本
-- 数据库名称：ry
-- 执行前请确保已选择 ry 数据库
-- ============================================

USE ry;

-- 检查并添加缺失字段到 study_plan 表
ALTER TABLE study_plan ADD COLUMN learning_goals varchar(500) DEFAULT '' COMMENT '学习目标' AFTER description;
ALTER TABLE study_plan ADD COLUMN total_tasks int(11) DEFAULT 0 COMMENT '总任务数' AFTER progress;
ALTER TABLE study_plan ADD COLUMN completed_tasks int(11) DEFAULT 0 COMMENT '已完成任务数' AFTER total_tasks;
ALTER TABLE study_plan ADD COLUMN is_template tinyint(1) DEFAULT 0 COMMENT '是否为模板 (0 否 1 是)' AFTER status;

SELECT '✅ study_plan 表所有缺失字段已添加完成！' AS result;
