package com.innercircle.survey.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA 설정
 * 
 * JPA Auditing과 Repository 기본 설정을 담당합니다.
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.innercircle.survey.infrastructure.repository")
public class JpaConfig {
    // JPA Auditing 자동 설정을 활성화하여 BaseEntity의 @CreatedDate, @LastModifiedDate 자동 처리
}
