<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6" v-for="(stat, index) in stats" :key="index">
        <el-card shadow="hover" class="stat-card-wrapper">
          <div class="stat-card">
            <div class="stat-icon" :style="{ backgroundColor: stat.color }">
              <el-icon :size="30"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-title">{{ stat.title }}</div>
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-trend" :class="stat.trend > 0 ? 'up' : 'down'">
                <el-icon><component :is="stat.trend > 0 ? 'CaretTop' : 'CaretBottom'" /></el-icon>
                <span>{{ Math.abs(stat.trend) }}%</span>
                <span class="stat-desc">较昨日</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <!-- 访问趋势图 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>访问趋势</span>
              <el-radio-group v-model="visitTrendPeriod" size="small" @change="loadVisitTrend">
                <el-radio-button label="7">近7天</el-radio-button>
                <el-radio-button label="30">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="visitTrendChart" style="width: 100%; height: 350px"></div>
        </el-card>
      </el-col>

      <!-- 内容类型分布 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span>内容类型分布</span>
          </template>
          <div ref="contentDistChart" style="width: 100%; height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 热门纪念空间 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>热门纪念空间 TOP 10</span>
              <el-radio-group v-model="topMemorialsOrderBy" size="small" @change="loadTopMemorials">
                <el-radio-button label="visitCount">访问量</el-radio-button>
                <el-radio-button label="interactionCount">互动量</el-radio-button>
                <el-radio-button label="messageCount">留言数</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="topMemorialsChart" style="width: 100%; height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时数据 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>实时访问</span>
              <el-tag size="small" type="success">实时更新</el-tag>
            </div>
          </template>
          <div class="real-time-data">
            <div class="real-time-item" v-for="item in realTimeVisits" :key="item.id">
              <el-avatar :size="40" :src="item.avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <div class="real-time-content">
                <div class="real-time-name">{{ item.userName }}</div>
                <div class="real-time-desc">访问了 {{ item.memorialName }}</div>
              </div>
              <div class="real-time-time">{{ item.time }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>系统信息</span>
          </template>
          <div class="system-info">
            <div class="info-item">
              <span class="info-label">系统版本</span>
              <span class="info-value">v1.0.0</span>
            </div>
            <div class="info-item">
              <span class="info-label">运行时间</span>
              <span class="info-value">{{ systemRuntime }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">在线用户</span>
              <span class="info-value">{{ onlineUsers }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">今日访问</span>
              <span class="info-value">{{ todayVisits }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import dayjs from 'dayjs'
import { 
  getMemorialOverview, 
  getProviderStatistics,
  getVisitTrend,
  getContentTypeDistribution,
  getTopMemorials,
  getRealTimeStatistics
} from '@/api/dashboard'
import { ElMessage } from 'element-plus'

// 统计卡片数据
const stats = ref([
  { title: '纪念空间总数', value: '0', trend: 0, icon: 'Collection', color: '#409eff' },
  { title: '今日访问', value: '0', trend: 0, icon: 'View', color: '#67c23a' },
  { title: '总互动数', value: '0', trend: 0, icon: 'ChatLineRound', color: '#e6a23c' },
  { title: '服务商数量', value: '0', trend: 0, icon: 'Shop', color: '#f56c6c' }
])

// 图表相关
const visitTrendChart = ref(null)
const contentDistChart = ref(null)
const topMemorialsChart = ref(null)
let visitTrendChartInstance = null
let contentDistChartInstance = null
let topMemorialsChartInstance = null

const visitTrendPeriod = ref('7')
const topMemorialsOrderBy = ref('visitCount')

// 实时数据
const realTimeVisits = ref([])
const systemRuntime = ref('0天0小时')
const onlineUsers = ref(0)
const todayVisits = ref(0)

let refreshTimer = null

// 加载统计卡片数据
const loadStatistics = async () => {
  try {
    // 加载纪念空间概况
    const overviewRes = await getMemorialOverview()
    if (overviewRes.code === 200 && overviewRes.data) {
      const data = overviewRes.data
      stats.value[0].value = data.totalMemorials || 0
      stats.value[0].trend = data.memorialTrend || 0
      stats.value[1].value = data.todayVisits || 0
      stats.value[1].trend = data.visitTrend || 0
      stats.value[2].value = data.totalInteractions || 0
      stats.value[2].trend = data.interactionTrend || 0
    }

    // 加载服务商统计
    const providerRes = await getProviderStatistics()
    if (providerRes.code === 200 && providerRes.data) {
      stats.value[3].value = providerRes.data.totalProviders || 0
      stats.value[3].trend = providerRes.data.providerTrend || 0
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    // 使用模拟数据
    stats.value[0].value = '156'
    stats.value[0].trend = 12.5
    stats.value[1].value = '1,234'
    stats.value[1].trend = 8.3
    stats.value[2].value = '8,765'
    stats.value[2].trend = 15.2
    stats.value[3].value = '45'
    stats.value[3].trend = 5.6
  }
}

// 加载访问趋势
const loadVisitTrend = async () => {
  try {
    const days = parseInt(visitTrendPeriod.value)
    const endDate = dayjs()
    const startDate = endDate.subtract(days, 'day')
    
    const res = await getVisitTrend({
      startDate: startDate.format('YYYY-MM-DD'),
      endDate: endDate.format('YYYY-MM-DD')
    })

    if (res.code === 200 && res.data) {
      renderVisitTrendChart(res.data)
    } else {
      renderMockVisitTrendChart(days)
    }
  } catch (error) {
    console.error('加载访问趋势失败:', error)
    renderMockVisitTrendChart(parseInt(visitTrendPeriod.value))
  }
}

// 渲染访问趋势图表
const renderVisitTrendChart = (data) => {
  if (!visitTrendChartInstance) {
    visitTrendChartInstance = echarts.init(visitTrendChart.value)
  }

  const dates = data.map(item => item.date || item.dateStr)
  const visits = data.map(item => item.visitCount || item.visits || 0)
  const interactions = data.map(item => item.interactionCount || item.interactions || 0)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['访问量', '互动量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '访问量',
        type: 'line',
        smooth: true,
        data: visits,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ])
        },
        itemStyle: {
          color: '#409eff'
        }
      },
      {
        name: '互动量',
        type: 'line',
        smooth: true,
        data: interactions,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ])
        },
        itemStyle: {
          color: '#67c23a'
        }
      }
    ]
  }

  visitTrendChartInstance.setOption(option)
}

// 渲染模拟访问趋势图表
const renderMockVisitTrendChart = (days) => {
  const dates = []
  const visits = []
  const interactions = []
  
  for (let i = days - 1; i >= 0; i--) {
    dates.push(dayjs().subtract(i, 'day').format('MM-DD'))
    visits.push(Math.floor(Math.random() * 500) + 800)
    interactions.push(Math.floor(Math.random() * 300) + 400)
  }

  renderVisitTrendChart(dates.map((date, index) => ({
    date,
    visitCount: visits[index],
    interactionCount: interactions[index]
  })))
}

// 加载内容类型分布
const loadContentDistribution = async () => {
  try {
    const res = await getContentTypeDistribution()
    
    if (res.code === 200 && res.data) {
      renderContentDistChart(res.data)
    } else {
      renderMockContentDistChart()
    }
  } catch (error) {
    console.error('加载内容分布失败:', error)
    renderMockContentDistChart()
  }
}

// 渲染内容类型分布图表
const renderContentDistChart = (data) => {
  if (!contentDistChartInstance) {
    contentDistChartInstance = echarts.init(contentDistChart.value)
  }

  const chartData = data.map(item => ({
    name: item.typeName || item.type || item.name,
    value: item.count || item.value || 0
  }))

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '内容类型',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: chartData
      }
    ]
  }

  contentDistChartInstance.setOption(option)
}

// 渲染模拟内容分布图表
const renderMockContentDistChart = () => {
  renderContentDistChart([
    { name: '图片', value: 1245 },
    { name: '视频', value: 356 },
    { name: '音频', value: 178 },
    { name: '文字', value: 892 }
  ])
}

// 加载热门纪念空间
const loadTopMemorials = async () => {
  try {
    const res = await getTopMemorials({
      orderBy: topMemorialsOrderBy.value,
      limit: 10
    })

    if (res.code === 200 && res.data) {
      renderTopMemorialsChart(res.data)
    } else {
      renderMockTopMemorialsChart()
    }
  } catch (error) {
    console.error('加载热门纪念空间失败:', error)
    renderMockTopMemorialsChart()
  }
}

// 渲染热门纪念空间图表
const renderTopMemorialsChart = (data) => {
  if (!topMemorialsChartInstance) {
    topMemorialsChartInstance = echarts.init(topMemorialsChart.value)
  }

  const names = data.map(item => item.memorialName || item.name)
  const values = data.map(item => {
    if (topMemorialsOrderBy.value === 'visitCount') return item.visitCount || 0
    if (topMemorialsOrderBy.value === 'interactionCount') return item.interactionCount || 0
    if (topMemorialsOrderBy.value === 'messageCount') return item.messageCount || 0
    return 0
  })

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: names
    },
    series: [
      {
        type: 'bar',
        data: values,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#409eff' },
            { offset: 1, color: '#67c23a' }
          ])
        },
        barWidth: '60%'
      }
    ]
  }

  topMemorialsChartInstance.setOption(option)
}

// 渲染模拟热门纪念空间图表
const renderMockTopMemorialsChart = () => {
  const mockData = [
    { name: '张三纪念空间', visitCount: 2345, interactionCount: 856, messageCount: 234 },
    { name: '李四纪念空间', visitCount: 2123, interactionCount: 745, messageCount: 198 },
    { name: '王五纪念空间', visitCount: 1987, interactionCount: 678, messageCount: 176 },
    { name: '赵六纪念空间', visitCount: 1856, interactionCount: 623, messageCount: 165 },
    { name: '孙七纪念空间', visitCount: 1734, interactionCount: 589, messageCount: 154 },
    { name: '周八纪念空间', visitCount: 1623, interactionCount: 534, messageCount: 143 },
    { name: '吴九纪念空间', visitCount: 1512, interactionCount: 498, messageCount: 132 },
    { name: '郑十纪念空间', visitCount: 1445, interactionCount: 467, messageCount: 128 },
    { name: '冯十一纪念空间', visitCount: 1389, interactionCount: 445, messageCount: 121 },
    { name: '陈十二纪念空间', visitCount: 1298, interactionCount: 423, messageCount: 115 }
  ]
  renderTopMemorialsChart(mockData.map(item => ({
    memorialName: item.name,
    ...item
  })))
}

// 加载实时数据
const loadRealTimeData = async () => {
  try {
    const res = await getRealTimeStatistics()
    
    if (res.code === 200 && res.data) {
      realTimeVisits.value = res.data.recentVisits || []
      onlineUsers.value = res.data.onlineUsers || 0
      todayVisits.value = res.data.todayVisits || 0
      systemRuntime.value = res.data.systemRuntime || '0天0小时'
    } else {
      loadMockRealTimeData()
    }
  } catch (error) {
    console.error('加载实时数据失败:', error)
    loadMockRealTimeData()
  }
}

// 加载模拟实时数据
const loadMockRealTimeData = () => {
  const mockVisits = []
  const names = ['张三', '李四', '王五', '赵六', '孙七']
  const memorials = ['怀念父亲', '缅怀母亲', '纪念爷爷', '追思奶奶', '悼念挚友']
  
  for (let i = 0; i < 5; i++) {
    mockVisits.push({
      id: i,
      userName: names[i],
      memorialName: memorials[i],
      time: `${i + 1}分钟前`
    })
  }
  
  realTimeVisits.value = mockVisits
  onlineUsers.value = 23
  todayVisits.value = 1234
  systemRuntime.value = '15天8小时'
}

// 初始化所有图表
const initCharts = async () => {
  await nextTick()
  await loadStatistics()
  await loadVisitTrend()
  await loadContentDistribution()
  await loadTopMemorials()
  await loadRealTimeData()
}

// 窗口大小改变时重新渲染图表
const handleResize = () => {
  visitTrendChartInstance?.resize()
  contentDistChartInstance?.resize()
  topMemorialsChartInstance?.resize()
}

// 定时刷新实时数据
const startRefresh = () => {
  refreshTimer = setInterval(() => {
    loadRealTimeData()
  }, 30000) // 每30秒刷新一次
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
  startRefresh()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
  visitTrendChartInstance?.dispose()
  contentDistChartInstance?.dispose()
  topMemorialsChartInstance?.dispose()
})
</script>

<style scoped>
.dashboard {
  width: 100%;
  padding-bottom: 20px;
}

.stat-cards {
  margin-bottom: 0;
}

.stat-card-wrapper {
  transition: transform 0.3s;
}

.stat-card-wrapper:hover {
  transform: translateY(-5px);
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 15px;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 26px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 6px;
}

.stat-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-trend.up {
  color: #67c23a;
}

.stat-trend.down {
  color: #f56c6c;
}

.stat-desc {
  color: #909399;
  margin-left: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.real-time-data {
  max-height: 400px;
  overflow-y: auto;
}

.real-time-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.real-time-item:last-child {
  border-bottom: none;
}

.real-time-content {
  flex: 1;
  margin-left: 12px;
}

.real-time-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.real-time-desc {
  font-size: 12px;
  color: #909399;
}

.real-time-time {
  font-size: 12px;
  color: #c0c4cc;
}

.system-info {
  padding: 10px 0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 14px;
  color: #606266;
}

.info-value {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}
</style>
