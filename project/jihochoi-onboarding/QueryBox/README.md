# 📋 설문조사 서비스 (Survey Service)

설문조사 생성 및 응답 수집 기능을 제공하는 도메인 기반 설문 서비스입니다.  
Google Forms, Tally 등과 유사한 기능을 목표로 하며, DDD 스타일로 작성합니다.

---

## ✅ 주요 도메인 모델

### 📦 Entity 목록

| Entity | 설명                                      |
|--------|-----------------------------------------|
| `Survey` | 설문 전체. 항목 목록을 포함한 루트 애그리거트              |
| `Question` | 하나의 항목. 입력 형태, 필수 여부 등 포함               |
| `Response` | 사용자의 응답. 여러 Answer를 포함, 루트 애그리거트        |
| `Answer` *(선택)* | 특정 Question에 대한 응답. 필요 시 Entity, 아니면 VO |

### 🧱 Value Object 목록

| Value Object | 설명 |
|--------------|------|
| `Option` | 선택형 항목의 선택지. 값만으로 동일성 판단 |
| `QuestionType` | 단답형, 장문형, 단일/다중 선택형 등 |
| `AnswerContent` *(선택)* | 텍스트 or 선택지 응답값을 캡슐화할 경우 |
| `QuestionName`, `QuestionDescription` *(선택)* | 값 타입으로 캡슐화 가능 |

---

## 🔒 도메인 불변식 (Invariant)

### Survey
- [x] 설문 제목은 공백일 수 없음

### Questions
- [x] 항목은 1~10개 사이여야 함
- [x] 항목 이름은 중복되면 안 됨

### Question
- [x] 항목 이름은 공백일 수 없음
- [x] 선택형 항목(SINGLE, MULTIPLE)은 옵션이 1개 이상 존재해야 함
- [x] 주관식 항목(SHORT_TEXT, LONG_TEXT)은 옵션이 없어야 함

### Answers
- [x] 응답은 최소 하나 이상의 Answer를 포함해야 함
- [x] 동일한 Question에 대해 중복 응답이 존재하면 안 됨

### Answer
- [x] 주관식인 경우: text 값이 반드시 존재해야 함
- [x] 선택형인 경우: 최소 하나 이상의 option ID가 포함되어야 함

---

## 🧩 기능 목록 (Feature To-do)

### 설문 관리

- [x] 설문 생성 API
- [x] 설문 수정 API
- [x] 설문 조회 API (생성/수정 결과 확인용)

### 응답 관리

- [x] 설문 응답 제출 API
- [x] 설문 응답 전체 조회 API (항목 이름 + 응답 값 기준)
- [ ] (추가) 응답 제출 시 설문 제출된 내용 메일 발송 
---

## 🏗️ 기술 스택

- Kotlin + Spring Boot
- JPA + Hibernate
- H2
- Test: JUnit5
---

