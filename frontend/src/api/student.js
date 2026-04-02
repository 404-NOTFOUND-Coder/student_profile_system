import request from './index'

// ==================== 学生档案API ====================

export const studentApi = {
  /**
   * 分页查询学生列表
   */
  list(params) {
    return request.get('/student/list', { params })
  },

  /**
   * 根据学号获取学生详情
   */
  getByStudentNo(studentNo) {
    return request.get(`/student/${studentNo}`)
  },

  /**
   * 创建学生档案
   */
  create(data) {
    return request.post('/student', data)
  },

  /**
   * 更新学生档案
   */
  update(id, data) {
    return request.put(`/student/${id}`, data)
  },

  /**
   * 删除学生档案
   */
  delete(id) {
    return request.delete(`/student/${id}`)
  },

  /**
   * 批量查询学生
   */
  batchGet(studentNos) {
    return request.post('/student/batch', studentNos)
  }
}

// ==================== 学生画像API ====================

export const profileApi = {
  /**
   * 获取学生完整画像
   */
  getProfile(studentNo) {
    return request.get(`/student/${studentNo}/profile`)
  },

  /**
   * 图游走查询
   */
  getGraphPath(studentNo, depth = 3) {
    return request.get(`/student/${studentNo}/graph/path`, {
      params: { depth }
    })
  },

  /**
   * 获取证据链
   */
  getEvidence(studentNo, topK = 10) {
    return request.get(`/student/${studentNo}/evidence`, {
      params: { topK }
    })
  },

  /**
   * 获取知识结构熵
   */
  getKnowledgeEntropy(studentNo) {
    return request.get(`/student/${studentNo}/knowledge-entropy`)
  },

  /**
   * 获取图编辑距离
   */
  getGraphEditDistance(studentNo) {
    return request.get(`/student/${studentNo}/graph-edit-distance`)
  },

  /**
   * 时序证据检索
   */
  getTemporalEvidence(studentNo, query, topK = 10, timeDecayFactor = 0.95) {
    return request.get(`/student/${studentNo}/temporal-evidence`, {
      params: { query, topK, timeDecayFactor }
    })
  },

  /**
   * 学业预警分析
   */
  academicWarning(studentNo) {
    return request.get(`/student/${studentNo}/warning`)
  },

  /**
   * 知识薄弱点诊断
   */
  diagnoseWeakKnowledge(studentNo) {
    return request.get(`/student/${studentNo}/weak-knowledge`)
  },

  /**
   * 相似学生分析
   */
  findSimilarStudents(studentNo, limit = 5) {
    return request.get(`/student/${studentNo}/similar`, {
      params: { limit }
    })
  },

  /**
   * 批量生成画像
   */
  batchGenerateProfiles(studentNos) {
    return request.post('/student/profile/batch', studentNos)
  }
}

// ==================== 统计API ====================

export const statsApi = {
  /**
   * 获取统计数据
   */
  getDashboardStats() {
    return request.get('/dashboard/stats')
  },

  /**
   * 获取预警列表
   */
  getWarningList(params) {
    return request.get('/dashboard/warnings', { params })
  },

  /**
   * 获取画像示例列表
   */
  getProfileExamples(params) {
    return request.get('/dashboard/profile-examples', { params })
  },

  /**
   * 获取学院统计数据
   */
  statsByCollege() {
    return request.get('/dashboard/college-stats')
  },

  /**
   * 获取专业统计数据
   */
  statsByMajor() {
    return request.get('/student/stats/major')
  }
}

export default {
  student: studentApi,
  profile: profileApi,
  stats: statsApi
}
