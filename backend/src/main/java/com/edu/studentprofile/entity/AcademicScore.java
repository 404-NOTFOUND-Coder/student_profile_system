package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成绩实体类
 */
@Data
@TableName("academic_score")
public class AcademicScore {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 课程代码 */
    private String courseCode;

    /** 课程名称 */
    private String courseName;

    /** 课程类型: 1-必修, 2-选修, 3-公选 */
    private Integer courseType;

    /** 学分 */
    private BigDecimal credit;

    /** 学期: 2023-1 */
    private String semester;

    /** 成绩 */
    private BigDecimal score;

    /** 成绩类型: 1-百分制, 2-等级制 */
    private Integer scoreType;

    /** 班级排名 */
    private Integer rankInClass;

    /** 专业排名 */
    private Integer rankInMajor;

    /** 考试类型: 1-正常, 2-补考, 3-重修 */
    private Integer examType;

    /** 是否挑战性课程 */
    private Integer isChallenge;

    /** 绩点 */
    private BigDecimal gpa;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
