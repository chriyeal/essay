<template>
  <div class="app-container plan-detail">
    <!-- 返回按钮 -->
    <div class="back-bar">
      <el-button icon="el-icon-arrow-left" @click="goBack">返回列表</el-button>
    </div>

    <!-- 计划概览 -->
    <el-card class="plan-overview">
      <div slot="header" class="clearfix">
        <span class="card-title">计划概览</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="16">
          <div class="plan-basic-info">
            <h2 class="plan-title">
              <i :class="getPriorityIcon(planDetail.priority)" :style="{ color: getPriorityColor(planDetail.priority) }"></i>
              {{ planDetail.title }}
              <el-tag v-if="planDetail.isTemplate" size="small" type="info">模板</el-tag>
            </h2>
            <div class="plan-meta">
              <span class="meta-item">
                <i class="el-icon-collection-tag"></i>
                {{ planDetail.subject }}
              </span>
              <span class="meta-item">
                <i class="el-icon-timer"></i>
                {{ formatDate(planDetail.startDate) }} 至 {{ formatDate(planDetail.endDate) }}
              </span>
              <span class="meta-item">
                <i class="el-icon-star-off"></i>
                难度等级 {{ planDetail.difficulty }}/3
              </span>
            </div>
            <div class="plan-description" v-if="planDetail.description">
              <h4>计划描述</h4>
              <p>{{ planDetail.description }}</p>
            </div>
            <div class="learning-goals" v-if="planDetail.learningGoals">
              <h4>学习目标</h4>
              <ul>
                <li v-for="(goal, index) in formatGoals(planDetail.learningGoals)" :key="index">
                  {{ goal }}
                </li>
              </ul>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="plan-progress-summary">
            <div class="progress-circle">
              <el-progress
                type="circle"
                :percentage="planDetail.progress"
                :status="getProgressStatus(planDetail.progress)"
                :width="120"
                :stroke-width="10"
              ></el-progress>
              <div class="progress-text">{{ planDetail.progress }}%</div>
            </div>
            <div class="progress-stats">
              <div class="stat-item">
                <span class="stat-label">总任务数</span>
                <span class="stat-value">{{ planDetail.totalTasks }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">已完成</span>
                <span class="stat-value completed">{{ planDetail.completedTasks }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">进行中</span>
                <span class="stat-value ongoing">{{ planDetail.ongoingTasks }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">待开始</span>
                <span class="stat-value pending">{{ planDetail.pendingTasks }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 任务列表 -->
    <el-card class="tasks-section">
      <div slot="header" class="clearfix">
        <span class="card-title">学习任务</span>
        <div style="float: right;">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAddTask">添加任务</el-button>
          <el-button icon="el-icon-sort" size="small" @click="sortTasks">任务排序</el-button>
        </div>
      </div>
      
      <el-table
        :data="taskList"
        row-key="taskId"
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        class="tasks-table"
      >
        <el-table-column prop="title" label="任务标题" min-width="250">
          <template slot-scope="scope">
            <div class="task-title-cell">
              <el-checkbox
                v-model="scope.row.completed"
                @change="handleTaskComplete(scope.row)"
                :disabled="scope.row.status === '2'"
              ></el-checkbox>
              <span :class="{ 'completed-task': scope.row.completed }">{{ scope.row.title }}</span>
              <el-tag v-if="scope.row.isMilestone" size="mini" type="warning">里程碑</el-tag>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="estimatedTime" label="预估时间" width="120">
          <template slot-scope="scope">
            {{ scope.row.estimatedTime ? scope.row.estimatedTime + '分钟' : '-' }}
          </template>
        </el-table-column>
        
        <el-table-column prop="actualTime" label="实际用时" width="120">
          <template slot-scope="scope">
            {{ scope.row.actualTime ? scope.row.actualTime + '分钟' : '-' }}
          </template>
        </el-table-column>
        
        <el-table-column prop="deadline" label="截止时间" width="120">
          <template slot-scope="scope">
            {{ scope.row.deadline ? formatDate(scope.row.deadline) : '-' }}
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTaskStatusType(scope.row.status)">
              {{ getTaskStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEditTask(scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDeleteTask(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 学习记录 -->
    <el-card class="records-section">
      <div slot="header" class="clearfix">
        <span class="card-title">学习记录</span>
        <div style="float: right;">
          <el-button type="success" icon="el-icon-plus" size="small" @click="handleAddRecord">添加记录</el-button>
        </div>
      </div>
      
      <el-timeline>
        <el-timeline-item
          v-for="(record, index) in recordList"
          :key="index"
          :timestamp="formatDateTime(record.createTime)"
          placement="top"
        >
          <el-card>
            <h4>{{ record.title }}</h4>
            <p>{{ record.content }}</p>
            <div class="record-meta">
              <span class="meta-item">
                <i class="el-icon-time"></i>
                学习时长: {{ record.studyDuration }}分钟
              </span>
              <span class="meta-item" v-if="record.tomatoCount">
                <i class="el-icon-timer"></i>
                番茄钟: {{ record.tomatoCount }}个
              </span>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 添加/编辑任务对话框 -->
    <el-dialog :title="taskTitle" :visible.sync="taskOpen" width="600px" append-to-body>
      <el-form ref="taskForm" :model="taskForm" :rules="taskRules" label-width="100px">
        <el-form-item label="任务标题" prop="title">
          <el-input v-model="taskForm.title" placeholder="请输入任务标题" />
        </el-form-item>
        
        <el-form-item label="任务描述">
          <el-input
            v-model="taskForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入任务描述"
          ></el-input>
        </el-form-item>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="预估时间(分钟)" prop="estimatedTime">
              <el-input-number
                v-model="taskForm.estimatedTime"
                :min="1"
                :max="1440"
                controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="截止时间" prop="deadline">
              <el-date-picker
                v-model="taskForm.deadline"
                type="datetime"
                placeholder="选择截止时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="设为里程碑">
          <el-switch
            v-model="taskForm.isMilestone"
            active-text="是"
            inactive-text="否"
          ></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTaskForm">确 定</el-button>
        <el-button @click="cancelTask">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加学习记录对话框 -->
    <el-dialog title="添加学习记录" :visible.sync="recordOpen" width="600px" append-to-body>
      <el-form ref="recordForm" :model="recordForm" :rules="recordRules" label-width="100px">
        <el-form-item label="记录标题" prop="title">
          <el-input v-model="recordForm.title" placeholder="请输入记录标题" />
        </el-form-item>
        
        <el-form-item label="学习内容" prop="content">
          <el-input
            v-model="recordForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入学习内容"
          ></el-input>
        </el-form-item>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="学习时长(分钟)" prop="studyDuration">
              <el-input-number
                v-model="recordForm.studyDuration"
                :min="1"
                :max="1440"
                controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="番茄钟数量" prop="tomatoCount">
              <el-input-number
                v-model="recordForm.tomatoCount"
                :min="0"
                :max="24"
                controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRecordForm">确 定</el-button>
        <el-button @click="cancelRecord">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStudyPlan } from "@/api/study/plan";

export default {
  name: "PlanDetail",
  data() {
    return {
      // 计划详情
      planDetail: {},
      // 任务列表
      taskList: [],
      // 学习记录列表
      recordList: [],
      // 任务对话框
      taskOpen: false,
      taskTitle: "",
      taskForm: {},
      taskRules: {
        title: [
          { required: true, message: "任务标题不能为空", trigger: "blur" }
        ]
      },
      // 记录对话框
      recordOpen: false,
      recordForm: {},
      recordRules: {
        title: [
          { required: true, message: "记录标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "学习内容不能为空", trigger: "blur" }
        ],
        studyDuration: [
          { required: true, message: "学习时长不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    const planId = this.$route.params && this.$route.params.planId;
    if (planId) {
      this.getPlanDetail(planId);
    }
  },
  methods: {
    /** 获取计划详情 */
    getPlanDetail(planId) {
      getStudyPlan(planId).then(response => {
        this.planDetail = response.data;
        // 模拟任务数据
        this.taskList = [
          {
            taskId: 1,
            title: "学习基础知识",
            description: "掌握相关基础概念和理论",
            estimatedTime: 120,
            actualTime: 90,
            deadline: "2024-12-25",
            status: "1",
            completed: true,
            isMilestone: true
          },
          {
            taskId: 2,
            title: "练习题目",
            description: "完成相关练习题巩固知识",
            estimatedTime: 90,
            actualTime: 0,
            deadline: "2024-12-28",
            status: "0",
            completed: false,
            isMilestone: false
          }
        ];
        // 模拟记录数据
        this.recordList = [
          {
            title: "今日学习总结",
            content: "今天主要学习了基础知识部分，理解了核心概念。",
            studyDuration: 120,
            tomatoCount: 2,
            createTime: "2024-12-20 15:30:00"
          }
        ];
      });
    },
    /** 返回列表 */
    goBack() {
      this.$router.push("/study/plan");
    },
    /** 添加任务 */
    handleAddTask() {
      this.taskForm = {
        taskId: null,
        title: null,
        description: null,
        estimatedTime: 60,
        deadline: null,
        isMilestone: false
      };
      this.taskOpen = true;
      this.taskTitle = "添加任务";
    },
    /** 编辑任务 */
    handleEditTask(row) {
      this.taskForm = { ...row };
      this.taskOpen = true;
      this.taskTitle = "编辑任务";
    },
    /** 删除任务 */
    handleDeleteTask(row) {
      this.$modal.confirm('是否确认删除任务"' + row.title + '"？').then(() => {
        // TODO: 调用删除API
        this.$modal.msgSuccess("删除成功");
        // 重新加载任务列表
      }).catch(() => {});
    },
    /** 任务完成状态变更 */
    handleTaskComplete(task) {
      // TODO: 调用更新任务状态API
      this.$message.success('任务状态已更新');
    },
    /** 提交任务表单 */
    submitTaskForm() {
      this.$refs["taskForm"].validate(valid => {
        if (valid) {
          // TODO: 调用添加或更新任务API
          this.$modal.msgSuccess("操作成功");
          this.taskOpen = false;
          // 重新加载任务列表
        }
      });
    },
    /** 取消任务操作 */
    cancelTask() {
      this.taskOpen = false;
      this.resetTaskForm();
    },
    /** 重置任务表单 */
    resetTaskForm() {
      this.taskForm = {
        taskId: null,
        title: null,
        description: null,
        estimatedTime: 60,
        deadline: null,
        isMilestone: false
      };
      this.resetForm("taskForm");
    },
    /** 任务排序 */
    sortTasks() {
      this.$message.info('任务排序功能待实现');
    },
    /** 添加学习记录 */
    handleAddRecord() {
      this.recordForm = {
        title: null,
        content: null,
        studyDuration: 60,
        tomatoCount: 0
      };
      this.recordOpen = true;
    },
    /** 提交记录表单 */
    submitRecordForm() {
      this.$refs["recordForm"].validate(valid => {
        if (valid) {
          // TODO: 调用添加学习记录API
          this.$modal.msgSuccess("添加成功");
          this.recordOpen = false;
          // 重新加载记录列表
        }
      });
    },
    /** 取消记录操作 */
    cancelRecord() {
      this.recordOpen = false;
      this.resetRecordForm();
    },
    /** 重置记录表单 */
    resetRecordForm() {
      this.recordForm = {
        title: null,
        content: null,
        studyDuration: 60,
        tomatoCount: 0
      };
      this.resetForm("recordForm");
    },
    // 格式化日期
    formatDate(date) {
      if (!date) return '-';
      return date.split(' ')[0];
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-';
      return dateTime;
    },
    // 获取优先级图标
    getPriorityIcon(priority) {
      const icons = {
        '0': 'el-icon-arrow-down',
        '1': 'el-icon-minus',
        '2': 'el-icon-arrow-up'
      };
      return icons[priority] || 'el-icon-minus';
    },
    // 获取优先级颜色
    getPriorityColor(priority) {
      const colors = {
        '0': '#909399',
        '1': '#409EFF',
        '2': '#F56C6C'
      };
      return colors[priority] || '#409EFF';
    },
    // 获取进度状态
    getProgressStatus(progress) {
      if (progress >= 100) return 'success';
      if (progress >= 50) return 'warning';
      return 'exception';
    },
    // 获取任务状态类型
    getTaskStatusType(status) {
      const types = {
        '0': 'info',     // 待开始
        '1': 'primary',  // 进行中
        '2': 'success'   // 已完成
      };
      return types[status] || 'info';
    },
    // 获取任务状态文本
    getTaskStatusText(status) {
      const texts = {
        '0': '待开始',
        '1': '进行中',
        '2': '已完成'
      };
      return texts[status] || '未知';
    },
    // 格式化学习目标
    formatGoals(goals) {
      if (!goals) return [];
      return goals.split(';').filter(goal => goal.trim());
    }
  }
};
</script>

<style scoped lang="scss">
.plan-detail {
  padding: 20px;
  
  .back-bar {
    margin-bottom: 20px;
  }
  
  .card-title {
    font-size: 1.2rem;
    font-weight: 500;
    color: #333;
  }
  
  .plan-overview {
    margin-bottom: 20px;
    
    .plan-basic-info {
      .plan-title {
        font-size: 1.8rem;
        font-weight: 500;
        color: #333;
        margin-bottom: 15px;
        display: flex;
        align-items: center;
        gap: 10px;
      }
      
      .plan-meta {
        display: flex;
        gap: 20px;
        margin-bottom: 20px;
        flex-wrap: wrap;
        
        .meta-item {
          display: flex;
          align-items: center;
          gap: 5px;
          color: #666;
          
          i {
            color: #409EFF;
          }
        }
      }
      
      .plan-description, .learning-goals {
        margin-bottom: 20px;
        
        h4 {
          font-size: 1.1rem;
          margin-bottom: 10px;
          color: #333;
        }
        
        p {
          color: #666;
          line-height: 1.6;
        }
        
        ul {
          padding-left: 20px;
          
          li {
            color: #666;
            line-height: 1.6;
            margin-bottom: 5px;
          }
        }
      }
    }
    
    .plan-progress-summary {
      text-align: center;
      
      .progress-circle {
        position: relative;
        margin-bottom: 20px;
        
        .progress-text {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          font-size: 1.2rem;
          font-weight: 600;
          color: #333;
        }
      }
      
      .progress-stats {
        .stat-item {
          display: flex;
          justify-content: space-between;
          padding: 8px 0;
          border-bottom: 1px solid #f0f0f0;
          
          .stat-label {
            color: #666;
          }
          
          .stat-value {
            font-weight: 500;
            
            &.completed {
              color: #67C23A;
            }
            
            &.ongoing {
              color: #409EFF;
            }
            
            &.pending {
              color: #909399;
            }
          }
        }
      }
    }
  }
  
  .tasks-section, .records-section {
    margin-bottom: 20px;
    
    .task-title-cell {
      display: flex;
      align-items: center;
      gap: 10px;
      
      .completed-task {
        text-decoration: line-through;
        color: #999;
      }
    }
    
    .record-meta {
      display: flex;
      gap: 20px;
      margin-top: 10px;
      
      .meta-item {
        display: flex;
        align-items: center;
        gap: 5px;
        color: #666;
        font-size: 0.9rem;
      }
    }
  }
  
  // 响应式设计
  @media (max-width: 768px) {
    .plan-overview .el-row {
      flex-direction: column;
    }
    
    .plan-meta {
      flex-direction: column;
      gap: 10px !important;
    }
    
    .record-meta {
      flex-direction: column;
      gap: 10px !important;
    }
  }
}
</style>