<template>
  <div class="memorial-container">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Collection /></el-icon>
        数字纪念空间
      </h2>
      <p class="page-desc">管理数字纪念空间、纪念内容及互动信息</p>
    </div>

    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="空间名称">
          <el-input v-model="searchForm.spaceName" placeholder="请输入空间名称" clearable />
        </el-form-item>
        <el-form-item label="逝者姓名">
          <el-input v-model="searchForm.deceasedName" placeholder="请输入逝者姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="spaceName" label="空间名称" width="200" />
        <el-table-column prop="deceasedName" label="逝者姓名" width="120" />
        <el-table-column prop="birthDate" label="生卒日期" width="220">
          <template #default="{ row }">
            {{ row.birthDate }} ~ {{ row.deathDate }}
          </template>
        </el-table-column>
        <el-table-column prop="creatorName" label="创建人" width="120" />
        <el-table-column prop="visitCount" label="访问量" width="100" />
        <el-table-column prop="contentCount" label="内容数" width="100">
          <template #default="{ row }">
            <el-tag type="info">{{ row.contentCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="success" @click="handleManageContent(row)">内容管理</el-button>
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
import { Search, Refresh, Collection } from '@element-plus/icons-vue'

const searchForm = reactive({ spaceName: '', deceasedName: '' })
const pagination = reactive({ page: 1, pageSize: 10, total: 0 })
const tableData = ref([])
const loading = ref(false)

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, { spaceName: '', deceasedName: '' })
  handleSearch()
}

const handleView = (row) => {
  ElMessage.info('查看纪念空间')
}

const handleManageContent = (row) => {
  ElMessage.info('管理纪念内容')
}

const handleDelete = (row) => {
  ElMessage.warning('删除纪念空间')
}

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        spaceName: '思念爷爷',
        deceasedName: '张大明',
        birthDate: '1930-05-20',
        deathDate: '2020-10-15',
        creatorName: '张小明',
        visitCount: 1523,
        contentCount: 45,
        createTime: '2020-10-20 10:00:00'
      },
      {
        id: 2,
        spaceName: '永远怀念',
        deceasedName: '李秀英',
        birthDate: '1935-08-12',
        deathDate: '2021-03-28',
        creatorName: '李华',
        visitCount: 892,
        contentCount: 32,
        createTime: '2021-04-01 14:30:00'
      }
    ]
    pagination.total = 2
    loading.value = false
  }, 500)
}

onMounted(() => fetchData())
</script>

<style lang="scss" scoped>
.memorial-container {
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
