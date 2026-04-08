package com.education.studentprofile.repository.neo4j;

import com.education.studentprofile.entity.neo4j.MajorNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorGraphRepository extends Neo4jRepository<MajorNode, Long> {

    Optional<MajorNode> findByName(String name);
}
