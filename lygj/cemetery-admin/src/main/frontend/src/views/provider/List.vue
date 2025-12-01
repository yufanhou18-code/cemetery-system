<template>
  <div class="provider-container">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Shop /></el-icon>
        服务商管理
      </h2>
      <p class="page-desc">管理陵园服务商信息、服务项目及评价</p>
    </div>

    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="服务商名称">
          <el-input v-model="searchForm.providerName" placeholder="请输入服务商名称" clearable />
        </el-form-item>
        <el-form-item label="服务类型">
          <el-select v-model="searchForm.serviceType" placeholder="请选择" clearable>
            <el-option label="殡葬服务" :value="1" />
            <el-option label="墓地维护" :value="2" />
            <el-option label="祭祀用品" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="never">
      <div style="margin-bottom: 15px;">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增服务商</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="providerName" label="服务商名称" width="200" />
        <el-table-column prop="providerType" label="服务类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.providerType)">
              {{ getTypeText(row.providerType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="150" />
        <el-table-column prop="rating" label="评分" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column prop="serviceCount" label="服务次数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '营业中' : '已停业' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="success" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="info" @click="handleServices(row)">服务项目</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @current-change="fetchData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Plus, Shop } from '@element-plus/icons-vue'

const searchForm = reactive({ providerName: '', serviceType: '' })
const pagination = reactive({ page: 1, pageSize: 10, total: 0 })
const tableData = ref([])
const loading = ref(false)

const getTypeText = (type) => {
  const map = { 1: '殡葬服务', 2: '墓地维护', 3: '祭祀用品' }
  return map[type] || '其他'
}

const getTypeTag = (type) => {
  const map = { 1: 'primary', 2: 'success', 3: 'warning' }
  return map[type] || 'info'
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, { providerName: '', serviceType: '' })
  handleSearch()
}

const handleAdd = () => {
  ElMessage.info('新增服务商功能开发中...')
}

const handleView = (row) => {
  ElMessage.info('查看服务商详情')
}

const handleEdit = (row) => {
  ElMessage.info('编辑服务商')
}

const handleServices = (row) => {
  ElMessage.info('管理服务项目')
}

const handleDelete = (row) => {
  ElMessage.warning('删除服务商')
}

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        providerName: '优质殡葬服务公司',
        providerType: 1,
        contactPerson: '张经理',
        contactPhone: '13800138001',
        rating: 4.8,
        serviceCount: 235,
        status: 1,
        createTime: '2025-01-01 10:00:00'
      },
      {
        id: 2,
        providerName: '专业墓地维护中心',
        providerType: 2,
        contactPerson: '李经理',
        contactPhone: '13800138002',
        rating: 4.9,
        serviceCount: 189,
        status: 1,
        createTime: '2025-01-02 11:00:00'
      }
    ]
    pagination.total = 2
    loading.value = false
  }, 500)
}

onMounted(() => fetchData())
</script>

<style lang="scss" scoped>
.provider-container {
  .page-header {
    margin-bottom: 20px;
    padding: 24px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 8px;
    color: #fff;

    .page-title {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 24px;
      font-weight: 600;
      margin: 0 0 8px 0;
    }

    .page-desc {
      margin: 0;
      opacity: 0.9;
    }
  }

  .search-card,
  .table-card {
    margin-bottom: 20px;
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}
</style>
