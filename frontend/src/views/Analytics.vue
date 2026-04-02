<template>
  <div class="analytics-container">
    <el-card class="mb-20">
      <template #header>
        <span>数据分析与统计</span>
      </template>
      
      <el-tabs v-model="activeTab" class="analytics-tabs">
        <el-tab-pane label="学生统计" name="student" class="tab-content">
          <div class="chart-row">
            <div ref="collegeChartRef" class="chart-container full-width"></div>
          </div>
          <div class="chart-row">
            <div ref="majorChartRef" class="chart-container full-width"></div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="成绩分析" name="score" class="tab-content">
          <div class="chart-row">
            <div ref="gpaDistChartRef" class="chart-container half-width"></div>
            <div ref="scoreTrendChartRef" class="chart-container half-width"></div>
          </div>
          <div class="chart-row">
            <div ref="courseChartRef" class="chart-container full-width"></div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="画像分析" name="profile" class="tab-content">
          <div class="chart-row">
            <div ref="profileTypeChartRef" class="chart-container third-width"></div>
            <div ref="abilityRadarChartRef" class="chart-container third-width"></div>
            <div ref="knowledgeChartRef" class="chart-container third-width"></div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="预警分析" name="warning" class="tab-content">
          <div class="chart-row">
            <div ref="warningTypeChartRef" class="chart-container half-width"></div>
            <div ref="warningTrendChartRef" class="chart-container half-width"></div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-card>
      <template #header>
        <span>详细数据报表</span>
      </template>
      <el-table :data="tableData" stripe border>
        <el-table-column prop="indicator" label="指标" width="200" />
        <el-table-column prop="value" label="数值" width="120" />
        <el-table-column prop="rank" label="排名" width="100" />
        <el-table-column prop="change" label="较上期" width="120">
          <template #default="{ row }">
            <span :class="row.change > 0 ? 'text-success' : 'text-danger'">
              {{ row.change > 0 ? '+' : '' }}{{ row.change }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { statsApi } from '@/api/student'
import request from '@/api/index'

const activeTab = ref('student')

const collegeChartRef = ref(null)
const majorChartRef = ref(null)
const gpaDistChartRef = ref(null)
const scoreTrendChartRef = ref(null)
const courseChartRef = ref(null)
const profileTypeChartRef = ref(null)
const abilityRadarChartRef = ref(null)
const knowledgeChartRef = ref(null)
const warningTypeChartRef = ref(null)
const warningTrendChartRef = ref(null)

// 图表数据
const chartData = reactive({
  college: [],
  major: [],
  gpaDist: [],
  scoreTrend: [],
  course: [],
  profileType: [],
  ability: [],
  knowledge: [],
  warningType: [],
  warningTrend: []
})

const tableData = ref([])

let charts = []

// 从后端API加载数据
const fetchData = async () => {
  try {
    // 获取学院统计
    const collegeRes = await statsApi.statsByCollege()
    if (collegeRes.code === 200 && collegeRes.data) {
      chartData.college = collegeRes.data.map(item => ({
        name: item.name || item.college,
        value: item.count || item.value
      }))
    }

    // 获取专业统计
    const majorRes = await statsApi.statsByMajor()
    if (majorRes.code === 200 && majorRes.data) {
      chartData.major = majorRes.data.map(item => ({
        name: item.name || item.major,
        value: item.count || item.value
      }))
    }

    // 获取Dashboard统计数据
    const statsRes = await request.get('/dashboard/stats')
    if (statsRes.code === 200 && statsRes.data) {
      tableData.value = [
        { indicator: '学生总数', value: statsRes.data.totalStudents || 0, rank: '-', change: 0, description: '学生总数' },
        { indicator: '在读学生', value: statsRes.data.activeStudents || 0, rank: '-', change: 0, description: '在读学生数' },
        { indicator: '学业预警', value: statsRes.data.warningCount || 0, rank: '-', change: 0, description: '预警学生数' },
        { indicator: '平均绩点', value: (statsRes.data.avgGpa || 0).toFixed(2), rank: '-', change: 0, description: '平均绩点' }
      ]
    }

    // 获取预警列表
    const warningRes = await statsApi.getWarningList({ pageNum: 1, pageSize: 100 })
    if (warningRes.code === 200 && warningRes.data) {
      const warnings = warningRes.data.records || warningRes.data || []
      
      // 预警类型分布
      const warningTypeMap = {}
      warnings.forEach(w => {
        const type = w.warningTypeName || '其他'
        warningTypeMap[type] = (warningTypeMap[type] || 0) + 1
      })
      chartData.warningType = Object.entries(warningTypeMap).map(([name, value]) => ({
        name,
        value
      }))
    }

  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

const initCharts = () => {
  // 学院分布
  const collegeChart = echarts.init(collegeChartRef.value)
  collegeChart.setOption({
    title: { text: '各学院学生分布', left: 'center', textStyle: { fontSize: 16, fontWeight: 'bold' } },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: chartData.college.map(item => item.name), axisLabel: { fontSize: 12 } },
    yAxis: { type: 'value', axisLabel: { fontSize: 12 } },
    series: [{ type: 'bar', data: chartData.college.map(item => item.value), itemStyle: { color: '#409eff' }, barWidth: '50%' }]
  })
  charts.push(collegeChart)

  // 专业分布
  const majorChart = echarts.init(majorChartRef.value)
  majorChart.setOption({
    title: { text: '各专业学生分布', left: 'center', textStyle: { fontSize: 16, fontWeight: 'bold' } },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: chartData.major.map(item => item.name), axisLabel: { fontSize: 12 } },
    yAxis: { type: 'value', axisLabel: { fontSize: 12 } },
    series: [{ type: 'bar', data: chartData.major.map(item => item.value), itemStyle: { color: '#67c23a' }, barWidth: '50%' }]
  })
  charts.push(majorChart)

  // GPA分布
  const gpaDistChart = echarts.init(gpaDistChartRef.value)
  gpaDistChart.setOption({
    title: { text: 'GPA分数段分布', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    tooltip: { trigger: 'item' },
    legend: { bottom: 10, left: 'center', textStyle: { fontSize: 11 } },
    series: [{
      type: 'pie',
      radius: ['35%', '60%'],
      center: ['50%', '55%'],
      data: chartData.gpaDist.length > 0 ? chartData.gpaDist : [
        { value: 120, name: '3.5-4.0', itemStyle: { color: '#67c23a' } },
        { value: 380, name: '3.0-3.5', itemStyle: { color: '#409eff' } },
        { value: 450, name: '2.5-3.0', itemStyle: { color: '#e6a23c' } },
        { value: 230, name: '2.0-2.5', itemStyle: { color: '#f56c6c' } },
        { value: 100, name: '2.0以下', itemStyle: { color: '#909399' } }
      ]
    }]
  })
  charts.push(gpaDistChart)

  // 成绩趋势
  const scoreTrendChart = echarts.init(scoreTrendChartRef.value)
  scoreTrendChart.setOption({
    title: { text: '平均成绩趋势', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '25%', containLabel: true },
    xAxis: { type: 'category', data: chartData.scoreTrend.length > 0 ? chartData.scoreTrend.map(item => item.name) : ['2022-1', '2022-2', '2023-1', '2023-2', '2024-1'], axisLabel: { fontSize: 11 } },
    yAxis: { type: 'value', min: 60, max: 100, axisLabel: { fontSize: 11 } },
    series: [{
      type: 'line',
      smooth: true,
      data: chartData.scoreTrend.length > 0 ? chartData.scoreTrend.map(item => item.value) : [78, 80, 82, 85, 84],
      areaStyle: { color: 'rgba(64, 158, 255, 0.2)' },
      itemStyle: { color: '#409eff', width: 2 },
      lineStyle: { width: 2 }
    }]
  })
  charts.push(scoreTrendChart)

  // 课程难度分布
  const courseChart = echarts.init(courseChartRef.value)
  courseChart.setOption({
    title: { text: '课程难度分布', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '25%', containLabel: true },
    xAxis: { type: 'category', data: chartData.course.length > 0 ? chartData.course.map(item => item.name) : ['简单', '较易', '中等', '较难', '困难'], axisLabel: { fontSize: 12 } },
    yAxis: { type: 'value', axisLabel: { fontSize: 12 } },
    series: [{ type: 'bar', data: chartData.course.length > 0 ? chartData.course.map(item => item.value) : [30, 85, 150, 95, 40], itemStyle: { color: '#e6a23c' }, barWidth: '40%' }]
  })
  charts.push(courseChart)

  // 画像类型分布
  const profileTypeChart = echarts.init(profileTypeChartRef.value)
  profileTypeChart.setOption({
    title: { text: '学生画像类型', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    tooltip: { trigger: 'item' },
    legend: { bottom: 5, left: 'center', textStyle: { fontSize: 10 } },
    series: [{
      type: 'pie',
      radius: ['30%', '55%'],
      center: ['50%', '50%'],
      data: chartData.profileType.length > 0 ? chartData.profileType : [
        { value: 45, name: '学术研究型', itemStyle: { color: '#409eff' } },
        { value: 30, name: '实践应用型', itemStyle: { color: '#67c23a' } },
        { value: 25, name: '复合探索型', itemStyle: { color: '#e6a23c' } }
      ]
    }]
  })
  charts.push(profileTypeChart)

  // 能力雷达图
  const abilityRadarChart = echarts.init(abilityRadarChartRef.value)
  abilityRadarChart.setOption({
    title: { text: '学生能力雷达', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    radar: {
      center: ['50%', '55%'],
      radius: '65%',
      indicator: [
        { name: '学术', max: 100 }, { name: '实践', max: 100 },
        { name: '科研', max: 100 }, { name: '创新', max: 100 },
        { name: '协作', max: 100 }, { name: '综合', max: 100 }
      ],
      axisName: { fontSize: 11 }
    },
    series: [{
      type: 'radar',
      data: [{
        value: chartData.ability.length > 0 ? chartData.ability : [85, 92, 75, 88, 90, 85],
        name: '能力分布',
        areaStyle: { color: 'rgba(64, 158, 255, 0.3)' },
        itemStyle: { color: '#409eff' }
      }]
    }]
  })
  charts.push(abilityRadarChart)

  // 知识领域分布
  const knowledgeChart = echarts.init(knowledgeChartRef.value)
  knowledgeChart.setOption({
    title: { text: '知识领域分布', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    tooltip: { trigger: 'item' },
    legend: { bottom: 5, left: 'center', textStyle: { fontSize: 10 } },
    series: [{
      type: 'pie',
      radius: ['30%', '55%'],
      center: ['50%', '50%'],
      data: chartData.knowledge.length > 0 ? chartData.knowledge : [
        { value: 35, name: '计算机基础', itemStyle: { color: '#409eff' } },
        { value: 25, name: '系统基础', itemStyle: { color: '#67c23a' } },
        { value: 20, name: '数据管理', itemStyle: { color: '#e6a23c' } },
        { value: 12, name: '人工智能', itemStyle: { color: '#f56c6c' } },
        { value: 8, name: '其他', itemStyle: { color: '#909399' } }
      ]
    }]
  })
  charts.push(knowledgeChart)

  // 预警类型分布
  const warningTypeChart = echarts.init(warningTypeChartRef.value)
  warningTypeChart.setOption({
    title: { text: '预警类型分布', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    tooltip: { trigger: 'item' },
    legend: { bottom: 10, left: 'center', textStyle: { fontSize: 11 } },
    series: [{
      type: 'pie',
      radius: ['35%', '60%'],
      center: ['50%', '55%'],
      data: chartData.warningType.length > 0 ? chartData.warningType : [
        { value: 12, name: '成绩预警', itemStyle: { color: '#f56c6c' } },
        { value: 8, name: '学分预警', itemStyle: { color: '#e6a23c' } },
        { value: 5, name: '学业困难', itemStyle: { color: '#409eff' } },
        { value: 3, name: '毕业预警', itemStyle: { color: '#909399' } }
      ]
    }]
  })
  charts.push(warningTypeChart)

  // 预警趋势
  const warningTrendChart = echarts.init(warningTrendChartRef.value)
  warningTrendChart.setOption({
    title: { text: '预警趋势变化', left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '25%', containLabel: true },
    xAxis: { type: 'category', data: chartData.warningTrend.length > 0 ? chartData.warningTrend.map(item => item.name) : ['2023-09', '2023-10', '2023-11', '2023-12', '2024-01', '2024-02', '2024-03'], axisLabel: { fontSize: 10 } },
    yAxis: { type: 'value', axisLabel: { fontSize: 11 } },
    series: [{
      type: 'line',
      data: chartData.warningTrend.length > 0 ? chartData.warningTrend.map(item => item.value) : [35, 42, 38, 30, 25, 32, 28],
      itemStyle: { color: '#f56c6c' },
      areaStyle: { color: 'rgba(245, 108, 108, 0.2)' },
      lineStyle: { width: 2 }
    }]
  })
  charts.push(warningTrendChart)
}

const handleResize = () => {
  charts.forEach(chart => chart?.resize())
}

onMounted(async () => {
  await fetchData()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  charts.forEach(chart => chart?.dispose())
})
</script>

<style scoped lang="scss">
.analytics-container {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.analytics-tabs {
  :deep(.el-tabs__content) {
    overflow: visible;
  }
}

.tab-content {
  padding: 0 4px;
}

.chart-row {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.chart-container {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
  transition: all 0.3s;
  
  &:hover {
    background: #f0f0f0;
  }
  
  &.full-width {
    width: 100%;
    height: 320px;
  }
  
  &.half-width {
    width: 50%;
    height: 300px;
  }
  
  &.third-width {
    width: 33.33%;
    height: 280px;
  }
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}

@media (max-width: 1200px) {
  .chart-row {
    flex-wrap: wrap;
  }
  
  .chart-container {
    &.half-width,
    &.third-width {
      width: 100%;
      height: 280px;
    }
  }
}
</style>
