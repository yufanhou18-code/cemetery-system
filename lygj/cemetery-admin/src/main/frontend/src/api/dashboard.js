import request from '@/utils/request'

// 获取纪念空间整体概况
export function getMemorialOverview() {
  return request({
    url: '/memorial/statistics/overview',
    method: 'get'
  })
}

// 获取服务商统计
export function getProviderStatistics() {
  return request({
    url: '/service-provider/statistics',
    method: 'get'
  })
}

// 获取访问趋势
export function getVisitTrend(params) {
  return request({
    url: '/memorial/statistics/visit-trend',
    method: 'get',
    params
  })
}

// 获取内容类型分布
export function getContentTypeDistribution(memorialId) {
  return request({
    url: '/memorial/statistics/content-type-distribution',
    method: 'get',
    params: { memorialId }
  })
}

// 获取热门纪念空间排行
export function getTopMemorials(params) {
  return request({
    url: '/memorial/statistics/top-memorials',
    method: 'get',
    params
  })
}

// 获取实时统计数据
export function getRealTimeStatistics() {
  return request({
    url: '/memorial/statistics/real-time',
    method: 'get'
  })
}
