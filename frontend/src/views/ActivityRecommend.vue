<template>
  <div class="activity-recommend">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>活动推荐</span>
        </div>
      </template>

      <!-- 筛选表单 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="学生姓名">
          <el-input v-model="queryForm.studentName" placeholder="请输入学生姓名" clearable />
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

      <!-- 推荐列表 -->
      <el-table :data="tableData" stripe border v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="studentName" label="学生姓名" width="100" />
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
import { ref, reactive, onMounted } from 'vue'
import { activityRecommendations } from '@/data/mockData'

const loading = ref(false)
const tableData = ref([])

const queryForm = reactive({
  studentName: '',
  type: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 加载推荐数据（从统一数据源）
const loadData = () => {
  loading.value = true

  let filtered = [...activityRecommendations]

  if (queryForm.studentName) {
    filtered = filtered.filter(r => r.studentName.includes(queryForm.studentName))
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
  queryForm.studentName = ''
  queryForm.type = ''
  pagination.pageNum = 1
  loadData()
}

const getScoreColor = (score) => {
  if (score >= 90) return '#67c23a'
  if (score >= 75) return '#409eff'
  if (score >= 60) return '#e6a23c'
  return '#909399'
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
</style>