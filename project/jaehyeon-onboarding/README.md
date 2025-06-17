## 버전 정보
springboot version: 3.5.0


## 테이블 설계

### 설문(survey)
| column         | type      | description      |
|----------------|-----------|------------------|
| id             | BIGINT    | PK               |
| name           | VARCHAR   | 설문명              |
| description    | VARCHAR   | 설문 설명            |
| is_deleted     | BOOLEAN   | soft delete flag |
| latest_version | INT       |                  |
| created_at     | TIMESTAMP |                  |
| updated_at    | TIMESTAMP |                  |


### 설문 항목 그룹(question_group)
| field      | type   | description |
|------------|--------|-------------|
| id         | BIGINT | PK          |
| survey_id  | BIGINT | FK(survey)  |
| version    | INT    | 버전          |
| created_at | TIMESTAMP |             |

### 설문 항목(question)
| field             | type    | description                             |
|-------------------|---------|-----------------------------------------|
| id                | BIGINT  | PK                                      |
| question_group_id | BIGINT  | FK(question_group)                      |
| position          | INT     | 항목 순서                                     |
| description       | VARCHAR | 설문 항목 설명                                |
| label             | VARCHAR | 설문 항목 표시 문구                             |
| input_type        | VARCHAR | 입력 타입(text, long_text, radio, checkbox) |
| required          | BOOLEAN | 필수 여부                                   |
| options           | VARCHAR | 선택지(json array)                         |
| created_at     | TIMESTAMP |                                         |

### 응답(answer)
| field             | type   | description  |
|-------------------|--------|--------------|
| id                | BIGINT | PK           |
| question_group_id | BIGINT | FK(question) |
| content           | TEXT   | 설문 응답 내용  |
| created_at     | TIMESTAMP |              | 


## API 설계

- 응답 공통

| field             | type   | description  |
|-------------------|--------|--------------|
| code              | INT    | response code |
| message           | STRING | response message |
| data              | OBJECT | response data |

- 응답 코드
  - 10000 : Success
  - 2xxxx : Client Error
  - 3xxxx : Server Error


### 설문 등록 API
- uri : /v1/surveys
- method : POST

| field                  | type             | required | description |
|------------------------|------------------|----------|-------------|
| name                   | STRING           | Y        | 설문명        |
| description            | STRING           | N        | 설문 설명      |
| questions              | List\<Question\> | Y | 설문 항목 리스트 |
| - question.label       | STRING           | Y        | 항목 표시 문구  |
| - question.description | STRING           | N        | 항목 설명      |
| - question.input_type  | ENUM             | Y        | 입력 타입(text, long_text, radio, checkbox) |
| - question.required    | BOOLEAN          | Y        | 필수 여부     |
| - question.options     | List\<String\>   | N | 선택지(json array) |

```curl

# Request
POST /v1/surveys
{
    "name": "설문1",
    "description": "",
    "questions": [
         {
            "label": "성명을 입력해주세요.",
            "description": "",
            "input_type": "text",
            "required": true
        }
        , {
            "label": "어떤 경로로 알게 되셨나요?",
            "description": "",
            "input_type": "radio",
            "required": false,
            "options": ["웹 검색", "SNS", "지인을 통해"]
        }, {
            "label": "관심사를 선택해주세요.",
            "description": "", 
            "input_type": "checkbox",
            "required": false,
            "options": ["독서", "TV시청", "게임", "영화"]
        }
    ]
}

# Response
{
    "code": 10000,
    "message": "Success",
    "data": null
}
```

### 설문 수정 API
- uri: /v1/surveys/{surveyId}
- method: PUT

| field                  | type             | required | description |
|------------------------|------------------|----------|-------------|
| name                   | STRING           | N        | 설문명        |
| description            | STRING           | N        | 설문 설명      |
| questions              | List\<Question\> | N | 설문 항목 리스트 |
| - question.label       | STRING           | N        | 항목 표시 문구  |
| - question.description | STRING           | N        | 항목 설명      |
| - question.input_type  | ENUM             | N        | 입력 타입(text, long_text, radio, checkbox) |
| - question.required    | BOOLEAN          | N        | 필수 여부     |
| - question.options     | List\<String\>   | N | 선택지(json array) |

```curl
PUT /v1/surveys/{surveyId}
{
    "name": "설문1-수정",
    "description": "수정테스트",
    "version": 1,
    "questions": [
         {
            "label": "성명을 입력해주세요.",
            "description": "",
            "input_type": "text",
            "required": true
        }
        , {
            "label": "어떤 경로로 알게 되셨나요?",
            "description": "",
            "input_type": "radio",
            "required": false,
            "options": ["웹 검색", "지인을 통해", "소셜미디어", "기타"]
        }
        , {
            "label": "관심사",
            "description": "",
            "input_type": "checkbox",
            "required": false,
            "options": ["독서", "TV시청", "게임", "영화", "여행", "기타"]
        }
    ]
}

# Response
{
    "code": 10000,
    "message": "Success",
    "data": null
}
```

### 설문 응답 제출 API
- uri: /v1/surveys/{surveyId}/answers
- method: POST

| field                    | type             | required | description                             |
|--------------------------|------------------|----------|-----------------------------------------|
| question_group_id        | BIGINT           | Y        | 항목 그룹 ID                                |
| questions                | List\<Question\> | Y | 설문 항목 리스트                               |
| - question.label         | STRING           | Y        | 항목 표시 문구                                |
| - question.description   | STRING           | N        | 항목 설명                                   |
| - question.input_type    | ENUM             | Y        | 입력 타입(text, long_text, radio, checkbox) |
| - question.required      | BOOLEAN          | Y        | 필수 여부                                   |
| - question.options       | List\<String\>   | N | 선택지(json array)                         |
| - question.answer        | List\<Answer\>   | Y | 설문 응답 내용                                |
| - qustion.answer.content | STRING           | Y        | 설문 응답 내용                                |

```curl
# Request
POST /v1/surveys/{surveyId}/answers
{
    "question_group_id": 2,
    "questions": [
        {
            "label": "성명을 입력해주세요.",
            "description": "",
            "input_type": "text",
            "required": true,
            "answer":  {"content": "김재현" }
        }
        , {
            "label": "어떤 경로로 알게 되셨나요?",
            "description": "",
            "input_type": "radio",
            "required": false,
            "options": ["웹 검색", "지인을 통해", "소셜미디어", "기타"],
            "answer": { "content" : "웹 검색" }
        }
        , {
            "label": "관심사",
            "description": "",
            "input_type": "checkbox",
            "required": false,
            "options": ["독서", "TV시청", "게임", "영화", "여행", "기타"],
            "answer": { "content" :["영화", "여행", "기타:누워있기"] }
        }
    ]
}

# Response
{
    "code": 10000,
    "message": "Success",
    "data": null
}
```

### 설문 응답 조회 API
- uri: /v1/surveys/{surveyId}/answers
- method: GET


```curl
# Request
GET /v1/surveys/{surveyId}/answers

# Response
{
  "code": 10000,
  "message": "Success",
  "data": [
    {
      "question_group_id": 2,
      "version": 2,
      "submitted_at": "2025-06-13 00:00:00",
      "answers": [
        {
          "label": "성명을 입력해주세요.",
          "input_type": "text",
          "required": true,
          "value": "김재현"
        },
        {
          "label": "어떤 경로로 알게 되셨나요?",
          "input_type": "radio",
          "required": false,
          "options": ["웹 검색", "SNS", "지인을 통해"],
          "value": "웹 검색"
        },
        {
          "label": "관심사를 선택해주세요.",
          "input_type": "checkbox",
          "required": false,
          "options": ["독서", "TV시청", "게임", "영화", "여행", "기타"],
          "value": ["영화", "여행", "기타:누워있기"]
        }
      ]
    }
  ]
}
```