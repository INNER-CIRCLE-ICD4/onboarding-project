# 설문조사 서비스 (Survey Service)

## 📋 프로젝트 개요

이너써클 백엔드 온보딩 프로젝트로 구현한 설문조사 서비스입니다. 사용자가 설문조사 양식을 생성하고, 응답을 수집하며, 결과를 조회할 수 있는 RESTful API 서비스입니다.

## 🛠 기술 스택

### 핵심 기술
- **Language**: Kotlin 2.0.0
- **Framework**: Spring Boot 3.3.5
- **Java Version**: 21 LTS
- **Build Tool**: Gradle 9.x (Kotlin DSL)

### 주요 의존성
- **Spring Boot Starter Web**: REST API 개발
- **Spring Boot Starter Data JPA**: 데이터 영속성 관리
- **Spring Boot Starter Validation**: 요청 데이터 검증
- **H2 Database**: 인메모리 데이터베이스
- **SpringDoc OpenAPI**: API 문서 자동화 (Swagger UI)
- **Jackson Kotlin Module**: JSON 직렬화/역직렬화
- **Kotlin Logging**: 로깅 프레임워크

### 테스트 도구
- **Spring Boot Starter Test**: 통합 테스트
- **MockK**: Kotlin 전용 모킹 라이브러리
- **SpringMockK**: Spring 환경에서의 MockK 통합

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/innercircle/survey/
│   │       ├── SurveyApplication.kt          # 메인 애플리케이션
│   │       ├── config/                       # 설정 클래스
│   │       │   └── SurveyProperties.kt      # 애플리케이션 프로퍼티
│   │       ├── controller/                   # REST 컨트롤러
│   │       ├── service/                      # 비즈니스 로직
│   │       ├── repository/                   # 데이터 접근 계층
│   │       ├── domain/                       # 도메인 엔티티
│   │       ├── dto/                          # 데이터 전송 객체
│   │       └── exception/                    # 예외 처리
│   └── resources/
│       └── application.yml                   # 애플리케이션 설정
└── test/
    └── kotlin/                               # 테스트 코드
```

## 🚀 실행 방법

### 사전 요구사항
- JDK 21 이상
- Gradle 9.x 이상

### 애플리케이션 실행
```bash
# 프로젝트 디렉토리로 이동
cd project/hajin-onboarding

# Gradle Wrapper를 사용한 실행
./gradlew bootRun

```

## 💬 문의사항

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.

---
*Last Updated: 2025년 6월*