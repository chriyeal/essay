-- 个性化学习计划管理系统数据库脚本

-- ----------------------------
-- 1、学习计划表
-- ----------------------------
drop table if exists study_plan;
create table study_plan (
  plan_id           bigint(20)      not null auto_increment    comment '学习计划ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  plan_name         varchar(100)    not null                   comment '学习计划名称',
  deadline          datetime        default null               comment '截止日期',
  priority          int(1)          default 3                  comment '重要程度(1-5级，5最重要)',
  tags              varchar(200)    default ''                 comment '标签分类(逗号分隔)',
  auto_generate_daily tinyint(1)    default 0                  comment '是否系统生成每日计划(0否1是)',
  daily_plan        text            default null               comment '自定义每日计划内容',
  is_completed      tinyint(1)      default 0                  comment '是否完成(0未完成1已完成)',
  completed_time    datetime        default null               comment '完成时间',
  estimated_hours   decimal(5,2)    default 0.00               comment '预计学习时长(小时)',
  actual_hours      decimal(5,2)    default 0.00               comment '实际学习时长(小时)',
  subject           varchar(50)     default ''                 comment '学科/类别',
  description       varchar(500)    default ''                 comment '计划描述',
  create_time       datetime        default current_timestamp  comment '创建时间',
  update_time       datetime        default current_timestamp on update current_timestamp comment '更新时间',
  primary key (plan_id),
  key idx_user_id (user_id),
  key idx_deadline (deadline),
  key idx_priority (priority),
  key idx_is_completed (is_completed)
) engine=innodb auto_increment=1 comment = '学习计划表';

-- ----------------------------
-- 2、番茄钟记录表
-- ----------------------------
drop table if exists tomato_record;
create table tomato_record (
  record_id         bigint(20)      not null auto_increment    comment '记录ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  plan_id           bigint(20)      default null               comment '关联学习计划ID',
  tomato_duration   int(11)         default 25                 comment '番茄钟时长(分钟)',
  rest_duration     int(11)         default 5                  comment '休息时长(分钟)',
  start_time        datetime        not null                   comment '开始时间',
  end_time          datetime        default null               comment '结束时间',
  status            tinyint(1)      default 0                  comment '状态(0进行中1已完成2中断)',
  interruption_reason varchar(200)  default ''                 comment '中断原因',
  create_time       datetime        default current_timestamp  comment '创建时间',
  primary key (record_id),
  key idx_user_id (user_id),
  key idx_plan_id (plan_id),
  key idx_start_time (start_time),
  key idx_status (status)
) engine=innodb auto_increment=1 comment = '番茄钟记录表';

-- ----------------------------
-- 3、学习统计数据表
-- ----------------------------
drop table if exists study_statistics;
create table study_statistics (
  stat_id           bigint(20)      not null auto_increment    comment '统计ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  study_date        date            not null                   comment '学习日期',
  plan_time_spent   int(11)         default 0                  comment '计划学习时长(分钟)',
  tomato_time_spent int(11)         default 0                  comment '番茄钟学习时长(分钟)',
  total_study_time  int(11)         default 0                  comment '总学习时长(分钟)',
  completed_plans   int(11)         default 0                  comment '完成计划数',
  subject_distribution json          default null               comment '各科目学习时长分布',
  productivity_score decimal(3,2)   default 0.00               comment '学习效率评分(0-10)',
  create_time       datetime        default current_timestamp  comment '创建时间',
  update_time       datetime        default current_timestamp on update current_timestamp comment '更新时间',
  primary key (stat_id),
  unique key uk_user_date (user_id, study_date),
  key idx_user_id (user_id),
  key idx_study_date (study_date)
) engine=innodb auto_increment=1 comment = '学习统计数据表';

-- ----------------------------
-- 4、用户学习偏好表
-- ----------------------------
drop table if exists user_study_preference;
create table user_study_preference (
  pref_id           bigint(20)      not null auto_increment    comment '偏好ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  preferred_subjects json            default null               comment '偏好学科(json数组)',
  study_time_slots  json            default null               comment '偏好的学习时间段(json数组)',
  focus_duration    int(11)         default 25                 comment '专注时长偏好(分钟)',
  rest_duration     int(11)         default 5                  comment '休息时长偏好(分钟)',
  notification_enabled tinyint(1)    default 1                  comment '是否开启学习提醒(0否1是)',
  reminder_time     varchar(20)     default '09:00'            comment '每日提醒时间',
  weekly_goal       int(11)         default 20                 comment '每周学习目标(小时)',
  create_time       datetime        default current_timestamp  comment '创建时间',
  update_time       datetime        default current_timestamp on update current_timestamp comment '更新时间',
  primary key (pref_id),
  unique key uk_user_id (user_id)
) engine=innodb auto_increment=1 comment = '用户学习偏好表';

-- ----------------------------
-- 修改用户表，添加学习系统相关字段
-- ----------------------------
alter table sys_user 
add column signature varchar(200) default '' comment '个性签名',
add column learning_points int(11) default 0 comment '学习积分',
add column total_study_time int(11) default 0 comment '累计学习时长(分钟)',
add column last_study_date date default null comment '最后学习日期';

-- ----------------------------
-- 插入学习相关的字典数据
-- ----------------------------
-- 学习计划优先级字典
insert into sys_dict_type values(100, '学习计划优先级', 'study_plan_priority', '0', 'admin', sysdate(), '', null, '学习计划优先级');
insert into sys_dict_data values(100, 1, '紧急重要', '5', 'study_plan_priority', '', 'danger', 'Y', '0', 'admin', sysdate(), '', null, '紧急且重要');
insert into sys_dict_data values(101, 2, '重要不紧急', '4', 'study_plan_priority', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '重要但不紧急');
insert into sys_dict_data values(102, 3, '紧急不重要', '3', 'study_plan_priority', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '紧急但不重要');
insert into sys_dict_data values(103, 4, '一般', '2', 'study_plan_priority', '', 'info', 'Y', '0', 'admin', sysdate(), '', null, '一般优先级');
insert into sys_dict_data values(104, 5, '低', '1', 'study_plan_priority', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '低优先级');

-- 学习科目字典
insert into sys_dict_type values(101, '学习科目', 'study_subject', '0', 'admin', sysdate(), '', null, '学习科目分类');
insert into sys_dict_data values(105, 1, '数学', 'mathematics', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '数学学科');
insert into sys_dict_data values(106, 2, '英语', 'english', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '英语学科');
insert into sys_dict_data values(107, 3, '语文', 'chinese', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '语文学科');
insert into sys_dict_data values(108, 4, '物理', 'physics', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '物理学科');
insert into sys_dict_data values(109, 5, '化学', 'chemistry', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '化学学科');
insert into sys_dict_data values(110, 6, '生物', 'biology', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '生物学科');
insert into sys_dict_data values(111, 7, '历史', 'history', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '历史学科');
insert into sys_dict_data values(112, 8, '地理', 'geography', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '地理学科');
insert into sys_dict_data values(113, 9, '政治', 'politics', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '政治学科');
insert into sys_dict_data values(114, 10, '计算机', 'computer', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '计算机学科');
insert into sys_dict_data values(115, 11, '其他', 'other', 'study_subject', '', '', 'Y', '0', 'admin', sysdate(), '', null, '其他学科');

-- 番茄钟状态字典
insert into sys_dict_type values(102, '番茄钟状态', 'tomato_status', '0', 'admin', sysdate(), '', null, '番茄钟状态');
insert into sys_dict_data values(116, 1, '进行中', '0', 'tomato_status', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '番茄钟进行中');
insert into sys_dict_data values(117, 2, '已完成', '1', 'tomato_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '番茄钟已完成');
insert into sys_dict_data values(118, 3, '已中断', '2', 'tomato_status', '', 'danger', 'Y', '0', 'admin', sysdate(), '', null, '番茄钟已中断');

-- ----------------------------
-- 初始化管理员用户为管理员角色，普通用户为普通角色
-- ----------------------------
-- 确保角色数据正确
update sys_user set user_type = 'admin' where user_name = 'admin';
update sys_user set user_type = 'normal' where user_name = 'ry';

-- ----------------------------
-- 创建视图：用户学习概况
-- ----------------------------
create view user_study_overview as
select 
    u.user_id,
    u.user_name,
    u.nick_name,
    u.avatar,
    u.signature,
    u.learning_points,
    u.total_study_time,
    u.last_study_date,
    count(sp.plan_id) as total_plans,
    sum(case when sp.is_completed = 1 then 1 else 0 end) as completed_plans,
    avg(sp.priority) as avg_priority,
    max(st.study_date) as recent_study_date
from sys_user u
left join study_plan sp on u.user_id = sp.user_id
left join study_statistics st on u.user_id = st.user_id
where u.del_flag = '0'
group by u.user_id;

-- ----------------------------
-- 创建视图：每日学习统计
-- ----------------------------
create view daily_study_stats as
select 
    ss.user_id,
    u.nick_name,
    ss.study_date,
    ss.total_study_time,
    ss.completed_plans,
    ss.productivity_score,
    count(tr.record_id) as tomato_count,
    sum(tr.tomato_duration) as tomato_minutes
from study_statistics ss
join sys_user u on ss.user_id = u.user_id
left join tomato_record tr on ss.user_id = tr.user_id 
    and date(tr.start_time) = ss.study_date 
    and tr.status = 1
where u.del_flag = '0'
group by ss.user_id, ss.study_date;
