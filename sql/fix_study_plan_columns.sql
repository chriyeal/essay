-- 修复学习计划表缺失字段的 SQL 脚本
-- 用于添加 is_completed 和 deadline 字段

USE study_system;

-- 添加 deadline 字段（如果不存在）
SET @dbname = DATABASE();
SELECT COUNT(*) INTO @col_exists FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = 'study_plan' AND COLUMN_NAME = 'deadline';

SET @sqlstmt = CASE WHEN @col_exists = 0 THEN 
    'ALTER TABLE study_plan ADD COLUMN deadline datetime DEFAULT NULL COMMENT ''截止日期'' AFTER end_date'
ELSE 'SELECT ''deadline 字段已存在''' END;
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 is_completed 字段（如果不存在）
SELECT COUNT(*) INTO @col_exists FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = 'study_plan' AND COLUMN_NAME = 'is_completed';

SET @sqlstmt = CASE WHEN @col_exists = 0 THEN 
    'ALTER TABLE study_plan ADD COLUMN is_completed tinyint(1) DEFAULT 0 COMMENT ''是否完成 (0 未完成 1 已完成)'' AFTER status'
ELSE 'SELECT ''is_completed 字段已存在''' END;
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 completed_time 字段（如果不存在）
SELECT COUNT(*) INTO @col_exists FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = 'study_plan' AND COLUMN_NAME = 'completed_time';

SET @sqlstmt = CASE WHEN @col_exists = 0 THEN 
    'ALTER TABLE study_plan ADD COLUMN completed_time datetime DEFAULT NULL COMMENT ''完成时间'' AFTER is_completed'
ELSE 'SELECT ''completed_time 字段已存在''' END;
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 tags 字段（如果不存在）
SELECT COUNT(*) INTO @col_exists FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = 'study_plan' AND COLUMN_NAME = 'tags';

SET @sqlstmt = CASE WHEN @col_exists = 0 THEN 
    'ALTER TABLE study_plan ADD COLUMN tags varchar(200) DEFAULT '''' COMMENT ''标签分类'' AFTER difficulty'
ELSE 'SELECT ''tags 字段已存在''' END;
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 actual_hours 字段（如果不存在）
SELECT COUNT(*) INTO @col_exists FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = 'study_plan' AND COLUMN_NAME = 'actual_hours';

SET @sqlstmt = CASE WHEN @col_exists = 0 THEN 
    'ALTER TABLE study_plan ADD COLUMN actual_hours decimal(5,2) DEFAULT 0.00 COMMENT ''实际时长 (小时)'' AFTER estimated_hours'
ELSE 'SELECT ''actual_hours 字段已存在''' END;
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加索引
ALTER TABLE study_plan ADD INDEX idx_is_completed (is_completed);
ALTER TABLE study_plan ADD INDEX idx_deadline (deadline);

SELECT '学习计划表字段修复完成！' AS result;
