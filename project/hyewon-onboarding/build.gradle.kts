import org.gradle.api.tasks.testing.Test
import org.gradle.api.JavaVersion

plugins {
  id("org.springframework.boot") version "3.2.5"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
  id("org.jetbrains.kotlin.plugin.spring") version "1.9.0"
}

group = "com.survey"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.core:jackson-databind")

  // Kotlin specific
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")

  // h2 Database
  runtimeOnly("com.h2database:h2")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}