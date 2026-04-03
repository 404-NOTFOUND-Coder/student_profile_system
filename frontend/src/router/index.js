import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', icon: 'Odometer' }
      },
      {
        path: 'student/list',
        name: 'StudentList',
        component: () => import('@/views/student/StudentList.vue'),
        meta: { title: '学生档案', icon: 'User' }
      },
      {
        path: 'student/profile/:studentNo',
        name: 'StudentProfile',
        component: () => import('@/views/student/StudentProfile.vue'),
        meta: { title: '学生画像' }
      },
      {
        path: 'warning',
        name: 'AcademicWarning',
        component: () => import('@/views/Warning.vue'),
        meta: { title: '学业预警', icon: 'Warning' }
      },
      {
        path: 'analytics',
        name: 'Analytics',
        component: () => import('@/views/Analytics.vue'),
        meta: { title: '数据分析', icon: 'DataAnalysis' }
      },
      {
        path: 'knowledge-graph',
        name: 'KnowledgeGraph',
        component: () => import('@/views/KnowledgeGraph.vue'),
        meta: { title: '知识图谱', icon: 'Connection' }
      },
      {
        path: 'activity/recommend',
        name: 'ActivityRecommend',
        component: () => import('@/views/ActivityRecommend.vue'),
        meta: { title: '活动推荐', icon: 'Bell' }
      },
      {
        path: 'activity/manage',
        name: 'ActivityManagement',
        component: () => import('@/views/ActivityManagement.vue'),
        meta: { title: '活动管理', icon: 'Management' }
      },
      {
        path: 'personal-center',
        name: 'PersonalCenter',
        component: () => import('@/views/PersonalCenter.vue'),
        meta: { title: '个人中心', icon: 'User' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue'),
        meta: { title: '系统设置', icon: 'Setting' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - 学生用户画像系统'
  }
  next()
})

export default router
