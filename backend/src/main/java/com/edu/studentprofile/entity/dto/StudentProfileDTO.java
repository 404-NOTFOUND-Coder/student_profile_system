package com.edu.studentprofile.entity.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 学生画像数据传输对象
 */
@Data
public class StudentProfileDTO {

    // ==================== 基本信息 ====================
    private String studentNo;
    private String name;
    private String gender;
    private String college;
    private String major;
    private String enrollmentYear;
    private String educationLevel;

    // ==================== 画像核心指标 ====================
    /** 画像类型: 学术研究型/实践应用型/复合探索型 */
    private String profileType;

    /** 知识结构熵 */
    private Double knowledgeEntropy;

    /** 图编辑距离 */
    private Double graphEditDistance;

    /** 能力等级 */
    private String abilityLevel;

    /** 人格特征 */
    private String personality;

    /** 综合评分 */
    private Double profileScore;

    // ==================== 学业分析 ====================
    /** 平均绩点 */
    private Double gpa;

    /** 总学分 */
    private Double totalCredits;

    /** 课程数量 */
    private Integer courseCount;

    /** 课程成绩列表 */
    private List<CoursePerformance> coursePerformance;

    /** 知识领域掌握情况 */
    private Map<String, Double> knowledgeMastery;

    /** 学业趋势分析 */
    private AcademicTrend academicTrend;

    // ==================== 活动分析 ====================
    /** 活动得分 */
    private Double activityScore;

    /** 活动总数 */
    private Integer activityCount;

    /** 活动分类统计 */
    private Map<String, Integer> activityDistribution;

    /** 获奖情况 */
    private List<AwardRecord> awardRecords;

    /** 重要活动列表 */
    private List<ActivityRecord> importantActivities;

    // ==================== 画像标签 ====================
    /** 学术标签 */
    private List<String> academicTags;

    /** 实践标签 */
    private List<String> practiceTags;

    /** 发展建议标签 */
    private List<String> suggestionTags;

    // ==================== 时序证据 ====================
    /** 关键事件时间线 */
    private List<TimelineEvent> timeline;

    /** Top-K证据链 */
    private List<Evidence> evidenceChain;

    // ==================== 生成信息 ====================
    /** 画像生成时间 */
    private String generatedAt;

    /** 画像置信度 */
    private Double confidence;

    // ==================== 内部类定义 ====================

    @Data
    public static class CoursePerformance {
        private String courseCode;
        private String courseName;
        private String semester;
        private Double score;
        private Double gpa;
        private String knowledgeArea;
        private Boolean isChallenge;
    }

    @Data
    public static class AcademicTrend {
        /** 学业趋势: RISING(上升), STABLE(稳定), DECLINING(下降) */
        private String trend;

        /** 趋势描述 */
        private String description;

        /** 波动程度 */
        private Double volatility;
    }

    @Data
    public static class AwardRecord {
        private String awardName;
        private String level;
        private String date;
        private String activityName;
        private Double weight;
    }

    @Data
    public static class ActivityRecord {
        private String activityName;
        private String activityType;
        private String level;
        private String date;
        private String role;
    }

    @Data
    public static class TimelineEvent {
        private String date;
        private String eventType;
        private String eventName;
        private String description;
        private Double importance;
    }

    @Data
    public static class Evidence {
        private String evidenceId;
        private String evidenceType;
        private String content;
        private Double relevance;
        private String source;
    }
}
