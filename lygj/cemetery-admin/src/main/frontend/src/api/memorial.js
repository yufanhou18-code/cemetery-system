import request from '@/utils/request'

// 分页查询纪念空间
export function getMemorialPage(params) {
  return request({
    url: '/memorial-space/page',
    method: 'get',
    params
  })
}

// 获取纪念空间详情
export function getMemorialDetail(id) {
  return request({
    url: `/memorial-space/${id}`,
    method: 'get'
  })
}

// 创建纪念空间
export function createMemorial(data) {
  return request({
    url: '/memorial-space',
    method: 'post',
    data
  })
}

// 更新纪念空间
export function updateMemorial(data) {
  return request({
    url: '/memorial-space',
    method: 'put',
    data
  })
}

// 删除纪念空间
export function deleteMemorial(id) {
  return request({
    url: `/memorial-space/${id}`,
    method: 'delete'
  })
}

// 搜索纪念空间
export function searchMemorial(params) {
  return request({
    url: '/memorial-space/search',
    method: 'get',
    params
  })
}
