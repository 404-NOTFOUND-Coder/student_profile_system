<template>
  <div class="profile-generate-container">
    <el-card class="mb-20">
      <template #header>
        <span>学生画像生成</span>
      </template>

      <div class="generate-form">
        <el-input
          v-model="studentNo"
          placeholder="请输入学号"
          class="student-no-input"
          clearable
        >
          <template #prepend>学号</template>
        </el-input>
        <el-button type="primary" @click="generateProfile" :loading="loading">
          生成用户画像
        </el-button>
        <el-button @click="viewProfile" :disabled="!hasProfile">
          查看画像详情
        </el-button>
      </div>
    </el-card>

    <!-- 画像报告展示 -->
    <el-card v-if="hasProfile" class="profile-report">
      <template #header>
        <div class="report-header">
          <span>画像分析报告</span>
          <el-tag type="success">AI生成</el-tag>
        </div>
      </template>

      <div class="report-content" v-html="formattedReport"></div>

      <div class="report-actions">
        <el-button type="primary" @click="downloadReport">下载报告</el-button>
        <el-button @click="resetProfile">重置</el-button>
      </div>
    </el-card>

    <!-- 加载状态 -->
    <el-card v-if="loading" class="loading-card">
      <div class="loading-content">
        <el-icon class="is-loading"><Loading /></el-icon>
        <span>正在生成画像分析报告，请稍候...</span>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { allStudents, allActivities } from '@/data/mockData'
import { Loading } from '@element-plus/icons-vue'

const router = useRouter()
const studentNo = ref('')
const loading = ref(false)
const profileReport = ref('')
const hasProfile = computed(() => !!profileReport.value)

// 格式化报告，转换Markdown格式到HTML
const formattedReport = computed(() => {
  if (!profileReport.value) return ''
  let html = profileReport.value
    // 标题处理
    .replace(/^### (.+)$/gm, '<h3>$1</h3>')
    .replace(/^## (.+)$/gm, '<h2>$1</h2>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.+?)\*/g, '<em>$1</em>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/^(\d+)\. (.+)$/gm, '<li><strong>$1.</strong> $2</li>')
  // 段落处理
  html = html.split('\n\n').map(p => {
    if (p.startsWith('<h') || p.startsWith('<li')) return p
    return `<p>${p}</p>`
  }).join('')
  return html
})

// 生成画像报告
const generateProfile = () => {
  if (!studentNo.value) {
    ElMessage.warning('请输入学号')
    return
  }

  // 检查学号是否存在
  const student = allStudents.find(s => s.studentNo === studentNo.value)
  if (!student) {
    ElMessage.error('学号不存在')
    return
  }

  loading.value = true

  // 模拟AI生成报告
  setTimeout(() => {
    profileReport.value = generateProfileReport(student)
    loading.value = false
    ElMessage.success('画像生成成功')
  }, 2000)
}

// 生成个性化报告
const generateProfileReport = (student) => {
  const studentActivities = allActivities.filter(a =>
    a.participants && a.participants.includes(student.studentNo)
  )

  // 计算统计数据
  const activityCount = studentActivities.length
  const nationalLevel = studentActivities.filter(a => a.level === '国家级' || a.level === '国际级').length
  const volunteerHours = studentActivities.filter(a => a.type === '志愿服务').length * 8

  // 根据学生特征生成不同报告
  const profileType = student.profileType || '复合探索型'
  const knowledgeEntropy = student.knowledgeEntropy || 3.5
  const graphEditDistance = student.graphEditDistance || 5.0

  let typeDescription = ''
  let trendDescription = ''
  let suggestionDescription = ''

  if (profileType === '学术研究型') {
    typeDescription = '该生展现出较强的学术研究潜力和专业深度导向。'
  } else if (profileType === '实践应用型') {
    typeDescription = '该生展现出较强的实践能力和应用导向。'
  } else if (profileType === '创新创业型') {
    typeDescription = '该生展现出较强的创新意识和创业潜质。'
  } else {
    typeDescription = '该生展现出多元化的学习和发展倾向。'
  }

  // 高熵值评价
  if (knowledgeEntropy > 3.5) {
    trendDescription = '该生知识结构呈现高度发散状态。这表明其兴趣点广泛，倾向于跨学科、跨领域的知识摄取。'
  } else {
    trendDescription = '该生知识结构相对集中，具有较好的专业深度。'
  }

  // GED评价
  if (graphEditDistance > 10) {
    suggestionDescription = '建议在保持现有优势的同时，尝试担任技术类项目的负责人，将"组织领导力"与"技术创新"结合。'
  } else {
    suggestionDescription = '建议继续深耕专业领域，通过参与高水平学科竞赛将知识点串联起来。'
  }

  return `你好，我是教育诊断专家 AI。基于你提供的多模态数据，我对该学生（ID: ${student.studentNo}）进行了深度画像分析，报告如下：

### 1. 事实归纳：${profileType}轨迹

${typeDescription}
* **组织经历：** 积极参与各类学生活动，具备一定的团队协作能力。
* **综合素质：** 参与${activityCount}项活动，其中${nationalLevel}项为国家级以上，展现出全面的发展潜力。
* **实践基础：** 累计志愿服务${volunteerHours}小时，体现了良好的社会责任感。

### 2. 指标诊断：知识结构分析
* **知识结构熵 H(S)=${knowledgeEntropy.toFixed(1)}：**
${trendDescription}
* **学习路径偏离度 GED=${graphEditDistance.toFixed(2)}：**
结合其丰富的社会实践与组织经历，该偏离度属于正常范围。表明该生在课堂学习与课外实践之间取得了较好的平衡。

### 3. 趋势预测：发展潜力评估
* **职业潜力：** 该生具备成为"复合型"人才的潜力，其多元化的经历为其未来发展奠定了良好基础。
* **优势领域：** 在实践应用、创新思维和团队协作方面表现突出。

### 4. 建议：个性化发展指导
${suggestionDescription}

**总结：** 该生是一位具备综合素质的人才苗子。建议通过参与高水平竞赛和科研项目，将现有的实践经验转化为专业核心竞争力。`
}

const viewProfile = () => {
  if (studentNo.value) {
    router.push(`/student/profile/${studentNo.value}`)
  }
}

const downloadReport = () => {
  ElMessage.success('报告下载功能开发中')
}

const resetProfile = () => {
  studentNo.value = ''
  profileReport.value = ''
}
</script>

<style scoped lang="scss">
.profile-generate-container {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.generate-form {
  display: flex;
  gap: 12px;
  align-items: center;

  .student-no-input {
    width: 300px;
  }
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-report {
  margin-bottom: 20px;
}

.report-content {
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
  line-height: 1.8;
  font-size: 14px;

  :deep(h2) {
    font-size: 18px;
    color: #409eff;
    margin: 20px 0 10px 0;
    border-bottom: 2px solid #409eff;
    padding-bottom: 8px;
  }

  :deep(h3) {
    font-size: 16px;
    color: #303133;
    margin: 16px 0 8px 0;
  }

  :deep(p) {
    margin: 10px 0;
    text-indent: 2em;
  }

  :deep(li) {
    margin: 8px 0;
    padding-left: 1em;
  }

  :deep(strong) {
    color: #409eff;
  }
}

.report-actions {
  margin-top: 20px;
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.loading-card {
  text-align: center;
  padding: 40px;
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;

  .el-icon {
    font-size: 32px;
    color: #409eff;
  }
}
</style>