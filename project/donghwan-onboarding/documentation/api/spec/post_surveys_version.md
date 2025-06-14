# PUT /api/v1/surveys/{surveyId}

## Request
### RequestBody
| 필드명         | 타입                                      | 필수 | 제약조건                      |
|-------------|-----------------------------------------|----|---------------------------|
| title       | String                                  | 필수 | 최대 100자, 한글/영문/숫자/공백만 허용  |
| description | String                                  | 선택 | 최대 1000자, 한글/영문/숫자/공백만 허용 |
| items       | [List](../dto/SurveyQuestionItemDto.md) | 필수 | -                         |
### RequestExample
```text

```
---
## Response
### ResponseBody
```text
{
  "title": "2024년 개발자 설문조사",
  "description": "개발자들의 선호도를 조사합니다",
  "items": [
    {
      "title": "개발자 닉네임을 입력해주세요",
      "description": "온라인에서 사용하는 닉네임을 알려주세요",
      "type": "SHORT_ANSWER",
      "order": 1,
      "required": true
    },
    {
      "title": "개발 경험을 자세히 설명해주세요",
      "description": "프로젝트 경험과 기술 스택에 대해 설명해주세요",
      "type": "LONG_ANSWER",
      "order": 2,
      "required": true
    },
    {
      "title": "선호하는 개발 환경은?",
      "description": "주로 사용하는 IDE를 선택해주세요",
      "type": "SINGLE_CHOICE",
      "order": 3,
      "required": true,
      "options": [
        {
          "content": "IntelliJ IDEA",
          "order": 1
        },
        {
          "content": "Eclipse",
          "order": 2
        },
        {
          "content": "VS Code",
          "order": 3
        }
      ]
    },
    {
      "title": "사용 가능한 프로그래밍 언어",
      "description": "실무에서 사용 가능한 언어를 모두 선택해주세요",
      "type": "MULTIPLE_CHOICE",
      "order": 4,
      "required": false,
      "options": [
        {
          "content": "Java",
          "order": 1
        },
        {
          "content": "Kotlin",
          "order": 2
        },
        {
          "content": "Python",
          "order": 3
        },
        {
          "content": "JavaScript",
          "order": 4
        }
      ]
    }
  ]
}
```
### ResponseExample
```text
'Success Case'

Status: 201 Created
Header: [
    Location: /api/v1/surveys/{id}
]
Body: {}
```

```text
'Failure Case'

Status: 400 Bad Request
Body: {}

Status: 404 Not Found
Body: {}
```