// 统一模拟数据源 - 基于真实学号数据
// 学号格式: 年份(4位) + 学院代码(2位) + 专业代码(2位) + 班级代码(2位) + 序号(3位)

// 学院代码映射 - 使用电子科技大学真实学院名称
const collegeCodeMap = {
  '01': '计算机科学与工程学院（网络空间安全学院）',
  '02': '信息与软件工程学院（示范性软件学院）',
  '03': '信息与通信工程学院',
  '04': '电子科学与工程学院',
  '05': '机械与电气工程学院',
  '06': '自动化工程学院',
  '07': '材料与能源学院',
  '08': '光电科学与工程学院',
  '09': '经济与管理学院',
  '10': '公共管理学院',
  '11': '外国语学院',
  '12': '格拉斯哥学院',
  '13': '数学科学学院',
  '14': '物理学院',
  '15': '生命科学与技术学院',
  '16': '医学院',
  '17': '集成电路科学与工程学院（示范性微电子学院）',
  '19': '航空航天学院',
  '20': '资源与环境学院',
  '21': '英才实验学院（未来技术学院）',
  '27': '公共管理学院',
  '31': '体育学院',
  '34': '外国语学院'
}

// 专业代码映射 - 使用电子科技大学真实专业名称
const majorCodeMap = {
  '01': { '01': '计算机科学与技术', '02': '网络空间安全', '03': '软件工程', '04': '数据科学与大数据技术', '05': '数字媒体技术' },
  '02': { '01': '软件工程', '02': '网络工程', '03': '信息安全' },
  '03': { '01': '通信工程', '02': '网络工程', '03': '信息工程' },
  '04': { '01': '电子科学与技术', '02': '电子信息工程', '03': '微电子科学与工程', '04': '电磁场与无线技术' },
  '05': { '01': '机械设计制造及其自动化', '02': '电气工程及其自动化', '03': '工业工程', '04': '机器人工程' },
  '06': { '01': '自动化', '02': '电气工程及其自动化', '03': '智能电网信息工程', '04': '测控技术与仪器' },
  '07': { '01': '材料科学与工程', '02': '新能源材料与器件', '03': '应用化学', '04': '光源与照明' },
  '08': { '01': '光电信息科学与工程', '02': '光源与照明', '03': '测控技术与仪器' },
  '09': { '01': '工商管理', '02': '金融学', '03': '电子商务', '04': '信息管理与信息系统' },
  '10': { '01': '行政管理', '02': '城市管理', '03': '法学' },
  '11': { '01': '英语', '02': '日语', '03': '法语', '04': '翻译' },
  '12': { '01': '电子信息类（中外合作办学）（通信工程）', '02': '电子信息类（中外合作办学）（电子信息）', '03': '电子信息类（中外合作办学）（微电子科学与工程）' },
  '13': { '01': '数学与应用数学', '02': '信息与计算科学', '03': '数据科学与大数据技术' },
  '14': { '01': '电子信息科学与技术', '02': '应用物理学', '03': '数理基础科学（"成电英才计划"拔尖创新人才实验班）' },
  '15': { '01': '生物技术', '02': '生物医学工程', '03': '数据科学与大数据技术' },
  '16': { '01': '临床医学', '02': '护理学', '03': '生物医学工程' },
  '17': { '01': '集成电路设计与集成系统', '02': '微电子科学与工程', '03': '电子科学与技术' },
  '19': { '01': '航空航天工程', '02': '飞行器控制与信息工程', '03': '无人驾驶航空器系统工程' },
  '20': { '01': '地球信息科学与技术', '02': '遥感科学与技术', '03': '空间信息与数字技术' },
  '21': { '01': '计算机科学与技术（"成电英才计划"拔尖创新人才实验班）', '02': '电子信息工程（"成电英才计划"拔尖创新人才实验班）' },
  '27': { '01': '行政管理', '02': '城市管理' },
  '31': { '01': '体育教育', '02': '社会体育指导与管理' },
  '34': { '01': '英语', '02': '日语' }
}

// 班级代码映射
const classCodeMap = {
  '01': ['1班', '2班', '3班', '4班', '5班', '6班', '7班'],
  '02': ['1班', '2班', '3班', '4班'],
  '03': ['1班', '2班', '3班', '4班'],
  '04': ['1班', '2班', '3班', '4班'],
  '05': ['1班', '2班', '3班', '4班'],
  '06': ['1班', '2班', '3班', '4班'],
  '07': ['1班', '2班', '3班', '4班'],
  '08': ['1班', '2班', '3班'],
  '09': ['1班', '2班', '3班', '4班'],
  '10': ['1班', '2班', '3班'],
  '11': ['1班', '2班', '3班', '4班'],
  '12': ['1班', '2班', '3班'],
  '13': ['1班', '2班', '3班', '4班'],
  '14': ['1班', '2班', '3班', '4班'],
  '15': ['1班', '2班', '3班'],
  '16': ['1班', '2班', '3班'],
  '17': ['1班', '2班', '3班'],
  '19': ['1班', '2班', '3班'],
  '20': ['1班', '2班', '3班'],
  '21': ['1班', '2班'],
  '27': ['1班', '2班'],
  '31': ['1班', '2班'],
  '34': ['1班', '2班']
}

// 真实学号列表（从entities.txt提取，分布所有学院）
const realStudentNos = [
  '2020010906005', '2020010906012', '2020010906013', '2020010906020', '2020010906021',
  '2021020908012', '2021020905002', '2022020912026', '2023060904021', '2022150903020',
  '2021120101018', '2023090913025', '2023120902003', '2022010906026', '2022190506038',
  '2023050909001', '2021050909002', '2023010908004', '2023010909003', '2020120905015',
  '2022010915016', '2022010906003', '2023310201005', '2021060902008', '2023010902005',
  '2020190907008', '2020020907021', '2023120902010', '2020100402013', '2021020915005',
  '2023020910029', '2020050901003', '2023060904014', '2023190906004', '2020040903009',
  '2021340105001', '2020150903001', '2022070903022', '2022090901020', '2021080902020',
  '2023050905019', '2020110901004', '2022020910027', '2021190902016', '2020110903029',
  '2021040901006', '2023160902021', '2022030901021', '2023040902027', '2023090911014',
  '2021100901014', '2022020913010', '2020020914024', '2022090917009', '2023030903006',
  '2021080907020', '2021120903007', '2022060909001', '2023010901006', '2020060908025',
  '2020040901017', '2021140903001', '2021150903016', '2020050901015', '2023130101012',
  '2020020996004', '2021100903025', '2021080401015', '2023310203020', '2020010909022',
  '2021040906008', '2023010902003', '2021020914021', '2023080911010', '2021130101021',
  '2022110903024', '2022340101026', '2023090909006', '2023170201007', '2021130102018',
  '2023120101019', '2023060904023', '2020060904005', '2021010917001', '2021030902016',
  '2021140903003', '2023100301024', '2022130201017', '2022020907015', '2020110902006',
  '2021050904021', '2023060909003', '2023020904009', '2023120905012', '2020270903007',
  '2023050907002', '2020090901007', '2020140902022', '2023070901006', '2022091203024',
  '2021020911007', '2023150903002', '2020190908001', '2022010910008', '2022050901026',
  // 资源与环境学院学生 (college code 20)
  '2023200101001', '2023200101002', '2023200101003', '2023200102001', '2023200102002',
  '2023200103001', '2023200201001', '2023200201002', '2023200202001', '2023200301001',
  // 英才实验学院学生 (college code 21)
  '2023210101001', '2023210101002', '2023210101003', '2023210102001', '2023210102002',
  '2023210201001', '2023210201002', '2023210201003', '2023210202001', '2023210202002'
]

// 真实姓名列表
const studentNames = [
  '张伟', '李娜', '王丽', '刘洋', '陈静', '杨帆', '黄磊', '赵敏', '周涛', '吴芳',
  '徐强', '孙悦', '马超', '朱琳', '胡军', '郭倩', '何伟', '高飞', '林雪', '罗鹏',
  '吴凯', '郑云', '冯华', '许晴', '邓超', '谢军', '韩梅', '唐杰', '萧然', '曹鹏',
  '彭涛', '吕欣', '苏婷', '卢刚', '蒋伟', '蔡红', '丁明', '余芳', '梁磊', '魏强',
  '许静', '钱伟', '孙丽', '李军', '周杰', '吴娜', '郑鹏', '王婷', '刘刚', '陈雪',
  '杨军', '黄伟', '赵娜', '周强', '吴静', '徐军', '孙伟', '马丽', '朱强', '胡娜',
  '郭军', '何敏', '高娜', '林强', '罗静', '黄军', '杨伟', '张娜', '李强', '王静',
  '刘军', '陈伟', '徐娜', '孙强', '周静', '吴军', '郑伟', '马娜', '朱强', '胡静',
  '郭军', '何伟', '高娜', '林强', '罗静', '黄军', '杨伟', '张娜', '李强', '王静',
  '刘军', '陈伟', '徐娜', '孙强', '周静', '吴军', '郑伟', '马娜', '朱强', '胡静'
]

// 解析学号获取学生信息
const parseStudentNo = (studentNo) => {
  const year = studentNo.substring(0, 4)
  const collegeCode = studentNo.substring(4, 6)
  const majorCode = studentNo.substring(6, 8)
  const classCode = studentNo.substring(8, 10)

  const college = collegeCodeMap[collegeCode] || '计算机科学与工程学院（网络空间安全学院）'

  // 获取专业映射
  const majorMapping = majorCodeMap[collegeCode] || {}
  const major = majorMapping[majorCode] || '计算机科学与技术'

  // 获取班级
  const classMapping = classCodeMap[collegeCode] || ['1班']
  const classIndex = (parseInt(classCode) - 1) % classMapping.length
  const className = classMapping[classIndex] || '1班'

  return {
    enrollmentYear: parseInt(year),
    college,
    major,
    className
  }
}

// 生成唯一ID
let studentIdCounter = 1
const generateId = () => studentIdCounter++

// 创建学生对象
const createStudent = (studentNo, name, parsedInfo, _index) => ({
  id: generateId(),
  studentNo,
  name,
  gender: Math.random() > 0.5 ? 1 : 2,
  birthday: `${1998 + Math.floor(Math.random() * 6)}-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
  phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
  email: `${studentNo}@university.edu.cn`,
  college: parsedInfo.college,
  major: parsedInfo.major,
  className: parsedInfo.className,
  enrollmentYear: parsedInfo.enrollmentYear,
  studentStatus: 1,
  gpa: parseFloat((2.0 + Math.random() * 2.0).toFixed(2))
})

// 预定义的5个核心学生 - 使用电子科技大学真实学院专业名称
export const coreStudents = [
  {
    studentNo: '2020010906005',
    name: '张伟',
    gender: 1,
    college: '计算机科学与工程学院（网络空间安全学院）',
    major: '计算机科学与技术',
    className: '1班',
    enrollmentYear: 2020,
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
      { courseName: '数据结构与算法', semester: '2020-1', score: 95, knowledgeArea: '计算机基础', isChallenge: true },
      { courseName: '计算机网络', semester: '2020-2', score: 88, knowledgeArea: '系统基础', isChallenge: false },
      { courseName: '操作系统', semester: '2021-1', score: 92, knowledgeArea: '系统基础', isChallenge: true },
      { courseName: '数据库原理', semester: '2021-2', score: 90, knowledgeArea: '数据管理', isChallenge: false },
      { courseName: '人工智能导论', semester: '2022-1', score: 94, knowledgeArea: '人工智能', isChallenge: true }
    ],
    evidenceChain: [
      { evidenceId: 1, evidenceType: '竞赛获奖', relevance: 0.95, content: 'ACM-ICPC亚洲区域赛银奖', source: '竞赛管理系统' },
      { evidenceId: 2, evidenceType: '科研项目', relevance: 0.88, content: '参与导师AI科研项目', source: '科研管理系统' },
      { evidenceId: 3, evidenceType: '论文发表', relevance: 0.82, content: '在投SCI论文第一作者', source: '学术管理系统' }
    ],
    timeline: [
      { date: '2020-09', eventType: '学业', eventName: '入学教育', description: '参加新生入学教育活动' },
      { date: '2021-06', eventType: '竞赛', eventName: '程序设计竞赛', description: '获得校级一等奖' },
      { date: '2022-03', eventType: '科研', eventName: '加入实验室', description: '进入AI研究实验室' },
      { date: '2023-10', eventType: '竞赛', eventName: 'ACM竞赛', description: '获得亚洲区域赛银奖' }
    ],
    warnings: []
  },
  {
    studentNo: '2020010906012',
    name: '李娜',
    gender: 2,
    college: '计算机科学与工程学院（网络空间安全学院）',
    major: '软件工程',
    className: '1班',
    enrollmentYear: 2020,
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
      { courseName: '离散数学', semester: '2020-1', score: 82, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: '面向对象编程', semester: '2020-2', score: 85, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: '计算机组成原理', semester: '2021-1', score: 78, knowledgeArea: '系统基础', isChallenge: true }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: [{ id: 1, warningMessage: '平均绩点偏低', level: 1, suggestAction: '保持当前学习状态' }]
  },
  {
    studentNo: '2020010906013',
    name: '王丽',
    gender: 2,
    college: '信息与软件工程学院（示范性软件学院）',
    major: '软件工程',
    className: '1班',
    enrollmentYear: 2020,
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
      { courseName: '多媒体技术', semester: '2020-1', score: 88, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: '图形学基础', semester: '2020-2', score: 85, knowledgeArea: '计算机基础', isChallenge: false },
      { courseName: 'UI设计原理', semester: '2021-1', score: 90, knowledgeArea: '数据管理', isChallenge: false }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: []
  },
  {
    studentNo: '2020010906020',
    name: '刘洋',
    gender: 1,
    college: '电子科学与工程学院',
    major: '电子科学与技术',
    className: '1班',
    enrollmentYear: 2020,
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
      { courseName: '信号与系统', semester: '2020-1', score: 90, knowledgeArea: '计算机基础', isChallenge: true },
      { courseName: '数字信号处理', semester: '2020-2', score: 88, knowledgeArea: '系统基础', isChallenge: true },
      { courseName: '通信原理', semester: '2021-1', score: 92, knowledgeArea: '数据管理', isChallenge: false }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: []
  },
  {
    studentNo: '2020010906021',
    name: '陈静',
    gender: 2,
    college: '机械与电气工程学院',
    major: '机械设计制造及其自动化',
    className: '1班',
    enrollmentYear: 2020,
    studentStatus: 2,
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
      { courseName: '电路基础', semester: '2020-1', score: 68, knowledgeArea: '系统基础', isChallenge: false },
      { courseName: '模拟电子技术', semester: '2020-2', score: 62, knowledgeArea: '系统基础', isChallenge: true }
    ],
    evidenceChain: [],
    timeline: [],
    warnings: [{ id: 2, warningMessage: '多门课程不及格,学业困难', level: 2, suggestAction: '需要重点关注,建议约谈家长和导师' }]
  }
]

// 扩展为100个学生
export const allStudents = [
  ...coreStudents,
  ...Array.from({ length: 95 }, (_, i) => {
    const index = i + 5
    const studentNo = realStudentNos[index]
    const name = studentNames[index]
    const parsedInfo = parseStudentNo(studentNo)
    return createStudent(studentNo, name, parsedInfo, index)
  })
]

// 学业预警数据（分布不同学院） - 使用真实学院专业名称
export const allWarnings = [
  { id: 1, studentNo: '2020010906005', college: '计算机科学与工程学院（网络空间安全学院）', major: '计算机科学与技术', warningType: '成绩预警', level: 1, levelName: '一般', warningMessage: '数据结构与算法成绩偏低', createTime: '2024-03-15' },
  { id: 2, studentNo: '2020010906012', college: '计算机科学与工程学院（网络空间安全学院）', major: '软件工程', warningType: '学分预警', level: 2, levelName: '较重', warningMessage: '已获学分不足毕业要求', createTime: '2024-03-12' },
  { id: 3, studentNo: '2020010907002', college: '格拉斯哥学院', major: '电子信息类（中外合作办学）（通信工程）', warningType: '学业困难', level: 1, levelName: '一般', warningMessage: '部分课程学习困难', createTime: '2024-03-10' },
  { id: 4, studentNo: '2020010906021', college: '机械与电气工程学院', major: '机械设计制造及其自动化', warningType: '学业困难', level: 3, levelName: '严重', warningMessage: '多门课程不及格', createTime: '2024-03-08' },
  { id: 5, studentNo: '2020010910008', college: '经济与管理学院', major: '工商管理', warningType: '成绩预警', level: 1, levelName: '一般', warningMessage: '管理学原理成绩偏低', createTime: '2024-03-05' },
  { id: 6, studentNo: '2020010911005', college: '公共管理学院', major: '法学', warningType: '学分预警', level: 2, levelName: '较重', warningMessage: '必修课学分不足', createTime: '2024-03-01' },
  { id: 7, studentNo: '2020010912003', college: '光电科学与工程学院', major: '光电信息科学与工程', warningType: '毕业预警', level: 3, levelName: '严重', warningMessage: '毕业设计进度滞后', createTime: '2024-02-28' },
  { id: 8, studentNo: '2020010913014', college: '数学科学学院', major: '数学与应用数学', warningType: '成绩预警', level: 1, levelName: '一般', warningMessage: '高等代数成绩偏低', createTime: '2024-02-25' },
  { id: 9, studentNo: '2020010914004', college: '生命科学与技术学院', major: '生物技术', warningType: '学业困难', level: 2, levelName: '较重', warningMessage: '专业课程学习困难', createTime: '2024-02-20' }
]

// 活动数据（分布不同学院） - 使用真实学院名称
export const allActivities = [
  // 计算机科学与工程学院 - 张伟
  { id: 1, name: 'ACM-ICPC国际大学生程序设计竞赛', type: '科技创新', level: '国家级', time: '2023-05', description: '亚洲区域赛银奖', participants: ['2020010906005', '2020010906012'], award: '国家级_二等奖' },
  { id: 2, name: '全国大学生程序设计竞赛', type: '科技创新', level: '国家级', time: '2022-10', description: '获得一等奖', participants: ['2020010906005'], award: '国家级_一等奖' },
  { id: 3, name: '人工智能创新实验', type: '学术研究', level: '校级', time: '2023-03', description: '优秀项目', participants: ['2020010906005'], award: null },
  { id: 4, name: '暑期三下乡社会实践', type: '志愿服务', level: '院级', time: '2022-07', description: '支教活动', participants: ['2020010906005', '2020010906013'], award: null },
  // 计算机科学与工程学院 - 李娜
  { id: 5, name: '全国大学生数学建模竞赛', type: '学科竞赛', level: '省部级', time: '2023-09', description: '获得二等奖', participants: ['2020010906012'], award: '省部级_二等奖' },
  { id: 6, name: '区块链技术研究', type: '学术研究', level: '校级', time: '2023-06', description: '发表论文', participants: ['2020010906012'], award: null },
  // 经济与管理学院
  { id: 7, name: '全国大学生广告艺术大赛', type: '文化艺术', level: '国家级', time: '2023-08', description: '优秀作品奖', participants: ['2020010910008'], award: '国家级_优秀奖' },
  { id: 8, name: '互联网公司实习', type: '社会实践', level: '校级', time: '2023-07', description: '暑期实习', participants: ['2020010910008'], award: null },
  // 数学科学学院
  { id: 9, name: '全国大学生电子设计竞赛', type: '科技创新', level: '国家级', time: '2023-04', description: '获得一等奖', participants: ['2020010913014'], award: '国家级_一等奖' },
  { id: 10, name: '5G通信技术研究', type: '学术研究', level: '校级', time: '2023-01', description: '科研项目', participants: ['2020010913014'], award: null },
  // 更多跨学院活动
  { id: 11, name: '大学生创新创业大赛', type: '科技创新', level: '省部级', time: '2023-11', description: '创业项目', participants: ['2020010906005', '2020010906012', '2020010910008'], award: '省部级_铜奖' },
  { id: 12, name: '程序设计校赛', type: '科技创新', level: '校级', time: '2023-06', description: '个人赛', participants: ['2020010906005'], award: '校级_一等奖' },
  { id: 13, name: '数学竞赛', type: '学科竞赛', level: '校级', time: '2023-10', description: '校级选拔赛', participants: ['2020010906012', '2020010913014'], award: '校级_一等奖' },
  { id: 14, name: '校园篮球赛', type: '体育活动', level: '院级', time: '2023-04', description: '团体赛', participants: ['2020010914004', '2020010915001'], award: '院级_一等奖' },
  { id: 15, name: '暑期支教活动', type: '志愿服务', level: '校级', time: '2022-08', description: '爱心支教', participants: ['2020010906005', '2020010906013'], award: null },
  { id: 16, name: '网络安全竞赛', type: '科技创新', level: '国家级', time: '2024-03', description: '获得三等奖', participants: ['2020010906005'], award: '国家级_三等奖' },
  { id: 17, name: '英语演讲比赛', type: '学科竞赛', level: '校级', time: '2023-11', description: '二等奖', participants: ['2020010906012'], award: '校级_二等奖' },
  { id: 18, name: '市场营销大赛', type: '社会实践', level: '省部级', time: '2024-01', description: '获得三等奖', participants: ['2020010910008', '2020010910014'], award: '省部级_三等奖' },
  { id: 19, name: '建筑设计竞赛', type: '学术研究', level: '国家级', time: '2023-09', description: '获得优秀奖', participants: ['2020010912003'], award: '国家级_优秀奖' },
  { id: 20, name: '生物实验技能大赛', type: '学术研究', level: '省部级', time: '2023-05', description: '获得二等奖', participants: ['2020010914004'], award: '省部级_二等奖' }
]

// 活动推荐数据
export const activityRecommendations = [
  { id: 1, studentNo: '2020010906005', activity: '全国大学生机器人大赛', reason: '您的算法能力强,适合参加技术类竞赛', matchScore: 95 },
  { id: 2, studentNo: '2020010906005', activity: '人工智能挑战赛', reason: '与您的科研方向高度契合', matchScore: 92 },
  { id: 3, studentNo: '2020010906012', activity: '社会调研项目', reason: '您有丰富的社会实践经验', matchScore: 88 },
  { id: 4, studentNo: '2020010906012', activity: '创新创业训练计划', reason: '您具备团队协作和组织能力', matchScore: 85 },
  { id: 5, studentNo: '2020010910008', activity: '商业案例大赛', reason: '您的专业和创意非常匹配', matchScore: 94 },
  { id: 6, studentNo: '2020010910008', activity: '企业实习', reason: '您有相关实践经验', matchScore: 90 },
  { id: 7, studentNo: '2020010913014', activity: '数学建模竞赛', reason: '您的专业技能突出', matchScore: 96 },
  { id: 8, studentNo: '2020010913014', activity: '学术研讨会', reason: '与您的研究方向一致', matchScore: 93 },
  { id: 9, studentNo: '2020010906021', activity: '学业辅导班', reason: '建议加强基础知识学习', matchScore: 80 },
  { id: 10, studentNo: '2020010906021', activity: '一对一帮扶计划', reason: '需要重点关注学业', matchScore: 75 },
  { id: 11, studentNo: '2020010914004', activity: '生物实验技能大赛', reason: '您对生物技术有浓厚兴趣', matchScore: 91 },
  { id: 12, studentNo: '2020010912003', activity: '环境艺术设计展', reason: '与您的专业方向契合', matchScore: 89 }
]

// 知识图谱数据（基于学生关系） - 使用真实学院专业名称
export const knowledgeGraphData = {
  '2020010906005': {
    staticRelations: [
      { studentNo: '2020010906005', relation: '隶属院系', tailName: '计算机科学与工程学院（网络空间安全学院）' },
      { studentNo: '2020010906005', relation: '所属专业', tailName: '计算机科学与技术' },
      { studentNo: '2020010906005', relation: '所属班级', tailName: '1班' }
    ],
    dynamicRelations: [
      { studentNo: '2020010906005', relation: '比赛获奖', subRelation: '科技创新', tailName: 'ACM-ICPC国际大学生程序设计竞赛', timeDimension: '2023-05' },
      { studentNo: '2020010906005', relation: '比赛获奖', subRelation: '科技创新', tailName: '全国大学生程序设计竞赛', timeDimension: '2022-10' },
      { studentNo: '2020010906005', relation: '学术科研', subRelation: '科研项目', tailName: '人工智能创新实验', timeDimension: '2023-03' },
      { studentNo: '2020010906005', relation: '志愿服务', subRelation: '社会实践', tailName: '暑期三下乡社会实践', timeDimension: '2022-07' }
    ]
  },
  '2020010906012': {
    staticRelations: [
      { studentNo: '2020010906012', relation: '隶属院系', tailName: '计算机科学与工程学院（网络空间安全学院）' },
      { studentNo: '2020010906012', relation: '所属专业', tailName: '软件工程' },
      { studentNo: '2020010906012', relation: '所属班级', tailName: '1班' }
    ],
    dynamicRelations: [
      { studentNo: '2020010906012', relation: '比赛获奖', subRelation: '科技创新', tailName: 'ACM-ICPC国际大学生程序设计竞赛', timeDimension: '2023-05' },
      { studentNo: '2020010906012', relation: '比赛获奖', subRelation: '学科竞赛', tailName: '全国大学生数学建模竞赛', timeDimension: '2023-09' },
      { studentNo: '2020010906012', relation: '学术科研', subRelation: '学术研究', tailName: '区块链技术研究', timeDimension: '2023-06' }
    ]
  },
  '2020010906013': {
    staticRelations: [
      { studentNo: '2020010906013', relation: '隶属院系', tailName: '信息与软件工程学院（示范性软件学院）' },
      { studentNo: '2020010906013', relation: '所属专业', tailName: '软件工程' },
      { studentNo: '2020010906013', relation: '所属班级', tailName: '1班' }
    ],
    dynamicRelations: [
      { studentNo: '2020010906013', relation: '比赛获奖', subRelation: '文化艺术', tailName: '全国大学生广告艺术大赛', timeDimension: '2023-08' },
      { studentNo: '2020010906013', relation: '社会实践', subRelation: '社会实践', tailName: '互联网公司实习', timeDimension: '2023-07' },
      { studentNo: '2020010906013', relation: '志愿服务', subRelation: '社会实践', tailName: '暑期三下乡社会实践', timeDimension: '2022-07' }
    ]
  },
  '2020010906020': {
    staticRelations: [
      { studentNo: '2020010906020', relation: '隶属院系', tailName: '电子科学与工程学院' },
      { studentNo: '2020010906020', relation: '所属专业', tailName: '电子科学与技术' },
      { studentNo: '2020010906020', relation: '所属班级', tailName: '1班' }
    ],
    dynamicRelations: [
      { studentNo: '2020010906020', relation: '比赛获奖', subRelation: '科技创新', tailName: '全国大学生电子设计竞赛', timeDimension: '2023-04' },
      { studentNo: '2020010906020', relation: '比赛获奖', subRelation: '学科竞赛', tailName: '数学竞赛', timeDimension: '2023-10' },
      { studentNo: '2020010906020', relation: '学术科研', subRelation: '学术研究', tailName: '5G通信技术研究', timeDimension: '2023-01' }
    ]
  },
  '2020010906021': {
    staticRelations: [
      { studentNo: '2020010906021', relation: '隶属院系', tailName: '机械与电气工程学院' },
      { studentNo: '2020010906021', relation: '所属专业', tailName: '机械设计制造及其自动化' },
      { studentNo: '2020010906021', relation: '所属班级', tailName: '1班' }
    ],
    dynamicRelations: [
      { studentNo: '2020010906021', relation: '比赛获奖', subRelation: '科技创新', tailName: '程序设计校赛', timeDimension: '2023-06' }
    ]
  }
}

// 仪表盘统计数据
export const dashboardStats = {
  totalStudents: allStudents.length,
  activeStudents: allStudents.filter(s => s.studentStatus === 1).length,
  warningCount: allWarnings.length,
  avgGpa: parseFloat((allStudents.reduce((sum, s) => sum + s.gpa, 0) / allStudents.length).toFixed(2))
}

// 相似学生数据
export const similarStudents = {
  '2020010906005': [
    { studentNo: '2020010906013', profileType: '复合探索型', gpa: 3.42, similarity: 0.75 },
    { studentNo: '2020010906020', profileType: '学术研究型', gpa: 3.65, similarity: 0.68 }
  ],
  '2020010906012': [
    { studentNo: '2020010906005', profileType: '学术研究型', gpa: 3.85, similarity: 0.72 },
    { studentNo: '2020010906020', profileType: '学术研究型', gpa: 3.65, similarity: 0.65 }
  ]
}

// 学院统计数据 - 使用电子科技大学真实学院名称
export const colleges = [
  '机械与电气工程学院',
  '物理学院',
  '计算机科学与工程学院（网络空间安全学院）',
  '电子科学与工程学院',
  '材料与能源学院',
  '自动化工程学院',
  '数学科学学院',
  '经济与管理学院',
  '格拉斯哥学院',
  '集成电路科学与工程学院（示范性微电子学院）',
  '公共管理学院',
  '光电科学与工程学院',
  '资源与环境学院',
  '信息与软件工程学院（示范性软件学院）',
  '航空航天学院',
  '生命科学与技术学院',
  '外国语学院',
  '英才实验学院（未来技术学院）',
  '医学院',
  '信息与通信工程学院',
  '体育学院'
]
export const collegeStats = colleges.map(college => ({
  name: college,
  value: allStudents.filter(s => s.college === college).length
}))

// 专业统计数据 - 使用电子科技大学真实专业名称
export const majorMapping = {
  '机械与电气工程学院': ['机械设计制造及其自动化', '电气工程及其自动化', '工业工程', '机器人工程', '测控技术与仪器', '光电信息科学与工程'],
  '物理学院': ['电子信息科学与技术', '应用物理学', '数理基础科学（"成电英才计划"拔尖创新人才实验班）'],
  '计算机科学与工程学院（网络空间安全学院）': ['计算机科学与技术', '网络空间安全', '软件工程', '数据科学与大数据技术', '数字媒体技术', '物联网工程'],
  '电子科学与工程学院': ['电子科学与技术', '电子信息工程', '微电子科学与工程', '电磁场与无线技术', '电波传播与天线'],
  '材料与能源学院': ['材料科学与工程', '新能源材料与器件', '应用化学', '光源与照明'],
  '自动化工程学院': ['自动化', '电气工程及其自动化', '信息工程', '智能电网信息工程', '机器人工程'],
  '数学科学学院': ['数学与应用数学', '信息与计算科学', '数据科学与大数据技术'],
  '经济与管理学院': ['工商管理', '金融学', '电子商务', '信息管理与信息系统', '城市管理'],
  '格拉斯哥学院': ['电子信息类（中外合作办学）（通信工程）', '电子信息类（中外合作办学）（电子信息）', '电子信息类（中外合作办学）（微电子科学与工程）'],
  '集成电路科学与工程学院（示范性微电子学院）': ['集成电路设计与集成系统', '微电子科学与工程', '电子科学与技术'],
  '公共管理学院': ['行政管理', '城市管理', '法学'],
  '光电科学与工程学院': ['光电信息科学与工程', '光源与照明', '测控技术与仪器'],
  '资源与环境学院': ['地球信息科学与技术', '环境科学', '遥感科学与技术', '空间信息与数字技术'],
  '信息与软件工程学院（示范性软件学院）': ['软件工程', '网络工程', '信息安全'],
  '航空航天学院': ['航空航天工程', '飞行器控制与信息工程', '无人驾驶航空器系统工程', '探测制导与控制技术'],
  '生命科学与技术学院': ['生物技术', '生物医学工程', '数据科学与大数据技术'],
  '外国语学院': ['英语', '日语', '法语', '翻译', '外国语言文学类'],
  '英才实验学院（未来技术学院）': ['计算机科学与技术（"成电英才计划"拔尖创新人才实验班）', '电子信息工程（"成电英才计划"拔尖创新人才实验班）', '数学与应用数学（"成电英才计划"拔尖创新人才实验班）'],
  '医学院': ['临床医学', '护理学', '生物医学工程'],
  '信息与通信工程学院': ['通信工程', '网络工程', '信息工程'],
  '体育学院': ['体育教育', '社会体育指导与管理']
}

export const majorStats = Object.entries(majorMapping).flatMap(([_college, majors]) =>
  majors.map(major => ({
    name: major,
    value: allStudents.filter(s => s.major === major).length
  }))
)

// 用户账号数据 - 使用真实学院专业名称
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
    department: '计算机科学与工程学院（网络空间安全学院）',
    position: '2020级辅导员'
  },
  {
    username: 'student',
    password: 'student123',
    role: 'student',
    roleName: '普通学生',
    name: '张伟',
    employeeNo: '20240004',
    studentNo: '2020010906005',
    college: '计算机科学与工程学院（网络空间安全学院）',
    major: '计算机科学与技术',
    className: '1班'
  }
]

// 学生学号映射表（用于快速查找）
export const studentMap = new Map(allStudents.map(s => [s.studentNo, s]))

// 根据学号获取学生信息
export const getStudentByNo = (studentNo) => studentMap.get(studentNo) || null
