package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 知识图谱实体类
 * 用于存储学生相关的静态和动态知识表示
 * 
 * 静态实体：学号、院系、专业、性别、年级、班级、生源地
 * 动态实体：比赛获奖、培训经历、工作经历、学生活动、个人荣誉、资格认证
 * 
 * 采用四元组表示：(头实体, 关系, 尾实体/成果名称, 时间)
 */
@Data
@TableName("knowledge_graph")
public class KnowledgeGraph {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 头实体类型: student, college, major, course, activity, etc. */
    private String headType;

    /** 头实体ID */
    private String headId;

    /** 头实体名称 */
    private String headName;

    /** 关系类型: static_relation, dynamic_relation */
    private String relationType;

    /** 一级关系: 隶属院系, 隶属专业, 参加活动, 获得奖项, etc. */
    private String relation;

    /** 二级关系(可选): 科技创新, 学术研究, etc. */
    private String subRelation;

    /** 尾实体类型 */
    private String tailType;

    /** 尾实体ID */
    private String tailId;

    /** 尾实体名称/成果名称 */
    private String tailName;

    /** 发生时间 */
    private LocalDateTime eventTime;

    /** 时间维度值(如: 2022-05) */
    private String timeDimension;

    /** 活动级别: 院级, 校级, 省部级, 国家级, 国际级 */
    private String activityLevel;

    /** 奖项等级: 特等奖, 一等奖, 二等奖, 三等奖, 参与奖 */
    private String awardLevel;

    /** 置信度: 0-1 */
    private Double confidence;

    /** 来源: manual, system, knowledge_base */
    private String source;

    /** 证据/备注 */
    private String evidence;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ==================== 非数据库字段 ====================
    
    /** 关系类型名称映射 */
    @TableField(exist = false)
    private String relationTypeName;
    
    /** 活动级别名称 */
    @TableField(exist = false)
    private String activityLevelName;
    
    /** 奖项等级名称 */
    @TableField(exist = false)
    private String awardLevelName;
    
    /** 四元组字符串表示 */
    @TableField(exist = false)
    private String quadString;
}
