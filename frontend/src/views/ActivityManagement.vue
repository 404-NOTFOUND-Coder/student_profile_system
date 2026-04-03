<template>
  <div class="activity-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>活动管理</span>
          <el-button type="primary" @click="openAddDialog">添加活动</el-button>
        </div>
      </template>

      <!-- 筛选条件 -->
      <div class="filter-section">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="活动分类">
            <el-select v-model="filterForm.category" placeholder="选择分类" clearable class="select-wide">
              <el-option v-for="cat in categories" :key="cat.value" :label="cat.label" :value="cat.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="活动级别">
            <el-select v-model="filterForm.level" placeholder="选择级别" clearable class="select-wide">
              <el-option v-for="level in levelOptions" :key="level.value" :label="level.label" :value="level.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="活动状态">
            <el-select v-model="filterForm.status" placeholder="选择状态" clearable class="select-wide">
              <el-option label="草稿" value="draft" />
              <el-option label="已发布" value="published" />
              <el-option label="进行中" value="ongoing" />
              <el-option label="已结束" value="ended" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadActivities">查询</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 活动列表 -->
      <el-table :data="activities" stripe border v-loading="loading">
        <el-table-column prop="name" label="活动名称" min-width="180" />
        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            <el-tag type="primary">{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="subCategory" label="二级分类" width="120" />
        <el-table-column prop="levelName" label="级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)">{{ row.levelName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="organizer" label="主办方" width="150" />
        <el-table-column prop="registerEndTime" label="报名截止" width="120">
          <template #default="{ row }">
            {{ formatDate(row.registerEndTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="editActivity(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteActivity(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadActivities"
          @current-change="loadActivities"
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑活动' : '添加活动'" width="700px" @close="resetForm">
      <el-form :model="activityForm" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="activityForm.name" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动分类" prop="category">
          <el-select v-model="activityForm.category" placeholder="选择分类" class="full-width">
            <el-option v-for="cat in categories" :key="cat.value" :label="cat.label" :value="cat.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="二级分类" prop="subCategory">
          <el-select v-model="activityForm.subCategory" placeholder="选择二级分类" class="full-width" clearable>
            <el-option v-for="sub in currentSubCategories" :key="sub" :label="sub" :value="sub" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动级别" prop="level">
          <el-select v-model="activityForm.level" placeholder="选择级别" class="full-width">
            <el-option v-for="level in levelOptions" :key="level.value" :label="level.label" :value="level.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="主办方" prop="organizer">
          <el-input v-model="activityForm.organizer" placeholder="请输入主办方" />
        </el-form-item>
        <el-form-item label="面向对象" prop="targetAudience">
          <el-input v-model="activityForm.targetAudience" placeholder="请输入面向对象" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报名开始" prop="registerStartTime">
              <el-date-picker v-model="activityForm.registerStartTime" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" class="full-width" placeholder="选择日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报名截止" prop="registerEndTime">
              <el-date-picker v-model="activityForm.registerEndTime" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" class="full-width" placeholder="选择日期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动开始" prop="activityStartTime">
              <el-date-picker v-model="activityForm.activityStartTime" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" class="full-width" placeholder="选择日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动结束" prop="activityEndTime">
              <el-date-picker v-model="activityForm.activityEndTime" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" class="full-width" placeholder="选择日期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="activityForm.location" placeholder="请输入活动地点" />
        </el-form-item>
        <el-form-item label="奖励/收获" prop="reward">
          <el-input v-model="activityForm.reward" type="textarea" :rows="3" placeholder="请输入奖励或收获" />
        </el-form-item>
        <el-form-item label="参赛要求" prop="requirements">
          <el-input v-model="activityForm.requirements" type="textarea" :rows="2" placeholder="请输入参赛要求" />
        </el-form-item>
        <el-form-item label="活动状态" prop="status">
          <el-select v-model="activityForm.status" placeholder="选择状态" class="full-width">
            <el-option label="草稿" value="draft" />
            <el-option label="已发布" value="published" />
            <el-option label="进行中" value="ongoing" />
            <el-option label="已结束" value="ended" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="开启推荐">
          <el-switch v-model="activityForm.enableRecommend" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="推荐权重" v-if="activityForm.enableRecommend">
          <el-slider v-model="activityForm.recommendWeight" :min="0" :max="100" :step="5" show-stops />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api/index'

const loading = ref(false)
const activities = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const filterForm = reactive({
  category: '',
  level: '',
  status: ''
})

const activityForm = reactive({
  id: null,
  name: '',
  category: '',
  subCategory: '',
  level: null,
  organizer: '',
  targetAudience: '',
  registerStartTime: '',
  registerEndTime: '',
  activityStartTime: '',
  activityEndTime: '',
  location: '',
  description: '',
  reward: '',
  requirements: '',
  status: 'draft',
  enableRecommend: 0,
  recommendWeight: 50
})

const rules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
  level: [{ required: true, message: '请选择活动级别', trigger: 'change' }],
  organizer: [{ required: true, message: '请输入主办方', trigger: 'blur' }],
  status: [{ required: true, message: '请选择活动状态', trigger: 'change' }]
}

const categories = [
  { value: '比赛获奖', label: '比赛获奖' },
  { value: '培训经历', label: '培训经历' },
  { value: '工作经历', label: '工作经历' },
  { value: '学生活动', label: '学生活动' },
  { value: '个人荣誉', label: '个人荣誉' },
  { value: '资格认证', label: '资格认证' }
]

const subCategoriesMap = {
  '比赛获奖': ['创新创业', '科技创新', '劳动实践', '思想成长', '体育活动', '文化艺术', '心理成长', '学术研究'],
  '培训经历': ['技能培训', '语言培训', '专业培训'],
  '工作经历': ['实习', '兼职', '勤工俭学'],
  '学生活动': ['社团活动', '班级活动', '志愿服务'],
  '个人荣誉': ['三好学生', '优秀干部', '奖学金'],
  '资格认证': ['专业证书', '技能证书', '语言证书']
}

const levelOptions = [
  { value: 1, label: '院级' },
  { value: 2, label: '校级' },
  { value: 3, label: '省部级' },
  { value: 4, label: '国家级' },
  { value: 5, label: '国际级' }
]

const currentSubCategories = computed(() => {
  return subCategoriesMap[activityForm.category] || []
})

const getLevelType = (level) => {
  const typeMap = { 1: 'info', 2: 'success', 3: 'warning', 4: 'danger', 5: 'danger' }
  return typeMap[level] || 'info'
}

const getStatusType = (status) => {
  const typeMap = { draft: 'info', published: 'success', ongoing: 'primary', ended: 'warning', cancelled: 'danger' }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = { draft: '草稿', published: '已发布', ongoing: '进行中', ended: '已结束', cancelled: '已取消' }
  return textMap[status] || status
}

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
}

const loadActivities = async () => {
  loading.value = true
  try {
    const res = await request.get('/activity/page', {
      params: {
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize,
        ...filterForm
      }
    })
    if (res.code === 200 && res.data) {
      activities.value = res.data.records || res.data.list || []
      pagination.total = res.data.total || 0
    }
  } catch (error) {
    console.error('加载活动列表失败:', error)
  } finally {
    loading.value = false
  }
}

const openAddDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const editActivity = (row) => {
  isEdit.value = true
  Object.assign(activityForm, row)
  dialogVisible.value = true
}

const deleteActivity = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该活动吗?', '提示', {
      type: 'warning'
    })
    const res = await request.delete(`/activity/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadActivities()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const url = isEdit.value ? `/activity/${activityForm.id}` : '/activity'
        const method = isEdit.value ? 'put' : 'post'
        const res = await request[method](url, activityForm)
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
          dialogVisible.value = false
          loadActivities()
        }
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '添加失败')
      }
    }
  })
}

const resetForm = () => {
  Object.assign(activityForm, {
    id: null,
    name: '',
    category: '',
    subCategory: '',
    level: null,
    organizer: '',
    targetAudience: '',
    registerStartTime: '',
    registerEndTime: '',
    activityStartTime: '',
    activityEndTime: '',
    location: '',
    description: '',
    reward: '',
    requirements: '',
    status: 'draft',
    enableRecommend: 0,
    recommendWeight: 50
  })
  formRef.value?.clearValidate()
}

const resetFilter = () => {
  filterForm.category = ''
  filterForm.level = ''
  filterForm.status = ''
  loadActivities()
}

onMounted(() => {
  loadActivities()
})
</script>

<style scoped lang="scss">
.activity-management-container {
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

.full-width {
  width: 100%;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
