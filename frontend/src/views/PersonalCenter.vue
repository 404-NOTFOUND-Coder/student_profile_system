<template>
  <div class="personal-center">
    <!-- 用户信息卡片 -->
    <el-card class="user-card mb-20">
      <div class="user-profile">
        <div class="user-avatar-section">
          <el-avatar :size="100" :style="{ background: avatarBgColor }">
            {{ getAvatarText() }}
          </el-avatar>
          <div class="user-badge">
            <el-tag type="success" size="small">已认证</el-tag>
          </div>
        </div>
        <div class="user-basic-info">
          <div class="user-name-row">
            <span class="user-name">{{ getDisplayName() }}</span>
            <el-tag type="warning" size="small">{{ userInfo.roleName }}</el-tag>
          </div>
          <div class="user-meta">
            <!-- 系统管理员 -->
            <template v-if="isAdmin">
              <span><el-icon><User /></el-icon> 管理员编号：{{ userInfo.employeeNo }}</span>
              <span><el-icon><OfficeBuilding /></el-icon> {{ userInfo.department }}</span>
              <span><el-icon><Calendar /></el-icon> 任职时间：{{ userInfo.joinDate }}</span>
            </template>
            <!-- 教务处老师 -->
            <template v-else-if="isJiaowu">
              <span><el-icon><User /></el-icon> 教职工号：{{ userInfo.employeeNo }}</span>
              <span><el-icon><OfficeBuilding /></el-icon> {{ userInfo.department }}</span>
              <span><el-icon><Calendar /></el-icon> 入职时间：{{ userInfo.joinDate }}</span>
            </template>
            <!-- 辅导员 -->
            <template v-else-if="isFudaoyuan">
              <span><el-icon><User /></el-icon> 辅导员编号：{{ userInfo.employeeNo }}</span>
              <span><el-icon><OfficeBuilding /></el-icon> 负责学院：{{ userInfo.managedCollege }}</span>
              <span><el-icon><Calendar /></el-icon> 任职时间：{{ userInfo.joinDate }}</span>
            </template>
            <!-- 学生 -->
            <template v-else>
              <span><el-icon><User /></el-icon> 学号：{{ userInfo.studentNo }}</span>
              <span><el-icon><OfficeBuilding /></el-icon> {{ userInfo.college }}</span>
              <span><el-icon><Calendar /></el-icon> 入学时间：{{ userInfo.enrollmentYear }}-09</span>
            </template>
          </div>
        </div>
        <div class="user-stats">
          <div class="stat-item" v-if="!isStudent">
            <div class="stat-value">{{ userInfo.managedCount }}</div>
            <div class="stat-label">{{ isAdmin ? '管理用户' : isJiaowu ? '管理班级' : '管理学生' }}</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ userInfo.loginCount }}</div>
            <div class="stat-label">登录次数</div>
          </div>

          <div class="stat-item" v-if="isFudaoyuan">
            <div class="stat-value">{{ userInfo.warningCount }}</div>
            <div class="stat-label">预警学生</div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 基本信息 - 系统管理员 -->
    <el-card class="mb-20" v-if="isAdmin">
      <template #header>
        <span>基本信息</span>
      </template>
      <el-form :model="userInfo" label-width="120px" class="info-form">
        <el-form-item label="管理员编号">
          <el-input v-model="userInfo.employeeNo" disabled />
        </el-form-item>
        <el-form-item label="管理员姓名">
          <el-input v-model="userInfo.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input v-model="userInfo.department" disabled />
        </el-form-item>
        <el-form-item label="管理员级别">
          <el-input v-model="userInfo.adminLevel" disabled />
        </el-form-item>
        <el-form-item label="任职时间">
          <el-input v-model="userInfo.joinDate" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <div class="form-actions">
        <el-button type="primary" @click="saveProfile">保存修改</el-button>
        <el-button @click="resetForm">重置</el-button>
      </div>
    </el-card>

    <!-- 基本信息 - 教务处老师 -->
    <el-card class="mb-20" v-else-if="isJiaowu">
      <template #header>
        <span>基本信息</span>
      </template>
      <el-form :model="userInfo" label-width="120px" class="info-form">
        <el-form-item label="教职工号">
          <el-input v-model="userInfo.employeeNo" disabled />
        </el-form-item>
        <el-form-item label="教师姓名">
          <el-input v-model="userInfo.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input v-model="userInfo.department" />
        </el-form-item>
        <el-form-item label="职位职称">
          <el-input v-model="userInfo.title" placeholder="请输入职位职称" />
        </el-form-item>
        <el-form-item label="任教科目">
          <el-input v-model="userInfo.subject" placeholder="请输入任教科目" />
        </el-form-item>
        <el-form-item label="入职时间">
          <el-input v-model="userInfo.joinDate" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <div class="form-actions">
        <el-button type="primary" @click="saveProfile">保存修改</el-button>
        <el-button @click="resetForm">重置</el-button>
      </div>
    </el-card>

    <!-- 基本信息 - 辅导员 -->
    <el-card class="mb-20" v-else-if="isFudaoyuan">
      <template #header>
        <span>基本信息</span>
      </template>
      <el-form :model="userInfo" label-width="120px" class="info-form">
        <el-form-item label="辅导员编号">
          <el-input v-model="userInfo.employeeNo" disabled />
        </el-form-item>
        <el-form-item label="辅导员姓名">
          <el-input v-model="userInfo.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="负责学院">
          <el-input v-model="userInfo.managedCollege" />
        </el-form-item>
        <el-form-item label="所带班级">
          <el-input v-model="userInfo.managedClasses" disabled />
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="userInfo.position" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="任职时间">
          <el-input v-model="userInfo.joinDate" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <div class="form-actions">
        <el-button type="primary" @click="saveProfile">保存修改</el-button>
        <el-button @click="resetForm">重置</el-button>
      </div>
    </el-card>

    <!-- 基本信息 - 学生 -->
    <el-card class="mb-20" v-else>
      <template #header>
        <span>基本信息</span>
      </template>
      <el-form :model="userInfo" label-width="120px" class="info-form">
        <el-form-item label="学号">
          <el-input v-model="userInfo.studentNo" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="userInfo.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="userInfo.gender" disabled />
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="userInfo.college" disabled />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="userInfo.major" disabled />
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="userInfo.className" disabled />
        </el-form-item>
        <el-form-item label="入学年份">
          <el-input v-model="userInfo.enrollmentYear" disabled />
        </el-form-item>
        <el-form-item label="学籍状态">
          <el-input v-model="userInfo.studentStatus" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <div class="form-actions">
        <el-button type="primary" @click="saveProfile">保存修改</el-button>
        <el-button @click="resetForm">重置</el-button>
      </div>
    </el-card>

    <!-- 安全设置 -->
    <el-card class="mt-20">
      <template #header>
        <span>安全设置</span>
      </template>

      <div class="security-item">
        <div class="security-info">
          <div class="security-title">
            <el-icon><Lock /></el-icon>
            <span>登录密码</span>
          </div>
          <div class="security-desc">定期修改密码可以提高账号安全性</div>
        </div>
        <el-button @click="showPasswordDialog = true">修改</el-button>
      </div>

      <el-divider />

      <div class="security-item">
        <div class="security-info">
          <div class="security-title">
            <el-icon><Message /></el-icon>
            <span>邮箱绑定</span>
          </div>
          <div class="security-desc">已绑定邮箱：{{ maskEmail(userInfo.email) }}</div>
        </div>
        <el-tag :type="emailVerified ? 'success' : 'warning'" size="small">
          {{ emailVerified ? '已验证' : '未验证' }}
        </el-tag>
      </div>

      <el-divider />

      <div class="security-item">
        <div class="security-info">
          <div class="security-title">
            <el-icon><Phone /></el-icon>
            <span>手机绑定</span>
          </div>
          <div class="security-desc">已绑定手机：{{ maskPhone(userInfo.phone) }}</div>
        </div>
        <el-tag type="success" size="small">已绑定</el-tag>
      </div>
    </el-card>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="500px">
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入当前密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { allStudents, userAccounts } from '@/data/mockData'

// 获取用户角色
const userRole = computed(() => localStorage.getItem('userRole') || 'admin')
const isAdmin = computed(() => userRole.value === 'admin')
const isJiaowu = computed(() => userRole.value === 'jiaowu')
const isFudaoyuan = computed(() => userRole.value === 'fudaoyuan')
const isStudent = computed(() => userRole.value === 'student')

const avatarBgColor = ref('#409eff')
const emailVerified = ref(true)
const showPasswordDialog = ref(false)
const passwordFormRef = ref(null)

// 根据角色获取用户信息
const getUserInfoByRole = () => {
  const baseInfo = {
    loginCount: 128,
    email: '',
    phone: ''
  }

  if (isAdmin.value) {
    return {
      ...baseInfo,
      employeeNo: 'A00001',
      name: '系统管理员',
      roleName: '系统管理员',
      role: 'admin',
      department: '信息中心',
      adminLevel: '超级管理员',
      joinDate: '2019-09-01',
      managedCount: 50,
      email: 'admin@university.edu.cn',
      phone: '13800138000'
    }
  } else if (isJiaowu.value) {
    return {
      ...baseInfo,
      employeeNo: 'J2020001',
      name: '张教务',
      roleName: '教务处老师',
      role: 'jiaowu',
      department: '教务处',
      title: '教务主任',
      subject: '教学管理',
      joinDate: '2020-09-01',
      managedCount: 45,
      email: 'jiaowu@university.edu.cn',
      phone: '13800138001'
    }
  } else if (isFudaoyuan.value) {
    return {
      ...baseInfo,
      employeeNo: 'F2020001',
      name: '李辅导员',
      roleName: '辅导员',
      role: 'fudaoyuan',
      managedCollege: '计算机科学与工程学院',
      managedClasses: '软工1班、软工2班、计科1班、计科2班',
      position: '年级辅导员',
      joinDate: '2020-09-01',
      managedCount: allStudents.filter(s => s.college === '计算机科学与工程学院（网络空间安全学院）').length,
      warningCount: allStudents.filter(s => s.college === '计算机科学与工程学院（网络空间安全学院）' && s.studentStatus === 3).length,
      email: 'fudaoyuan@university.edu.cn',
      phone: '13800138002'
    }
  } else {
    // 学生角色 - 使用第一个学生数据作为示例
    const student = allStudents[0] || {}
    return {
      ...baseInfo,
      studentNo: student.studentNo || '2020010906005',
      name: student.name || '张伟',
      roleName: '学生',
      role: 'student',
      gender: student.gender === 1 ? '男' : '女',
      college: student.college || '计算机科学与工程学院',
      major: student.major || '计算机科学与技术',
      className: student.className || '1班',
      enrollmentYear: student.enrollmentYear || 2020,
      studentStatus: student.studentStatus === 1 ? '在读' : '其他',
      email: student.email || 'student@university.edu.cn',
      phone: student.phone || '13800138003'
    }
  }
}

const userInfo = reactive(getUserInfoByRole())

const getAvatarText = () => {
  if (isStudent.value) {
    return userInfo.studentNo?.substring(10, 13) || '学'
  }
  return userInfo.name?.charAt(0) || '管'
}

const getDisplayName = () => {
  if (isStudent.value) {
    return `${userInfo.name} (${userInfo.studentNo})`
  }
  return userInfo.name
}

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const maskEmail = (email) => {
  if (!email) return ''
  const [username, domain] = email.split('@')
  if (!username || !domain) return email
  const maskedUsername = username.charAt(0) + '***' + username.charAt(username.length - 1)
  return maskedUsername + '@' + domain
}

const maskPhone = (phone) => {
  if (!phone) return ''
  return phone.substring(0, 3) + '****' + phone.substring(7)
}

const sendVerifyEmail = () => {
  ElMessage.success('验证邮件已发送，请登录邮箱查看')
}

const saveProfile = () => {
  ElMessage.success('个人信息保存成功（模拟）')
}

const resetForm = () => {
  ElMessage.info('已重置')
}

const changePassword = async () => {
  try {
    await passwordFormRef.value.validate()
    ElMessage.success('密码修改成功（模拟）')
    showPasswordDialog.value = false
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

onMounted(() => {
  // 更新统计数据
  if (isFudaoyuan.value) {
    userInfo.managedCount = allStudents.filter(s => s.college === '计算机科学与工程学院（网络空间安全学院）').length
    userInfo.warningCount = allStudents.filter(s => s.college === '计算机科学与工程学院（网络空间安全学院）' && s.studentStatus === 3).length
  }
})
</script>

<style scoped lang="scss">
.personal-center {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.user-card {
  .user-profile {
    display: flex;
    align-items: center;
    gap: 40px;
    padding: 20px;

    .user-avatar-section {
      position: relative;
      flex-shrink: 0;

      .user-badge {
        position: absolute;
        bottom: 0;
        right: -10px;
      }
    }

    .user-basic-info {
      flex: 1;

      .user-name-row {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 12px;

        .user-name {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
        }
      }

      .user-meta {
        display: flex;
        gap: 24px;
        color: #606266;
        font-size: 14px;

        span {
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }

    .user-stats {
      display: flex;
      gap: 40px;
      flex-shrink: 0;

      .stat-item {
        text-align: center;
        min-width: 80px;

        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #409eff;
        }

        .stat-label {
          font-size: 12px;
          color: #909399;
          margin-top: 4px;
        }
      }
    }
  }
}

.info-form {
  padding: 20px;
}

.form-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.security-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0;

  .security-info {
    .security-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 4px;
    }

    .security-desc {
      font-size: 12px;
      color: #909399;
      margin-left: 26px;
    }
  }
}
</style>