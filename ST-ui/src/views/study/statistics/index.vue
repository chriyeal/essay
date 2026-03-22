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
            <div class="metric-change" :class="metric.change > 0 ? 'positive' : 'negative'">
              <i :class="metric.change > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ Math.abs(metric.change) }}%
            </div>
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
              <el-radio-group v-model="trendType" size="small">
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
        <el-table-column prop="subjects" label="涉及学科">
          <template slot-scope="scope">
            <el-tag
              v-for="subject in scope.row.subjects"
              :key="subject"
              size="mini"
              style="margin-right: 5px;"
            >
              {{ subject }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getDetailData"
      />
    </el-card>

    <!-- 成就系统 -->
    <el-card class="achievements-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span class="card-title">学习成就</span>
        <span style="float: right; color: #999;">已获得 {{ achievements.length }} 个成就</span>
      </div>
      
      <div class="achievements-grid">
        <div
          v-for="achievement in achievements"
          :key="achievement.id"
          class="achievement-item"
          :class="{ unlocked: achievement.unlocked }"
        >
          <div class="achievement-icon">
            <i :class="achievement.icon"></i>
          </div>
          <div class="achievement-content">
            <h4>{{ achievement.name }}</h4>
            <p>{{ achievement.description }}</p>
            <div v-if="achievement.unlocked" class="unlocked-date">
              获得于 {{ achievement.unlockDate }}
            </div>
            <div v-else class="progress-info">
              进度: {{ achievement.progress }}/{{ achievement.target }}
            </div>
          </div>
        </div>
      </div>
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
      
      // 趋势类型
      trendType: 'studyTime',
      
      // 核心指标
      metrics: [
        { key: 'totalStudyTime', label: '总学习时长', value: '0小时', icon: 'el-icon-time', color: '#409EFF', change: 12.5 },
        { key: 'tomatoCount', label: '番茄钟总数', value: '0个', icon: 'el-icon-timer', color: '#67C23A', change: 8.3 },
        { key: 'completedTasks', label: '完成任务数', value: '0个', icon: 'el-icon-check', color: '#E6A23C', change: -2.1 },
        { key: 'efficiencyScore', label: '平均效率', value: '0分', icon: 'el-icon-data-analysis', color: '#F56C6C', change: 5.7 }
      ],
      
      // 效率指标
      efficiencyMetrics: [
        { key: 'focus', label: '专注度', value: 0 },
        { key: 'completion', label: '完成率', value: 0 },
        { key: 'consistency', label: '连续性', value: 0 },
        { key: 'productivity', label: '产出比', value: 0 }
      ],
      
      // 成就数据
      achievements: [],
      
      // 详细数据
      detailData: [],
      tableLoading: false,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      
      // 图表实例
      trendChart: null,
      subjectChart: null,
      efficiencyChart: null,
      timeChart: null
    };
  },
  mounted() {
    this.initCharts();
    this.loadData();
    this.getDetailData();
    this.loadAchievements();
  },
  beforeDestroy() {
    this.disposeCharts();
  },
  methods: {
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
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['学习时长', '番茄钟数', '完成任务']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '学习时长',
            type: 'line',
            stack: '总量',
            data: [0, 0, 0, 0, 0, 0, 0],
            smooth: true
          },
          {
            name: '番茄钟数',
            type: 'line',
            stack: '总量',
            data: [0, 0, 0, 0, 0, 0, 0],
            smooth: true
          },
          {
            name: '完成任务',
            type: 'line',
            stack: '总量',
            data: [0, 0, 0, 0, 0, 0, 0],
            smooth: true
          }
        ]
      };
      this.trendChart.setOption(option);
    },
    
    /** 初始化学科分布图 */
    initSubjectChart() {
      this.subjectChart = echarts.init(this.$refs.subjectChart);
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '学科分布',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 0, name: '暂无数据' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
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
          data: [
            {
              value: [0, 0, 0, 0, 0],
              name: '当前水平'
            }
          ]
        }]
      };
      this.efficiencyChart.setOption(option);
    },
    
    /** 初始化时间分布图 */
    initTimeChart() {
      this.timeChart = echarts.init(this.$refs.timeChart);
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: ['6-8', '8-10', '10-12', '12-14', '14-16', '16-18', '18-20', '20-22']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [0, 0, 0, 0, 0, 0, 0, 0],
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
      this.loadChartsData();
    },
    
    /** 加载核心指标 */
    loadMetrics() {
      // 先从番茄钟统计获取番茄钟数据
      getTomatoStatistics().then(tomatoRes => {
        const tomatoData = tomatoRes.data || {};
        
        // 然后从学习统计获取其他数据
        getStudyStatistics().then(response => {
          const data = response.data || {};
          // study_hours 是分钟数
          const studyMinutes = data.study_hours || 0;
          this.metrics[0].value = this.formatTime(studyMinutes);
          // 番茄钟数量从番茄钟统计获取
          this.metrics[1].value = (tomatoData.totalCount || 0) + '个';
          // 完成任务数
          this.metrics[2].value = (data.completed_tasks || 0) + '个';
          // 平均效率
          this.metrics[3].value = (data.average_efficiency || 0) + '分';
        }).catch(() => {
          // 使用番茄钟数据
          this.metrics[0].value = '0分钟';
          this.metrics[1].value = (tomatoData.totalCount || 0) + '个';
          this.metrics[2].value = '0个';
          this.metrics[3].value = '0分';
        });
      }).catch(() => {
        // 新用户没有数据，显示默认值
        this.metrics = [
          { key: 'totalStudyTime', label: '总学习时长', value: '0分钟', icon: 'el-icon-time', color: '#409EFF', change: 0 },
          { key: 'tomatoCount', label: '番茄钟总数', value: '0个', icon: 'el-icon-timer', color: '#67C23A', change: 0 },
          { key: 'completedTasks', label: '完成任务数', value: '0个', icon: 'el-icon-check', color: '#E6A23C', change: 0 },
          { key: 'efficiencyScore', label: '平均效率', value: '0分', icon: 'el-icon-data-analysis', color: '#F56C6C', change: 0 }
        ];
      });
    },
    
    /** 加载图表数据 */
    loadChartsData() {
      // 这里应该调用相应的API获取图表数据
      // 暂时使用初始化时的模拟数据
    },
    
    /** 获取详细数据 */
    getDetailData() {
      this.tableLoading = true;
      // 新用户没有数据，显示空列表
      this.detailData = [];
      this.total = 0;
      this.tableLoading = false;
    },
    
    /** 加载成就数据 */
    loadAchievements() {
      getAchievements().then(response => {
        this.achievements = response.data || [];
      }).catch(() => {
        // 新用户没有成就
        this.achievements = [];
      });
    },
    
    /** 时间范围变化 */
    handleTimeRangeChange() {
      // 根据选择的时间范围更新dateRange
      const now = new Date();
      let startDate;
      
      switch (this.timeRange) {
        case '7day':
          startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
          break;
        case '30day':
          startDate = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);
          break;
        case '90day':
          startDate = new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000);
          break;
        case 'year':
          startDate = new Date(now.getFullYear(), 0, 1);
          break;
      }
      
      this.dateRange = [this.formatDate(startDate), this.formatDate(now)];
      this.refreshData();
    },
    
    /** 日期范围变化 */
    handleDateRangeChange() {
      this.refreshData();
    },
    
    /** 刷新数据 */
    refreshData() {
      this.loadData();
      this.getDetailData();
    },
    
    /** 导出数据 */
    exportData() {
      exportStudyData().then(response => {
        const blob = new Blob([response]);
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = `学习数据_${this.formatDate(new Date())}.xlsx`;
        link.click();
        URL.revokeObjectURL(link.href);
        this.$message.success('数据导出成功');
      }).catch(() => {
        this.$message.error('数据导出失败');
      });
    },
    
    // 工具方法
    formatDate(date) {
      return date.toISOString().split('T')[0];
    },
    
    formatTime(minutes) {
      if (minutes < 60) {
        return `${minutes}分钟`;
      }
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
          margin-bottom: 10px;
        }
        
        .metric-change {
          font-size: 0.9rem;
          font-weight: 500;
          
          &.positive {
            color: #67C23A;
          }
          
          &.negative {
            color: #F56C6C;
          }
        }
      }
    }
  }
  
  .chart-card, .efficiency-card, .time-distribution-card, .data-table-card, .achievements-card {
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
  
  .achievements-card {
    .achievements-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 20px;
      
      .achievement-item {
        display: flex;
        padding: 20px;
        border-radius: 12px;
        border: 2px dashed #ddd;
        transition: all 0.3s ease;
        
        &.unlocked {
          border-color: #409EFF;
          background: rgba(64, 158, 255, 0.05);
        }
        
        .achievement-icon {
          font-size: 2rem;
          color: #ddd;
          margin-right: 15px;
          min-width: 50px;
          display: flex;
          align-items: center;
          justify-content: center;
          
          .unlocked & {
            color: #409EFF;
          }
        }
        
        .achievement-content {
          flex: 1;
          
          h4 {
            font-size: 1.1rem;
            margin-bottom: 8px;
            color: #999;
            
            .unlocked & {
              color: #333;
            }
          }
          
          p {
            font-size: 0.9rem;
            color: #999;
            margin-bottom: 10px;
            line-height: 1.4;
            
            .unlocked & {
              color: #666;
            }
          }
          
          .unlocked-date {
            font-size: 0.8rem;
            color: #409EFF;
            font-weight: 500;
          }
          
          .progress-info {
            font-size: 0.8rem;
            color: #999;
          }
        }
      }
    }
  }
  
  // 响应式设计
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
    
    .achievements-grid {
      grid-template-columns: 1fr !important;
    }
  }
}
</style>