-- ============================================
-- 紧急修复学习计划表缺失字段
-- 请在 Navicat 中打开此文件并执行
-- ============================================

USE ry;

-- 1. 添加 learning_goals 字段
ALTER TABLE study_plan ADD COLUMN learning_goals varchar(500) DEFAULT '' COMMENT '学习目标' AFTER description;

-- 2. 添加 total_tasks 字段  
ALTER TABLE study_plan ADD COLUMN total_tasks int(11) DEFAULT 0 COMMENT '总任务数' AFTER progress;

-- 3. 添加 completed_tasks 字段
ALTER TABLE study_plan ADD COLUMN completed_tasks int(11) DEFAULT 0 COMMENT '已完成任务数' AFTER total_tasks;

-- 4. 添加 is_template 字段
ALTER TABLE study_plan ADD COLUMN is_template tinyint(1) DEFAULT 0 COMMENT '是否为模板 (0 否 1 是)' AFTER status;

SELECT '✅ 所有缺失字段已添加完成！请刷新浏览器重新测试。' AS result;
