# 설문조사 서비스 API 명세서

## 개요
설문조사 양식을 생성하고 관리하며, 응답을 수집할 수 있는 RESTful API 서비스입니다.

## 공통 응답 형식

### 성공 응답
```json
{
  "status": 200,
  "message": "성공 메시지",
  "path": "/api/surveys",
  "timestamp": "2025-06-14T10:30:00",
  "error": null,
  "data": {
    // 실제 데이터
  }
}
```

### 에러 응답
```json
{
  "status": 400,
  "message": "에러 메시지",
  "path": "/api/surveys",
  "timestamp": "2025-06-14T10:30:00",
  "error": "Bad Request",
  "data": null
}
```

## 에러 코드 정의

| HTTP 상태 | code                        | 에러 메시지                       | 설명                                            |
|-----------|-----------------------------|------------------------------|-----------------------------------------------|
| **400 - Bad Request** |
| 400 | INVALID_REQUEST_FORMAT      | 잘못된 요청 데이터입니다                | 요청 형식이 올바르지 않음                                |
| 400 | VALIDATION_FAILED           | 유효성 검사에 실패했습니다               | 일반적인 유효성 검사 실패                                |
| **설문조사 생성/수정 관련** |
| 400 | SURVEY_NAME_DUPLICATED        | 이미 등록된 설문조사 이름입니다            | 설문조사 이름 중복                                    |
| 400 | SURVEY_NAME_REQUIRED        | 설문조사 이름은 필수입니다               | 설문조사 이름 누락                                    |
| 400 | SURVEY_NAME_TOO_LONG        | 설문조사 이름은 200자를 초과할 수 없습니다    | 설문조사 이름 길이 초과                                 |
| 400 | SURVEY_DESCRIPTION_TOO_LONG | 설문조사 설명은 2000자를 초과할 수 없습니다   | 설문조사 설명 길이 초과                                 |
| 400 | SURVEY_ITEMS_INVALID        | 설문 항목은 1개 이상 10개 이하여야 합니다    | 설문 항목 누락 및 개수 초과                              |
| 400 | ITEM_NAME_REQUIRED          | 설문 항목 이름은 필수입니다              | 설문 항목 이름 누락                                   |
| 400 | ITEM_NAME_TOO_LONG          | 설문 항목 이름은 200자를 초과할 수 없습니다   | 설문 항목 이름 길이 초과                                |
| 400 | ITEM_DESCRIPTION_TOO_LONG   | 설문 항목 설명은 2000자를 초과할 수 없습니다  | 설문 항목 설명 길이 초과                                |
| 400 | OPTION_NAME_REQUIRED        | 항목 옵션 이름은 필수입니다              | 항목 옵션 이름 누락                                   |
| 400 | OPTION_NAME_TOO_LONG        | 항목 옵션 이름은 200자를 초과할 수 없습니다   | 항목 옵션 이름 길이 초과                                |
| 400 | SELECT_OPTIONS_REQUIRED     | 선택형 문항에는 선택지가 필요합니다          | SINGLE_SELECT, MULTIPLE_SELECT 타입에 options 누락 |
| 400 | INVALID_ITEM_TYPE           | 유효하지 않은 설문 항목 타입입니다          | 지원하지 않는 설문 항목 타입                              |
| **응답 제출 관련** |
| 400 | INVALID_OPTION_NAME           | 유효하지 않은 옵션 이름입니다             | 없는 옵션 선택                                      |
| 400 | REQUIRED_RESPONSE_MISSING   | 필수 항목이 누락되었습니다               | 필수 응답 항목 미입력                                  |
| 400 | RESPONSE_DUPLICATED   | 응답 항목이 중복되었습니다               | 응답 항목 중복 입력                                   |
| 400 | INVALID_RESPONSE_FORMAT     | 응답 형식이 올바르지 않습니다             | 응답이 문자열 배열이 아님                                |
| 400 | SHORT_TEXT_TOO_LONG         | 단답형 응답은 200자를 초과할 수 없습니다     | SHORT_TEXT 길이 초과                              |
| 400 | LONG_TEXT_TOO_LONG          | 장문형 응답은 2000자를 초과할 수 없습니다    | LONG_TEXT 길이 초과                               |
| 400 | INVALID_SINGLE_SELECT       | 단일 선택 항목에는 하나의 값만 선택할 수 있습니다 | SINGLE_SELECT에 여러 값 또는 잘못된 값                  |
| 400 | INVALID_MULTIPLE_SELECT     | 다중 선택 항목의 값이 유효하지 않습니다       | MULTIPLE_SELECT에 options에 없는 값                |
| 400 | MULTIPLE_SELECT_REQUIRED    | 다중 선택 항목에는 최소 하나의 값이 필요합니다   | MULTIPLE_SELECT에 빈 배열                         |
| **403 - Forbidden** |
| 403 | SURVEY_CLOSED               | 마감된 설문조사입니다                  | 설문이 종료됨                                       |
| **404 - Not Found** |
| 404 | NOT_FOUND_SURVEY            | 설문조사를 찾을 수 없습니다              | 존재하지 않는 설문조사 ID                               |
| 404 | NOT_FOUND_SURVEY_ITEM       | 설문 항목을 찾을 수 없습니다             | 존재하지 않는 설문 항목 ID                              |
| 404 | NOT_FOUND_RESPONSE          | 응답을 찾을 수 없습니다                | 존재하지 않는 응답 ID                                 |
| **500 - Internal Server Error** |
| 500 | INTERNAL_SERVER_ERROR       | 서버 내부 오류가 발생했습니다             | 예상치 못한 서버 오류                                  |

---

## 1. 설문조사 생성 API

### `POST /api/surveys`

설문조사를 새로 생성합니다.

#### Request Body
```json
{
  "surveyName": "고객 만족도 조사",
  "surveyDescription": "서비스 개선을 위한 고객 만족도 조사입니다.",
  "items": [
    {
      "itemName": "이름",
      "itemDescription": "귀하의 성함을 입력해주세요",
      "itemType": "SHORT_TEXT",
      "isRequired": true
    },
    {
      "itemName": "연령대",
      "itemDescription": "귀하의 연령대를 선택해주세요",
      "itemType": "SINGLE_SELECT",
      "isRequired": true,
      "options": [
        {
          "optionName": "10대"
        },
        {
          "optionName": "20대"
        },
        {
          "optionName": "30대"
        }
      ]
    },
    {
      "itemName": "개선사항",
      "itemDescription": "개선이 필요한 부분을 모두 선택해주세요",
      "itemType": "MULTIPLE_SELECT",
      "hasOtherOption": true,
      "options": [
        {
          "optionName": "속도"
        },
        {
          "optionName": "기능"
        },
        {
          "optionName": "고객지원"
        },
        {
          "optionName": "가격"
        }
      ]
    },
    {
      "itemName": "추가의견",
      "itemDescription": "추가로 전달하고 싶은 의견이 있으시면 자유롭게 작성해주세요",
      "itemType": "LONG_TEXT"
    }
  ]
}
```

#### Request Body 필드 설명

| 필드                                | 타입 | 필수 | 설명                                                            |
|-----------------------------------|------|---|---------------------------------------------------------------|
| `surveyName`                      | string | O | 설문조사 이름 (1-200자)                                              |
| `surveyDescription`               | string | X | 설문조사 설명 (1-2000자)                                             |
| `items`                           | array | O | 설문 항목 배열 (1-10개)                                              |
| `items[].itemName`                | string | O | 항목 이름 (1-200자)                                                |
| `items[].itemDescription`         | string | X | 항목 설명 (최대 2000자)                                              |
| `items[].itemType`                | string | O | 입력 형태 (SHORT_TEXT, LONG_TEXT, SINGLE_SELECT, MULTIPLE_SELECT) |
| `items[].isRequired`              | boolean | X | 필수 여부 (기본값 : false)                                           |
| `items[].hasOtherOption`          | boolean | X | 기타 옵션 여부 (기본값 : false)                                        |
| `items[].options`                 | array | △ | 선택 옵션 (SINGLE_SELECT, MULTIPLE_SELECT인 경우 필수)                 |
| `items[].options[].optionName`    | string | O | 옵션 이름                                                         |

#### Response (성공)
```json
{
  "status": 201,
  "message": "설문조사가 생성되었습니다.",
  "path": "/api/surveys",
  "timestamp": "2025-06-14T10:30:00",
  "error": null,
  "data": {
    "surveyId": "SUR1234567890",
    "surveyName": "고객 만족도 조사",
    "surveyDescription": "서비스 개선을 위한 고객 만족도 조사입니다.",
    "items": [
      {
        "itemId": "ITM1234567890",
        "itemName": "이름",
        "itemDescription": "귀하의 성함을 입력해주세요",
        "itemType": "SHORT_TEXT",
        "isRequired": true,
        "hasOtherOption": false,
        "options": []
      },
      {
        "itemId": "ITM1234567891",
        "itemName": "연령대",
        "itemDescription": "귀하의 연령대를 선택해주세요",
        "itemType": "SINGLE_SELECT",
        "isRequired": true,
        "hasOtherOption": false,
        "options": [
          {
            "optionId": "OPT1234567890",
            "optionName": "10대"
          },
          {
            "optionId": "OPT1234567891",
            "optionName": "20대"
          },
          {
            "optionId": "OPT1234567892",
            "optionName": "30대"
          }
        ]
      }
      // ... 나머지 항목들
    ]
  }
}
```

---

## 2. 설문조사 조회 API

### `GET /api/surveys/{surveyId}`

특정 설문조사 항목을 조회합니다.

#### Path Parameters
| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `surveyId` | string | 설문조사 ID (CHAR(13)) |

#### Response (성공)
```json
{
  "status": 200,
  "message": "설문조사 항목 조회가 완료되었습니다.",
  "path": "/api/surveys/SUR1234567890",
  "timestamp": "2025-06-14T15:00:00",
  "error": null,
  "data": {
    "surveyId": "SUR1234567890",
    "surveyName": "고객 만족도 조사",
    "surveyDescription": "서비스 개선을 위한 고객 만족도 조사입니다.",
    "items": [
      {
        "itemId": "ITM1234567890",
        "itemName": "이름",
        "itemDescription": "귀하의 성함을 입력해주세요",
        "itemType": "SHORT_TEXT",
        "isRequired": true,
        "hasOtherOption": false,
        "options": []
      },
      {
        "itemId": "ITM1234567891",
        "itemName": "연령대",
        "itemDescription": "귀하의 연령대를 선택해주세요",
        "itemType": "SINGLE_SELECT",
        "isRequired": true,
        "hasOtherOption": false,
        "options": [
          {
            "optionId": "OPT1234567890",
            "optionName": "10대",
            "isDeleted": false
          },
          {
            "optionId": "OPT1234567891",
            "optionName": "20대",
            "isDeleted": false
          },
          {
            "optionId": "OPT1234567892",
            "optionName": "30대",
            "isDeleted": false
          }
        ]
      }
      // ... 나머지 항목들
    ]
  }
}
```

---

## 3. 설문조사 수정 API

### `PUT /api/surveys/{surveyId}`

기존 설문조사를 수정합니다.

#### Path Parameters
| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `surveyId` | string | 설문조사 ID (CHAR(13)) |

#### Request Body
```json
{
  "surveyName": "수정된 고객 만족도 조사",
  "surveyDescription": "업데이트된 서비스 개선을 위한 고객 만족도 조사입니다.",
  "items": [
    {
      "itemName": "이름",
      "itemDescription": "귀하의 성함을 입력해주세요",
      "itemType": "SHORT_TEXT",
      "isRequired": true
    },
    {
      "itemName": "연령대",
      "itemDescription": "귀하의 연령대를 선택해주세요",
      "itemType": "SINGLE_SELECT",
      "isRequired": true,
      "options": [
        {
          "optionName": "10대"
        },
        {
          "optionName": "20대"
        },
        {
          "optionName": "30대"
        },
        {
          "optionName": "40대 이상"
        }
      ]
    }
    // ... 수정된 항목들
  ]
}
```

#### Response (성공)
```json
{
  "status": 200,
  "message": "설문조사가 수정되었습니다.",
  "path": "/api/surveys/SUR1234567890",
  "timestamp": "2025-06-14T11:15:00",
  "error": null,
  "data": {
    "surveyId": "SUR1234567890",
    "surveyName": "수정된 고객 만족도 조사",
    "surveyDescription": "업데이트된 서비스 개선을 위한 고객 만족도 조사입니다.",
    "items": [
      // 수정된 항목들
    ]
  }
}
```

---

## 4. 설문조사 응답 제출 API

### `POST /api/surveys/{surveyId}/responses`

설문조사에 대한 응답을 제출합니다.

#### Path Parameters
| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `surveyId` | string | 설문조사 ID (CHAR(13)) |

#### Request Body
```json
{
  "responseUser": "user123",
  "answers": [
    {
      "itemId": "ITM1234567890",
      "answer": "홍길동"
    },
    {
      "itemId": "ITM1234567891",
      "options": ["30대"]
    },
    {
      "itemId": "ITM1234567893",
      "options": ["속도", "가격"],
      "otherValue": "모바일 UI"
    },
    {
      "itemId": "ITM1234567894",
      "answer": "전반적으로 만족스러우나 모바일 UI 개선이 필요할 것 같습니다."
    }
  ]
}
```

#### Request Body 필드 설명
| 필드                            | 타입      | 필수 | 설명       |
|-------------------------------|---------|----|----------|
| `responseUser`                | string  | X  | 응답자 식별자 (최대 100자) |
| `answers`                     | array   | O  | 응답 배열    |
| `answers[].itemId`            | string  | O  | 설문 항목 ID (CHAR(13)) |
| `answers[].answer`            | string  | △  | 단답, 장문 응답 값 |
| `answers[].options`           | 문자열 배열  | △  | 옵션 이름    |
| `answers[].otherValue`        | string  | X  | 기타 값     |

#### Response (성공)
```json
{
  "status": 201,
  "message": "응답이 제출되었습니다.",
  "path": "/api/surveys/SUR1234567890/responses",
  "timestamp": "2025-06-14T14:30:00",
  "error": null,
  "data": {
    "responseId": "RES1234567890",
    "responseUser": "user123",
    "createdDate": "2025-06-14T14:30:00",
    "answers": [
      {
        "answerId": "ANS1234567890",
        "itemName": "이름",
        "answer": ["홍길동"]
      },
      {
        "answerId": "ANS1234567891",
        "itemName": "연령대",
        "answer": ["30대"]
      }
      // ... 나머지 응답들
    ]
  }
}
```

---

## 5. 설문조사 응답 조회 API

### `GET /api/surveys/{surveyId}/responses`

특정 설문조사의 모든 응답을 조회합니다.

#### Path Parameters
| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `surveyId` | string | 설문조사 ID (CHAR(13)) |

#### Query Parameters (Advanced - 검색 기능)
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `itemName` | string | X | 검색할 항목 이름 |
| `answerValue` | string | X | 검색할 응답 값 |
| `responseUser` | string | X | 응답자 식별자 |
| `page` | int | X | 페이지 번호 (기본값: 0) |
| `size` | int | X | 페이지 크기 (기본값: 20) |

#### Response (성공)
```json
{
  "status": 200,
  "message": "응답 조회가 완료되었습니다.",
  "path": "/api/surveys/SUR1234567890/responses",
  "timestamp": "2025-06₩-14T15:00:00",
  "error": null,
  "data": {
    "content": [
      {
        "responseId": "RES1234567890",
        "responseUser": null,
        "createdDate": "2025-06-14T14:30:00",
        "answers": [
          {
            "answerId": "ANS1234567890",
            "itemName": "이름",
            "answer": ["홍길동"]
          },
          {
            "answerId": "ANS1234567891",
            "itemName": "연령대",
            "answer": ["30대"]
          }
          // ... 나머지 응답들
        ]
      }
      // ... 다른 응답들
    ],
    "page": 0,
    "size": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": true
  }
}
```

---

## 유효성 검사 규칙

### 설문조사 생성/수정 시
- 설문조사, 항목 이름: 1-200자 필수
- 설문조사, 항목 설명: 1-2000자
- 설문 항목: 1-10개 필수
- SINGLE_SELECT, MULTIPLE_SELECT인 경우 options 필수

### 응답 제출 시
- 필수 항목은 반드시 응답 값 포함
- SHORT_TEXT: 최대 200자
- LONG_TEXT: 최대 2000자
- SINGLE_SELECT: options 중 하나의 값
- MULTIPLE_SELECT: options 중 하나 이상의 값
