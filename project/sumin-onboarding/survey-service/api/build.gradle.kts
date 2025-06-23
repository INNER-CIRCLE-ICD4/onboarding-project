plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("java")
    id("application")
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    runtimeOnly("com.h2database:h2")

}
application {
    mainClass.set("com.innercircle.survey.SurveyServiceApplication")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("com.innercircle.survey.SurveyServiceApplication")
}
