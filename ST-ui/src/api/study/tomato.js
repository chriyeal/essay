import request from '@/utils/request'

// 查询番茄钟记录列表
export function listTomatoRecord(query) {
  return request({
    url: '/study/tomato/list',
    method: 'get',
    params: query
  })
}

// 查询番茄钟记录详细
export function getTomatoRecord(recordId) {
  return request({
    url: '/study/tomato/' + recordId,
    method: 'get'
  })
}

// 新增番茄钟记录
export function addTomatoRecord(data) {
  return request({
    url: '/study/tomato',
    method: 'post',
    data: data
  })
}

// 修改番茄钟记录
export function updateTomatoRecord(data) {
  return request({
    url: '/study/tomato',
    method: 'put',
    data: data
  })
}

// 删除番茄钟记录
export function delTomatoRecord(recordId) {
  return request({
    url: '/study/tomato/' + recordId,
    method: 'delete'
  })
}

// 开始番茄钟
export function startTomato(data) {
  return request({
    url: '/study/tomato/start',
    method: 'post',
    data: data
  })
}

// 暂停番茄钟
export function pauseTomato(recordId) {
  return request({
    url: '/study/tomato/pause/' + recordId,
    method: 'put'
  })
}

// 恢复番茄钟
export function resumeTomato(recordId) {
  return request({
    url: '/study/tomato/resume/' + recordId,
    method: 'put'
  })
}

// 完成番茄钟
export function completeTomato(recordId) {
  return request({
    url: '/study/tomato/complete/' + recordId,
    method: 'put'
  })
}

// 放弃番茄钟
export function abandonTomato(recordId) {
  return request({
    url: '/study/tomato/abandon/' + recordId,
    method: 'put'
  })
}

// 获取番茄钟统计信息
export function getTomatoStatistics() {
  return request({
    url: '/study/tomato/statistics',
    method: 'get'
  })
}

// 获取今日番茄钟记录
export function getTodayTomatoRecords() {
  return request({
    url: '/study/tomato/today',
    method: 'get'
  })
}

// 强制中断进行中的番茄钟
export function forceStopActiveTomato() {
  return request({
    url: '/study/tomato/force-stop',
    method: 'put'
  })
}