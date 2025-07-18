dependencies {
    // 모듈 의존성
    implementation(project(":survey-common"))
    
    // JPA & Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    
    // Bean Validation 2.0 - 데이터 검증 필수
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
