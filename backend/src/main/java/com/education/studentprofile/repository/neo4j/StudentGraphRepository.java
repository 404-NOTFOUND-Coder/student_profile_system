package com.education.studentprofile.repository.neo4j;

import com.education.studentprofile.entity.neo4j.StudentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentGraphRepository extends Neo4jRepository<StudentNode, Long> {

    Optional<StudentNode> findByStudentNo(String studentNo);

    @Query("MATCH (s:Student)-[:PARTICIPATED]->(a:Activity) WHERE s.studentNo = $studentNo RETURN s, a")
    Object findStudentWithActivities(String studentNo);
}
