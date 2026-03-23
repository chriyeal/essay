# 个性化学习计划管理系统

## Personal Learning Plan Management System

<p align="center">
  <img src="https://img.shields.io/badge/Spring-Boot-2.5.15-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-2.6-blue" alt="Vue">
  <img src="https://img.shields.io/badge/Element-UI-2.15-orange" alt="Element UI">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue" alt="MySQL">
</p>

---

## 一、系统简介

### 1.1 项目背景

随着在线教育和终身学习理念的普及，学生和自学者需要一个能够科学管理学习计划、提升学习效率的工具。传统的学习计划管理方式存在以下痛点：

- **计划制定盲目**：缺乏系统性的学习目标和阶段性规划
- **执行缺乏监督**：学习过程中容易拖延，缺乏有效的时间管理工具
- **进度不可视化**：无法直观了解学习进度和效果
- **数据无法追踪**：学习成果缺乏量化的数据支撑

本系统应运而生，旨在为用户提供一套完整的**个性化学习计划管理解决方案**。

### 1.2 系统定位

本系统是一个基于 **Spring Boot + Vue** 技术栈开发的**前后端分离**学习管理系统，专注于帮助用户：

1. **制定科学的学习计划**：支持多日计划（总体计划）和每日计划的创建与管理
2. **提升专注效率**：集成番茄工作法，通过计时器帮助用户保持专注
3. **追踪学习数据**：记录和分析学习时长、完成任务数等关键指标
4. **可视化进度展示**：通过进度条、统计卡片等形式直观展示学习状态

### 1.3 技术栈

| 层级 | 技术选型 | 版本 |
|------|---------|------|
| 后端框架 | Spring Boot | 2.5.15 |
| 安全框架 | Spring Security + JWT | - |
| 数据库 | MySQL | 8.0 |
| ORM | MyBatis | - |
| 前端框架 | Vue.js | 2.6 |
| UI组件库 | Element UI | 2.15 |
| 构建工具 | Maven / npm | - |
| 服务器 | Apache Tomcat | 9.0 |

---

## 二、系统存在的意义

### 2.1 解决用户痛点

本系统针对以下核心问题提供解决方案：

| 痛点 | 本系统解决方案 |
|------|---------------|
| 学习计划制定困难 | 提供智能生成功能，根据多日计划自动分解每日任务 |
| 专注力不足 | 番茄钟计时器，25分钟专注+5分钟休息的工作节奏 |
| 计划执行不到位 | 进度条实时跟踪，计划自动标记完成 |
| 学习效果不可知 | 统计数据面板，本周/本月/总计数据一目了然 |

### 2.2 核心价值

1. **个性化**：根据用户的学习目标、可用时间、学科偏好定制学习计划
2. **科学化**：采用番茄工作法等经过验证的学习方法
3. **数据化**：量化学习过程，让进步看得见
4. **便捷化**：一键智能生成，自动化任务分配

---

## 三、内置功能

### 3.1 核心功能模块

#### 3.1.1 学习计划管理

- **多日计划（总体计划）**
  - 创建长期学习目标
  - 设置计划名称、学科、难度、预估时长
  - 设置开始日期和截止日期
  - 设置优先级和标签

- **今日计划**
  - 从多日计划智能生成每日任务
  - 手动创建单日学习计划
  - 计划状态管理（进行中/已完成/已取消）
  - 进度自动计算和展示

#### 3.1.2 番茄钟计时器

- **专注计时**
  - 自定义专注时长（默认25分钟）
  - 自定义短休息时长（默认5分钟）
  - 自定义长休息时长（默认15分钟）
  - 长休息间隔设置

- **任务关联**
  - 番茄钟关联学习计划
  - 自动记录学习时长

- **数据记录**
  - 自动保存番茄钟记录
  - 记录开始时间、结束时长、状态

#### 3.1.3 数据统计

- **顶部统计卡片**
  - 今日番茄钟数量
  - 本周番茄钟数量
  - 本月番茄钟数量
  - 总计番茄钟数量

- **首页统计面板**
  - 学习计划总数
  - 今日学习时长（小时）
  - 今日完成任务数

- **历史记录**
  - 今日番茄钟列表
  - 专注时间统计
  - 中断次数统计

### 3.2 辅助功能

- **用户管理**
  - 用户注册
  - 个人信息修改
  - 头像上传

- **权限控制**
  - 基于角色的访问控制
  - 用户只能访问自己的数据

---

## 四、功能需求

### 4.1 核心功能需求

| 功能模块 | 功能名称 | 功能描述 | 优先级 |
|---------|---------|---------|--------|
| 学习计划 | 创建多日计划 | 用户可以创建包含目标、学科、难度的长期学习计划 | P0 |
| 学习计划 | 智能生成今日计划 | 根据多日计划自动生成今日任务，按优先级和截止日期排序 | P0 |
| 学习计划 | 计划进度跟踪 | 实时显示计划完成进度，已完成自动标记100% | P0 |
| 学习计划 | 计划列表筛选 | 支持按状态（进行中/已完成）、类型（多日/今日）筛选 | P1 |
| 番茄钟 | 开始/暂停/恢复 | 控制番茄钟计时器运行状态 | P0 |
| 番茄钟 | 番茄钟完成 | 计时结束后自动记录，切换到休息模式 | P0 |
| 番茄钟 | 番茄钟记录 | 保存每次番茄钟的开始时间、时长、关联计划 | P0 |
| 番茄钟 | 番茄钟统计 | 统计今日/本周/本月/总计番茄钟数量 | P0 |
| 数据统计 | 首页统计 | 显示学习计划数、今日学习时长、完成任务数 | P1 |
| 数据统计 | 今日记录 | 显示当日番茄钟列表、专注时间、中断次数 | P1 |

### 4.2 辅助功能需求

| 功能模块 | 功能名称 | 功能描述 | 优先级 |
|---------|---------|---------|--------|
| 用户管理 | 用户注册 | 新用户注册账号 | P1 |
| 用户管理 | 用户登录 | 已注册用户登录系统 | P1 |
| 用户管理 | 个人信息 | 查看和修改个人信息 | P2 |
| 学习计划 | 计划编辑 | 修改已创建的学习计划 | P2 |
| 学习计划 | 计划删除 | 删除不需要的学习计划 | P2 |
| 学习计划 | 手动完成 | 手动标记计划为已完成 | P2 |

---

## 五、非功能需求

### 5.1 性能需求

- **响应时间**：页面加载时间不超过3秒
- **接口响应**：API接口响应时间不超过500ms（正常负载下）
- **并发支持**：支持100个用户同时在线使用

### 5.2 易用性需求

- **界面简洁**：采用简约设计，关键功能一目了然
- **操作便捷**：主要操作不超过3步即可完成
- **反馈及时**：所有操作都有明确的成功/失败提示
- **容错性好**：错误操作有友好的错误提示，不影响正常使用

### 5.3 可拓展性需求

- **模块化设计**：各功能模块松耦合，便于功能扩展
- **接口规范**：RESTful API设计，便于第三方集成
- **配置分离**：数据库连接等配置外部化，便于部署

### 5.4 安全性需求

- **身份认证**：基于JWT的身份验证机制
- **权限控制**：基于Spring Security的细粒度权限控制
- **数据隔离**：用户只能访问自己的数据，防止越权访问

---

## 六、概要设计

### 6.1 整体架构

本系统采用经典的**三层架构**设计：

```
┌─────────────────────────────────────────────────────────────┐
│                      前端展示层 (Vue.js)                     │
├─────────────────────────────────────────────────────────────┤
│  首页 │ 学习计划 │ 番茄钟 │ 学习统计 │ 个人中心 │ 用户管理   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼ HTTP + JSON
┌─────────────────────────────────────────────────────────────┐
│                      后端服务层 (Spring Boot)                │
├─────────────────────────────────────────────────────────────┤
│  Controller层 │ Service层 │ Mapper层 │ Security            │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        数据存储层 (MySQL)                    │
├─────────────────────────────────────────────────────────────┤
│  用户表 │ 学习计划表 │ 番茄钟记录表 │ 学习统计表              │
└─────────────────────────────────────────────────────────────┘
```

### 6.2 模块划分

| 模块名称 | 职责描述 | 主要类 |
|---------|---------|-------|
| 用户模块 | 用户注册、登录、个人信息管理 | SysUser, SysLoginController |
| 学习计划模块 | 计划的CRUD、智能生成、进度管理 | StudyPlan, StudyPlanController |
| 番茄钟模块 | 计时控制、记录管理、统计 | TomatoRecord, TomatoRecordController |
| 统计模块 | 数据汇总、统计分析 | StudyStatistics, StudyStatisticsController |

### 6.3 数据流程

#### 6.3.1 番茄钟执行流程

```
用户点击"开始专注"
       │
       ▼
创建番茄钟记录 (status=0, 进行中)
       │
       ▼
启动计时器 (25分钟倒计时)
       │
       ├── 用户暂停 ──► 暂停计时，状态保持
       │
       └── 计时结束 ──► 
                  │
                  ▼
           调用完成接口 (status=1)
                  │
                  ▼
           刷新统计数据
                  │
                  ▼
           自动切换到休息模式
```

#### 6.3.2 智能生成今日计划流程

```
用户点击"智能生成今日计划"
       │
       ▼
查询所有未完成的多日计划(plan_type='overall', is_completed=0)
       │
       ▼
按优先级↓、难度↓、截止日期↑排序
       │
       ▼
选取Top 3的多日计划
       │
       ▼
对每个多日计划：
  ├── 检查已生成的今日计划数量
  ├── 如果未超过总天数，生成新的今日计划
  └── 设置计划日期为今天
       │
       ▼
批量保存今日计划到数据库
       │
       ▼
返回生成的今日计划列表
```

### 6.4 UML设计图

本节包含系统的用例图、时序图、架构图、数据流图、类图的文字描述版本。

#### 6.4.1 用例图

**用户角色与功能用例**

| 角色 | 用例分类 | 用例名称 | 用例描述 |
|------|---------|---------|--------|
| 用户 | 用户管理 | 用户注册 | 新用户注册账号 |
| 用户 | 用户管理 | 用户登录 | 已注册用户登录系统 |
| 用户 | 用户管理 | 修改个人信息 | 查看和修改个人信息 |
| 用户 | 用户管理 | 上传头像 | 上传个人头像图片 |
| 用户 | 学习计划 | 创建学习计划 | 创建多日计划或今日计划 |
| 用户 | 学习计划 | 编辑学习计划 | 修改已创建的学习计划 |
| 用户 | 学习计划 | 删除学习计划 | 删除不需要的学习计划 |
| 用户 | 学习计划 | 查看学习计划列表 | 查看所有学习计划 |
| 用户 | 学习计划 | 智能生成今日计划 | 根据多日计划自动生成今日任务 |
| 用户 | 学习计划 | 标记计划完成 | 手动标记计划为已完成 |
| 用户 | 学习计划 | 筛选计划 | 按状态、类型筛选计划 |
| 用户 | 番茄钟 | 开始番茄钟 | 开始专注计时 |
| 用户 | 番茄钟 | 暂停番茄钟 | 暂停当前计时 |
| 用户 | 番茄钟 | 恢复番茄钟 | 恢复暂停的计时 |
| 用户 | 番茄钟 | 完成番茄钟 | 计时结束自动完成 |
| 用户 | 番茄钟 | 放弃番茄钟 | 放弃当前计时 |
| 用户 | 番茄钟 | 查看今日记录 | 查看当日番茄钟列表 |
| 用户 | 番茄钟 | 自定义时长 | 设置专注和休息时长 |
| 用户 | 数据统计 | 查看学习统计 | 查看学习数据统计卡片 |
| 用户 | 数据统计 | 查看学习趋势 | 查看学习时长趋势图 |
| 用户 | 数据统计 | 查看完成情况 | 查看任务完成情况 |

**用例关系说明**
- **包含关系(include)**：智能生成今日计划 → 查看学习计划列表；完成番茄钟 → 查看今日记录；完成番茄钟 → 查看学习统计
- **扩展关系(extend)**：标记计划完成 → 查看学习统计

#### 6.4.2 时序图

**（1）用户登录时序**

```
用户 → 前端(Vue.js) → 后端(Spring Boot) → 数据库(MySQL) → JWT认证服务

1. 用户输入用户名密码
2. 前端发送 POST /login {username, password}
3. 后端查询数据库获取用户信息
4. 数据库返回用户数据

   分支1 - 用户存在且密码正确：
   ├── JWT生成Token
   ├── 返回成功 + Token + 用户信息
   ├── 前端存储Token到localStorage
   └── 跳转到首页

   分支2 - 用户不存在或密码错误：
   ├── 返回错误信息
   └── 前端显示错误提示
```

**（2）智能生成今日计划时序**

```
用户 → 前端 → Controller → Service → Mapper → 数据库

1. 用户点击"智能生成"按钮
2. 前端发送 POST /study/plan/generate
3. Controller调用 Service.generateDailyStudyPlan(userId)

4. Service处理流程：
   ├── 查询未完成的总体计划 {planType='overall', isCompleted=0}
   ├── 数据库返回计划列表
   ├── 按优先级↓、难度↓、截止日期↑排序
   ├── 选取前3个计划
   │
   └── 循环处理每个总体计划：
       ├── 检查并设置totalTasks=totalDays
       ├── 查询已生成的今日计划数量
       ├── 数据库返回已生成数量
       │
       └── 分支判断：
           ├── 未超过总天数：
           │   ├── 计算下一个序号 nextDay = generatedCount + 1
           │   ├── 创建今日计划 (计划名="总体计划 - 第N天")
           │   └── INSERT到数据库
           │
           └── 已生成完毕：跳过该计划

5. 返回生成的今日计划列表
6. 前端显示成功提示并刷新列表
```

**（3）番茄钟完成时序**

```
用户 → 前端 → Controller → Service → Mapper → 数据库

1. 番茄钟计时结束，前端显示完成提示
2. 前端发送 PUT /study/tomato/complete/{recordId}
3. Controller调用 Service.completeTomato(recordId)
4. Service处理：
   ├── 查询番茄钟记录
   ├── 设置status=1(已完成)
   ├── 设置endTime=当前时间
   ├── 更新数据库记录
   └── 返回成功
5. 前端刷新统计数据和今日记录
6. 前端切换到休息模式
7. 显示休息提示
```

**（4）学习统计数据加载时序**

```
用户 → 前端 → TomatoController → StatisticsController → 数据库

1. 用户进入学习统计页面
2. 前端发送 GET /study/tomato/statistics
3. 后端查询番茄钟统计（今日/本周/本月/总计）
4. 数据库返回统计数据
5. 前端发送 GET /study/statistics/summary
6. 后端执行汇总SQL查询
7. 数据库返回汇总数据
8. 前端组装展示数据
9. 显示统计卡片
```

#### 6.4.3 架构图

**（1）系统整体架构**

```
┌─────────────────────────────────────────────────────────────────────┐
│                        前端展示层 (Vue.js)                           │
├─────────────────────────────────────────────────────────────────────┤
│  Vue.js应用 │ Element UI组件库 │ Axios HTTP客户端 │ Vuex状态管理     │
│  Vue Router路由                                                      │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼ HTTP + JSON (REST API)
┌─────────────────────────────────────────────────────────────────────┐
│                      后端服务层 (Spring Boot)                        │
├─────────────────────────────────────────────────────────────────────┤
│  Controller层                                                        │
│  ├── StudyPlanController (学习计划接口)                             │
│  ├── TomatoRecordController (番茄钟接口)                            │
│  ├── StudyStatisticsController (统计接口)                           │
│  ├── SysLoginController (登录接口)                                  │
│  └── SysProfileController (个人信息接口)                            │
├─────────────────────────────────────────────────────────────────────┤
│  Service层                                                           │
│  ├── IStudyPlanService (学习计划服务)                               │
│  ├── ITomatoRecordService (番茄钟服务)                              │
│  ├── IStudyStatisticsService (统计服务)                             │
│  └── SysUserService (用户服务)                                      │
├─────────────────────────────────────────────────────────────────────┤
│  Mapper层                                                            │
│  ├── StudyPlanMapper │ TomatoRecordMapper │ StudyStatisticsMapper   │
│  └── SysUserMapper                                                  │
├─────────────────────────────────────────────────────────────────────┤
│  安全框架                                                            │
│  ├── Spring Security (安全框架)                                     │
│  ├── JWT Token (令牌认证)                                           │
│  └── 权限拦截器                                                      │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼ JDBC / MyBatis
┌─────────────────────────────────────────────────────────────────────┐
│                        数据存储层 (MySQL 8.0)                        │
├─────────────────────────────────────────────────────────────────────┤
│  sys_user (用户表) │ study_plan (学习计划表)                        │
│  tomato_record (番茄钟记录表) │ study_statistics (学习统计表)       │
└─────────────────────────────────────────────────────────────────────┘
```

**（2）前端模块架构**

```
前端应用 (ST-ui)
│
├── 视图层
│   ├── 首页
│   ├── 学习计划
│   ├── 番茄钟
│   ├── 学习统计
│   ├── 个人中心
│   └── 登录
│
├── 组件层
│   ├── 番茄钟计时器组件
│   ├── 计划卡片组件
│   ├── 统计卡片组件
│   └── 进度条组件
│
├── API层
│   ├── plan.js (学习计划API)
│   ├── tomato.js (番茄钟API)
│   ├── statistics.js (统计API)
│   └── user.js (用户API)
│
├── 工具层
│   ├── request.js (Axios封装)
│   ├── auth.js (认证工具)
│   └── validate.js (验证工具)
│
└── 状态管理
    ├── user模块 (用户状态)
    ├── app模块 (应用状态)
    └── permission模块 (权限状态)
```

**（3）后端分层架构**

```
后端应用 (ST-admin)
│
├── 表现层
│   ├── REST接口
│   │   ├── /study/plan (学习计划接口)
│   │   ├── /study/tomato (番茄钟接口)
│   │   ├── /study/statistics (统计接口)
│   │   └── /system/user (用户接口)
│   │
│   └── 安全过滤
│       ├── JWT认证过滤器
│       └── 权限拦截器
│
├── 业务层
│   ├── IStudyPlanService
│   ├── ITomatoRecordService
│   ├── IStudyStatisticsService
│   └── ISysUserService
│
├── 数据访问层
│   ├── StudyPlanMapper
│   ├── TomatoRecordMapper
│   ├── StudyStatisticsMapper
│   └── SysUserMapper
│
└── 领域模型
    ├── StudyPlan
    ├── TomatoRecord
    ├── StudyStatistics
    └── SysUser
```

#### 6.4.4 数据流图

**（1）顶层数据流图 (Level 0 DFD)**

```
┌─────────────┐                    ┌─────────────────────────────┐                    ┌─────────────┐
│             │  用户信息          │                             │  学习计划数据      │             │
│    用户     │ ─────────────────► │    个性化学习计划管理系统    │ ◄───────────────► │   数据库    │
│             │  登录请求          │                             │  番茄钟记录        │             │
│             │  计划数据          │                             │  统计数据          │             │
│             │  番茄钟操作        │                             │  用户信息          │             │
└─────────────┘ ◄─────────────── └─────────────────────────────┘ ─────────────────► └─────────────┘
                  认证结果                                                              学习计划数据
                  计划列表                                                              番茄钟记录
                  统计数据                                                              统计数据
                  操作反馈                                                              用户信息
```

**（2）一层数据流图 (Level 1 DFD)**

```
用户
 │
 ├────► 1.0 用户认证 ──────────────────────────────────────► D1 用户信息
 │            │
 │            ├── 登录请求(用户名,密码)
 │            └── 返回认证结果(Token)
 │
 ├────► 2.0 学习计划管理 ─────────────────────────────────► D2 学习计划
 │            │
 │            ├── 计划操作(创建/编辑/删除)
 │            ├── 计划数据读写
 │            └── 返回计划列表
 │
 ├────► 3.0 番茄钟计时 ───────────────────────────────────► D3 番茄钟记录
 │            │
 │            ├── 番茄钟操作(开始/暂停/完成)
 │            ├── 记录数据读写
 │            ├── 更新D4统计数据
 │            └── 返回计时状态
 │
 └────► 4.0 数据统计 ─────────────────────────────────────► D4 统计数据
              │
              ├── 查询D2计划
              ├── 查询D3记录
              ├── 查询D4统计
              └── 返回统计报表
```

**（3）二层数据流图 - 学习计划管理 (Level 2 DFD)**

```
用户
 │
 ├────► 2.1 创建计划
 │            │
 │            ├── 接收计划信息(名称,学科,日期等)
 │            ├── 存储到D2学习计划
 │            └── 返回创建结果
 │
 ├────► 2.2 编辑计划
 │            │
 │            ├── 接收修改信息(计划ID,更新数据)
 │            ├── 更新D2学习计划
 │            └── 返回修改结果
 │
 ├────► 2.3 删除计划
 │            │
 │            ├── 接收计划ID
 │            ├── 从D2删除计划
 │            └── 返回删除结果
 │
 ├────► 2.4 智能生成
 │            │
 │            ├── 接收生成请求
 │            ├── 查询D2总体计划
 │            ├── 排序筛选处理
 │            ├── 存储今日计划到D2
 │            └── 返回生成结果
 │
 ├────► 2.5 完成计划
 │            │
 │            ├── 接收计划ID
 │            ├── 更新D2状态为已完成
 │            └── 返回完成结果
 │
 └────► 2.6 查询计划
              │
              ├── 接收查询条件(类型,状态)
              ├── 查询D2学习计划
              └── 返回计划列表
```

**（4）二层数据流图 - 番茄钟计时 (Level 2 DFD)**

```
用户
 │
 ├────► 3.1 开始番茄钟
 │            │
 │            ├── 接收开始请求(计划ID,时长)
 │            ├── 关联D2学习计划
 │            ├── 创建记录到D3(status=0)
 │            └── 返回计时开始
 │
 ├────► 3.2 暂停/恢复
 │            │
 │            ├── 接收暂停/恢复请求
 │            ├── 更新D3记录状态
 │            └── 返回状态更新
 │
 ├────► 3.3 完成番茄钟
 │            │
 │            ├── 接收完成通知
 │            ├── 更新D3记录(status=1,endTime)
 │            ├── 更新D4今日统计
 │            └── 返回完成提示+休息建议
 │
 ├────► 3.4 放弃番茄钟
 │            │
 │            ├── 接收放弃请求
 │            ├── 更新D3记录(status=4)
 │            └── 返回放弃确认
 │
 └────► 3.5 查询记录
              │
              ├── 接收查询请求(日期范围)
              ├── 查询D3记录
              └── 返回记录列表
```

#### 6.4.5 类图

**（1）实体类**

```
┌─────────────────────────────────────────────────────────────────────┐
│                          StudyPlan (学习计划)                        │
├─────────────────────────────────────────────────────────────────────┤
│ - planId: Long              # 计划ID（主键）                        │
│ - userId: Long              # 用户ID（外键）                        │
│ - planName: String          # 计划名称                              │
│ - planType: String          # 计划类型(overall多日/today今日)        │
│ - parentPlanId: Long        # 父计划ID（关联多日计划）               │
│ - subject: String           # 学科                                  │
│ - difficulty: Integer       # 难度(1-5)                             │
│ - priority: Integer         # 优先级(1-5)                           │
│ - startDate: Date           # 开始日期                              │
│ - endDate: Date             # 结束日期                              │
│ - deadline: Date            # 截止时间                              │
│ - progress: Integer         # 进度百分比(0-100)                     │
│ - status: String            # 状态(0进行中/1已完成/2已取消)          │
│ - totalTasks: Integer       # 总任务数                              │
│ - completedTasks: Integer   # 已完成任务数                          │
│ - isCompleted: Integer      # 是否完成(0未完成/1已完成)              │
│ - isTemplate: Integer       # 是否模板(0否/1是)                     │
├─────────────────────────────────────────────────────────────────────┤
│ + getPlanId(): Long                                                   │
│ + setPlanId(Long): void                                              │
│ + getProgress(): Integer                                             │
│ + setProgress(Integer): void                                         │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                       TomatoRecord (番茄钟记录)                      │
├─────────────────────────────────────────────────────────────────────┤
│ - recordId: Long            # 记录ID（主键）                         │
│ - userId: Long              # 用户ID（外键）                         │
│ - planId: Long              # 关联计划ID                             │
│ - tomatoDuration: Integer   # 番茄钟时长（分钟）                     │
│ - restDuration: Integer     # 休息时长（分钟）                       │
│ - startTime: Date           # 开始时间                               │
│ - endTime: Date             # 结束时间                               │
│ - status: String            # 状态(0进行中/1已完成/2已中断/3已暂停/4已放弃) │
├─────────────────────────────────────────────────────────────────────┤
│ + getRecordId(): Long                                                 │
│ + setRecordId(Long): void                                            │
│ + complete(): void                                                   │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                      StudyStatistics (学习统计)                      │
├─────────────────────────────────────────────────────────────────────┤
│ - statId: Long              # 统计ID（主键）                         │
│ - userId: Long              # 用户ID（外键）                         │
│ - studyDate: Date           # 学习日期                               │
│ - planTimeSpent: Integer    # 计划学习时长(分钟)                     │
│ - tomatoTimeSpent: Integer  # 番茄钟学习时长(分钟)                   │
│ - totalTimeSpent: Integer   # 总学习时长(分钟)                       │
│ - completedPlans: Integer   # 完成计划数                             │
├─────────────────────────────────────────────────────────────────────┤
│ + getStatId(): Long                                                   │
│ + setStatId(Long): void                                              │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                           SysUser (用户)                             │
├─────────────────────────────────────────────────────────────────────┤
│ - userId: Long              # 用户ID（主键）                         │
│ - userName: String          # 用户名                                 │
│ - nickName: String          # 昵称                                   │
│ - email: String             # 邮箱                                   │
│ - phonenumber: String       # 手机号                                 │
│ - sex: String               # 性别                                   │
│ - avatar: String            # 头像                                   │
│ - password: String          # 密码                                   │
│ - status: String            # 状态                                   │
├─────────────────────────────────────────────────────────────────────┤
│ + getUserId(): Long                                                   │
│ + setUserId(Long): void                                              │
└─────────────────────────────────────────────────────────────────────┘
```

**（2）Service接口**

```
┌─────────────────────────────────────────────────────────────────────┐
│                    <<interface>> IStudyPlanService                   │
├─────────────────────────────────────────────────────────────────────┤
│ + selectStudyPlanByPlanId(Long): StudyPlan                           │
│ + selectStudyPlanList(StudyPlan): List<StudyPlan>                    │
│ + insertStudyPlan(StudyPlan): int                                    │
│ + updateStudyPlan(StudyPlan): int                                    │
│ + deleteStudyPlanByPlanIds(Long[]): int                              │
│ + generateDailyStudyPlan(Long): List<StudyPlan>                      │
│ + completeStudyPlan(Long, Double): int                               │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                   <<interface>> ITomatoRecordService                 │
├─────────────────────────────────────────────────────────────────────┤
│ + selectTomatoRecordByRecordId(Long): TomatoRecord                   │
│ + selectTomatoRecordList(TomatoRecord): List<TomatoRecord>           │
│ + insertTomatoRecord(TomatoRecord): int                              │
│ + updateTomatoRecord(TomatoRecord): int                              │
│ + startTomato(Long, Long, Integer, Integer): TomatoRecord            │
│ + completeTomato(Long): int                                          │
│ + countTodayCompletedTomatoByUserId(Long): int                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                  <<interface>> IStudyStatisticsService               │
├─────────────────────────────────────────────────────────────────────┤
│ + selectStudyStatisticsByStatId(Long): StudyStatistics               │
│ + selectStudySummaryByUserId(Long): Map<String, Object>              │
│ + updateDailyStudyStatistics(Long, Integer, Integer, Integer): int   │
└─────────────────────────────────────────────────────────────────────┘
```

**（3）类关系**

```
SysUser "1" ──────► "*" StudyPlan      : 拥有
SysUser "1" ──────► "*" TomatoRecord   : 创建
SysUser "1" ──────► "*" StudyStatistics : 统计
StudyPlan "1" ────► "*" TomatoRecord   : 关联
StudyPlan "1" ────► "*" StudyPlan      : 父子关系

IStudyPlanService ──────► StudyPlan      : 使用
ITomatoRecordService ───► TomatoRecord   : 使用
IStudyStatisticsService ► StudyStatistics: 使用
```

#### 6.4.6 ER图

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                    数据库实体关系图                                      │
└─────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐
│     sys_user        │
├─────────────────────┤
│ *user_id : BIGINT   │ ← 主键
│ user_name : VARCHAR │
│ nick_name : VARCHAR │
│ email : VARCHAR     │
│ phonenumber : VARCHAR│
│ sex : CHAR          │
│ avatar : VARCHAR    │
│ password : VARCHAR  │
│ status : CHAR       │
│ create_time : DATETIME│
└─────────┬───────────┘
          │
          │ 1
          │
          ├──────────────────────────────────────────────────────────────┐
          │                                                              │
          │ *                                                            │ *
          ▼                                                              ▼
┌─────────────────────┐                                      ┌─────────────────────┐
│    study_plan       │                                      │   tomato_record     │
├─────────────────────┤                                      ├─────────────────────┤
│ *plan_id : BIGINT   │ ← 主键                               │ *record_id : BIGINT │ ← 主键
│ user_id : BIGINT    │ ← 外键 → sys_user.user_id            │ user_id : BIGINT    │ ← 外键 → sys_user.user_id
│ plan_name : VARCHAR │                                      │ plan_id : BIGINT    │ ← 外键 → study_plan.plan_id
│ plan_type : VARCHAR │                                      │ tomato_duration : INT│
│ parent_plan_id : BIGINT│ ← 外键 → study_plan.plan_id       │ rest_duration : INT │
│ subject : VARCHAR   │                                      │ start_time : DATETIME│
│ difficulty : INT    │                                      │ end_time : DATETIME │
│ priority : INT      │                                      │ status : CHAR       │
│ start_date : DATE   │                                      │ create_time : DATETIME│
│ end_date : DATE     │                                      └─────────────────────┘
│ deadline : DATETIME │
│ estimated_hours : DECIMAL│
│ actual_hours : DECIMAL│
│ progress : INT      │
│ status : CHAR       │
│ total_tasks : INT   │
│ completed_tasks : INT│
│ is_completed : INT  │
│ is_template : INT   │
│ create_time : DATETIME│
│ update_time : DATETIME│
└─────────────────────┘
          │
          │ 自关联（父子计划）
          │
          ▼
    study_plan (自身)


┌─────────────────────┐
│  study_statistics   │
├─────────────────────┤
│ *stat_id : BIGINT   │ ← 主键
│ user_id : BIGINT    │ ← 外键 → sys_user.user_id
│ study_date : DATE   │
│ total_study_time : INT│
│ tomato_count : INT  │
│ completed_plans : INT│
│ subject_distribution : TEXT│
│ create_time : DATETIME│
│ update_time : DATETIME│
└─────────────────────┘
          ▲
          │ *
          │
          │ 1
          │
┌─────────────────────┐
│     sys_user        │
└─────────────────────┘

关系说明：
- sys_user 1:N study_plan      一个用户可以有多个学习计划
- sys_user 1:N tomato_record   一个用户可以有多个番茄钟记录
- sys_user 1:N study_statistics 一个用户可以有多条统计记录
- study_plan 1:N tomato_record 一个计划可以关联多个番茄钟
- study_plan 1:N study_plan    一个总体计划可以有多个子计划（自关联）
```

---

## 七、详细设计

### 7.1 数据库设计

#### 7.1.1 核心数据表

##### 用户表 (sys_user)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| user_id | BIGINT | 用户ID（主键） |
| user_name | VARCHAR(50) | 用户名 |
| nick_name | VARCHAR(30) | 昵称 |
| email | VARCHAR(50) | 邮箱 |
| phone | VARCHAR(11) | 手机号 |
| sex | CHAR(1) | 性别 |
| avatar | VARCHAR(100) | 头像 |
| password | VARCHAR(100) | 密码 |
| status | CHAR(1) | 状态（0正常 1停用） |
| create_time | DATETIME | 创建时间 |

##### 学习计划表 (study_plan)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| plan_id | BIGINT | 计划ID（主键） |
| user_id | BIGINT | 用户ID（外键） |
| plan_name | VARCHAR(100) | 计划名称 |
| plan_type | VARCHAR(20) | 计划类型（overall多日/today今日） |
| parent_plan_id | BIGINT | 父计划ID（关联多日计划） |
| subject | VARCHAR(50) | 学科 |
| difficulty | INT | 难度（1-5） |
| priority | INT | 优先级（1-5） |
| start_date | DATE | 开始日期 |
| end_date | DATE | 结束日期 |
| deadline | DATETIME | 截止时间 |
| progress | INT | 进度百分比（0-100） |
| status | CHAR(1) | 状态（0进行中/1已完成/2已取消） |
| total_tasks | INT | 总任务数 |
| completed_tasks | INT | 已完成任务数 |
| is_completed | INT | 是否完成（0未完成/1已完成） |
| is_template | INT | 是否模板（0否/1是） |
| create_time | DATETIME | 创建时间 |

##### 番茄钟记录表 (tomato_record)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| record_id | BIGINT | 记录ID（主键） |
| user_id | BIGINT | 用户ID（外键） |
| plan_id | BIGINT | 关联计划ID |
| tomato_duration | INT | 番茄钟时长（分钟） |
| rest_duration | INT | 休息时长（分钟） |
| start_time | DATETIME | 开始时间 |
| end_time | DATETIME | 结束时间 |
| status | CHAR(1) | 状态（0进行中/1已完成/2已中断） |
| create_time | DATETIME | 创建时间 |

##### 学习统计表 (study_statistics)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| stat_id | BIGINT | 统计ID（主键） |
| user_id | BIGINT | 用户ID（外键） |
| stat_date | DATE | 统计日期 |
| plan_count | INT | 计划数 |
| completed_plans | INT | 已完成计划数 |
| tomato_count | INT | 番茄钟数 |
| total_duration | INT | 总时长（分钟） |
| create_time | DATETIME | 创建时间 |

### 7.2 接口设计

#### 7.2.1 学习计划接口

| 接口路径 | 方法 | 说明 |
|---------|------|------|
| /study/plan | POST | 创建学习计划 |
| /study/plan | GET | 获取学习计划列表 |
| /study/plan/{planId} | GET | 获取计划详情 |
| /study/plan | PUT | 更新学习计划 |
| /study/plan/{planId} | DELETE | 删除学习计划 |
| /study/plan/generate | POST | 智能生成今日计划 |
| /study/plan/summary | GET | 获取计划统计摘要 |

#### 7.2.2 番茄钟接口

| 接口路径 | 方法 | 说明 |
|---------|------|------|
| /study/tomato/start | POST | 开始番茄钟 |
| /study/tomato/pause/{recordId} | PUT | 暂停番茄钟 |
| /study/tomato/resume/{recordId} | PUT | 恢复番茄钟 |
| /study/tomato/complete/{recordId} | PUT | 完成番茄钟 |
| /study/tomato/today | GET | 获取今日记录 |
| /study/tomato/statistics | GET | 获取统计数据 |
| /study/tomato/list | GET | 获取番茄钟记录列表 |

#### 7.2.3 统计接口

| 接口路径 | 方法 | 说明 |
|---------|------|------|
| /study/statistics/summary | GET | 获取首页统计摘要 |
| /study/statistics/trend | GET | 获取学习趋势数据 |

### 7.3 核心算法设计

#### 7.3.1 智能生成算法

```java
/**
 * 智能生成今日计划算法
 * 1. 查询所有未完成的总体计划
 * 2. 按优先级(高→低)、难度(高→低)、截止日期(近→远)排序
 * 3. 选取前3个计划
 * 4. 对每个计划，检查已生成的今日计划数量
 * 5. 如果未超过总天数，生成新的今日计划
 * 6. 计划日期设为今天
 */
public List<StudyPlan> generateDailyStudyPlan(Long userId) {
    // 1. 查询未完成的总体计划
    List<StudyPlan> overallPlans = selectOverallPlans(userId);
    
    // 2. 排序
    List<StudyPlan> sorted = overallPlans.stream()
        .sorted((p1, p2) -> {
            // 优先级高优先
            int r = p2.getPriority().compareTo(p1.getPriority());
            if (r != 0) return r;
            // 难度高优先
            r = p2.getDifficulty().compareTo(p1.getDifficulty());
            if (r != 0) return r;
            // 截止日期近优先
            return p1.getDeadline().compareTo(p2.getDeadline());
        })
        .collect(Collectors.toList());
    
    // 3-6. 生成今日计划
    List<StudyPlan> todayPlans = new ArrayList<>();
    for (int i = 0; i < Math.min(3, sorted.size()); i++) {
        // 检查已生成数量，生成剩余计划...
    }
    return todayPlans;
}
```

#### 7.3.2 进度计算算法

```java
/**
 * 学习计划进度计算
 * progress = (completedTasks / totalTasks) * 100%
 * 当status=1(已完成)时，强制设置progress=100%
 */
public void updateStudyPlan(StudyPlan plan) {
    if ("1".equals(plan.getStatus())) {
        plan.setProgress(100);
        plan.setIsCompleted(1);
        plan.setCompletedTime(new Date());
    }
}
```

---

## 八、开发环境

### 8.1 硬件要求

| 项目 | 最低配置 | 推荐配置 |
|------|---------|---------|
| CPU | 2核心 | 4核心及以上 |
| 内存 | 4GB | 8GB及以上 |
| 硬盘 | 20GB | 50GB SSD |
| 网络 | 宽带上网 | 宽带上网 |

### 8.2 软件要求

| 软件 | 版本要求 | 说明 |
|------|---------|------|
| 操作系统 | Windows 10+ / macOS / Linux | 推荐 Windows 10 |
| JDK | 1.8+ | 推荐 JDK 8 |
| Node.js | 14+ | 前端构建 |
| npm | 6+ | 前端包管理 |
| MySQL | 8.0+ | 数据库 |
| Maven | 3.6+ | 后端构建 |
| IDE | IntelliJ IDEA / VS Code | 开发工具 |

### 8.3 端口配置

| 服务 | 端口 | 说明 |
|------|------|------|
| 后端服务 | 8085 | Spring Boot 应用 |
| 前端服务 | 1117 | Vue 开发服务器 |
| MySQL | 3306 | 数据库服务 |

---

## 九、核心功能的代码实现过程

### 9.1 项目初始化

#### 9.1.1 后端初始化

```bash
# 克隆若依框架基础项目
git clone https://gitee.com/y_project/RuoYi-Vue.git

# 调整项目结构，移除不需要的模块（job、generator等）
# 保留核心模块：ruoyi-admin, ruoyi-common, ruoyi-framework, ruoyi-system
```

#### 9.1.2 前端初始化

```bash
# 安装依赖
cd ruoyi-ui
npm install

# 启动开发服务器
npm run dev
```

### 9.2 核心模块实现

#### 9.2.1 学习计划模块

**Domain实体类**
```java
// StudyPlan.java - 学习计划实体
public class StudyPlan extends BaseEntity {
    private Long planId;
    private Long userId;
    private String planName;
    private String planType;  // overall:多日计划, today:今日计划
    private Long parentPlanId;
    private String subject;
    private Integer difficulty;
    private Integer priority;
    private Date startDate;
    private Date endDate;
    private Date deadline;
    private Integer progress;
    private String status;  // 0:进行中, 1:已完成, 2:已取消
    private Integer isCompleted;
    // ... 其他字段
}
```

**Service业务逻辑**
```java
// StudyPlanServiceImpl.java
@Service
public class StudyPlanServiceImpl implements IStudyPlanService {
    
    // 智能生成今日计划
    @Override
    public List<StudyPlan> generateDailyStudyPlan(Long userId) {
        // 1. 查询未完成的总体计划
        // 2. 按优先级、难度、截止日期排序
        // 3. 选取Top 3
        // 4. 生成今日计划
        // 5. 批量插入数据库
    }
    
    // 更新计划时自动设置进度
    @Override
    public int updateStudyPlan(StudyPlan studyPlan) {
        if ("1".equals(studyPlan.getStatus())) {
            studyPlan.setProgress(100);
            studyPlan.setIsCompleted(1);
        }
        return studyPlanMapper.updateStudyPlan(studyPlan);
    }
}
```

#### 9.2.2 番茄钟模块

**前端番茄钟组件**
```javascript
// tomato/index.vue - 核心计时逻辑
export default {
    data() {
        return {
            timeLeft: 25 * 60,  // 剩余秒数
            timerMode: 'focus',   // focus:专注, break:休息
            isRunning: false,
            cycleCount: 0
        }
    },
    methods: {
        startTimer() {
            this.isRunning = true;
            this.timer = setInterval(() => {
                if (this.timeLeft > 0) {
                    this.timeLeft--;
                } else {
                    this.completePhase();  // 计时结束
                }
            }, 1000);
        },
        completePhase() {
            this.clearTimer();
            if (this.timerMode === 'focus') {
                // 完成专注，调用后端保存记录
                completeTomato(this.currentRecord.recordId).then(() => {
                    this.loadStatistics();  // 刷新统计
                    this.loadTodayRecords(); // 刷新今日记录
                });
                // 切换到休息模式
                this.timerMode = 'break';
                this.cycleCount++;
            }
        }
    }
}
```

#### 9.2.3 统计模块

**SQL统计查询**
```xml
<!-- TomatoRecordMapper.xml - 今日统计 -->
<select id="countTodayCompletedTomatoByUserId" resultType="int">
    SELECT COUNT(*) FROM tomato_record 
    WHERE user_id = #{userId} 
    AND status = 1 
    AND start_time >= CURDATE()
    AND start_time < DATE_ADD(CURDATE(), INTERVAL 1 DAY)
</select>

<!-- StudyStatisticsMapper.xml - 首页统计 -->
<select id="selectStudySummaryByUserId" resultType="HashMap">
    SELECT 
        (SELECT COUNT(*) FROM study_plan WHERE user_id = #{userId} AND is_template = 0) as plan_count,
        (SELECT COALESCE(SUM(tomato_duration), 0) FROM tomato_record 
          WHERE user_id = #{userId} AND status = 1 
          AND start_time >= CURDATE() AND start_time < DATE_ADD(CURDATE(), INTERVAL 1 DAY)) as study_hours,
        (SELECT COUNT(*) FROM tomato_record 
          WHERE user_id = #{userId} AND status = 1 
          AND start_time >= CURDATE() AND start_time < DATE_ADD(CURDATE(), INTERVAL 1 DAY)) as completed_tasks
</select>
```

---

## 十、系统不足与改进方向

### 10.1 当前系统存在的不足

#### 10.1.1 功能层面

1. **智能生成算法简单**
   - 当前仅按优先级和截止日期排序
   - 未考虑用户历史学习数据
   - 未实现真正的AI推荐

2. **缺乏数据可视化**
   - 统计页面功能单一
   - 缺少图表展示（趋势图、分布图等）
   - 无学习报告生成

3. **番茄钟功能不完善**
   - 不支持自定义提示音
   - 缺乏中断原因记录
   - 无番茄钟历史回顾

4. **协作功能缺失**
   - 不支持学习小组
   - 无分享功能
   - 缺乏社交元素

#### 10.1.2 技术层面

1. **前后端通信效率**
   - 未实现WebSocket，统计数据依赖轮询
   - 可考虑改为实时推送

2. **代码质量**
   - 部分业务逻辑可进一步抽象
   - 缺少单元测试

3. **安全性**
   - JWT token有效期较长
   - 缺乏IP限制等高级安全特性

### 10.2 后续改进方向

#### 10.2.1 功能增强

| 改进方向 | 具体内容 | 优先级 |
|---------|---------|--------|
| 智能推荐 | 引入机器学习算法，根据用户历史数据推荐学习计划 | 高 |
| 数据可视化 | 使用ECharts实现学习趋势图表 | 高 |
| 番茄钟增强 | 支持自定义时长、提示音、中断原因记录 | 中 |
| 移动端适配 | 开发微信小程序/移动端H5 | 中 |
| 社交功能 | 学习小组、排行榜、分享功能 | 低 |

#### 10.2.2 技术优化

| 改进方向 | 具体内容 | 优先级 |
|---------|---------|--------|
| 实时通信 | 使用WebSocket实现数据实时推送 | 高 |
| 性能优化 | 添加Redis缓存，减少数据库查询 | 中 |
| 代码规范 | 添加单元测试，完善代码注释 | 中 |
| 安全增强 | 短期Token、IP限制、请求限流 | 中 |

#### 10.2.3 长期愿景

1. **平台化发展**：接入更多学习资源，提供综合性学习平台
2. **AI智能化**：利用大语言模型提供学习辅导和计划定制
3. **社区化运营**：打造学习者社区，互相激励、共同进步

---

## 十一、快速开始

### 11.1 环境准备

1. 安装 MySQL 8.0+，创建数据库 `ry`
2. 安装 JDK 1.8+
3. 安装 Node.js 14+

### 11.2 数据库初始化

```bash
# 导入初始化SQL
mysql -u root -p ry < sql/init_database.sql
mysql -u root -p ry < sql/zero_init.sql
```

### 11.3 后端启动

```bash
# 编译打包
cd RuoYi-Vue-master
mvn clean package -DskipTests

# 启动服务
cd ruoyi-admin/target
java -jar ruoyi-admin.jar
```

### 11.4 前端启动

```bash
# 安装依赖
cd ruoyi-ui
npm install

# 启动开发服务器
npm run dev
```

### 11.5 访问系统

- 前端地址：http://localhost:1117
- 后端API：http://localhost:8085

---

## 十二、结语

个性化学习计划管理系统是一个持续迭代的项目。我们希望通过这个系统，帮助每一位学习者建立科学的学习习惯，让学习变得更加高效、有序。

欢迎各位开发者参与贡献代码，共同完善这个系统！

---

<p align="center">
  © 2026 个性化学习计划管理系统 - 让学习更高效
</p>
