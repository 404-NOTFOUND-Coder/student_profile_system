package com.education.studentprofile.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.LocalDateTime;

@Data
@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "student_no", length = 20)
    private String studentNo;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "birthday", length = 20)
    private String birthday;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "college", length = 200)
    private String college;

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "class_name", length = 50)
    private String className;

    @Column(name = "enrollment_year")
    private Integer enrollmentYear;

    @Column(name = "student_status")
    private Integer studentStatus;

    @Column(name = "profile_score")
    private Double profileScore;

    @Column(name = "knowledge_entropy")
    private Double knowledgeEntropy;

    @Column(name = "graph_edit_distance")
    private Double graphEditDistance;

    @Column(name = "activity_score")
    private Double activityScore;

    @Column(name = "profile_type", length = 50)
    private String profileType;

    @Column(name = "ability_level", length = 10)
    private String abilityLevel;

    @Column(name = "personality", length = 500)
    private String personality;

    @Column(name = "academic_tags", length = 500)
    private String academicTags;

    @Column(name = "practice_tags", length = 500)
    private String practiceTags;

    @Column(name = "suggestion_tags", length = 500)
    private String suggestionTags;

    @Column(name = "academic_trend_status", length = 20)
    private String academicTrendStatus;

    @Column(name = "academic_trend_desc", length = 200)
    private String academicTrendDesc;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
