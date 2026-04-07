<template>
  <div class="activity-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>活动管理</span>
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
        <el-table-column prop="award" label="奖项" width="100">
          <template #default="{ row }">
            <span v-if="row.award" class="award-text">{{ row.award }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDetail(row)">详情</el-button>
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
import { allActivities, allStudents } from '@/data/mockData'

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const currentActivity = ref(null)

const queryForm = reactive({
  type: '',
  level: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

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
</style>