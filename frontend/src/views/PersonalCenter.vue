<template>
  <div class="personal-center">
    <!-- 用户信息卡片 -->
    <el-card class="user-card mb-20">
      <div class="user-profile">
        <div class="user-avatar-section">
          <el-avatar :size="100" :style="{ background: avatarBgColor }">
            {{ userInfo.name?.charAt(0) || '管' }}
          </el-avatar>
          <div class="user-badge">
            <el-tag type="success" size="small">已认证</el-tag>
          </div>
        </div>
        <div class="user-basic-info">
          <div class="user-name-row">
            <span class="user-name">{{ userInfo.name }}</span>
            <el-tag type="warning" size="small">{{ userInfo.role }}</el-tag>
          </div>
          <div class="user-meta">
            <span><el-icon><User /></el-icon> 工号：{{ userInfo.employeeNo }}</span>
            <span><el-icon><OfficeBuilding /></el-icon> {{ userInfo.department }}</span>
            <span><el-icon><Calendar /></el-icon> 入职时间：2020-09-01</span>
          </div>
        </div>
        <div class="user-stats">
          <div class="stat-item">
            <div class="stat-value">{{ userInfo.studentCount }}</div>
            <div class="stat-label">管理学生</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ userInfo.loginCount }}</div>
            <div class="stat-label">登录次数</div>
          </div>
          <div class="stat-item" v-if="isStudent">
            <div class="stat-value">{{ userInfo.avgGpa }}</div>
            <div class="stat-label">平均绩点</div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 基本信息 -->
    <el-card class="mb-20">
      <template #header>
        <span>基本信息</span>
      </template>

      <el-form :model="userInfo" label-width="100px" class="info-form">
        <el-form-item label="用户名">
          <el-input v-model="userInfo.name" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="工号">
          <el-input v-model="userInfo.employeeNo" disabled />
            </el-form-item>

            <el-form-item label="所属部门">
              <el-select v-model="userInfo.department" placeholder="请选择部门">
                <el-option label="计算机学院" value="计算机学院" />
                <el-option label="软件学院" value="软件学院" />
                <el-option label="信息学院" value="信息学院" />
                <el-option label="教务处" value="教务处" />
              </el-select>
            </el-form-item>

            <el-form-item label="职位">
              <el-input v-model="userInfo.position" placeholder="请输入职位" />
            </el-form-item>

            <el-form-item label="邮箱">
              <el-input v-model="userInfo.email" placeholder="请输入邮箱">
                <template #append>
                  <el-button @click="sendVerifyEmail" :disabled="emailVerified">
                    {{ emailVerified ? '已验证' : '验证' }}
                  </el-button>
                </template>
              </el-input>
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
const isStudent = computed(() => userRole.value === 'student')

const avatarBgColor = ref('#409eff')
const emailVerified = ref(true)
const showPasswordDialog = ref(false)
const passwordFormRef = ref(null)

// 从localStorage获取用户信息
const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

const userInfo = reactive({
  name: storedUserInfo.name || '管理员',
  employeeNo: storedUserInfo.employeeNo || '20240001',
  role: storedUserInfo.roleName || '系统管理员',
  department: storedUserInfo.department || '计算机学院',
  position: storedUserInfo.position || '教学秘书',
  email: storedUserInfo.email || 'admin@university.edu.cn',
  phone: storedUserInfo.phone || '13800138000',
  studentCount: allStudents.length,
  loginCount: 128,
  avgGpa: parseFloat((allStudents.reduce((sum, s) => sum + s.gpa, 0) / allStudents.length).toFixed(2))
})

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
  userInfo.studentCount = allStudents.length
  userInfo.avgGpa = parseFloat((allStudents.reduce((sum, s) => sum + s.gpa, 0) / allStudents.length).toFixed(2))
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