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
        <el-icon size="40" color="#f56c6c"><Warning /></el-icon>
        <div class="stats-value">{{ stats.warningCount }}</div>
        <div class="stats-label">学业预警</div>
      </el-card>
      <el-card v-if="isStudent" shadow="hover" class="stats-card">
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
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近学业预警</span>
          </template>
          <el-table :data="warnings" style="width: 100%">
            <el-table-column prop="studentNo" label="学号" width="120" />
            <el-table-column prop="studentName" label="姓名" width="100" />
            <el-table-column prop="warningType" label="预警类型" width="100" />
            <el-table-column prop="level" label="等级">
              <template #default="{ row }">
                <el-tag :type="getWarningType(row.level)" size="small">
                  {{ row.levelName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="时间" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>学生画像示例</span>
          </template>
          <div v-for="profile in sampleProfiles" :key="profile.studentNo" 
               class="profile-item" @click="goToProfile(profile.studentNo)">
            <el-avatar :size="40" :style="{ background: profile.avatarColor }">
              {{ profile.name[0] }}
            </el-avatar>
            <div class="profile-info">
              <div class="profile-name">{{ profile.name }}</div>
              <div class="profile-meta">
                <span class="profile-type">{{ profile.profileType }}</span>
                <span class="profile-gpa">GPA: {{ profile.gpa }}</span>
              </div>
            </div>
            <el-tag :type="profile.abilityType">{{ profile.abilityLevel }}</el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import request from '@/api/index'

const router = useRouter()

// 获取用户角色
const userRole = localStorage.getItem('userRole') || 'admin'
const isStudent = computed(() => userRole === 'student')

const stats = reactive({
  totalStudents: 0,
  activeStudents: 0,
  warningCount: 0,
  avgGpa: 0
})

const warnings = ref([])
const sampleProfiles = ref([])

const collegeChartRef = ref(null)
const collegeChartData = ref([])

let collegeChart = null

const initCharts = () => {
  // 学院分布图
  collegeChart = echarts.init(collegeChartRef.value)
  updateCollegeChart()
}

const updateCollegeChart = () => {
  if (!collegeChart) {
    collegeChart = echarts.init(collegeChartRef.value)
  }

  const data = collegeChartData.value.map(item => ({
    name: item.name || item.college,
    value: item.value || item.count || 0
  }))

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

const loadData = async () => {
  try {
    // 调用后端API获取统计数据
    const statsRes = await request.get('/dashboard/stats')
    console.log('statsRes:', statsRes)
    if (statsRes.code === 200 && statsRes.data) {
      stats.totalStudents = statsRes.data.totalStudents || 0
      stats.activeStudents = statsRes.data.activeStudents || 0
      stats.warningCount = statsRes.data.warningCount || 0
      stats.avgGpa = statsRes.data.avgGpa || 0
    }

    // 调用后端API获取预警列表
    const warningsRes = await request.get('/dashboard/warnings')
    console.log('warningsRes:', warningsRes)
    if (warningsRes.code === 200 && warningsRes.data) {
      warnings.value = warningsRes.data.map(w => ({
        studentNo: w.studentNo || '',
        studentName: w.studentName || w.name || '',
        warningType: w.warningType || '成绩预警',
        level: w.level || 1,
        levelName: w.levelName || '一般',
        createTime: w.createTime ? new Date(w.createTime).toLocaleDateString() : ''
      }))
    }

    // 调用后端API获取画像示例
    const profilesRes = await request.get('/dashboard/profile-examples')
    console.log('profilesRes:', profilesRes)
    if (profilesRes.code === 200 && profilesRes.data) {
      sampleProfiles.value = profilesRes.data.map(p => ({
        studentNo: p.studentNo || '',
        name: p.name || '未知',
        profileType: p.profileType || '待分析',
        gpa: p.gpa || 0,
        abilityLevel: p.abilityLevel || 'C',
        abilityType: p.abilityType || 'info',
        avatarColor: p.avatarColor || '#409eff'
      }))
    }

    // 调用后端API获取学院统计数据
    const collegeRes = await request.get('/dashboard/college-stats')
    console.log('collegeRes:', collegeRes)
    if (collegeRes.code === 200 && collegeRes.data) {
      collegeChartData.value = collegeRes.data
      updateCollegeChart()
    }

  } catch (error) {
    console.error('加载数据失败:', error)
    // 如果API调用失败，使用模拟数据作为后备
    stats.totalStudents = 40
    stats.activeStudents = 38
    stats.warningCount = 5
    stats.avgGpa = 3.45
    warnings.value = []
    sampleProfiles.value = []
  }
}

const getWarningType = (level) => {
  const types = { 1: 'info', 2: 'warning', 3: 'danger' }
  return types[level] || 'info'
}

const goToProfile = (studentNo) => {
  router.push(`/student/profile/${studentNo}`)
}

onMounted(() => {
  loadData()
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

.profile-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  margin-bottom: 12px;
  background: #f5f7fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  
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
