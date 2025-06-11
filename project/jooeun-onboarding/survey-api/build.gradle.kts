dependencies {
    // 모듈 의존성
    implementation(project(":survey-common"))
    implementation(project(":survey-domain"))
    implementation(project(":survey-infrastructure"))
    
    // Web & API
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // API 문서화 (Swagger/OpenAPI) - API 명세 제출 요구사항
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    
    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testImplementation("com.h2database:h2")
    testImplementation("io.rest-assured:rest-assured:5.3.2")
}

// 메인 애플리케이션이므로 Spring Boot JAR 생성
tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true
    archiveClassifier = ""
}
