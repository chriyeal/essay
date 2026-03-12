-- 修复版学习计划系统数据库初始化脚本
-- 先删除旧表再重新创建，避免字段不匹配问题

SET NAMES utf8mb4;

-- =============================================
-- 清理现有表结构
-- =============================================
DROP TABLE IF EXISTS study_statistics;
DROP TABLE IF EXISTS tomato_record;
DROP TABLE IF EXISTS study_plan;
DROP TABLE IF EXISTS sys_user;

-- =============================================
-- 用户表 (重新创建完整结构)
-- =============================================
CREATE TABLE sys_user (
  user_id           bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '用户ID',
  dept_id           bigint(20)      DEFAULT NULL               COMMENT '部门ID',
  user_name         varchar(30)     NOT NULL                   COMMENT '用户账号',
  nick_name         varchar(30)     NOT NULL                   COMMENT '用户昵称',
  user_type         varchar(2)      DEFAULT '00'               COMMENT '用户类型（00系统用户）',
  email             varchar(50)     DEFAULT ''                 COMMENT '用户邮箱',
  phonenumber       varchar(11)     DEFAULT ''                 COMMENT '手机号码',
  sex               char(1)         DEFAULT '0'                COMMENT '用户性别（0男 1女 2未知）',
  avatar            varchar(100)    DEFAULT ''                 COMMENT '头像路径',
  password          varchar(100)    DEFAULT ''                 COMMENT '密码',
  status            char(1)         DEFAULT '0'                COMMENT '帐号状态（0正常 1停用）',
  del_flag          char(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(128)    DEFAULT ''                 COMMENT '最后登录IP',
  login_date        datetime        DEFAULT NULL               COMMENT '最后登录时间',
  create_by         varchar(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       datetime        DEFAULT NULL               COMMENT '创建时间',
  update_by         varchar(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       datetime        DEFAULT NULL               COMMENT '更新时间',
  remark            varchar(500)    DEFAULT NULL               COMMENT '备注',
  signature         varchar(500)    DEFAULT ''                 COMMENT '个性签名',
  PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 插入默认用户数据
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark, signature) VALUES
(1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-12-18 16:30:00', 'admin', '2024-12-18 16:30:00', '', NULL, '管理员', '专注学习，持续进步'),
(2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-12-18 16:30:00', 'admin', '2024-12-18 16:30:00', '', NULL, '测试员', '学而时习之，不亦说乎');

-- =============================================
-- 学习计划表
-- =============================================
CREATE TABLE study_plan (
  plan_id           bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '计划ID',
  user_id           bigint(20)      NOT NULL                   COMMENT '用户ID',
  plan_name         varchar(100)    NOT NULL                   COMMENT '计划名称',
  plan_type         varchar(20)     DEFAULT 'overall'          COMMENT '计划类型(overall总体计划, today今日计划)',
  parent_plan_id    bigint(20)      DEFAULT NULL               COMMENT '父计划ID(用于关联总体计划和今日计划)',
  start_date        date            DEFAULT NULL               COMMENT '开始日期',
  end_date          date            DEFAULT NULL               COMMENT '结束日期',
  deadline          datetime        DEFAULT NULL               COMMENT '截止日期',
  priority          int(1)          DEFAULT 1                  COMMENT '优先级(0低1中2高)',
  difficulty        int(1)          DEFAULT 2                  COMMENT '难度等级(1简单2中等3困难)',
  tags              varchar(200)    DEFAULT ''                 COMMENT '标签分类',
  subject           varchar(50)     DEFAULT ''                 COMMENT '学科分类',
  description       varchar(500)    DEFAULT ''                 COMMENT '计划描述',
  learning_goals    varchar(500)    DEFAULT ''                 COMMENT '学习目标',
  estimated_hours   decimal(5,2)    DEFAULT 0.00               COMMENT '预计时长(小时)',
  actual_hours      decimal(5,2)    DEFAULT 0.00               COMMENT '实际时长(小时)',
  progress          int(3)          DEFAULT 0                  COMMENT '进度百分比(0-100)',
  total_tasks       int(11)         DEFAULT 0                  COMMENT '总任务数',
  completed_tasks   int(11)         DEFAULT 0                  COMMENT '已完成任务数',
  status            varchar(20)     DEFAULT '0'                COMMENT '状态(0进行中1已完成2已取消)',
  is_template       tinyint(1)      DEFAULT 0                  COMMENT '是否为模板(0否1是)',
  is_completed      tinyint(1)      DEFAULT 0                  COMMENT '是否完成(0未完成1已完成)',
  completed_time    datetime        DEFAULT NULL               COMMENT '完成时间',
  create_time       datetime        DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  update_time       datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (plan_id),
  KEY idx_user_id (user_id),
  KEY idx_plan_type (plan_type),
  KEY idx_parent_plan_id (parent_plan_id),
  KEY idx_start_date (start_date),
  KEY idx_end_date (end_date),
  KEY idx_status (status),
  KEY idx_is_template (is_template),
  KEY idx_deadline (deadline),
  KEY idx_is_completed (is_completed)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学习计划表';

-- =============================================
-- 番茄钟记录表
-- =============================================
CREATE TABLE tomato_record (
  record_id         bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '记录ID',
  user_id           bigint(20)      NOT NULL                   COMMENT '用户ID',
  plan_id           bigint(20)      DEFAULT NULL               COMMENT '关联计划ID',
  tomato_duration   int(11)         DEFAULT 25                 COMMENT '番茄钟时长(分钟)',
  rest_duration     int(11)         DEFAULT 5                  COMMENT '休息时长(分钟)',
  start_time        datetime        NOT NULL                   COMMENT '开始时间',
  end_time          datetime        DEFAULT NULL               COMMENT '结束时间',
  status            tinyint(1)      DEFAULT 0                  COMMENT '状态(0进行中1已完成2中断)',
  create_time       datetime        DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  PRIMARY KEY (record_id),
  KEY idx_user_id (user_id),
  KEY idx_plan_id (plan_id),
  KEY idx_start_time (start_time)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='番茄钟记录表';

-- =============================================
-- 学习统计表
-- =============================================
CREATE TABLE study_statistics (
  stat_id             bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '统计ID',
  user_id             bigint(20)      NOT NULL                   COMMENT '用户ID',
  study_date          date            NOT NULL                   COMMENT '学习日期',
  total_study_time    int(11)         DEFAULT 0                  COMMENT '总学习时长(分钟)',
  tomato_count        int(11)         DEFAULT 0                  COMMENT '番茄钟数量',
  completed_plans     int(11)         DEFAULT 0                  COMMENT '完成计划数',
  subject_distribution varchar(500)   DEFAULT ''                 COMMENT '学科分布(JSON格式)',
  create_time         datetime        DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  update_time         datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (stat_id),
  UNIQUE KEY uk_user_date (user_id, study_date),
  KEY idx_user_id (user_id),
  KEY idx_study_date (study_date)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学习统计表';

-- =============================================
-- 插入示例学习数据
-- =============================================
INSERT INTO study_plan (user_id, plan_name, plan_type, start_date, end_date, priority, difficulty, subject, description, learning_goals, estimated_hours, progress, total_tasks, completed_tasks, status) VALUES
(1, 'Java基础学习', 'overall', '2026-03-01', '2026-04-30', 2, 2, '计算机', '系统学习Java编程语言基础', '掌握Java语法;理解面向对象编程;完成基础练习', 60.00, 45, 10, 4, '0'),
(1, '今日Java练习', 'today', '2026-03-12', '2026-03-12', 2, 2, '计算机', '完成Java数组和集合练习', '完成数组练习题5道;完成集合练习题3道', 3.00, 60, 8, 5, '0'),
(1, '数据结构复习', 'overall', '2026-03-15', '2026-04-15', 1, 3, '计算机', '复习常见的数据结构知识', '理解线性表;掌握树和图的基本概念;完成相关算法练习', 40.00, 20, 15, 3, '0'),
(2, '高等数学', 'overall', '2026-03-01', '2026-05-31', 2, 3, '数学', '学习微积分和线性代数', '掌握极限和导数;理解积分概念;学会矩阵运算', 80.00, 30, 20, 6, '0'),
(2, '今日微积分练习', 'today', '2026-03-12', '2026-03-12', 2, 3, '数学', '完成导数计算练习', '完成导数计算题10道;理解链式法则应用', 2.50, 0, 10, 0, '0');

INSERT INTO tomato_record (user_id, plan_id, tomato_duration, rest_duration, start_time, end_time, status) VALUES
(1, 1, 25, 5, '2026-03-12 09:00:00', '2026-03-12 09:25:00', 1),
(1, 1, 25, 5, '2026-03-12 09:35:00', '2026-03-12 10:00:00', 1),
(1, 2, 30, 10, '2026-03-12 14:00:00', '2026-03-12 14:30:00', 1),
(2, 4, 25, 5, '2026-03-12 10:00:00', '2026-03-12 10:25:00', 1),
(2, 4, 25, 5, '2026-03-12 10:35:00', '2026-03-12 11:00:00', 1);

INSERT INTO study_statistics (user_id, study_date, total_study_time, tomato_count, completed_plans, subject_distribution) VALUES
(1, '2026-03-12', 120, 3, 1, '{"计算机": 120}'),
(2, '2026-03-12', 80, 2, 0, '{"数学": 80}');

COMMIT;