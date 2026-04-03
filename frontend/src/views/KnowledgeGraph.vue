<template>
  <div class="knowledge-graph-container">
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>学生知识图谱</span>
          <div class="header-actions">
            <el-select v-model="selectedStudent" placeholder="选择学生" filterable @change="onStudentChange" class="student-select">
              <el-option v-for="student in students" :key="student.no" :label="student.name + ' (' + student.no + ')'" :value="student.no" />
            </el-select>
            <el-button-group>
              <el-button @click="openAddDialog('static')">添加静态关系</el-button>
              <el-button @click="openAddDialog('dynamic')">添加动态关系</el-button>
            </el-button-group>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="图谱视图" name="graph" :lazy="false">
          <div class="graph-container" ref="graphContainerRef"></div>
        </el-tab-pane>
        <el-tab-pane label="四元组视图" name="quads">
          <el-table :data="quadsData" stripe border max-height="500">
            <el-table-column prop="head" label="头实体(学生)" width="150" />
            <el-table-column prop="relation" label="关系" width="180">
              <template #default="{ row }">
                <el-tag type="info">{{ row.relation }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="tail" label="尾实体(对象)" width="200" />
            <el-table-column prop="time" label="时间" width="120" />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="{ row }">
                <el-tag :type="row.type === 'static' ? 'success' : 'warning'">
                  {{ row.type === 'static' ? '静态' : '动态' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="统计信息" name="stats" :lazy="false">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="stat-card">
                <h4>活动类型分布</h4>
                <div ref="relationChartRef" class="chart-wrapper"></div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-card">
                <h4>活动级别分布</h4>
                <div ref="levelChartRef" class="chart-wrapper"></div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="mt-20">
            <el-col :span="12">
              <div class="stat-card">
                <h4>年度活动趋势</h4>
                <div ref="yearChartRef" class="chart-wrapper"></div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-card">
                <h4>活动汇总</h4>
                <div class="summary-stats">
                  <div class="stat-item">
                    <span class="label">总活动数:</span>
                    <span class="value">{{ activityStats.total }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="label">国家级以上:</span>
                    <span class="value">{{ getHighLevelCount() }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="label">最近一年:</span>
                    <span class="value">{{ getRecentYearCount() }}</span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 添加关系对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'static' ? '添加静态关系' : '添加动态关系'" width="500px">
      <el-form :model="relationForm" label-width="100px">
        <el-form-item label="学生学号">
          <el-input v-model="relationForm.studentNo" disabled />
        </el-form-item>
        <el-form-item label="关系类型">
          <el-select v-model="relationForm.relation" placeholder="选择关系" class="full-width">
            <el-option v-for="r in availableRelations" :key="r.name" :label="r.name" :value="r.name" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="dialogType === 'dynamic'" label="二级分类">
          <el-select v-model="relationForm.subRelation" placeholder="选择二级分类" class="full-width">
            <el-option v-for="sub in currentSubCategories" :key="sub" :label="sub" :value="sub" />
          </el-select>
        </el-form-item>
        <el-form-item :label="dialogType === 'static' ? '属性值' : '活动名称'">
          <el-input v-model="relationForm.tailName" :placeholder="dialogType === 'static' ? '请输入属性值' : '请输入活动名称'" />
        </el-form-item>
        <el-form-item v-if="dialogType === 'dynamic'" label="时间">
          <el-date-picker v-model="relationForm.timeDimension" type="month" format="YYYY-MM" value-format="YYYY-MM" placeholder="选择时间" class="full-width" />
        </el-form-item>
        <el-form-item v-if="dialogType === 'dynamic'" label="活动级别">
          <el-select v-model="relationForm.activityLevel" placeholder="选择级别" class="full-width">
            <el-option v-for="level in activityLevels" :key="level" :label="level" :value="level" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="dialogType === 'dynamic'" label="奖项等级">
          <el-select v-model="relationForm.awardLevel" placeholder="选择奖项" class="full-width">
            <el-option v-for="level in awardLevels" :key="level" :label="level" :value="level" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRelation">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, nextTick, computed } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import request from '@/api/index'

const activeTab = ref('graph')
const selectedStudent = ref('')
const students = ref([])
const quadsData = ref([])
const activityStats = reactive({ total: 0, byRelation: {}, byLevel: {}, byYear: {} })

const graphContainerRef = ref(null)
const relationChartRef = ref(null)
const levelChartRef = ref(null)
const yearChartRef = ref(null)

let graphChart = null
let relationChart = null
let levelChart = null
let yearChart = null

const dialogVisible = ref(false)
const dialogType = ref('static')
const relationForm = reactive({
  studentNo: '',
  studentName: '',
  relation: '',
  subRelation: '',
  tailName: '',
  timeDimension: '',
  activityLevel: '',
  awardLevel: ''
})

const relationsConfig = ref({
  staticRelations: [],
  dynamicRelations: [],
  activityLevels: [],
  awardLevels: []
})

const availableRelations = computed(() => {
  return dialogType.value === 'static' ? relationsConfig.value.staticRelations : relationsConfig.value.dynamicRelations
})

const currentSubCategories = computed(() => {
  const selected = relationsConfig.value.dynamicRelations.find(r => r.name === relationForm.relation)
  return selected?.subCategories || []
})

const activityLevels = ['院级', '校级', '省部级', '国家级', '国际级']
const awardLevels = ['特等奖', '一等奖', '二等奖', '三等奖', '优秀奖', '参与奖']

// 加载学生列表
const loadStudents = async () => {
  try {
    const res = await request.get('/student/list')
    if (res.code === 200 && res.data) {
      students.value = res.data.records || res.data || []
    }
  } catch (error) {
    console.error('加载学生列表失败:', error)
  }
}

// 加载关系配置
const loadRelationsConfig = async () => {
  try {
    const res = await request.get('/knowledge-graph/relations')
    if (res.code === 200) {
      relationsConfig.value = res.data
    }
  } catch (error) {
    console.error('加载关系配置失败:', error)
  }
}

// 加载学生知识图谱
const loadKnowledgeGraph = async (studentNo) => {
  if (!studentNo) return
  
  try {
    const graphRes = await request.get(`/knowledge-graph/student/${studentNo}`)
    if (graphRes.code === 200 && graphRes.data) {
      quadsData.value = graphRes.data.quads || []
      initGraphChart(graphRes.data)
    }
    
    const statsRes = await request.get(`/knowledge-graph/student/${studentNo}/stats`)
    if (statsRes.code === 200 && statsRes.data) {
      Object.assign(activityStats, statsRes.data)
      nextTick(() => initStatCharts())
    }
  } catch (error) {
    console.error('加载知识图谱失败:', error)
  }
}

// 初始化图谱图表
const initGraphChart = (data) => {
  if (!graphContainerRef.value) return
  
  if (!graphChart) {
    graphChart = echarts.init(graphContainerRef.value)
  }
  
  const nodes = []
  const links = []
  const categories = [
    { name: '学生' },
    { name: '院系/专业' },
    { name: '活动' },
    { name: '属性' }
  ]
  
  const studentName = data.staticRelations?.[0]?.headName || '学生'
  nodes.push({
    name: studentName,
    category: 0,
    symbolSize: 60,
    itemStyle: { color: '#409eff' }
  })
  
  const staticRelations = data.staticRelations || []
  staticRelations.forEach(kg => {
    nodes.push({
      name: kg.tailName,
      category: 1,
      symbolSize: 40,
      itemStyle: { color: '#67c23a' }
    })
    links.push({
      source: studentName,
      target: kg.tailName,
      label: { show: true, formatter: kg.relation },
      lineStyle: { color: '#909399', width: 2 }
    })
  })
  
  const dynamicRelations = data.dynamicRelations || []
  dynamicRelations.forEach(kg => {
    nodes.push({
      name: kg.tailName,
      category: 2,
      symbolSize: 35,
      itemStyle: { color: '#e6a23c' }
    })
    let relationLabel = kg.relation
    if (kg.subRelation) relationLabel += '_' + kg.subRelation
    if (kg.timeDimension) relationLabel += '\n' + kg.timeDimension
    
    links.push({
      source: studentName,
      target: kg.tailName,
      label: { show: true, formatter: relationLabel, fontSize: 10 },
      lineStyle: { color: '#f56c6c', width: 2 }
    })
  })
  
  graphChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: (params) => {
        if (params.dataType === 'node') {
          return params.data.name
        }
        return `${params.data.source} → ${params.data.target}<br/>关系: ${params.data.label?.formatter || ''}`
      }
    },
    legend: [{ data: categories.map(c => c.name), bottom: 10 }],
    series: [{
      type: 'graph',
      layout: 'force',
      symbolSize: 40,
      roam: true,
      draggable: true,
      categories: categories,
      label: { show: true, position: 'bottom', fontSize: 12 },
      edgeSymbol: ['circle', 'arrow'],
      edgeSymbolSize: [4, 10],
      data: nodes,
      links: links,
      lineStyle: { opacity: 0.6, width: 2, curveness: 0.1 },
      emphasis: { focus: 'adjacency', lineStyle: { width: 4 } },
      force: { repulsion: 200, edgeLength: [80, 200], layoutAnimation: true }
    }]
  })
}

// 初始化统计图表
const initStatCharts = () => {
  if (relationChartRef.value) {
    if (!relationChart) relationChart = echarts.init(relationChartRef.value)
    const byRelation = activityStats.byRelation || {}
    relationChart.setOption({
      tooltip: { trigger: 'item' },
      series: [{
        type: 'pie',
        radius: ['35%', '60%'],
        data: Object.entries(byRelation).map(([name, value]) => ({ name, value })),
        label: { fontSize: 11 }
      }]
    })
  }
  
  if (levelChartRef.value) {
    if (!levelChart) levelChart = echarts.init(levelChartRef.value)
    const byLevel = activityStats.byLevel || {}
    levelChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: Object.keys(byLevel) },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: Object.values(byLevel), itemStyle: { color: '#409eff' } }]
    })
  }
  
  if (yearChartRef.value) {
    if (!yearChart) yearChart = echarts.init(yearChartRef.value)
    const byYear = activityStats.byYear || {}
    const years = Object.keys(byYear).sort()
    yearChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: years },
      yAxis: { type: 'value' },
      series: [{
        type: 'line',
        data: years.map(y => byYear[y]),
        areaStyle: { color: 'rgba(64, 158, 255, 0.2)' },
        itemStyle: { color: '#409eff' }
      }]
    })
  }
}

const getHighLevelCount = () => {
  const byLevel = activityStats.byLevel || {}
  return (byLevel['国家级'] || 0) + (byLevel['国际级'] || 0)
}

const getRecentYearCount = () => {
  const byYear = activityStats.byYear || {}
  const currentYear = new Date().getFullYear().toString()
  return byYear[currentYear] || 0
}

const onStudentChange = (studentNo) => {
  loadKnowledgeGraph(studentNo)
}

const openAddDialog = (type) => {
  if (!selectedStudent.value) {
    ElMessage.warning('请先选择学生')
    return
  }
  dialogType.value = type
  relationForm.studentNo = selectedStudent.value
  relationForm.studentName = students.value.find(s => s.no === selectedStudent.value)?.name || ''
  dialogVisible.value = true
}

const submitRelation = async () => {
  if (!relationForm.relation || !relationForm.tailName) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  const url = dialogType.value === 'static' ? '/knowledge-graph/static-relation' : '/knowledge-graph/dynamic-relation'
  try {
    const res = await request.post(url, relationForm)
    if (res.code === 200) {
      ElMessage.success('添加成功')
      dialogVisible.value = false
      loadKnowledgeGraph(selectedStudent.value)
    } else {
      ElMessage.error(res.message || '添加失败')
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

watch(activeTab, () => {
  nextTick(() => {
    graphChart?.resize()
    relationChart?.resize()
    levelChart?.resize()
    yearChart?.resize()
  })
})

onMounted(() => {
  loadStudents()
  loadRelationsConfig()
  window.addEventListener('resize', () => {
    graphChart?.resize()
    relationChart?.resize()
    levelChart?.resize()
    yearChart?.resize()
  })
})
</script>

<style scoped lang="scss">
.knowledge-graph-container {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .header-actions {
    display: flex;
    gap: 12px;
    
    .student-select {
      width: 250px;
    }
  }
}

.full-width {
  width: 100%;
}

.graph-container {
  width: 100%;
  height: 500px;
}

.chart-wrapper {
  width: 100%;
  height: 300px;
}

.stat-card {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
  
  h4 {
    margin: 0 0 16px 0;
    font-size: 16px;
    color: #303133;
  }
}

.summary-stats {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px;
  
  .stat-item {
    display: flex;
    justify-content: space-between;
    font-size: 16px;
    
    .label {
      color: #606266;
    }
    
    .value {
      font-weight: bold;
      color: #409eff;
      font-size: 20px;
    }
  }
}
</style>
