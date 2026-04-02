# 学生用户画像系统

> 基于知识图谱的学生多维画像系统，融合 MySQL 结构化数据与 Neo4j 图数据库

## 📋 项目概述

本系统是一个面向高校的学生用户画像平台，通过整合学生的学业成绩、课外活动、获奖情况等多维度数据，结合 Neo4j 知识图谱技术，构建学生的多维画像，为教育教学管理提供数据支撑。

### 核心技术栈

| 层级 | 技术选型 | 说明 |
|------|---------|------|
| 前端 | Vue 3 + Element Plus | 现代化组件化开发 |
| 后端 | Spring Boot 3 | 企业级应用框架 |
| ORM | MyBatis Plus | 简化数据库操作 |
| 关系数据库 | MySQL 8.0 | 结构化数据存储 |
| 图数据库 | Neo4j | 知识图谱存储与查询 |
| 构建工具 | Maven + Vite | 项目构建管理 |

## 🏗️ 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                        前端层 (Vue 3)                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐      │
│  │ Dashboard │  │ 学生档案  │  │ 画像分析  │  │ 知识图谱  │      │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘      │
└─────────────────────────────────────────────────────────────┘
                              │ HTTP/REST API
┌─────────────────────────────────────────────────────────────┐
│                       后端服务层 (Spring Boot)                │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Controller 层                      │   │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐      │   │
│  │  │ 学生档案API │  │ 画像生成API │  │ 图谱查询API │      │   │
│  │  └────────────┘  └────────────┘  └────────────┘      │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Service 层                         │   │
│  │  ┌──────────────┐        ┌──────────────────────┐   │   │
│  │  │ MySQL数据服务 │        │ Neo4j图谱服务         │   │   │
│  │  │ - 学生管理    │        │ - 图游走查询          │   │   │
│  │  │ - 成绩管理    │        │ - 知识熵计算          │   │   │
│  │  │ - 活动管理    │        │ - 证据链检索          │   │   │
│  │  └──────────────┘        └──────────────────────┘   │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                    │                    │
        ┌───────────┴──┐          ┌──────┴────────┐
        │    MySQL     │          │     Neo4j     │
        │              │          │               │
        │ - 学生表     │          │ - Student节点 │
        │ - 成绩表     │          │ - Course节点  │
        │ - 活动表     │          │ - Knowledge节点│
        │ - 预警表     │          │ - ENROLLED关系│
        └──────────────┘          │ - MASTERS关系 │
                                   └──────────────┘
```

## 📂 项目结构

```
student_profile_system/
├── backend/                          # 后端 Spring Boot 项目
│   ├── pom.xml                       # Maven 依赖配置
│   └── src/
│       └── main/
│           ├── java/com/edu/studentprofile/
│           │   ├── StudentProfileApplication.java
│           │   ├── config/          # 配置类
│           │   │   └── Neo4jConfig.java
│           │   ├── controller/       # 控制器层
│           │   │   └── StudentProfileController.java
│           │   ├── entity/          # 实体类
│           │   │   ├── Student.java
│           │   │   ├── AcademicScore.java
│           │   │   ├── dto/
│           │   │   │   └── StudentProfileDTO.java
│           │   │   └── neo4j/
│           │   │       ├── StudentNode.java
│           │   │       └── CourseNode.java
│           │   ├── mapper/          # 数据访问层
│           │   │   └── StudentMapper.java
│           │   ├── service/         # 业务逻辑层
│           │   │   ├── Neo4jGraphService.java
│           │   │   └── impl/
│           │   │       └── Neo4jGraphServiceImpl.java
│           │   └── util/
│           │       └── R.java       # 统一响应封装
│           └── resources/
│               ├── application.yml # 应用配置
│               └── db/
│                   ├── mysql_schema.sql   # MySQL 初始化脚本
│                   └── neo4j_schema.cql    # Neo4j 初始化脚本
│
├── frontend/                         # 前端 Vue 项目
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── main.js                  # 入口文件
│       ├── App.vue
│       ├── api/
│       │   └── student.js           # API 调用封装
│       ├── assets/
│       │   └── styles/
│       │       └── main.scss         # 全局样式
│       ├── router/
│       │   └── index.js              # 路由配置
│       └── views/
│           ├── Layout.vue            # 布局组件
│           ├── Dashboard.vue         # 首页
│           ├── Login.vue             # 登录页
│           ├── Graph.vue             # 知识图谱
│           ├── Warning.vue           # 学业预警
│           ├── Analytics.vue        # 数据分析
│           └── student/
│               ├── StudentList.vue   # 学生列表
│               └── StudentProfile.vue # 学生画像详情
│
└── README.md                         # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Neo4j 5.x

### 1. 启动 Neo4j 数据库

```bash
# 使用 Docker 启动
docker run -d \
  --name neo4j \
  -p 7474:7474 \
  -p 7687:7687 \
  -e NEO4J_AUTH=neo4j/neo4j123456 \
  neo4j:5.14.0

# 初始化图谱数据
cypher-shell -u neo4j -p neo4j123456 < backend/src/main/resources/db/neo4j_schema.cql
```

### 2. 启动后端服务

```bash
cd backend

# 安装依赖
mvn clean install

# 启动应用
mvn spring-boot:run
```

### 3. 启动前端服务

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 4. 访问系统

- 前端地址: http://localhost:3000
- 后端地址: http://localhost:8080/api
- Neo4j Browser: http://localhost:7474

## 📊 核心功能

### 1. 学生档案管理

- 学生信息的增删改查
- 批量导入/导出
- 多条件筛选

### 2. 学生画像分析

#### 画像类型分类

| 类型 | 特征描述 |
|------|---------|
| 学术研究型 | GPA 高、知识深度好、科研能力强 |
| 实践应用型 | 活动丰富、实践能力强、创新意识强 |
| 复合探索型 | 全面发展、兴趣广泛、综合素质高 |

#### 核心量化指标

| 指标 | 计算方式 | 说明 |
|------|---------|------|
| 知识结构熵 | H = -Σ(p * log₂(p)) | 衡量知识面广度 |
| 图编辑距离 | GED | 与标准培养方案的偏离程度 |
| 综合评分 | GPA×0.4 + 活动分×0.3 + 能力×0.3 | 综合素质评估 |

### 3. 知识图谱查询

```cypher
// 图游走查询示例
MATCH path = (s:Student {studentNo: '2022040906008'})-[*1..3]-(n)
RETURN path LIMIT 50

// 时序感知的证据检索
MATCH (s:Student {studentNo: $studentNo})-[r]-(n)
WHERE r.date IS NOT NULL
WITH s, n, r, 
     CASE WHEN r.semester CONTAINS '2024' THEN 1.0 ELSE 0.6 END as timeWeight
RETURN n, r, timeWeight
ORDER BY timeWeight DESC
```

### 4. 学业预警

- 成绩预警（GPA 低于阈值）
- 学分预警（未修满规定学分）
- 知识薄弱点诊断

## 🔧 API 接口

### 学生档案

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /student/list | 分页查询学生 |
| GET | /student/{studentNo} | 获取学生详情 |
| POST | /student | 创建学生 |
| PUT | /student/{id} | 更新学生 |
| DELETE | /student/{id} | 删除学生 |

### 画像分析

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /student/{studentNo}/profile | 获取完整画像 |
| GET | /student/{studentNo}/graph/path | 图游走查询 |
| GET | /student/{studentNo}/evidence | 获取证据链 |
| GET | /student/{studentNo}/knowledge-entropy | 知识结构熵 |
| GET | /student/{studentNo}/graph-edit-distance | 图编辑距离 |
| GET | /student/{studentNo}/similar | 相似学生分析 |

## 📝 Neo4j 图谱模型

### 节点类型

```
(:Student)           # 学生节点
(:Course)            # 课程节点
(:Knowledge)         # 知识域节点
(:Activity)          # 活动节点
(:Award)             # 奖项节点
```

### 关系类型

```
(:Student)-[:ENROLLED]->(:Course)       # 选课关系
(:Student)-[:MASTERS]->(:Knowledge)     # 知识掌握关系
(:Student)-[:PARTICIPATES]->(:Activity) # 参加活动关系
(:Activity)-[:WINS]->(:Award)           # 获奖关系
(:Course)-[:CONTAINS]->(:Knowledge)     # 课程包含知识关系
(:Knowledge)-[:PREREQUISITE]->(:Knowledge) # 知识前置关系
```

## 📚 技术文档

详细的开发文档位于各模块的 README 文件中：

- [后端开发指南](./backend/README.md)
- [前端开发指南](./frontend/README.md)
- [数据库设计文档](./docs/database.md)
- [API 接口文档](./docs/api.md)

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 👥 开发团队

- 架构设计：高级开发工程师
- 核心开发：团队成员
- 技术支持：持续迭代优化

---

**Made with ❤️ for Education**
