package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生实体类 - MySQL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_student")
public class Student {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学号 (唯一) */
    private String studentNo;

    /** 姓名 */
    private String name;

    /** 性别: 0-未知, 1-男, 2-女 */
    private Integer gender;

    /** 出生日期 */
    private LocalDate birthday;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 民族 */
    private String nationality;

    /** 政治面貌 */
    private String politicalStatus;

    /** 入学年份 */
    private Integer enrollmentYear;

    /** 入学月份 */
    private Integer enrollmentMonth;

    /** 学院 */
    private String college;

    /** 专业 */
    private String major;

    /** 班级 */
    private String className;

    /** 学历层次: 1-本科, 2-硕士, 3-博士 */
    private Integer educationLevel;

    /** 学籍状态: 1-在读, 2-休学, 3-退学, 4-毕业 */
    private Integer studentStatus;

    /** 籍贯 */
    private String hometown;

    /** 家庭住址 */
    private String address;

    /** 头像URL */
    private String avatarUrl;

    /** 学生标签 (JSON数组) */
    private String tags;

    /** 综合评分 */
    private Double profileScore;

    /** 逻辑删除标记 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ==================== 非数据库字段 ====================
    /** 知识结构熵 (从Neo4j获取) */
    @TableField(exist = false)
    private Double knowledgeEntropy;

    /** 图编辑距离 (从Neo4j获取) */
    @TableField(exist = false)
    private Double graphEditDistance;

    /** 画像类型 */
    @TableField(exist = false)
    private String profileType;

    /** 能力等级 */
    @TableField(exist = false)
    private String abilityLevel;

    /** 人格特征 */
    @TableField(exist = false)
    private String personality;

    /** 平均绩点 */
    @TableField(exist = false)
    private Double gpa;

    /** 活动得分 */
    @TableField(exist = false)
    private Double activityScore;

    /** 课程成绩列表 */
    @TableField(exist = false)
    private List<AcademicScore> scores;

    /** 活动列表 */
    @TableField(exist = false)
    private List<StudentActivity> activities;

    /** 画像标签列表 */
    @TableField(exist = false)
    private List<ProfileTag> profileTags;
}
