<template>
  <div class="activity-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学业活动管理</span>
          <el-button type="primary" @click="openAddDialog">
            <el-icon><Plus /></el-icon>
            新增活动
          </el-button>
        </div>
      </template>

      <!-- 筛选表单 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="活动类型">
          <el-select v-model="queryForm.type" placeholder="请选择类型" clearable class="select-wide">
            <el-option label="学科竞赛" value="学科竞赛" />
            <el-option label="学术科研" value="学术科研" />
            <el-option label="志愿服务" value="志愿服务" />
            <el-option label="社会实践" value="社会实践" />
            <el-option label="创新创业" value="创新创业" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动级别">
          <el-select v-model="queryForm.level" placeholder="请选择级别" clearable class="select-wide">
            <el-option label="院级" value="院级" />
            <el-option label="校级" value="校级" />
            <el-option label="省部级" value="省部级" />
            <el-option label="国家级" value="国家级" />
            <el-option label="国际级" value="国际级" />
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

      <!-- 活动列表 -->
      <el-table :data="tableData" stripe border v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="活动名称" min-width="200" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)" size="small">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelTag(row.level)" size="small">{{ row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="时间" width="120" />
        <el-table-column prop="participants" label="参与学号" min-width="180">
          <template #default="{ row }">
            <span v-for="(no, idx) in row.participants.slice(0, 3)" :key="no">
              {{ no }}{{ idx < Math.min(row.participants.length, 3) - 1 ? '、' : '' }}
            </span>
            <span v-if="row.participants.length > 3">等{{ row.participants.length }}人</span>
          </template>
        </el-table-column>
        <el-table-column prop="award" label="奖项" width="150">
          <template #default="{ row }">
            <span v-if="row.award" class="award-text">{{ row.award }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDetail(row)">详情</el-button>
            <el-button type="primary" link @click="editActivity(row)">编辑</el-button>
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

    <!-- 新增/编辑活动对话框 -->
    <el-dialog v-model="addDialogVisible" :title="isEdit ? '编辑活动' : '新增活动'" width="600px">
      <el-form :model="activityForm" label-width="100px" ref="activityFormRef">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="activityForm.name" placeholder="请输入活动名称" @blur="checkSimilarActivities" />
          <div v-if="similarActivities.length > 0" class="similar-hint">
            <el-alert type="info" :closable="false" show-icon>
              <template #title>
                发现相似活动，是否选择合并？
              </template>
              <div class="similar-list">
                <div v-for="activity in similarActivities" :key="activity.id" class="similar-item" @click="selectSimilarActivity(activity)">
                  <span>{{ activity.name }}</span>
                  <el-tag size="small" :type="getTypeTag(activity.type)">{{ activity.type }}</el-tag>
                </div>
              </div>
            </el-alert>
          </div>
        </el-form-item>
        <el-form-item label="活动类型" prop="type">
          <el-select v-model="activityForm.type" placeholder="请选择类型" class="full-width">
            <el-option label="学科竞赛" value="学科竞赛" />
            <el-option label="学术科研" value="学术科研" />
            <el-option label="志愿服务" value="志愿服务" />
            <el-option label="社会实践" value="社会实践" />
            <el-option label="创新创业" value="创新创业" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动级别" prop="level">
          <el-select v-model="activityForm.level" placeholder="请选择级别" class="full-width">
            <el-option label="院级" value="院级" />
            <el-option label="校级" value="校级" />
            <el-option label="省部级" value="省部级" />
            <el-option label="国家级" value="国家级" />
            <el-option label="国际级" value="国际级" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动时间" prop="time">
          <el-date-picker v-model="activityForm.time" type="month" format="YYYY-MM" value-format="YYYY-MM" placeholder="选择时间" class="full-width" />
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input v-model="activityForm.description" type="textarea" :rows="3" placeholder="请输入活动描述" />
        </el-form-item>
        <el-form-item label="参与学号" prop="participants">
          <el-input v-model="participantsInput" placeholder="请输入参与学号，多个用逗号分隔" />
          <div class="participants-hint">输入格式：2020010906005, 2020010906012</div>
        </el-form-item>
        <el-form-item label="获奖情况">
          <el-input v-model="activityForm.award" placeholder="请输入获奖情况，无则留空" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitActivity">确定</el-button>
      </template>
    </el-dialog>

    <!-- 活动详情对话框 -->
    <el-dialog v-model="detailVisible" :title="currentActivity?.name" width="600px">
      <el-descriptions :column="2" border v-if="currentActivity">
        <el-descriptions-item label="活动名称" :span="2">{{ currentActivity.name }}</el-descriptions-item>
        <el-descriptions-item label="活动类型">
          <el-tag :type="getTypeTag(currentActivity.type)" size="small">{{ currentActivity.type }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="活动级别">
          <el-tag :type="getLevelTag(currentActivity.level)" size="small">{{ currentActivity.level }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="活动时间">{{ currentActivity.time }}</el-descriptions-item>
        <el-descriptions-item label="获奖情况">
          <span v-if="currentActivity.award" class="award-text">{{ currentActivity.award }}</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="活动描述" :span="2">{{ currentActivity.description }}</el-descriptions-item>
        <el-descriptions-item label="参与学号" :span="2">
          <div class="participant-list">
            <span v-for="no in currentActivity.participants" :key="no" class="participant-tag">
              {{ no }}
            </span>
          </div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { allActivities } from '@/data/mockData'

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const currentActivity = ref(null)
const addDialogVisible = ref(false)
const isEdit = ref(false)
const similarActivities = ref([])
const activityFormRef = ref(null)

const participantsInput = ref('')

const activityForm = reactive({
  id: null,
  name: '',
  type: '',
  level: '',
  time: '',
  description: '',
  award: ''
})

const queryForm = reactive({
  type: '',
  level: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 计算字符串相似度
const calculateSimilarity = (str1, str2) => {
  const s1 = str1.toLowerCase()
  const s2 = str2.toLowerCase()
  if (s1 === s2) return 1
  if (s1.length === 0 || s2.length === 0) return 0

  const longer = s1.length > s2.length ? s1 : s2
  const shorter = s1.length > s2.length ? s2 : s1

  const longerLength = longer.length
  return (longerLength - editDistance(longer, shorter)) / parseFloat(longerLength)
}

const editDistance = (s1, s2) => {
  const costs = []
  for (let i = 0; i <= s1.length; i++) {
    let lastValue = i
    for (let j = 0; j <= s2.length; j++) {
      if (i === 0) {
        costs[j] = j
      } else if (j > 0) {
        let newValue = costs[j - 1]
        if (s1.charAt(i - 1) !== s2.charAt(j - 1)) {
          newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1
        }
        costs[j - 1] = lastValue
        lastValue = newValue
      }
    }
    if (i > 0) costs[s2.length] = lastValue
  }
  return costs[s2.length]
}

// 检查相似活动
const checkSimilarActivities = () => {
  if (!activityForm.name || activityForm.name.length < 2) {
    similarActivities.value = []
    return
  }

  const threshold = 0.6
  similarActivities.value = allActivities.filter(a => {
    const similarity = calculateSimilarity(activityForm.name, a.name)
    return similarity >= threshold && similarity < 1
  }).slice(0, 5)
}

// 选择相似活动
const selectSimilarActivity = (activity) => {
  activityForm.name = activity.name
  activityForm.type = activity.type
  activityForm.level = activity.level
  similarActivities.value = []
  ElMessage.success(`已选择活动"${activity.name}"，将添加到该活动的参与学号中`)
}

// 打开新增对话框
const openAddDialog = () => {
  isEdit.value = false
  resetForm()
  addDialogVisible.value = true
}

// 编辑活动
const editActivity = (row) => {
  isEdit.value = true
  activityForm.id = row.id
  activityForm.name = row.name
  activityForm.type = row.type
  activityForm.level = row.level
  activityForm.time = row.time
  activityForm.description = row.description
  activityForm.award = row.award || ''
  participantsInput.value = row.participants.join(', ')
  addDialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  activityForm.id = null
  activityForm.name = ''
  activityForm.type = ''
  activityForm.level = ''
  activityForm.time = ''
  activityForm.description = ''
  activityForm.award = ''
  participantsInput.value = ''
  similarActivities.value = []
}

// 提交活动
const submitActivity = () => {
  if (!activityForm.name || !activityForm.type || !activityForm.level || !activityForm.time) {
    ElMessage.warning('请填写完整信息')
    return
  }

  const participants = participantsInput.value
    .split(/[,，]/)
    .map(p => p.trim())
    .filter(p => p.length > 0)

  if (isEdit.value) {
    ElMessage.success('活动编辑成功')
  } else {
    ElMessage.success('活动添加成功')
  }

  addDialogVisible.value = false
  resetForm()
  loadData()
}

// 加载活动数据（从统一数据源）
const loadData = () => {
  loading.value = true

  let filtered = [...allActivities]

  if (queryForm.type) {
    filtered = filtered.filter(a => a.type === queryForm.type)
  }
  if (queryForm.level) {
    filtered = filtered.filter(a => a.level === queryForm.level)
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
  queryForm.type = ''
  queryForm.level = ''
  pagination.pageNum = 1
  loadData()
}

const viewDetail = (row) => {
  currentActivity.value = row
  detailVisible.value = true
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

const getLevelTag = (level) => {
  const types = {
    '院级': 'info',
    '校级': 'primary',
    '省部级': 'warning',
    '国家级': 'success',
    '国际级': 'danger'
  }
  return types[level] || 'info'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.activity-management {
  padding: 20px;
}

.card-header {
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

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.award-text {
  color: #f56c6c;
  font-weight: bold;
}

.participant-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  .participant-tag {
    background: #ecf5ff;
    color: #409eff;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
  }
}

.full-width {
  width: 100%;
}

.similar-hint {
  margin-top: 12px;
}

.similar-list {
  margin-top: 8px;
}

.similar-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  cursor: pointer;
  border-radius: 4px;

  &:hover {
    background: #f5f7fa;
  }
}

.participants-hint {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>