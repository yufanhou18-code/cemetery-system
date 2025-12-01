import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as apiLogin, logout as apiLogout, getUserInfo } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const permissions = ref([])
  const roles = ref([])

  // 登录
  const login = async (loginForm) => {
    try {
      const res = await apiLogin(loginForm)
      if (res.code === 200) {
        token.value = res.data.token
        localStorage.setItem('token', res.data.token)
        console.log('✓ 登录成功')
        return true
      }
      return false
    } catch (error) {
      console.error('登录失败:', error)
      return false
    }
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const res = await getUserInfo()
      if (res.code === 200) {
        userInfo.value = res.data
        permissions.value = res.data.permissions || []
        roles.value = res.data.roles || []
        localStorage.setItem('userInfo', JSON.stringify(res.data))
        console.log('✓ 获取用户信息成功')
        return true
      }
      return false
    } catch (error) {
      console.error('获取用户信息失败:', error)
      return false
    }
  }

  // 登出
  const logout = async () => {
    try {
      await apiLogout()
    } finally {
      token.value = ''
      userInfo.value = {}
      permissions.value = []
      roles.value = []
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  }

  // 检查权限
  const hasPermission = (permission) => {
    return permissions.value.includes(permission) || roles.value.includes('ADMIN')
  }

  // 检查角色
  const hasRole = (role) => {
    return roles.value.includes(role)
  }

  return {
    token,
    userInfo,
    permissions,
    roles,
    login,
    logout,
    fetchUserInfo,
    hasPermission,
    hasRole
  }
})
