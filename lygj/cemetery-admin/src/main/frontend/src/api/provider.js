import request from '@/utils/request'

// 分页查询服务商
export function getProviderPage(params) {
  return request({
    url: '/service-provider/page',
    method: 'get',
    params
  })
}

// 获取服务商详情
export function getProviderDetail(id) {
  return request({
    url: `/service-provider/${id}`,
    method: 'get'
  })
}

// 创建服务商
export function createProvider(data) {
  return request({
    url: '/service-provider',
    method: 'post',
    data
  })
}

// 更新服务商
export function updateProvider(data) {
  return request({
    url: '/service-provider',
    method: 'put',
    data
  })
}

// 删除服务商
export function deleteProvider(id) {
  return request({
    url: `/service-provider/${id}`,
    method: 'delete'
  })
}

// 搜索服务商
export function searchProvider(params) {
  return request({
    url: '/service-provider/search',
    method: 'get',
    params
  })
}
