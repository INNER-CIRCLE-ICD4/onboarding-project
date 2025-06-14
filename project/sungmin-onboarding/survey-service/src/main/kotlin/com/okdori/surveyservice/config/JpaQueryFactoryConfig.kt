package com.okdori.surveyservice.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * author       : okdori
 * date         : 2025. 6. 10.
 * description  :
 */

@Configuration
class JpaQueryFactoryConfig {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}
