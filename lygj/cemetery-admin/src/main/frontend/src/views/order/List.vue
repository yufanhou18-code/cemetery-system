<template>
  <div class="order-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Document /></el-icon>
        订单管理
      </h2>
      <p class="page-desc">管理墓位订单、支付状态及订单流程</p>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="待支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="orderNo" label="订单编号" width="180" fixed />
        <el-table-column prop="tombNo" label="墓位编号" width="150" />
        <el-table-column prop="userName" label="购买人" width="120" />
        <el-table-column prop="amount" label="订单金额" width="120">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.amount?.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="success" v-if="row.status === 0" @click="handlePay(row)">支付</el-button>
            <el-button link type="danger" v-if="row.status === 0" @click="handleCancel(row)">取消</el-button>
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
import { Search, Refresh, Document } from '@element-plus/icons-vue'

const searchForm = reactive({ orderNo: '', status: '' })
const pagination = reactive({ page: 1, pageSize: 10, total: 0 })
const tableData = ref([])
const loading = ref(false)

const getStatusText = (status) => {
  const map = { 0: '待支付', 1: '已支付', 2: '已完成', 3: '已取消' }
  return map[status] || '未知'
}

const getStatusTag = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }
  return map[status] || ''
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, { orderNo: '', status: '' })
  handleSearch()
}

const handleView = (row) => {
  ElMessage.info('查看订单详情')
}

const handlePay = (row) => {
  ElMessage.success('支付成功')
  fetchData()
}

const handleCancel = (row) => {
  ElMessage.warning('订单已取消')
  fetchData()
}

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        orderNo: 'ORD-20250101-001',
        tombNo: 'MB-A-001-001',
        userName: '张三',
        amount: 50000,
        status: 0,
        createTime: '2025-01-01 10:00:00'
      },
      {
        id: 2,
        orderNo: 'ORD-20250102-002',
        tombNo: 'MB-A-001-002',
        userName: '李四',
        amount: 80000,
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
.order-container {
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

  .price-text {
    color: #f56c6c;
    font-weight: 600;
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}
</style>
