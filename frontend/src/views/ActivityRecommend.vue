<template>
  <div class="activity-recommend">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学业活动推荐</span>
        </div>
      </template>

      <!-- 筛选表单 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="学号">
          <el-input v-model="queryForm.studentNo" placeholder="请输入学号" clearable />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="queryForm.type" placeholder="请选择类型" clearable class="select-wide">
            <el-option label="学科竞赛" value="学科竞赛" />
            <el-option label="学术科研" value="学术科研" />
            <el-option label="志愿服务" value="志愿服务" />
            <el-option label="社会实践" value="社会实践" />
            <el-option label="创新创业" value="创新创业" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 历史参与活动 + 动态推荐 -->
      <el-card class="mb-20" shadow="never">
        <template #header>
          <div class="section-header">
            <span>历史参与活动</span>
            <el-button type="primary" size="small" @click="generateDynamicRecommend" :loading="recommendLoading">
              <el-icon><MagicStick /></el-icon>
              基于历史活动生成推荐
            </el-button>
          </div>
        </template>

        <div v-if="studentHistoryActivities.length > 0" class="history-activities">
          <el-tag
            v-for="activity in studentHistoryActivities"
            :key="activity.id"
            class="activity-tag"
            :type="getTypeTag(activity.type)"
          >
            {{ activity.name }} ({{ activity.time }})
          </el-tag>
        </div>
        <el-empty v-else description="暂无参与活动记录" />
      </el-card>

      <!-- 动态推荐结果 -->
      <el-card v-if="dynamicRecommendations.length > 0" class="mb-20 recommend-result" shadow="never">
        <template #header>
          <div class="section-header">
            <span>动态推荐结果</span>
            <el-tag type="success">基于您的活动历史生成</el-tag>
          </div>
        </template>

        <div class="recommend-list">
          <div v-for="(rec, index) in dynamicRecommendations" :key="index" class="recommend-item">
            <div class="recommend-header">
              <h4>{{ rec.activity }}</h4>
              <el-progress :percentage="rec.matchScore" :color="getScoreColor(rec.matchScore)" />
            </div>
            <div class="recommend-reason">{{ rec.reason }}</div>
            <div class="recommend-meta">
              <el-tag size="small" :type="getTypeTag(rec.type)">{{ rec.type }}</el-tag>
              <el-tag size="small" type="info">{{ rec.level }}</el-tag>
              <span class="time-hint">建议参与时间：{{ rec.suggestedTime }}</span>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 推荐列表 -->
      <el-table :data="tableData" stripe border v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="studentNo" label="学号" width="180" />
        <el-table-column prop="activity" label="推荐活动" min-width="200" />
        <el-table-column prop="reason" label="推荐理由" min-width="250" />
        <el-table-column prop="matchScore" label="匹配度" width="100">
          <template #default="{ row }">
            <el-progress :percentage="row.matchScore" :color="getScoreColor(row.matchScore)" />
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { activityRecommendations, allActivities } from '@/data/mockData'

const loading = ref(false)
const recommendLoading = ref(false)
const tableData = ref([])
const studentHistoryActivities = ref([])
const dynamicRecommendations = ref([])

const queryForm = reactive({
  studentNo: '',
  type: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 监听学号变化，加载历史活动
watch(() => queryForm.studentNo, (newNo) => {
  if (newNo && newNo.length === 13) {
    loadStudentHistory(newNo)
  } else {
    studentHistoryActivities.value = []
    dynamicRecommendations.value = []
  }
})

// 加载学生历史活动
const loadStudentHistory = (studentNo) => {
  const history = allActivities.filter(a =>
    a.participants && a.participants.includes(studentNo)
  )
  studentHistoryActivities.value = history
}

// 生成动态推荐
const generateDynamicRecommend = () => {
  if (!queryForm.studentNo) {
    ElMessage.warning('请先输入学号')
    return
  }

  if (studentHistoryActivities.value.length === 0) {
    ElMessage.warning('该学生暂无参与活动记录，无法生成动态推荐')
    return
  }

  recommendLoading.value = true

  // 模拟AI分析历史活动并生成推荐
  setTimeout(() => {
    const historyTypes = [...new Set(studentHistoryActivities.value.map(a => a.type))]
    const historyLevels = [...new Set(studentHistoryActivities.value.map(a => a.level))]

    const allTypes = ['学科竞赛', '学术科研', '志愿服务', '社会实践', '创新创业']
    const allLevels = ['院级', '校级', '省部级', '国家级']

    // 找出学生未参与但可能感兴趣的类型
    const suggestedTypes = allTypes.filter(t => !historyTypes.includes(t))
    const suggestedLevels = allLevels.filter(l => !historyLevels.includes(l) || l === '校级')

    const recommendations = []
    const activityPool = allActivities.filter(a =>
      suggestedTypes.includes(a.type) && suggestedLevels.includes(a.level)
    )

    // 生成3-5条推荐
    const count = Math.min(activityPool.length, 5)
    for (let i = 0; i < count; i++) {
      const activity = activityPool[i]
      if (activity) {
        recommendations.push({
          activity: activity.name,
          type: activity.type,
          level: activity.level,
          matchScore: Math.floor(70 + Math.random() * 25),
          reason: generateReason(activity, studentHistoryActivities.value),
          suggestedTime: generateSuggestedTime(activity)
        })
      }
    }

    dynamicRecommendations.value = recommendations
    recommendLoading.value = false

    if (recommendations.length > 0) {
      ElMessage.success('已基于历史活动生成个性化推荐')
    } else {
      ElMessage.info('暂无合适的推荐活动')
    }
  }, 1500)
}

// 生成推荐理由
const generateReason = (activity, history) => {
  const historyNames = history.map(h => h.name).slice(0, 2).join('、')
  const reasons = [
    `您曾参与${historyNames}等活动，与本活动主题相关度高`,
    `基于您参与的${activity.type}类型活动，本活动非常适合您`,
    `您具备参与此类活动的经验，本活动可进一步提升您的能力`,
    `与您之前的活动经历形成梯度递进，建议挑战更高难度`
  ]
  return reasons[Math.floor(Math.random() * reasons.length)]
}

// 生成建议时间
const generateSuggestedTime = (activity) => {
  const currentYear = new Date().getFullYear()
  const seasons = ['上半年', '下半年']
  const season = seasons[Math.floor(Math.random() * seasons.length)]
  return `${currentYear}-${season}`
}

// 加载推荐数据（从统一数据源）
const loadData = () => {
  loading.value = true

  let filtered = [...activityRecommendations]

  if (queryForm.studentNo) {
    filtered = filtered.filter(r => r.studentNo.includes(queryForm.studentNo))
  }

  pagination.total = filtered.length
  const start = (pagination.pageNum - 1) * pagination.pageSize
  const end = start + pagination.pageSize
  tableData.value = filtered.slice(start, end)

  loading.value = false
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  queryForm.studentNo = ''
  queryForm.type = ''
  pagination.pageNum = 1
  studentHistoryActivities.value = []
  dynamicRecommendations.value = []
  loadData()
}

const getScoreColor = (score) => {
  if (score >= 90) return '#67c23a'
  if (score >= 75) return '#409eff'
  if (score >= 60) return '#e6a23c'
  return '#909399'
}

const getTypeTag = (type) => {
  const types = {
    '学科竞赛': 'primary',
    '学术科研': 'success',
    '志愿服务': 'warning',
    '社会实践': 'info',
    '创新创业': 'danger'
  }
  return types[type] || 'info'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.activity-recommend {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;

  .select-wide {
    width: 160px;
  }
}

.mb-20 {
  margin-bottom: 20px;
}

.history-activities {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  .activity-tag {
    margin-right: 8px;
    margin-bottom: 8px;
  }
}

.recommend-result {
  background: #f0f9ff;
  border: 1px solid #409eff;
}

.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.recommend-item {
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

  .recommend-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;

    h4 {
      margin: 0;
      color: #303133;
    }

    .el-progress {
      width: 120px;
    }
  }

  .recommend-reason {
    color: #606266;
    font-size: 14px;
    margin-bottom: 12px;
    padding-left: 8px;
    border-left: 3px solid #409eff;
  }

  .recommend-meta {
    display: flex;
    align-items: center;
    gap: 8px;

    .time-hint {
      margin-left: auto;
      color: #909399;
      font-size: 12px;
    }
  }
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>