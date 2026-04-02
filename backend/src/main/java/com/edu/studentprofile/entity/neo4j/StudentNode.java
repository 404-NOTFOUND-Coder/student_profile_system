package com.edu.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

/**
 * Neo4j学生画像节点
 */
@Data
@Node(labels = "Student")
public class StudentNode {

    @Id
    @GeneratedValue
    private String id;

    @Property("studentNo")
    private String studentNo;

    @Property("name")
    private String name;

    @Property("gender")
    private String gender;

    @Property("enrollmentYear")
    private Integer enrollmentYear;

    @Property("college")
    private String college;

    @Property("major")
    private String major;

    @Property("educationLevel")
    private String educationLevel;

    @Property("profileType")
    private String profileType;

    @Property("knowledgeEntropy")
    private Double knowledgeEntropy;

    @Property("graphEditDistance")
    private Double graphEditDistance;

    @Property("abilityLevel")
    private String abilityLevel;

    @Property("personality")
    private String personality;

    @Property("tags")
    private List<String> tags;

    @Property("gpa")
    private Double gpa;

    @Property("activityScore")
    private Double activityScore;

    // 关系定义
    @Relationship(type = "ENROLLED", direction = Relationship.Direction.OUTGOING)
    private List<CourseRelation> enrolledCourses;

    @Relationship(type = "MASTERS", direction = Relationship.Direction.OUTGOING)
    private List<KnowledgeRelation> masteredKnowledge;

    @Relationship(type = "PARTICIPATES", direction = Relationship.Direction.OUTGOING)
    private List<ActivityRelation> activities;

    @Relationship(type = "WINS", direction = Relationship.Direction.OUTGOING)
    private List<AwardRelation> awards;
}
