<template>
  <div class="student-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学生档案管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增学生
          </el-button>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryForm" class="search-form">
        <el-form-item label="学号">
          <el-input v-model="queryForm.studentNo" placeholder="请输入学号" clearable />
        </el-form-item>
        <el-form-item label="学院">
          <el-select v-model="queryForm.college" placeholder="请选择学院" clearable class="select-wide">
            <el-option v-for="college in colleges" :key="college" :label="college" :value="college" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="queryForm.major" placeholder="请输入专业" clearable />
        </el-form-item>
        <el-form-item label="入学年份">
          <el-select v-model="queryForm.enrollmentYear" placeholder="请选择年份" clearable class="select-wide">
            <el-option v-for="year in years" :key="year" :label="year" :value="year" />
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

      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe border>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="studentNo" label="学号" width="180" />
        <el-table-column prop="gender" label="性别" width="60">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院" width="320" />
        <el-table-column prop="major" label="专业" width="350" />
        <el-table-column prop="className" label="班级" width="120" />
        <el-table-column prop="enrollmentYear" label="入学年份" width="150" />
        <el-table-column prop="studentStatus" label="学籍状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.studentStatus)" size="small">
              {{ getStatusName(row.studentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号" prop="studentNo">
              <el-input v-model="formData.studentNo" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="formData.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker v-model="formData.birthday" type="date" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学院" prop="college">
              <el-input v-model="formData.college" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="formData.major" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="班级" prop="className">
              <el-input v-model="formData.className" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入学年份" prop="enrollmentYear">
              <el-input v-model="formData.enrollmentYear" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { allStudents, colleges } from '@/data/mockData'

const router = useRouter()

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const queryForm = reactive({
  studentNo: '',
  college: '',
  major: '',
  enrollmentYear: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})

const formData = reactive({
  id: null,
  studentNo: '',
  gender: 1,
  birthday: '',
  phone: '',
  email: '',
  college: '',
  major: '',
  className: '',
  enrollmentYear: new Date().getFullYear()
})

const rules = {
  studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  college: [{ required: true, message: '请输入学院', trigger: 'blur' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }]
}

const years = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 10 }, (_, i) => currentYear - i)
})

const dialogTitle = computed(() => isEdit.value ? '编辑学生' : '新增学生')

// 获取列表数据
const fetchList = () => {
  loading.value = true

  // 使用统一数据源
  let filtered = [...allStudents]

  // 模拟过滤
  if (queryForm.studentNo) {
    filtered = filtered.filter(s => s.studentNo.includes(queryForm.studentNo))
  }
  if (queryForm.college) {
    filtered = filtered.filter(s => s.college === queryForm.college)
  }
  if (queryForm.major) {
    filtered = filtered.filter(s => s.major.includes(queryForm.major))
  }
  if (queryForm.enrollmentYear) {
    filtered = filtered.filter(s => s.enrollmentYear === queryForm.enrollmentYear)
  }

  // 模拟分页
  pagination.total = filtered.length
  const start = (pagination.pageNum - 1) * pagination.pageSize
  const end = start + pagination.pageSize
  tableData.value = filtered.slice(start, end)

  loading.value = false
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchList()
}

const handleReset = () => {
  Object.keys(queryForm).forEach(key => {
    queryForm[key] = key === 'enrollmentYear' ? null : ''
  })
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'gender' ? 1 : key === 'enrollmentYear' ? new Date().getFullYear() : ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        ElMessage.success('更新成功（模拟）')
      } else {
        ElMessage.success('创建成功（模拟）')
      }
      dialogVisible.value = false
      fetchList()
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该学生档案吗？', '提示', {
    type: 'warning'
  }).then(() => {
  ElMessage.success('删除成功（模拟）')
  fetchList()
  }).catch(() => {})
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  fetchList()
}

const handlePageChange = (val) => {
  pagination.pageNum = val
  fetchList()
}

const getStatusType = (status) => {
  const types = { 1: 'success', 2: 'warning', 3: 'danger', 4: 'info' }
  return types[status] || 'info'
}

const getStatusName = (status) => {
  const names = { 1: '在读', 2: '休学', 3: '退学', 4: '毕业' }
  return names[status] || '未知'
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped lang="scss">
.student-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;

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