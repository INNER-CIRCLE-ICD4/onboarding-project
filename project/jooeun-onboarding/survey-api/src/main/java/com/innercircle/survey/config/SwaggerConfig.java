package com.innercircle.survey.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 설정
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("설문조사 API")
                        .version("1.0.0")
                        .description("""
                                설문조사 생성, 수정, 응답 수집을 위한 REST API입니다.
                                
                                **주요 기능:**
                                - 설문조사 CRUD (생성, 조회, 수정, 비활성화)
                                - 응답 수집 및 조회
                                - 기존 응답 보존을 위한 3중 보호 메커니즘
                                - 생성자 권한 기반 수정/삭제 제어
                                
                                **데이터 일관성 보장:**
                                - JPA 낙관적 락으로 동시성 제어
                                - Soft Delete로 기존 응답 참조 보존
                                - 완전한 스냅샷으로 응답 맥락 보존
                                """)
                        .contact(new Contact()
                                .name("Survey API Team")
                                .email("support@survey-api.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("로컬 개발 서버"),
                        new Server()
                                .url("https://api.survey.company.com")
                                .description("운영 서버")
                ));
    }
}
