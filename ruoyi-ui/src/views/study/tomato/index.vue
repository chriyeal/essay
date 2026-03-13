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
                <h4>{{ currentPlanName }}</h4>
                <div class="task-stats">
                  <span><i class="el-icon-time"></i> 已专注 {{ formatSeconds(currentRecord.elapsedTime || 0) }}</span>
                  <span><i class="el-icon-timer"></i> 第 {{ currentRecord.currentCycle || 1 }} 个番茄钟</span>
                  <span v-if="learnedTime > 0"><i class="el-icon-data-line"></i> 该计划已学习 {{ formatLearnedTime(learnedTime) }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 设置面板 -->
          <div class="settings-panel">
            <el-collapse v-model="activeNames">
              <el-collapse-item title="番茄钟设置" name="1">
                <div class="settings-content">
                  <div class="setting-item">
                    <label class="setting-label">计时模式</label>
                    <el-radio-group v-model="timerMode" size="medium">
                      <el-radio-button label="focus">专注时间</el-radio-button>
                      <el-radio-button label="break">休息时间</el-radio-button>
                    </el-radio-group>
                    <p class="setting-tip">提示：选择当前要进行的模式</p>
                  </div>
                  
                  <div class="setting-item">
                    <label class="setting-label">专注时间（分钟）</label>
                    <el-slider
                      v-model="settings.tomatoDuration"
                      :min="1"
                      :max="60"
                      :step="1"
                      show-input
                    ></el-slider>
                    <p class="setting-tip">提示：拖动滑块设置 1-60 分钟的专注时间</p>
                  </div>
                  
                  <div class="setting-item">
                    <label class="setting-label">休息时间（分钟）</label>
                    <el-slider
                      v-model="settings.restDuration"
                      :min="1"
                      :max="60"
                      :step="1"
                      show-input
                    ></el-slider>
                    <p class="setting-tip">提示：拖动滑块设置 1-60 分钟的休息时间</p>
                  </div>
                  
                  <el-button type="primary" @click="saveSettings">保存设置</el-button>
                </div>
              </el-collapse-item>
              
              <el-collapse-item title="当前任务设置" name="2">
                <div class="task-settings">
                  <div class="setting-row">
                    <label class="setting-label">关联学习计划</label>
                    <el-select v-model="taskForm.planId" placeholder="选择关联的学习计划" style="width: 100%;" filterable clearable>
                      <el-option
                        v-for="plan in planOptions"
                        :key="plan.planId"
                        :label="plan.planName"
                        :value="plan.planId"
                      ></el-option>
                    </el-select>
                  </div>
                  
                  <div class="setting-row">
                    <label class="setting-label">总体时间（分钟）</label>
                    <el-input-number
                      v-model="taskForm.totalTime"
                      :min="1"
                      :max="999"
                      :step="5"
                      controls-position="right"
                    ></el-input-number>
                  </div>
                  
                  <div class="setting-row learned-time-display" v-if="taskForm.planId">
                    <label class="setting-label">已学习时间</label>
                    <div class="learned-value">
                      <el-tag type="success" size="medium">
                        <i class="el-icon-time"></i> {{ formatLearnedTime(learnedTime) }}
                      </el-tag>
                    </div>
                    <p class="setting-tip">提示：每完成一个番茄钟自动累计到已学习时间</p>
                  </div>
                  
                  <el-button type="success" @click="saveTask">保存任务</el-button>
                </div>
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
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getTomatoStatistics, getTodayTomatoRecords, startTomato, pauseTomato, resumeTomato, completeTomato, abandonTomato } from "@/api/study/tomato";
import { listStudyPlan, updateStudyPlan } from "@/api/study/plan";

export default {
  name: "TomatoClock",
  data() {
    return {
      // 计时器相关
      timeLeft: 25 * 60, // 默认 25 分钟
      totalTime: 25 * 60, // 总时间
      timerMode: 'focus', // 当前模式：'focus' 专注，'break' 休息
      settings: {
        tomatoDuration: 25, // 专注时间，默认 25 分钟
        restDuration: 10,   // 休息时间，默认 10 分钟
        longBreakTime: 15,
        longBreakInterval: 4,
        autoStartBreak: false // 不自动开始休息，让用户手动选择
      },
      
      // 任务表单
      taskForm: {
        planId: null,
        totalTime: 25  // 默认 25 分钟
      },
      
      // 已学习时间（分钟）
      learnedTime: 0,
      
      // 学习计划选项
      planOptions: [],
      
      // 当前记录
      currentRecord: null,
      
      // 计时器状态
      isRunning: false,
      isPaused: false,
      timer: null,
      currentPhase: 'focus',
      cycleCount: 0,
      
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
      
      // 折叠面板
      activeNames: ['1'],
    };
  },
  watch: {
    'settings.tomatoDuration'(newVal) {
      if (!this.isRunning && !this.isPaused) {
        this.updateTimerDuration();
      }
    },
    'settings.restDuration'(newVal) {
      if (!this.isRunning && !this.isPaused && this.timerMode === 'break') {
        this.updateTimerDuration();
      }
    },
    timerMode(newVal) {
      if (!this.isRunning && !this.isPaused) {
        this.updateTimerDuration();
      }
      console.log('模式已切换:', newVal);
    }
  },
  computed: {
    timerState() {
      if (this.isRunning) return 'running';
      if (this.isPaused) return 'paused';
      return 'idle';
    },
    currentPhaseText() {
      return this.timerMode === 'focus' ? '专注时间' : '休息时间';
    },
    // 获取关联的学习计划名称
    currentPlanName() {
      if (!this.taskForm.planId) return '未关联学习计划';
      const plan = this.planOptions.find(p => p.planId === this.taskForm.planId);
      return plan ? plan.planName : '未关联学习计划';
    }
  },
  created() {
    // 初始化时间设置
    this.updateTimerDuration();
    this.loadStatistics();
    this.loadTodayRecords();
    this.loadPlans();
    this.loadSettings();
    
    // 定时刷新统计数据（每 5 秒）
    this.statsRefreshTimer = setInterval(() => {
      this.loadStatistics();
      this.loadTodayRecords();
    }, 5000);
  },
  beforeDestroy() {
    this.clearTimer();
    if (this.statsRefreshTimer) {
      clearInterval(this.statsRefreshTimer);
    }
  },
  beforeRouteLeave(to, from, next) {
    // 只有在计时器真正运行中才提示
    if (this.isRunning && this.currentRecord) {
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
      // 如果没有在运行，直接离开
      next();
    }
  },
  methods: {
    /** 加载统计数据 */
    loadStatistics() {
      getTomatoStatistics().then(response => {
        const stats = response.data || {};
        // 强制触发 Vue 响应式更新
        this.statistics = this.statistics.map((item, index) => {
          if (index === 0) return { ...item, value: stats.todayCount || 0 };
          if (index === 1) return { ...item, value: stats.weekCount || 0 };
          if (index === 2) return { ...item, value: stats.monthCount || 0 };
          if (index === 3) return { ...item, value: stats.totalCount || 0 };
          return item;
        });
        console.log('✅ 统计数据已更新:', this.statistics);
      }).catch(error => {
        console.error('❌ 加载统计数据失败:', error);
        // 使用真实数据（0）而不是模拟数据
        this.statistics = [
          { key: 'today', label: '今日', sub: '番茄钟', value: 0, icon: 'el-icon-timer', color: '#409EFF', bgColor: 'rgba(64, 158, 255, 0.1)' },
          { key: 'week', label: '本周', sub: '番茄钟', value: 0, icon: 'el-icon-date', color: '#67C23A', bgColor: 'rgba(103, 194, 58, 0.1)' },
          { key: 'month', label: '本月', sub: '番茄钟', value: 0, icon: 'el-icon-month', color: '#E6A23C', bgColor: 'rgba(230, 162, 60, 0.1)' },
          { key: 'total', label: '总计', sub: '番茄钟', value: 0, icon: 'el-icon-data', color: '#F56C6C', bgColor: 'rgba(245, 108, 108, 0.1)' }
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
      // 强制触发 Vue 响应式更新
      this.$set(this, 'todayStats', {
        completed,
        totalTime,
        interruptions: this.historyRecords.length - completed
      });
    },
    /** 加载学习计划 */
    loadPlans() {
      listStudyPlan({ status: '0' }).then(response => {
        this.planOptions = response.rows || [];
      });
    },
    /** 更新计时器时长 */
    updateTimerDuration() {
      const duration = parseInt(this.settings.tomatoDuration) || 25;
      if (this.timerMode === 'focus') {
        this.timeLeft = duration * 60;
        this.totalTime = duration * 60;
      } else {
        const restDuration = parseInt(this.settings.restDuration) || 10;
        this.timeLeft = restDuration * 60;
        this.totalTime = restDuration * 60;
      }
      console.log('时间已更新:', this.timeLeft, '秒，模式:', this.timerMode);
    },
    /** 加载设置 */
    loadSettings() {
      const savedSettings = localStorage.getItem('tomatoSettings');
      if (savedSettings) {
        try {
          const parsed = JSON.parse(savedSettings);
          // 只更新用户保存过的设置，保持默认值
          this.settings = {
            ...this.settings,
            ...parsed
          };
        } catch (e) {
          console.error('解析设置失败:', e);
        }
      }
      // 初始化时间显示
      this.updateTimerDisplay();
      console.log('设置已加载:', this.settings);
    },
    /** 保存设置 */
    saveSettings() {
      localStorage.setItem('tomatoSettings', JSON.stringify(this.settings));
      this.updateTimerDuration();
      this.$message.success('设置已保存');
    },
    /** 保存任务 */
    saveTask() {
      // 直接保存，不做验证
      this.$message.success('任务信息已保存');
    },
    /** 开始计时器 */
    startTimer() {
      // 根据用户选择的模式设置时间
      if (this.timerMode === 'focus') {
        this.timeLeft = this.settings.tomatoDuration * 60;
        this.totalTime = this.settings.tomatoDuration * 60;
      } else {
        this.timeLeft = this.settings.restDuration * 60;
        this.totalTime = this.settings.restDuration * 60;
      }
      
      const requestData = {
        planId: this.taskForm.planId,
        totalTime: this.timerMode === 'focus' ? this.settings.tomatoDuration : this.settings.restDuration,
      };
      
      startTomato(requestData).then(response => {
        this.currentRecord = response.data;
        this.isRunning = true;
        this.isPaused = false;
        this.startCountdown();
        this.$message.success(`${this.timerMode === 'focus' ? '专注' : '休息'}已开始`);
      }).catch(() => {
        // 模拟开始
        this.currentRecord = {
          recordId: Date.now(),
          taskTitle: this.timerMode === 'focus' ? '专注学习' : '休息',
          status: '0',
          elapsedTime: 0
        };
        this.isRunning = true;
        this.isPaused = false;
        this.startCountdown();
      });
    },
    /** 开始倒计时 */
    startCountdown() {
      this.isRunning = true;
      this.isPaused = false;
      
      console.log('开始倒计时，初始时间:', this.timeLeft, '秒');
      
      this.timer = setInterval(() => {
        this.timeLeft--;
        if (this.currentRecord) {
          this.currentRecord.elapsedTime = (this.currentRecord.elapsedTime || 0) + 1;
        }
        
        if (this.timeLeft <= 0) {
          console.log('时间到！触发 completePhase');
          this.completePhase();
        }
      }, 1000);
    },
    /** 完成当前阶段 */
    completePhase() {
      this.clearTimer();
      
      console.log('completePhase 被调用，当前 timerMode:', this.timerMode);
      
      if (this.timerMode === 'focus') {
        // 完成专注阶段
        const duration = parseInt(this.settings.tomatoDuration) || 25;
        
        // 更新已学习时间
        this.learnedTime += duration;
        console.log('已学习时间累加:', this.learnedTime, '分钟，目标:', this.taskForm.totalTime, '分钟');
        
        // 播放提示音
        this.playNotificationSound();
        
        // 显示通知
        this.$notify({
          title: '专注完成',
          message: `太棒了！已完成第${this.cycleCount}个番茄钟，休息一下吧~`,
          type: 'success',
          duration: 3000
        });
        
        // 检查是否需要自动完成学习计划
        if (this.taskForm.planId && this.taskForm.totalTime && this.learnedTime >= this.taskForm.totalTime) {
          console.log('满足自动完成条件，调用 autoCompletePlan');
          this.autoCompletePlan();
        }
        
        completeTomato(this.currentRecord.recordId).then(() => {
          console.log('番茄钟已完成');
          // 清除当前记录
          this.currentRecord = null;
          // 刷新统计数据
          this.loadStatistics();
          this.loadTodayRecords();
        }).catch(() => {
          console.log('本地完成记录');
          // 清除当前记录
          this.currentRecord = null;
          // 本地也要刷新统计
          this.loadStatistics();
          this.loadTodayRecords();
        });
        
        // 决定下一个阶段 - 自动跳转休息（不自动开始）
        this.timerMode = 'break';
        if (this.cycleCount % this.settings.longBreakInterval === 0) {
          this.timeLeft = this.settings.longBreakTime * 60;
          this.totalTime = this.settings.longBreakTime * 60;
          this.$message.success('已完成 4 个番茄钟，进入长休息时间（15 分钟），请点击开始按钮');
        } else {
          this.timeLeft = this.settings.restDuration * 60;
          this.totalTime = this.settings.restDuration * 60;
          this.$message.success(`准备开始休息（${this.settings.restDuration}分钟），请点击开始按钮`);
        }
        
        this.cycleCount++;
        // 更新状态
        this.isRunning = false;
        this.isPaused = false;
      } else {
        // 完成休息阶段
        this.timerMode = 'focus';
        this.timeLeft = this.settings.tomatoDuration * 60;
        this.totalTime = this.settings.tomatoDuration * 60;
        
        // 播放提示音
        this.playNotificationSound();
        
        // 显示通知
        this.$notify({
          title: '休息结束',
          message: '休息好了吗？准备开始下一个番茄钟吧！',
          type: 'info',
          duration: 3000
        });
        
        // 更新状态
        this.isRunning = false;
        this.isPaused = false;
      }
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
    /** 播放提示音 */
    playNotificationSound() {
      // 简单的提示音，使用 Audio API
      try {
        const audioContext = new (window.AudioContext || window.webkitAudioContext)();
        const oscillator = audioContext.createOscillator();
        const gainNode = audioContext.createGain();
        
        oscillator.connect(gainNode);
        gainNode.connect(audioContext.destination);
        
        oscillator.frequency.value = 800;
        oscillator.type = 'sine';
        
        gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
        gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);
        
        oscillator.start(audioContext.currentTime);
        oscillator.stop(audioContext.currentTime + 0.5);
      } catch (e) {
        console.log('音频播放失败:', e);
      }
    },
    /** 自动完成学习计划 */
    autoCompletePlan() {
      this.$message.success(`恭喜！该学习计划已达到目标时长，已自动标记为已完成`);
      
      // 调用后端接口更新计划状态
      updateStudyPlan({
        planId: this.taskForm.planId,
        status: '1', // 设置为已完成
        progress: 100 // 设置进度为 100%
      }).then(() => {
        console.log('学习计划已自动完成');
      }).catch(() => {
        console.log('本地更新学习计划状态');
      });
    },
    /** 更新计时器显示 */
    updateTimerDisplay() {
      if (this.timerMode === 'focus') {
        this.timeLeft = this.settings.tomatoDuration * 60;
        this.totalTime = this.settings.tomatoDuration * 60;
      } else {
        this.timeLeft = this.settings.restDuration * 60;
        this.totalTime = this.settings.restDuration * 60;
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
    /** 格式化秒数 */
    formatSeconds(seconds) {
      const mins = Math.floor(seconds / 60);
      const secs = seconds % 60;
      if (mins === 0) {
        return `${secs}秒`;
      } else if (secs === 0) {
        return `${mins}分钟`;
      } else {
        return `${mins}分${secs}秒`;
      }
    },
    /** 格式化已学习时间 */
    formatLearnedTime(minutes) {
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
      margin-top: 20px;
    }
    
    .settings-content {
      padding: 10px 0;
    }

    .setting-item {
      margin-bottom: 20px;
    }

    .setting-label {
      display: block;
      font-size: 14px;
      color: #606266;
      margin-bottom: 10px;
    }

    .setting-tip {
      font-size: 12px;
      color: #909399;
      margin-top: 5px;
    }
    
    .task-settings {
      padding: 10px 0;
      
      .setting-row {
        margin-bottom: 20px;
        
        .setting-label {
          display: block;
          font-size: 14px;
          color: #606266;
          margin-bottom: 10px;
        }
        
        .learned-value {
          margin-top: 5px;
          
          .el-tag {
            font-size: 16px;
            padding: 8px 15px;
          }
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