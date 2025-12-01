<template>
  <div class="tomb-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">
        <el-icon><Location /></el-icon>
        墓位管理
      </h2>
      <p class="page-desc">管理墓位信息、位置、状态及相关资料</p>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="墓位编号">
          <el-input
            v-model="searchForm.tombNo"
            placeholder="请输入墓位编号"
            clearable
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="墓位类型">
          <el-select v-model="searchForm.tombType" placeholder="请选择" clearable>
            <el-option label="单人墓" :value="1" />
            <el-option label="双人墓" :value="2" />
            <el-option label="家族墓" :value="3" />
            <el-option label="艺术墓" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="可售" :value="0" />
            <el-option label="已预订" :value="1" />
            <el-option label="已售" :value="2" />
            <el-option label="维护中" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <el-card class="action-card" shadow="never">
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增墓位</el-button>
      <el-button type="success" :icon="Download" @click="handleExport">导出数据</el-button>
      <el-button type="warning" :icon="Upload" @click="handleImport">批量导入</el-button>
      <el-button type="info" :icon="MapLocation" @click="handleShowMap">地图查看</el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
        :header-cell-style="{ background: '#fafafa', color: '#606266' }"
        stripe
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="tombNo" label="墓位编号" width="120" fixed />
        <el-table-column prop="tombType" label="墓位类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTombTypeTag(row.tombType)">
              {{ getTombTypeText(row.tombType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="area" label="区域" width="100" />
        <el-table-column prop="row" label="排" width="80" />
        <el-table-column prop="column" label="号" width="80" />
        <el-table-column prop="price" label="价格(元)" width="120">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.price?.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="area_size" label="面积(㎡)" width="100" />
        <el-table-column prop="orientation" label="朝向" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="owner" label="所有人" width="120" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button link type="success" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="info" :icon="MapLocation" @click="handleShowLocation(row)">位置</el-button>
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="墓位编号" prop="tombNo">
              <el-input v-model="form.tombNo" placeholder="自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="墓位类型" prop="tombType">
              <el-select v-model="form.tombType" placeholder="请选择">
                <el-option label="单人墓" :value="1" />
                <el-option label="双人墓" :value="2" />
                <el-option label="家族墓" :value="3" />
                <el-option label="艺术墓" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="区域" prop="area">
              <el-input v-model="form.area" placeholder="如：A区" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排" prop="row">
              <el-input-number v-model="form.row" :min="1" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="号" prop="column">
              <el-input-number v-model="form.column" :min="1" controls-position="right" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number
                v-model="form.price"
                :min="0"
                :precision="2"
                :step="1000"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面积" prop="area_size">
              <el-input-number
                v-model="form.area_size"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="朝向" prop="orientation">
              <el-select v-model="form.orientation" placeholder="请选择">
                <el-option label="东" value="东" />
                <el-option label="南" value="南" />
                <el-option label="西" value="西" />
                <el-option label="北" value="北" />
                <el-option label="东南" value="东南" />
                <el-option label="西南" value="西南" />
                <el-option label="东北" value="东北" />
                <el-option label="西北" value="西北" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">可售</el-radio>
                <el-radio :label="1">已预订</el-radio>
                <el-radio :label="2">已售</el-radio>
                <el-radio :label="3">维护中</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="点击地图选择" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="点击地图选择" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="墓位图片">
          <el-upload
            class="upload-demo"
            action="/api/upload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :file-list="form.images"
            list-type="picture"
          >
            <el-button type="primary" :icon="Upload">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">支持jpg/png文件，且不超过2MB</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 地图对话框 -->
    <el-dialog
      v-model="mapDialogVisible"
      title="墓位位置"
      width="900px"
      :close-on-click-modal="false"
    >
      <div id="map-container" style="height: 500px;"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Refresh, Plus, Download, Upload, MapLocation,
  View, Edit, Delete, Location
} from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  tombNo: '',
  tombType: '',
  status: ''
})

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增墓位')
const submitLoading = ref(false)

// 表单
const formRef = ref()
const form = reactive({
  id: null,
  tombNo: '',
  tombType: '',
  area: '',
  row: 1,
  column: 1,
  price: 0,
  area_size: 0,
  orientation: '',
  status: 0,
  longitude: '',
  latitude: '',
  images: [],
  remark: ''
})

// 表单验证规则
const formRules = {
  tombType: [{ required: true, message: '请选择墓位类型', trigger: 'change' }],
  area: [{ required: true, message: '请输入区域', trigger: 'blur' }],
  row: [{ required: true, message: '请输入排号', trigger: 'blur' }],
  column: [{ required: true, message: '请输入列号', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  area_size: [{ required: true, message: '请输入面积', trigger: 'blur' }],
  orientation: [{ required: true, message: '请选择朝向', trigger: 'change' }]
}

// 地图对话框
const mapDialogVisible = ref(false)

// 获取墓位类型文本
const getTombTypeText = (type) => {
  const map = { 1: '单人墓', 2: '双人墓', 3: '家族墓', 4: '艺术墓' }
  return map[type] || '未知'
}

// 获取墓位类型标签
const getTombTypeTag = (type) => {
  const map = { 1: 'success', 2: 'primary', 3: 'warning', 4: 'danger' }
  return map[type] || ''
}

// 获取状态文本
const getStatusText = (status) => {
  const map = { 0: '可售', 1: '已预订', 2: '已售', 3: '维护中' }
  return map[status] || '未知'
}

// 获取状态标签
const getStatusTag = (status) => {
  const map = { 0: 'success', 1: 'warning', 2: 'info', 3: 'danger' }
  return map[status] || ''
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    tombNo: '',
    tombType: '',
    status: ''
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增墓位'
  Object.assign(form, {
    id: null,
    tombNo: '自动生成',
    tombType: '',
    area: '',
    row: 1,
    column: 1,
    price: 0,
    area_size: 0,
    orientation: '',
    status: 0,
    longitude: '',
    latitude: '',
    images: [],
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑墓位'
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

// 查看
const handleView = (row) => {
  ElMessage.info('查看功能开发中...')
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该墓位吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    fetchData()
  }).catch(() => {})
}

// 显示位置
const handleShowLocation = (row) => {
  mapDialogVisible.value = true
  setTimeout(() => {
    initMap(row.longitude, row.latitude)
  }, 100)
}

// 显示地图
const handleShowMap = () => {
  ElMessage.info('地图查看功能开发中...')
}

// 导出
const handleExport = () => {
  ElMessage.success('导出成功')
}

// 导入
const handleImport = () => {
  ElMessage.info('导入功能开发中...')
}

// 文件预览
const handlePreview = (file) => {
  window.open(file.url)
}

// 文件删除
const handleRemove = (file, fileList) => {
  form.images = fileList
}

// 提交表单
const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      submitLoading.value = true
      setTimeout(() => {
        submitLoading.value = false
        dialogVisible.value = false
        ElMessage.success(form.id ? '修改成功' : '新增成功')
        fetchData()
      }, 1000)
    }
  })
}

// 分页变化
const handlePageChange = (page) => {
  pagination.page = page
  fetchData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchData()
}

// 初始化地图
const initMap = (lng, lat) => {
  // 这里使用高德地图示例，实际需要引入AMap
  ElMessage.info('地图功能需要配置高德地图API Key')
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  // 模拟数据
  setTimeout(() => {
    tableData.value = [
      {
        id: 1,
        tombNo: 'MB-A-001-001',
        tombType: 1,
        area: 'A区',
        row: 1,
        column: 1,
        price: 50000,
        area_size: 2.5,
        orientation: '南',
        status: 0,
        owner: '-',
        longitude: '116.397428',
        latitude: '39.90923',
        createTime: '2025-01-01 10:00:00'
      },
      {
        id: 2,
        tombNo: 'MB-A-001-002',
        tombType: 2,
        area: 'A区',
        row: 1,
        column: 2,
        price: 80000,
        area_size: 4.0,
        orientation: '东南',
        status: 1,
        owner: '张三',
        longitude: '116.397428',
        latitude: '39.90923',
        createTime: '2025-01-02 11:00:00'
      },
      {
        id: 3,
        tombNo: 'MB-B-002-001',
        tombType: 3,
        area: 'B区',
        row: 2,
        column: 1,
        price: 150000,
        area_size: 8.0,
        orientation: '南',
        status: 2,
        owner: '李四',
        longitude: '116.397428',
        latitude: '39.90923',
        createTime: '2025-01-03 14:00:00'
      }
    ]
    pagination.total = 3
    loading.value = false
  }, 500)
}

onMounted(() => {
  fetchData()
})
</script>

<style lang="scss" scoped>
.tomb-container {
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

      .el-icon {
        font-size: 28px;
      }
    }

    .page-desc {
      margin: 0;
      opacity: 0.9;
      font-size: 14px;
    }
  }

  .search-card,
  .action-card,
  .table-card {
    margin-bottom: 20px;
  }

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
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

  .upload-demo {
    width: 100%;
  }
}
</style>
