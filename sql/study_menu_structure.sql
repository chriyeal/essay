-- ====================
-- 个性化学习计划系统菜单结构
-- ====================

-- 删除原有的菜单结构
DELETE FROM sys_menu WHERE menu_id <= 200000;

-- 插入新的学习系统菜单结构
-- 首页菜单
INSERT INTO sys_menu VALUES('900', '首页', '0', '0', 'index', 'index', '', '', 1, 0, 'C', '0', '0', '', 'dashboard', 'admin', sysdate(), '', null, '首页');

-- 一级菜单
INSERT INTO sys_menu VALUES('1000', '学习计划', '0', '1', 'study-plan', 'study/plan/index', '', '', 1, 0, 'C', '0', '0', 'study:plan:list', 'education', 'admin', sysdate(), '', null, '学习计划菜单');
INSERT INTO sys_menu VALUES('1001', '番茄钟', '0', '2', 'tomato-clock', 'study/tomato/index', '', '', 1, 0, 'C', '0', '0', 'study:tomato:list', 'clock', 'admin', sysdate(), '', null, '番茄钟菜单');
INSERT INTO sys_menu VALUES('1002', '数据统计', '0', '3', 'statistics', 'study/statistics/index', '', '', 1, 0, 'C', '0', '0', 'study:statistics:list', 'chart', 'admin', sysdate(), '', null, '学习数据统计菜单');

-- 二级菜单 - 用户管理（仅管理员可见）
INSERT INTO sys_menu VALUES('1010', '用户管理', '0', '4', 'user-management', 'system/user/admin', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', sysdate(), '', null, '用户管理菜单');

-- 学习计划子菜单（按钮权限）
INSERT INTO sys_menu VALUES('100001', '学习计划查询', '1000', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'study:plan:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100002', '学习计划新增', '1000', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'study:plan:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100003', '学习计划修改', '1000', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'study:plan:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100004', '学习计划删除', '1000', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'study:plan:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100005', '学习计划详情', '1000', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'study:plan:detail', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100006', '学习计划完成', '1000', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'study:plan:complete', '#', 'admin', sysdate(), '', null, '');

-- 番茄钟子菜单（按钮权限）
INSERT INTO sys_menu VALUES('100101', '番茄钟开始', '1001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'study:tomato:start', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100102', '番茄钟暂停', '1001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'study:tomato:pause', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100103', '番茄钟恢复', '1001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'study:tomato:resume', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100104', '番茄钟结束', '1001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'study:tomato:stop', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100105', '番茄钟记录查询', '1001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'study:tomato:query', '#', 'admin', sysdate(), '', null, '');

-- 数据统计子菜单（按钮权限）
INSERT INTO sys_menu VALUES('100201', '统计数据查询', '1002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'study:statistics:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('100202', '学习报告生成', '1002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'study:statistics:report', '#', 'admin', sysdate(), '', null, '');

-- 用户管理子菜单（按钮权限，仅管理员）
INSERT INTO sys_menu VALUES('101001', '用户查询', '1010', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('101002', '用户新增', '1010', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('101003', '用户修改', '1010', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('101004', '用户删除', '1010', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('101005', '重置密码', '1010', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', sysdate(), '', null, '');

-- 更新角色菜单关联关系
-- 删除原有的角色菜单关联
DELETE FROM sys_role_menu;

-- 普通用户角色权限（角色ID=2）
INSERT INTO sys_role_menu VALUES ('2', '900'); -- 首页
INSERT INTO sys_role_menu VALUES ('2', '1000'); -- 学习计划
INSERT INTO sys_role_menu VALUES ('2', '100001'); -- 学习计划查询
INSERT INTO sys_role_menu VALUES ('2', '100002'); -- 学习计划新增
INSERT INTO sys_role_menu VALUES ('2', '100003'); -- 学习计划修改
INSERT INTO sys_role_menu VALUES ('2', '100004'); -- 学习计划删除
INSERT INTO sys_role_menu VALUES ('2', '100005'); -- 学习计划详情
INSERT INTO sys_role_menu VALUES ('2', '100006'); -- 学习计划完成

INSERT INTO sys_role_menu VALUES ('2', '1001'); -- 番茄钟
INSERT INTO sys_role_menu VALUES ('2', '100101'); -- 番茄钟开始
INSERT INTO sys_role_menu VALUES ('2', '100102'); -- 番茄钟暂停
INSERT INTO sys_role_menu VALUES ('2', '100103'); -- 番茄钟恢复
INSERT INTO sys_role_menu VALUES ('2', '100104'); -- 番茄钟结束
INSERT INTO sys_role_menu VALUES ('2', '100105'); -- 番茄钟记录查询

INSERT INTO sys_role_menu VALUES ('2', '1002'); -- 数据统计
INSERT INTO sys_role_menu VALUES ('2', '100201'); -- 统计数据查询
INSERT INTO sys_role_menu VALUES ('2', '100202'); -- 学习报告生成

-- 管理员角色权限（角色ID=1）
INSERT INTO sys_role_menu VALUES ('1', '900'); -- 首页
INSERT INTO sys_role_menu VALUES ('1', '1000'); -- 学习计划
INSERT INTO sys_role_menu VALUES ('1', '100001'); -- 学习计划查询
INSERT INTO sys_role_menu VALUES ('1', '100002'); -- 学习计划新增
INSERT INTO sys_role_menu VALUES ('1', '100003'); -- 学习计划修改
INSERT INTO sys_role_menu VALUES ('1', '100004'); -- 学习计划删除
INSERT INTO sys_role_menu VALUES ('1', '100005'); -- 学习计划详情
INSERT INTO sys_role_menu VALUES ('1', '100006'); -- 学习计划完成

INSERT INTO sys_role_menu VALUES ('1', '1001'); -- 番茄钟
INSERT INTO sys_role_menu VALUES ('1', '100101'); -- 番茄钟开始
INSERT INTO sys_role_menu VALUES ('1', '100102'); -- 番茄钟暂停
INSERT INTO sys_role_menu VALUES ('1', '100103'); -- 番茄钟恢复
INSERT INTO sys_role_menu VALUES ('1', '100104'); -- 番茄钟结束
INSERT INTO sys_role_menu VALUES ('1', '100105'); -- 番茄钟记录查询

INSERT INTO sys_role_menu VALUES ('1', '1002'); -- 数据统计
INSERT INTO sys_role_menu VALUES ('1', '100201'); -- 统计数据查询
INSERT INTO sys_role_menu VALUES ('1', '100202'); -- 学习报告生成

INSERT INTO sys_role_menu VALUES ('1', '1010'); -- 用户管理
INSERT INTO sys_role_menu VALUES ('1', '101001'); -- 用户查询
INSERT INTO sys_role_menu VALUES ('1', '101002'); -- 用户新增
INSERT INTO sys_role_menu VALUES ('1', '101003'); -- 用户修改
INSERT INTO sys_role_menu VALUES ('1', '101004'); -- 用户删除
INSERT INTO sys_role_menu VALUES ('1', '101005'); -- 重置密码