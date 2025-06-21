plugins {
    java
    jacoco
}

dependencies {
    // JSON 직렬화/역직렬화
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // JPA & Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // 분산 ID 생성 (Snowflake 방식) - 우대사항: 다중 인스턴스 고려
    implementation("de.huxhorn.sulky:de.huxhorn.sulky.ulid:8.3.0")
    
    // 유틸리티
    implementation("org.apache.commons:commons-lang3:3.14.0")
    
    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// JAR 파일 생성 설정
tasks.jar {
    enabled = true
    archiveClassifier.set("")
}

// Spring Boot JAR 생성 비활성화 (라이브러리 모듈이므로)
tasks.bootJar {
    enabled = false
}

// 테스트 설정
tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// 테스트 커버리지 설정
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
