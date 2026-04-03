package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 活动实体类
 * 用于管理系统中的各类活动信息
 */
@Data
@TableName("activity")
public class Activity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 活动名称 */
    private String name;

    /** 活动类型: 1-竞赛, 2-科研, 3-社会实践, 4-学生工作, 5-志愿服务, 6-文体活动, 7-培训, 8-资格认证 */
    private Integer activityType;

    /** 一级分类: 比赛获奖, 培训经历, 工作经历, 学生活动, 个人荣誉, 资格认证 */
    private String category;

    /** 二级分类: 创新创业, 科技创新, 劳动实践, 思想成长, 体育活动, 文化艺术, 心理成长, 学术研究 */
    private String subCategory;

    /** 活动级别: 1-院级, 2-校级, 3-省部级, 4-国家级, 5-国际级 */
    private Integer level;

    /** 主办单位 */
    private String organizer;

    /** 承办单位 */
    private String coOrganizer;

    /** 活动描述 */
    private String description;

    /** 面向对象 */
    private String targetAudience;

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

    /** 报名人数上限 */
    private Integer maxParticipants;

    /** 当前报名人数 */
    private Integer currentParticipants;

    /** 奖励/收获 */
    private String reward;

    /** 参赛要求 */
    private String requirements;

    /** 联系方式 */
    private String contact;

    /** 活动海报URL */
    private String posterUrl;

    /** 活动详情URL */
    private String detailUrl;

    /** 是否开启推荐: 0-否, 1-是 */
    private Integer enableRecommend;

    /** 推荐权重 */
    private Double recommendWeight;

    /** 是否推荐给新人: 0-否, 1-是 */
    private Integer recommendToNewbie;

    /** 是否推荐给竞赛型学生: 0-否, 1-是 */
    private Integer recommendToCompetitionType;

    /** 是否推荐给科研型学生: 0-否, 1-是 */
    private Integer recommendToResearchType;

    /** 是否推荐给创业型学生: 0-否, 1-是 */
    private Integer recommendToStartupType;

    /** 状态: draft-草稿, published-已发布, ongoing-进行中, ended-已结束, cancelled-已取消 */
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
    
    /** 分类名称 */
    @TableField(exist = false)
    private String categoryName;
    
    /** 状态名称 */
    @TableField(exist = false)
    private String statusName;
    
    /** 是否可报名 */
    @TableField(exist = false)
    private Boolean canRegister;
}
