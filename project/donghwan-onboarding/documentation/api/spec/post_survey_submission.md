# POST /api/v1/surveys{surveyId}/versions/{versionId}

## Request

### RequestBody

| 필드명     | 타입                                | 필수 | 제약조건 |
|---------|-----------------------------------|----|------|
| answers | [List](../dto/SurveyAnswerDto.md) | 필수 | -    |

### RequestExample
```text
{
  "answers": [
    {
      "questionItemId": 1,
      "questionItemType": "SHORT_ANSWER",
      "questionItemValue": "코딩하는 펭귄"
    },
    {
      "questionItemId": 2,
      "questionItemType": "LONG_ANSWER", 
      "questionItemValue": "클린 코드와 지속적인 리팩토링을 중요하게 생각합니다. 또한 테스트 커버리지를 높게 유지하면서 안정적인 코드를 작성하려고 노력합니다."
    },
    {
      "questionItemId": 3,
      "questionItemType": "SINGLE_CHOICE",
      "questionItemValue": ["IntelliJ IDEA"]
    },
    {
      "questionItemId": 4,
      "questionItemType": "MULTIPLE_CHOICE",
      "questionItemValue": ["Spring Framework", "JPA", "MySQL"]
    }
  ]
}
```
---

## Response

### ResponseBody

```text

```

### ResponseExample

```text
'Success Case'

Status: 201 Created
Header: [
    Location: /api/v1/surveys/{surveyId}/submissions/{submissionId}
]
Body: {}
```

```text
'Failure Case'

Status: 400 Bad Request
Body: {}

Status: 409 Bad Request
Body: {}
```