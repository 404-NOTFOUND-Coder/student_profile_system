package com.edu.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * 奖项关系
 */
@Data
public class AwardRelation {
    @Property("awardName")
    private String awardName;

    @Property("level")
    private String level;

    @Property("date")
    private String awardDate;

    @Property("weight")
    private Double weight;
}
