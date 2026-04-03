// 统一模拟数据源 - 所有模块共享此数据
// 确保数据联动一致性

// 学院和专业映射
export const colleges = ['计算机学院', '软件学院', '信息学院', '电子学院', '机械学院']

export const majorMapping = {
  '计算机学院': ['计算机科学与技术', '软件工程', '信息安全', '数据科学'],
  '软件学院': ['软件工程', '数字媒体技术', '物联网工程'],
  '信息学院': ['通信工程', '电子信息工程', '信息工程'],
  '电子学院': ['电子科学与技术', '微电子科学与工程'],
  '机械学院': ['机械设计制造', '机械电子工程', '材料成型']
}

// 姓氏列表
const surnames = ['张', '李', '王', '刘', '陈', '杨', '黄', '赵', '周', '吴', '徐', '孙', '马', '朱', '胡', '郭', '何', '高', '林', '罗']
const givenNames = ['明', '华', '伟', '强', '丽', '敏', '军', '杰', '涛', '鹏', '磊', '宇', '婷', '欣', '怡', '静', '雅', '文', '博', '昊']

// 生成唯一ID
let studentIdCounter = 1
const generateId = () => studentIdCounter++

// 创建学生对象
const createStudent = (studentNo, name, college, major, className, enrollmentYear) => ({
  id: generateId(),
  studentNo,
  name,
  gender: Math.random() > 0.5 ? 1 : 2,
  birthday: `${1998 + Math.floor(Math.random() * 6)}-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
  phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
  email: `${studentNo}@university.edu.cn`,
  college,
  major,
  className,
  enrollmentYear,
  studentStatus: 1, // 默认在读
  gpa: parseFloat((2.5 + Math.random() * 1.5).toFixed(2))
})

// 预定义的5个核心学生（用于画像、预警、图谱等详情展示）
export const coreStudents = [
  {
    studentNo: '2021001001',
    name: '张三',
    gender: 1, // 男
    college: '计算机学院',
    major: '软件工程',
    className: '软工1班',
    enrollmentYear: 2021,
    studentStatus: 1,
    gpa: 3.85,
    profileScore: 88,
    knowledgeEntropy: 8.5,
    graphEditDistance: 2.3,
    activityScore: 92,
    profileType: '学术研究型',
    abilityLevel: 'A',
    personality: '勤奋好学,逻辑思维强,创新意识高',
    academicTags: ['编程达人', '算法高手', '科研新星'],
    practiceTags: ['竞赛选手', '项目骨干', '技术分享者'],
    suggestionTags: ['加强实践经验', '提升团队协作', '关注前沿技术'],
    academicTrend: { trend: 'RISING', description: '学业表现稳步提升' },
    coursePerformance: [
      { courseName: '数据结构与算法', semester: '2021-1', score: 95, knowledgeArea: '计算机基础', isChallenge: true },
      { courseName: '计算机网络', semester: '2021-2', score: 88, knowledgeArea: '系统基础', isChallenge: false },
      { courseName: '操作系统', semester: '2022-1', score: 92, knowledgeArea: '系统基础', isChallenge: true },
      { courseName: '数据库原理', semester: '2022-2', score: 90, knowledgeArea: '数据管理', isChallenge: false },
      { courseName: '人工智能导论', semester: '2023-1', score: 94, knowledgeArea: '人工智能', isChallenge: true }
    ],
    evidenceChain: [
      { evidenceId: 1, evidenceType: '竞赛获奖', relevance: 0.95, content: 'ACM-ICPC亚洲区域赛银奖', source: '竞赛管理系统' },
      { evidenceId: 2, evidenceType: '科研项目', relevance: 0.88, content: '参与导师AI科研项目', source: '科研管理系统' },
      { evidenceId: 3, evidenceType: '论文发表', relevance: 0.82, content: '在投SCI论文第一作者', source: '学术管理系统' }
    ],
    timeline: [
      { date: '2021-09', eventType: '学业', eventName: '入学教育', description: '参加新生入学教育活动' },
      { date: '2022-06', eventType: '竞赛', eventName: '程序设计竞赛', description: '获得校级一等奖' },
      { date: '2023-03', eventType: '科研', eventName: '加入实验室', description: '进入AI研究实验室' },
      { date: '2023-10', eventType: '竞赛', eventName: 'ACM竞赛', description: '获得亚洲区域赛银奖' }
    ],
    warnings: []
  },
  {
    studentNo: '2021001002',
    name: '李四',
    gender: 2, // 女
    college: '计算机学院',
    major: '计算机科学与技术',
    className: '计科2班',
    enrollmentYear: 2021,
    studentStatus: 1,
    gpa: 3.12,
    profileScore: 75,
    knowledgeEntropy: 7.2,
    graphEditDistance: 3.5,
    activityScore: 68,
    profileType: '实践应用型',
    abilityLevel: 'B+',
    personality: '动手能力强,善于实践,团队协作好',
    academicTags: ['实践达人', '项目经验丰富'],
    practiceTags: ['学生干部', '志愿者标兵', '社团活跃分子'],
    suggestionTags: ['加强理论学习', '提升学术研究能力'],
    academicTrend: { trend: 'STABLE', description: '学业表现稳定' },
    coursePerformance: [
      { courseName: '离散数学', semester: '2021-1', score: 82, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: '面向对象编程', semester: '2021-2', score: 85, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: '计算机组成原理', semester: '2022-1', score: 78, knowledgeArea: '系统基础', isChallenge: true }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: [{ id: 1, warningMessage: '平均绩点偏低', level: 1, suggestAction: '保持当前学习状态' }]
  },
  {
    studentNo: '2021001003',
    name: '王五',
    gender: 1, // 男
    college: '软件学院',
    major: '数字媒体技术',
    className: '数媒1班',
    enrollmentYear: 2021,
    studentStatus: 1,
    gpa: 3.42,
    profileScore: 78,
    knowledgeEntropy: 7.8,
    graphEditDistance: 2.8,
    activityScore: 85,
    profileType: '复合探索型',
    abilityLevel: 'B+',
    personality: '兴趣广泛,创意无限,审美能力强',
    academicTags: ['设计达人', '创意先锋'],
    practiceTags: ['社会实践', '艺术活动', '新媒体运营'],
    suggestionTags: ['深化专业技能', '注重成果积累'],
    academicTrend: { trend: 'RISING', description: '学业表现稳中有升' },
    coursePerformance: [
      { courseName: '多媒体技术', semester: '2021-1', score: 88, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: '图形学基础', semester: '2021-2', score: 85, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: 'UI设计原理', semester: '2022-1', score: 90, knowledgeArea: '数据管理', isChallenge: false }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: []
  },
  {
    studentNo: '2021001004',
    name: '赵六',
    gender: 2, // 女
    college: '信息学院',
    major: '通信工程',
    className: '通信1班',
    enrollmentYear: 2021,
    studentStatus: 1,
    gpa: 3.65,
    profileScore: 82,
    knowledgeEntropy: 7.5,
    graphEditDistance: 2.5,
    activityScore: 78,
    profileType: '学术研究型',
    abilityLevel: 'A-',
    personality: '认真踏实,科研潜力大,数理基础扎实',
    academicTags: ['学术新星', '竞赛选手'],
    practiceTags: ['科研项目', '学科竞赛'],
    suggestionTags: ['拓宽国际视野', '加强交流合作'],
    academicTrend: { trend: 'RISING', description: '学业表现优秀且持续进步' },
    coursePerformance: [
      { courseName: '信号与系统', semester: '2021-1', score: 90, knowledgeArea: '计算机基础', isChallenge: true },
      { courseName: '数字信号处理', semester: '2021-2', score: 88, knowledgeArea: '系统基础', isChallenge: true },
      { courseName: '通信原理', semester: '2022-1', score: 92, knowledgeArea: '数据管理', isChallenge: false }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: []
  },
  {
    studentNo: '2021001005',
    name: '钱七',
    gender: 1, // 男
    college: '电子学院',
    major: '电子科学与技术',
    className: '电子1班',
    enrollmentYear: 2021,
    studentStatus: 2, // 休学
    gpa: 2.85,
    profileScore: 65,
    knowledgeEntropy: 6.5,
    graphEditDistance: 4.2,
    activityScore: 55,
    profileType: '待分析',
    abilityLevel: 'C',
    personality: '需要关注,学业困难',
    academicTags: ['学业困难'],
    practiceTags: [],
    suggestionTags: ['寻求帮助', '制定学习计划'],
    academicTrend: { trend: 'DECLINING', description: '学业表现下滑' },
    coursePerformance: [
      { courseName: '电路基础', semester: '2021-1', score: 68, knowledgeArea: '系统基础', isChallenge: false },
      { courseName: '模拟电子技术', semester: '2021-2', score: 62, knowledgeArea: '系统基础', isChallenge: true }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: [{ id: 2, warningMessage: '多门课程不及格,学业困难', level: 2, suggestAction: '需要重点关注,建议约谈家长和导师' }]
  }
]

// 扩展为100个学生（包含5个核心学生）
export const allStudents = [
  ...coreStudents,
  ...Array.from({ length: 95 }, (_, i) => {
    const idx = i + 6
    const college = colleges[Math.floor(Math.random() * colleges.length)]
    const majorList = majorMapping[college]
    const major = majorList[Math.floor(Math.random() * majorList.length)]
    const enrollmentYear = 2020 + Math.floor(Math.random() * 5)
    const studentNo = `${enrollmentYear}${String(idx).padStart(4, '0')}`
    const name = surnames[Math.floor(Math.random() * surnames.length)] + givenNames[Math.floor(Math.random() * givenNames.length)]
    const className = `${major.slice(0, 2)}软工${Math.floor(Math.random() * 4) + 1}班`

    return createStudent(studentNo, name, college, major, className, enrollmentYear)
  })
]

// 学业预警数据（基于学生数据联动）
export const allWarnings = [
  // 从核心学生的warnings中提取，并补充更多预警
  { id: 1, studentNo: '2021001001', studentName: '张三', college: '计算机学院', major: '软件工程', warningType: '成绩预警', level: 1, levelName: '一般', warningMessage: '数据结构与算法成绩偏低', createTime: '2024-03-15' },
  { id: 2, studentNo: '2021001002', studentName: '李四', college: '计算机学院', major: '计算机科学与技术', warningType: '学分预警', level: 2, levelName: '较重', warningMessage: '已获学分不足毕业要求', createTime: '2024-03-12' },
  { id: 3, studentNo: '2021001003', studentName: '王五', college: '软件学院', major: '数字媒体技术', warningType: '学业困难', level: 1, levelName: '一般', warningMessage: '部分课程学习困难', createTime: '2024-03-10' },
  { id: 4, studentNo: '2021001005', studentName: '钱七', college: '电子学院', major: '电子科学与技术', warningType: '学业困难', level: 3, levelName: '严重', warningMessage: '多门课程不及格', createTime: '2024-03-08' },
  { id: 5, studentNo: '2021001006', studentName: '周八', college: '信息学院', major: '通信工程', warningType: '成绩预警', level: 1, levelName: '一般', warningMessage: '高等数学成绩偏低', createTime: '2024-03-05' },
  { id: 6, studentNo: '2021001007', studentName: '吴九', college: '软件学院', major: '软件工程', warningType: '学分预警', level: 2, levelName: '较重', warningMessage: '必修课学分不足', createTime: '2024-03-01' },
  { id: 7, studentNo: '2021001008', studentName: '郑十', college: '机械学院', major: '机械设计制造', warningType: '毕业预警', level: 3, levelName: '严重', warningMessage: '毕业设计进度滞后', createTime: '2024-02-28' }
]

// 活动数据（与学生联动）
export const allActivities = [
  // 张三参与的活动
  { id: 1, name: 'ACM-ICPC国际大学生程序设计竞赛', type: '学科竞赛', level: '国家级', time: '2023-05', description: '亚洲区域赛银奖', participants: ['2021001001', '2021001002'], award: '银奖' },
  { id: 2, name: '全国大学生程序设计竞赛', type: '学科竞赛', level: '国家级', time: '2022-10', description: '获得一等奖', participants: ['2021001001'], award: '一等奖' },
  { id: 3, name: '人工智能创新实验', type: '学术科研', level: '校级', time: '2023-03', description: '优秀项目', participants: ['2021001001'], award: null },
  { id: 4, name: '暑期三下乡社会实践', type: '志愿服务', level: '院级', time: '2022-07', description: '支教活动', participants: ['2021001001', '2021001003'], award: null },
  // 李四参与的活动
  { id: 5, name: '全国大学生数学建模竞赛', type: '学科竞赛', level: '省部级', time: '2023-09', description: '获得二等奖', participants: ['2021001002'], award: '二等奖' },
  { id: 6, name: '区块链技术研究', type: '学术科研', level: '校级', time: '2023-06', description: '发表论文', participants: ['2021001002'], award: null },
  // 王五参与的活动
  { id: 7, name: '全国大学生广告艺术大赛', type: '学科竞赛', level: '国家级', time: '2023-08', description: '优秀作品奖', participants: ['2021001003'], award: '优秀奖' },
  { id: 8, name: '互联网公司实习', type: '社会实践', level: '校级', time: '2023-07', description: '暑期实习', participants: ['2021001003'], award: null },
  // 赵六参与的活动
  { id: 9, name: '全国大学生电子设计竞赛', type: '学科竞赛', level: '国家级', time: '2023-04', description: '获得一等奖', participants: ['2021001004'], award: '一等奖' },
  { id: 10, name: '5G通信技术研究', type: '学术科研', level: '校级', time: '2023-01', description: '科研项目', participants: ['2021001004'], award: null },
  { id: 11, name: '北京冬奥会志愿者', type: '志愿服务', level: '国家级', time: '2022-02', description: '赛事服务', participants: ['2021001004'], award: null },
  // 更多活动
  { id: 12, name: '大学生创新创业大赛', type: '创新创业', level: '省部级', time: '2023-11', description: '创业项目', participants: ['2021001001', '2021001002', '2021001003'], award: '铜奖' },
  { id: 13, name: '程序设计校赛', type: '学科竞赛', level: '校级', time: '2023-06', description: '个人赛', participants: ['2021001001', '2021001005'], award: '一等奖' },
  { id: 14, name: '机器人竞赛', type: '学科竞赛', level: '省部级', time: '2023-05', description: '团队赛', participants: ['2021001004'], award: '二等奖' },
  { id: 15, name: '数学竞赛', type: '学科竞赛', level: '校级', time: '2023-10', description: '校级选拔赛', participants: ['2021001002', '2021001004'], award: '一等奖' }
]

// 活动推荐数据
export const activityRecommendations = [
  { id: 1, studentNo: '2021001001', studentName: '张三', activity: '全国大学生机器人大赛', reason: '您的算法能力强,适合参加技术类竞赛', matchScore: 95 },
  { id: 2, studentNo: '2021001001', studentName: '张三', activity: '人工智能挑战赛', reason: '与您的科研方向高度契合', matchScore: 92 },
  { id: 3, studentNo: '2021001002', studentName: '李四', activity: '社会调研项目', reason: '您有丰富的社会实践经验', matchScore: 88 },
  { id: 4, studentNo: '2021001002', studentName: '李四', activity: '创新创业训练计划', reason: '您具备团队协作和组织能力', matchScore: 85 },
  { id: 5, studentNo: '2021001003', studentName: '王五', activity: '数字媒体创意大赛', reason: '您的专业和创意非常匹配', matchScore: 94 },
  { id: 6, studentNo: '2021001003', studentName: '王五', activity: '新媒体运营实习', reason: '您有相关实践经验', matchScore: 90 },
  { id: 7, studentNo: '2021001004', studentName: '赵六', activity: '电子设计竞赛', reason: '您的专业技能突出', matchScore: 96 },
  { id: 8, studentNo: '2021001004', studentName: '赵六', activity: '通信技术研讨会', reason: '与您的研究方向一致', matchScore: 93 },
  { id: 9, studentNo: '2021001005', studentName: '钱七', activity: '学业辅导班', reason: '建议加强基础知识学习', matchScore: 80 },
  { id: 10, studentNo: '2021001005', studentName: '钱七', activity: '一对一帮扶计划', reason: '需要重点关注学业', matchScore: 75 }
]

// 知识图谱数据（基于学生关系）
export const knowledgeGraphData = {
  '2021001001': {
    staticRelations: [
      { headName: '张三', relation: '隶属院系', tailName: '计算机学院' },
      { headName: '张三', relation: '所属专业', tailName: '软件工程' },
      { headName: '张三', relation: '所属班级', tailName: '软工1班' }
    ],
    dynamicRelations: [
      { relation: '比赛获奖', subRelation: '学科竞赛', tailName: '全国大学生程序设计竞赛', timeDimension: '2022-10' },
      { relation: '比赛获奖', subRelation: '科技创新', tailName: 'ACM-ICPC国际大学生程序设计竞赛', timeDimension: '2023-05' },
      { relation: '学术科研', subRelation: '科研项目', tailName: '人工智能创新实验', timeDimension: '2023-03' },
      { relation: '志愿服务', subRelation: '支教活动', tailName: '暑期三下乡社会实践', timeDimension: '2022-07' }
    ]
  },
  '2021001002': {
    staticRelations: [
      { headName: '李四', relation: '隶属院系', tailName: '计算机学院' },
      { headName: '李四', relation: '所属专业', tailName: '计算机科学与技术' },
      { headName: '李四', relation: '所属班级', tailName: '计科2班' }
    ],
    dynamicRelations: [
      { relation: '比赛获奖', subRelation: '学科竞赛', tailName: 'ACM-ICPC国际大学生程序设计竞赛', timeDimension: '2023-05' },
      { relation: '比赛获奖', subRelation: '学科竞赛', tailName: '全国大学生数学建模竞赛', timeDimension: '2023-09' },
      { relation: '学术科研', subRelation: '论文发表', tailName: '区块链技术研究', timeDimension: '2023-06' }
    ]
  },
  '2021001003': {
    staticRelations: [
      { headName: '王五', relation: '隶属院系', tailName: '软件学院' },
      { headName: '王五', relation: '所属专业', tailName: '数字媒体技术' },
      { headName: '王五', relation: '所属班级', tailName: '数媒1班' }
    ],
    dynamicRelations: [
      { relation: '比赛获奖', subRelation: '文体艺术', tailName: '全国大学生广告艺术大赛', timeDimension: '2023-08' },
      { relation: '社会实践', subRelation: '企业实习', tailName: '互联网公司实习', timeDimension: '2023-07' },
      { relation: '志愿服务', subRelation: '支教活动', tailName: '暑期三下乡社会实践', timeDimension: '2022-07' }
    ]
  },
  '2021001004': {
    staticRelations: [
      { headName: '赵六', relation: '隶属院系', tailName: '信息学院' },
      { headName: '赵六', relation: '所属专业', tailName: '通信工程' },
      { headName: '赵六', relation: '所属班级', tailName: '通信1班' }
    ],
    dynamicRelations: [
      { relation: '比赛获奖', subRelation: '科技创新', tailName: '全国大学生电子设计竞赛', timeDimension: '2023-04' },
      { relation: '比赛获奖', subRelation: '学科竞赛', tailName: '数学竞赛', timeDimension: '2023-10' },
      { relation: '学术科研', subRelation: '科研项目', tailName: '5G通信技术研究', timeDimension: '2023-01' },
      { relation: '志愿服务', subRelation: '赛事服务', tailName: '北京冬奥会志愿者', timeDimension: '2022-02' }
    ]
  },
  '2021001005': {
    staticRelations: [
      { headName: '钱七', relation: '隶属院系', tailName: '电子学院' },
      { headName: '钱七', relation: '所属专业', tailName: '电子科学与技术' },
      { headName: '钱七', relation: '所属班级', tailName: '电子1班' }
    ],
    dynamicRelations: [
      { relation: '比赛获奖', subRelation: '学科竞赛', tailName: '程序设计校赛', timeDimension: '2023-06' }
    ]
  }
}

// 仪表盘统计数据（基于学生数据计算）
export const dashboardStats = {
  totalStudents: allStudents.length,
  activeStudents: allStudents.filter(s => s.studentStatus === 1).length,
  warningCount: allWarnings.length,
  avgGpa: parseFloat((allStudents.reduce((sum, s) => sum + s.gpa, 0) / allStudents.length).toFixed(2))
}

// 相似学生数据
export const similarStudents = {
  '2021001001': [
    { studentNo: '2021001003', name: '王五', profileType: '复合探索型', gpa: 3.42, similarity: 0.75 },
    { studentNo: '2021001004', name: '赵六', profileType: '学术研究型', gpa: 3.65, similarity: 0.68 }
  ],
  '2021001002': [
    { studentNo: '2021001001', name: '张三', profileType: '学术研究型', gpa: 3.85, similarity: 0.72 },
    { studentNo: '2021001004', name: '赵六', profileType: '学术研究型', gpa: 3.65, similarity: 0.65 }
  ]
}

// 学院统计数据
export const collegeStats = colleges.map(college => ({
  name: college,
  value: allStudents.filter(s => s.college === college).length
}))

// 专业统计数据
export const majorStats = Object.entries(majorMapping).flatMap(([college, majors]) =>
  majors.map(major => ({
    name: major,
    value: allStudents.filter(s => s.major === major).length
  }))
)

// 用户账号数据
export const userAccounts = [
  {
    username: 'admin',
    password: 'admin123',
    role: 'admin',
    roleName: '系统管理员',
    name: '管理员',
    employeeNo: '20240001',
    department: '信息中心',
    position: '系统管理员'
  },
  {
    username: 'jiaowu',
    password: 'jiaowu123',
    role: 'teacher',
    roleName: '教务处老师',
    name: '张教务',
    employeeNo: '20240002',
    department: '教务处',
    position: '教务科科长'
  },
  {
    username: 'fudaoyuan',
    password: 'fudaoyuan123',
    role: 'counselor',
    roleName: '辅导员',
    name: '李辅导员',
    employeeNo: '20240003',
    department: '计算机学院',
    position: '2021级辅导员'
  },
  {
    username: 'student',
    password: 'student123',
    role: 'student',
    roleName: '普通学生',
    name: '张三',
    employeeNo: '20240004',
    studentNo: '2021001001',
    college: '计算机学院',
    major: '软件工程',
    className: '软工1班'
  }
]
