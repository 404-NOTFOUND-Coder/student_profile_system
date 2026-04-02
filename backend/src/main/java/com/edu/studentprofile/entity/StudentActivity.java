package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生活动实体类
 */
@Data
@TableName("student_activity")
public class StudentActivity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 活动类型: 1-竞赛, 2-科研, 3-社会实践, 4-学生工作, 5-志愿服务, 6-文体活动 */
    private Integer activityType;

    /** 活动名称 */
    private String activityName;

    /** 组织单位 */
    private String organization;

    /** 担任角色 */
    private String role;

    /** 活动日期 */
    private LocalDate activityDate;

    /** 所属学期 */
    private String semester;

    /** 活动描述 */
    private String description;

    /** 证书编号 */
    private String certificateNo;

    /** 级别: 1-院级, 2-校级, 3-省级, 4-国家级, 5-国际级 */
    private Integer level;

    /** 奖项类型: 1-一等奖, 2-二等奖, 3-三等奖, 4-优秀奖, 5-参与奖 */
    private Integer awardType;

    /** 获奖名称 */
    private String awardName;

    /** 评分权重 */
    private Double scoreWeight;

    /** 证明材料URL */
    private String evidenceUrl;

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

    /** 奖项类型名称 */
    @TableField(exist = false)
    private String awardTypeName;
}
