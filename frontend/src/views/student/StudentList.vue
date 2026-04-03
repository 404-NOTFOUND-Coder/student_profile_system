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
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="学院">
          <el-select v-model="queryForm.college" placeholder="请选择学院" clearable class="select-wide">
            <el-option label="计算机学院" value="计算机学院" />
            <el-option label="软件学院" value="软件学院" />
            <el-option label="信息学院" value="信息学院" />
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
        <el-table-column prop="studentNo" label="学号" width="140" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="60">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院" width="120" />
        <el-table-column prop="major" label="专业" width="150" />
        <el-table-column prop="className" label="班级" width="120" />
        <el-table-column prop="enrollmentYear" label="入学年份" width="100" />
        <el-table-column prop="studentStatus" label="学籍状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.studentStatus)" size="small">
              {{ getStatusName(row.studentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gpa" label="GPA" width="80" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewProfile(row)">
              画像
            </el-button>
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
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
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

// 生成100个模拟学生数据
const generateMockStudents = () => {
  const colleges = ['计算机学院', '软件学院', '信息学院', '电子学院', '机械学院']
  const majors = {
    '计算机学院': ['计算机科学与技术', '软件工程', '信息安全', '数据科学'],
    '软件学院': ['软件工程', '数字媒体技术', '物联网工程'],
    '信息学院': ['通信工程', '电子信息工程', '信息工程'],
    '电子学院': ['电子科学与技术', '微电子科学与工程'],
    '机械学院': ['机械设计制造', '机械电子工程', '材料成型']
  }
  const classes = ['1班', '2班', '3班', '4班']
  const statuses = [1, 2, 3, 4] // 在读、休学、退学、毕业
  const surnames = ['张', '李', '王', '刘', '陈', '杨', '黄', '赵', '周', '吴', '徐', '孙', '马', '朱', '胡', '郭', '何', '高', '林', '罗']

  const students = []
  for (let i = 1; i <= 100; i++) {
    const college = colleges[Math.floor(Math.random() * colleges.length)]
    const majorList = majors[college]
    const major = majorList[Math.floor(Math.random() * majorList.length)]
    const className = `${major.slice(0, 2)}软工${classes[Math.floor(Math.random() * classes.length)]}`
    const enrollmentYear = 2020 + Math.floor(Math.random() * 5)
    const status = statuses[Math.floor(Math.random() * 4)]
    const gpa = (2.5 + Math.random() * 1.5).toFixed(2)

    students.push({
      id: i,
      studentNo: `${enrollmentYear}${String(Math.floor(Math.random() * 9999)).padStart(4, '0')}`,
      name: surnames[Math.floor(Math.random() * surnames.length)] + ['明', '华', '伟', '强', '丽', '敏', '军', '杰', '涛', '鹏'][Math.floor(Math.random() * 10)],
      gender: Math.random() > 0.5 ? 1 : 2,
      birthday: `${1998 + Math.floor(Math.random() * 6)}-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
      phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
      email: `student${i}@university.edu.cn`,
      college,
      major,
      className,
      enrollmentYear,
      studentStatus: status,
      gpa: parseFloat(gpa)
    })
  }
  return students
}

const allMockStudents = generateMockStudents()

const router = useRouter()

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const queryForm = reactive({
  name: '',
  college: '',
  major: '',
  enrollmentYear: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  studentNo: '',
  name: '',
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
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  college: [{ required: true, message: '请输入学院', trigger: 'blur' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }]
}

const years = computed(() => {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 10 }, (_, i) => currentYear - i)
})

const dialogTitle = computed(() => isEdit.value ? '编辑学生' : '新增学生')

// 获取列表数据（模拟）
const fetchList = () => {
  loading.value = true

  // 模拟过滤
  let filtered = [...allMockStudents]
  if (queryForm.name) {
    filtered = filtered.filter(s => s.name.includes(queryForm.name))
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
  }).then(async () => {
    ElMessage.success('删除成功')
    fetchList()
  }).catch(() => {})
}

const viewProfile = (row) => {
  router.push(`/student/profile/${row.studentNo}`)
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
