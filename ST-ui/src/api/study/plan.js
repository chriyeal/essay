import request from '@/utils/request'

// 查询学习计划列表
export function listStudyPlan(query) {
  return request({
    url: '/study/plan/list',
    method: 'get',
    params: query
  })
}

// 查询学习计划详细
export function getStudyPlan(planId) {
  return request({
    url: '/study/plan/' + planId,
    method: 'get'
  })
}

// 新增学习计划
export function addStudyPlan(data) {
  return request({
    url: '/study/plan',
    method: 'post',
    data: data
  })
}

// 修改学习计划
export function updateStudyPlan(data) {
  return request({
    url: '/study/plan',
    method: 'put',
    data: data
  })
}

// 删除学习计划
export function delStudyPlan(planId) {
  return request({
    url: '/study/plan/' + planId,
    method: 'delete'
  })
}

// 完成学习计划
export function completeStudyPlan(planId) {
  return request({
    url: '/study/plan/complete/' + planId,
    method: 'put'
  })
}

// 获取计划统计信息
export function getPlanStatistics() {
  return request({
    url: '/study/plan/statistics',
    method: 'get'
  })
}

// 获取计划统计汇总
export function getPlanSummary() {
  return request({
    url: '/study/plan/summary',
    method: 'get'
  })
}

// 生成智能学习计划
export function generateSmartPlan(data) {
  return request({
    url: '/study/plan/generate',
    method: 'post',
    data: data
  })
}