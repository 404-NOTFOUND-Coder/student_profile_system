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

        <el-tab-pane label="画像分析" name="profile">
          <div class="chart-section three-col">
            <div ref="profileTypeChartRef" class="chart-item"></div>
            <div ref="abilityRadarChartRef" class="chart-item"></div>
            <div ref="knowledgeChartRef" class="chart-item"></div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { allStudents, collegeStats } from '@/data/mockData'

const activeTab = ref('student')

const collegeChartRef = ref(null)
const profileTypeChartRef = ref(null)
const abilityRadarChartRef = ref(null)
const knowledgeChartRef = ref(null)

let charts = {}

// 图表配置（使用统一数据源）
const chartConfig = {
  college: {
    title: '各学院学生分布',
    type: 'bar',
    data: collegeStats,
    color: '#409eff'
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
    if (!charts.profileType && profileTypeChartRef.value) {
      charts.profileType = initChart(profileTypeChartRef.value, chartConfig.profileType)
    }
    if (!charts.ability && abilityRadarChartRef.value) {
      charts.ability = initChart(abilityRadarChartRef.value, chartConfig.ability)
    }
    if (!charts.knowledge && knowledgeChartRef.value) {
      charts.knowledge = initChart(knowledgeChartRef.value, chartConfig.knowledge)
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
}

.chart-section.three-col .chart-item {
  width: calc(33.33% - 12px);
  min-width: 200px;
}

@media (max-width: 1200px) {
  .chart-section {
    flex-wrap: wrap;
  }

  .chart-section.three-col .chart-item {
    width: 100%;
  }
}
</style>