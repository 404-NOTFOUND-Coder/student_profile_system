<template>
  <div class="warning-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学业预警管理</span>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="预警类型">
          <el-select v-model="queryForm.warningType" placeholder="请选择预警类型" clearable class="select-wide">
            <el-option label="成绩预警" value="1" />
            <el-option label="学分预警" value="2" />
            <el-option label="学业困难" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警等级">
          <el-select v-model="queryForm.level" placeholder="请选择预警等级" clearable class="select-wide">
            <el-option label="一般" :value="1" />
            <el-option label="较重" :value="2" />
            <el-option label="严重" :value="3" />
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

      <el-table :data="tableData" stripe border v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="studentNo" label="学号" width="140" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="college" label="学院" width="120" />
        <el-table-column prop="major" label="专业" width="150" />
        <el-table-column prop="warningType" label="预警类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getWarningTypeTag(row.warningType)">
              {{ row.warningType || '成绩预警' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="等级" width="80">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)" effect="dark">
              {{ row.levelName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningMessage" label="预警信息" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDetail(row)">
              查看
            </el-button>
            <el-button type="success" link @click="handleWarning(row)">
              处理
            </el-button>
            <el-button type="info" link @click="trackStudent(row)">
              追踪
            </el-button>
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

    <!-- 预警详情对话框 -->
    <el-dialog v-model="detailVisible" title="预警详情" width="600px">
      <el-descriptions :column="2" border v-if="currentWarning">
        <el-descriptions-item label="学号">{{ currentWarning.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentWarning.studentName }}</el-descriptions-item>
        <el-descriptions-item label="预警类型">
          <el-tag :type="getWarningTypeTag(currentWarning.warningType)">
            {{ currentWarning.warningType || '成绩预警' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警等级">
          <el-tag :type="getLevelType(currentWarning.level)" effect="dark">
            {{ currentWarning.levelName }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警信息" :span="2">
          {{ currentWarning.warningMessage }}
        </el-descriptions-item>
        <el-descriptions-item label="触发条件" :span="2">
          {{ currentWarning.warningMessage || '学业评分偏低' }}
        </el-descriptions-item>
        <el-descriptions-item label="建议措施" :span="2">
          {{ getSuggestAction(currentWarning.level) }}
        </el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag type="warning">待处理</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(currentWarning.createTime) }}
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleWarning(currentWarning)">
          处理预警
        </el-button>
      </template>
    </el-dialog>

    <!-- 处理对话框 -->
    <el-dialog v-model="handleVisible" title="处理预警" width="500px">
      <el-form :model="handleForm" label-width="100px">
        <el-form-item label="处理方式">
          <el-radio-group v-model="handleForm.action">
            <el-radio label="counsel">约谈辅导</el-radio>
            <el-radio label="tutor">联系导师</el-radio>
            <el-radio label="parent">联系家长</el-radio>
            <el-radio label="other">其他措施</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理说明">
          <el-input v-model="handleForm.remark" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { statsApi } from '@/api/student'

const router = useRouter()

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const handleVisible = ref(false)
const currentWarning = ref(null)

const queryForm = reactive({
  warningType: '',
  level: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const handleForm = reactive({
  action: 'counsel',
  remark: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await statsApi.getWarningList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    if (res.code === 200 && res.data) {
      tableData.value = res.data.records || res.data || []
      pagination.total = res.data.total || tableData.value.length
    }
  } catch (error) {
    console.error('获取预警列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  queryForm.warningType = ''
  queryForm.level = ''
  pagination.pageNum = 1
  loadData()
}

const viewDetail = (row) => {
  currentWarning.value = row
  detailVisible.value = true
}

const handleWarning = (row) => {
  currentWarning.value = row
  handleVisible.value = true
}

const submitHandle = () => {
  ElMessage.success('处理提交成功')
  handleVisible.value = false
  loadData()
}

const trackStudent = (row) => {
  router.push(`/student/profile/${row.studentNo}`)
}

const getWarningType = (type) => {
  const types = { 1: 'danger', 2: 'warning', 3: 'info' }
  return types[type] || 'info'
}

const getWarningTypeTag = (type) => {
  // type 是字符串如 "成绩预警"
  if (type === '成绩预警') return 'danger'
  if (type === '学分预警') return 'warning'
  if (type === '学业困难') return 'info'
  return 'info'
}

const getLevelType = (level) => {
  const types = { 1: 'info', 2: 'warning', 3: 'danger' }
  return types[level] || 'info'
}

const getSuggestAction = (level) => {
  const actions = {
    1: '保持当前学习状态，注意查漏补缺',
    2: '建议加强学业辅导，增加自习时间',
    3: '需要重点关注，建议约谈家长和导师'
  }
  return actions[level] || '建议加强学业辅导'
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.warning-container {
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
