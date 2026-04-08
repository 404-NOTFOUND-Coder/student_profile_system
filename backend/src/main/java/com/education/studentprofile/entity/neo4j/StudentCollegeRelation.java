package com.education.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Data
public class StudentCollegeRelation {

    @Id
    @GeneratedValue
    private Long id;

    @Property("type")
    private String type;

    @TargetNode
    private CollegeNode college;
}
