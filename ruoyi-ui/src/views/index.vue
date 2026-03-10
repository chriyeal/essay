<template>
  <div class="app-container home">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-content">
        <div class="welcome-text">
          <h1 class="welcome-title">
            <span class="zh-title">欢迎来到个性化学习计划管理系统</span>
            <span class="en-title">Welcome to Personalized Learning Plan Management System</span>
          </h1>
          <p class="welcome-subtitle">
            <span class="zh-subtitle">让学习更高效，让成长更简单</span>
            <span class="en-subtitle">Make Learning More Efficient, Make Growth Simpler</span>
          </p>
        </div>
        <div class="welcome-stats">
          <div class="stat-item">
            <div class="stat-number">{{ studyStats.planCount || 0 }}</div>
            <div class="stat-label">学习计划</div>
            <div class="stat-label-en">Learning Plans</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ studyStats.studyHours || 0 }}</div>
            <div class="stat-label">学习时长(小时)</div>
            <div class="stat-label-en">Study Hours</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ studyStats.completedTasks || 0 }}</div>
            <div class="stat-label">完成任务</div>
            <div class="stat-label-en">Completed Tasks</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="quick-actions">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6" v-for="item in quickActions" :key="item.title">
          <div class="action-card" @click="handleQuickAction(item.path)">
            <div class="card-icon" :style="{ backgroundColor: item.color }">
              <i :class="item.icon"></i>
            </div>
            <div class="card-content">
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能特色 -->
    <div class="features-section">
      <h2 class="section-title">核心功能 / Core Features</h2>
      <el-row :gutter="40" class="feature-row">
        <el-col :xs="24" :sm="12" :md="8" v-for="feature in features" :key="feature.title">
          <div class="feature-card">
            <div class="feature-icon" :style="{ color: feature.color }">
              <i :class="feature.icon"></i>
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
            <p class="feature-desc-en">{{ feature.descEn }}</p>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getStudyStatistics } from '@/api/study/statistics'

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "1.0.0",
      // 学习统计数据
      studyStats: {
        planCount: 0,
        studyHours: 0,
        completedTasks: 0
      },
      // 快捷入口
      quickActions: [
        {
          title: '学习计划',
          description: '制定和管理学习计划',
          icon: 'el-icon-notebook-2',
          color: '#409EFF',
          path: '/study/plan'
        },
        {
          title: '番茄钟',
          description: '专注学习计时器',
          icon: 'el-icon-timer',
          color: '#67C23A',
          path: '/study/tomato'
        },
        {
          title: '学习统计',
          description: '查看学习数据分析',
          icon: 'el-icon-data-analysis',
          color: '#E6A23C',
          path: '/study/statistics'
        },
        {
          title: '个人中心',
          description: '管理个人信息设置',
          icon: 'el-icon-user',
          color: '#F56C6C',
          path: '/user/profile'
        }
      ],
      // 功能特色
      features: [
        {
          title: '智能计划制定',
          description: '根据学习目标自动生成个性化学习计划',
          descEn: 'Automatically generate personalized learning plans based on learning goals',
          icon: 'el-icon-magic-stick',
          color: '#409EFF'
        },
        {
          title: '专注力训练',
          description: '番茄钟技术帮助提升学习专注度',
          descEn: 'Pomodoro technique to improve learning focus',
          icon: 'el-icon-alarm-clock',
          color: '#67C23A'
        },
        {
          title: '数据可视化',
          description: '直观展示学习进度和效果分析',
          descEn: 'Visualize learning progress and effectiveness analysis',
          icon: 'el-icon-pie-chart',
          color: '#E6A23C'
        },
        {
          title: '个性化推荐',
          description: '基于学习行为智能推荐相关内容',
          descEn: 'Intelligently recommend relevant content based on learning behavior',
          icon: 'el-icon-lightning',
          color: '#F56C6C'
        },
        {
          title: '进度追踪',
          description: '实时监控学习进度和完成情况',
          descEn: 'Real-time monitoring of learning progress and completion',
          icon: 'el-icon-odometer',
          color: '#909399'
        },
        {
          title: '成就系统',
          description: '激励学习动力的成就和奖励机制',
          descEn: 'Achievement and reward system to motivate learning',
          icon: 'el-icon-medal',
          color: '#FF69B4'
        }
      ]
    }
  },
  created() {
    this.loadStudyStatistics()
  },
  methods: {
    // 加载学习统计数据
    loadStudyStatistics() {
      getStudyStatistics().then(response => {
        this.studyStats = response.data || {}
      }).catch(() => {
        // 如果获取失败，使用默认值
        this.studyStats = {
          planCount: 12,
          studyHours: 45,
          completedTasks: 86
        }
      })
    },
    // 处理快捷入口点击
    handleQuickAction(path) {
      this.$router.push(path)
    },
    goTarget(href) {
      window.open(href, "_blank")
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  padding: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: calc(100vh - 84px);
  
  // 欢迎横幅
  .welcome-banner {
    background: linear-gradient(120deg, #2c3e50 0%, #4a6478 100%);
    color: white;
    padding: 60px 40px;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: -50%;
      right: -50%;
      width: 100%;
      height: 200%;
      background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
      transform: rotate(30deg);
    }
    
    .banner-content {
      position: relative;
      z-index: 2;
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 30px;
      
      @media (max-width: 768px) {
        flex-direction: column;
        text-align: center;
      }
    }
    
    .welcome-text {
      flex: 1;
      min-width: 300px;
      
      .welcome-title {
        font-size: 2.5rem;
        font-weight: 300;
        margin-bottom: 20px;
        line-height: 1.3;
        
        .zh-title {
          display: block;
          margin-bottom: 10px;
        }
        
        .en-title {
          display: block;
          font-size: 1.2rem;
          font-weight: 200;
          opacity: 0.9;
        }
      }
      
      .welcome-subtitle {
        font-size: 1.2rem;
        opacity: 0.85;
        line-height: 1.6;
        
        .zh-subtitle {
          display: block;
          margin-bottom: 8px;
        }
        
        .en-subtitle {
          display: block;
          font-size: 1rem;
          font-weight: 300;
          opacity: 0.7;
        }
      }
    }
    
    .welcome-stats {
      display: flex;
      gap: 30px;
      
      @media (max-width: 768px) {
        width: 100%;
        justify-content: center;
      }
      
      .stat-item {
        text-align: center;
        background: rgba(255, 255, 255, 0.1);
        padding: 20px;
        border-radius: 12px;
        backdrop-filter: blur(10px);
        min-width: 120px;
        
        .stat-number {
          font-size: 2.2rem;
          font-weight: 600;
          margin-bottom: 8px;
          color: #409EFF;
        }
        
        .stat-label {
          font-size: 1rem;
          margin-bottom: 4px;
          opacity: 0.9;
        }
        
        .stat-label-en {
          font-size: 0.8rem;
          opacity: 0.7;
          font-weight: 300;
        }
      }
    }
  }
  
  // 快捷入口
  .quick-actions {
    padding: 50px 40px;
    background: white;
    
    .action-card {
      background: white;
      border-radius: 16px;
      padding: 30px 20px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      border: 1px solid #f0f0f0;
      height: 100%;
      
      &:hover {
        transform: translateY(-8px);
        box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
        border-color: #409EFF;
      }
      
      .card-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 20px;
        color: white;
        font-size: 24px;
        transition: all 0.3s ease;
      }
      
      .card-content {
        h3 {
          font-size: 1.3rem;
          margin-bottom: 12px;
          color: #333;
          font-weight: 500;
        }
        
        p {
          color: #666;
          font-size: 0.95rem;
          line-height: 1.5;
        }
      }
    }
  }
  
  // 功能特色
  .features-section {
    padding: 80px 60px;
    background: #f8f9fa;
    
    .section-title {
      text-align: center;
      font-size: 2rem;
      font-weight: 300;
      margin-bottom: 60px;
      color: #333;
      position: relative;
      
      &::after {
        content: '';
        position: absolute;
        bottom: -15px;
        left: 50%;
        transform: translateX(-50%);
        width: 60px;
        height: 3px;
        background: linear-gradient(90deg, #409EFF, #67C23A);
        border-radius: 2px;
      }
    }
    
    .feature-row {
      display: flex;
      flex-wrap: wrap;
    }
    
    .feature-card {
      background: white;
      border-radius: 20px;
      padding: 50px 35px;
      text-align: center;
      transition: all 0.3s ease;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
      height: 100%;
      margin-bottom: 40px;
      
      &:hover {
        transform: translateY(-10px);
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
      }
      
      .feature-icon {
        font-size: 3.5rem;
        margin-bottom: 30px;
      }
      
      h3 {
        font-size: 1.5rem;
        margin-bottom: 20px;
        color: #333;
        font-weight: 500;
      }
      
      p {
        color: #666;
        font-size: 1rem;
        line-height: 1.8;
        margin-bottom: 12px;
      }
      
      .feature-desc-en {
        color: #999;
        font-size: 0.9rem;
        font-weight: 300;
        font-style: italic;
      }
    }
  }
  
  // 联系我们
  .contact-section {
    padding: 60px 40px;
    background: white;
    
    .section-title {
      text-align: center;
      font-size: 2rem;
      font-weight: 300;
      margin-bottom: 50px;
      color: #333;
    }
    
    .contact-card {
      background: #f8f9fa;
      border-radius: 16px;
      padding: 40px 30px;
      text-align: center;
      transition: all 0.3s ease;
      border: 1px solid #e9ecef;
      
      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        border-color: #409EFF;
      }
      
      .contact-icon {
        font-size: 2.5rem;
        color: #409EFF;
        margin-bottom: 20px;
      }
      
      h3 {
        font-size: 1.3rem;
        margin-bottom: 10px;
        color: #333;
      }
      
      p {
        color: #666;
        font-size: 0.95rem;
        margin-bottom: 20px;
      }
      
      a {
        color: #409EFF;
        text-decoration: none;
        font-weight: 500;
        
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
  
  // 系统信息
  .system-info {
    padding: 60px 40px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    
    .info-card {
      background: rgba(255, 255, 255, 0.1);
      border-radius: 16px;
      padding: 40px 30px;
      text-align: center;
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255, 255, 255, 0.2);
      
      h3 {
        font-size: 1.4rem;
        margin-bottom: 10px;
        font-weight: 500;
      }
      
      p {
        color: rgba(255, 255, 255, 0.8);
        font-size: 0.95rem;
        margin-bottom: 20px;
      }
      
      .version-tag {
        display: inline-block;
        background: rgba(64, 158, 255, 0.3);
        color: white;
        padding: 8px 20px;
        border-radius: 20px;
        font-weight: 500;
        font-size: 1.1rem;
      }
      
      .update-list {
        text-align: left;
        max-width: 250px;
        margin: 0 auto;
        
        li {
          margin-bottom: 10px;
          font-size: 0.9rem;
          color: rgba(255, 255, 255, 0.9);
          position: relative;
          padding-left: 15px;
          
          &::before {
            content: '•';
            position: absolute;
            left: 0;
            color: #409EFF;
          }
        }
      }
      
      .about-content {
        p {
          margin-bottom: 15px;
          line-height: 1.6;
        }
        
        .en-about {
          font-size: 0.85rem;
          font-style: italic;
          opacity: 0.8;
        }
      }
    }
  }
  
  // 响应式设计
  @media (max-width: 768px) {
    .welcome-banner {
      padding: 40px 20px;
      
      .welcome-text .welcome-title {
        font-size: 1.8rem;
      }
      
      .welcome-stats {
        gap: 15px;
        
        .stat-item {
          min-width: 90px;
          padding: 15px;
          
          .stat-number {
            font-size: 1.8rem;
          }
        }
      }
    }
    
    .quick-actions, .features-section, .contact-section, .system-info {
      padding: 40px 20px;
    }
    
    .section-title {
      font-size: 1.6rem !important;
    }
  }
}
</style>

