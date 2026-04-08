package com.education.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node(labels = "Student")
@Data
public class StudentNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property("studentNo")
    private String studentNo;

    @Property("name")
    private String name;

    @Property("college")
    private String college;

    @Property("major")
    private String major;

    @Property("profileType")
    private String profileType;
}
