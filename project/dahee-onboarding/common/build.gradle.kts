// survey-common/build.gradle.kts
plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    // 공통 DTO 검증용
    api("jakarta.validation:jakarta.validation-api:3.0.2")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
