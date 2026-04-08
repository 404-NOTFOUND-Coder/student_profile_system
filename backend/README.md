# 学生画像系统后端

基于 Spring Boot 3.2 的后端服务，使用 MySQL 和 Neo4j 作为数据存储。

## 技术栈

- **框架**: Spring Boot 3.2
- **关系数据库**: MySQL 8.0 (学生、活动、预警等数据)
- **图数据库**: Neo4j (知识图谱数据)
- **ORM**: Spring Data JPA, Spring Data Neo4j
- **构建工具**: Maven

## 项目结构

```
backend/
├── src/main/java/com/education/studentprofile/
│   ├── config/              # 配置类
│   ├── controller/         # REST 控制器
│   ├── dto/                # 数据传输对象
│   ├── entity/
│   │   ├── mysql/          # MySQL 实体类
│   │   └── neo4j/          # Neo4j 实体类
│   ├── repository/
│   │   ├── mysql/          # MySQL 仓库
│   │   └── neo4j/          # Neo4j 仓库
│   ├── service/            # 业务服务层
│   └── util/              # 工具类
├── src/main/resources/
│   ├── application.yml     # 应用配置
│   └── application-dev.yml # 开发环境配置
└── pom.xml
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Neo4j 4.4+

### 数据库配置

1. **MySQL 配置**
```sql
CREATE DATABASE student_profile DEFAULT CHARACTER SET utf8mb4;
```

2. **Neo4j 配置**
```bash
# 启动 Neo4j
neo4j start
# 默认账号密码: neo4j / neo4j123
```

### 启动后端服务

```bash
cd backend

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/student-profile-system-1.0.0.jar
```

### API 端口

后端服务默认运行在: `http://localhost:8088`

## API 接口

### 学生管理
- `GET /api/students` - 获取所有学生
- `GET /api/students/{studentNo}` - 根据学号获取学生
- `GET /api/students/search` - 搜索学生
- `POST /api/students` - 创建学生
- `PUT /api/students/{studentNo}` - 更新学生
- `DELETE /api/students/{studentNo}` - 删除学生

### 活动管理
- `GET /api/activities` - 获取所有活动
- `GET /api/activities/type/{type}` - 按类型获取活动
- `GET /api/activities/student/{studentNo}` - 获取学生参与的活动
- `POST /api/activities` - 创建活动
- `PUT /api/activities/{id}` - 更新活动
- `DELETE /api/activities/{id}` - 删除活动

### 预警管理
- `GET /api/warnings` - 获取所有预警
- `GET /api/warnings/student/{studentNo}` - 获取学生的预警
- `GET /api/warnings/type/{warningType}` - 按类型获取预警
- `POST /api/warnings` - 创建预警
- `DELETE /api/warnings/{id}` - 删除预警

### 画像生成
- `POST /api/profile/generate` - 生成学生画像报告

### 知识图谱
- `GET /api/knowledge-graph/global` - 获取全局知识图谱
- `GET /api/knowledge-graph/student/{studentNo}` - 获取学生知识图谱

### 数据统计
- `GET /api/dashboard/stats` - 获取仪表盘统计数据

## 前端对接

前端的 API 请求需要指向后端服务地址：

```javascript
// vite.config.js 或 .env
VITE_API_BASE_URL=http://localhost:8088/api
```

CORS 已配置允许跨域访问。
