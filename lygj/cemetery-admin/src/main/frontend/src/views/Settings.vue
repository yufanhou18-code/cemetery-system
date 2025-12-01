<template>
  <div class="settings-container">
    <el-card shadow="hover">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基础设置 -->
        <el-tab-pane label="基础设置" name="basic">
          <el-form :model="basicForm" label-width="120px">
            <el-form-item label="系统名称">
              <el-input v-model="basicForm.systemName" />
            </el-form-item>
            <el-form-item label="系统Logo">
              <el-upload
                class="logo-uploader"
                action="#"
                :show-file-list="false"
                :auto-upload="false"
              >
                <img v-if="basicForm.logo" :src="basicForm.logo" class="logo" />
                <el-icon v-else class="logo-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="系统描述">
              <el-input v-model="basicForm.systemDesc" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item label="版权信息">
              <el-input v-model="basicForm.copyright" />
            </el-form-item>
            <el-form-item label="联系方式">
              <el-input v-model="basicForm.contact" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveBasic" :loading="saving">保存设置</el-button>
              <el-button @click="handleResetBasic">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 安全设置 -->
        <el-tab-pane label="安全设置" name="security">
          <el-form :model="securityForm" label-width="160px">
            <el-form-item label="密码复杂度要求">
              <el-switch v-model="securityForm.passwordComplexity" />
              <span class="form-tip">启用后需包含大小写字母、数字和特殊字符</span>
            </el-form-item>
            <el-form-item label="密码最小长度">
              <el-input-number v-model="securityForm.passwordMinLength" :min="6" :max="20" />
            </el-form-item>
            <el-form-item label="密码过期时间(天)">
              <el-input-number v-model="securityForm.passwordExpireDays" :min="0" :max="365" />
              <span class="form-tip">设置为0表示永不过期</span>
            </el-form-item>
            <el-form-item label="登录失败锁定">
              <el-switch v-model="securityForm.loginLockEnabled" />
            </el-form-item>
            <el-form-item label="失败次数阈值" v-if="securityForm.loginLockEnabled">
              <el-input-number v-model="securityForm.loginFailThreshold" :min="3" :max="10" />
            </el-form-item>
            <el-form-item label="锁定时长(分钟)" v-if="securityForm.loginLockEnabled">
              <el-input-number v-model="securityForm.lockDuration" :min="5" :max="120" />
            </el-form-item>
            <el-form-item label="会话超时(分钟)">
              <el-input-number v-model="securityForm.sessionTimeout" :min="10" :max="480" />
            </el-form-item>
            <el-form-item label="启用验证码">
              <el-switch v-model="securityForm.captchaEnabled" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveSecurity" :loading="saving">保存设置</el-button>
              <el-button @click="handleResetSecurity">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 通知设置 -->
        <el-tab-pane label="通知设置" name="notification">
          <el-form :model="notificationForm" label-width="140px">
            <el-form-item label="邮件通知">
              <el-switch v-model="notificationForm.emailEnabled" />
            </el-form-item>
            <el-form-item label="SMTP服务器" v-if="notificationForm.emailEnabled">
              <el-input v-model="notificationForm.smtpServer" />
            </el-form-item>
            <el-form-item label="SMTP端口" v-if="notificationForm.emailEnabled">
              <el-input-number v-model="notificationForm.smtpPort" :min="1" :max="65535" />
            </el-form-item>
            <el-form-item label="发件人邮箱" v-if="notificationForm.emailEnabled">
              <el-input v-model="notificationForm.senderEmail" />
            </el-form-item>
            <el-form-item label="邮箱密码" v-if="notificationForm.emailEnabled">
              <el-input v-model="notificationForm.emailPassword" type="password" show-password />
            </el-form-item>
            <el-divider />
            <el-form-item label="短信通知">
              <el-switch v-model="notificationForm.smsEnabled" />
            </el-form-item>
            <el-form-item label="短信平台" v-if="notificationForm.smsEnabled">
              <el-select v-model="notificationForm.smsProvider">
                <el-option label="阿里云" value="aliyun" />
                <el-option label="腾讯云" value="tencent" />
                <el-option label="华为云" value="huawei" />
              </el-select>
            </el-form-item>
            <el-form-item label="AccessKey" v-if="notificationForm.smsEnabled">
              <el-input v-model="notificationForm.smsAccessKey" />
            </el-form-item>
            <el-form-item label="SecretKey" v-if="notificationForm.smsEnabled">
              <el-input v-model="notificationForm.smsSecretKey" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveNotification" :loading="saving">保存设置</el-button>
              <el-button @click="handleResetNotification">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 存储设置 -->
        <el-tab-pane label="存储设置" name="storage">
          <el-form :model="storageForm" label-width="140px">
            <el-form-item label="存储方式">
              <el-radio-group v-model="storageForm.storageType">
                <el-radio label="local">本地存储</el-radio>
                <el-radio label="oss">阿里云OSS</el-radio>
                <el-radio label="cos">腾讯云COS</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="本地存储路径" v-if="storageForm.storageType === 'local'">
              <el-input v-model="storageForm.localPath" />
            </el-form-item>
            <template v-if="storageForm.storageType !== 'local'">
              <el-form-item label="Bucket名称">
                <el-input v-model="storageForm.bucket" />
              </el-form-item>
              <el-form-item label="访问域名">
                <el-input v-model="storageForm.domain" />
              </el-form-item>
              <el-form-item label="AccessKey">
                <el-input v-model="storageForm.accessKey" />
              </el-form-item>
              <el-form-item label="SecretKey">
                <el-input v-model="storageForm.secretKey" type="password" show-password />
              </el-form-item>
              <el-form-item label="区域">
                <el-input v-model="storageForm.region" />
              </el-form-item>
            </template>
            <el-form-item label="文件大小限制(MB)">
              <el-input-number v-model="storageForm.maxFileSize" :min="1" :max="1024" />
            </el-form-item>
            <el-form-item label="允许的文件类型">
              <el-select v-model="storageForm.allowedTypes" multiple placeholder="请选择">
                <el-option label="图片(jpg,png,gif)" value="image" />
                <el-option label="视频(mp4,avi)" value="video" />
                <el-option label="音频(mp3,wav)" value="audio" />
                <el-option label="文档(pdf,doc)" value="document" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveStorage" :loading="saving">保存设置</el-button>
              <el-button @click="handleResetStorage">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 系统维护 -->
        <el-tab-pane label="系统维护" name="maintenance">
          <el-descriptions title="系统信息" :column="2" border>
            <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
            <el-descriptions-item label="框架版本">Vue 3.3.4</el-descriptions-item>
            <el-descriptions-item label="运行环境">{{ systemInfo.os }}</el-descriptions-item>
            <el-descriptions-item label="Node版本">{{ systemInfo.nodeVersion }}</el-descriptions-item>
            <el-descriptions-item label="数据库">MySQL 8.0</el-descriptions-item>
            <el-descriptions-item label="缓存">Redis 6.0</el-descriptions-item>
            <el-descriptions-item label="运行时间">{{ systemInfo.uptime }}</el-descriptions-item>
            <el-descriptions-item label="服务器时间">{{ systemInfo.serverTime }}</el-descriptions-item>
          </el-descriptions>

          <el-divider />

          <div class="maintenance-actions">
            <h3>系统操作</h3>
            <el-space wrap>
              <el-button type="primary" @click="handleClearCache">
                <el-icon><Delete /></el-icon>
                清除缓存
              </el-button>
              <el-button type="warning" @click="handleBackup">
                <el-icon><Download /></el-icon>
                数据备份
              </el-button>
              <el-button type="info" @click="handleRestore">
                <el-icon><Upload /></el-icon>
                数据恢复
              </el-button>
              <el-button type="success" @click="handleExportLogs">
                <el-icon><Document /></el-icon>
                导出日志
              </el-button>
            </el-space>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

const activeTab = ref('basic')
const saving = ref(false)

// 基础设置
const basicForm = reactive({
  systemName: '陵园管家管理系统',
  logo: '',
  systemDesc: '智慧陵园管理系统，提供墓位管理、纪念空间、服务商管理等功能',
  copyright: '© 2025 陵园管家. All rights reserved.',
  contact: '400-888-8888'
})

// 安全设置
const securityForm = reactive({
  passwordComplexity: true,
  passwordMinLength: 8,
  passwordExpireDays: 90,
  loginLockEnabled: true,
  loginFailThreshold: 5,
  lockDuration: 30,
  sessionTimeout: 120,
  captchaEnabled: true
})

// 通知设置
const notificationForm = reactive({
  emailEnabled: false,
  smtpServer: '',
  smtpPort: 465,
  senderEmail: '',
  emailPassword: '',
  smsEnabled: false,
  smsProvider: 'aliyun',
  smsAccessKey: '',
  smsSecretKey: ''
})

// 存储设置
const storageForm = reactive({
  storageType: 'local',
  localPath: '/data/uploads',
  bucket: '',
  domain: '',
  accessKey: '',
  secretKey: '',
  region: '',
  maxFileSize: 50,
  allowedTypes: ['image', 'video', 'audio']
})

// 系统信息
const systemInfo = reactive({
  os: 'Windows 10',
  nodeVersion: 'v18.16.0',
  uptime: '15天8小时',
  serverTime: dayjs().format('YYYY-MM-DD HH:mm:ss')
})

// 保存基础设置
const handleSaveBasic = async () => {
  saving.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 重置基础设置
const handleResetBasic = () => {
  ElMessageBox.confirm('确定要重置为默认设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 重置逻辑
    ElMessage.success('已重置')
  }).catch(() => {})
}

// 保存安全设置
const handleSaveSecurity = async () => {
  saving.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 重置安全设置
const handleResetSecurity = () => {
  ElMessageBox.confirm('确定要重置为默认设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('已重置')
  }).catch(() => {})
}

// 保存通知设置
const handleSaveNotification = async () => {
  saving.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 重置通知设置
const handleResetNotification = () => {
  ElMessageBox.confirm('确定要重置为默认设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('已重置')
  }).catch(() => {})
}

// 保存存储设置
const handleSaveStorage = async () => {
  saving.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 重置存储设置
const handleResetStorage = () => {
  ElMessageBox.confirm('确定要重置为默认设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('已重置')
  }).catch(() => {})
}

// 清除缓存
const handleClearCache = () => {
  ElMessageBox.confirm('确定要清除系统缓存吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('缓存已清除')
  }).catch(() => {})
}

// 数据备份
const handleBackup = () => {
  ElMessageBox.confirm('确定要备份数据吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(async () => {
    await new Promise(resolve => setTimeout(resolve, 2000))
    ElMessage.success('备份完成，文件已保存到服务器')
  }).catch(() => {})
}

// 数据恢复
const handleRestore = () => {
  ElMessage.info('数据恢复功能开发中...')
}

// 导出日志
const handleExportLogs = () => {
  ElMessage.success('日志导出中，请稍候...')
}

// 更新服务器时间
const updateServerTime = () => {
  systemInfo.serverTime = dayjs().format('YYYY-MM-DD HH:mm:ss')
}

onMounted(() => {
  // 每秒更新服务器时间
  setInterval(updateServerTime, 1000)
})
</script>

<style scoped>
.settings-container {
  padding: 20px;
}

.form-tip {
  margin-left: 10px;
  font-size: 12px;
  color: #909399;
}

.logo-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

.logo-uploader:hover {
  border-color: #409eff;
}

.logo-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.logo {
  width: 178px;
  height: 178px;
  display: block;
}

.maintenance-actions {
  margin-top: 20px;
}

.maintenance-actions h3 {
  margin-bottom: 15px;
  color: #303133;
}
</style>
