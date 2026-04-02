<template>
  <div class="settings-page">
    <el-card>
      <template #header>
        <span>系统设置</span>
      </template>

      <el-tabs v-model="activeTab" class="settings-tabs">
        <!-- 界面设置 -->
        <el-tab-pane label="界面设置" name="interface">
          <div class="settings-section">
            <div class="section-title">主题配置</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">深色模式</div>
                <div class="setting-desc">开启后系统将使用深色主题</div>
              </div>
              <el-switch v-model="settings.darkMode" @change="handleDarkModeChange" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">紧凑模式</div>
                <div class="setting-desc">减少界面元素间距，在屏幕上显示更多内容</div>
              </div>
              <el-switch v-model="settings.compactMode" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">侧边栏折叠</div>
                <div class="setting-desc">启动时自动折叠侧边栏</div>
              </div>
              <el-switch v-model="settings.sidebarCollapsed" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">主题颜色</div>
                <div class="setting-desc">自定义系统主题色彩</div>
              </div>
              <el-color-picker v-model="settings.themeColor" @change="handleThemeColorChange" />
            </div>
          </div>
        </el-tab-pane>

        <!-- 通知设置 -->
        <el-tab-pane label="通知设置" name="notification">
          <div class="settings-section">
            <div class="section-title">预警通知</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">学业预警通知</div>
                <div class="setting-desc">当学生触发学业预警时，发送通知</div>
              </div>
              <el-switch v-model="settings.warningNotification" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">预警阈值</div>
                <div class="setting-desc">当 GPA 低于此值时触发预警</div>
              </div>
              <el-input-number 
                v-model="settings.gpaThreshold" 
                :min="1" 
                :max="4" 
                :step="0.1" 
                :precision="1"
                style="width: 120px;"
              />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">邮件通知</div>
                <div class="setting-desc">通过邮件发送预警通知</div>
              </div>
              <el-switch v-model="settings.emailNotification" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">站内消息</div>
                <div class="setting-desc">在系统内显示预警消息</div>
              </div>
              <el-switch v-model="settings.siteNotification" />
            </div>
          </div>

          <div class="settings-section">
            <div class="section-title">系统通知</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">系统更新提示</div>
                <div class="setting-desc">有新版本时显示更新提示</div>
              </div>
              <el-switch v-model="settings.updateNotification" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">操作成功提示</div>
                <div class="setting-desc">执行操作后显示成功提示</div>
              </div>
              <el-switch v-model="settings.successNotification" />
            </div>
          </div>
        </el-tab-pane>

        <!-- 数据设置 -->
        <el-tab-pane label="数据设置" name="data">
          <div class="settings-section">
            <div class="section-title">数据展示</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">每页显示条数</div>
                <div class="setting-desc">列表每页显示的数据条数</div>
              </div>
              <el-select v-model="settings.pageSize" style="width: 120px;">
                <el-option :label="10" :value="10" />
                <el-option :label="20" :value="20" />
                <el-option :label="50" :value="50" />
                <el-option :label="100" :value="100" />
              </el-select>
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">数据刷新间隔</div>
                <div class="setting-desc">自动刷新数据的间隔时间（秒）</div>
              </div>
              <el-input-number 
                v-model="settings.refreshInterval" 
                :min="0" 
                :max="300" 
                :step="10"
                style="width: 120px;"
              />
              <span class="ml-10" style="color: #909399;">0 表示不自动刷新</span>
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">默认排序字段</div>
                <div class="setting-desc">列表默认使用的排序字段</div>
              </div>
              <el-select v-model="settings.defaultSortField" style="width: 150px;">
                <el-option label="学号" value="studentNo" />
                <el-option label="姓名" value="name" />
                <el-option label="GPA" value="gpa" />
                <el-option label="更新时间" value="updateTime" />
              </el-select>
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">默认排序方式</div>
                <div class="setting-desc">列表默认的排序方式</div>
              </div>
              <el-select v-model="settings.defaultSortOrder" style="width: 120px;">
                <el-option label="升序" value="asc" />
                <el-option label="降序" value="desc" />
              </el-select>
            </div>
          </div>

          <div class="settings-section">
            <div class="section-title">数据导出</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">导出文件格式</div>
                <div class="setting-desc">导出数据时使用的文件格式</div>
              </div>
              <el-select v-model="settings.exportFormat" style="width: 120px;">
                <el-option label="Excel" value="xlsx" />
                <el-option label="CSV" value="csv" />
                <el-option label="JSON" value="json" />
              </el-select>
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">导出包含图表</div>
                <div class="setting-desc">导出时包含图表数据</div>
              </div>
              <el-switch v-model="settings.exportWithChart" />
            </div>
          </div>
        </el-tab-pane>

        <!-- 隐私设置 -->
        <el-tab-pane label="隐私设置" name="privacy">
          <div class="settings-section">
            <div class="section-title">信息可见性</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">显示登录记录</div>
                <div class="setting-desc">在个人中心显示登录历史</div>
              </div>
              <el-switch v-model="settings.showLoginHistory" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">显示操作日志</div>
                <div class="setting-desc">显示最近的操作记录</div>
              </div>
              <el-switch v-model="settings.showOperationLog" />
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">公开个人资料</div>
                <div class="setting-desc">允许其他用户查看您的个人资料</div>
              </div>
              <el-switch v-model="settings.publicProfile" />
            </div>
          </div>

          <div class="settings-section">
            <div class="section-title">会话管理</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">自动退出登录</div>
                <div class="setting-desc">无操作一段时间后自动退出</div>
              </div>
              <el-select v-model="settings.autoLogout" style="width: 150px;">
                <el-option label="30 分钟" :value="30" />
                <el-option label="1 小时" :value="60" />
                <el-option label="2 小时" :value="120" />
                <el-option label="从不" :value="0" />
              </el-select>
            </div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">允许多端登录</div>
                <div class="setting-desc">允许在不同设备同时登录</div>
              </div>
              <el-switch v-model="settings.allowMultiLogin" />
            </div>
          </div>
        </el-tab-pane>

        <!-- 关于 -->
        <el-tab-pane label="关于系统" name="about">
          <div class="settings-section">
            <div class="section-title">系统信息</div>
            
            <div class="about-item">
              <span class="about-label">系统名称</span>
              <span class="about-value">学生用户画像系统</span>
            </div>
            
            <div class="about-item">
              <span class="about-label">版本号</span>
              <span class="about-value">v1.0.0</span>
            </div>
            
            <div class="about-item">
              <span class="about-label">前端框架</span>
              <span class="about-value">Vue 3 + Element Plus</span>
            </div>
            
            <div class="about-item">
              <span class="about-label">后端框架</span>
              <span class="about-value">Spring Boot</span>
            </div>
            
            <div class="about-item">
              <span class="about-label">数据库</span>
              <span class="about-value">MySQL 8.0</span>
            </div>
            
            <div class="about-item">
              <span class="about-label">部署时间</span>
              <span class="about-value">2024-03-01</span>
            </div>
          </div>

          <div class="settings-section">
            <div class="section-title">检查更新</div>
            
            <div class="setting-item">
              <div class="setting-info">
                <div class="setting-label">自动检查更新</div>
                <div class="setting-desc">启动时自动检查是否有新版本</div>
              </div>
              <el-switch v-model="settings.autoCheckUpdate" />
            </div>
            
            <div class="check-update">
              <el-button type="primary" plain @click="checkForUpdate">
                <el-icon><Refresh /></el-icon>
                检查更新
              </el-button>
              <span class="update-tip">当前已是最新版本</span>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div class="settings-footer">
        <el-button type="primary" @click="saveSettings">保存设置</el-button>
        <el-button @click="resetSettings">恢复默认</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('interface')

const settings = reactive({
  // 界面设置
  darkMode: false,
  compactMode: false,
  sidebarCollapsed: false,
  themeColor: '#409eff',
  
  // 通知设置
  warningNotification: true,
  gpaThreshold: 2.0,
  emailNotification: true,
  siteNotification: true,
  updateNotification: true,
  successNotification: true,
  
  // 数据设置
  pageSize: 20,
  refreshInterval: 0,
  defaultSortField: 'studentNo',
  defaultSortOrder: 'desc',
  exportFormat: 'xlsx',
  exportWithChart: false,
  
  // 隐私设置
  showLoginHistory: true,
  showOperationLog: true,
  publicProfile: false,
  autoLogout: 60,
  allowMultiLogin: true,
  
  // 关于
  autoCheckUpdate: true
})

const handleDarkModeChange = (value) => {
  ElMessage.success(value ? '已开启深色模式' : '已关闭深色模式')
}

const handleThemeColorChange = (value) => {
  ElMessage.success('主题颜色已更新')
}

const checkForUpdate = () => {
  ElMessage.info('当前已是最新版本')
}

const saveSettings = () => {
  // 保存到本地存储
  localStorage.setItem('systemSettings', JSON.stringify(settings))
  ElMessage.success('设置保存成功')
}

const resetSettings = () => {
  // 重置为默认值
  Object.assign(settings, {
    darkMode: false,
    compactMode: false,
    sidebarCollapsed: false,
    themeColor: '#409eff',
    warningNotification: true,
    gpaThreshold: 2.0,
    emailNotification: true,
    siteNotification: true,
    updateNotification: true,
    successNotification: true,
    pageSize: 20,
    refreshInterval: 0,
    defaultSortField: 'studentNo',
    defaultSortOrder: 'desc',
    exportFormat: 'xlsx',
    exportWithChart: false,
    showLoginHistory: true,
    showOperationLog: true,
    publicProfile: false,
    autoLogout: 60,
    allowMultiLogin: true,
    autoCheckUpdate: true
  })
  ElMessage.success('已恢复默认设置')
}
</script>

<style scoped lang="scss">
.settings-page {
  padding: 20px;
  max-width: 900px;
}

.settings-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 20px;
  }
}

.settings-section {
  padding: 20px 0;
  
  &:not(:last-child) {
    border-bottom: 1px solid #ebeef5;
  }
  
  .section-title {
    font-size: 16px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 20px;
  }
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  
  .setting-info {
    flex: 1;
    
    .setting-label {
      font-weight: 500;
      color: #303133;
      margin-bottom: 4px;
    }
    
    .setting-desc {
      font-size: 12px;
      color: #909399;
    }
  }
  
  .ml-10 {
    margin-left: 10px;
  }
}

.about-item {
  display: flex;
  padding: 12px 0;
  
  .about-label {
    width: 120px;
    color: #909399;
  }
  
  .about-value {
    color: #303133;
    font-weight: 500;
  }
}

.check-update {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  
  .update-tip {
    color: #67c23a;
    font-size: 12px;
  }
}

.settings-footer {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  text-align: center;
}
</style>
