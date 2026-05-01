import request from '@/utils/request'

// 获取学习统计汇总（今日数据）
export function getStudyStatistics() {
  return request({
    url: '/study/statistics/summary',
    method: 'get'
  })
}

// 获取学习趋势数据
export function getStudyTrends(days) {
  return request({
    url: '/study/statistics/trend',
    method: 'get',
    params: { days }
  })
}

// 获取学科分布数据
export function getSubjectDistribution(startDate, endDate) {
  return request({
    url: '/study/statistics/distribution',
    method: 'get',
    params: { startDate, endDate }
  })
}

// 获取近期7天统计数据
export function getRecentStatistics() {
  return request({
    url: '/study/statistics/recent',
    method: 'get'
  })
}

// 获取效率评分
export function getProductivityScore(studyDate) {
  return request({
    url: '/study/statistics/productivity',
    method: 'get',
    params: { studyDate }
  })
}

// 获取学习统计列表
export function listStudyStatistics(query) {
  return request({
    url: '/study/statistics/list',
    method: 'get',
    params: query
  })
}

// 导出学习数据
export function exportStudyData() {
  return request({
    url: '/study/statistics/export',
    method: 'post',
    responseType: 'blob'
  })
}

// 获取时间分布数据（按小时）
export function getTimeDistribution(startDate, endDate) {
  return request({
    url: '/study/statistics/time-distribution',
    method: 'get',
    params: { startDate, endDate }
  })
}

// 获取效率分析数据
export function getEfficiencyAnalysis(studyDate) {
  return request({
    url: '/study/statistics/productivity',
    method: 'get',
    params: { studyDate }
  })
}

// 获取成就数据
export function getAchievements() {
  return request({
    url: '/study/statistics/achievements',
    method: 'get'
  })
}
