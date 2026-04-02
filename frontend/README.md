# 前端开发指南

## 📦 技术栈

- **框架**: Vue 3.4 + Composition API
- **状态管理**: Pinia 2.1
- **UI 组件**: Element Plus 2.5
- **图表**: ECharts 5.4
- **构建工具**: Vite 5.0
- **样式**: SCSS

## 🏃 项目启动

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

## 📐 项目结构

```
src/
├── api/              # API 接口封装
├── assets/           # 静态资源
│   └── styles/       # 全局样式
├── components/       # 公共组件
├── router/           # 路由配置
├── stores/           # Pinia 状态管理
├── utils/            # 工具函数
└── views/            # 页面组件
    └── student/      # 学生模块
```

## 🎨 开发规范

### 组件规范

```vue
<template>
  <div class="component-name">
    <!-- 模板内容 -->
  </div>
</template>

<script setup>
// 1. 导入
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { studentApi } from '@/api/student'

// 2. 响应式数据
const loading = ref(false)
const data = ref([])

// 3. 计算属性
const computedData = computed(() => data.value.length)

// 4. 方法
const fetchData = async () => {
  loading.value = true
  try {
    const res = await studentApi.list()
    data.value = res.data
  } finally {
    loading.value = false
  }
}

// 5. 生命周期
onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
/* 样式隔离 */
.component-name {
  /* SCSS 样式 */
}
</style>
```

### API 调用规范

```javascript
// api/student.js
import request from './request'

export const studentApi = {
  // GET 请求
  list(params) {
    return request.get('/student/list', { params })
  },
  
  // POST 请求
  create(data) {
    return request.post('/student', data)
  },
  
  // 路径参数
  getById(id) {
    return request.get(`/student/${id}`)
  }
}
```

## 📊 ECharts 使用

### 基础图表

```javascript
import * as echarts from 'echarts'

const chartRef = ref(null)
let chart = null

const initChart = () => {
  chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['Mon', 'Tue', 'Wed'] },
    yAxis: { type: 'value' },
    series: [{ data: [120, 200, 150], type: 'bar' }]
  })
}

// 响应式处理
window.addEventListener('resize', () => chart?.resize())

onUnmounted(() => {
  chart?.dispose()
})
```

### 雷达图

```javascript
const radarOption = {
  radar: {
    indicator: [
      { name: '学术能力', max: 100 },
      { name: '实践能力', max: 100 }
    ]
  },
  series: [{
    type: 'radar',
    data: [{ value: [85, 92], name: '能力分布' }]
  }]
}
```

## 🔐 路由守卫

```javascript
// router/index.js
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title + ' - 系统名称'
  }
  
  // 权限验证
  const token = localStorage.getItem('token')
  if (to.meta.requireAuth && !token) {
    next('/login')
  } else {
    next()
  }
})
```

## 🎯 Element Plus 组件使用

### 表格分页

```vue
<el-table :data="tableData" v-loading="loading">
  <!-- 列定义 -->
</el-table>

<el-pagination
  v-model:current-page="pagination.pageNum"
  v-model:page-size="pagination.pageSize"
  :total="pagination.total"
  @current-change="handlePageChange"
/>
```

### 表单验证

```javascript
const rules = {
  studentNo: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { pattern: /^\d{10}$/, message: '学号格式错误', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式错误', trigger: 'blur' }
  ]
}
```

## 📝 开发技巧

### 1. 性能优化

- 使用 `v-once` 缓存静态内容
- 使用 `shallowRef` 避免深度响应
- 路由懒加载: `() => import('@/views/...')`

### 2. 调试技巧

```javascript
// Vue DevTools
console.log('数据:', data)

// ECharts 调试
chart.on('click', params => {
  console.log('点击参数:', params)
})
```

### 3. 响应式图表

```javascript
// 监听窗口变化
const resizeHandler = () => {
  if (chart) {
    chart.resize()
  }
}

onMounted(() => {
  window.addEventListener('resize', resizeHandler)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeHandler)
  chart?.dispose()
})
```
