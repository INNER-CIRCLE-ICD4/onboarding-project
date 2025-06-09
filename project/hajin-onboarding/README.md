# 설문조사 서비스 (Survey Service)

이너써클 백엔드 온보딩 프로젝트 - 설문조사 서비스 구현

## 프로젝트 개요

Google Forms와 유사한 설문조사 서비스를 구현합니다. 사용자는 설문조사를 생성하고, 수정하며, 응답을 수집하고 조회할 수 있습니다.

## 기술 스택

- **Language**: Kotlin 2.0.0
- **Framework**: Spring Boot 3.3.5
- **Java**: 17
- **Build Tool**: Gradle
- **Database**: H2 (In-memory)
- **ORM**: Spring Data JPA
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Testing**: Kotest, MockK, REST Assured, Testcontainers
- **Code Quality**: ktlint

## 프로젝트 구조

```
src/main/kotlin/com/innercircle/survey/
├── common/                 # 공통 기능
│   ├── adapter/           # 전역 어댑터 (예외 처리 등)
│   ├── config/            # 설정 클래스
│   ├── domain/            # 공통 도메인 (BaseEntity 등)
│   ├── dto/               # 공통 DTO
│   ├── exception/         # 예외 정의
│   └── utils/             # 유틸리티
├── survey/                # 설문조사 기능
│   ├── adapter/
│   │   ├── in/web/       # REST 컨트롤러
│   │   └── out/persistence/ # JPA 리포지토리
│   ├── application/
│   │   ├── port/         # 인터페이스 정의
│   │   └── service/      # 비즈니스 로직
│   └── domain/           # 도메인 엔티티
└── response/             # 응답 기능
    ├── adapter/
    ├── application/
    └── domain/
```

### 아키텍처 원칙

- **헥사고날 아키텍처 (Ports & Adapters)**: 비즈니스 로직과 기술적 세부사항을 분리
- **기능별 패키징**: 기술 계층이 아닌 비즈니스 기능 중심으로 코드 구성
- **도메인 중심 설계**: 비즈니스 로직은 도메인 엔티티에 위치
- **명시적 트랜잭션 관리**: 서비스 계층에서만 트랜잭션 경계 설정

## 실행 방법

### 로컬 환경에서 실행

```bash
# 프로젝트 디렉토리로 이동
cd project/hajin-onboarding

# 빌드
./gradlew clean build

# 실행
./gradlew bootRun
```

## 💬 문의사항

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.

---
*Last Updated: 2025년 6월*
