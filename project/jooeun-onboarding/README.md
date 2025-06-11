#  Survey Service - 설문조사 서비스

##  프로젝트 개요

설문조사 양식을 만들고, 만들어진 양식을 기반으로 응답을 받을 수 있는 서비스입니다.

## 🏗 아키텍처

### 멀티모듈 구조 (우대사항 적용)
```
jooeun-onboarding/
├── survey-api/              # 웹 레이어 (REST API, 문서화)
├── survey-domain/           # 도메인 로직 (엔티티, 비즈니스 규칙)
├── survey-infrastructure/   # 데이터 액세스 (Repository, JPA)
└── survey-common/          # 공통 유틸리티 (ULID, 상수)
```

### 의존성 방향 제약
```
survey-api → survey-infrastructure → survey-domain → survey-common
```
각 모듈은 하위 레벨 모듈만 의존하며, 순환 의존성이 발생하지 않도록 설계했습니다.

## ️ 사용된 기술 스택 및 오픈소스

### **Core Framework**
| 라이브러리 | 버전 | 사용 목적 | 요구사항 |
|-----------|------|----------|---------|
| **Spring Boot** | 3.2.0 | • 애플리케이션 기본 프레임워크<br>• 자동 설정을 통한 개발 생산성 향상 | ✅ 필수 |
| **Spring Data JPA** | 3.2.0 | • 데이터베이스 접근 계층 추상화<br>• JPA 기반 데이터 처리 | ✅ 필수 |
| **QueryDSL** | 5.0.0 | • 타입 안전한 동적 쿼리 생성<br>• Advanced 검색 기능 구현 | 🔍 Advanced |

### **Database**
| 라이브러리 | 버전 | 사용 목적 | 요구사항 |
|-----------|------|----------|---------|
| **H2 Database** | Latest | • 인메모리 데이터베이스<br>• 개발/테스트 환경용 | ✅ 필수 |

### **ID Generation**
| 라이브러리 | 버전 | 사용 목적 | 요구사항 |
|-----------|------|----------|---------|
| **Sulky ULID** | 8.3.0 | • 분산 환경에서 고유 ID 생성<br>• 시간 기반 정렬 가능<br>• 다중 인스턴스에서 충돌 방지 | 🏆 우대사항 |

### **API Documentation**
| 라이브러리 | 버전 | 사용 목적 | 요구사항 |
|-----------|------|----------|---------|
| **SpringDoc OpenAPI** | 2.2.0 | • API 문서 자동 생성<br>• Swagger UI 제공<br>• API 명세 제출 요구사항 충족 | ✅ 필수 |

### **Validation & Utility**
| 라이브러리 | 버전 | 사용 목적 | 요구사항 |
|-----------|------|----------|---------|
| **Bean Validation** | 3.0 | • 요청 데이터 검증<br>• 비즈니스 규칙 검증 | ✅ 필수 |
| **Apache Commons Lang** | 3.14.0 | • 문자열, 유틸리티 함수<br>• 코드 간소화 | 🔧 유틸리티 |
| **Lombok** | Latest | • 보일러플레이트 코드 제거<br>• 코드 가독성 향상 | 🔧 유틸리티 |

### **Monitoring**
| 라이브러리 | 버전 | 사용 목적 | 요구사항 |
|-----------|------|----------|---------|
| **Spring Boot Actuator** | 3.2.0 | • 애플리케이션 헬스체크<br>• 운영 환경 모니터링 | 🏆 우대사항 |

### **Testing**
| 라이브러리 | 버전 | 사용 목적 | 요구사항 |
|-----------|------|----------|---------|
| **JUnit 5** | 5.10.1 | • 단위 테스트 프레임워크<br>• 파라미터화 테스트, Nested 테스트 | ✅ 필수 |
| **AssertJ** | 3.24.2 | • 유창한 API의 assertion 라이브러리<br>• 가독성 높은 테스트 코드 작성 | ✅ 필수 |
| **JaCoCo** | 0.8.12 | • 테스트 커버리지 측정<br>• 코드 품질 관리 | 🧪 품질향상 |
| **Spring Boot Test** | 3.2.0 | • 통합 테스트 프레임워크<br>• Mock 기반 테스트 | ✅ 필수 |
| **TestContainers** | 1.19.3 | • 격리된 테스트 환경<br>• 실제 DB와 유사한 테스트 | 🧪 품질향상 |
| **Rest Assured** | 5.3.2 | • API 테스트<br>• End-to-End 테스트 | 🧪 품질향상 |

##  향후 확장을 위한 제거된 라이브러리 적용 방법

### **운영 환경 데이터베이스 (PostgreSQL)**
```groovy
// survey-infrastructure/build.gradle.kts
dependencies {
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
}
```

**적용 시점**: 운영 배포 시
**적용 이유**: 
- H2는 개발/테스트용, PostgreSQL은 운영환경 안정성
- Flyway로 스키마 버전 관리 및 마이그레이션 자동화

### **분산 캐싱 및 세션 관리 (Redis)**
```groovy
// survey-infrastructure/build.gradle.kts
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.redisson:redisson-spring-boot-starter:3.25.2")
    implementation("org.springframework.session:spring-session-data-redis")
}
```

**적용 시점**: 다중 인스턴스 배포 시
**적용 이유**: 
- 세션 외부화로 로드밸런서 환경 지원
- Redisson으로 분산 락 구현 (동시성 이슈 해결)
- 응답 집계 결과 캐싱으로 성능 향상

### **분산 추적 및 모니터링 (Zipkin)**
```groovy
// survey-api/build.gradle.kts
dependencies {
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("io.micrometer:micrometer-registry-prometheus")
}
```

**적용 시점**: 마이크로서비스 분리 시
**적용 이유**: 
- 서비스 간 요청 흐름 추적
- 성능 병목 지점 분석
- Prometheus + Grafana 연동

### **복원력 패턴 (Resilience4j)**
```groovy
// survey-api/build.gradle.kts
dependencies {
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
    implementation("io.github.resilience4j:resilience4j-reactor:2.2.0")
}
```

**적용 시점**: 외부 API 연동 시 (이메일, SMS 등)
**적용 이유**: 
- Circuit Breaker로 장애 전파 방지
- Retry 패턴으로 일시적 장애 극복
- Bulkhead로 자원 격리

### **Rate Limiting (Bucket4j)**
```groovy
// survey-api/build.gradle.kts
dependencies {
    implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:7.6.0")
    implementation("com.github.vladimir-bukhtoyarov:bucket4j-redis:7.6.0")
}
```

**적용 시점**: 공개 API 제공 시
**적용 이유**: 
- API 남용 방지
- 사용자별/IP별 요청 제한
- DDoS 공격 대응


## 🚀 실행 방법

### **개발 환경 실행**
```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew :survey-api:bootRun
```

### **접속 정보**
- **애플리케이션**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health

## 📝 API 명세

### **주요 엔드포인트**
| Method | Endpoint | 설명 |
|--------|----------|------|
| `POST` | `/api/surveys` | 설문조사 생성 |
| `PUT` | `/api/surveys/{id}` | 설문조사 수정 |
| `POST` | `/api/surveys/{id}/responses` | 응답 제출 |
| `GET` | `/api/surveys/{id}/responses` | 응답 조회 |
| `GET` | `/api/surveys/{id}/responses/search` | 응답 검색 (Advanced) |

*상세한 API 명세는 Swagger UI에서 확인 가능합니다.*


## 📊 성능 최적화 전략

### **현재 적용된 최적화**
- **ULID 기반 ID**: 분산 환경에서 충돌 없는 고유 ID 생성
- **QueryDSL**: 복잡한 검색 조건의 동적 쿼리 최적화
- **JPA 최적화**: Lazy Loading, Fetch Join 활용

### **향후 적용 예정 최적화**
- **데이터베이스 인덱싱**: 검색 컬럼 복합 인덱스
- **Redis 캐싱**: 설문조사 메타데이터 캐시
- **Connection Pool 튜닝**: HikariCP 설정 최적화
