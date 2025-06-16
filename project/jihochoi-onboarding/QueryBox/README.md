# 📋 설문조사 서비스 (Survey Service)

설문조사 생성 및 응답 수집 기능을 제공하는 도메인 기반 설문 서비스입니다.  
Google Forms, Tally 등과 유사한 기능을 목표로 하며, DDD 스타일로 작성됩니다.

---

## ✅ 주요 도메인 모델

### 📦 Entity 목록

| Entity | 설명 |
|--------|------|
| `Survey` | 설문 전체. 질문 목록을 포함한 루트 애그리거트 |
| `Question` | 하나의 문항. 입력 형태, 필수 여부 등 포함 |
| `Response` | 사용자의 응답. 여러 Answer를 포함 |
| `Answer` *(선택)* | 특정 Question에 대한 응답. 필요 시 Entity, 아니면 VO |

### 🧱 Value Object 목록

| Value Object | 설명 |
|--------------|------|
| `Option` | 선택형 문항의 선택지. 값만으로 동일성 판단 |
| `QuestionType` | 단답형, 장문형, 단일/다중 선택형 등 |
| `AnswerContent` *(선택)* | 텍스트 or 선택지 응답값을 캡슐화할 경우 |
| `QuestionName`, `QuestionDescription` *(선택)* | 값 타입으로 캡슐화 가능 |

---

## 🔒 도메인 불변식 (Invariant)

### Survey
- [ ] 제목은 공백일 수 없음
- [ ] 문항은 1~10개 사이여야 함
- [ ] 문항 이름은 중복되면 안 됨

### Question
- [ ] 이름은 공백일 수 없음
- [ ] 선택형 문항(SINGLE, MULTIPLE)은 옵션이 1개 이상 존재해야 함
- [ ] 주관식 문항(SHORT_TEXT, LONG_TEXT)은 옵션이 없어야 함
- [ ] 옵션 이름은 중복되면 안 됨

### Response
- [ ] 응답은 최소 하나 이상의 Answer를 포함해야 함
- [ ] 동일한 Question에 대해 중복 응답이 존재하면 안 됨

### Answer
- [ ] 주관식인 경우: text 값이 반드시 존재해야 함
- [ ] 선택형인 경우: 최소 하나 이상의 option ID가 포함되어야 함

---

## 🧩 기능 목록 (Feature To-do)

### 설문 관리

- [ ] 설문 생성 API
- [ ] 설문 수정 API
- [ ] 설문 삭제 API (선택)
- [ ] 설문 조회 API

### 문항 관리 (Survey 내 포함)

- [ ] 문항 추가
- [ ] 문항 수정
- [ ] 문항 삭제

### 응답 관리

- [ ] 설문 응답 제출 API
- [ ] 설문 응답 전체 조회 API
- [ ] 설문 응답 검색 (문항 이름 + 응답 값 기준)

### 도메인 이벤트

- [ ] `SurveyCreatedEvent` 등록 및 발행
- [ ] `ResponseSubmittedEvent` 등록 및 발행

---

## 🧪 테스트 전략

- 단위 테스트: Survey/Question 등 도메인 객체의 불변식 보장
- 통합 테스트: 설문 생성 → 응답 제출 → 응답 조회 플로우 검증
- 이벤트 테스트: registerEvent 사용 여부 확인

---

## 🏗️ 기술 스택

- Kotlin + Spring Boot
- JPA + Hibernate
- H2 or PostgreSQL
- Test: JUnit5, Mockk
- (선택) Kafka or Event Publisher

---

