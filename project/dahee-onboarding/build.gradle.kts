plugins {
    // 선언만, 서브모듈에서 apply
    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    java
}

allprojects {
    group = "com.survey"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
