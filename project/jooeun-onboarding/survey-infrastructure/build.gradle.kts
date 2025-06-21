dependencies {
    // 모듈 의존성
    implementation(project(":survey-common"))
    implementation(project(":survey-domain"))
    
    // Database (인메모리 H2)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    
    // Query DSL (복잡한 검색 기능용)
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    
    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testImplementation("com.h2database:h2")
}

// JAR 파일 생성 설정
tasks.jar {
    enabled = true
    archiveClassifier = ""
}

// Spring Boot JAR 생성 비활성화 (라이브러리 모듈이므로)
tasks.bootJar {
    enabled = false
}
