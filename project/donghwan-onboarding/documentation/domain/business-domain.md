## 도메인 도출

```
Survey
- id:Long
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```
```
SurveyVersion
- id: Long
- surveyId: Long
- title: String
- description: String
- order: Integer
- createdAt: LocalDateTime
```
```
QuestionItem
- id: Long
- surveyVersionId: Long
- isRequired: Boolean
- order: Integer
- title: String
- description: String
- type: String(Enum: SHORT_ANSWER, LONG_ANSWER, SINGLE_CHOICE, MULTIPLE_CHOICE)
- choiceOptions: List<QuestionChoiceOption>
- createdAt: LocalDateTime
```
```
QuestionChoiceOption
- id: Long
- questionItemId: Long
- order: Integer
- content: String
```
```
SurveySubmission
- id: Long
- surveyVersionId: Long
- submittedAt: LocalDateTime
```
```
AnswerItem
- id: Long
- surveySubmissionId: Long
- questionItemId: Long
- content: String
```