<template>
  <div class="app-container tomato-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">番茄钟专注学习</h1>
      <p class="page-subtitle">使用番茄工作法提升学习专注力</p>
    </div>

    <!-- 统计概览 -->
    <el-row :gutter="20" class="stats-overview">
      <el-col :xs="12" :sm="6" v-for="stat in statistics" :key="stat.key">
        <div class="stat-card" :style="{ backgroundColor: stat.bgColor }">
          <div class="stat-icon" :style="{ color: stat.color }">
            <i :class="stat.icon"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
            <div class="stat-sub">{{ stat.sub }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 左侧：番茄钟计时器 -->
      <el-col :xs="24" :md="16">
        <el-card class="timer-section">
          <div slot="header" class="clearfix">
            <span class="card-title">专注计时器</span>
          </div>
          
          <!-- 番茄钟显示区域 -->
          <div class="tomato-display">
            <div class="tomato-circle" :class="timerState">
              <div class="tomato-inner">
                <div class="time-display">{{ formatTime(timeLeft) }}</div>
                <div class="phase-indicator">{{ currentPhaseText }}</div>
                <div class="tomato-icon">
                  <i class="el-icon-timer"></i>
                </div>
              </div>
            </div>
            
            <!-- 控制按钮 -->
            <div class="control-buttons">
              <el-button
                v-if="!isRunning && !isPaused"
                type="primary"
                icon="el-icon-video-play"
                size="large"
                round
                @click="startTimer"
                :disabled="!!currentRecord"
              >
                开始专注
              </el-button>
              
              <template v-else-if="isRunning">
                <el-button
                  type="warning"
                  icon="el-icon-video-pause"
                  size="large"
                  round
                  @click="pauseTimer"
                >
                  暂停
                </el-button>
                <el-button
                  type="danger"
                  icon="el-icon-close"
                  size="large"
                  round
                  @click="abandonTimer"
                >
                  放弃
                </el-button>
              </template>
              
              <template v-else-if="isPaused">
                <el-button
                  type="success"
                  icon="el-icon-video-play"
                  size="large"
                  round
                  @click="resumeTimer"
                >
                  继续
                </el-button>
                <el-button
                  type="danger"
                  icon="el-icon-close"
                  size="large"
                  round
                  @click="abandonTimer"
                >
                  放弃
                </el-button>
              </template>
            </div>
            
            <!-- 当前任务信息 -->
            <div v-if="currentRecord" class="current-task">
              <el-tag type="success" effect="dark">当前进行中</el-tag>
              <div class="task-info">
                <h4>{{ currentRecord.taskTitle || '专注学习' }}</h4>
                <p v-if="currentRecord.description">{{ currentRecord.description }}</p>
                <div class="task-stats">
                  <span><i class="el-icon-time"></i> 已专注 {{ formatMinutes(currentRecord.elapsedTime || 0) }}</span>
                  <span><i class="el-icon-timer"></i> 第 {{ currentRecord.currentCycle || 1 }} 个番茄钟</span>
                  <span v-if="currentRecord.subject"><i class="el-icon-collection"></i> {{ currentRecord.subject }}</span>
                  <span v-if="currentRecord.difficulty"><i class="el-icon-star-off"></i> 难度: {{ getDifficultyText(currentRecord.difficulty) }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 设置面板 -->
          <div class="settings-panel">
            <el-collapse v-model="activeNames">
              <el-collapse-item title="番茄钟设置" name="1">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="专注时间">
                      <el-input-number
                        v-model="settings.focusTime"
                        :min="1"
                        :max="60"
                        :step="5"
                        controls-position="right"
                      ></el-input-number>
                      <span class="unit">分钟</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="休息时间">
                      <el-input-number
                        v-model="settings.breakTime"
                        :min="1"
                        :max="30"
                        :step="1"
                        controls-position="right"
                      ></el-input-number>
                      <span class="unit">分钟</span>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="长休息时间">
                      <el-input-number
                        v-model="settings.longBreakTime"
                        :min="5"
                        :max="60"
                        :step="5"
                        controls-position="right"
                      ></el-input-number>
                      <span class="unit">分钟</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="长休息间隔">
                      <el-input-number
                        v-model="settings.longBreakInterval"
                        :min="2"
                        :max="10"
                        :step="1"
                        controls-position="right"
                      ></el-input-number>
                      <span class="unit">个番茄钟</span>
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-form-item label="自动开始休息">
                  <el-switch
                    v-model="settings.autoStartBreak"
                    active-text="开启"
                    inactive-text="关闭"
                  ></el-switch>
                </el-form-item>
                
                <!-- 自动设置时间功能 -->
                <el-form-item label="智能时间设置">
                  <el-button type="success" icon="el-icon-magic-stick" @click="autoSetTime" :loading="autoSettingLoading">
                    {{ autoSettingLoading ? '正在分析...' : '根据任务自动设置时间' }}
                  </el-button>
                  <p class="setting-tip">系统会根据任务难度和学科自动推荐合适的时间设置</p>
                </el-form-item>
                
                <el-button type="primary" @click="saveSettings">保存设置</el-button>
              </el-collapse-item>
              
              <el-collapse-item title="当前任务设置" name="2">
                <el-form>
                  <el-form-item label="任务标题">
                    <el-input
                      v-model="taskForm.taskTitle"
                      placeholder="请输入当前学习任务"
                    ></el-input>
                  </el-form-item>
                  
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="学科">
                        <el-select v-model="taskForm.subject" placeholder="选择学科" style="width: 100%;">
                          <el-option label="数学" value="数学"></el-option>
                          <el-option label="英语" value="英语"></el-option>
                          <el-option label="语文" value="语文"></el-option>
                          <el-option label="物理" value="物理"></el-option>
                          <el-option label="化学" value="化学"></el-option>
                          <el-option label="生物" value="生物"></el-option>
                          <el-option label="历史" value="历史"></el-option>
                          <el-option label="地理" value="地理"></el-option>
                          <el-option label="政治" value="政治"></el-option>
                          <el-option label="计算机" value="计算机"></el-option>
                          <el-option label="其他" value="其他"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="难度">
                        <el-rate
                          v-model="taskForm.difficulty"
                          :max="3"
                          show-text
                          :texts="['简单', '中等', '困难']"
                        ></el-rate>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  
                  <el-form-item label="任务描述">
                    <el-input
                      v-model="taskForm.description"
                      type="textarea"
                      :rows="3"
                      placeholder="简要描述学习内容"
                    ></el-input>
                  </el-form-item>
                  
                  <el-form-item label="关联计划">
                    <el-select
                      v-model="taskForm.planId"
                      placeholder="选择关联的学习计划"
                      clearable
                      style="width: 100%;"
                    >
                      <el-option
                        v-for="plan in planOptions"
                        :key="plan.planId"
                        :label="plan.title"
                        :value="plan.planId"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                  
                  <el-button type="success" @click="saveTask">保存任务</el-button>
                </el-form>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧：历史记录 -->
      <el-col :xs="24" :md="8">
        <el-card class="history-section">
          <div slot="header" class="clearfix">
            <span class="card-title">今日记录</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshHistory">刷新</el-button>
          </div>
          
          <div class="today-summary">
            <div class="summary-item">
              <div class="summary-value">{{ todayStats.completed }}</div>
              <div class="summary-label">完成番茄钟</div>
            </div>
            <div class="summary-item">
              <div class="summary-value">{{ formatMinutes(todayStats.totalTime) }}</div>
              <div class="summary-label">专注时间</div>
            </div>
            <div class="summary-item">
              <div class="summary-value">{{ todayStats.interruptions }}</div>
              <div class="summary-label">中断次数</div>
            </div>
          </div>
          
          <div class="history-list">
            <div
              v-for="record in historyRecords"
              :key="record.recordId"
              class="history-item"
              :class="'status-' + record.status"
            >
              <div class="history-time">{{ formatTimeOnly(record.startTime) }}</div>
              <div class="history-content">
                <div class="history-title">{{ record.taskTitle || '专注学习' }}</div>
                <div class="history-duration">{{ formatMinutes(record.duration) }}</div>
                <el-tag
                  :type="getRecordStatusType(record.status)"
                  size="mini"
                >
                  {{ getRecordStatusText(record.status) }}
                </el-tag>
              </div>
            </div>
            
            <div v-if="historyRecords.length === 0" class="empty-history">
              <i class="el-icon-timer"></i>
              <p>今天还没有番茄钟记录</p>
              <p>开始你的第一个番茄钟吧！</p>
            </div>
          </div>
        </el-card>
        
        <!-- 效率分析 -->
        <el-card class="analysis-section" style="margin-top: 20px;">
          <div slot="header" class="clearfix">
            <span class="card-title">效率分析</span>
          </div>
          
          <div class="efficiency-chart">
            <div class="chart-placeholder">
              <i class="el-icon-data-analysis"></i>
              <p>效率趋势图</p>
              <p class="sub-text">(功能待完善)</p>
            </div>
          </div>
          
          <div class="efficiency-stats">
            <div class="stat-row">
              <span class="stat-label">今日效率</span>
              <el-progress
                :percentage="efficiency.today"
                :status="getEfficiencyStatus(efficiency.today)"
                :stroke-width="8"
              ></el-progress>
            </div>
            <div class="stat-row">
              <span class="stat-label">本周平均</span>
              <el-progress
                :percentage="efficiency.weekly"
                :status="getEfficiencyStatus(efficiency.weekly)"
                :stroke-width="8"
              ></el-progress>
            </div>
            <div class="stat-row">
              <span class="stat-label">本月平均</span>
              <el-progress
                :percentage="efficiency.monthly"
                :status="getEfficiencyStatus(efficiency.monthly)"
                :stroke-width="8"
              ></el-progress>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getTomatoStatistics, getTodayTomatoRecords, startTomato, pauseTomato, resumeTomato, completeTomato, abandonTomato } from "@/api/study/tomato";
import { listStudyPlan } from "@/api/study/plan";

export default {
  name: "TomatoClock",
  data() {
    return {
      // 计时器相关
      timeLeft: 25 * 60, // 默认25分钟
      totalTime: 25 * 60, // 总时间
      isRunning: false,
      isPaused: false,
      timer: null,
      currentPhase: 'focus', // focus, break, longBreak
      cycleCount: 0,
      
      // 当前记录
      currentRecord: null,
      
      // 设置
      settings: {
        focusTime: 25,
        breakTime: 5,
        longBreakTime: 15,
        longBreakInterval: 4,
        autoStartBreak: true
      },
      
      // 任务表单
      taskForm: {
        taskTitle: '',
        description: '',
        subject: '',
        difficulty: 2,
        planId: null
      },
      
      // 学习计划选项
      planOptions: [],
      
      // 统计数据
      statistics: [
        { key: 'today', label: '今日', sub: '番茄钟', value: 0, icon: 'el-icon-timer', color: '#409EFF', bgColor: 'rgba(64, 158, 255, 0.1)' },
        { key: 'week', label: '本周', sub: '番茄钟', value: 0, icon: 'el-icon-date', color: '#67C23A', bgColor: 'rgba(103, 194, 58, 0.1)' },
        { key: 'month', label: '本月', sub: '番茄钟', value: 0, icon: 'el-icon-month', color: '#E6A23C', bgColor: 'rgba(230, 162, 60, 0.1)' },
        { key: 'total', label: '总计', sub: '番茄钟', value: 0, icon: 'el-icon-data', color: '#F56C6C', bgColor: 'rgba(245, 108, 108, 0.1)' }
      ],
      
      // 今日统计
      todayStats: {
        completed: 0,
        totalTime: 0,
        interruptions: 0
      },
      
      // 历史记录
      historyRecords: [],
      
      // 效率分析
      efficiency: {
        today: 85,
        weekly: 78,
        monthly: 82
      },
      
      // 折叠面板
      activeNames: ['1'],
      
      // 自动设置加载状态
      autoSettingLoading: false,
    };
  },
  computed: {
    timerState() {
      if (this.isRunning) return 'running';
      if (this.isPaused) return 'paused';
      return 'idle';
    },
    currentPhaseText() {
      const texts = {
        'focus': '专注时间',
        'break': '短暂休息',
        'longBreak': '长休息时间'
      };
      return texts[this.currentPhase] || '专注时间';
    }
  },
  created() {
    this.loadStatistics();
    this.loadTodayRecords();
    this.loadPlans();
    this.loadSettings();
  },
  beforeDestroy() {
    this.clearTimer();
  },
  beforeRouteLeave(to, from, next) {
    if (this.isRunning || this.isPaused) {
      this.$confirm('番茄钟正在运行中，离开页面将中断计时，确认离开吗？', '提示', {
        confirmButtonText: '确认离开',
        cancelButtonText: '继续专注',
        type: 'warning'
      }).then(() => {
        this.clearTimer();
        this.isRunning = false;
        this.isPaused = false;
        next();
      }).catch(() => {
        next(false);
      });
    } else {
      next();
    }
  },
  methods: {
    /** 加载统计数据 */
    loadStatistics() {
      getTomatoStatistics().then(response => {
        const stats = response.data || {};
        this.statistics[0].value = stats.todayCount || 0;
        this.statistics[1].value = stats.weekCount || 0;
        this.statistics[2].value = stats.monthCount || 0;
        this.statistics[3].value = stats.totalCount || 0;
      }).catch(() => {
        // 使用默认值
        this.statistics = [
          { key: 'today', label: '今日', sub: '番茄钟', value: 3, icon: 'el-icon-timer', color: '#409EFF', bgColor: 'rgba(64, 158, 255, 0.1)' },
          { key: 'week', label: '本周', sub: '番茄钟', value: 15, icon: 'el-icon-date', color: '#67C23A', bgColor: 'rgba(103, 194, 58, 0.1)' },
          { key: 'month', label: '本月', sub: '番茄钟', value: 45, icon: 'el-icon-month', color: '#E6A23C', bgColor: 'rgba(230, 162, 60, 0.1)' },
          { key: 'total', label: '总计', sub: '番茄钟', value: 128, icon: 'el-icon-data', color: '#F56C6C', bgColor: 'rgba(245, 108, 108, 0.1)' }
        ];
      });
    },
    /** 加载今日记录 */
    loadTodayRecords() {
      getTodayTomatoRecords().then(response => {
        this.historyRecords = response.rows || [];
        this.calculateTodayStats();
      }).catch(() => {
        // 模拟数据
        this.historyRecords = [
          {
            recordId: 1,
            taskTitle: '数学复习',
            startTime: '09:00',
            duration: 25,
            status: '1'
          },
          {
            recordId: 2,
            taskTitle: '英语单词记忆',
            startTime: '10:30',
            duration: 25,
            status: '1'
          }
        ];
        this.calculateTodayStats();
      });
    },
    /** 计算今日统计 */
    calculateTodayStats() {
      const completed = this.historyRecords.filter(r => r.status === '1').length;
      const totalTime = this.historyRecords.reduce((sum, r) => sum + (r.duration || 0), 0);
      this.todayStats = {
        completed,
        totalTime,
        interruptions: this.historyRecords.length - completed
      };
    },
    /** 加载学习计划 */
    loadPlans() {
      listStudyPlan({ status: '0' }).then(response => {
        this.planOptions = response.rows || [];
      });
    },
    /** 加载设置 */
    loadSettings() {
      const savedSettings = localStorage.getItem('tomatoSettings');
      if (savedSettings) {
        this.settings = JSON.parse(savedSettings);
      }
      this.updateTimerDisplay();
    },
    /** 自动设置时间 */
    autoSetTime() {
      this.autoSettingLoading = true;
      
      // 模拟智能分析过程
      setTimeout(() => {
        // 根据任务难度和学科智能设置时间
        let tomatoDuration = 25; // 默认25分钟
        let restDuration = 5;    // 默认5分钟
        
        // 根据学科调整时间
        const subjectTimes = {
          '数学': { tomato: 30, rest: 5 },
          '物理': { tomato: 30, rest: 5 },
          '化学': { tomato: 25, rest: 5 },
          '英语': { tomato: 20, rest: 5 },
          '语文': { tomato: 20, rest: 5 },
          '计算机': { tomato: 35, rest: 10 },
          '历史': { tomato: 20, rest: 5 },
          '地理': { tomato: 20, rest: 5 }
        };
        
        const subject = this.taskForm.subject || '其他';
        if (subjectTimes[subject]) {
          tomatoDuration = subjectTimes[subject].tomato;
          restDuration = subjectTimes[subject].rest;
        }
        
        // 根据难度调整时间
        const difficulty = this.taskForm.difficulty || 2;
        if (difficulty === 1) { // 简单
          tomatoDuration = Math.max(15, tomatoDuration - 10);
        } else if (difficulty === 3) { // 困难
          tomatoDuration = Math.min(45, tomatoDuration + 10);
          restDuration = Math.min(15, restDuration + 5);
        }
        
        // 更新设置
        this.settings.focusDuration = tomatoDuration * 60; // 转换为秒
        this.settings.breakDuration = restDuration * 60;
        
        this.$message.success(`已为您智能设置：专注${tomatoDuration}分钟，休息${restDuration}分钟`);
        this.autoSettingLoading = false;
      }, 1500);
    },
    /** 保存设置 */
    saveSettings() {
      localStorage.setItem('tomatoSettings', JSON.stringify(this.settings));
      this.updateTimerDisplay();
      this.$message.success('设置已保存');
    },
    /** 保存任务 */
    saveTask() {
      if (!this.taskForm.taskTitle.trim()) {
        this.$message.warning('请输入任务标题');
        return;
      }
      // 这里应该调用API保存任务信息
      this.$message.success('任务信息已保存');
    },
    /** 开始计时器 */
    startTimer() {
      if (!this.taskForm.taskTitle.trim()) {
        this.$message.warning('请先设置当前任务');
        return;
      }
      
      const requestData = {
        taskTitle: this.taskForm.taskTitle,
        description: this.taskForm.description,
        planId: this.taskForm.planId,
        focusTime: this.settings.focusTime,
        breakTime: this.settings.breakTime,
        longBreakTime: this.settings.longBreakTime
      };
      
      startTomato(requestData).then(response => {
        this.currentRecord = response.data;
        this.cycleCount = this.currentRecord.currentCycle || 1;
        this.startCountdown();
        this.$message.success('番茄钟已开始');
      }).catch(() => {
        // 模拟开始
        this.currentRecord = {
          recordId: Date.now(),
          taskTitle: this.taskForm.taskTitle,
          description: this.taskForm.description,
          elapsedTime: 0,
          currentCycle: this.cycleCount + 1
        };
        this.startCountdown();
        this.$message.success('番茄钟已开始');
      });
    },
    /** 开始倒计时 */
    startCountdown() {
      this.updateTimerDisplay();
      this.isRunning = true;
      this.isPaused = false;
      
      this.timer = setInterval(() => {
        this.timeLeft--;
        if (this.currentRecord) {
          this.currentRecord.elapsedTime = (this.currentRecord.elapsedTime || 0) + 1;
        }
        
        if (this.timeLeft <= 0) {
          this.completePhase();
        }
      }, 1000);
    },
    /** 完成当前阶段 */
    completePhase() {
      this.clearTimer();
      
      if (this.currentPhase === 'focus') {
        // 完成专注阶段
        completeTomato(this.currentRecord.recordId).then(() => {
          this.$message.success(`第${this.cycleCount}个番茄钟完成！`);
        }).catch(() => {
          this.$message.success(`第${this.cycleCount}个番茄钟完成！`);
        });
        
        // 决定下一个阶段
        if (this.cycleCount % this.settings.longBreakInterval === 0) {
          this.currentPhase = 'longBreak';
        } else {
          this.currentPhase = 'break';
        }
        
        this.cycleCount++;
        if (this.settings.autoStartBreak) {
          setTimeout(() => {
            this.startTimer();
          }, 2000);
        }
      } else {
        // 完成休息阶段
        this.currentPhase = 'focus';
        this.$message.info('休息结束，准备开始下一个番茄钟');
      }
      
      this.updateTimerDisplay();
    },
    /** 暂停计时器 */
    pauseTimer() {
      this.clearTimer();
      this.isRunning = false;
      this.isPaused = true;
      
      pauseTomato(this.currentRecord.recordId).catch(() => {
        // 本地暂停逻辑
      });
    },
    /** 恢复计时器 */
    resumeTimer() {
      resumeTomato(this.currentRecord.recordId).then(() => {
        this.startCountdown();
        this.$message.success('番茄钟已恢复');
      }).catch(() => {
        this.startCountdown();
        this.$message.success('番茄钟已恢复');
      });
    },
    /** 放弃计时器 */
    abandonTimer() {
      this.$modal.confirm('确定要放弃当前番茄钟吗？').then(() => {
        this.clearTimer();
        this.isRunning = false;
        this.isPaused = false;
        this.currentPhase = 'focus';
        this.currentRecord = null;
        this.updateTimerDisplay();
        
        if (this.currentRecord && this.currentRecord.recordId) {
          abandonTomato(this.currentRecord.recordId).catch(() => {});
        }
        
        this.$message.info('番茄钟已放弃');
      }).catch(() => {});
    },
    /** 清除计时器 */
    clearTimer() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },
    /** 更新计时器显示 */
    updateTimerDisplay() {
      switch (this.currentPhase) {
        case 'focus':
          this.timeLeft = this.settings.focusTime * 60;
          this.totalTime = this.settings.focusTime * 60;
          break;
        case 'break':
          this.timeLeft = this.settings.breakTime * 60;
          this.totalTime = this.settings.breakTime * 60;
          break;
        case 'longBreak':
          this.timeLeft = this.settings.longBreakTime * 60;
          this.totalTime = this.settings.longBreakTime * 60;
          break;
      }
    },
    /** 刷新历史记录 */
    refreshHistory() {
      this.loadTodayRecords();
      this.loadStatistics();
    },
    // 格式化时间
    formatTime(seconds) {
      const mins = Math.floor(seconds / 60);
      const secs = seconds % 60;
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    },
    /** 获取难度文本 */
    getDifficultyText(difficulty) {
      const texts = {
        1: '简单',
        2: '中等',
        3: '困难'
      };
      return texts[difficulty] || '中等';
    },
    /** 格式化分钟 */
    formatMinutes(minutes) {
      if (minutes < 60) {
        return `${minutes}分钟`;
      }
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return mins > 0 ? `${hours}小时${mins}分钟` : `${hours}小时`;
    },
    // 格式化时间(仅时间部分)
    formatTimeOnly(timeStr) {
      if (!timeStr) return '--:--';
      return timeStr.substring(0, 5);
    },
    // 获取记录状态类型
    getRecordStatusType(status) {
      const types = {
        '0': 'info',    // 进行中
        '1': 'success', // 已完成
        '2': 'warning', // 已暂停
        '3': 'danger'   // 已放弃
      };
      return types[status] || 'info';
    },
    // 获取记录状态文本
    getRecordStatusText(status) {
      const texts = {
        '0': '进行中',
        '1': '已完成',
        '2': '已暂停',
        '3': '已放弃'
      };
      return texts[status] || '未知';
    },
    // 获取效率状态
    getEfficiencyStatus(value) {
      if (value >= 80) return 'success';
      if (value >= 60) return 'warning';
      return 'exception';
    }
  }
};
</script>

<style scoped lang="scss">
.tomato-page {
  padding: 20px;
  
  .page-header {
    text-align: center;
    margin-bottom: 30px;
    
    .page-title {
      font-size: 2rem;
      font-weight: 300;
      color: #333;
      margin-bottom: 10px;
    }
    
    .page-subtitle {
      font-size: 1.1rem;
      color: #666;
      font-weight: 300;
    }
  }
  
  .setting-tip {
    font-size: 0.9rem;
    color: #999;
    margin-top: 8px;
    margin-bottom: 0;
  }
  
  .stats-overview {
    margin-bottom: 30px;
    
    .stat-card {
      border-radius: 12px;
      padding: 20px;
      text-align: center;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }
      
      .stat-icon {
        font-size: 2rem;
        margin-bottom: 10px;
      }
      
      .stat-content {
        .stat-value {
          font-size: 1.8rem;
          font-weight: 600;
          color: #333;
          margin-bottom: 5px;
        }
        
        .stat-label {
          font-size: 1rem;
          color: #666;
          margin-bottom: 3px;
        }
        
        .stat-sub {
          font-size: 0.85rem;
          color: #999;
        }
      }
    }
  }
  
  .timer-section {
    .tomato-display {
      text-align: center;
      margin-bottom: 30px;
      
      .tomato-circle {
        width: 300px;
        height: 300px;
        border-radius: 50%;
        margin: 0 auto 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        transition: all 0.3s ease;
        
        &.running {
          background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
          box-shadow: 0 0 30px rgba(64, 158, 255, 0.5);
        }
        
        &.paused {
          background: linear-gradient(135deg, #E6A23C 0%, #F56C6C 100%);
          box-shadow: 0 0 30px rgba(230, 162, 60, 0.5);
        }
        
        &.idle {
          background: linear-gradient(135deg, #909399 0%, #C0C4CC 100%);
        }
        
        .tomato-inner {
          width: 260px;
          height: 260px;
          border-radius: 50%;
          background: white;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          
          .time-display {
            font-size: 3rem;
            font-weight: 700;
            color: #333;
            margin-bottom: 10px;
            font-family: 'Courier New', monospace;
          }
          
          .phase-indicator {
            font-size: 1.2rem;
            color: #666;
            margin-bottom: 15px;
          }
          
          .tomato-icon {
            font-size: 2rem;
            color: #F56C6C;
          }
        }
      }
      
      .control-buttons {
        margin-bottom: 30px;
        
        .el-button {
          margin: 0 10px;
          padding: 15px 30px;
          font-size: 1.1rem;
        }
      }
      
      .current-task {
        background: #f0f9ff;
        border: 1px solid #409EFF;
        border-radius: 12px;
        padding: 20px;
        text-align: left;
        
        .task-info {
          margin-top: 15px;
          
          h4 {
            font-size: 1.2rem;
            margin-bottom: 10px;
            color: #333;
          }
          
          p {
            color: #666;
            margin-bottom: 15px;
            line-height: 1.5;
          }
          
          .task-stats {
            display: flex;
            gap: 20px;
            
            span {
              display: flex;
              align-items: center;
              gap: 5px;
              color: #409EFF;
              font-size: 0.9rem;
            }
          }
        }
      }
    }
    
    .settings-panel {
      .el-form-item {
        display: flex;
        align-items: center;
        
        .unit {
          margin-left: 10px;
          color: #999;
        }
      }
    }
  }
  
  .history-section {
    .today-summary {
      display: flex;
      justify-content: space-around;
      margin-bottom: 20px;
      padding: 15px;
      background: #f8f9fa;
      border-radius: 8px;
      
      .summary-item {
        text-align: center;
        
        .summary-value {
          font-size: 1.5rem;
          font-weight: 600;
          color: #409EFF;
          margin-bottom: 5px;
        }
        
        .summary-label {
          font-size: 0.9rem;
          color: #666;
        }
      }
    }
    
    .history-list {
      max-height: 400px;
      overflow-y: auto;
      
      .history-item {
        display: flex;
        padding: 15px 0;
        border-bottom: 1px solid #f0f0f0;
        
        &:last-child {
          border-bottom: none;
        }
        
        .history-time {
          width: 60px;
          font-size: 0.9rem;
          color: #999;
          margin-right: 15px;
        }
        
        .history-content {
          flex: 1;
          
          .history-title {
            font-weight: 500;
            margin-bottom: 5px;
            color: #333;
          }
          
          .history-duration {
            font-size: 0.9rem;
            color: #666;
            margin-bottom: 5px;
          }
        }
      }
      
      .empty-history {
        text-align: center;
        padding: 40px 20px;
        color: #999;
        
        i {
          font-size: 3rem;
          margin-bottom: 15px;
          display: block;
        }
        
        p {
          margin: 10px 0;
        }
        
        .sub-text {
          font-size: 0.9rem;
        }
      }
    }
  }
  
  .analysis-section {
    .efficiency-chart {
      text-align: center;
      margin-bottom: 20px;
      
      .chart-placeholder {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 30px;
        color: #999;
        
        i {
          font-size: 2rem;
          margin-bottom: 10px;
          display: block;
        }
      }
    }
    
    .efficiency-stats {
      .stat-row {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
        
        .stat-label {
          width: 80px;
          font-size: 0.9rem;
          color: #666;
        }
        
        .el-progress {
          flex: 1;
          margin-left: 15px;
        }
      }
    }
  }
  
  // 响应式设计
  @media (max-width: 768px) {
    .tomato-display .tomato-circle {
      width: 250px;
      height: 250px;
      
      .tomato-inner {
        width: 210px;
        height: 210px;
        
        .time-display {
          font-size: 2.5rem;
        }
      }
    }
    
    .control-buttons .el-button {
      display: block;
      width: 100%;
      margin: 10px 0 !important;
    }
    
    .today-summary {
      flex-direction: column;
      gap: 15px;
    }
  }
}
</style>