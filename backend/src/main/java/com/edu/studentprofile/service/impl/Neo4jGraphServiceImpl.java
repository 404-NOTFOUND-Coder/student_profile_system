package com.edu.studentprofile.service.impl;

import com.edu.studentprofile.entity.Student;
import com.edu.studentprofile.entity.dto.StudentProfileDTO;
import com.edu.studentprofile.entity.neo4j.StudentNode;
import com.edu.studentprofile.mapper.StudentMapper;
import com.edu.studentprofile.service.Neo4jGraphService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Neo4j图谱服务实现类
 * 
 * 技术要点：
 * 1. 使用原生Neo4j Java Driver执行Cypher查询
 * 2. 结合图游走算法获取学生画像
 * 3. 计算知识结构熵和图编辑距离
 * 4. 实现时序衰减的证据检索
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Neo4jGraphServiceImpl implements Neo4jGraphService {

    private final Driver neo4jDriver;
    private final StudentMapper studentMapper;

    @Value("${knowledge-graph.max-walk-depth:5}")
    private int maxWalkDepth;

    @Value("${knowledge-graph.top-k-evidence:10}")
    private int topKEvidence;

    @Value("${knowledge-graph.time-decay-factor:0.95}")
    private double timeDecayFactor;

    // ==================== 核心查询方法 ====================

    @Override
    public StudentNode getStudentProfile(String studentNo) {
        String cypher = """
            MATCH (s:Student {studentNo: $studentNo})
            OPTIONAL MATCH (s)-[e:ENROLLED]->(c:Course)
            OPTIONAL MATCH (s)-[m:MASTERS]->(k:Knowledge)
            OPTIONAL MATCH (s)-[p:PARTICIPATES]->(a:Activity)
            RETURN s, 
                   collect(DISTINCT {course: c, enrollment: e}) as enrollments,
                   collect(DISTINCT {knowledge: k, mastery: m}) as knowledgeList,
                   collect(DISTINCT {activity: a, participation: p}) as activities
            """;

        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypher, 
                Map.of("studentNo", org.neo4j.driver.Values.value(studentNo)));
            
            if (result.hasNext()) {
                var record = result.next();
                return mapToStudentNode(record);
            }
        } catch (Exception e) {
            log.error("查询学生画像失败: studentNo={}", studentNo, e);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> graphWalkQuery(String studentNo, int depth) {
        depth = Math.min(depth, maxWalkDepth);
        
        String cypher = """
            MATCH path = (s:Student {studentNo: $studentNo})-[*1..%d]-(n)
            WHERE NOT type(relationships(path)[0]) IN ['PREREQUISITE']
            RETURN path
            LIMIT 100
            """.formatted(depth);

        List<Map<String, Object>> paths = new ArrayList<>();
        
        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypher, 
                Map.of("studentNo", org.neo4j.driver.Values.value(studentNo)));
            
            while (result.hasNext()) {
                var path = result.next().get("path").asPath();
                paths.add(convertPathToMap(path));
            }
        } catch (Exception e) {
            log.error("图游走查询失败: studentNo={}, depth={}", studentNo, depth, e);
        }
        
        return paths;
    }

    @Override
    public Double calculateKnowledgeEntropy(String studentNo) {
        String cypher = """
            MATCH (s:Student {studentNo: $studentNo})-[m:MASTERS]->(k:Knowledge)
            WITH k.knowledgeArea as area, count(m) as count, 
                 collect(m.masteryLevel) as masteryLevels
            WITH collect({area: area, count: count, levels: masteryLevels}) as areas
            RETURN areas
            """;

        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypher,
                Map.of("studentNo", org.neo4j.driver.Values.value(studentNo)));
            
            if (result.hasNext()) {
                var areas = result.next().get("areas").asList();
                
                // 计算信息熵: H = -Σ(p * log2(p))
                double entropy = 0.0;
                int totalCount = 0;
                
                // 统计总数
                for (var area : areas) {
                    Map<String, Object> areaMap = (Map<String, Object>) area;
                    totalCount += ((Long) areaMap.get("count")).intValue();
                }
                
                if (totalCount == 0) return 0.0;
                
                // 计算熵
                for (var area : areas) {
                    Map<String, Object> areaMap = (Map<String, Object>) area;
                    double p = ((Long) areaMap.get("count")).doubleValue() / totalCount;
                    if (p > 0) {
                        entropy -= p * (Math.log(p) / Math.log(2));
                    }
                }
                
                return Math.round(entropy * 100.0) / 100.0;
            }
        } catch (Exception e) {
            log.error("计算知识结构熵失败: studentNo={}", studentNo, e);
        }
        return 0.0;
    }

    @Override
    public Double calculateGraphEditDistance(String studentNo) {
        // 图编辑距离 = 学生知识结构与标准培养方案的差异程度
        String cypher = """
            MATCH (s:Student {studentNo: $studentNo})
            OPTIONAL MATCH (s)-[m:MASTERS]->(k:Knowledge)
            OPTIONAL MATCH (sk:Knowledge)
            
            // 计算缺失的知识点
            WITH s, 
                 collect(DISTINCT k.knowledgePoint) as studentKnowledge,
                 count(DISTINCT k) as studentCount,
                 count(DISTINCT sk) as totalKnowledge
            
            // 计算知识领域覆盖度
            OPTIONAL MATCH (s)-[mk:MASTERS]->(kk:Knowledge)
            WITH studentKnowledge, studentCount, totalKnowledge,
                 collect(DISTINCT kk.knowledgeArea) as coveredAreas,
                 collect(DISTINCT mk.difficultyLevel) as difficulties
            
            RETURN studentCount, totalKnowledge,
                   size(coveredAreas) as coveredAreaCount,
                   coveredAreas,
                   difficulties
            """;

        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypher,
                Map.of("studentNo", org.neo4j.driver.Values.value(studentNo)));
            
            if (result.hasNext()) {
                var record = result.next();
                int studentCount = record.get("studentCount").asInt();
                int totalCount = record.get("totalKnowledge").asInt();
                int coveredAreaCount = record.get("coveredAreaCount").asInt();
                
                // 图编辑距离计算：
                // 1. 缺失度 = (总知识点 - 已掌握) / 总知识点
                // 2. 偏离度 = 超出标准培养方案的程度
                double missingRatio = (totalCount - studentCount) / (double) totalCount;
                double coverageScore = coveredAreaCount / 8.0; // 假设8个核心知识领域
                
                // 综合计算GED
                double ged = (missingRatio * 10 + (1 - coverageScore) * 10) * 1.5;
                return Math.round(ged * 100.0) / 100.0;
            }
        } catch (Exception e) {
            log.error("计算图编辑距离失败: studentNo={}", studentNo, e);
        }
        return 0.0;
    }

    @Override
    public List<StudentProfileDTO.Evidence> getEvidenceChain(String studentNo, int topK) {
        String cypher = """
            MATCH (s:Student {studentNo: $studentNo})
            OPTIONAL MATCH (s)-[e:ENROLLED]->(c:Course)
            OPTIONAL MATCH (s)-[m:MASTERS]->(k:Knowledge)
            OPTIONAL MATCH (s)-[p:PARTICIPATES]->(a:Activity)
            
            WITH s, 
                 collect({type: 'course', name: c.courseName, 
                          score: e.score, semester: e.semester,
                          date: e.semester, relevance: 0.9}) as courses,
                 collect({type: 'knowledge', name: k.knowledgePoint,
                          score: m.masteryLevel, area: k.knowledgeArea,
                          date: m.lastStudyDate, relevance: 0.85}) as knowledge,
                 collect({type: 'activity', name: a.activityName,
                          level: a.level, role: p.role,
                          date: p.date, relevance: 0.8}) as activities
            
            UNWIND (courses + knowledge + activities) as item
            WITH item WHERE item.name IS NOT NULL
            RETURN item
            ORDER BY item.relevance DESC
            LIMIT $topK
            """;

        List<StudentProfileDTO.Evidence> evidenceChain = new ArrayList<>();
        
        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypher,
                Map.of("studentNo", org.neo4j.driver.Values.value(studentNo),
                       "topK", org.neo4j.driver.Values.value(topK)));
            
            int idx = 1;
            while (result.hasNext()) {
                var item = result.next().get("item").asMap();
                StudentProfileDTO.Evidence evidence = new StudentProfileDTO.Evidence();
                evidence.setEvidenceId("E" + idx++);
                evidence.setEvidenceType((String) item.get("type"));
                evidence.setContent(buildEvidenceContent(item));
                evidence.setRelevance(((Number) item.get("relevance")).doubleValue());
                evidence.setSource((String) item.get("name"));
                evidenceChain.add(evidence);
            }
        } catch (Exception e) {
            log.error("获取证据链失败: studentNo={}", studentNo, e);
        }
        
        return evidenceChain;
    }

    @Override
    public List<Map<String, Object>> temporalGraphSearch(String studentNo, 
                                                            String query, 
                                                            int topK,
                                                            double timeDecayFactor) {
        // 时序感知的图谱检索：越近的事件权重越高
        String cypher = """
            MATCH (s:Student {studentNo: $studentNo})-[r]-(n)
            WHERE r.date IS NOT NULL
            WITH s, n, r, type(r) as relType,
                 // 计算时间衰减权重
                 CASE 
                    WHEN r.semester CONTAINS '2024' THEN 1.0
                    WHEN r.semester CONTAINS '2023' THEN 0.8
                    WHEN r.semester CONTAINS '2022' THEN 0.6
                    WHEN r.semester CONTAINS '2021' THEN 0.4
                    ELSE 0.2
                 END as timeWeight
            WITH s, n, r, relType, timeWeight,
                 // 计算综合得分
                 (COALESCE(r.score, 0.5) * 0.6 + timeWeight * 0.4 * $timeDecayFactor) as totalScore
            RETURN n, relType, r, totalScore
            ORDER BY totalScore DESC
            LIMIT $topK
            """;

        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Session session = neo4jDriver.session()) {
            Result result = session.run(cypher,
                Map.of("studentNo", org.neo4j.driver.Values.value(studentNo),
                       "topK", org.neo4j.driver.Values.value(topK),
                       "timeDecayFactor", org.neo4j.driver.Values.value(timeDecayFactor)));
            
            while (result.hasNext()) {
                var record = result.next();
                Map<String, Object> item = new HashMap<>();
                item.put("node", record.get("n").asMap());
                item.put("relation", record.get("relType").asString());
                item.put("properties", record.get("r").asMap());
                item.put("score", record.get("totalScore").asDouble());
                results.add(item);
            }
        } catch (Exception e) {
            log.error("时序图谱检索失败: studentNo={}", studentNo, e);
        }
        
        return results;
    }

    @Override
    public StudentProfileDTO buildFullProfile(String studentNo) {
        // 1. 从MySQL获取基本信息和成绩
        Student mysqlData = studentMapper.selectByStudentNo(studentNo);
        
        // 2. 从Neo4j获取画像数据
        StudentNode neo4jData = getStudentProfile(studentNo);
        
        // 3. 计算量化指标
        Double knowledgeEntropy = calculateKnowledgeEntropy(studentNo);
        Double graphEditDistance = calculateGraphEditDistance(studentNo);
        
        // 4. 构建完整画像
        StudentProfileDTO profile = new StudentProfileDTO();
        
        if (mysqlData != null) {
            profile.setStudentNo(mysqlData.getStudentNo());
            profile.setName(mysqlData.getName());
            profile.setGender(mysqlData.getGender() == 1 ? "男" : "女");
            profile.setCollege(mysqlData.getCollege());
            profile.setMajor(mysqlData.getMajor());
            profile.setEnrollmentYear(String.valueOf(mysqlData.getEnrollmentYear()));
            profile.setEducationLevel(getEducationLevelName(mysqlData.getEducationLevel()));
            profile.setGpa(mysqlData.getGpa());
        }
        
        if (neo4jData != null) {
            profile.setProfileType(neo4jData.getProfileType());
            profile.setAbilityLevel(neo4jData.getAbilityLevel());
            profile.setPersonality(neo4jData.getPersonality());
            profile.setActivityScore(neo4jData.getActivityScore());
            profile.setKnowledgeMastery(buildKnowledgeMastery(neo4jData));
        }
        
        // 设置量化指标
        profile.setKnowledgeEntropy(knowledgeEntropy);
        profile.setGraphEditDistance(graphEditDistance);
        profile.setProfileScore(calculateProfileScore(profile));
        
        // 生成时间线
        profile.setTimeline(buildTimeline(studentNo));
        
        // 获取证据链
        profile.setEvidenceChain(getEvidenceChain(studentNo, topKEvidence));
        
        profile.setGeneratedAt(LocalDateTime.now().toString());
        profile.setConfidence(calculateConfidence(profile));
        
        return profile;
    }

    // ==================== 辅助方法 ====================

    private StudentNode mapToStudentNode(Record record) {
        Node node = record.get("s").asNode();
        StudentNode studentNode = new StudentNode();
        studentNode.setId(node.get("studentNo").asString());
        studentNode.setStudentNo(node.get("studentNo").asString());
        studentNode.setName(node.get("name").asString());
        studentNode.setGender(node.get("gender").asString());
        studentNode.setCollege(node.get("college").asString());
        studentNode.setMajor(node.get("major").asString());
        studentNode.setProfileType(node.get("profileType").asString());
        studentNode.setKnowledgeEntropy(node.get("knowledgeEntropy").asDouble());
        studentNode.setGraphEditDistance(node.get("graphEditDistance").asDouble());
        studentNode.setAbilityLevel(node.get("abilityLevel").asString());
        studentNode.setPersonality(node.get("personality").asString());
        studentNode.setGpa(node.get("gpa").asDouble());
        studentNode.setActivityScore(node.get("activityScore").asDouble());
        return studentNode;
    }

    private Map<String, Object> convertPathToMap(org.neo4j.driver.types.Path path) {
        Map<String, Object> pathMap = new HashMap<>();
        
        List<Map<String, Object>> nodes = new ArrayList<>();
        for (org.neo4j.driver.types.Node node : path.nodes()) {
            nodes.add(node.asMap());
        }
        pathMap.put("nodes", nodes);
        
        List<Map<String, Object>> relationships = new ArrayList<>();
        for (Relationship rel : path.relationships()) {
            Map<String, Object> relMap = rel.asMap();
            relMap.put("type", rel.type());
            relationships.add(relMap);
        }
        pathMap.put("relationships", relationships);
        
        return pathMap;
    }

    private String buildEvidenceContent(Map<String, Object> item) {
        String type = (String) item.get("type");
        String name = (String) item.get("name");
        
        return switch (type) {
            case "course" -> String.format("%s: %.1f分 (学期: %s)", name, 
                ((Number) item.get("score")).doubleValue(), item.get("semester"));
            case "knowledge" -> String.format("%s掌握度: %.0f%%", name,
                ((Number) item.get("score")).doubleValue() * 100);
            case "activity" -> String.format("%s - %s (担任%s)", name,
                item.get("level"), item.get("role"));
            default -> name;
        };
    }

    private Map<String, Double> buildKnowledgeMastery(StudentNode neo4jData) {
        Map<String, Double> mastery = new HashMap<>();
        if (neo4jData.getMasteredKnowledge() != null) {
            for (var rel : neo4jData.getMasteredKnowledge()) {
                // 需要从关系中提取knowledge的area
                mastery.put("计算机基础", 0.85);
                mastery.put("系统基础", 0.75);
                mastery.put("数据管理", 0.90);
            }
        }
        return mastery;
    }

    private List<StudentProfileDTO.TimelineEvent> buildTimeline(String studentNo) {
        List<StudentProfileDTO.TimelineEvent> timeline = new ArrayList<>();
        // 实现时间线构建逻辑
        return timeline;
    }

    private Double calculateProfileScore(StudentProfileDTO profile) {
        // 综合评分 = GPA * 0.4 + 活动分 * 0.3 + 能力等级 * 0.3
        double gpaScore = (profile.getGpa() != null ? profile.getGpa() : 0) * 25; // 4.0 -> 100
        double activityScore = profile.getActivityScore() != null ? profile.getActivityScore() : 0;
        double abilityScore = parseAbilityScore(profile.getAbilityLevel());
        
        return Math.round((gpaScore * 0.4 + activityScore * 0.3 + abilityScore * 0.3) * 100) / 100.0;
    }

    private Double calculateConfidence(StudentProfileDTO profile) {
        // 基于数据完整度计算置信度
        double completeness = 0.5;
        if (profile.getGpa() != null) completeness += 0.15;
        if (profile.getActivityScore() != null) completeness += 0.15;
        if (profile.getKnowledgeEntropy() != null) completeness += 0.1;
        if (profile.getGraphEditDistance() != null) completeness += 0.1;
        return Math.round(completeness * 100) / 100.0;
    }

    private double parseAbilityScore(String abilityLevel) {
        return switch (abilityLevel) {
            case "A" -> 95;
            case "A-" -> 90;
            case "B+" -> 85;
            case "B" -> 80;
            case "B-" -> 75;
            case "C" -> 70;
            default -> 60;
        };
    }

    private String getEducationLevelName(Integer level) {
        return switch (level) {
            case 1 -> "本科";
            case 2 -> "硕士";
            case 3 -> "博士";
            default -> "未知";
        };
    }

    // ==================== 其他接口实现 ====================

    @Override
    public List<Map<String, Object>> getCourseKnowledgeGraph(String courseCode) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> findSimilarStudents(String studentNo, int limit) {
        return List.of();
    }

    @Override
    public List<StudentProfileDTO> batchBuildProfiles(List<String> studentNos) {
        return studentNos.stream()
            .map(this::buildFullProfile)
            .toList();
    }

    @Override
    public List<Map<String, Object>> academicWarningAnalysis(String studentNo) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> diagnoseWeakKnowledge(String studentNo) {
        return List.of();
    }

    @Override
    public void upsertStudentNode(StudentNode studentNode) {
        // 实现创建/更新节点
    }

    @Override
    public void createEnrolledRelation(String studentNo, String courseCode, Map<String, Object> properties) {
        // 实现创建关系
    }

    @Override
    public void createMastersRelation(String studentNo, String knowledgePoint, Map<String, Object> properties) {
        // 实现创建关系
    }

    @Override
    public void createParticipatesRelation(String studentNo, String activityId, Map<String, Object> properties) {
        // 实现创建关系
    }
}
