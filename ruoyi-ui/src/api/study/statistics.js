import request from '@/utils/request'

// 获取学习统计数据
export function getStudyStatistics() {
  return request({
    url: '/study/statistics',
    method: 'get'
  })
}

// 获取学习趋势数据
export function getStudyTrends(query) {
  return request({
    url: '/study/statistics/trends',
    method: 'get',
    params: query
  })
}

// 获取学科分布数据
export function getSubjectDistribution() {
  return request({
    url: '/study/statistics/subject-distribution',
    method: 'get'
  })
}

// 获取时间分布数据
export function getTimeDistribution() {
  return request({
    url: '/study/statistics/time-distribution',
    method: 'get'
  })
}

// 获取效率分析数据
export function getEfficiencyAnalysis() {
  return request({
    url: '/study/statistics/efficiency',
    method: 'get'
  })
}

// 获取成就数据
export function getAchievements() {
  return request({
    url: '/study/statistics/achievements',
    method: 'get'
  })
}

// 获取学习排行榜
export function getStudyRankings() {
  return request({
    url: '/study/statistics/rankings',
    method: 'get'
  })
}

// 导出学习数据
export function exportStudyData() {
  return request({
    url: '/study/statistics/export',
    method: 'get',
    responseType: 'blob'
  })
}