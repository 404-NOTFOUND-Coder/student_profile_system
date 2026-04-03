package com.edu.studentprofile.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.studentprofile.entity.KnowledgeGraph;
import com.edu.studentprofile.mapper.KnowledgeGraphMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识图谱服务
 */
@Service
public class KnowledgeGraphService extends ServiceImpl<KnowledgeGraphMapper, KnowledgeGraph> {

    /**
     * 获取学生的完整知识图谱
     * 包括静态属性和动态活动
     */
    public Map<String, Object> getStudentKnowledgeGraph(String studentNo) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取静态关系
        List<KnowledgeGraph> staticRelations = baseMapper.findStaticRelationsByStudentId(studentNo);
        result.put("staticRelations", staticRelations);
        
        // 获取动态关系
        List<KnowledgeGraph> dynamicRelations = baseMapper.findDynamicRelationsByStudentId(studentNo);
        result.put("dynamicRelations", dynamicRelations);
        
        // 统计活动数量
        Integer activityCount = baseMapper.countActivitiesByStudentId(studentNo);
        result.put("activityCount", activityCount);
        
        // 构建四元组列表
        List<Map<String, Object>> quads = buildQuads(staticRelations, dynamicRelations);
        result.put("quads", quads);
        
        return result;
    }

    /**
     * 构建四元组列表
     */
    private List<Map<String, Object>> buildQuads(List<KnowledgeGraph> staticRelations, List<KnowledgeGraph> dynamicRelations) {
        List<Map<String, Object>> quads = new ArrayList<>();
        
        // 添加静态关系四元组
        for (KnowledgeGraph kg : staticRelations) {
            Map<String, Object> quad = new HashMap<>();
            quad.put("head", kg.getHeadName());
            quad.put("relation", kg.getRelation());
            quad.put("tail", kg.getTailName());
            quad.put("time", null);
            quad.put("type", "static");
            quads.add(quad);
        }
        
        // 添加动态关系四元组
        for (KnowledgeGraph kg : dynamicRelations) {
            Map<String, Object> quad = new HashMap<>();
            quad.put("head", kg.getHeadName());
            // 构建完整关系
            String fullRelation = kg.getRelation();
            if (kg.getSubRelation() != null && !kg.getSubRelation().isEmpty()) {
                fullRelation += "_" + kg.getSubRelation();
            }
            quad.put("relation", fullRelation);
            quad.put("tail", kg.getTailName());
            quad.put("time", kg.getTimeDimension());
            quad.put("type", "dynamic");
            quad.put("level", kg.getActivityLevel());
            quad.put("awardLevel", kg.getAwardLevel());
            quads.add(quad);
        }
        
        return quads;
    }

    /**
     * 添加静态关系
     */
    public boolean addStaticRelation(String studentNo, String studentName, String relation, String tailName) {
        KnowledgeGraph kg = new KnowledgeGraph();
        kg.setHeadType("student");
        kg.setHeadId(studentNo);
        kg.setHeadName(studentName);
        kg.setRelationType("static_relation");
        kg.setRelation(relation);
        kg.setTailType(getTailType(relation));
        kg.setTailName(tailName);
        kg.setSource("system");
        kg.setCreateTime(LocalDateTime.now());
        return this.save(kg);
    }

    /**
     * 添加动态关系
     */
    public boolean addDynamicRelation(String studentNo, String studentName, String relation, 
                                      String subRelation, String activityName, 
                                      String timeDimension, String level, String awardLevel) {
        KnowledgeGraph kg = new KnowledgeGraph();
        kg.setHeadType("student");
        kg.setHeadId(studentNo);
        kg.setHeadName(studentName);
        kg.setRelationType("dynamic_relation");
        kg.setRelation(relation);
        kg.setSubRelation(subRelation);
        kg.setTailType("activity");
        kg.setTailName(activityName);
        kg.setTimeDimension(timeDimension);
        kg.setActivityLevel(level);
        kg.setAwardLevel(awardLevel);
        kg.setSource("manual");
        kg.setCreateTime(LocalDateTime.now());
        return this.save(kg);
    }

    /**
     * 根据关系类型获取尾实体类型
     */
    private String getTailType(String relation) {
        switch (relation) {
            case "隶属院系":
                return "college";
            case "隶属专业":
                return "major";
            case "性别":
                return "gender";
            case "年级":
                return "grade";
            case "班级":
                return "class";
            case "来自于":
                return "hometown";
            default:
                return "unknown";
        }
    }

    /**
     * 获取学生的活动统计
     */
    public Map<String, Object> getActivityStatistics(String studentNo) {
        Map<String, Object> stats = new HashMap<>();
        
        List<KnowledgeGraph> dynamicRelations = baseMapper.findDynamicRelationsByStudentId(studentNo);
        
        // 按一级关系分类统计
        Map<String, Long> byRelation = dynamicRelations.stream()
                .collect(Collectors.groupingBy(KnowledgeGraph::getRelation, Collectors.counting()));
        stats.put("byRelation", byRelation);
        
        // 按级别分类统计
        Map<String, Long> byLevel = dynamicRelations.stream()
                .filter(kg -> kg.getActivityLevel() != null)
                .collect(Collectors.groupingBy(KnowledgeGraph::getActivityLevel, Collectors.counting()));
        stats.put("byLevel", byLevel);
        
        // 按年份统计
        Map<String, Long> byYear = dynamicRelations.stream()
                .filter(kg -> kg.getTimeDimension() != null)
                .collect(Collectors.groupingBy(kg -> kg.getTimeDimension().substring(0, 4), Collectors.counting()));
        stats.put("byYear", byYear);
        
        // 总活动数
        stats.put("total", dynamicRelations.size());
        
        return stats;
    }
}
