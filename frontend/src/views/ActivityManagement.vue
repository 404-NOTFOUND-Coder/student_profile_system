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

// 模拟数据
const mockActivities = [
  {
    id: 1,
    name: '全国大学生程序设计竞赛',
    category: '比赛获奖',
    subCategory: '科技创新',
    level: 4,
    levelName: '国家级',
    organizer: '教育部高等学校计算机类专业教学指导委员会',
    registerStartTime: '2024-03-01',
    registerEndTime: '2024-03-15',
    activityStartTime: '2024-04-01',
    activityEndTime: '2024-04-03',
    location: '电子科技大学',
    reward: '获得ACM金银铜牌证书，增加保研积分',
    requirements: '具有编程基础，建议ACM队成员参加',
    status: 'published',
    enableRecommend: 1,
    recommendWeight: 90,
    targetAudience: '全校本科生'
  },
  {
    id: 2,
    name: '挑战杯全国大学生课外学术科技作品竞赛',
    category: '比赛获奖',
    subCategory: '学术研究',
    level: 4,
    levelName: '国家级',
    organizer: '共青团中央',
    registerStartTime: '2024-02-01',
    registerEndTime: '2024-02-28',
    activityStartTime: '2024-05-01',
    activityEndTime: '2024-05-15',
    location: '待定',
    reward: '国赛获奖可获得保研资格和创新创业学分',
    requirements: '需有创新项目或科研作品',
    status: 'published',
    enableRecommend: 1,
    recommendWeight: 95,
    targetAudience: '全日制本科生'
  },
  {
    id: 3,
    name: '大学生创新创业训练计划项目',
    category: '培训经历',
    subCategory: '创新创业',
    level: 3,
    levelName: '省部级',
    organizer: '教育部高等教育司',
    registerStartTime: '2024-01-01',
    registerEndTime: '2024-03-30',
    activityStartTime: '2024-04-01',
    activityEndTime: '2024-12-31',
    location: '校内',
    reward: '国家级立项可获得创新学分和科研经费支持',
    requirements: '有创新创业想法的学生团队',
    status: 'published',
    enableRecommend: 1,
    recommendWeight: 85,
    targetAudience: '全日制学生'
  },
  {
    id: 4,
    name: '暑期社会实践活动',
    category: '学生活动',
    subCategory: '劳动实践',
    level: 2,
    levelName: '校级',
    organizer: '校团委',
    registerStartTime: '2024-05-01',
    registerEndTime: '2024-06-20',
    activityStartTime: '2024-07-01',
    activityEndTime: '2024-08-31',
    location: '各地实践基地',
    reward: '获得社会实践学分和志愿者时长',
    requirements: '身体健康，需服从安排',
    status: 'published',
    enableRecommend: 1,
    recommendWeight: 70,
    targetAudience: '全日制本科生'
  },
  {
    id: 5,
    name: '全国大学生英语竞赛',
    category: '比赛获奖',
    subCategory: '学术研究',
    level: 4,
    levelName: '国家级',
    organizer: '高等学校大学外语教学研究会',
    registerStartTime: '2024-02-15',
    registerEndTime: '2024-03-20',
    activityStartTime: '2024-04-21',
    activityEndTime: '2024-04-21',
    location: '各省级考点',
    reward: '获奖可获得学分和奖学金评定资格',
    requirements: '英语水平良好',
    status: 'published',
    enableRecommend: 1,
    recommendWeight: 80,
    targetAudience: '本科生'
  },
  {
    id: 6,
    name: '职业生涯规划培训',
    category: '培训经历',
    subCategory: '技能培训',
    level: 2,
    levelName: '校级',
    organizer: '就业指导中心',
    registerStartTime: '2024-03-01',
    registerEndTime: '2024-03-10',
    activityStartTime: '2024-03-15',
    activityEndTime: '2024-03-16',
    location: '图书馆报告厅',
    reward: '帮助学生明确职业发展方向',
    requirements: '无特殊要求',
    status: 'published',
    enableRecommend: 1,
    recommendWeight: 60,
    targetAudience: '大一、大二学生'
  }
]

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

const loadActivities = () => {
  loading.value = true
  
  setTimeout(() => {
    let filtered = [...mockActivities]
    
    if (filterForm.category) {
      filtered = filtered.filter(item => item.category === filterForm.category)
    }
    if (filterForm.level) {
      filtered = filtered.filter(item => item.level === filterForm.level)
    }
    if (filterForm.status) {
      filtered = filtered.filter(item => item.status === filterForm.status)
    }
    
    activities.value = filtered
    pagination.total = filtered.length
    loading.value = false
  }, 300)
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
    ElMessage.success('删除成功')
    loadActivities()
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
      ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadActivities()
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
