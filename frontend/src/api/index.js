// API 配置
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8088/api'

const apiRequest = async (url, options = {}) => {
  const fullUrl = `${BASE_URL}${url}`

  const defaultOptions = {
    headers: {
      'Content-Type': 'application/json',
    },
  }

  const response = await fetch(fullUrl, { ...defaultOptions, ...options })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  const data = await response.json()
  return data
}

// 学生 API
export const studentApi = {
  getAll: () => apiRequest('/students'),
  getByNo: (studentNo) => apiRequest(`/students/${studentNo}`),
  search: (params) => {
    const query = new URLSearchParams(params).toString()
    return apiRequest(`/students/search?${query}`)
  },
  getByCollege: (college) => apiRequest(`/students/college/${encodeURIComponent(college)}`),
  getByProfileType: (profileType) => apiRequest(`/students/profile-type/${profileType}`),
  create: (data) => apiRequest('/students', { method: 'POST', body: JSON.stringify(data) }),
  update: (studentNo, data) => apiRequest(`/students/${studentNo}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (studentNo) => apiRequest(`/students/${studentNo}`, { method: 'DELETE' }),
  getActiveCount: () => apiRequest('/students/stats/active'),
}

// 活动 API
export const activityApi = {
  getAll: () => apiRequest('/activities'),
  getById: (id) => apiRequest(`/activities/${id}`),
  getByType: (type) => apiRequest(`/activities/type/${type}`),
  getByLevel: (level) => apiRequest(`/activities/level/${level}`),
  getByStudent: (studentNo) => apiRequest(`/activities/student/${studentNo}`),
  search: (keyword) => apiRequest(`/activities/search?keyword=${encodeURIComponent(keyword)}`),
  create: (data) => apiRequest('/activities', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => apiRequest(`/activities/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => apiRequest(`/activities/${id}`, { method: 'DELETE' }),
}

// 预警 API
export const warningApi = {
  getAll: () => apiRequest('/warnings'),
  getByStudent: (studentNo) => apiRequest(`/warnings/student/${studentNo}`),
  getByCollege: (college) => apiRequest(`/warnings/college/${encodeURIComponent(college)}`),
  getByType: (type) => apiRequest(`/warnings/type/${type}`),
  getByLevel: (level) => apiRequest(`/warnings/level/${level}`),
  create: (data) => apiRequest('/warnings', { method: 'POST', body: JSON.stringify(data) }),
  delete: (id) => apiRequest(`/warnings/${id}`, { method: 'DELETE' }),
}

// 画像 API
export const profileApi = {
  generate: (studentNo) => apiRequest('/profile/generate', { method: 'POST', body: JSON.stringify({ studentNo }) }),
}

// 知识图谱 API
export const graphApi = {
  getGlobal: () => apiRequest('/knowledge-graph/global'),
  getStudentGraph: (studentNo) => apiRequest(`/knowledge-graph/student/${studentNo}`),
}

// 仪表盘 API
export const dashboardApi = {
  getStats: () => apiRequest('/dashboard/stats'),
}

export default {
  student: studentApi,
  activity: activityApi,
  warning: warningApi,
  profile: profileApi,
  graph: graphApi,
  dashboard: dashboardApi,
}
