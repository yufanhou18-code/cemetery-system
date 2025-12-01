<template>
  <div class="main-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
        <div class="logo-container">
          <el-icon v-if="!isCollapse" class="logo-icon-main"><Location /></el-icon>
          <span v-if="!isCollapse" class="logo-text">陵园管家</span>
          <el-icon v-else class="logo-icon"><Location /></el-icon>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :unique-opened="true"
          background-color="#2c3e50"
          text-color="#ecf0f1"
          active-text-color="#3498db"
          router
        >
          <template v-for="route in menuRoutes" :key="route.path">
            <el-menu-item
              v-if="!route.children || route.children.length === 0"
              :index="'/' + route.path"
            >
              <el-icon><component :is="route.meta.icon" /></el-icon>
              <template #title>{{ route.meta.title }}</template>
            </el-menu-item>
            
            <el-sub-menu v-else :index="route.path">
              <template #title>
                <el-icon><component :is="route.meta.icon" /></el-icon>
                <span>{{ route.meta.title }}</span>
              </template>
              <el-menu-item
                v-for="child in route.children"
                :key="child.path"
                :index="'/' + route.path + '/' + child.path"
              >
                {{ child.meta.title }}
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container class="main-container">
        <!-- 顶部导航栏 -->
        <el-header class="header">
          <div class="header-left">
            <el-icon class="collapse-btn" @click="toggleCollapse">
              <Expand v-if="isCollapse" />
              <Fold v-else />
            </el-icon>
            <el-breadcrumb separator="/">
              <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="{ path: item.path }">
                {{ item.meta.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <el-badge :value="3" class="notification-badge">
              <el-icon class="header-icon"><Bell /></el-icon>
            </el-badge>
            
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="32" :src="userStore.userInfo.avatar" >
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span class="username">{{ userStore.userInfo.realName || userStore.userInfo.username || 'Admin' }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>系统设置
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 内容区域 -->
        <el-main class="main-content">
          <router-view v-slot="{ Component }">
            <transition name="fade-transform" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const appStore = useAppStore()

const isCollapse = computed(() => appStore.isCollapse)

// 菜单路由
const menuRoutes = computed(() => {
  const routes = router.getRoutes()
  return routes
    .find(r => r.path === '/')
    ?.children.filter(r => r.meta && r.meta.title && r.meta.icon) || []
})

// 当前激活菜单
const activeMenu = computed(() => {
  const { path } = route
  return path
})

// 面包屑
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched
})

// 切换侧边栏
const toggleCollapse = () => {
  appStore.toggleCollapse()
}

// 下拉菜单命令
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userStore.logout()
        router.push('/login')
        ElMessage.success('退出成功')
      }).catch(() => {})
      break
  }
}
</script>

<style lang="scss" scoped>
.main-layout {
  height: 100vh;
  background: #f0f2f5;

  .el-container {
    height: 100%;
  }

  .sidebar {
    background: #2c3e50;
    box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
    transition: width 0.3s;
    overflow-x: hidden;

    .logo-container {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 20px;
      background: #1a252f;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);

      .logo-icon-main {
        font-size: 32px;
        color: #3498db;
        margin-right: 12px;
      }

      .logo-text {
        font-size: 18px;
        font-weight: 600;
        color: #ecf0f1;
        letter-spacing: 1px;
      }

      .logo-icon {
        font-size: 28px;
        color: #3498db;
      }
    }

    .el-menu {
      border-right: none;
      
      :deep(.el-menu-item),
      :deep(.el-sub-menu__title) {
        &:hover {
          background-color: rgba(52, 152, 219, 0.1) !important;
        }

        &.is-active {
          background-color: rgba(52, 152, 219, 0.2) !important;
          border-right: 3px solid #3498db;
        }
      }
    }
  }

  .main-container {
    display: flex;
    flex-direction: column;
  }

  .header {
    background: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    z-index: 999;

    .header-left {
      display: flex;
      align-items: center;
      gap: 20px;

      .collapse-btn {
        font-size: 20px;
        cursor: pointer;
        transition: color 0.3s;

        &:hover {
          color: #3498db;
        }
      }

      .el-breadcrumb {
        font-size: 14px;
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 20px;

      .notification-badge {
        cursor: pointer;

        .header-icon {
          font-size: 20px;
          color: #606266;
          transition: color 0.3s;

          &:hover {
            color: #3498db;
          }
        }
      }

      .user-info {
        display: flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        padding: 5px 10px;
        border-radius: 4px;
        transition: background-color 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        .username {
          font-size: 14px;
          color: #303133;
        }

        .el-icon {
          color: #909399;
        }
      }
    }
  }

  .main-content {
    background: #f0f2f5;
    padding: 20px;
    overflow-y: auto;
  }
}

// 过渡动画
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
