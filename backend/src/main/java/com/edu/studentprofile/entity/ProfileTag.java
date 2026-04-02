package com.edu.studentprofile.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 学生画像标签实体类
 */
@Data
@TableName("profile_tag")
public class ProfileTag {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 标签分类: personality, ability, behavior, interest */
    private String tagCategory;

    /** 标签名称 */
    private String tagName;

    /** 标签值 */
    private String tagValue;

    /** 置信度: 0-1 */
    private Double confidence;

    /** 来源: knowledge_graph, behavior_analysis, manual */
    private String source;

    /** 证据 */
    private String evidence;

    /** 衍生时间 */
    private LocalDateTime derivedAt;

    /** 过期时间 */
    private LocalDateTime expiredAt;

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
