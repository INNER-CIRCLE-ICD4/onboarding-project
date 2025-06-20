# 설문조사 API 명세서

## 프로젝트 정보
- **포트**: 8099
- **컨텍스트 패스**: /api
- **베이스 URL**: http://localhost:8099/api

## API 엔드포인트

### 1. 테스트 API

#### Hello World 테스트
```
GET /test
```

**응답 예시**
```
Hello, World!
```

---

### 2. 설문조사 API

#### 2-1. 설문조사 생성
```
POST /surveys
```

**요청 Body**
```json
{
  "name": "설문조사명 (필수)",
  "description": "설문조사 설명 (선택)",
  "surveyItems": [
    {
      "name": "설문항목명 (필수)",
      "description": "설문항목 설명 (선택)",
      "inputType": "SHORT_TEXT | LONG_TEXT | SINGLE_CHOICE | MULTIPLE_CHOICE (필수)",
      "options": [
        {
          "name": "옵션명"
        }
      ],
      "isRequired": true/false (필수)
    }
  ]
}
```

**응답**
- **상태 코드**: 201 Created
- **응답 Body**:
```json
{
  "success": true,
  "message": "설문조사가 생성되었습니다.",
}
```

**유효성 검증**
- `name`: 필수값, 공백 불가
- `surveyItems`: 최소 1개, 최대 10개
- `inputType`: SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE 중 하나

---

#### 2-2. 설문조사 목록 조회
```
GET /surveys
```

**응답**
- **상태 코드**: 200 OK
- **응답 Body**:
```json
{
  "success": true,
  "message": "설문조사 목록을 조회했습니다.",
  "data": [
    {
      "id": 1,
      "name": "설문조사명",
      "description": "설문조사 설명",
      "createdAt": "2025-06-21T10:00:00",
      "updatedAt": "2025-06-21T10:00:00"
    }
  ]
}
```

---

#### 2-3. 설문조사 수정
```
PUT /surveys/{surveyId}
```

**경로 변수**
- `surveyId`: 수정할 설문조사 ID

**요청 Body**
```json
{
  "name": "수정된 설문조사명 (필수)",
  "description": "수정된 설문조사 설명 (선택)"
}
```

**응답**
- **상태 코드**: 200 OK
- **응답 Body**:
```json
{
  "success": true,
  "message": "설문조사를 수정했습니다."
}
```

---

#### 2-4. 설문조사 응답 생성
```
POST /surveys/{surveyId}/responses
```

**경로 변수**
- `surveyId`: 응답할 설문조사 ID

**요청 Body**
```json
{
  "respondent": "응답자명 (필수)",
  "answers": [
    {
      "surveyItemId": 1,
      "answer": "답변 내용"
    }
  ]
}
```

**응답**
- **상태 코드**: 200 OK
- **응답 Body**:
```json
{
  "success": true,
  "message": "설문조사 항목에 대한 응답이 생성되었습니다."
}
```

**유효성 검증**
- `respondent`: 필수값, 공백 불가
- `answers`: 최소 1개 이상의 답변 필요

---

#### 2-5. 설문조사 응답 조회
```
GET /surveys/{surveyId}/responses
```

**경로 변수**
- `surveyId`: 조회할 설문조사 ID

**응답**
- **상태 코드**: 200 OK
- **응답 Body**:
```json
{
  "success": true,
  "message": "설문조사에 대한 전체 응답을 조회했습니다.",
  "data": {
    "survey": {
      "id": 1,
      "name": "설문조사명",
      "description": "설문조사 설명"
    },
    "surveyItems": [
      {
        "id": 1,
        "name": "설문항목명",
        "description": "설문항목 설명",
        "inputType": "SHORT_TEXT",
        "isRequired": true,
        "options": []
      }
    ],
    "responses": [
      {
        "id": 1,
        "respondent": "응답자명",
        "answers": [
          {
            "surveyItemId": 1,
            "answer": "답변 내용"
          }
        ]
      }
    ]
  }
}
```

---

## 설문 항목 입력 타입

| 타입 | 설명 |
|------|------|
| SHORT_TEXT | 짧은 텍스트 입력 |
| LONG_TEXT | 긴 텍스트 입력 |
| SINGLE_CHOICE | 단일 선택 |
| MULTIPLE_CHOICE | 다중 선택 |

---

## 에러 응답 형식

에러 발생 시 다음과 같은 형식으로 응답합니다:

```json
{
  "success": false,
  "data": null,
  "message": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 메시지"
  }
}
```

### 에러 코드 및 상태 코드

| HTTP 상태 | 에러 코드 | 메시지 | 설명 |
|-----------|-----------|--------|------|
| 500 | ERR001 | 서버 오류가 발생하였습니다. 관리자에게 문의해주세요. | 내부 서버 오류 |
| 400 | ERR002 | HTTP 요청 메세지를 읽을 수 없습니다. | 잘못된 JSON 형식 |
| 400 | ERR003 | 요청한 값이 검증에 실패했습니다. | 유효성 검증 실패 |
| 400 | ERR004 | 요청 URL 혹은 요청 값의 타입이 올바르지 않습니다. | 타입 불일치 |
| 400 | ERR005 | 유효하지 않은 설문조사 항목 입력 종류입니다. 옵션을 추가하려면 SINGLE_CHOICE 혹은 MULTIPLE_CHOICE 여야 합니다. | 잘못된 설문 항목 타입 |
| 400 | ERR006 | 설문조사 항목 선택 옵션은 최소 2개 이상이어야 합니다. | 선택 옵션 부족 |
| 400 | ERR007 | 해당 설문조사는 존재하지 않습니다. | 설문조사 미존재 |
| 400 | ERR008 | 해당 설문조사 항목은 존재하지 않습니다. | 설문 항목 미존재 |
| 400 | ERR009 | 설문조사 항목에 대한 유효한 응답이 아닙니다. 선택 가능한 옵션중에서 다시 응답해주세요. | 잘못된 선택 응답 |

### 에러 응답 예시

#### 1. 유효성 검증 실패 (ERR003)
```json
{
  "success": false,
  "error": {
    "code": "ERR003",
    "message": "[name] 설문조사 이름은 필수값 입니다. rejected value: [] [surveyItems] 설문조사 항목은 최소 1개, 최대 10개까지 가능합니다. rejected value: [] "
  }
}
```

#### 2. 설문조사 미존재 (ERR007)
```json
{
  "success": false,
  "error": {
    "code": "ERR007",
    "message": "해당 설문조사는 존재하지 않습니다."
  }
}
```

#### 3. 타입 불일치 (ERR004)
```json
{
  "success": false,
  "error": {
    "code": "ERR004",
    "message": "[surveyId] required type: [class java.lang.Long] value: [abc] "
  }
}
```

#### 4. 잘못된 JSON 형식 (ERR002)
```json
{
  "success": false,
  "error": {
    "code": "ERR002",
    "message": "HTTP 요청 메세지를 읽을 수 없습니다."
  }
}
```

#### 5. 잘못된 설문 항목 타입 (ERR005)
```json
{
  "success": false,
  "error": {
    "code": "ERR005",
    "message": "유효하지 않은 설문조사 항목 입력 종류입니다. 옵션을 추가하려면 SINGLE_CHOICE 혹은 MULTIPLE_CHOICE 여야 합니다."
  }
}
```

#### 6. 선택 옵션 부족 (ERR006)
```json
{
  "success": false,
  "error": {
    "code": "ERR006",
    "message": "설문조사 항목 선택 옵션은 최소 2개 이상이어야 합니다."
  }
}
```

---

## 개발 환경

- **Java**: Kotlin
- **Framework**: Spring Boot
- **Database**: H2 (In-Memory)
- **Port**: 8099
- **Context Path**: /api
- **H2 Console**: http://localhost:8099/api/h2-console

## API 문서

프로젝트에 Swagger가 설정되어 있습니다. 서버 실행 후 다음 경로에서 API 문서를 확인할 수 있습니다:
- Swagger UI: http://localhost:8099/api/swagger-ui.html
