package com.education.studentprofile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.education.studentprofile.repository.neo4j")
public class Neo4jConfig {
    // Neo4j configuration is handled by application.yml
    // This class enables Neo4j repositories
}
