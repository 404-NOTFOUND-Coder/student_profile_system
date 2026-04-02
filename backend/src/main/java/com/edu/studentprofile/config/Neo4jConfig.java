package com.edu.studentprofile.config;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Neo4j配置类
 */
@Configuration
public class Neo4jConfig {

    /**
     * 配置Neo4j事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager(Driver driver, 
                                                          DatabaseSelectionProvider databaseSelectionProvider) {
        return new Neo4jTransactionManager(driver, databaseSelectionProvider);
    }
}
