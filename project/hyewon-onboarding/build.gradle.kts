plugins {
  id 'org.springframework.boot' version '2.7.10'
  id 'io.spring.dependency-management' version '1.0.11.RELEASE'
  id 'java'
}

group = 'com.survey'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'
targetCompatibility = '17'

repositories {
  mavenCentral()
}

dependencies {
  // Spring Web: REST API
  implementation 'org.springframework.boot:spring-boot-starter-web'

  // JSON 직렬화/역직렬화를 위한 Jackson
  implementation 'com.fasterxml.jackson.core:jackson-databind'

  // (선택) DB 연동이 필요하면 JPA + H2 등 추가
  // implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
  // runtimeOnly 'com.h2database:h2'

  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
  useJUnitPlatform()
}