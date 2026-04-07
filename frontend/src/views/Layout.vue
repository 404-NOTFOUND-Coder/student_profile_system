<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo">
        <el-icon v-if="!isCollapse" :size="32" color="#409eff"><UserFilled /></el-icon>
        <span v-if="!isCollapse">学生画像系统</span>
        <el-icon v-else><Operation /></el-icon>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        router
        class="menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="student">
          <template #title>
            <el-icon><User /></el-icon>
            <span>学生档案</span>
          </template>
          <el-menu-item index="/student/list">
            <el-icon><Document /></el-icon>
            <template #title>学生基本信息</template>
          </el-menu-item>
          <el-menu-item index="/student/activities">
            <el-icon><Management /></el-icon>
            <template #title>学生活动管理</template>
          </el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/knowledge-graph">
          <el-icon><Connection /></el-icon>
          <template #title>知识图谱</template>
        </el-menu-item>

        <el-menu-item index="/profile/generate">
          <el-icon><UserFilled /></el-icon>
          <template #title>学生画像</template>
        </el-menu-item>

        <el-menu-item index="/activity/recommend">
          <el-icon><Bell /></el-icon>
          <template #title>学业活动推荐</template>
        </el-menu-item>

        <el-menu-item index="/analytics">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>数据分析</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" /><Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="$route.meta.title !== '首页'">
              {{ $route.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32">管</el-avatar>
              <span class="username">管理员</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>
                  设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main class="main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  switch (command) {
    case 'logout':
      localStorage.removeItem('token')
      router.push('/login')
      break
    case 'profile':
      router.push('/personal-center')
      break
    case 'settings':
      router.push('/settings')
      break
  }
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
}

.aside {
  background: #1e1e1e;
  transition: width 0.3s;
  overflow: hidden;
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    background: #252525;
    
    img {
      width: 32px;
      height: 32px;
    }
  }
  
  .menu {
    border-right: none;
    background: #1e1e1e;
    
    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      color: #a0a0a0;
      background: transparent;
      
      &:hover {
        color: #fff;
        background: #2d2d2d;
      }
    }
    
    :deep(.el-menu-item.is-active) {
      background: #409eff;
      color: #fff;
    }
    
    :deep(.el-sub-menu.is-active > .el-sub-menu__title) {
      color: #fff;
      background: #2d2d2d;
    }
    
    :deep(.el-sub-menu .el-menu-item) {
      padding-left: 50px !important;
      color: #909090;
      background: #1e1e1e;

      &:hover {
        color: #fff;
        background: #2d2d2d;
      }

      &.is-active {
        background: #409eff;
        color: #fff;
      }
    }
  }
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 20px;
  
  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;
    
    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
      color: #606266;
      
      &:hover {
        color: #409eff;
      }
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      
      .username {
        color: #606266;
      }
    }
  }
}

.main {
  background: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
