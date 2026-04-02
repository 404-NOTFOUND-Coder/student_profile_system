package com.edu.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * 课程关系
 */
@Data
public class CourseRelation {
    @Property("semester")
    private String semester;

    @Property("score")
    private Double score;

    @Property("gpa")
    private Double gpa;

    @Property("isChallenge")
    private Boolean isChallenge;
}
