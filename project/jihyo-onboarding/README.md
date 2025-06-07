# 설문조사 서비스 - 이너써클 온보딩 프로젝트 - 김지효

## 개요

본 프로젝트는 이너써클 BE 온보딩을 위한 설문조사 서비스 구현입니다.  
Spring Boot, JPA, H2 기반으로 설문조사 생성부터 응답 조회까지의 기능을 포함합니다.

---

## 구현 목표

- 설문조사 생성 API
- 설문조사 수정 API
- 설문 응답 제출 API
- 설문 응답 조회 API
  - 응답 필터링 기능

---

## 기술 스택

- Kotlin
- Spring Boot
- Spring Data JPA
- H2 (인메모리 DB)
- Gradle

---

## 프로젝트 구조

> TODO: 패키지 구조와 각 모듈 또는 레이어 설명을 여기에 작성할 예정

---

## DB 스키마 구조

### 1. Survey

| 필드명        | 타입                 | 설명                            |
| ------------- | -------------------- | ------------------------------- |
| `id`          | Long (PK)            | 설문 ID                         |
| `title`       | String               | 설문 제목                       |
| `description` | Text                 | 설문 설명                       |
| `is_deleted`  | Boolean              | soft delete 여부 (기본값 false) |
| `deleted_at`  | Timestamp (nullable) | 삭제된 시각                     |
| `created_at`  | Timestamp            | 생성 시각                       |
| `updated_at`  | Timestamp            | 마지막 수정 시각                |

---

### 2. QuestionGroup

| 필드명       | 타입                  | 설명                              |
| ------------ | --------------------- | --------------------------------- |
| `id`         | UUID (PK)             | 질문 그룹 ID                      |
| `survey_id`  | Long (FK → Survey.id) | 어떤 설문에 속한 질문 그룹인지    |
| `version`    | Int                   | 해당 설문 내에서의 질문 그룹 버전 |
| `created_at` | Timestamp             | 생성 시각                         |

- 하나의 Survey는 여러 QuestionGroup을 가질 수 있으며, 하나의 응답은 정확히 하나의 QuestionGroup과 연결됩니다.
- Answer에 position을 저장하고, question을 복제하지 않는 방식도 고려했으나, history와 필터링 등 확장성, 유지보수의 측면에서 업데이트 시 모든 question을 복제하여 새로 생성하고 QuestionGroup을 만드는 방식으로 결정했습니다.
- 설문 생성 및 수정 시점에 생성됩니다.

---

### 3. Question

| 필드명              | 타입                                                                | 설명                                  |
| ------------------- | ------------------------------------------------------------------- | ------------------------------------- |
| `id`                | UUID (PK)                                                           | 질문 ID                               |
| `question_group_id` | UUID (FK → QuestionGroup.id)                                        | 질문이 속한 그룹                      |
| `position`          | Int                                                                 | 해당 질문의 순서 (0부터 시작)         |
| `type`              | ENUM('SHORT_TEXT', 'LONG_TEXT', 'SINGLE_SELECT', 'MULTIPLE_SELECT') | 질문 타입                             |
| `title`             | String                                                              | 질문 제목 (예: "성별을 선택해주세요") |
| `description`       | String (nullable)                                                   | 질문에 대한 설명                      |
| `is_required`       | Boolean                                                             | 필수 여부                             |
| `created_at`        | Timestamp                                                           | 생성 시각                             |

---

### 4. QuestionOption

| 필드명        | 타입                    | 설명                           |
| ------------- | ----------------------- | ------------------------------ |
| `id`          | UUID (PK)               | 옵션 ID                        |
| `question_id` | UUID (FK → Question.id) | 연결된 질문                    |
| `value`       | String                  | 선택지 값 (예: "남성", "여성") |
| `position`    | Int                     | 옵션 순서                      |
| `created_at`  | Timestamp               | 생성 시각                      |

- `SINGLE_SELECT`, `MULTIPLE_SELECT` 타입의 질문에만 옵션이 존재합니다.

---

### 5. Response

| 필드명              | 타입                         | 설명                           |
| ------------------- | ---------------------------- | ------------------------------ |
| `id`                | UUID (PK)                    | 응답 ID                        |
| `survey_id`         | Long (FK → Survey.id)        | 어떤 설문에 대한 응답인지      |
| `question_group_id` | UUID (FK → QuestionGroup.id) | 어떤 질문 세트에 대한 응답인지 |
| `submitted_at`      | Timestamp                    | 응답 완료 시각                 |

- 응답 단위로 하나의 `question_group_id`에 종속되므로 질문 구조를 그대로 복원할 수 있습니다.

---

### 6. Answer

| 필드명        | 타입                    | 설명                                                             |
| ------------- | ----------------------- | ---------------------------------------------------------------- |
| `id`          | UUID (PK)               | 응답 상세 항목 ID                                                |
| `response_id` | UUID (FK → Response.id) | 어떤 응답에 속한 답변인지                                        |
| `question_id` | UUID (FK → Question.id) | 어떤 질문에 대한 응답인지                                        |
| `value`       | Text                    | 응답 값 (단답/장문/선택값 등, 복수 선택은 JSON 배열 등으로 표현) |
| `position`    | Int                     | 질문 내 위치 (정렬용, Question.position 복사값)                  |

---

### 정리

- `Survey`는 soft delete를 고려함
- `QuestionGroup`은 설문 내 질문 세트를 버전으로 관리
- `Question`은 수정 시 새로운 row를 생성 (불변성 유지)
- `Answer`는 `question_id`를 명시하여 해당 시점 질문을 참조 가능
- `position`을 통해 질문 순서를 유지
- `Response`는 설문과 질문 세트를 모두 명시함으로써 응답 구조 추적 가능

---

## 🔁 관계 요약

---

## API 명세

> TODO: 각 API 요청/응답 형식 및 예시 명세 추가 예정

---

## 예외 처리

> TODO: 공통 예외 응답 포맷, 커스텀 예외 정의 및 처리 방식 정리 예정

---

## 테스트

> TODO: 단위 테스트 및 통합 테스트 전략, 주요 테스트 항목 정리 예정

---

## 기타 구현 사항

> TODO: 추가 기능, 성능 고려 사항, 우대 구현사항 대응 여부 등 기술 예정

---

> 본 문서는 기능 구현과 함께 지속적으로 업데이트됩니다.
