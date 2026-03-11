-- =============================================
-- 个性化学习计划管理系统 - 数据库脚本
-- 数据库: MySQL 5.7+
-- 字符集: utf8mb4
-- =============================================

-- =============================================
-- 用户表 (user_type: 0=管理员, 1=普通用户)
-- =============================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
  user_id           bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '用户ID',
  user_name         varchar(30)     NOT NULL                   COMMENT '用户账号',
  nick_name         varchar(30)     NOT NULL                   COMMENT '用户昵称',
  email             varchar(50)     DEFAULT ''                 COMMENT '用户邮箱',
  phonenumber       varchar(11)     DEFAULT ''                 COMMENT '手机号码',
  sex               char(1)         DEFAULT '0'                COMMENT '用户性别（0男 1女 2未知）',
  avatar            varchar(100)    DEFAULT ''                 COMMENT '头像地址',
  password          varchar(100)    DEFAULT ''                 COMMENT '密码',
  status            char(1)         DEFAULT '0'                COMMENT '帐号状态（0正常 1停用）',
  del_flag          char(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(128)    DEFAULT ''                 COMMENT '最后登录IP',
  login_date        datetime                                   COMMENT '最后登录时间',
  user_type         char(1)         DEFAULT '1'                COMMENT '用户类型（0管理员 1普通用户）',
  create_time       datetime        DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  update_time       datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  signature         varchar(200)    DEFAULT ''                 COMMENT '个性签名',
  PRIMARY KEY (user_id),
  UNIQUE KEY uk_user_name (user_name)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =============================================
-- 学习计划表
-- =============================================
DROP TABLE IF EXISTS study_plan;
CREATE TABLE study_plan (
  plan_id           bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '计划ID',
  user_id           bigint(20)      NOT NULL                   COMMENT '用户ID',
  plan_name         varchar(100)    NOT NULL                   COMMENT '计划名称',
  deadline          datetime        DEFAULT NULL               COMMENT '截止日期',
  priority          int(1)          DEFAULT 3                  COMMENT '优先级(1-5，5最高)',
  tags              varchar(200)    DEFAULT ''                 COMMENT '标签分类',
  subject           varchar(50)     DEFAULT ''                 COMMENT '学科分类',
  description       varchar(500)    DEFAULT ''                 COMMENT '计划描述',
  estimated_hours   decimal(5,2)    DEFAULT 0.00               COMMENT '预计时长(小时)',
  actual_hours      decimal(5,2)    DEFAULT 0.00               COMMENT '实际时长(小时)',
  is_completed      tinyint(1)      DEFAULT 0                  COMMENT '是否完成(0未完成1已完成)',
  completed_time    datetime        DEFAULT NULL               COMMENT '完成时间',
  create_time       datetime        DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  update_time       datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (plan_id),
  KEY idx_user_id (user_id),
  KEY idx_deadline (deadline),
  KEY idx_is_completed (is_completed)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学习计划表';

-- =============================================
-- 番茄钟记录表
-- =============================================
DROP TABLE IF EXISTS tomato_record;
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
DROP TABLE IF EXISTS study_statistics;
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
-- 初始化用户数据
-- =============================================
-- 管理员账号: admin 密码: admin123
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, password, status, del_flag, user_type, signature) VALUES
(1, 'admin', '管理员', 'admin@example.com', '13800000000', '0', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '0', '系统管理员');

-- 普通用户: zhangsan 密码: 123456
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, password, status, del_flag, user_type, signature) VALUES
(100, 'zhangsan', '张三', 'zhangsan@example.com', '13800138001', '0', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '1', '每天进步一点点');

-- 普通用户: lisi 密码: 123456
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, password, status, del_flag, user_type, signature) VALUES
(101, 'lisi', '李四', 'lisi@example.com', '13800138002', '1', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '1', '学习使我快乐');

-- 普通用户: wangwu 密码: 123456
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, password, status, del_flag, user_type, signature) VALUES
(102, 'wangwu', '王五', 'wangwu@example.com', '13800138003', '0', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '1', '坚持就是胜利');

-- =============================================
-- 学习计划示例数据
-- =============================================
-- 张三的学习计划
INSERT INTO study_plan (user_id, plan_name, deadline, priority, tags, subject, description, estimated_hours, actual_hours, is_completed, completed_time) VALUES
(100, '高等数学期末复习', '2026-03-20 18:00:00', 5, '数学,考试', '数学', '复习微积分、线性代数重点章节', 20.00, 8.50, 0, NULL),
(100, '英语四级备考', '2026-04-15 18:00:00', 4, '英语,考试', '英语', '背诵单词、练习听力、阅读理解', 30.00, 12.00, 0, NULL),
(100, 'Java编程入门', '2026-03-25 18:00:00', 3, '编程,Java', '计算机', '学习Java基础语法和面向对象', 15.00, 15.00, 1, '2026-03-10 16:30:00');

-- 李四的学习计划
INSERT INTO study_plan (user_id, plan_name, deadline, priority, tags, subject, description, estimated_hours, actual_hours, is_completed, completed_time) VALUES
(101, '数据结构学习', '2026-03-30 18:00:00', 5, '编程,数据结构', '计算机', '学习链表、树、图等基础数据结构', 25.00, 10.00, 0, NULL),
(101, '毕业论文写作', '2026-05-01 18:00:00', 5, '论文,毕业', '综合', '完成毕业论文初稿', 40.00, 5.00, 0, NULL),
(101, 'Python数据分析', '2026-04-10 18:00:00', 3, 'Python,数据分析', '计算机', '学习Pandas和Matplotlib', 18.00, 18.00, 1, '2026-03-09 14:00:00');

-- 王五的学习计划
INSERT INTO study_plan (user_id, plan_name, deadline, priority, tags, subject, description, estimated_hours, actual_hours, is_completed, completed_time) VALUES
(102, '机器学习入门', '2026-04-20 18:00:00', 5, 'AI,机器学习', '计算机', '学习监督学习和非监督学习基础', 35.00, 15.00, 0, NULL),
(102, '前端开发实战', '2026-03-28 18:00:00', 4, '前端,Vue', '计算机', '完成Vue项目实战练习', 20.00, 20.00, 1, '2026-03-08 17:00:00'),
(102, '操作系统原理', '2026-04-05 18:00:00', 3, '计算机,操作系统', '计算机', '学习进程管理和内存管理', 12.00, 6.00, 0, NULL);

-- =============================================
-- 番茄钟记录示例数据
-- =============================================
-- 张三的番茄钟记录
INSERT INTO tomato_record (user_id, plan_id, tomato_duration, rest_duration, start_time, end_time, status) VALUES
(100, 1, 25, 5, '2026-03-10 09:00:00', '2026-03-10 09:25:00', 1),
(100, 1, 25, 5, '2026-03-10 09:35:00', '2026-03-10 10:00:00', 1),
(100, 2, 30, 10, '2026-03-10 14:00:00', '2026-03-10 14:30:00', 1),
(100, 1, 25, 5, '2026-03-10 15:00:00', '2026-03-10 15:25:00', 1);

-- 李四的番茄钟记录
INSERT INTO tomato_record (user_id, plan_id, tomato_duration, rest_duration, start_time, end_time, status) VALUES
(101, 4, 25, 5, '2026-03-10 10:00:00', '2026-03-10 10:25:00', 1),
(101, 4, 25, 5, '2026-03-10 10:35:00', '2026-03-10 11:00:00', 1),
(101, 5, 30, 10, '2026-03-10 16:00:00', '2026-03-10 16:30:00', 1);

-- 王五的番茄钟记录
INSERT INTO tomato_record (user_id, plan_id, tomato_duration, rest_duration, start_time, end_time, status) VALUES
(102, 7, 25, 5, '2026-03-10 08:00:00', '2026-03-10 08:25:00', 1),
(102, 7, 25, 5, '2026-03-10 08:35:00', '2026-03-10 09:00:00', 1),
(102, 9, 20, 5, '2026-03-10 11:00:00', '2026-03-10 11:20:00', 1);

-- =============================================
-- 学习统计示例数据
-- =============================================
INSERT INTO study_statistics (user_id, study_date, total_study_time, tomato_count, completed_plans, subject_distribution) VALUES
(100, '2026-03-10', 105, 4, 0, '{"数学":60,"英语":45}'),
(100, '2026-03-09', 75, 3, 1, '{"数学":45,"英语":30}'),
(100, '2026-03-08', 50, 2, 0, '{"数学":50}'),
(101, '2026-03-10', 85, 3, 0, '{"计算机":55,"综合":30}'),
(101, '2026-03-09', 60, 2, 1, '{"计算机":60}'),
(101, '2026-03-08', 45, 2, 0, '{"计算机":45}'),
(102, '2026-03-10', 70, 3, 0, '{"计算机":70}'),
(102, '2026-03-09', 55, 2, 1, '{"计算机":55}'),
(102, '2026-03-08', 40, 2, 0, '{"计算机":40}');

-- =============================================
-- 完成
-- =============================================
SELECT '学习计划管理系统数据库初始化完成！' AS message;
