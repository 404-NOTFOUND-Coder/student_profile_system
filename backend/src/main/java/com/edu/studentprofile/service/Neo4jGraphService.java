package com.edu.studentprofile.service;

import com.edu.studentprofile.entity.dto.StudentProfileDTO;
import com.edu.studentprofile.entity.neo4j.StudentNode;

import java.util.List;
import java.util.Map;

/**
 * Neo4j图谱服务接口
 */
public interface Neo4jGraphService {

    /**
     * 根据学号获取学生画像节点
     */
    StudentNode getStudentProfile(String studentNo);

    /**
     * 图游走查询 - 获取学生相关实体路径
     */
    List<Map<String, Object>> graphWalkQuery(String studentNo, int depth);

    /**
     * 计算知识结构熵
     */
    Double calculateKnowledgeEntropy(String studentNo);

    /**
     * 计算图编辑距离
     */
    Double calculateGraphEditDistance(String studentNo);

    /**
     * 获取学生证据链
     */
    List<StudentProfileDTO.Evidence> getEvidenceChain(String studentNo, int topK);

    /**
     * 课程知识图谱查询
     */
    List<Map<String, Object>> getCourseKnowledgeGraph(String courseCode);

    /**
     * 学生相似度分析
     */
    List<Map<String, Object>> findSimilarStudents(String studentNo, int limit);

    /**
     * 时序感知的图谱检索（带时序衰减）
     */
    List<Map<String, Object>> temporalGraphSearch(String studentNo, 
                                                   String query, 
                                                   int topK,
                                                   double timeDecayFactor);

    /**
     * 构建完整学生画像（融合MySQL + Neo4j）
     */
    StudentProfileDTO buildFullProfile(String studentNo);

    /**
     * 批量生成学生画像
     */
    List<StudentProfileDTO> batchBuildProfiles(List<String> studentNos);

    /**
     * 学业预警分析
     */
    List<Map<String, Object>> academicWarningAnalysis(String studentNo);

    /**
     * 知识薄弱点诊断
     */
    List<Map<String, Object>> diagnoseWeakKnowledge(String studentNo);

    /**
     * 创建或更新学生节点
     */
    void upsertStudentNode(StudentNode studentNode);

    /**
     * 创建学生-课程关系
     */
    void createEnrolledRelation(String studentNo, String courseCode, 
                                Map<String, Object> properties);

    /**
     * 创建学生-知识域关系
     */
    void createMastersRelation(String studentNo, String knowledgePoint,
                              Map<String, Object> properties);

    /**
     * 创建学生活动关系
     */
    void createParticipatesRelation(String studentNo, String activityId,
                                   Map<String, Object> properties);
}
