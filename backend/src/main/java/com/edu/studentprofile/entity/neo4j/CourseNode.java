package com.edu.studentprofile.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

/**
 * Neo4j课程节点
 */
@Data
@Node(labels = "Course")
public class CourseNode {

    @Id
    @GeneratedValue
    private String id;

    @Property("courseCode")
    private String courseCode;

    @Property("courseName")
    private String courseName;

    @Property("credit")
    private Double credit;

    @Property("courseType")
    private String courseType;

    @Property("knowledgeArea")
    private String knowledgeArea;

    @Property("difficultyLevel")
    private Integer difficultyLevel;

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private List<KnowledgeNode> containsKnowledge;
}

/**
 * Neo4j知识域节点
 */
@Data
@Node(labels = "Knowledge")
class KnowledgeNode {

    @Id
    @GeneratedValue
    private String id;

    @Property("knowledgeArea")
    private String knowledgeArea;

    @Property("knowledgePoint")
    private String knowledgePoint;

    @Property("difficultyLevel")
    private Integer difficultyLevel;

    @Property("masteryLevel")
    private Double masteryLevel;
}

/**
 * Neo4j活动节点
 */
@Data
@Node(labels = "Activity")
class ActivityNode {

    @Id
    @GeneratedValue
    private String id;

    @Property("activityId")
    private String activityId;

    @Property("activityName")
    private String activityName;

    @Property("activityType")
    private String activityType;

    @Property("level")
    private String level;

    @Property("date")
    private String date;

    @Property("role")
    private String role;

    @Property("description")
    private String description;
}

/**
 * Neo4j奖项节点
 */
@Data
@Node(labels = "Award")
class AwardNode {

    @Id
    @GeneratedValue
    private String id;

    @Property("awardId")
    private String awardId;

    @Property("awardName")
    private String awardName;

    @Property("awardType")
    private String awardType;

    @Property("level")
    private String level;

    @Property("date")
    private String date;

    @Property("weight")
    private Double weight;
}
