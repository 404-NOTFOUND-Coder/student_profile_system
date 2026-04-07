<template>
  <div class="dashboard">
    <div class="stats-row mb-20">
      <el-card shadow="hover" class="stats-card">
        <el-icon size="40" color="#409eff"><User /></el-icon>
        <div class="stats-value">{{ stats.totalStudents }}</div>
        <div class="stats-label">学生总数</div>
      </el-card>
      <el-card shadow="hover" class="stats-card">
        <el-icon size="40" color="#67c23a"><CircleCheck /></el-icon>
        <div class="stats-value">{{ stats.activeStudents }}</div>
        <div class="stats-label">在读学生</div>
      </el-card>
      <el-card shadow="hover" class="stats-card">
        <el-icon size="40" color="#e6a23c"><DataLine /></el-icon>
        <div class="stats-value">{{ stats.avgGpa }}</div>
        <div class="stats-label">平均绩点</div>
      </el-card>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>学生分布 - 各学院</span>
          </template>
          <div ref="collegeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>学生画像示例</span>
          </template>
          <div class="profile-list">
            <div v-for="profile in sampleProfiles" :key="profile.studentNo"
                 class="profile-item" @click="goToProfile(profile.studentNo)">
              <el-avatar :size="40" :style="{ background: profile.avatarColor }">
                {{ profile.studentNo[0] }}
              </el-avatar>
              <div class="profile-info">
                <div class="profile-name">{{ profile.studentNo }}</div>
                <div class="profile-meta">
                  <span class="profile-type">{{ profile.profileType }}</span>
                  <span class="profile-gpa">GPA: {{ profile.gpa }}</span>
                </div>
              </div>
              <el-tag :type="profile.abilityType">{{ profile.abilityLevel }}</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { allStudents, collegeStats } from '@/data/mockData'

const router = useRouter()

// 获取用户角色
const userRole = localStorage.getItem('userRole') || 'admin'
const isStudent = computed(() => userRole === 'student')

// 统计数据（从统一数据源计算）
const stats = reactive({
  totalStudents: allStudents.length,
  activeStudents: allStudents.filter(s => s.studentStatus === 1).length,
  avgGpa: parseFloat((allStudents.reduce((sum, s) => sum + s.gpa, 0) / allStudents.length).toFixed(2))
})

// 样本画像（使用核心学生的画像数据）
const sampleProfiles = ref([
  { studentNo: '2020010906005', profileType: '学术研究型', gpa: 3.85, abilityLevel: 'A', abilityType: 'success', avatarColor: '#409eff' },
  { studentNo: '2020010906012', profileType: '实践应用型', gpa: 3.12, abilityLevel: 'B+', abilityType: 'warning', avatarColor: '#67c23a' },
  { studentNo: '2020010906013', profileType: '复合探索型', gpa: 3.42, abilityLevel: 'B+', abilityType: 'info', avatarColor: '#e6a23c' }
])

const collegeChartRef = ref(null)
let collegeChart = null

const initCharts = () => {
  if (collegeChartRef.value) {
    collegeChart = echarts.init(collegeChartRef.value)
    updateCollegeChart()
  }
}

const updateCollegeChart = () => {
  if (!collegeChart) {
    collegeChart = echarts.init(collegeChartRef.value)
  }

  const data = collegeStats

  collegeChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    xAxis: {
      type: 'category',
      data: data.map(item => item.name),
      axisLabel: { rotate: data.length > 5 ? 30 : 0 }
    },
    yAxis: { type: 'value' },
    series: [{
      type: 'bar',
      data: data.map(item => item.value),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#409eff' },
          { offset: 1, color: '#67c23a' }
        ])
      },
      barWidth: '50%'
    }],
    grid: { left: '3%', right: '4%', bottom: '15%', top: '10%', containLabel: true }
  })
}

const goToProfile = (studentNo) => {
  router.push(`/student/profile/${studentNo}`)
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', () => {
    collegeChart?.resize()
  })
})
</script>

<style scoped lang="scss">
.dashboard {
  padding: 20px;
}

.stats-row {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;

  .stats-card {
    flex: 1;
    text-align: center;
    padding: 24px 0;
    min-width: 0;

    .el-icon {
      margin-bottom: 12px;
    }

    .stats-value {
      font-size: 28px;
      font-weight: bold;
      color: #303133;
      margin: 12px 0;
    }

    .stats-label {
      color: #909399;
      font-size: 14px;
    }
  }
}

.mb-20 {
  margin-bottom: 24px;
}

.chart-container {
  width: 100%;
  height: 300px;
  margin: 16px 0;
}

.profile-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.profile-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  background: #f5f7fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  flex: 1;
  min-width: 280px;

  &:hover {
    background: #ecf5ff;
    transform: translateX(4px);
  }

  .profile-info {
    flex: 1;

    .profile-name {
      font-weight: bold;
      margin-bottom: 4px;
    }

    .profile-meta {
      font-size: 12px;
      color: #909399;

      .profile-type {
        margin-right: 12px;
      }
    }
  }
}
</style>