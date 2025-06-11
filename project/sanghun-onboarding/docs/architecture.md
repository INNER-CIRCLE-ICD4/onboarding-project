# 🏗️ 프로젝트 아키텍처: 설문조사 API

## 📁 프로젝트 디렉토리 구조
---
src/
└── main/
├── java/
│ └── com/example/survey/
│ ├── controller/ # REST API 엔드포인트
│ ├── service/ # 비즈니스 로직
│ ├── repository/ # JPA 인터페이스 계층
│ ├── domain/ # 핵심 도메인 (Entity)
│ │ ├── survey/ # 설문 관련 도메인
│ │ └── response/ # 응답 관련 도메인
│ └── dto/ # 요청/응답 DTO 클래스
└── resources/
├── application.yml # H2 및 JPA 설정
├── data.sql # 초기 데이터 (선택)
└── schema.sql # 수동 스키마 (선택)

## 🧱 계층 구조 설명 (Layered Architecture)

- **Controller Layer**
  - 클라이언트 요청 수신 (REST API 제공)
  - DTO 변환 및 응답 반환
- **Service Layer**
  - 트랜잭션 관리
  - 비즈니스 로직 처리
- **Repository Layer**
  - JPA 기반 DB 접근
- **Domain Layer**
  - 설문, 질문, 응답 등 핵심 비즈니스 모델
- **DTO Layer**
  - Controller ↔ Service 간 데이터 전달 객체

---

## 🗃️ 기술 스택

| 영역 | 사용 기술 |
|------|-----------|
| Backend | Spring Boot 3.x |
| ORM | Spring Data JPA |
| DB | H2 (In-Memory) |
| Build Tool | Gradle |
| API 테스트 | Postman |
| 문서화 | Markdown (`/docs`) |

---

## 🔄 API 흐름 예시

1. 설문 생성 요청
   → `SurveyController.createSurvey()`
   → `SurveyService.saveSurvey()`
   → `SurveyRepository.save()`
   → DB 저장

2. 설문 응답 제출
   → `AnswerController.submit()`
   → `AnswerService.save()`
   → `AnswerRepository.saveAll()`

---

## 📌 기타

- H2 웹 콘솔: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`, 사용자: `sa`
- `ddl-auto: update` 설정으로 Entity 기반 자동 테이블 생성

---

필요하다면 `docs/api-spec.md` 파일 만들어서 **API 스펙 문서화**도 가능하고,  
ERD나 구조도 이미지도 `/docs/img` 폴더 만들어서 넣으면 좋아.

---

**👉 지금 바로 `/docs/architecture.md` 파일로 만들고 싶은 거면**, 내가 마크다운 파일 생성해줄게. 원할까?
