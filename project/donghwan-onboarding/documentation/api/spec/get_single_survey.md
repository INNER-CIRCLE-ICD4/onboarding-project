# GET /api/v1/surveys/{surveyId}

## Response

### ResponseBody

| 필드명        | 타입                                      | 필수 | 제약조건 |
|------------|-----------------------------------------|----|------|
| surveyInfo | [Object](../dto/SurveyInfoDto.md)       | 필수 | -    |
| items      | [List](../dto/SurveyQuestionItemDto.md) | 필수 | -    |

### ResponseExample
```text
'Success Case'

Status: 200 Ok
Body: {
  "surveyInfo": {
    "surveyId": 100,
    "surveyVersionId": 1,
    "surveyTitle": "2024년 개발자 설문조사",
    "surveyDescription": "개발자들의 선호도를 조사합니다",
    "versionNumber": 1,
    "createdAt": "2024-06-14T10:00:00"
  },
  "items": [
    {
      "title": "개발자 닉네임을 입력해주세요",
      "description": "온라인에서 사용하는 닉네임을 알려주세요",
      "type": "SHORT_ANSWER",
      "order": 1,
      "required": true
    },
    {
      "title": "본인의 개발 철학을 설명해주세요",
      "description": "당신이 개발할 때 가장 중요하게 생각하는 것은 무엇인가요?",
      "type": "LONG_ANSWER",
      "order": 2,
      "required": true
    },
    {
      "title": "주력 프로그래밍 언어는 무엇인가요?",
      "description": "가장 자신있는 언어를 선택해주세요",
      "type": "SINGLE_CHOICE",
      "order": 3,
      "required": true,
      "options": [
        {
          "content": "Java",
          "order": 1
        },
        {
          "content": "Python",
          "order": 2
        },
        {
          "content": "JavaScript",
          "order": 3
        }
      ]
    },
    {
      "title": "사용 가능한 기술 스택을 모두 선택해주세요",
      "description": "실무에서 활용 가능한 수준의 기술을 선택해주세요",
      "type": "MULTIPLE_CHOICE",
      "order": 4,
      "required": false,
      "options": [
        {
          "content": "Spring Framework",
          "order": 1
        },
        {
          "content": "JPA",
          "order": 2
        },
        {
          "content": "MySQL",
          "order": 3
        }
      ]
    }
  ]
}
```

```text
'Failure Case'

Status: 404 Not Found
Body: {}

Status: 409 Conflict
Body: {}
```