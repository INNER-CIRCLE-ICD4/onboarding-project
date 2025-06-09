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
| `option_id`   | UUID (nullable)         | 선택형 질문일 경우, 선택한 옵션의 ID (QuestionOption.id)         |
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

### 📄 Survey ↔ QuestionGroup

- 관계: `Survey (1) ↔ (N) QuestionGroup`
- PK: `Survey.id`
- FK: `QuestionGroup.survey_id → Survey.id`

### 📄 QuestionGroup ↔ Question

- 관계: `QuestionGroup (1) ↔ (N) Question`
- PK: `QuestionGroup.id`
- FK: `Question.question_group_id → QuestionGroup.id`

### 📄 Question ↔ QuestionOption

- 관계: `Question (1) ↔ (N) QuestionOption`
- PK: `Question.id`
- FK: `QuestionOption.question_id → Question.id`

### 📄 Survey ↔ Response

- 관계: `Survey (1) ↔ (N) Response`
- PK: `Survey.id`
- FK: `Response.survey_id → Survey.id`

### 📄 QuestionGroup ↔ Response

- 관계: `QuestionGroup (1) ↔ (N) Response`
- PK: `QuestionGroup.id`
- FK: `Response.question_group_id → QuestionGroup.id`

### 📄 Response ↔ Answer

- 관계: `Response (1) ↔ (N) Answer`
- PK: `Response.id`
- FK: `Answer.response_id → Response.id`

### 📄 Question ↔ Answer

- 관계: `Question (1) ↔ (N) Answer`
- PK: `Question.id`
- FK: `Answer.question_id → Question.id`

---

## API 명세

### 공통 사항

- 요청/응답은 모두 `application/json` 형식 사용
- 모든 시간은 ISO 8601 포맷 (e.g., `2025-06-07T13:45:00Z`)
- 모든 ID는 UUID 형식 (단, `survey_id`는 Long)
- 성공 응답은 HTTP 상태 코드 기준으로 처리 (200, 201 등)

---

### 1. 설문조사 생성 API

- **URL**: `POST /api/surveys`
- **설명**: 설문과 질문을 생성함. QuestionGroup도 함께 생성됨.
- **성공 응답 코드**: `201 Created`

#### 요청

```json
{
	"title": "설문 제목",
	"description": "설문 설명",
	"questions": [
		{
			"type": "SINGLE_SELECT",
			"title": "성별을 선택해주세요",
			"description": "선택형 질문 예시",
			"is_required": true,
			"position": 0,
			"options": [
				{ "value": "남성", "position": 0 },
				{ "value": "여성", "position": 1 }
			]
		}
	]
}
```

#### 응답

```json
{
	"survey_id": 1,
	"question_group_id": "uuid-qg1"
}
```

---

### 2. 설문조사 수정 API

- **URL**: `PUT /api/surveys/{survey_id}`
- **설명**: 설문 내용을 수정하고 새로운 QuestionGroup을 생성함.
- **성공 응답 코드**: `200 OK`

#### 요청: 생성과 동일한 구조

- 응답은 동일 (새로운 `question_group_id` 반환)

---

### 3. 설문 응답 제출 API

- **URL**: `POST /api/responses`
- **설명**: 특정 설문에 대한 응답을 제출함
- **성공 응답 코드**: `201 Created`

#### 요청

```json
{
	"survey_id": 1,
	"question_group_id": "uuid-qg1",
	"answers": [
		{
			"question_id": "uuid-q1",
			"value": "남성",
			"option_id": "uuid-opt-a"
		},
		{
			"question_id": "uuid-q2",
			"value": "좋아요"
		}
	]
}
```

#### 응답

```json
{
	"response_id": "uuid-response-1"
}
```

---

### 4. 설문 응답 조회 API

- **URL**: `GET /api/surveys/{survey_id}/responses`
- **설명**: 해당 설문에 대한 모든 응답을 조회
- **성공 응답 코드**: `200 OK`
- **Query Parameter (optional)**:

  - `question_title`: 특정 질문에 대해 필터링
  - `value`: 특정 값에 대해 필터링

#### 응답 예시

```json
{
	"responses": [
		{
			"response_id": "uuid-response-1",
			"submitted_at": "2025-06-07T14:00:00Z",
			"question_group_id": "uuid-qg1",
			"answers": [
				{
					"question_id": "uuid-q1",
					"option_id": "uuid-opt-a",
					"value": "남성"
				},
				{
					"question_id": "uuid-q2",
					"value": "좋아요"
				}
			]
		}
	]
}
```

---

### 5. 설문조사 조회 API (추가)

- **URL**: `GET /api/surveys/{survey_id}`
- **설명**: 설명: 설문 정보를 조회하고, 최신 질문 목록(QuestionGroup 포함)을 반환함
- **성공 응답 코드**: `200 OK`

#### 응답 예시

```json
{
	"survey_id": 1,
	"title": "설문조사 제목",
	"description": "설문조사 설명",
	"question_group_id": "uuid-qg3",
	"questions": [
		{
			"question_id": "uuid-q1",
			"type": "SINGLE_SELECT",
			"title": "설문조사 질문 예시 1",
			"description": null,
			"is_required": true,
			"position": 0,
			"options": [
				{ "option_id": "uuid-opt-a", "value": "답1", "position": 0 },
				{ "option_id": "uuid-opt-b", "value": "답2", "position": 1 }
			]
		},
		{
			"question_id": "uuid-q2",
			"type": "LONG_TEXT",
			"title": "설문조사 질문 예시 2",
			"description": null,
			"is_required": false,
			"position": 1,
			"options": []
		}
	]
}
```

---

### 에러 응답 형식 (공통)

```json
{
	"error": {
		"code": "INVALID_REQUEST",
		"message": "question_id는 필수입니다."
	}
}
```

#### 예시 코드

- `INVALID_REQUEST`
- `NOT_FOUND`
- `INTERNAL_ERROR`

---

## 예외 처리

### 공통 응답 형식

```json
{
	"error": {
		"code": "ERROR_CODE",
		"message": "설명 메시지"
	}
}
```

---

### 1. 설문조사 생성/수정 API (`POST /api/surveys`, `PUT /api/surveys/{id}`)

#### 400 Bad Request

| 세부 코드 (`code`)            | 설명                                            |
| ----------------------------- | ----------------------------------------------- |
| `TOO_FEW_QUESTIONS`           | 질문은 최소 1개 이상이어야 합니다.              |
| `TOO_MANY_QUESTIONS`          | 질문은 최대 10개까지만 등록할 수 있습니다.      |
| `INVALID_QUESTION_TYPE`       | 존재하지 않는 질문 타입입니다.                  |
| `DUPLICATE_QUESTION_POSITION` | 같은 position이 두 개 이상 존재합니다.          |
| `MISSING_OPTIONS`             | 선택형 질문에 옵션이 존재하지 않습니다.         |
| `DUPLICATE_OPTION_POSITION`   | 하나의 질문 내 옵션 position이 중복되었습니다.  |
| `MISSING_REQUIRED_FIELD`      | 필수 입력 항목이 누락되었습니다. (ex: title 등) |

#### 404 Not Found

| 세부 코드 (`code`) | 설명                                |
| ------------------ | ----------------------------------- |
| `SURVEY_NOT_FOUND` | 해당 survey_id가 존재하지 않습니다. |

---

### 2. 설문 응답 제출 API (`POST /api/responses`)

#### 400 Bad Request

| 세부 코드 (`code`)            | 설명                                                        |
| ----------------------------- | ----------------------------------------------------------- |
| `QUESTION_GROUP_MISMATCH`     | 설문과 질문 그룹 ID가 일치하지 않습니다.                    |
| `INVALID_QUESTION_REFERENCE`  | 응답 항목 중 존재하지 않는 question_id가 포함되어 있습니다. |
| `MISSING_REQUIRED_ANSWER`     | 필수 응답 항목이 누락되었습니다.                            |
| `INVALID_OPTION_REFERENCE`    | option_id가 해당 질문의 유효한 선택지가 아닙니다.           |
| `INVALID_ANSWER_TYPE`         | 입력한 응답 값이 질문 타입과 일치하지 않습니다.             |
| `INVALID_MULTI_SELECT_FORMAT` | MULTIPLE_SELECT 응답은 배열 형식이어야 합니다.              |

#### 404 Not Found

| 세부 코드 (`code`)         | 설명                                |
| -------------------------- | ----------------------------------- |
| `SURVEY_NOT_FOUND`         | 해당 설문이 존재하지 않습니다.      |
| `QUESTION_GROUP_NOT_FOUND` | 해당 질문 그룹이 존재하지 않습니다. |

---

### 3. 설문 응답 조회 API (`GET /api/surveys/{survey_id}/responses`)

#### 404 Not Found

| 세부 코드 (`code`) | 설명                           |
| ------------------ | ------------------------------ |
| `SURVEY_NOT_FOUND` | 해당 설문이 존재하지 않습니다. |

---

### 4. 설문조사 조회 API (`GET /api/surveys/{survey_id}`)

#### 404 Not Found

| 세부 코드 (`code`) | 설명                           |
| ------------------ | ------------------------------ |
| `SURVEY_NOT_FOUND` | 해당 설문이 존재하지 않습니다. |

---

### 5. 공통 서버 오류

#### 500 Internal Server Error

| 세부 코드 (`code`) | 설명                            |
| ------------------ | ------------------------------- |
| `INTERNAL_ERROR`   | 서버 내부 오류가 발생했습니다.  |
| `DATABASE_ERROR`   | DB 처리 중 문제가 발생했습니다. |
| `UNKNOWN_ERROR`    | 알 수 없는 오류가 발생했습니다. |

---

## 테스트

> TODO: 단위 테스트 및 통합 테스트 전략, 주요 테스트 항목 정리 예정

---

## 기타 구현 사항

> TODO: 추가 기능, 성능 고려 사항, 우대 구현사항 대응 여부 등 기술 예정

---

> 본 문서는 기능 구현과 함께 지속적으로 업데이트됩니다.
