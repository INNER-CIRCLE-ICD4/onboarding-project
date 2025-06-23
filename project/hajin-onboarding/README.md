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
    │   ├── in/web/       # REST 컨트롤러
    │   └── out/persistence/ # JPA 리포지토리
    ├── application/
    │   ├── port/         # 인터페이스 정의
    │   └── service/      # 비즈니스 로직
    └── domain/           # 도메인 엔티티 (Response, Answer)
```

### 아키텍처 원칙

- **헥사고날 아키텍처 (Ports & Adapters)**: 비즈니스 로직과 기술적 세부사항을 분리
- **기능별 패키징**: 기술 계층이 아닌 비즈니스 기능 중심으로 코드 구성
- **도메인 중심 설계**: 비즈니스 로직은 도메인 엔티티에 위치
- **명시적 트랜잭션 관리**: 서비스 계층에서만 트랜잭션 경계 설정

## API 목록

### 1. 설문조사 생성
- `POST /api/v1/surveys`
- 새로운 설문조사를 생성합니다.

### 2. 설문조사 수정
- `PUT /api/v1/surveys/{surveyId}`
- 기존 설문조사를 수정합니다. 기존 응답은 유지됩니다.

### 3. 설문조사 단건 조회
- `GET /api/v1/surveys/{surveyId}`
- 특정 설문조사를 조회합니다.

### 4. 설문조사 목록 조회
- `GET /api/v1/surveys`
- 설문조사 목록을 페이징하여 조회합니다.
- `?summary=true` 파라미터로 요약 정보만 조회 가능

### 5. 설문조사 응답 제출
- `POST /api/v1/surveys/{surveyId}/responses`
- 설문조사에 대한 응답을 제출합니다.

### 6. 응답 단건 조회
- `GET /api/v1/responses/{responseId}`
- 특정 응답을 조회합니다.

### 7. 설문조사별 응답 목록 조회
- `GET /api/v1/surveys/{surveyId}/responses`
- 특정 설문조사의 응답 목록을 페이징하여 조회합니다.
- `?summary=true` 파라미터로 요약 정보만 조회 가능
- **검색 기능 (Advanced)**:
  - `?questionTitle=검색어` - 질문 제목으로 검색 (부분 일치)
  - `?answerValue=검색어` - 응답 값으로 검색 (텍스트 응답 및 선택지 텍스트 부분 일치)
  - 두 파라미터를 함께 사용하여 AND 조건 검색 가능

자세한 API 명세는 [API 문서](docs/)를 참고하시거나 실행 후 `/swagger-ui.html`에서 확인하세요.

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
