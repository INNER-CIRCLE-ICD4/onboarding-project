package com.survey.daheeonboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "com.survey.daheeonboarding", // API & Controller & DTO
                "com.survey.service",          // Service 계층
                "com.survey.core"              // Core(Entity, Repository)
        }
)
@EnableJpaRepositories(basePackages = "com.survey.core.repository")
@EntityScan(basePackages = "com.survey.core.entity")
public class DaheeOnboardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaheeOnboardingApplication.class, args);
    }

}
