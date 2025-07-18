dependencies {
    // JSON 직렬화/역직렬화
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    
    // 분산 ID 생성 (Snowflake 방식) - 우대사항: 다중 인스턴스 고려
    implementation("de.huxhorn.sulky:de.huxhorn.sulky.ulid:8.3.0")
    
    // 유틸리티
    implementation("org.apache.commons:commons-lang3:3.14.0")
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
