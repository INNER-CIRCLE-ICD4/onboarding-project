## API 설계

### API 버전 관리
- **Base URL**: `/api/v1`
- **인증**: Bearer Token (필요시)
- **Content-Type**: `application/json`

---

### 1. 설문조사 생성 API

#### 엔드포인트
```
POST /api/v1/surveys
```

#### 요청 예시
```json
{
  "title": "고객 만족도 조사",
  "description": "서비스 개선을 위한 고객 만족도 조사입니다.",
  "questions": [
    {
      "name": "이름",
      "description": "응답자의 이름을 입력해주세요",
      "inputType": "SHORT_TEXT",
      "isRequired": true,
      "order": 1
    },
    {
      "name": "만족도",
      "description": "전반적인 서비스 만족도를 선택해주세요",
      "inputType": "SINGLE_CHOICE",
      "isRequired": true,
      "options": ["매우 만족", "만족", "보통", "불만족", "매우 불만족"],
      "order": 2
    },
    {
      "name": "개선사항",
      "description": "개선이 필요한 부분을 자유롭게 작성해주세요",
      "inputType": "LONG_TEXT",
      "isRequired": false,
      "order": 3
    }
  ]
}
```

#### 응답 예시
```json
{
  "success": true,
  "data": {
    "surveyId": "01b23456-789a-bcde-f012-3456789abcde",
    "title": "고객 만족도 조사",
    "description": "서비스 개선을 위한 고객 만족도 조사입니다.",
    "version": 1,
    "questions": [
      {
        "id": "01b23456-789a-bcde-f012-3456789abcdf",
        "name": "이름",
        "description": "응답자의 이름을 입력해주세요",
        "inputType": "SHORT_TEXT",
        "isRequired": true,
        "order": 1
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce0",
        "name": "만족도",
        "description": "전반적인 서비스 만족도를 선택해주세요",
        "inputType": "SINGLE_CHOICE",
        "isRequired": true,
        "order": 2,
        "options": [
          {"id": "01b23456-789a-bcde-f012-3456789abce1", "text": "매우 만족", "order": 1},
          {"id": "01b23456-789a-bcde-f012-3456789abce2", "text": "만족", "order": 2},
          {"id": "01b23456-789a-bcde-f012-3456789abce3", "text": "보통", "order": 3},
          {"id": "01b23456-789a-bcde-f012-3456789abce4", "text": "불만족", "order": 4},
          {"id": "01b23456-789a-bcde-f012-3456789abce5", "text": "매우 불만족", "order": 5}
        ]
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce6",
        "name": "개선사항",
        "description": "개선이 필요한 부분을 자유롭게 작성해주세요",
        "inputType": "LONG_TEXT",
        "isRequired": false,
        "order": 3
      }
    ],
    "createdAt": "2025-06-10T10:00:00Z"
  }
}
```

---

### 2. 설문조사 수정 API

#### 엔드포인트
```
PUT /api/v1/surveys/{surveyId}
```

#### 핵심 동작 원리
1. 새로운 버전의 `survey_version` 레코드 생성
2. `surveys` 테이블의 `current_version` 업데이트

#### 요청 예시 (변경사항만 포함)
```json
{
  "title": "고객 만족도 조사 (개정판)",
  "questions": [
    {
      "operation": "UPDATE",
      "questionId": "01b23456-789a-bcde-f012-3456789abce0",
      "name": "만족도",
      "description": "전반적인 서비스 만족도를 선택해주세요",
      "inputType": "SINGLE_CHOICE",
      "isRequired": true,
      "options": ["매우 만족", "만족", "보통", "불만족", "매우 불만족"],
      "order": 2
    },
    {
      "operation": "CREATE",
      "name": "추천 의향",
      "description": "지인에게 추천할 의향이 있으신가요?",
      "inputType": "SINGLE_CHOICE",
      "isRequired": true,
      "options": ["적극 추천", "추천", "보통", "비추천"],
      "order": 4
    },
    {
      "operation": "DELETE",
      "questionId": "01b23456-789a-bcde-f012-3456789abce6"
    }
  ]
}
```

#### 응답 예시
```json
{
  "success": true,
  "data": {
    "surveyId": "01b23456-789a-bcde-f012-3456789abcde",
    "title": "고객 만족도 조사 (개정판)",
    "description": "서비스 개선을 위한 고객 만족도 조사입니다.",
    "version": 2,
    "previousVersion": 1,
    "operations": [
      {
        "operation": "UPDATE",
        "questionId": "01b23456-789a-bcde-f012-3456789abce0",
        "newQuestionId": "01b23456-789a-bcde-f012-3456789abce7",
        "status": "success"
      },
      {
        "operation": "CREATE",
        "newQuestionId": "01b23456-789a-bcde-f012-3456789abce8",
        "status": "success"
      },
      {
        "operation": "DELETE",
        "questionId": "01b23456-789a-bcde-f012-3456789abce6",
        "status": "success"
      }
    ],
    "questions": [
      {
        "id": "01b23456-789a-bcde-f012-3456789abce9",
        "name": "이름",
        "description": "응답자의 이름을 입력해주세요",
        "inputType": "SHORT_TEXT",
        "isRequired": true,
        "order": 1
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce7",
        "name": "만족도",
        "description": "전반적인 서비스 만족도를 선택해주세요",
        "inputType": "SINGLE_CHOICE",
        "isRequired": true,
        "order": 2,
        "options": [
          {"id": "01b23456-789a-bcde-f012-3456789abcea", "text": "매우 만족", "order": 1},
          {"id": "01b23456-789a-bcde-f012-3456789abceb", "text": "만족", "order": 2},
          {"id": "01b23456-789a-bcde-f012-3456789abcec", "text": "보통", "order": 3},
          {"id": "01b23456-789a-bcde-f012-3456789abced", "text": "불만족", "order": 4},
          {"id": "01b23456-789a-bcde-f012-3456789abcee", "text": "매우 불만족", "order": 5}
        ]
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce8",
        "name": "추천 의향",
        "description": "지인에게 추천할 의향이 있으신가요?",
        "inputType": "SINGLE_CHOICE",
        "isRequired": true,
        "order": 4,
        "options": [
          {"id": "01b23456-789a-bcde-f012-3456789abcef", "text": "적극 추천", "order": 1},
          {"id": "01b23456-789a-bcde-f012-3456789abcf0", "text": "추천", "order": 2},
          {"id": "01b23456-789a-bcde-f012-3456789abcf1", "text": "보통", "order": 3},
          {"id": "01b23456-789a-bcde-f012-3456789abcf2", "text": "비추천", "order": 4}
        ]
      }
    ],
    "updatedAt": "2025-06-10T11:00:00Z"
  }
}
```

---

### 3. 설문조사 응답 제출 API

#### 엔드포인트
```
POST /api/v1/surveys/{surveyId}/responses
```

#### 요청 예시
```json
{
  "respondentEmail": "user@example.com", // optional
  "answers": [
    {
      "questionId": "01b23456-789a-bcde-f012-3456789abce9",
      "answerText": "홍길동"
    },
    {
      "questionId": "01b23456-789a-bcde-f012-3456789abce7",
      "selectedOptionIds": ["01b23456-789a-bcde-f012-3456789abceb"] // "만족" 선택
    },
    {
      "questionId": "01b23456-789a-bcde-f012-3456789abce8",
      "selectedOptionIds": ["01b23456-789a-bcde-f012-3456789abcf0"] // "추천" 선택
    }
  ]
}
```

#### 응답 예시
```json
{
  "success": true,
  "data": {
    "responseId": "01b23456-789a-bcde-f012-3456789abcf3",
    "surveyId": "01b23456-789a-bcde-f012-3456789abcde",
    "surveyVersion": 2,
    "respondentEmail": "user@example.com",
    "submittedAt": "2025-06-10T12:00:00Z",
    "answers": [
      {
        "questionId": "01b23456-789a-bcde-f012-3456789abce9",
        "questionName": "이름",
        "answerText": "홍길동"
      },
      {
        "questionId": "01b23456-789a-bcde-f012-3456789abce7",
        "questionName": "만족도",
        "selectedOptions": [
          {"id": "01b23456-789a-bcde-f012-3456789abceb", "text": "만족"}
        ]
      },
      {
        "questionId": "01b23456-789a-bcde-f012-3456789abce8",
        "questionName": "추천 의향",
        "selectedOptions": [
          {"id": "01b23456-789a-bcde-f012-3456789abcf0", "text": "추천"}
        ]
      }
    ]
  }
}
```

---

### 4. 설문조사 응답 조회 API

#### 엔드포인트
```
GET /api/v1/surveys/{surveyId}/responses
```

#### 쿼리 파라미터
| 파라미터 | 타입 | 설명 | 기본값 |
|---------|------|------|--------|
| `questionName` | `string` | 특정 질문명으로 필터링 | - |
| `answerText` | `string` | 응답 내용으로 검색 | - |
| `page` | `integer` | 페이지 번호 | 1 |
| `limit` | `integer` | 페이지 크기 | 20 |

#### 요청 예시
```
GET /api/v1/surveys/01b23456-789a-bcde-f012-3456789abcde/responses?page=1&limit=10
```

#### 응답 예시
```json
{
  "success": true,
  "data": {
    "surveyId": "01b23456-789a-bcde-f012-3456789abcde",
    "surveyTitle": "고객 만족도 조사 (개정판)",
    "totalResponses": 25,
    "currentPage": 1,
    "totalPages": 3,
    "responses": [
      {
        "responseId": "01b23456-789a-bcde-f012-3456789abcf3",
        "version": 2,
        "respondentEmail": "user@example.com",
        "submittedAt": "2025-06-10T12:00:00Z",
        "answers": [
          {
            "questionId": "01b23456-789a-bcde-f012-3456789abce9",
            "questionName": "이름",
            "questionType": "SHORT_TEXT",
            "answerText": "홍길동",
            "isDeleted": false
          },
          {
            "questionId": "01b23456-789a-bcde-f012-3456789abce7",
            "questionName": "만족도",
            "questionType": "SINGLE_CHOICE",
            "selectedOptions": [
              {"id": "01b23456-789a-bcde-f012-3456789abceb", "text": "만족"}
            ],
            "isDeleted": false
          },
          {
            "questionId": "01b23456-789a-bcde-f012-3456789abce8",
            "questionName": "추천 의향",
            "questionType": "SINGLE_CHOICE",
            "selectedOptions": [
              {"id": "01b23456-789a-bcde-f012-3456789abcf0", "text": "추천"}
            ],
            "isDeleted": false
          }
        ]
      },
      {
        "responseId": "01b23456-789a-bcde-f012-3456789abcf4",
        "version": 1,
        "respondentEmail": "kim@example.com",
        "submittedAt": "2025-06-09T15:30:00Z",
        "answers": [
          {
            "questionId": "01b23456-789a-bcde-f012-3456789abcdf",
            "questionName": "이름",
            "questionType": "SHORT_TEXT",
            "answerText": "김철수",
            "isDeleted": false
          },
          {
            "questionId": "01b23456-789a-bcde-f012-3456789abce0",
            "questionName": "만족도",
            "questionType": "SINGLE_CHOICE",
            "selectedOptions": [
              {"id": "01b23456-789a-bcde-f012-3456789abce3", "text": "보통"}
            ],
            "isDeleted": false
          },
          {
            "questionId": "01b23456-789a-bcde-f012-3456789abce6",
            "questionName": "개선사항",
            "questionType": "LONG_TEXT",
            "answerText": "UI가 더 직관적이었으면 좋겠습니다.",
            "isDeleted": true,
            "versionNote": "이 질문은 버전 2에서 삭제되었습니다."
          }
        ]
      }
    ]
  }
}
```

---

### 5. 설문조사 조회 API

#### 엔드포인트
```
GET /api/v1/surveys/{surveyId}
```

#### 요청 예시
```
GET /api/v1/surveys/01b23456-789a-bcde-f012-3456789abcde
```

#### 응답 예시
```json
{
  "success": true,
  "data": {
    "surveyId": "01b23456-789a-bcde-f012-3456789abcde",
    "title": "고객 만족도 조사 (개정판)",
    "description": "서비스 개선을 위한 고객 만족도 조사입니다.",
    "currentVersion": 2,
    "questions": [
      {
        "id": "01b23456-789a-bcde-f012-3456789abce9",
        "name": "이름",
        "description": "응답자의 이름을 입력해주세요",
        "inputType": "SHORT_TEXT",
        "isRequired": true,
        "order": 1
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce7",
        "name": "만족도",
        "description": "전반적인 서비스 만족도를 선택해주세요",
        "inputType": "SINGLE_CHOICE",
        "isRequired": true,
        "order": 2,
        "options": [
          {"id": "01b23456-789a-bcde-f012-3456789abcea", "text": "매우 만족", "order": 1},
          {"id": "01b23456-789a-bcde-f012-3456789abceb", "text": "만족", "order": 2},
          {"id": "01b23456-789a-bcde-f012-3456789abcec", "text": "보통", "order": 3},
          {"id": "01b23456-789a-bcde-f012-3456789abced", "text": "불만족", "order": 4},
          {"id": "01b23456-789a-bcde-f012-3456789abcee", "text": "매우 불만족", "order": 5}
        ]
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce8",
        "name": "추천 의향",
        "description": "지인에게 추천할 의향이 있으신가요?",
        "inputType": "SINGLE_CHOICE",
        "isRequired": true,
        "order": 4,
        "options": [
          {"id": "01b23456-789a-bcde-f012-3456789abcef", "text": "적극 추천", "order": 1},
          {"id": "01b23456-789a-bcde-f012-3456789abcf0", "text": "추천", "order": 2},
          {"id": "01b23456-789a-bcde-f012-3456789abcf1", "text": "보통", "order": 3},
          {"id": "01b23456-789a-bcde-f012-3456789abcf2", "text": "비추천", "order": 4}
        ]
      }
    ],
    "createdAt": "2025-06-10T10:00:00Z",
    "updatedAt": "2025-06-10T11:00:00Z"
  }
}
```

---

### 6. 설문조사 목록 조회 API

#### 엔드포인트
```
GET /api/v1/surveys
```

#### 요청 예시
```
GET /api/v1/surveys
```

#### 응답 예시
```json
{
  "success": true,
  "data": [{
    "surveyId": "01b23456-789a-bcde-f012-3456789abcde",
    "title": "고객 만족도 조사 (개정판)",
    "description": "서비스 개선을 위한 고객 만족도 조사입니다.",
    "currentVersion": 2,
    "questions": [
      {
        "id": "01b23456-789a-bcde-f012-3456789abce9",
        "name": "이름",
        "description": "응답자의 이름을 입력해주세요",
        "inputType": "SHORT_TEXT",
        "isRequired": true,
        "order": 1
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce7",
        "name": "만족도",
        "description": "전반적인 서비스 만족도를 선택해주세요",
        "inputType": "SINGLE_CHOICE",
        "isRequired": true,
        "order": 2,
        "options": [
          {"id": "01b23456-789a-bcde-f012-3456789abcea", "text": "매우 만족", "order": 1},
          {"id": "01b23456-789a-bcde-f012-3456789abceb", "text": "만족", "order": 2},
          {"id": "01b23456-789a-bcde-f012-3456789abcec", "text": "보통", "order": 3},
          {"id": "01b23456-789a-bcde-f012-3456789abced", "text": "불만족", "order": 4},
          {"id": "01b23456-789a-bcde-f012-3456789abcee", "text": "매우 불만족", "order": 5}
        ]
      },
      {
        "id": "01b23456-789a-bcde-f012-3456789abce8",
        "name": "추천 의향",
        "description": "지인에게 추천할 의향이 있으신가요?",
        "inputType": "SINGLE_CHOICE",
        "isRequired": true,
        "order": 4,
        "options": [
          {"id": "01b23456-789a-bcde-f012-3456789abcef", "text": "적극 추천", "order": 1},
          {"id": "01b23456-789a-bcde-f012-3456789abcf0", "text": "추천", "order": 2},
          {"id": "01b23456-789a-bcde-f012-3456789abcf1", "text": "보통", "order": 3},
          {"id": "01b23456-789a-bcde-f012-3456789abcf2", "text": "비추천", "order": 4}
        ]
      }
    ],
    "createdAt": "2025-06-10T10:00:00Z",
    "updatedAt": "2025-06-10T11:00:00Z"
  }]
}
```

---


