# GET /api/v1/surveys/{surveyId}/submissions/{submissionId}
## Response
### ResponseBody
| 필드명         | 타입                                          | 필수 | 제약조건 |
|-------------|---------------------------------------------|----|------|
| id          | Long                                        | 필수 | -    |
| surveyInfo  | [Object](../dto/SurveyInfoDto.md)           | 필수 | -    |
| answers     | [List](../dto/SurveySubmissionAnswerDto.md) | 필수 | -    |
| submittedAt | LocalDateTime                               | 필수 | -    |
### ResponseExample
```text
'Success Case'

Status: 200 Ok
Body: {
  "id": 1234,
  "surveyInfo": {
    "surveyId": 100,
    "surveyVersionId": 1,
    "surveyTitle": "2024년 개발자 설문조사",
    "surveyDescription": "개발자들의 선호도를 조사합니다",
    "versionNumber": 1,
    "createdAt": "2024-06-14T10:00:00"
  },
  "answers": [
    {
      "questionTitle": "개발자 닉네임을 입력해주세요",
      "questionDescription": "온라인에서 사용하는 닉네임을 알려주세요",
      "questionType": "SHORT_ANSWER",
      "questionContent": "코딩마스터"
    },
    {
      "questionTitle": "본인의 개발 철학을 설명해주세요",
      "questionDescription": "당신이 개발할 때 가장 중요하게 생각하는 것은 무엇인가요?",
      "questionType": "LONG_ANSWER",
      "questionContent": "저는 코드의 가독성과 유지보수성을 가장 중요하게 생각합니다. 복잡한 로직이라도 누구나 이해할 수 있도록 작성하려 노력합니다."
    },
    {
      "questionTitle": "주력 프로그래밍 언어는 무엇인가요?",
      "questionDescription": "가장 자신있는 언어를 선택해주세요",
      "questionType": "SINGLE_CHOICE",
      "questionContent": {
        "content": "Java",
        "order": 2,
        "selected": true
      }
    },
    {
      "questionTitle": "사용 가능한 기술 스택을 모두 선택해주세요",
      "questionDescription": "실무에서 활용 가능한 수준의 기술을 선택해주세요",
      "questionType": "MULTIPLE_CHOICE",
      "questionContent": [
        {
          "content": "Spring Framework",
          "order": 1,
          "selected": true
        },
        {
          "content": "JPA",
          "order": 2,
          "selected": true
        },
        {
          "content": "MySQL",
          "order": 3,
          "selected": false
        }
      ]
    }
  ],
  "submittedAt": "2024-06-14T15:30:00"
}
```
```text
'Failure Case'
Status: 404 Not Found
Body: {}
```