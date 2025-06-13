plugins {
    id("org.springframework.boot")       // ★ 반드시 있어야 developmentOnly DSL 생성
    id("io.spring.dependency-management")
    java
}

group = "com.survey"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(project(":survey-service"))
    implementation(project(":survey-core"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    developmentOnly("org.springframework.boot:spring-boot-devtools")  // IDE 인식 위해선 위 플러그인 必
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0") //spring boot 3이상 버전에서 가능
}
