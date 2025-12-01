import request from '@/utils/request'

// 分页查询墓位
export function getTombPage(params) {
  return request({
    url: '/tomb/page',
    method: 'get',
    params
  })
}

// 获取墓位详情
export function getTombDetail(id) {
  return request({
    url: `/tomb/${id}`,
    method: 'get'
  })
}

// 创建墓位
export function createTomb(data) {
  return request({
    url: '/tomb',
    method: 'post',
    data
  })
}

// 更新墓位
export function updateTomb(data) {
  return request({
    url: '/tomb',
    method: 'put',
    data
  })
}

// 删除墓位
export function deleteTomb(id) {
  return request({
    url: `/tomb/${id}`,
    method: 'delete'
  })
}

// 搜索墓位
export function searchTomb(params) {
  return request({
    url: '/tomb/search',
    method: 'get',
    params
  })
}
