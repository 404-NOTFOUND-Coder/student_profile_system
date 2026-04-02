package com.edu.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * 知识域关系
 */
@Data
public class KnowledgeRelation {
    @Property("masteryLevel")
    private Double masteryLevel;

    @Property("score")
    private Double score;

    @Property("lastStudyDate")
    private String lastStudyDate;
}
