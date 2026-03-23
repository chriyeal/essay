# 个性化学习计划管理系统 - UML图

本文档包含系统的用例图、时序图、架构图、数据流图的PlantUML代码。

---

## 一、用例图 (Use Case Diagram)

```plantuml
@startuml UseCaseDiagram
left to right direction
skinparam packageStyle rectangle

actor "用户" as User
actor "管理员" as Admin

rectangle "个性化学习计划管理系统" {
    ' 用户用例
    package "用户管理" {
        usecase "用户注册" as UC_Register
        usecase "用户登录" as UC_Login
        usecase "修改个人信息" as UC_EditProfile
        usecase "上传头像" as UC_UploadAvatar
    }
    
    package "学习计划管理" {
        usecase "创建学习计划" as UC_CreatePlan
        usecase "编辑学习计划" as UC_EditPlan
        usecase "删除学习计划" as UC_DeletePlan
        usecase "查看学习计划列表" as UC_ViewPlans
        usecase "智能生成今日计划" as UC_GeneratePlan
        usecase "标记计划完成" as UC_CompletePlan
        usecase "筛选计划" as UC_FilterPlans
    }
    
    package "番茄钟功能" {
        usecase "开始番茄钟" as UC_StartTomato
        usecase "暂停番茄钟" as UC_PauseTomato
        usecase "恢复番茄钟" as UC_ResumeTomato
        usecase "完成番茄钟" as UC_CompleteTomato
        usecase "放弃番茄钟" as UC_AbandonTomato
        usecase "查看今日记录" as UC_ViewRecords
        usecase "自定义时长" as UC_CustomDuration
    }
    
    package "数据统计" {
        usecase "查看学习统计" as UC_ViewStats
        usecase "查看学习趋势" as UC_ViewTrend
        usecase "查看完成情况" as UC_ViewCompletion
    }
}

' 用户关联
User --> UC_Register
User --> UC_Login
User --> UC_EditProfile
User --> UC_UploadAvatar
User --> UC_CreatePlan
User --> UC_EditPlan
User --> UC_DeletePlan
User --> UC_ViewPlans
User --> UC_GeneratePlan
User --> UC_CompletePlan
User --> UC_FilterPlans
User --> UC_StartTomato
User --> UC_PauseTomato
User --> UC_ResumeTomato
User --> UC_CompleteTomato
User --> UC_AbandonTomato
User --> UC_ViewRecords
User --> UC_CustomDuration
User --> UC_ViewStats
User --> UC_ViewTrend
User --> UC_ViewCompletion

' 用例关系
UC_GeneratePlan ..> UC_ViewPlans : <<extend>>
UC_CompletePlan ..> UC_ViewStats : <<include>>
UC_CompleteTomato ..> UC_ViewRecords : <<include>>
UC_CompleteTomato ..> UC_ViewStats : <<include>>

@enduml
```

---

## 二、时序图 (Sequence Diagram)

### 2.1 用户登录时序图

```plantuml
@startuml LoginSequence
actor 用户 as User
participant "前端\n(Vue.js)" as Frontend
participant "后端\n(Spring Boot)" as Backend
participant "数据库\n(MySQL)" as Database
participant "JWT\n认证服务" as JWT

User -> Frontend : 输入用户名密码
Frontend -> Backend : POST /login\n{username, password}
Backend -> Database : 查询用户信息
Database --> Backend : 返回用户数据

alt 用户存在且密码正确
    Backend -> JWT : 生成Token
    JWT --> Backend : 返回Token
    Backend --> Frontend : 返回成功+Token+用户信息
    Frontend -> Frontend : 存储Token到localStorage
    Frontend --> User : 跳转到首页
else 用户不存在或密码错误
    Backend --> Frontend : 返回错误信息
    Frontend --> User : 显示错误提示
end

@enduml
```

### 2.2 智能生成今日计划时序图

```plantuml
@startuml GeneratePlanSequence
actor 用户 as User
participant "前端\n(Vue.js)" as Frontend
participant "StudyPlanController" as Controller
participant "StudyPlanService" as Service
participant "StudyPlanMapper" as Mapper
participant "数据库\n(MySQL)" as Database

User -> Frontend : 点击"智能生成"
Frontend -> Controller : POST /study/plan/generate
Controller -> Service : generateDailyStudyPlan(userId)

' 查询总体计划
Service -> Mapper : selectStudyPlanList\n{planType='overall', isCompleted=0}
Mapper -> Database : 查询未完成的总体计划
Database --> Mapper : 返回计划列表
Mapper --> Service : 返回计划列表

' 排序处理
Service -> Service : 按优先级、难度、截止日期排序
Service -> Service : 选取前3个计划

loop 对每个选中的总体计划
    ' 确保总体计划的totalTasks正确设置
    Service -> Service: 检查并设置totalTasks=totalDays
    
    ' 查询已生成的今日计划（不限制日期）
    Service -> Mapper : selectStudyPlanList\n{parentPlanId=overall.planId}
    Mapper -> Database : 查询所有关联的今日计划
    Database --> Mapper : 返回计划列表
    Mapper --> Service: 返回已生成数量
    
    alt 未超过总天数
        Service -> Service : 计算下一个序号\nnextDay = generatedCount + 1
        Service -> Service : 创建今日计划\n(计划名="总体计划 - 第N天")
        Service -> Mapper : insertStudyPlan
        Mapper -> Database : INSERT INTO study_plan
        Database --> Mapper : 返回结果
    else 已生成完毕
        Service -> Service : 跳过该总体计划
    end
end

Service --> Controller : 返回生成的今日计划列表
Controller --> Frontend : 返回成功+计划列表
Frontend --> User : 显示成功提示+刷新列表

@enduml
```

### 2.3 番茄钟完成时序图

```plantuml
@startuml TomatoCompleteSequence
actor 用户 as User
participant "前端\n(Vue.js)" as Frontend
participant "TomatoController" as Controller
participant "TomatoService" as Service
participant "TomatoMapper" as Mapper
participant "数据库\n(MySQL)" as Database

User -> Frontend : 番茄钟计时结束
Frontend -> Frontend : 显示完成提示

Frontend -> Controller : PUT /study/tomato/complete/{recordId}
Controller -> Service : completeTomatoRecord(recordId)
Service -> Mapper : selectTomatoRecordByRecordId
Mapper -> Database : 查询番茄钟记录
Database --> Mapper : 返回记录
Mapper --> Service : 返回记录

Service -> Service : 设置status=1(已完成)
Service -> Service : 设置endTime=当前时间
Service -> Mapper : updateTomatoRecord
Mapper -> Database : UPDATE tomato_record
Database --> Mapper : 返回结果
Mapper --> Service : 返回结果

Service --> Controller : 返回成功
Controller --> Frontend : 返回成功

Frontend -> Frontend : 刷新统计数据
Frontend -> Frontend : 刷新今日记录
Frontend -> Frontend : 切换到休息模式
Frontend --> User : 显示休息提示

@enduml
```

### 2.4 学习统计数据加载时序图

```plantuml
@startuml StatisticsLoadSequence
actor 用户 as User
participant "前端\n(Vue.js)" as Frontend
participant "TomatoController" as TomatoCtrl
participant "StatisticsController" as StatsCtrl
participant "StatisticsService" as Service
participant "数据库\n(MySQL)" as Database

User -> Frontend : 进入学习统计页面
Frontend -> TomatoCtrl : GET /study/tomato/statistics
TomatoCtrl -> Database : 查询番茄钟统计
Database --> TomatoCtrl : 返回统计数据
TomatoCtrl --> Frontend : 返回番茄钟统计

Frontend -> StatsCtrl : GET /study/statistics/summary
StatsCtrl -> Service : selectStudySummaryByUserId
Service -> Database : 执行汇总SQL
Database --> Service : 返回汇总数据
Service --> StatsCtrl : 返回汇总数据
StatsCtrl --> Frontend : 返回学习统计

Frontend -> Frontend : 组装展示数据
Frontend --> User : 显示统计卡片

@enduml
```

---

## 三、架构图 (Architecture Diagram)

### 3.1 系统整体架构图

```plantuml
@startuml SystemArchitecture
skinparam componentStyle rectangle

package "前端展示层" {
    [Vue.js 应用] as Vue
    [Element UI 组件库] as ElementUI
    [Axios HTTP客户端] as Axios
    [Vuex 状态管理] as Vuex
    [Vue Router 路由] as Router
    
    Vue --> ElementUI
    Vue --> Axios
    Vue --> Vuex
    Vue --> Router
}

package "后端服务层" {
    package "Controller层" {
        [StudyPlanController] as PlanCtrl
        [TomatoRecordController] as TomatoCtrl
        [StudyStatisticsController] as StatsCtrl
        [SysLoginController] as LoginCtrl
        [SysProfileController] as ProfileCtrl
    }
    
    package "Service层" {
        [IStudyPlanService] as PlanSvc
        [ITomatoRecordService] as TomatoSvc
        [IStudyStatisticsService] as StatsSvc
        [SysUserService] as UserSvc
    }
    
    package "Mapper层" {
        [StudyPlanMapper] as PlanMapper
        [TomatoRecordMapper] as TomatoMapper
        [StudyStatisticsMapper] as StatsMapper
        [SysUserMapper] as UserMapper
    }
    
    package "安全框架" {
        [Spring Security] as Security
        [JWT Token] as JWT
        [权限拦截器] as Interceptor
    }
    
    PlanCtrl --> PlanSvc
    TomatoCtrl --> TomatoSvc
    StatsCtrl --> StatsSvc
    LoginCtrl --> UserSvc
    ProfileCtrl --> UserSvc
    
    PlanSvc --> PlanMapper
    TomatoSvc --> TomatoMapper
    StatsSvc --> StatsMapper
    UserSvc --> UserMapper
    
    Security --> JWT
    Security --> Interceptor
}

package "数据存储层" {
    database "MySQL 8.0" {
        [sys_user表] as UserTable
        [study_plan表] as PlanTable
        [tomato_record表] as TomatoTable
        [study_statistics表] as StatsTable
    }
}

' 层间关系
Vue ..> Axios : HTTP/JSON
Axios ..> PlanCtrl : REST API
Axios ..> TomatoCtrl : REST API
Axios ..> StatsCtrl : REST API
Axios ..> LoginCtrl : REST API

Interceptor ..> PlanCtrl
Interceptor ..> TomatoCtrl
Interceptor ..> StatsCtrl

PlanMapper --> PlanTable
TomatoMapper --> TomatoTable
StatsMapper --> StatsTable
UserMapper --> UserTable

@enduml
```

### 3.2 前端模块架构图

```plantuml
@startuml FrontendArchitecture
skinparam componentStyle rectangle

package "前端应用 (ST-ui)" {
    package "视图层 (views)" {
        [首页 index.vue] as Home
        [学习计划 plan/index.vue] as PlanView
        [番茄钟 tomato/index.vue] as TomatoView
        [学习统计 statistics/index.vue] as StatsView
        [个人中心 profile/index.vue] as ProfileView
        [登录 login.vue] as LoginView
    }
    
    package "组件层 (components)" {
        [番茄钟计时器] as TomatoTimer
        [计划卡片] as PlanCard
        [统计卡片] as StatsCard
        [进度条] as ProgressBar
    }
    
    package "API层 (api)" {
        [plan.js] as PlanAPI
        [tomato.js] as TomatoAPI
        [statistics.js] as StatsAPI
        [user.js] as UserAPI
    }
    
    package "工具层 (utils)" {
        [request.js] as Request
        [auth.js] as Auth
        [validate.js] as Validate
    }
    
    package "状态管理 (store)" {
        [user模块] as StoreUser
        [app模块] as StoreApp
        [permission模块] as StorePerm
    }
}

' 视图使用组件
PlanView --> PlanCard
PlanView --> ProgressBar
TomatoView --> TomatoTimer
StatsView --> StatsCard

' 视图调用API
PlanView --> PlanAPI
TomatoView --> TomatoAPI
StatsView --> StatsAPI
ProfileView --> UserAPI
LoginView --> UserAPI

' API使用工具
PlanAPI --> Request
TomatoAPI --> Request
StatsAPI --> Request
UserAPI --> Request

Request --> Auth

' 状态管理
LoginView --> StoreUser
Home --> StoreUser

@enduml
```

### 3.3 后端分层架构图

```plantuml
@startuml BackendArchitecture
skinparam componentStyle rectangle

package "后端应用 (ST-admin)" {
    
    package "表现层 (Controller)" {
        component "REST接口" {
            [学习计划接口\n/study/plan] as PlanAPI
            [番茄钟接口\n/study/tomato] as TomatoAPI
            [统计接口\n/study/statistics] as StatsAPI
            [用户接口\n/system/user] as UserAPI
        }
        
        component "安全过滤" {
            [JWT认证过滤器] as JWTFilter
            [权限拦截器] as AuthInterceptor
        }
    }
    
    package "业务层 (Service)" {
        [学习计划服务\nIStudyPlanService] as PlanSvc
        [番茄钟服务\nITomatoRecordService] as TomatoSvc
        [统计服务\nIStudyStatisticsService] as StatsSvc
        [用户服务\nISysUserService] as UserSvc
    }
    
    package "数据访问层 (Mapper)" {
        [StudyPlanMapper] as PlanMapper
        [TomatoRecordMapper] as TomatoMapper
        [StudyStatisticsMapper] as StatsMapper
        [SysUserMapper] as UserMapper
    }
    
    package "领域模型 (Domain)" {
        [StudyPlan] as PlanEntity
        [TomatoRecord] as TomatoEntity
        [StudyStatistics] as StatsEntity
        [SysUser] as UserEntity
    }
}

package "数据层" {
    database "MySQL" {
        [study_plan] as PlanTable
        [tomato_record] as TomatoTable
        [study_statistics] as StatsTable
        [sys_user] as UserTable
    }
}

' 层间调用
JWTFilter --> PlanAPI
JWTFilter --> TomatoAPI
JWTFilter --> StatsAPI
JWTFilter --> UserAPI

PlanAPI --> PlanSvc
TomatoAPI --> TomatoSvc
StatsAPI --> StatsSvc
UserAPI --> UserSvc

PlanSvc --> PlanMapper
TomatoSvc --> TomatoMapper
StatsSvc --> StatsMapper
UserSvc --> UserMapper

PlanMapper --> PlanEntity
TomatoMapper --> TomatoEntity
StatsMapper --> StatsEntity
UserMapper --> UserEntity

PlanMapper --> PlanTable
TomatoMapper --> TomatoTable
StatsMapper --> StatsTable
UserMapper --> UserTable

@enduml
```

---

## 四、数据流图 (Data Flow Diagram)

### 4.1 顶层数据流图 (Level 0 DFD)

```plantuml
@startuml DFD_Level0
skinparam rectangle {
    BackgroundColor<<external>> LightBlue
    BackgroundColor<<process>> LightYellow
    BackgroundColor<<datastore>> LightGreen
}

rectangle "用户" as User <<external>>
rectangle "个性化学习计划管理系统" as System <<process>>
rectangle "数据库" as DB <<datastore>>

User -> System : 用户信息\n登录请求\n计划数据\n番茄钟操作
System -> User : 认证结果\n计划列表\n统计数据\n操作反馈

System <-> DB : 学习计划数据\n番茄钟记录\n统计数据\n用户信息

@enduml
```

### 4.2 一层数据流图 (Level 1 DFD)

```plantuml
@startuml DFD_Level1
skinparam rectangle {
    BackgroundColor<<external>> LightBlue
    BackgroundColor<<process>> LightYellow
    BackgroundColor<<datastore>> LightGreen
}

rectangle "用户" as User <<external>>

' 处理过程
rectangle "1.0 用户认证" as P1 <<process>>
rectangle "2.0 学习计划管理" as P2 <<process>>
rectangle "3.0 番茄钟计时" as P3 <<process>>
rectangle "4.0 数据统计" as P4 <<process>>

' 数据存储
rectangle "D1 用户信息" as D1 <<datastore>>
rectangle "D2 学习计划" as D2 <<datastore>>
rectangle "D3 番茄钟记录" as D3 <<datastore>>
rectangle "D4 统计数据" as D4 <<datastore>>

' 数据流
User -> P1 : 登录请求\n(用户名,密码)
P1 -> D1 : 查询用户
D1 --> P1 : 用户信息
P1 --> User : 认证结果\n(Token)

User -> P2 : 计划操作\n(创建/编辑/删除)
P2 <-> D2 : 计划数据
P2 --> User : 计划列表

User -> P3 : 番茄钟操作\n(开始/暂停/完成)
P3 <-> D3 : 记录数据
P3 -> D4 : 更新统计
P3 --> User : 计时状态

P4 -> D2 : 查询计划
P4 -> D3 : 查询记录
P4 -> D4 : 查询统计
P4 --> User : 统计报表

@enduml
```

### 4.3 二层数据流图 - 学习计划管理 (Level 2 DFD - Plan Management)

```plantuml
@startuml DFD_Level2_Plan
skinparam rectangle {
    BackgroundColor<<external>> LightBlue
    BackgroundColor<<process>> LightYellow
    BackgroundColor<<datastore>> LightGreen
}

rectangle "用户" as User <<external>>

' 子过程
rectangle "2.1 创建计划" as P21 <<process>>
rectangle "2.2 编辑计划" as P22 <<process>>
rectangle "2.3 删除计划" as P23 <<process>>
rectangle "2.4 智能生成" as P24 <<process>>
rectangle "2.5 完成计划" as P25 <<process>>
rectangle "2.6 查询计划" as P26 <<process>>

' 数据存储
rectangle "D2 学习计划" as D2 <<datastore>>

' 数据流
User -> P21 : 计划信息\n(名称,学科,日期等)
P21 -> D2 : 存储计划
P21 --> User : 创建结果

User -> P22 : 修改信息\n(计划ID,更新数据)
P22 -> D2 : 更新计划
P22 --> User : 修改结果

User -> P23 : 计划ID
P23 -> D2 : 删除计划
P23 --> User : 删除结果

User -> P24 : 生成请求
P24 -> D2 : 查询总体计划
D2 --> P24 : 总体计划列表
P24 -> P24 : 排序筛选
P24 -> D2 : 存储今日计划
P24 --> User : 生成结果

User -> P25 : 计划ID
P25 -> D2 : 更新状态\n(已完成)
P25 --> User : 完成结果

User -> P26 : 查询条件\n(类型,状态)
P26 -> D2 : 查询计划
D2 --> P26 : 计划列表
P26 --> User : 计划列表

@enduml
```

### 4.4 二层数据流图 - 番茄钟计时 (Level 2 DFD - Tomato Timer)

```plantuml
@startuml DFD_Level2_Tomato
skinparam rectangle {
    BackgroundColor<<external>> LightBlue
    BackgroundColor<<process>> LightYellow
    BackgroundColor<<datastore>> LightGreen
}

rectangle "用户" as User <<external>>

' 子过程
rectangle "3.1 开始番茄钟" as P31 <<process>>
rectangle "3.2 暂停/恢复" as P32 <<process>>
rectangle "3.3 完成番茄钟" as P33 <<process>>
rectangle "3.4 放弃番茄钟" as P34 <<process>>
rectangle "3.5 查询记录" as P35 <<process>>

' 数据存储
rectangle "D2 学习计划" as D2 <<datastore>>
rectangle "D3 番茄钟记录" as D3 <<datastore>>
rectangle "D4 统计数据" as D4 <<datastore>>

' 数据流
User -> P31 : 开始请求\n(计划ID,时长)
P31 -> D2 : 关联计划
P31 -> D3 : 创建记录\n(status=0)
P31 --> User : 计时开始

User -> P32 : 暂停/恢复请求
P32 -> D3 : 更新记录状态
P32 --> User : 状态更新

User -> P33 : 完成通知
P33 -> D3 : 更新记录\n(status=1,endTime)
P33 -> D4 : 更新今日统计
P33 --> User : 完成提示\n休息建议

User -> P34 : 放弃请求
P34 -> D3 : 更新记录\n(status=4)
P34 --> User : 放弃确认

User -> P35 : 查询请求\n(日期范围)
P35 -> D3 : 查询记录
D3 --> P35 : 记录列表
P35 --> User : 记录列表

@enduml
```

---

## 五、类图 (Class Diagram)

```plantuml
@startuml ClassDiagram
skinparam classAttributeIconSize 0

' 实体类
class StudyPlan {
    - Long planId
    - Long userId
    - String planName
    - String planType
    - Long parentPlanId
    - String subject
    - Integer difficulty
    - Integer priority
    - Date startDate
    - Date endDate
    - Date deadline
    - Integer progress
    - String status
    - Integer totalTasks
    - Integer completedTasks
    - Integer isCompleted
    - Integer isTemplate
    + getPlanId() : Long
    + setPlanId(Long) : void
    + getProgress() : Integer
    + setProgress(Integer) : void
}

class TomatoRecord {
    - Long recordId
    - Long userId
    - Long planId
    - Integer tomatoDuration
    - Integer restDuration
    - Date startTime
    - Date endTime
    - String status
    + getRecordId() : Long
    + setRecordId(Long) : void
    + complete() : void
}

class StudyStatistics {
    - Long statId
    - Long userId
    - Date studyDate
    - Integer planTimeSpent
    - Integer tomatoTimeSpent
    - Integer totalTimeSpent
    - Integer completedPlans
    + getStatId() : Long
    + setStatId(Long) : void
}

class SysUser {
    - Long userId
    - String userName
    - String nickName
    - String email
    - String phonenumber
    - String sex
    - String avatar
    - String password
    - String status
    + getUserId() : Long
    + setUserId(Long) : void
}

' Service接口
interface IStudyPlanService {
    + selectStudyPlanByPlanId(Long) : StudyPlan
    + selectStudyPlanList(StudyPlan) : List<StudyPlan>
    + insertStudyPlan(StudyPlan) : int
    + updateStudyPlan(StudyPlan) : int
    + deleteStudyPlanByPlanIds(Long[]) : int
    + generateDailyStudyPlan(Long) : List<StudyPlan>
    + completeStudyPlan(Long, Double) : int
}

interface ITomatoRecordService {
    + selectTomatoRecordByRecordId(Long) : TomatoRecord
    + selectTomatoRecordList(TomatoRecord) : List<TomatoRecord>
    + insertTomatoRecord(TomatoRecord) : int
    + updateTomatoRecord(TomatoRecord) : int
    + startTomatoRecord(TomatoRecord) : TomatoRecord
    + completeTomatoRecord(Long) : int
    + countTodayCompletedTomatoByUserId(Long) : int
}

interface IStudyStatisticsService {
    + selectStudyStatisticsByStatId(Long) : StudyStatistics
    + selectStudySummaryByUserId(Long) : Map
    + updateDailyStudyStatistics(Long, Integer, Integer, Integer) : int
}

' 关系
StudyPlan "1" --> "*" TomatoRecord : has
SysUser "1" --> "*" StudyPlan : owns
SysUser "1" --> "*" TomatoRecord : creates
SysUser "1" --> "*" StudyStatistics : has

IStudyPlanService ..> StudyPlan : uses
ITomatoRecordService ..> TomatoRecord : uses
IStudyStatisticsService ..> StudyStatistics : uses

@enduml
```

---

## 六、ER图 (Entity-Relationship Diagram)

```plantuml
@startuml ERDiagram
skinparam linetype ortho

entity "sys_user" as user {
    *user_id : BIGINT <<PK>>
    --
    user_name : VARCHAR(50)
    nick_name : VARCHAR(30)
    email : VARCHAR(50)
    phonenumber : VARCHAR(11)
    sex : CHAR(1)
    avatar : VARCHAR(100)
    password : VARCHAR(100)
    status : CHAR(1)
    create_time : DATETIME
}

entity "study_plan" as plan {
    *plan_id : BIGINT <<PK>>
    --
    user_id : BIGINT <<FK>>
    plan_name : VARCHAR(100)
    plan_type : VARCHAR(20)
    parent_plan_id : BIGINT <<FK>>
    subject : VARCHAR(50)
    difficulty : INT
    priority : INT
    start_date : DATE
    end_date : DATE
    deadline : DATETIME
    estimated_hours : DECIMAL(5,2)
    actual_hours : DECIMAL(5,2)
    progress : INT
    status : CHAR(1)
    total_tasks : INT
    completed_tasks : INT
    is_completed : INT
    is_template : INT
    create_time : DATETIME
    update_time : DATETIME
}

entity "tomato_record" as tomato {
    *record_id : BIGINT <<PK>>
    --
    user_id : BIGINT <<FK>>
    plan_id : BIGINT <<FK>>
    tomato_duration : INT
    rest_duration : INT
    start_time : DATETIME
    end_time : DATETIME
    status : CHAR(1)
    create_time : DATETIME
}

entity "study_statistics" as stats {
    *stat_id : BIGINT <<PK>>
    --
    user_id : BIGINT <<FK>>
    study_date : DATE
    total_study_time : INT
    tomato_count : INT
    completed_plans : INT
    subject_distribution : TEXT
    create_time : DATETIME
    update_time : DATETIME
}

user ||..o{ plan : "拥有"
user ||..o{ tomato : "创建"
user ||..o{ stats : "统计"
plan ||..o{ tomato : "关联"
plan ||..o{ plan : "父子关系"

@enduml
```

---

## 使用说明

### 在线渲染工具

1. **PlantUML官方服务器**: https://www.plantuml.com/plantuml/uml/
2. **VS Code插件**: 安装 "PlantUML" 插件，可直接预览
3. **IDEA插件**: 安装 "PlantUML Integration" 插件

### 本地渲染方法

1. 安装Java运行环境
2. 安装Graphviz（绘图工具）
3. 下载PlantUML jar包
4. 运行命令：
```bash
java -jar plantuml.jar diagrams.puml
```

---

<p align="center">
  © 2026 个性化学习计划管理系统 - UML设计文档
</p>
