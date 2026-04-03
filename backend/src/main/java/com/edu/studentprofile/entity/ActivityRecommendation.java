package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 活动推荐实体类
 * 基于学生画像推荐相关活动
 */
@Data
@TableName("activity_recommendation")
public class ActivityRecommendation {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 学生学号 */
    private String studentNo;

    /** 活动ID */
    private Long activityId;

    /** 活动名称 */
    private String activityName;

    /** 活动类型: 1-竞赛, 2-科研, 3-社会实践, 4-学生工作, 5-志愿服务, 6-文体活动, 7-培训, 8-资格认证 */
    private Integer activityType;

    /** 一级分类: 比赛获奖, 培训经历, 工作经历, 学生活动, 个人荣誉, 资格认证 */
    private String category;

    /** 二级分类: 创新创业, 科技创新, 劳动实践, 思想成长, 体育活动, 文化艺术, 心理成长, 学术研究 */
    private String subCategory;

    /** 推荐分数: 0-100 */
    private Double recommendScore;

    /** 推荐理由 */
    private String recommendReason;

    /** 匹配维度: profile_match-画像匹配, history_match-历史相似, similar_student-同类学生 */
    private String matchDimension;

    /** 活动级别: 院级, 校级, 省部级, 国家级, 国际级 */
    private String level;

    /** 主办单位 */
    private String organizer;

    /** 报名开始时间 */
    private LocalDate registerStartTime;

    /** 报名截止时间 */
    private LocalDate registerEndTime;

    /** 活动开始时间 */
    private LocalDate activityStartTime;

    /** 活动结束时间 */
    private LocalDate activityEndTime;

    /** 活动地点 */
    private String location;

    /** 面向对象 */
    private String targetAudience;

    /** 奖励/收获 */
    private String reward;

    /** 是否已报名: 0-否, 1-是 */
    private Integer isRegistered;

    /** 是否已完成: 0-否, 1-是 */
    private Integer isCompleted;

    /** 推荐状态: pending-待处理, viewed-已查看, interested-感兴趣, applied-已报名, not_interested-不感兴趣 */
    private String status;

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
    
    /** 活动类型名称 */
    @TableField(exist = false)
    private String activityTypeName;
    
    /** 级别名称 */
    @TableField(exist = false)
    private String levelName;
    
    /** 推荐状态名称 */
    @TableField(exist = false)
    private String statusName;
    
    /** 剩余报名天数 */
    @TableField(exist = false)
    private Integer remainingDays;
}
