// common/build.gradle.kts
plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.springframework:spring-web:6.0.11")   // 사용 중인 Spring Boot 3.5.x 에 맞는 버전
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
