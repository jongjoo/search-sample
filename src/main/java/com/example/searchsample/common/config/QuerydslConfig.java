package com.example.searchsample.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * QueryDsl Config
 */
@Configuration
public class QuerydslConfig {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * QueryDsl Config
     */
    public QuerydslConfig() {
    }

    /**
     * QueryDsl Factory Bean
     *
     * @return the jpa query factory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(this.entityManager);
    }
}
