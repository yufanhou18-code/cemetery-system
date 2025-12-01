<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <!-- 左侧：个人信息卡片 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="profile-card">
            <div class="avatar-section">
              <el-avatar :size="120" :src="userInfo.avatar">
                <el-icon :size="60"><User /></el-icon>
              </el-avatar>
              <el-button type="primary" size="small" class="upload-btn" @click="handleUploadAvatar">
                <el-icon><Camera /></el-icon>
                更换头像
              </el-button>
            </div>
            <div class="user-info">
              <h2>{{ userInfo.realName || userInfo.username }}</h2>
              <p class="user-role">{{ userInfo.role || '管理员' }}</p>
              <div class="user-stats">
                <div class="stat-item">
                  <div class="stat-value">{{ userInfo.loginCount || 0 }}</div>
                  <div class="stat-label">登录次数</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ userInfo.lastLoginDays || 0 }}</div>
                  <div class="stat-label">连续登录</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <el-card shadow="hover" style="margin-top: 20px">
          <template #header>
            <span>账号安全</span>
          </template>
          <div class="security-items">
            <div class="security-item">
              <div class="security-left">
                <el-icon color="#67c23a"><CircleCheck /></el-icon>
                <span>登录密码</span>
              </div>
              <el-button type="text" @click="showChangePasswordDialog = true">修改</el-button>
            </div>
            <div class="security-item">
              <div class="security-left">
                <el-icon color="#e6a23c"><Warning /></el-icon>
                <span>手机绑定</span>
              </div>
              <el-button type="text">绑定</el-button>
            </div>
            <div class="security-item">
              <div class="security-left">
                <el-icon color="#e6a23c"><Warning /></el-icon>
                <span>邮箱绑定</span>
              </div>
              <el-button type="text">绑定</el-button>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：详细信息 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
              <el-button type="primary" size="small" @click="handleEdit">
                <el-icon><Edit /></el-icon>
                编辑资料
              </el-button>
            </div>
          </template>
          <el-form :model="form" label-width="100px" :disabled="!isEditing">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名">
                  <el-input v-model="form.username" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="真实姓名">
                  <el-input v-model="form.realName" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="性别">
                  <el-select v-model="form.gender" placeholder="请选择性别">
                    <el-option label="男" :value="1" />
                    <el-option label="女" :value="2" />
                    <el-option label="保密" :value="0" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号">
                  <el-input v-model="form.phone" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱">
                  <el-input v-model="form.email" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="部门">
                  <el-input v-model="form.department" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="职位">
                  <el-input v-model="form.position" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="创建时间">
                  <el-input v-model="form.createTime" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="个人简介">
                  <el-input v-model="form.bio" type="textarea" :rows="3" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item v-if="isEditing">
              <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
              <el-button @click="handleCancel">取消</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="hover" style="margin-top: 20px">
          <template #header>
            <span>登录日志</span>
          </template>
          <el-table :data="loginLogs" style="width: 100%">
            <el-table-column prop="loginTime" label="登录时间" width="180" />
            <el-table-column prop="ip" label="IP地址" width="150" />
            <el-table-column prop="location" label="登录地点" />
            <el-table-column prop="device" label="设备" width="120" />
            <el-table-column label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'success' ? 'success' : 'danger'">
                  {{ scope.row.status === 'success' ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="showChangePasswordDialog" title="修改密码" width="500px">
      <el-form :model="passwordForm" label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showChangePasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword" :loading="changingPassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const userStore = useUserStore()
const userInfo = ref(userStore.userInfo || {})

const isEditing = ref(false)
const saving = ref(false)
const showChangePasswordDialog = ref(false)
const changingPassword = ref(false)

const form = reactive({
  username: '',
  realName: '',
  gender: 0,
  phone: '',
  email: '',
  department: '',
  position: '',
  createTime: '',
  bio: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 登录日志模拟数据
const loginLogs = ref([
  {
    loginTime: dayjs().subtract(1, 'hour').format('YYYY-MM-DD HH:mm:ss'),
    ip: '192.168.1.100',
    location: '北京市',
    device: 'Chrome',
    status: 'success'
  },
  {
    loginTime: dayjs().subtract(1, 'day').format('YYYY-MM-DD HH:mm:ss'),
    ip: '192.168.1.101',
    location: '上海市',
    device: 'Firefox',
    status: 'success'
  },
  {
    loginTime: dayjs().subtract(2, 'day').format('YYYY-MM-DD HH:mm:ss'),
    ip: '192.168.1.102',
    location: '广州市',
    device: 'Safari',
    status: 'success'
  }
])

// 加载用户信息
const loadUserInfo = () => {
  // 从store中获取用户信息
  const user = userStore.userInfo
  form.username = user.username || 'admin'
  form.realName = user.realName || '管理员'
  form.gender = user.gender || 0
  form.phone = user.phone || ''
  form.email = user.email || ''
  form.department = user.department || '技术部'
  form.position = user.position || '系统管理员'
  form.createTime = user.createTime || dayjs().format('YYYY-MM-DD HH:mm:ss')
  form.bio = user.bio || '这个人很懒，什么都没留下...'
  
  userInfo.value = {
    ...user,
    loginCount: user.loginCount || 156,
    lastLoginDays: user.lastLoginDays || 7
  }
}

// 编辑
const handleEdit = () => {
  isEditing.value = true
}

// 取消编辑
const handleCancel = () => {
  isEditing.value = false
  loadUserInfo()
}

// 保存
const handleSave = async () => {
  saving.value = true
  try {
    // TODO: 调用API保存用户信息
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('保存成功')
    isEditing.value = false
    
    // 更新store中的用户信息
    userStore.userInfo = { ...userStore.userInfo, ...form }
    localStorage.setItem('userInfo', JSON.stringify(userStore.userInfo))
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 上传头像
const handleUploadAvatar = () => {
  ElMessage.info('上传头像功能开发中...')
}

// 修改密码
const handleChangePassword = async () => {
  if (!passwordForm.oldPassword || !passwordForm.newPassword || !passwordForm.confirmPassword) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次密码输入不一致')
    return
  }
  
  if (passwordForm.newPassword.length < 6) {
    ElMessage.warning('密码长度至少6位')
    return
  }
  
  changingPassword.value = true
  try {
    // TODO: 调用API修改密码
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('密码修改成功，请重新登录')
    showChangePasswordDialog.value = false
    
    // 清空表单
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    ElMessage.error('密码修改失败')
  } finally {
    changingPassword.value = false
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

.profile-card {
  text-align: center;
}

.avatar-section {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.upload-btn {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  opacity: 0;
  transition: opacity 0.3s;
}

.avatar-section:hover .upload-btn {
  opacity: 1;
}

.user-info h2 {
  margin: 10px 0;
  font-size: 24px;
  color: #303133;
}

.user-role {
  color: #909399;
  font-size: 14px;
  margin-bottom: 20px;
}

.user-stats {
  display: flex;
  justify-content: center;
  gap: 40px;
  padding: 20px 0;
  border-top: 1px solid #ebeef5;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.security-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.security-item:last-child {
  border-bottom: none;
}

.security-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
