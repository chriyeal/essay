USE ry;

-- 添加 total_days 字段到 study_plan 表
ALTER TABLE study_plan ADD COLUMN total_days int(11) DEFAULT 0 COMMENT '总天数（多日计划的总天数）' AFTER actual_hours;

SELECT '✅ total_days 字段已添加完成！请刷新浏览器重新测试。' AS result;
