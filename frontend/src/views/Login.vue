<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>学生用户画像系统</h2>
          <p>多维度学生画像分析平台</p>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-checkbox v-model="form.remember">记住密码</el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            :loading="loading" 
            class="login-btn"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span>默认账号: admin / admin123</span>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: 'admin',
  password: 'admin123',
  remember: false
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    
    // 模拟登录
    setTimeout(() => {
      ElMessage.success('登录成功')
      localStorage.setItem('token', 'mock-jwt-token')
      // 保存用户角色：admin=管理员, teacher=老师, student=学生
      const role = form.username === 'admin' ? 'admin' : form.username === 'teacher' ? 'teacher' : 'student'
      localStorage.setItem('userRole', role)
      router.push('/dashboard')
      loading.value = false
    }, 800)
    
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);

  .card-header {
    text-align: center;
    
    h2 {
      margin: 0 0 8px 0;
      color: #303133;
    }
    
    p {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }
}

.login-btn {
  width: 100%;
}

.login-footer {
  text-align: center;
  color: #909399;
  font-size: 12px;
  margin-top: 16px;
}
</style>
