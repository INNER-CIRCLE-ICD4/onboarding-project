# 설문조사 서비스 API 명세서

## 개요
설문조사 생성, 수정, 응답 제출 및 조회 기능을 제공하는 REST API 서비스입니다.
**설문 항목이 추가/변경/삭제되어도 기존 응답은 안전하게 유지됩니다.**

## 기본 정보
- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json`
- **인코딩**: UTF-8

## 기존 응답 유지 시스템

###  버전 관리 시스템
- 설문 항목이 변경되면 **새로운 버전**이 자동 생성됩니다(v1,v2,v3...)
  - 기존 응답은 **이전 버전과 연결**되어 영구 보존됩니다
- 각 응답은 특정 설문 버전과 연결되어 **데이터 무결성**을 보장합니다

### 안전한 응답 조회
- 삭제된 항목에 대한 응답도 **안전하게 조회** 가능
- 삭제된 항목은 `[삭제된 항목]` 표시로 구분
- 응답 검색 시 **레거시 데이터 포함** 검색 지원

### 변경 영향도 알림
- 설문 수정 시 **기존 응답 수 자동 확인**
- 서버 로그에 영향도 정보 자동 기록
- 변경 전 영향도 미리 확인할 수 있는 API 제공

## 공통 응답 형식

### 성공 응답
```json
{
  "success": true,
  "message": "성공 메시지",
  "data": { /* 응답 데이터 */ }
}
```

### 실패 응답
```json
{
  "success": false,
  "message": "에러 메시지",
  "data": null
}
```

---

## 1. 설문조사 관리 API

### 1.1 설문조사 생성
**POST** `/survey`

#### 요청 본문
```json
{
  "name": "설문조사 제목",
  "description": "설문조사 설명",
  "state": "ACTIVE",
  "items": [
    {
      "title": "설문 항목 제목",
      "description": "설문 항목 설명",
      "inputType": "SHORT_TYPE",
      "required": true,
      "itemOrder": 1,
      "itemOption": "선택지1,선택지2,선택지3"
    }
  ]
}
```

#### 필드 설명
- `name`: 설문조사 제목 (필수, 1-100자)
- `description`: 설문조사 설명 (선택)
- `state`: 설문조사 상태 (ACTIVE, INACTIVE)
- `items`: 설문 항목 배열 (필수, 1-10개)
  - `title`: 항목 제목 (필수)
  - `description`: 항목 설명 (선택)
  - `inputType`: 입력 유형 (필수)
    - `SHORT_TYPE`: 단답형 (1-100자)
    - `LONG_TYPE`: 장문형 (1-1000자)
    - `SINGLE_TYPE`: 단일 선택
    - `MULTIPLE_TYPE`: 복수 선택
  - `required`: 필수 여부 (true/false)
  - `itemOrder`: 항목 순서 (필수, 양수)
  - `itemOption`: 선택지 (SINGLE_TYPE, MULTIPLE_TYPE일 때 필수, 콤마로 구분)

#### 응답 예시
```json
{
  "success": true,
  "message": "설문조사가 성공적으로 생성되었습니다.",
  "data": {
    "surveyId": 1,
    "name": "고객 만족도 조사",
    "description": "서비스 개선을 위한 설문조사",
    "state": "ACTIVE",
    "createdAt": "2025-06-19T12:00:00",
    "versions": [
      {
        "versionId": 1,
        "versionNumber": 1,
        "items": [
          {
            "itemId": 1,
            "title": "이름",
            "description": "성함을 입력해주세요",
            "inputType": "SHORT_TYPE",
            "required": true,
            "itemOrder": 1,
            "itemOption": ""
          }
        ]
      }
    ]
  }
}
```

### 1.2 설문조사 수정
**PATCH** `/survey/{surveyId}`

#### 경로 매개변수
- `surveyId`: 설문조사 ID (필수)

#### 요청 본문
```json
{
  "name": "수정된 설문조사 제목",
  "description": "수정된 설문조사 설명",
  "items": [
    {
      "title": "수정된 항목 제목",
      "description": "수정된 항목 설명",
      "inputType": "LONG_TYPE",
      "required": false,
      "itemOrder": 1,
      "itemOption": ""
    }
  ]
}
```

#### 버전 관리 로직
1. **기본 정보만 변경**: 기존 버전 업데이트, 새 버전 생성 안함
2. **설문 항목 변경**: 새 버전 자동 생성, 기존 응답은 이전 버전과 연결 유지

#### 응답 예시
```json
{
  "success": true,
  "message": "설문조사가 성공적으로 수정되었습니다.",
  "data": {
    "surveyId": 1,
    "name": "수정된 고객 만족도 조사",
    "description": "개선된 서비스를 위한 설문조사",
    "state": "ACTIVE",
    "updatedAt": "2025-06-19T13:00:00",
    "versions": [
      {
        "versionId": 2,
        "versionNumber": 2,
        "items": [...]
      }
    ]
  }
}
```

### 1.3 응답 영향도 확인
**GET** `/survey/{surveyId}/response-count`

설문조사 수정 전 기존 응답에 미치는 영향을 미리 확인할 수 있습니다.

#### 경로 매개변수
- `surveyId`: 설문조사 ID (필수)

#### 응답 예시
```json
{
  "success": true,
  "message": "응답 수 조회가 완료되었습니다.",
  "data": {
    "surveyId": 1,
    "existingResponseCount": 25,
    "canModifyFreely": false,
    "warning": "설문 항목을 변경하면 새로운 버전이 생성되며, 기존 25개의 응답은 유지됩니다."
  }
}
```

#### 응답이 없는 경우
```json
{
  "success": true,
  "message": "응답 수 조회가 완료되었습니다.",
  "data": {
    "surveyId": 1,
    "existingResponseCount": 0,
    "canModifyFreely": true,
    "message": "기존 응답이 없으므로 자유롭게 수정할 수 있습니다."
  }
}
```

---

## 2. 설문 응답 API

### 2.1 설문 응답 제출
**POST** `/answer/{surveyVersionId}`

#### 경로 매개변수
- `surveyVersionId`: 설문조사 버전 ID (필수)

#### 요청 본문
```json
{
  "answers": [
    {
      "itemId": 1,
      "answerValue": "홍길동"
    },
    {
      "itemId": 2,
      "answerValue": "25"
    }
  ]
}
```

#### 필드 설명
- `answers`: 응답 배열 (필수)
  - `itemId`: 설문 항목 ID (필수)
  - `answerValue`: 응답 값 (필수)
    - SHORT_TYPE: 1-100자 텍스트
    - LONG_TYPE: 1-1000자 텍스트
    - SINGLE_TYPE: 제공된 선택지 중 하나
    - MULTIPLE_TYPE: 제공된 선택지 중 여러 개 (콤마로 구분)

#### 응답 예시
```json
{
  "success": true,
  "message": "설문 응답이 성공적으로 제출되었습니다.",
  "data": {
    "responseId": 1,
    "surveyId": 1,
    "surveyVersionId": 1,
    "submittedAt": "2025-06-19T14:30:00",
    "answers": [
      {
        "itemId": 1,
        "itemTitle": "이름",
        "answerValue": "홍길동",
        "itemOrder": 1
      },
      {
        "itemId": 2,
        "itemTitle": "나이",
        "answerValue": "25",
        "itemOrder": 2
      }
    ]
  }
}
```

#### 검증 규칙
1. **필수 항목 완전성**: 모든 필수 항목에 대한 응답 포함
2. **InputType 검증**: 각 항목의 입력 유형에 맞는 형식 검증
3. **선택지 일치**: SINGLE/MULTIPLE 유형에서 제공된 선택지와 일치
4. **중복 방지**: 동일한 항목에 대한 중복 응답 방지
5. **순서 일치**: 설문 항목과 응답의 일대일 대응

---

## 3. 응답 조회 API

### 3.1 개별 설문 응답 조회
**GET** `/answer/response/{responseId}`

#### 경로 매개변수
- `responseId`: 응답 ID (필수)

#### 삭제된 항목 처리
삭제된 항목에 대한 응답도 안전하게 조회됩니다:
- 삭제된 항목은 `itemOrder: -1`로 표시
- 응답 값에 `[삭제된 항목]` 표시 추가

#### 응답 예시
```json
{
  "success": true,
  "message": "설문 응답 조회가 완료되었습니다.",
  "data": {
    "responseId": 1,
    "surveyId": 1,
    "surveyVersionId": 1,
    "surveyTitle": "고객 만족도 조사",
    "submittedAt": "2025-06-19T14:30:00",
    "answers": [
      {
        "itemId": 1,
        "itemTitle": "이름",
        "answerValue": "홍길동",
        "itemOrder": 1
      },
      {
        "itemId": -1,
        "itemTitle": null,
        "answerValue": "매우만족 [삭제된 항목]",
        "itemOrder": -1
      }
    ]
  }
}
```

### 3.2 설문조사 전체 응답 조회
**GET** `/answer/survey/{surveyId}/responses`

#### 경로 매개변수
- `surveyId`: 설문조사 ID (필수)

####  다중 버전 지원
모든 버전의 응답을 통합하여 조회합니다:
- 버전 1의 응답과 버전 2의 응답을 모두 포함
- 각 응답에는 해당 버전 정보가 포함됨

#### 응답 예시
```json
{
  "success": true,
  "message": "설문 응답 목록 조회가 완료되었습니다.",
  "data": [
    {
      "responseId": 1,
      "surveyId": 1,
      "surveyVersionId": 1,
      "surveyTitle": "고객 만족도 조사",
      "submittedAt": "2025-06-19T14:30:00",
      "answers": [...]
    },
    {
      "responseId": 2,
      "surveyId": 1,
      "surveyVersionId": 2,
      "surveyTitle": "고객 만족도 조사",
      "submittedAt": "2025-06-19T15:00:00",
      "answers": [...]
    }
  ]
}
```

### 3.3 설문 응답 검색 (Advanced)
**GET** `/answer/survey/{surveyId}/responses/search`

#### 경로 매개변수
- `surveyId`: 설문조사 ID (필수)

#### 쿼리 매개변수
- `itemTitle`: 검색할 설문 항목 제목 (선택, 부분 검색 지원)
- `answerValue`: 검색할 응답 값 (선택, 부분 검색 지원)

####  레거시 응답 포함 검색
- **삭제된 항목**: 항목 제목으로 검색 불가, 응답 값으로만 검색 가능
- **버전 정보 없음**: 응답 값으로만 검색 (레거시 데이터 지원)
- **다중 버전**: 모든 버전의 응답에서 통합 검색

#### 요청 예시
```
GET /answer/survey/1/responses/search?itemTitle=이름&answerValue=홍
```

#### 응답 예시 (삭제된 항목 포함)
```json
{
  "success": true,
  "message": "설문 응답 검색이 완료되었습니다.",
  "data": [
    {
      "responseId": 1,
      "surveyId": 1,
      "surveyVersionId": 1,
      "surveyTitle": "고객 만족도 조사",
      "submittedAt": "2025-06-19T14:30:00",
      "answers": [
        {
          "itemId": 1,
          "itemTitle": "이름",
          "answerValue": "홍길동",
          "itemOrder": 1
        }
      ]
    },
    {
      "responseId": 3,
      "surveyId": 1,
      "surveyVersionId": 2,
      "surveyTitle": "고객 만족도 조사",
      "submittedAt": "2025-06-19T16:00:00",
      "answers": [
        {
          "itemId": 2,
          "itemTitle": "성명",
          "answerValue": "홍영희",
          "itemOrder": 1
        },
        {
          "itemId": -1,
          "itemTitle": null,
          "answerValue": "서울 [삭제된 항목]",
          "itemOrder": -1
        }
      ]
    }
  ]
}
```

#### 검색 기능
- **부분 검색**: 입력한 키워드가 포함된 모든 결과 반환
- **대소문자 무관**: 영문 대소문자 구분 없이 검색
- **다중 조건**: 항목 제목과 응답 값을 동시에 검색 가능
- **AND 조건**: 여러 조건을 모두 만족하는 결과만 반환
- **레거시 지원**: 삭제된 항목과 버전 정보 없는 응답도 검색 포함

---

## 4. 에러 코드

### 4.1 일반 에러
| HTTP 상태 | 에러 코드 | 메시지 | 설명 |
|-----------|----------|--------|------|
| 400 | BAD_REQUEST | 잘못된 요청입니다 | 요청 형식이 올바르지 않음 |
| 404 | NOT_FOUND | 리소스를 찾을 수 없습니다 | 요청한 리소스가 존재하지 않음 |
| 500 | INTERNAL_SERVER_ERROR | 서버 내부 오류가 발생했습니다 | 서버 처리 중 오류 발생 |

### 4.2 설문조사 관련 에러
| HTTP 상태 | 에러 코드 | 메시지 |
|-----------|----------|--------|
| 400 | INVALID_SURVEY_ITEM_COUNT | 설문 항목은 1개~10개까지만 포함할 수 있습니다 |
| 400 | DUPLICATE_ITEM_ORDER | 중복된 항목 순서가 있습니다 |
| 400 | INVALID_INPUT_TYPE | 유효하지 않은 입력 유형입니다 |

### 4.3 응답 관련 에러
| HTTP 상태 | 에러 코드 | 메시지 |
|-----------|----------|--------|
| 400 | MISSING_REQUIRED_ITEM | 필수 설문 항목에 대한 응답이 누락되었습니다 |
| 400 | INVALID_ANSWER_LENGTH | 응답 길이가 유효하지 않습니다 |
| 400 | INVALID_OPTION_VALUE | 유효하지 않은 선택지입니다 |
| 400 | DUPLICATE_ITEM_RESPONSE | 동일한 항목에 대한 중복 응답이 있습니다 |

---

## 5. 기존 응답 유지 시나리오

### 5.1 설문 항목 추가 시나리오

#### 1단계: 초기 설문조사 생성
```bash
curl -X POST "http://localhost:8080/survey" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "고객 만족도 조사",
    "description": "서비스 개선을 위한 설문조사",
    "state": "ACTIVE",
    "items": [
      {
        "title": "이름",
        "inputType": "SHORT_TYPE",
        "required": true,
        "itemOrder": 1
      }
    ]
  }'
```

#### 2단계: 초기 응답 제출
```bash
curl -X POST "http://localhost:8080/answer/1" \
  -H "Content-Type: application/json" \
  -d '{
    "answers": [
      {
        "itemId": 1,
        "answerValue": "홍길동"
      }
    ]
  }'
```

#### 3단계: 영향도 확인
```bash
curl -X GET "http://localhost:8080/survey/1/response-count"
```

#### 4단계: 설문 항목 추가 (새 버전 생성)
```bash
curl -X PATCH "http://localhost:8080/survey/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "고객 만족도 조사",
    "items": [
      {
        "title": "이름",
        "inputType": "SHORT_TYPE",
        "required": true,
        "itemOrder": 1
      },
      {
        "title": "나이",
        "inputType": "SHORT_TYPE",
        "required": true,
        "itemOrder": 2
      }
    ]
  }'
```

#### 5단계: 새 버전으로 응답 제출
```bash
curl -X POST "http://localhost:8080/answer/2" \
  -H "Content-Type: application/json" \
  -d '{
    "answers": [
      {
        "itemId": 1,
        "answerValue": "김영희"
      },
      {
        "itemId": 2,
        "answerValue": "28"
      }
    ]
  }'
```

#### 6단계: 기존 응답 유지 확인
```bash
curl -X GET "http://localhost:8080/answer/survey/1/responses"
```

### 5.2 설문 항목 삭제 시나리오

#### 1단계: 항목 삭제 (새 버전 생성)
```bash
curl -X PATCH "http://localhost:8080/survey/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "고객 만족도 조사",
    "items": [
      {
        "title": "이름",
        "inputType": "SHORT_TYPE",
        "required": true,
        "itemOrder": 1
      }
    ]
  }'
```

#### 2단계: 삭제된 항목 응답 검색
```bash
curl -X GET "http://localhost:8080/answer/survey/1/responses/search?answerValue=28"
```

**결과**: 삭제된 "나이" 항목의 응답 "28"이 `[삭제된 항목]` 표시와 함께 조회됩니다.

---

## 6. 기술 스택
- **Framework**: Spring Boot 3.5.0
- **Database**: MySQL 8.4.5
- **ORM**: JPA/Hibernate
- **Validation**: Jakarta Validation
- **Architecture**: Layered Architecture (Controller → Service → Repository → Domain)

## 7. 보안 고려사항
- 입력 값 검증 및 XSS 방지
- SQL Injection 방지를 위한 JPA 쿼리 사용
- 응답 길이 제한으로 DoS 공격 방지
- 트랜잭션 처리로 데이터 일관성 보장

## 8. 기존 응답 유지 보장사항

###  데이터 무결성
- **영구 보존**: 기존 응답은 절대 삭제되지 않음
- **버전 연결**: 각 응답은 특정 설문 버전과 영구 연결
- **안전한 조회**: 삭제된 항목도 안전하게 조회 가능

###  투명성
- **변경 알림**: 설문 수정 시 영향받는 응답 수 자동 알림
- **로그 기록**: 모든 변경사항과 영향도 서버 로그에 기록
- **미리 확인**: 변경 전 영향도 확인 API 제공

###  호환성
- **다중 버전**: 여러 버전의 응답을 통합 관리
- **레거시 지원**: 이전 버전 응답도 완전 지원
- **검색 포함**: 모든 버전의 응답에서 통합 검색 가능 