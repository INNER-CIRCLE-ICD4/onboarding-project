plugins {
    id("java")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
}