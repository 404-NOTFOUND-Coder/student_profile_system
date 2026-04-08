package com.education.studentprofile.repository.neo4j;

import com.education.studentprofile.entity.neo4j.ActivityNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityGraphRepository extends Neo4jRepository<ActivityNode, Long> {

    Optional<ActivityNode> findByName(String name);
}
