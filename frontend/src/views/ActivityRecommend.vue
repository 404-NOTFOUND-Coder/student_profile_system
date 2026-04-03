<template>
  <div class="activity-recommend-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>活动推荐</span>
          <el-button type="primary" @click="generateRecommendations">刷新推荐</el-button>
        </div>
      </template>

      <!-- 筛选条件 -->
      <div class="filter-section">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="活动类型">
            <el-select v-model="filterForm.category" placeholder="选择类型" clearable class="select-wide">
              <el-option v-for="cat in categories" :key="cat.value" :label="cat.label" :value="cat.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="活动级别">
            <el-select v-model="filterForm.level" placeholder="选择级别" clearable class="select-wide">
              <el-option v-for="level in levels" :key="level.value" :label="level.label" :value="level.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="推荐状态">
            <el-select v-model="filterForm.status" placeholder="选择状态" clearable class="select-wide">
              <el-option label="待处理" value="pending" />
              <el-option label="感兴趣" value="interested" />
              <el-option label="已报名" value="applied" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadRecommendations">查询</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 推荐列表 -->
      <div class="recommend-list">
        <el-row :gutter="20">
          <el-col v-for="item in recommendations" :key="item.id" :span="12" class="mb-20">
            <el-card shadow="hover" class="recommend-card">
              <div class="recommend-header">
                <div class="activity-info">
                  <h3>{{ item.activityName }}</h3>
                  <div class="tags">
                    <el-tag type="primary" size="small">{{ item.category }}</el-tag>
                    <el-tag v-if="item.subCategory" type="success" size="small">{{ item.subCategory }}</el-tag>
                    <el-tag :type="getLevelType(item.level)" size="small">{{ item.level }}</el-tag>
                  </div>
                </div>
                <div class="recommend-score">
                  <el-progress type="circle" :percentage="Math.round(item.recommendScore)" :width="60" :stroke-width="6">
                    <template #default>
                      <span class="score-value">{{ Math.round(item.recommendScore) }}</span>
                    </template>
                  </el-progress>
                  <span class="score-label">推荐度</span>
                </div>
              </div>
              
              <div class="recommend-body">
                <div class="info-row">
                  <span class="label">主办方:</span>
                  <span>{{ item.organizer || '未知' }}</span>
                </div>
                <div class="info-row">
                  <span class="label">报名截止:</span>
                  <span>{{ formatDate(item.registerEndTime) }}</span>
                  <el-tag v-if="getRemainingDays(item.registerEndTime) <= 7 && getRemainingDays(item.registerEndTime) > 0" type="danger" size="small">
                    剩余 {{ getRemainingDays(item.registerEndTime) }} 天
                  </el-tag>
                </div>
                <div class="info-row">
                  <span class="label">活动时间:</span>
                  <span>{{ formatDateRange(item.activityStartTime, item.activityEndTime) }}</span>
                </div>
                <div class="info-row">
                  <span class="label">活动地点:</span>
                  <span>{{ item.location || '待定' }}</span>
                </div>
                <div class="reason">
                  <span class="label">推荐理由:</span>
                  <p>{{ item.recommendReason }}</p>
                </div>
              </div>
              
              <div class="recommend-footer">
                <el-button size="small" @click="showDetail(item)">查看详情</el-button>
                <el-button type="primary" size="small" @click="updateStatus(item.id, 'interested')">感兴趣</el-button>
                <el-button type="success" size="small" @click="updateStatus(item.id, 'applied')">立即报名</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-empty v-if="recommendations.length === 0" description="暂无推荐内容" />
      </div>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" :title="currentActivity?.activityName" width="600px">
      <div class="activity-detail" v-if="currentActivity">
        <div class="detail-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="活动类型">{{ currentActivity.category }}</el-descriptions-item>
            <el-descriptions-item label="活动级别">{{ currentActivity.level }}</el-descriptions-item>
            <el-descriptions-item label="主办方">{{ currentActivity.organizer }}</el-descriptions-item>
            <el-descriptions-item label="活动地点">{{ currentActivity.location }}</el-descriptions-item>
            <el-descriptions-item label="报名截止">{{ formatDate(currentActivity.registerEndTime) }}</el-descriptions-item>
            <el-descriptions-item label="活动开始">{{ formatDate(currentActivity.activityStartTime) }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="detail-section">
          <h4>奖励/收获</h4>
          <p>{{ currentActivity.reward || '暂无说明' }}</p>
        </div>
        
        <div class="detail-section">
          <h4>推荐理由</h4>
          <p>{{ currentActivity.recommendReason }}</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleApply">立即报名</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const recommendations = ref([])
const filterForm = reactive({
  category: '',
  level: '',
  status: ''
})

const detailVisible = ref(false)
const currentActivity = ref(null)

const categories = [
  { value: '比赛获奖', label: '比赛获奖' },
  { value: '培训经历', label: '培训经历' },
  { value: '工作经历', label: '工作经历' },
  { value: '学生活动', label: '学生活动' },
  { value: '个人荣誉', label: '个人荣誉' },
  { value: '资格认证', label: '资格认证' }
]

const levels = [
  { value: 1, label: '院级' },
  { value: 2, label: '校级' },
  { value: 3, label: '省部级' },
  { value: 4, label: '国家级' },
  { value: 5, label: '国际级' }
]

// 模拟数据
const mockData = [
  {
    id: 1,
    activityName: '全国大学生程序设计竞赛',
    category: '比赛获奖',
    subCategory: '科技创新',
    level: '国家级',
    levelValue: 4,
    organizer: '教育部高等学校计算机类专业教学指导委员会',
    registerEndTime: '2024-03-15',
    activityStartTime: '2024-04-01',
    activityEndTime: '2024-04-03',
    location: '电子科技大学',
    reward: '获得ACM金银铜牌证书，增加保研积分',
    recommendScore: 92,
    recommendReason: '根据您的学术研究型画像，这类高难度竞赛能充分发挥您的优势'
  },
  {
    id: 2,
    activityName: '挑战杯全国大学生课外学术科技作品竞赛',
    category: '比赛获奖',
    subCategory: '学术研究',
    level: '国家级',
    levelValue: 4,
    organizer: '共青团中央',
    registerEndTime: '2024-02-28',
    activityStartTime: '2024-05-01',
    activityEndTime: '2024-05-15',
    location: '待定',
    reward: '国赛获奖可获得保研资格和创新创业学分',
    recommendScore: 88,
    recommendReason: '与您的学术背景高度契合，有助于提升科研能力'
  },
  {
    id: 3,
    activityName: '大学生创新创业训练计划项目',
    category: '培训经历',
    subCategory: '创新创业',
    level: '省部级',
    levelValue: 3,
    organizer: '教育部高等教育司',
    registerEndTime: '2024-03-30',
    activityStartTime: '2024-04-01',
    activityEndTime: '2024-12-31',
    location: '校内',
    reward: '国家级立项可获得创新学分和科研经费支持',
    recommendScore: 75,
    recommendReason: '建议在课余时间积极参与，提升综合能力'
  },
  {
    id: 4,
    activityName: '暑期社会实践活动',
    category: '学生活动',
    subCategory: '劳动实践',
    level: '校级',
    levelValue: 2,
    organizer: '校团委',
    registerEndTime: '2024-06-20',
    activityStartTime: '2024-07-01',
    activityEndTime: '2024-08-31',
    location: '各地实践基地',
    reward: '获得社会实践学分和志愿者时长',
    recommendScore: 68,
    recommendReason: '符合您全面发展的需求，建议积极参与'
  },
  {
    id: 5,
    activityName: '职业生涯规划培训',
    category: '培训经历',
    subCategory: '技能培训',
    level: '校级',
    levelValue: 2,
    organizer: '就业指导中心',
    registerEndTime: '2024-03-10',
    activityStartTime: '2024-03-15',
    activityEndTime: '2024-03-16',
    location: '图书馆报告厅',
    reward: '帮助学生明确职业发展方向',
    recommendScore: 65,
    recommendReason: '建议提前规划职业发展路径'
  }
]

const getLevelType = (level) => {
  const typeMap = {
    '院级': 'info',
    '校级': 'success',
    '省部级': 'warning',
    '国家级': 'danger',
    '国际级': 'danger'
  }
  return typeMap[level] || 'info'
}

const formatDate = (date) => {
  if (!date) return '待定'
  const d = new Date(date)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

const formatDateRange = (start, end) => {
  if (!start) return '待定'
  const s = formatDate(start)
  const e = end ? formatDate(end) : ''
  return e ? `${s} - ${e}` : s
}

const getRemainingDays = (endDate) => {
  if (!endDate) return 999
  const now = new Date()
  const end = new Date(endDate)
  const diff = Math.ceil((end - now) / (1000 * 60 * 60 * 24))
  return diff
}

const loadRecommendations = () => {
  let filtered = [...mockData]
  
  if (filterForm.category) {
    filtered = filtered.filter(item => item.category === filterForm.category)
  }
  if (filterForm.level) {
    filtered = filtered.filter(item => item.levelValue === filterForm.level)
  }
  
  recommendations.value = filtered
}

const generateRecommendations = () => {
  recommendations.value = [...mockData].sort((a, b) => b.recommendScore - a.recommendScore)
  ElMessage.success('推荐已刷新')
}

const updateStatus = (id, status) => {
  const statusText = { interested: '感兴趣', applied: '已报名' }
  ElMessage.success(`已标记为${statusText[status]}`)
}

const showDetail = (item) => {
  currentActivity.value = item
  detailVisible.value = true
}

const handleApply = () => {
  if (currentActivity.value) {
    updateStatus(currentActivity.value.id, 'applied')
    detailVisible.value = false
  }
}

const resetFilter = () => {
  filterForm.category = ''
  filterForm.level = ''
  filterForm.status = ''
  recommendations.value = [...mockData]
}

onMounted(() => {
  recommendations.value = [...mockData]
})
</script>

<style scoped lang="scss">
.activity-recommend-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
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

.recommend-card {
  height: 100%;
  
  .recommend-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 16px;
    
    .activity-info {
      flex: 1;
      
      h3 {
        margin: 0 0 8px 0;
        font-size: 18px;
        color: #303133;
      }
      
      .tags {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }
    }
    
    .recommend-score {
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .score-value {
        font-size: 16px;
        font-weight: bold;
        color: #409eff;
      }
      
      .score-label {
        font-size: 12px;
        color: #909399;
      }
    }
  }
  
  .recommend-body {
    margin-bottom: 16px;
    
    .info-row {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
      font-size: 14px;
      
      .label {
        color: #909399;
        min-width: 70px;
      }
    }
    
    .reason {
      margin-top: 12px;
      padding: 12px;
      background: #f0f9eb;
      border-radius: 4px;
      
      .label {
        color: #67c23a;
        font-weight: bold;
      }
      
      p {
        margin: 8px 0 0 0;
        color: #606266;
      }
    }
  }
  
  .recommend-footer {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    padding-top: 12px;
    border-top: 1px solid #eee;
  }
}

.activity-detail {
  .detail-section {
    margin-bottom: 20px;
    
    h4 {
      margin: 0 0 12px 0;
      padding-bottom: 8px;
      border-bottom: 2px solid #409eff;
      color: #303133;
    }
    
    p {
      margin: 0;
      color: #606266;
      line-height: 1.6;
    }
  }
}
</style>
