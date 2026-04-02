// =============================================
// 学生用户画像系统 - Neo4j图数据库初始化脚本
// =============================================
// 使用说明: 在Neo4j Browser中执行此脚本
// 连接方式: bolt://localhost:7687 (neo4j/neo4j123456)
// =============================================

// =============================================
// 第一部分：创建约束和索引（提升查询性能）
// =============================================

// 创建学生节点唯一约束
CREATE CONSTRAINT student_no_unique IF NOT EXISTS
FOR (s:Student) REQUIRE s.studentNo IS UNIQUE;

// 创建课程节点唯一约束
CREATE CONSTRAINT course_code_unique IF NOT EXISTS
FOR (c:Course) REQUIRE c.courseCode IS UNIQUE;

// 创建知识域节点唯一约束
CREATE CONSTRAINT knowledge_point_unique IF NOT EXISTS
FOR (k:Knowledge) REQUIRE k.knowledgePoint IS UNIQUE;

// 创建活动节点唯一约束
CREATE CONSTRAINT activity_id_unique IF NOT EXISTS
FOR (a:Activity) REQUIRE a.activityId IS UNIQUE;

// 创建奖项节点唯一约束
CREATE CONSTRAINT award_id_unique IF NOT EXISTS
FOR (aw:Award) REQUIRE aw.awardId IS UNIQUE;

// 创建索引
CREATE INDEX student_name_index IF NOT EXISTS FOR (s:Student) ON (s.name);
CREATE INDEX student_college_index IF NOT EXISTS FOR (s:Student) ON (s.college);
CREATE INDEX student_enrollment_year_index IF NOT EXISTS FOR (s:Student) ON (s.enrollmentYear);
CREATE INDEX course_name_index IF NOT EXISTS FOR (c:Course) ON (c.courseName);
CREATE INDEX knowledge_area_index IF NOT EXISTS FOR (k:Knowledge) ON (k.knowledgeArea);


// =============================================
// 第二部分：定义节点类型和属性
// =============================================

// 学生节点模板
// (:Student {
//     studentNo: String,        // 学号 (唯一)
//     name: String,            // 姓名
//     gender: String,          // 性别
//     enrollmentYear: Integer, // 入学年份
//     college: String,         // 学院
//     major: String,           // 专业
//     educationLevel: String, // 学历层次
//     profileType: String,     // 画像类型: 学术型/实践型/复合探索型
//     knowledgeEntropy: Float,// 知识结构熵
//     graphEditDistance: Float,// 图编辑距离
//     abilityLevel: String,    // 能力等级
//     personality: String,    // 人格特征
//     tags: List<String>,      // 画像标签
//     gpa: Float,             // 平均绩点
//     activityScore: Float,   // 活动得分
//     createTime: DateTime
// })

// 课程节点模板
// (:Course {
//     courseCode: String,      // 课程代码 (唯一)
//     courseName: String,      // 课程名称
//     credit: Float,           // 学分
//     courseType: String,      // 课程类型
//     knowledgeArea: String,   // 知识领域
//     difficultyLevel: Integer // 难度等级 1-5
// })

// 知识域节点模板
// (:Knowledge {
//     knowledgeArea: String,   // 知识领域
//     knowledgePoint: String,  // 知识点 (唯一)
//     difficultyLevel: Integer,// 难度等级
//     masteryLevel: Float      // 掌握程度
// })

// 活动节点模板
// (:Activity {
//     activityId: String,      // 活动ID (唯一)
//     activityName: String,    // 活动名称
//     activityType: String,    // 活动类型
//     level: String,           // 级别
//     date: Date,             // 日期
//     role: String,           // 角色
//     description: String      // 描述
// })

// 奖项节点模板
// (:Award {
//     awardId: String,        // 奖项ID
//     awardName: String,       // 奖项名称
//     awardType: String,      // 奖项类型
//     level: String,          // 级别
//     date: Date,            // 获奖日期
//     weight: Float           // 权重
// })


// =============================================
// 第三部分：定义关系类型
// =============================================

// 学生-课程关系
// (:Student)-[:ENROLLED {semester: String, score: Float, gpa: Float, isChallenge: Boolean}]->(:Course)

// 学生-知识域关系
// (:Student)-[:MASTERS {masteryLevel: Float, score: Float, lastStudyDate: Date}]->(:Knowledge)

// 学生活动关系
// (:Student)-[:PARTICIPATES {role: String, date: Date, semester: String}]->(:Activity)

// 活动-奖项关系
// (:Activity)-[:WINS {awardName: String, level: String, date: Date}]->(:Award)

// 课程-知识域关系 (课程包含哪些知识)
// (:Course)-[:CONTAINS {weight: Float}]->(:Knowledge)

// 知识域之间的关系 (知识点前置关系)
// (:Knowledge)-[:PREREQUISITE {strength: Float}]->(:Knowledge)


// =============================================
// 第四部分：示例数据创建
// =============================================

// 创建示例学生
CREATE (s1:Student {
    studentNo: '2022040906008',
    name: '张三',
    gender: '男',
    enrollmentYear: 2022,
    college: '计算机学院',
    major: '软件工程',
    educationLevel: '本科',
    profileType: '复合探索型',
    knowledgeEntropy: 4.0,
    graphEditDistance: 15.28,
    abilityLevel: 'B+',
    personality: '开放型,责任心强',
    tags: ['学术优秀', '学生干部', '社会实践积极'],
    gpa: 3.65,
    activityScore: 92.5,
    createTime: datetime()
})

CREATE (s2:Student {
    studentNo: '2021040906015',
    name: '李四',
    gender: '女',
    enrollmentYear: 2021,
    college: '计算机学院',
    major: '计算机科学与技术',
    educationLevel: '本科',
    profileType: '学术研究型',
    knowledgeEntropy: 2.5,
    graphEditDistance: 8.5,
    abilityLevel: 'A',
    personality: '严谨型,分析能力强',
    tags: ['科研达人', '论文发表', '竞赛获奖'],
    gpa: 3.92,
    activityScore: 78.0,
    createTime: datetime()
})

CREATE (s3:Student {
    studentNo: '2022040906022',
    name: '王五',
    gender: '男',
    enrollmentYear: 2022,
    college: '计算机学院',
    major: '网络工程',
    educationLevel: '本科',
    profileType: '实践应用型',
    knowledgeEntropy: 3.8,
    graphEditDistance: 22.5,
    abilityLevel: 'B',
    personality: '动手能力强,团队协作',
    tags: ['技术实践', '创业尝试', '志愿服务'],
    gpa: 3.21,
    activityScore: 95.0,
    createTime: datetime()
})

// 创建示例课程
CREATE (c1:Course {courseCode: 'CS101', courseName: '计算机导论', credit: 3.0, courseType: '必修', knowledgeArea: '计算机基础', difficultyLevel: 1})
CREATE (c2:Course {courseCode: 'CS201', courseName: '数据结构', credit: 4.0, courseType: '必修', knowledgeArea: '计算机基础', difficultyLevel: 3})
CREATE (c3:Course {courseCode: 'CS301', courseName: '操作系统', credit: 4.0, courseType: '必修', knowledgeArea: '系统基础', difficultyLevel: 4})
CREATE (c4:Course {courseCode: 'CS401', courseName: '数据库原理', credit: 3.0, courseType: '必修', knowledgeArea: '数据管理', difficultyLevel: 3})
CREATE (c5:Course {courseCode: 'CS302', courseName: '计算机网络', credit: 3.0, courseType: '必修', knowledgeArea: '网络基础', difficultyLevel: 4})
CREATE (c6:Course {courseCode: 'CS501', courseName: '机器学习', credit: 3.0, courseType: '选修', knowledgeArea: '人工智能', difficultyLevel: 5})

// 创建知识域节点
CREATE (k1:Knowledge {knowledgeArea: '计算机基础', knowledgePoint: '程序设计基础', difficultyLevel: 1})
CREATE (k2:Knowledge {knowledgeArea: '计算机基础', knowledgePoint: '数据结构与算法', difficultyLevel: 2})
CREATE (k3:Knowledge {knowledgeArea: '计算机基础', knowledgePoint: '线性结构', difficultyLevel: 3})
CREATE (k4:Knowledge {knowledgeArea: '计算机基础', knowledgePoint: '树形结构', difficultyLevel: 3})
CREATE (k5:Knowledge {knowledgeArea: '系统基础', knowledgePoint: '进程管理', difficultyLevel: 4})
CREATE (k6:Knowledge {knowledgeArea: '系统基础', knowledgePoint: '内存管理', difficultyLevel: 4})
CREATE (k7:Knowledge {knowledgeArea: '数据管理', knowledgePoint: '关系数据库', difficultyLevel: 3})
CREATE (k8:Knowledge {knowledgeArea: '数据管理', knowledgePoint: 'SQL查询', difficultyLevel: 3})
CREATE (k9:Knowledge {knowledgeArea: '人工智能', knowledgePoint: '机器学习基础', difficultyLevel: 5})
CREATE (k10:Knowledge {knowledgeArea: '人工智能', knowledgePoint: '深度学习', difficultyLevel: 5})

// 创建活动节点
CREATE (a1:Activity {
    activityId: 'ACT001',
    activityName: '全国大学生程序设计竞赛',
    activityType: '学科竞赛',
    level: '国家级',
    date: date('2024-10-15'),
    role: '队长',
    description: 'ACM-ICPC区域赛获银奖'
})
CREATE (a2:Activity {
    activityId: 'ACT002',
    activityName: '暑期社会实践',
    activityType: '社会实践',
    level: '校级',
    date: date('2024-07-01'),
    role: '队长',
    description: '乡村教育帮扶实践活动'
})
CREATE (a3:Activity {
    activityId: 'ACT003',
    activityName: '学生会主席',
    activityType: '学生工作',
    level: '校级',
    date: date('2023-09-01'),
    role: '主席',
    description: '计算机学院学生会主席'
})
CREATE (a4:Activity {
    activityId: 'ACT004',
    activityName: '志愿服务活动',
    activityType: '志愿服务',
    level: '院级',
    date: date('2024-03-05'),
    role: '志愿者',
    description: '图书馆志愿服务'
})

// =============================================
// 第五部分：创建关系
// =============================================

// 学生-课程关系
MATCH (s:Student {studentNo: '2022040906008'}), (c:Course {courseCode: 'CS101'})
CREATE (s)-[:ENROLLED {semester: '2022-1', score: 95.0, gpa: 4.0, isChallenge: false}]->(c)

MATCH (s:Student {studentNo: '2022040906008'}), (c:Course {courseCode: 'CS201'})
CREATE (s)-[:ENROLLED {semester: '2022-2', score: 88.0, gpa: 3.7, isChallenge: false}]->(c)

MATCH (s:Student {studentNo: '2022040906008'}), (c:Course {courseCode: 'CS301'})
CREATE (s)-[:ENROLLED {semester: '2023-1', score: 82.0, gpa: 3.5, isChallenge: false}]->(c)

MATCH (s:Student {studentNo: '2022040906008'}), (c:Course {courseCode: 'CS401'})
CREATE (s)-[:ENROLLED {semester: '2023-2', score: 91.0, gpa: 4.0, isChallenge: false}]->(c)

MATCH (s:Student {studentNo: '2022040906008'}), (c:Course {courseCode: 'CS501'})
CREATE (s)-[:ENROLLED {semester: '2024-1', score: 85.0, gpa: 3.7, isChallenge: true}]->(c)

MATCH (s:Student {studentNo: '2021040906015'}), (c:Course {courseCode: 'CS101'})
CREATE (s)-[:ENROLLED {semester: '2021-1', score: 98.0, gpa: 4.0, isChallenge: false}]->(c)

MATCH (s:Student {studentNo: '2021040906015'}), (c:Course {courseCode: 'CS201'})
CREATE (s)-[:ENROLLED {semester: '2021-2', score: 95.0, gpa: 4.0, isChallenge: false}]->(c)

MATCH (s:Student {studentNo: '2021040906015'}), (c:Course {courseCode: 'CS501'})
CREATE (s)-[:ENROLLED {semester: '2023-2', score: 92.0, gpa: 4.0, isChallenge: true}]->(c)

// 学生-知识域关系
MATCH (s:Student {studentNo: '2022040906008'}), (k:Knowledge {knowledgePoint: '程序设计基础'})
CREATE (s)-[:MASTERS {masteryLevel: 0.95, score: 95.0, lastStudyDate: date('2022-12-15')}]->(k)

MATCH (s:Student {studentNo: '2022040906008'}), (k:Knowledge {knowledgePoint: '数据结构与算法'})
CREATE (s)-[:MASTERS {masteryLevel: 0.88, score: 88.0, lastStudyDate: date('2023-06-15')}]->(k)

MATCH (s:Student {studentNo: '2022040906008'}), (k:Knowledge {knowledgePoint: '关系数据库'})
CREATE (s)-[:MASTERS {masteryLevel: 0.91, score: 91.0, lastStudyDate: date('2023-12-15')}]->(k)

MATCH (s:Student {studentNo: '2021040906015'}), (k:Knowledge {knowledgePoint: '机器学习基础'})
CREATE (s)-[:MASTERS {masteryLevel: 0.92, score: 92.0, lastStudyDate: date('2024-01-15')}]->(k)

MATCH (s:Student {studentNo: '2021040906015'}), (k:Knowledge {knowledgePoint: '深度学习'})
CREATE (s)-[:MASTERS {masteryLevel: 0.88, score: 88.0, lastStudyDate: date('2024-06-15')}]->(k)

// 学生活动关系
MATCH (s:Student {studentNo: '2022040906008'}), (a:Activity {activityId: 'ACT001'})
CREATE (s)-[:PARTICIPATES {role: '队长', date: date('2024-10-15'), semester: '2024-1'}]->(a)

MATCH (s:Student {studentNo: '2022040906008'}), (a:Activity {activityId: 'ACT002'})
CREATE (s)-[:PARTICIPATES {role: '队长', date: date('2024-07-01'), semester: '2024-0'}]->(a)

MATCH (s:Student {studentNo: '2022040906008'}), (a:Activity {activityId: 'ACT003'})
CREATE (s)-[:PARTICIPATES {role: '主席', date: date('2023-09-01'), semester: '2023-1'}]->(a)

MATCH (s:Student {studentNo: '2022040906008'}), (a:Activity {activityId: 'ACT004'})
CREATE (s)-[:PARTICIPATES {role: '志愿者', date: date('2024-03-05'), semester: '2024-0'}]->(a)

// 活动-奖项关系
MATCH (a:Activity {activityId: 'ACT001'})
CREATE (a)-[:WINS {awardName: 'ACM-ICPC区域赛银奖', level: '国家级', date: date('2024-10-20'), weight: 0.8}]->(a)

MATCH (a:Activity {activityId: 'ACT002'})
CREATE (a)-[:WINS {awardName: '优秀社会实践团队', level: '校级', date: date('2024-08-01'), weight: 0.6}]->(a)

MATCH (a:Activity {activityId: 'ACT003'})
CREATE (a)-[:WINS {awardName: '优秀学生干部', level: '校级', date: date('2024-05-01'), weight: 0.7}]->(a)

// 课程-知识域关系
MATCH (c:Course {courseCode: 'CS101'}), (k:Knowledge {knowledgePoint: '程序设计基础'})
CREATE (c)-[:CONTAINS {weight: 1.0}]->(k)

MATCH (c:Course {courseCode: 'CS201'}), (k:Knowledge {knowledgePoint: '数据结构与算法'})
CREATE (c)-[:CONTAINS {weight: 1.0}]->(k)

MATCH (c:Course {courseCode: 'CS201'}), (k:Knowledge {knowledgePoint: '线性结构'})
CREATE (c)-[:CONTAINS {weight: 0.8}]->(k)

MATCH (c:Course {courseCode: 'CS201'}), (k:Knowledge {knowledgePoint: '树形结构'})
CREATE (c)-[:CONTAINS {weight: 0.8}]->(k)

MATCH (c:Course {courseCode: 'CS301'}), (k:Knowledge {knowledgePoint: '进程管理'})
CREATE (c)-[:CONTAINS {weight: 1.0}]->(k)

MATCH (c:Course {courseCode: 'CS401'}), (k:Knowledge {knowledgePoint: '关系数据库'})
CREATE (c)-[:CONTAINS {weight: 1.0}]->(k)

MATCH (c:Course {courseCode: 'CS401'}), (k:Knowledge {knowledgePoint: 'SQL查询'})
CREATE (c)-[:CONTAINS {weight: 0.9}]->(k)

MATCH (c:Course {courseCode: 'CS501'}), (k:Knowledge {knowledgePoint: '机器学习基础'})
CREATE (c)-[:CONTAINS {weight: 1.0}]->(k)

MATCH (c:Course {courseCode: 'CS501'}), (k:Knowledge {knowledgePoint: '深度学习'})
CREATE (c)-[:CONTAINS {weight: 0.8}]->(k)

// 知识域前置关系
MATCH (k1:Knowledge {knowledgePoint: '程序设计基础'}), (k2:Knowledge {knowledgePoint: '数据结构与算法'})
CREATE (k2)-[:PREREQUISITE {strength: 0.9}]->(k1)

MATCH (k1:Knowledge {knowledgePoint: '数据结构与算法'}), (k2:Knowledge {knowledgePoint: '机器学习基础'})
CREATE (k2)-[:PREREQUISITE {strength: 0.85}]->(k1)

MATCH (k1:Knowledge {knowledgePoint: '线性结构'}), (k2:Knowledge {knowledgePoint: '树形结构'})
CREATE (k2)-[:PREREQUISITE {strength: 0.8}]->(k1)

MATCH (k1:Knowledge {knowledgePoint: '机器学习基础'}), (k2:Knowledge {knowledgePoint: '深度学习'})
CREATE (k2)-[:PREREQUISITE {strength: 0.9}]->(k1)


// =============================================
// 第六部分：验证图谱
// =============================================

// 统计节点数量
MATCH (n) RETURN labels(n)[0] as nodeType, count(n) as count ORDER BY count DESC;

// 统计关系数量
MATCH ()-[r]->() RETURN type(r) as relationType, count(r) as count ORDER BY count DESC;

// 查看学生画像类型分布
MATCH (s:Student) RETURN s.profileType as profileType, count(s) as count;

// 查看特定学生的完整画像
MATCH path = (s:Student {studentNo: '2022040906008'})-[*..2]-(n)
RETURN path LIMIT 50;

// =============================================
// 第七部分：常见查询示例
// =============================================

// 1. 查询学生的课程成绩
// MATCH (s:Student {studentNo: '2022040906008'})-[e:ENROLLED]->(c:Course)
// RETURN s.name, c.courseName, e.semester, e.score, e.gpa
// ORDER BY e.semester

// 2. 查询学生的知识掌握情况
// MATCH (s:Student {studentNo: '2022040906008'})-[m:MASTERS]->(k:Knowledge)
// RETURN k.knowledgeArea, k.knowledgePoint, m.masteryLevel, m.score
// ORDER BY m.masteryLevel DESC

// 3. 图游走查询 - 获取学生相关实体
// MATCH path = (s:Student {studentNo: '2022040906008'})-[:ENROLLED|MASTERS|PARTICIPATES*1..3]-(n)
// RETURN path LIMIT 50

// 4. 计算学生之间的相似度（基于共同课程）
// MATCH (s1:Student {studentNo: '2022040906008'})-[:ENROLLED]->(c:Course)<-[:ENROLLED]-(s2:Student)
// WITH s1, s2, collect(c.courseCode) as commonCourses
// RETURN s1.name, s2.name, size(commonCourses) as commonCount, commonCourses

// 5. 学业预警查询 - 查找知识结构熵异常的学生
// MATCH (s:Student)
// WHERE s.knowledgeEntropy > 4.0 OR s.graphEditDistance > 20
// RETURN s.studentNo, s.name, s.knowledgeEntropy, s.graphEditDistance, s.profileType

// 6. 查询课程知识图谱
// MATCH path = (c:Course {courseCode: 'CS501'})-[:CONTAINS]->(k:Knowledge)-[:PREREQUISITE*]-(p:Knowledge)
// RETURN c.courseName, path

// =============================================
// 完成提示
// =============================================

RETURN '🎉 知识图谱初始化完成！' as message;
