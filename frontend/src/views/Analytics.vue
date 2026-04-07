<template>
  <div class="analytics-container">
    <el-card class="mb-20">
      <template #header>
        <span>数据分析与统计</span>
      </template>

      <el-tabs v-model="activeTab" class="analytics-tabs" @tab-change="onTabChange">
        <el-tab-pane label="学生统计" name="student">
          <div class="chart-section">
            <div ref="collegeChartRef" class="chart-item full"></div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="成绩分析" name="score">
          <div class="chart-section">
            <div ref="gpaDistChartRef" class="chart-item half"></div>
            <div ref="scoreTrendChartRef" class="chart-item half"></div>
          </div>
          <div class="chart-section">
            <div ref="courseChartRef" class="chart-item full"></div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="画像分析" name="profile">
          <div class="chart-section three-col">
            <div ref="profileTypeChartRef" class="chart-item"></div>
            <div ref="abilityRadarChartRef" class="chart-item"></div>
            <div ref="knowledgeChartRef" class="chart-item"></div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="预警分析" name="warning">
          <div class="chart-section">
            <div ref="warningTypeChartRef" class="chart-item half"></div>
            <div ref="warningTrendChartRef" class="chart-item half"></div>
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
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { allStudents, allWarnings, collegeStats } from '@/data/mockData'

const activeTab = ref('student')

const collegeChartRef = ref(null)
const gpaDistChartRef = ref(null)
const scoreTrendChartRef = ref(null)
const courseChartRef = ref(null)
const profileTypeChartRef = ref(null)
const abilityRadarChartRef = ref(null)
const knowledgeChartRef = ref(null)
const warningTypeChartRef = ref(null)
const warningTrendChartRef = ref(null)

// 统计数据报表（从统一数据源计算）
const tableData = ref([
  { indicator: '学生总数', value: allStudents.length, rank: '-', change: 5.2, description: '学生总数' },
  { indicator: '在读学生', value: allStudents.filter(s => s.studentStatus === 1).length, rank: '-', change: 2.8, description: '在读学生数' },
  { indicator: '学业预警', value: allWarnings.length, rank: '-', change: -12.5, description: '预警学生数' },
  { indicator: '平均绩点', value: (allStudents.reduce((sum, s) => sum + s.gpa, 0) / allStudents.length).toFixed(2), rank: '-', change: 3.1, description: '平均绩点' }
])

let charts = {}

// 图表配置（使用统一数据源）
const chartConfig = {
  college: {
    title: '各学院学生分布',
    type: 'bar',
    data: collegeStats,
    color: '#409eff'
  },
  gpaDist: {
    title: 'GPA分数段分布',
    type: 'pie',
    data: [
      { name: '3.5-4.0', value: Math.round(allStudents.filter(s => s.gpa >= 3.5).length * 0.8), color: '#67c23a' },
      { name: '3.0-3.5', value: Math.round(allStudents.filter(s => s.gpa >= 3.0 && s.gpa < 3.5).length * 0.8), color: '#409eff' },
      { name: '2.5-3.0', value: Math.round(allStudents.filter(s => s.gpa >= 2.5 && s.gpa < 3.0).length * 0.8), color: '#e6a23c' },
      { name: '2.0-2.5', value: Math.round(allStudents.filter(s => s.gpa >= 2.0 && s.gpa < 2.5).length * 0.8), color: '#f56c6c' },
      { name: '2.0以下', value: Math.round(allStudents.filter(s => s.gpa < 2.0).length * 0.8), color: '#909399' }
    ]
  },
  scoreTrend: {
    title: '平均成绩趋势',
    type: 'line',
    data: ['2022-1', '2022-2', '2023-1', '2023-2', '2024-1'],
    values: [78, 80, 82, 85, 84],
    color: '#409eff'
  },
  course: {
    title: '课程难度分布',
    type: 'bar',
    data: [
      { name: '简单', value: 15 },
      { name: '较易', value: 25 },
      { name: '中等', value: 35 },
      { name: '较难', value: 18 },
      { name: '困难', value: 7 }
    ],
    color: '#e6a23c'
  },
  profileType: {
    title: '学生画像类型',
    type: 'pie',
    data: [
      { name: '学术研究型', value: Math.round(allStudents.filter(s => s.profileType === '学术研究型').length * 1.5), color: '#409eff' },
      { name: '实践应用型', value: Math.round(allStudents.filter(s => s.profileType === '实践应用型').length * 1.5), color: '#67c23a' },
      { name: '复合探索型', value: Math.round(allStudents.filter(s => s.profileType === '复合探索型').length * 1.5), color: '#e6a23c' },
      { name: '创新创业型', value: Math.round(allStudents.filter(s => s.profileType === '创新创业型').length * 1.5), color: '#f56c6c' }
    ]
  },
  ability: {
    title: '学生能力雷达',
    type: 'radar',
    indicator: ['学术', '实践', '科研', '创新', '协作', '综合'],
    values: [85, 92, 75, 88, 90, 85]
  },
  knowledge: {
    title: '知识领域分布',
    type: 'pie',
    data: [
      { name: '计算机基础', value: 30, color: '#409eff' },
      { name: '系统基础', value: 25, color: '#67c23a' },
      { name: '数据管理', value: 20, color: '#e6a23c' },
      { name: '人工智能', value: 15, color: '#f56c6c' },
      { name: '其他', value: 10, color: '#909399' }
    ]
  },
  warningType: {
    title: '预警类型分布',
    type: 'pie',
    data: [
      { name: '成绩预警', value: allWarnings.filter(w => w.warningType === '成绩预警').length, color: '#f56c6c' },
      { name: '学分预警', value: allWarnings.filter(w => w.warningType === '学分预警').length, color: '#e6a23c' },
      { name: '学业困难', value: allWarnings.filter(w => w.warningType === '学业困难').length, color: '#409eff' }
    ]
  },
  warningTrend: {
    title: '预警趋势变化',
    type: 'line',
    data: ['2023-09', '2023-10', '2023-11', '2023-12', '2024-01', '2024-02', '2024-03'],
    values: [12, 15, 11, 8, 6, 9, 8],
    color: '#f56c6c'
  }
}

const initChart = (ref, config) => {
  if (!ref || !config) return null

  const chart = echarts.init(ref)
  let option = {}

  if (config.type === 'bar') {
    option = {
      title: { text: config.title, left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      grid: { left: '3%', right: '4%', bottom: '10%', top: '20%', containLabel: true },
      xAxis: { type: 'category', data: config.data.map(d => d.name), axisLabel: { fontSize: 11, rotate: 30 } },
      yAxis: { type: 'value', axisLabel: { fontSize: 11 } },
      series: [{ type: 'bar', data: config.data.map(d => d.value), itemStyle: { color: config.color }, barWidth: '50%' }]
    }
  } else if (config.type === 'pie') {
    option = {
      title: { text: config.title, left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
      tooltip: { trigger: 'item' },
      legend: { bottom: 10, left: 'center', textStyle: { fontSize: 10 } },
      series: [{
        type: 'pie',
        radius: ['30%', '50%'],
        center: ['50%', '45%'],
        data: config.data.map(d => ({ name: d.name, value: d.value, itemStyle: { color: d.color } })),
        label: { fontSize: 11 }
      }]
    }
  } else if (config.type === 'line') {
    option = {
      title: { text: config.title, left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '10%', top: '20%', containLabel: true },
      xAxis: { type: 'category', data: config.data, axisLabel: { fontSize: 10 } },
      yAxis: { type: 'value', min: 60, max: 100, axisLabel: { fontSize: 10 } },
      series: [{
        type: 'line',
        smooth: true,
        data: config.values,
        areaStyle: { color: `${config.color}33` },
        itemStyle: { color: config.color },
        lineStyle: { width: 2 }
      }]
    }
  } else if (config.type === 'radar') {
    option = {
      title: { text: config.title, left: 'center', top: 10, textStyle: { fontSize: 14, fontWeight: 'bold' } },
      tooltip: {},
      radar: {
        center: ['50%', '50%'],
        radius: '55%',
        indicator: config.indicator.map((name) => ({ name, max: 100 })),
        axisName: { fontSize: 10 }
      },
      series: [{
        type: 'radar',
        data: [{ value: config.values, name: '能力分布', areaStyle: { color: 'rgba(64, 158, 255, 0.3)' }, itemStyle: { color: '#409eff' } }]
      }]
    }
  }

  chart.setOption(option)
  return chart
}

const initAllCharts = () => {
  nextTick(() => {
    if (!charts.college && collegeChartRef.value) {
      charts.college = initChart(collegeChartRef.value, chartConfig.college)
    }
    if (!charts.gpaDist && gpaDistChartRef.value) {
      charts.gpaDist = initChart(gpaDistChartRef.value, chartConfig.gpaDist)
    }
    if (!charts.scoreTrend && scoreTrendChartRef.value) {
      charts.scoreTrend = initChart(scoreTrendChartRef.value, chartConfig.scoreTrend)
    }
    if (!charts.course && courseChartRef.value) {
      charts.course = initChart(courseChartRef.value, chartConfig.course)
    }
    if (!charts.profileType && profileTypeChartRef.value) {
      charts.profileType = initChart(profileTypeChartRef.value, chartConfig.profileType)
    }
    if (!charts.ability && abilityRadarChartRef.value) {
      charts.ability = initChart(abilityRadarChartRef.value, chartConfig.ability)
    }
    if (!charts.knowledge && knowledgeChartRef.value) {
      charts.knowledge = initChart(knowledgeChartRef.value, chartConfig.knowledge)
    }
    if (!charts.warningType && warningTypeChartRef.value) {
      charts.warningType = initChart(warningTypeChartRef.value, chartConfig.warningType)
    }
    if (!charts.warningTrend && warningTrendChartRef.value) {
      charts.warningTrend = initChart(warningTrendChartRef.value, chartConfig.warningTrend)
    }
  })
}

const resizeCharts = () => {
  Object.values(charts).forEach(chart => chart?.resize())
}

const onTabChange = () => {
  nextTick(() => {
    resizeCharts()
  })
}

onMounted(() => {
  initAllCharts()
  window.addEventListener('resize', resizeCharts)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  Object.values(charts).forEach(chart => chart?.dispose())
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

.chart-section {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;

  &.three-col {
    gap: 16px;
  }

  &:last-child {
    margin-bottom: 0;
  }
}

.chart-item {
  background: #fafafa;
  border-radius: 8px;
  height: 320px;
  flex-shrink: 0;

  &.full {
    width: 100%;
  }

  &.half {
    width: calc(50% - 10px);
    min-width: 300px;
  }
}

.chart-section.three-col .chart-item {
  width: calc(33.33% - 12px);
  min-width: 200px;
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}

@media (max-width: 1200px) {
  .chart-section {
    flex-wrap: wrap;
  }

  .chart-item.half,
  .chart-section.three-col .chart-item {
    width: 100%;
  }
}
</style>