package com.education.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node(labels = "Activity")
@Data
public class ActivityNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property("name")
    private String name;

    @Property("type")
    private String type;

    @Property("level")
    private String level;
}
