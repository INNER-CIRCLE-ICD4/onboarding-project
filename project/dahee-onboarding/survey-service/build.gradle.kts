plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}


dependencies {
    implementation(project(":survey-core"))
    implementation(project(":common"))

    // Boot 플러그인이 spring-boot-dependencies BOM을 자동 import 해서
    // 버전 없이 선언해도 됩니다
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
