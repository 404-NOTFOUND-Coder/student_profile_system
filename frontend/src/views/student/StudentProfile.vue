<template>
  <div class="student-profile" v-loading="loading">
    <!-- 头部信息卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card shadow="hover">
          <div class="profile-header">
            <el-avatar :size="80" :style="{ background: headerBgColor }">
              {{ profile.studentNo?.[0] || 'S' }}
            </el-avatar>
            <div class="header-info">
              <h2 class="student-name">{{ profile.studentNo }}</h2>
              <div class="basic-info">
                <el-tag size="small">{{ profile.studentNo }}</el-tag>
                <span class="info-item">
                  <el-icon><Male v-if="profile.gender === 1" /><Female v-else /></el-icon>
                  {{ profile.gender === 1 ? '男' : '女' }}
                </span>
                <span class="info-item">
                  <el-icon><School /></el-icon>
                  {{ profile.college }} · {{ profile.major }}
                </span>
                <span class="info-item">
                  <el-icon><Calendar /></el-icon>
                  {{ profile.enrollmentYear }}级
                </span>
              </div>
            </div>
            <div class="header-score">
              <div class="score-circle">
                <el-progress type="circle" :percentage="profile.profileScore || 0"
                             :width="100" :color="scoreColor">
                  <template #default>
                    <span class="score-value">{{ profile.profileScore || 0 }}</span>
                    <span class="score-label">综合评分</span>
                  </template>
                </el-progress>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 核心指标 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <el-card shadow="hover" class="metric-card">
          <div class="metric-header">
            <el-icon size="24" color="#409eff"><Odometer /></el-icon>
            <span>知识结构熵</span>
          </div>
          <div class="metric-value">{{ profile.knowledgeEntropy || 0 }}</div>
          <div class="metric-desc">越高表示知识面越广泛</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="metric-card">
          <div class="metric-header">
            <el-icon size="24" color="#67c23a"><DataLine /></el-icon>
            <span>图编辑距离</span>
          </div>
          <div class="metric-value">{{ profile.graphEditDistance || 0 }}</div>
          <div class="metric-desc">偏离标准培养方案程度</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="metric-card">
          <div class="metric-header">
            <el-icon size="24" color="#f56c6c"><Medal /></el-icon>
            <span>活动得分</span>
          </div>
          <div class="metric-value">{{ profile.activityScore || 0 }}</div>
          <div class="metric-desc">课外活动累计得分</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 左栏 -->
      <el-col :span="16">
        <!-- 画像类型 -->
        <el-card shadow="hover" class="mb-20">
          <template #header>
            <span>🎯 画像类型分析</span>
          </template>
          <div class="profile-type-section">
            <div class="profile-type-tag" :class="profile.profileType">
              {{ profile.profileType || '待分析' }}
            </div>
            <div class="profile-ability">
              <span>能力等级：</span>
              <el-rate v-model="abilityLevel" disabled show-text :texts="['C', 'B-', 'B', 'B+', 'A', 'A+']" />
            </div>
            <div class="personality-tags">
              <span class="label">人格特征：</span>
              <el-tag v-for="tag in personalityList" :key="tag" size="small" class="mr-8">
                {{ tag }}
              </el-tag>
            </div>
          </div>
          <div ref="radarChartRef" class="radar-chart"></div>
        </el-card>

        <!-- 活动时间线 -->
        <el-card shadow="hover" class="mb-20">
          <template #header>
            <span>⏰ 关键事件时间线</span>
          </template>
          <div class="timeline">
            <div v-for="(event, index) in timeline" :key="index" class="timeline-item">
              <div class="timeline-date">{{ event.date }}</div>
              <div class="timeline-content">
                <el-tag size="small" :type="getEventType(event.eventType)">
                  {{ event.eventType }}
                </el-tag>
                <div class="event-name">{{ event.eventName }}</div>
                <div class="event-desc">{{ event.description }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右栏 -->
      <el-col :span="8">
        <!-- 画像标签 -->
        <el-card shadow="hover" class="mb-20">
          <template #header>
            <span>🏷️ 画像标签</span>
          </template>
          <div class="tag-section">
            <div class="tag-category">
              <span class="category-title">学术标签</span>
              <div class="tags">
                <span v-for="tag in profile.academicTags" :key="tag" class="profile-tag academic">
                  {{ tag }}
                </span>
              </div>
            </div>
            <div class="tag-category">
              <span class="category-title">实践标签</span>
              <div class="tags">
                <span v-for="tag in profile.practiceTags" :key="tag" class="profile-tag practice">
                  {{ tag }}
                </span>
              </div>
            </div>
            <div class="tag-category">
              <span class="category-title">发展建议</span>
              <div class="tags">
                <span v-for="tag in profile.suggestionTags" :key="tag" class="profile-tag suggestion">
                  {{ tag }}
                </span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 证据链 -->
        <el-card shadow="hover" class="mb-20">
          <template #header>
            <span>📋 证据链</span>
          </template>
          <div class="evidence-list">
            <div v-for="evidence in evidenceChain" :key="evidence.evidenceId" class="evidence-item">
              <div class="evidence-header">
                <el-tag size="small">{{ evidence.evidenceType }}</el-tag>
                <span class="relevance">{{ (evidence.relevance * 100).toFixed(0) }}%</span>
              </div>
              <div class="evidence-content">{{ evidence.content }}</div>
              <div class="evidence-source">来源: {{ evidence.source }}</div>
            </div>
          </div>
        </el-card>

        <!-- 相似学生 -->
        <el-card shadow="hover" class="mb-20">
          <template #header>
            <span>👥 相似学生</span>
          </template>
          <div class="similar-list">
            <div v-for="student in similarStudents" :key="student.studentNo"
                 class="similar-item" @click="goToProfile(student.studentNo)">
              <el-avatar :size="36">{{ student.studentNo?.[0] }}</el-avatar>
              <div class="similar-info">
                <div class="similar-name">{{ student.studentNo }}</div>
                <div class="similar-meta">{{ student.profileType }}</div>
              </div>
              <span class="similarity">{{ (student.similarity * 100).toFixed(0) }}%</span>
            </div>
          </div>
        </el-card>

        <!-- 学业预警 -->
        <el-card shadow="hover" v-if="warnings.length > 0">
          <template #header>
            <span class="warning-header">⚠️ 学业预警</span>
          </template>
          <div class="warning-list">
            <el-alert v-for="warning in warnings" :key="warning.id"
                      :title="warning.warningMessage"
                      :type="getWarningAlertType(warning.level)"
                      :description="warning.suggestAction"
                      show-icon :closable="false" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { allStudents, similarStudents as studentsSimilarData, allWarnings } from '@/data/mockData'

const route = useRoute()
const router = useRouter()

const loading = ref(false)

const profile = ref({})
const evidenceChain = ref([])
const timeline = ref([])
const warnings = ref([])
const similarStudents = ref([])

const radarChartRef = ref(null)

let radarChart = null

const abilityLevel = computed(() => {
  const levelMap = { 'A+': 5, 'A': 5, 'A-': 4.5, 'B+': 4, 'B': 3.5, 'B-': 3, 'C': 2 }
  return levelMap[profile.value.abilityLevel] || 3
})

const personalityList = computed(() => {
  return profile.value.personality?.split(',').map(p => p.trim()) || []
})

const headerBgColor = computed(() => {
  const type = profile.value.profileType
  if (type === '学术研究型') return 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  if (type === '实践应用型') return 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  if (type === '复合探索型') return 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  return '#409eff'
})

const scoreColor = computed(() => {
  const score = profile.value.profileScore || 0
  if (score >= 85) return '#67c23a'
  if (score >= 70) return '#e6a23c'
  return '#f56c6c'
})

// 获取画像数据（使用统一数据源）
const fetchProfile = () => {
  const studentNo = route.params.studentNo || '2021001001'
  loading.value = true

  // 从统一数据源获取学生数据
  const studentData = allStudents.find(s => s.studentNo === studentNo)

  if (studentData) {
    profile.value = { ...studentData }
    evidenceChain.value = studentData.evidenceChain || []
    timeline.value = studentData.timeline || []
    warnings.value = allWarnings.filter(w => w.studentNo === studentNo)
    similarStudents.value = studentsSimilarData[studentNo] || []
  } else {
    // 默认使用第一个学生
    const defaultStudent = allStudents[0]
    profile.value = { ...defaultStudent }
    evidenceChain.value = defaultStudent.evidenceChain || []
    timeline.value = defaultStudent.timeline || []
    warnings.value = []
    similarStudents.value = studentsSimilarData[defaultStudent.studentNo] || []
  }

  nextTick(() => {
    initCharts()
    loading.value = false
  })
}

const initCharts = () => {
  // 雷达图
  if (radarChartRef.value) {
    if (radarChart) radarChart.dispose()
    radarChart = echarts.init(radarChartRef.value)
    radarChart.setOption({
      radar: {
        indicator: [
          { name: '学术能力', max: 100 },
          { name: '实践能力', max: 100 },
          { name: '科研能力', max: 100 },
          { name: '创新能力', max: 100 },
          { name: '协作能力', max: 100 },
          { name: '综合素质', max: 100 }
        ],
        radius: '65%'
      },
      series: [{
        type: 'radar',
        data: [{
          value: [85, 92, 75, 88, 90, 85],
          name: '能力分布',
          areaStyle: { color: 'rgba(64, 158, 255, 0.3)' },
          lineStyle: { color: '#409eff' },
          itemStyle: { color: '#409eff' }
        }]
      }]
    })
  }
}

// 辅助方法
const getTrendType = (trend) => ({ RISING: 'success', STABLE: 'warning', DECLINING: 'danger' }[trend] || 'info')
const getTrendName = (trend) => ({ RISING: '上升', STABLE: '稳定', DECLINING: '下降' }[trend] || '未知')
const getEventType = (type) => ({ '学业': 'success', '竞赛': 'warning', '实践': 'primary', '科研': 'info' }[type] || 'info')
const getWarningAlertType = (level) => ({ 1: 'info', 2: 'warning', 3: 'error' }[level] || 'info')
const goToProfile = (studentNo) => router.push(`/student/profile/${studentNo}`)

onMounted(() => {
  fetchProfile()
  window.addEventListener('resize', () => {
    radarChart?.resize()
  })
})
</script>

<style scoped lang="scss">
.student-profile {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.mb-20 {
  margin-bottom: 20px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;

  .header-info {
    flex: 1;

    .student-name {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 8px;
    }

    .basic-info {
      display: flex;
      align-items: center;
      gap: 16px;
      color: #606266;

      .info-item {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }

  .header-score {
    .score-value {
      font-size: 24px;
      font-weight: bold;
      display: block;
    }
    .score-label {
      font-size: 12px;
      color: #909399;
    }
  }
}

.metric-card {
  text-align: center;

  .metric-header {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    margin-bottom: 12px;
    color: #606266;
  }

  .metric-value {
    font-size: 32px;
    font-weight: bold;
    color: #303133;
    margin: 8px 0;
  }

  .metric-desc {
    font-size: 12px;
    color: #909399;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-type-section {
  margin-bottom: 20px;

  .profile-type-tag {
    display: inline-block;
    padding: 8px 24px;
    border-radius: 20px;
    font-weight: bold;
    font-size: 16px;
    margin-bottom: 16px;

    &.学术研究型 { background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; }
    &.实践应用型 { background: linear-gradient(135deg, #43e97b, #38f9d7); color: #fff; }
    &.复合探索型 { background: linear-gradient(135deg, #fa709a, #fee140); color: #fff; }
  }

  .profile-ability {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;
  }

  .personality-tags {
    display: flex;
    align-items: center;
    flex-wrap: wrap;

    .label {
      font-weight: bold;
      margin-right: 8px;
    }
  }
}

.radar-chart {
  width: 100%;
  height: 280px;
}

.timeline {
  position: relative;
  padding-left: 20px;

  &::before {
    content: '';
    position: absolute;
    left: 6px;
    top: 0;
    bottom: 0;
    width: 2px;
    background: #dcdfe6;
  }

  .timeline-item {
    position: relative;
    margin-bottom: 20px;

    &::before {
      content: '';
      position: absolute;
      left: -18px;
      top: 4px;
      width: 10px;
      height: 10px;
      background: #409eff;
      border-radius: 50%;
      border: 2px solid #fff;
    }

    .timeline-date {
      font-size: 12px;
      color: #909399;
      margin-bottom: 4px;
    }

    .timeline-content {
      background: #f5f7fa;
      padding: 12px;
      border-radius: 8px;

      .event-name {
        font-weight: bold;
        margin: 8px 0 4px;
      }

      .event-desc {
        font-size: 12px;
        color: #606266;
      }
    }
  }
}

.tag-section {
  .tag-category {
    margin-bottom: 16px;

    .category-title {
      font-weight: bold;
      display: block;
      margin-bottom: 8px;
      color: #303133;
    }

    .tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }
  }
}

.profile-tag {
  &.academic { background: #ecf5ff; color: #409eff; padding: 4px 12px; border-radius: 16px; }
  &.practice { background: #f0f9eb; color: #67c23a; padding: 4px 12px; border-radius: 16px; }
  &.suggestion { background: #fef0f0; color: #f56c6c; padding: 4px 12px; border-radius: 16px; }
}

.evidence-list {
  .evidence-item {
    padding: 12px;
    border-bottom: 1px solid #ebeef5;

    &:last-child { border-bottom: none; }

    .evidence-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;

      .relevance {
        color: #67c23a;
        font-weight: bold;
      }
    }

    .evidence-content {
      color: #303133;
      margin-bottom: 4px;
    }

    .evidence-source {
      font-size: 12px;
      color: #909399;
    }
  }
}

.similar-list {
  .similar-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 8px;
    margin-bottom: 12px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      background: #ecf5ff;
      transform: translateX(4px);
    }

    .similar-info {
      flex: 1;

      .similar-name {
        font-weight: bold;
      }

      .similar-meta {
        font-size: 12px;
        color: #909399;
      }
    }

    .similarity {
      color: #409eff;
      font-weight: bold;
    }
  }
}

.warning-header {
  color: #f56c6c;
}

.warning-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.mr-8 { margin-right: 8px; }
</style>