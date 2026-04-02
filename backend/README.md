# 后端开发指南

## 📦 技术栈

- **框架**: Spring Boot 3.2.0
- **ORM**: MyBatis Plus 3.5.5
- **图数据库**: Neo4j 5.14.0 (Bolt协议)
- **数据库**: MySQL 8.0
- **工具库**: Hutool 5.8.23
- **安全**: JWT (jjwt 0.12.3)

## 🏃 项目启动

### 1. 配置 application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_profile
    username: root
    password: your_password
  
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: neo4j_password
```

### 2. 初始化数据库

```bash
# MySQL
mysql -u root -p < src/main/resources/db/mysql_schema.sql

# Neo4j
cypher-shell -u neo4j -p password < src/main/resources/db/neo4j_schema.cql
```

### 3. 启动应用

```bash
mvn spring-boot:run
```

## 📐 架构设计

### 分层结构

```
Controller -> Service -> Mapper
                  ↓
              Neo4j Service
                  ↓
             Neo4j Driver
```

### 核心服务

#### Neo4jGraphService

图谱服务核心接口，提供以下能力：

1. **图游走查询**: 基于深度优先/广度优先的图遍历
2. **知识熵计算**: 基于信息论的量化指标
3. **证据链检索**: 时序感知的上下文检索
4. **画像生成**: 融合多数据源的画像构建

## 🔐 安全配置

### JWT Token 验证

```java
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, ...) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            // 验证token
            String jwt = token.substring(7);
            // 解码并验证
        }
        return true;
    }
}
```

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 生成测试报告
mvn test jacoco:report
```

## 📊 性能优化

### Neo4j 查询优化

1. **使用参数化查询**: 避免 Cypher 注入
2. **限制返回数量**: 使用 `LIMIT`
3. **创建索引**: 对常用查询字段创建索引
4. **使用 PROFILE**: 分析查询计划

### MySQL 优化

1. **使用连接池**: HikariCP (默认配置)
2. **批量操作**: 使用 MyBatis Plus 的批量方法
3. **分页查询**: 使用 PageHelper 或 MyBatis Plus 分页

## 📝 Cypher 查询示例

### 查询学生画像

```cypher
MATCH (s:Student {studentNo: $studentNo})
OPTIONAL MATCH (s)-[e:ENROLLED]->(c:Course)
OPTIONAL MATCH (s)-[m:MASTERS]->(k:Knowledge)
RETURN s, 
       collect(DISTINCT {course: c, enrollment: e}) as enrollments,
       collect(DISTINCT {knowledge: k, mastery: m}) as knowledgeList
```

### 时序感知检索

```cypher
MATCH (s:Student {studentNo: $studentNo})-[r]-(n)
WITH s, n, r,
     CASE 
        WHEN r.semester CONTAINS '2024' THEN 1.0
        WHEN r.semester CONTAINS '2023' THEN 0.8
        ELSE 0.6
     END as timeWeight
RETURN n, r, timeWeight
ORDER BY timeWeight DESC
LIMIT $topK
```

## 🚀 部署

### Docker 部署

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
COPY target/student-profile-system-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 环境变量配置

```bash
export SPRING_DATASOURCE_PASSWORD=your_password
export SPRING_NEO4J_AUTHENTICATION_PASSWORD=neo4j_password
java -jar app.jar
```
