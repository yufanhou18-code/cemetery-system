import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'DataAnalysis' }
      },
      {
        path: 'tomb',
        name: 'Tomb',
        component: () => import('@/views/tomb/Index.vue'),
        redirect: '/tomb/list',
        meta: { title: '墓位管理', icon: 'Location' },
        children: [
          {
            path: 'list',
            name: 'TombList',
            component: () => import('@/views/tomb/List.vue'),
            meta: { title: '墓位列表' }
          }
        ]
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/order/Index.vue'),
        redirect: '/order/list',
        meta: { title: '订单管理', icon: 'Document' },
        children: [
          {
            path: 'list',
            name: 'OrderList',
            component: () => import('@/views/order/List.vue'),
            meta: { title: '订单列表' }
          }
        ]
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/Index.vue'),
        redirect: '/user/list',
        meta: { title: '用户管理', icon: 'User' },
        children: [
          {
            path: 'list',
            name: 'UserList',
            component: () => import('@/views/user/List.vue'),
            meta: { title: '用户列表' }
          }
        ]
      },
      {
        path: 'memorial',
        name: 'Memorial',
        component: () => import('@/views/memorial/Index.vue'),
        redirect: '/memorial/list',
        meta: { title: '数字纪念空间', icon: 'Collection' },
        children: [
          {
            path: 'list',
            name: 'MemorialList',
            component: () => import('@/views/memorial/List.vue'),
            meta: { title: '纪念空间列表' }
          }
        ]
      },
      {
        path: 'provider',
        name: 'Provider',
        component: () => import('@/views/provider/Index.vue'),
        redirect: '/provider/list',
        meta: { title: '服务商管理', icon: 'Shop' },
        children: [
          {
            path: 'list',
            name: 'ProviderList',
            component: () => import('@/views/provider/List.vue'),
            meta: { title: '服务商列表' }
          }
        ]
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue'),
        meta: { title: '系统设置' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/404.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token

  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
