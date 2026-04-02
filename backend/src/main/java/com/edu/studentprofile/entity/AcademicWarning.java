package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 学业预警实体类
 */
@Data
@TableName("academic_warning")
public class AcademicWarning {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 预警类型: 1-成绩预警, 2-学业困难, 3-学分预警, 4-毕业预警 */
    private Integer warningType;

    /** 预警等级: 1-一般, 2-较重, 3-严重 */
    private Integer warningLevel;

    /** 预警信息 */
    private String warningMessage;

    /** 触发条件 */
    private String triggerCondition;

    /** 建议措施 */
    private String suggestAction;

    /** 处理状态: 0-未处理, 1-处理中, 2-已解决, 3-忽略 */
    private Integer status;

    /** 处理时间 */
    private LocalDateTime handleTime;

    /** 处理人 */
    private String handler;

    /** 处理结果 */
    private String handleResult;

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
