package com.edu.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * 活动关系
 */
@Data
public class ActivityRelation {
    @Property("role")
    private String role;

    @Property("date")
    private String date;

    @Property("semester")
    private String semester;
}
