<template>
  <div class="app-container statistics-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">学习数据可视化</h1>
      <p class="page-subtitle">全面了解您的学习情况和发展趋势</p>
    </div>

    <!-- 时间筛选器 -->
    <div class="filter-bar">
      <el-radio-group v-model="timeRange" @change="handleTimeRangeChange" size="medium">
        <el-radio-button label="7day">近7天</el-radio-button>
        <el-radio-button label="30day">近30天</el-radio-button>
        <el-radio-button label="90day">近90天</el-radio-button>
        <el-radio-button label="year">今年</el-radio-button>
      </el-radio-group>
      
      <div class="filter-actions">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          @change="handleDateRangeChange"
          style="width: 280px; margin-right: 15px;"
        ></el-date-picker>
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">刷新数据</el-button>
        <el-button icon="el-icon-download" @click="exportData">导出数据</el-button>
      </div>
    </div>

    <!-- 核心指标概览 -->
    <el-row :gutter="20" class="metrics-overview">
      <el-col :xs="12" :sm="6" v-for="metric in metrics" :key="metric.key">
        <div class="metric-card" :style="{ borderColor: metric.color }">
          <div class="metric-icon" :style="{ backgroundColor: metric.color }">
            <i :class="metric.icon"></i>
          </div>
          <div class="metric-content">
            <div class="metric-value">{{ metric.value }}</div>
            <div class="metric-label">{{ metric.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 左侧：学习趋势图 -->
      <el-col :xs="24" :md="16">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span class="card-title">学习趋势分析</span>
            <div style="float: right;">
              <el-radio-group v-model="trendType" size="small" @change="updateTrendChart">
                <el-radio-button label="studyTime">学习时长</el-radio-button>
                <el-radio-button label="tomatoCount">番茄钟数</el-radio-button>
                <el-radio-button label="completedTasks">完成任务</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="chart-container">
            <div ref="trendChart" style="width: 100%; height: 350px;"></div>
          </div>
        </el-card>

        <!-- 学科分布 -->
        <el-card class="chart-card" style="margin-top: 20px;">
          <div slot="header" class="clearfix">
            <span class="card-title">学科学习分布</span>
          </div>
          <div class="chart-container">
            <div ref="subjectChart" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：效率分析 -->
      <el-col :xs="24" :md="8">
        <el-card class="efficiency-card">
          <div slot="header" class="clearfix">
            <span class="card-title">学习效率分析</span>
          </div>
          
          <!-- 效率雷达图 -->
          <div class="radar-chart">
            <div ref="efficiencyChart" style="width: 100%; height: 250px;"></div>
          </div>
          
          <!-- 效率指标 -->
          <div class="efficiency-metrics">
            <div class="efficiency-item" v-for="item in efficiencyMetrics" :key="item.key">
              <div class="efficiency-label">{{ item.label }}</div>
              <el-progress
                :percentage="Math.min(100, Math.max(0, item.value || 0))"
                :status="getEfficiencyStatus(item.value)"
                :stroke-width="8"
                text-inside
              ></el-progress>
              <div class="efficiency-value">{{ item.value }}%</div>
            </div>
          </div>
        </el-card>

        <!-- 时间分布 -->
        <el-card class="time-distribution-card" style="margin-top: 20px;">
          <div slot="header" class="clearfix">
            <span class="card-title">学习时间分布</span>
          </div>
          <div class="distribution-container">
            <div ref="timeChart" style="width: 100%; height: 200px;"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细数据表格 -->
    <el-card class="data-table-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span class="card-title">详细学习记录</span>
      </div>
      
      <el-table
        :data="detailData"
        style="width: 100%"
        v-loading="tableLoading"
      >
        <el-table-column prop="date" label="日期" width="120"></el-table-column>
        <el-table-column prop="studyTime" label="学习时长(分钟)" width="150">
          <template slot-scope="scope">
            <span class="time-value">{{ scope.row.studyTime }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="tomatoCount" label="番茄钟数" width="120">
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.tomatoCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completedTasks" label="完成任务" width="120">
          <template slot-scope="scope">
            <el-tag type="primary">{{ scope.row.completedTasks }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="efficiencyScore" label="效率评分" width="120">
          <template slot-scope="scope">
            <el-tag :type="getScoreType(scope.row.efficiencyScore)">
              {{ scope.row.efficiencyScore }}分
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getStudyStatistics, getStudyTrends, getSubjectDistribution, getTimeDistribution, getEfficiencyAnalysis, getAchievements, exportStudyData } from "@/api/study/statistics";
import { getTomatoStatistics } from "@/api/study/tomato";
import * as echarts from 'echarts';

export default {
  name: "StudyStatistics",
  data() {
    return {
      // 时间范围
      timeRange: '30day',
      dateRange: [],
      startDate: '',
      endDate: '',
      
      // 趋势类型
      trendType: 'studyTime',
      
      // 核心指标
      metrics: [
        { key: 'totalStudyTime', label: '今日学习时长', value: '0分钟', icon: 'el-icon-time', color: '#409EFF' },
        { key: 'tomatoCount', label: '今日番茄钟', value: '0个', icon: 'el-icon-timer', color: '#67C23A' },
        { key: 'completedTasks', label: '今日完成任务', value: '0个', icon: 'el-icon-check', color: '#E6A23C' },
        { key: 'efficiencyScore', label: '效率评分', value: '0分', icon: 'el-icon-data-analysis', color: '#F56C6C' }
      ],
      
      // 效率指标
      efficiencyMetrics: [
        { key: 'focus', label: '专注度', value: 0 },
        { key: 'completion', label: '完成率', value: 0 },
        { key: 'consistency', label: '连续性', value: 0 },
        { key: 'productivity', label: '产出比', value: 0 }
      ],
      
      // 详细数据
      detailData: [],
      tableLoading: false,
      
      // 图表实例
      trendChart: null,
      subjectChart: null,
      efficiencyChart: null,
      timeChart: null,
      
      // 趋势原始数据
      trendData: []
    };
  },
  mounted() {
    this.initDateRange();
    this.initCharts();
    this.loadData();
  },
  beforeDestroy() {
    this.disposeCharts();
  },
  methods: {
    /** 初始化日期范围 */
    initDateRange() {
      const now = new Date();
      const days = 30;
      const start = new Date(now.getTime() - (days - 1) * 24 * 60 * 60 * 1000);
      this.startDate = this.formatDateStr(start);
      this.endDate = this.formatDateStr(now);
    },
    
    /** 初始化图表 */
    initCharts() {
      this.$nextTick(() => {
        this.initTrendChart();
        this.initSubjectChart();
        this.initEfficiencyChart();
        this.initTimeChart();
      });
    },
    
    /** 初始化趋势图 */
    initTrendChart() {
      this.trendChart = echarts.init(this.$refs.trendChart);
      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['学习时长', '番茄钟数', '完成任务'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: [] },
        yAxis: { type: 'value' },
        series: [
          { name: '学习时长', type: 'line', data: [], smooth: true },
          { name: '番茄钟数', type: 'line', data: [], smooth: true },
          { name: '完成任务', type: 'line', data: [], smooth: true }
        ]
      };
      this.trendChart.setOption(option);
    },
    
    /** 初始化学科分布图 */
    initSubjectChart() {
      this.subjectChart = echarts.init(this.$refs.subjectChart);
      const option = {
        tooltip: { trigger: 'item', formatter: '{b}: {c}分钟 ({d}%)' },
        legend: { orient: 'vertical', left: 'left' },
        series: [{
          name: '学科分布',
          type: 'pie',
          radius: '50%',
          data: [{ value: 0, name: '暂无数据' }],
          emphasis: {
            itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' }
          }
        }]
      };
      this.subjectChart.setOption(option);
    },
    
    /** 初始化效率雷达图 */
    initEfficiencyChart() {
      this.efficiencyChart = echarts.init(this.$refs.efficiencyChart);
      const option = {
        radar: {
          indicator: [
            { name: '专注度', max: 100 },
            { name: '完成率', max: 100 },
            { name: '连续性', max: 100 },
            { name: '产出比', max: 100 },
            { name: '稳定性', max: 100 }
          ]
        },
        series: [{
          name: '效率指标',
          type: 'radar',
          data: [{ value: [0, 0, 0, 0, 0], name: '当前水平' }]
        }]
      };
      this.efficiencyChart.setOption(option);
    },
    
    /** 初始化时间分布图 */
    initTimeChart() {
      this.timeChart = echarts.init(this.$refs.timeChart);
      const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        xAxis: { type: 'category', data: [] },
        yAxis: { type: 'value' },
        series: [{
          data: [],
          type: 'bar',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      };
      this.timeChart.setOption(option);
    },
    
    /** 销毁图表 */
    disposeCharts() {
      if (this.trendChart) this.trendChart.dispose();
      if (this.subjectChart) this.subjectChart.dispose();
      if (this.efficiencyChart) this.efficiencyChart.dispose();
      if (this.timeChart) this.timeChart.dispose();
    },
    
    /** 加载数据 */
    loadData() {
      this.loadMetrics();
      this.loadTrendData();
      this.loadSubjectDistribution();
      this.loadTimeDistribution();
      this.loadEfficiencyData();
      this.loadDetailData();
    },
    
    /** 加载核心指标（今日数据） */
    loadMetrics() {
      getStudyStatistics().then(response => {
        const data = response.data || {};
        const studyMinutes = data.study_minutes || 0;
        const tomatoCount = data.tomato_count || 0;
        const completedTasks = data.completed_tasks || 0;
        
        this.metrics[0].value = this.formatTime(studyMinutes);
        this.metrics[1].value = tomatoCount + '个';
        this.metrics[2].value = completedTasks + '个';
        this.metrics[3].value = this.calcEfficiencyScore(studyMinutes, completedTasks) + '分';
      }).catch(() => {
        this.metrics[0].value = '0分钟';
        this.metrics[1].value = '0个';
        this.metrics[2].value = '0个';
        this.metrics[3].value = '0分';
      });
    },
    
    /** 计算效率评分 */
    calcEfficiencyScore(minutes, tasks) {
      const score = tasks * 2 + minutes / 60;
      return Math.min(10, Math.round(score * 10) / 10);
    },
    
    /** 加载趋势数据 */
    loadTrendData() {
      const days = this.getDaysFromTimeRange();
      getStudyTrends(days).then(response => {
        this.trendData = response.data || [];
        this.updateTrendChart();
      }).catch(() => {
        this.trendData = [];
        this.updateTrendChart();
      });
    },
    
    /** 更新趋势图 */
    updateTrendChart() {
      if (!this.trendChart || !this.trendData.length) return;
      
      const dates = this.trendData.map(d => d.date);
      const studyTimes = this.trendData.map(d => d.studyTime || 0);
      const tomatoCounts = this.trendData.map(d => d.tomatoCount || 0);
      const completedTasks = this.trendData.map(d => d.completedTasks || 0);
      
      let activeData, activeName;
      switch (this.trendType) {
        case 'tomatoCount':
          activeData = tomatoCounts; activeName = '番茄钟数'; break;
        case 'completedTasks':
          activeData = completedTasks; activeName = '完成任务'; break;
        default:
          activeData = studyTimes; activeName = '学习时长';
      }
      
      this.trendChart.setOption({
        xAxis: { data: dates },
        series: [
          { name: '学习时长', data: studyTimes, type: 'line' },
          { name: '番茄钟数', data: tomatoCounts, type: 'line' },
          { name: '完成任务', data: completedTasks, type: 'line' }
        ]
      });
    },
    
    /** 加载学科分布数据 */
    loadSubjectDistribution() {
      getSubjectDistribution(this.startDate, this.endDate).then(response => {
        const data = response.data || [];
        if (this.subjectChart) {
          if (data.length === 0) {
            this.subjectChart.setOption({
              series: [{ data: [{ value: 0, name: '暂无数据' }] }]
            });
          } else {
            const pieData = data.map(item => ({
              value: item.totalMinutes || 0,
              name: item.subjectName || '未分类'
            }));
            this.subjectChart.setOption({
              series: [{ data: pieData }]
            });
          }
        }
      }).catch(() => {
        if (this.subjectChart) {
          this.subjectChart.setOption({
            series: [{ data: [{ value: 0, name: '暂无数据' }] }]
          });
        }
      });
    },
    
    /** 加载时间分布数据 */
    loadTimeDistribution() {
      getTimeDistribution(this.startDate, this.endDate).then(response => {
        const data = response.data || [];
        if (this.timeChart) {
          const slots = data.map(d => d.timeSlot || '');
          const minutes = data.map(d => d.totalMinutes || 0);
          this.timeChart.setOption({
            xAxis: { data: slots },
            series: [{ data: minutes }]
          });
        }
      }).catch(() => {
        // 保持空数据
      });
    },
    
    /** 加载效率数据 */
    loadEfficiencyData() {
      const today = this.formatDateStr(new Date());
      getEfficiencyAnalysis(today).then(response => {
        const score = response.data || 0;
        // 基于效率评分计算各项指标
        const focusScore = Math.min(100, Math.round(score * 10));
        const completionScore = Math.min(100, Math.round(score * 10));
        const consistencyScore = Math.min(100, Math.round(score * 8));
        const productivityScore = Math.min(100, Math.round(score * 12));
        const stabilityScore = Math.min(100, Math.round(score * 9));
        
        this.efficiencyMetrics = [
          { key: 'focus', label: '专注度', value: focusScore },
          { key: 'completion', label: '完成率', value: completionScore },
          { key: 'consistency', label: '连续性', value: consistencyScore },
          { key: 'productivity', label: '产出比', value: productivityScore }
        ];
        
        if (this.efficiencyChart) {
          this.efficiencyChart.setOption({
            series: [{
              data: [{ value: [focusScore, completionScore, consistencyScore, productivityScore, stabilityScore], name: '当前水平' }]
            }]
          });
        }
      }).catch(() => {
        // 保持默认值
      });
    },
    
    /** 加载详细数据表格 */
    loadDetailData() {
      this.tableLoading = true;
      getStudyTrends(this.getDaysFromTimeRange()).then(response => {
        const data = response.data || [];
        this.detailData = data.map(item => ({
          date: item.date,
          studyTime: item.studyTime || 0,
          tomatoCount: item.tomatoCount || 0,
          completedTasks: item.completedTasks || 0,
          efficiencyScore: this.calcEfficiencyScore(item.studyTime || 0, item.completedTasks || 0)
        })).reverse();
        this.tableLoading = false;
      }).catch(() => {
        this.detailData = [];
        this.tableLoading = false;
      });
    },
    
    /** 获取天数 */
    getDaysFromTimeRange() {
      switch (this.timeRange) {
        case '7day': return 7;
        case '30day': return 30;
        case '90day': return 90;
        case 'year': return 365;
        default: return 30;
      }
    },
    
    /** 时间范围变化 */
    handleTimeRangeChange() {
      const now = new Date();
      let days = this.getDaysFromTimeRange();
      const start = new Date(now.getTime() - (days - 1) * 24 * 60 * 60 * 1000);
      this.startDate = this.formatDateStr(start);
      this.endDate = this.formatDateStr(now);
      this.dateRange = [this.startDate, this.endDate];
      this.refreshData();
    },
    
    /** 日期范围变化 */
    handleDateRangeChange() {
      if (this.dateRange && this.dateRange.length === 2) {
        this.startDate = this.dateRange[0];
        this.endDate = this.dateRange[1];
      }
      this.refreshData();
    },
    
    /** 刷新数据 */
    refreshData() {
      this.loadData();
    },
    
    /** 导出数据 */
    exportData() {
      exportStudyData().then(response => {
        const blob = new Blob([response]);
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = `学习数据_${this.formatDateStr(new Date())}.xlsx`;
        link.click();
        URL.revokeObjectURL(link.href);
        this.$message.success('数据导出成功');
      }).catch(() => {
        this.$message.error('数据导出失败');
      });
    },
    
    // 工具方法
    formatDateStr(date) {
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, '0');
      const d = String(date.getDate()).padStart(2, '0');
      return `${y}-${m}-${d}`;
    },
    
    formatTime(minutes) {
      if (minutes < 60) return `${minutes}分钟`;
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return mins > 0 ? `${hours}小时${mins}分钟` : `${hours}小时`;
    },
    
    getEfficiencyStatus(value) {
      if (value >= 80) return 'success';
      if (value >= 60) return 'warning';
      return 'exception';
    },
    
    getScoreType(score) {
      if (score >= 90) return 'success';
      if (score >= 80) return 'primary';
      if (score >= 70) return 'warning';
      return 'danger';
    }
  }
};
</script>

<style scoped lang="scss">
.statistics-page {
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
  
  .filter-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    flex-wrap: wrap;
    gap: 15px;
    
    .filter-actions {
      display: flex;
      align-items: center;
    }
  }
  
  .metrics-overview {
    margin-bottom: 30px;
    
    .metric-card {
      background: white;
      border-radius: 12px;
      padding: 25px;
      text-align: center;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      border: 1px solid #f0f0f0;
      transition: all 0.3s ease;
      height: 100%;
      
      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
      }
      
      .metric-icon {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 15px;
        color: white;
        font-size: 24px;
      }
      
      .metric-content {
        .metric-value {
          font-size: 1.8rem;
          font-weight: 600;
          color: #333;
          margin-bottom: 8px;
        }
        
        .metric-label {
          font-size: 1rem;
          color: #666;
        }
      }
    }
  }
  
  .chart-card, .efficiency-card, .time-distribution-card, .data-table-card {
    margin-bottom: 20px;
    
    .card-title {
      font-size: 1.2rem;
      font-weight: 500;
      color: #333;
    }
    
    .chart-container {
      padding: 10px 0;
    }
  }
  
  .efficiency-card {
    .radar-chart {
      margin-bottom: 20px;
    }
    
    .efficiency-metrics {
      .efficiency-item {
        margin-bottom: 20px;
        
        .efficiency-label {
          font-size: 0.9rem;
          color: #666;
          margin-bottom: 8px;
        }
        
        .efficiency-value {
          font-size: 0.9rem;
          font-weight: 500;
          color: #333;
          margin-top: 5px;
          text-align: right;
        }
      }
    }
  }
  
  .data-table-card {
    .time-value {
      font-weight: 500;
      color: #409EFF;
    }
  }
  
  @media (max-width: 768px) {
    .filter-bar {
      flex-direction: column;
      align-items: stretch;
      
      .filter-actions {
        justify-content: center;
        flex-wrap: wrap;
      }
    }
    
    .metrics-overview .metric-card {
      padding: 15px;
      
      .metric-value {
        font-size: 1.4rem;
      }
    }
  }
}
</style>
