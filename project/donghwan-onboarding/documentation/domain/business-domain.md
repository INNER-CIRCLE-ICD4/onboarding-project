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
- sequecne: Integer
- title: String
- description: String
- createdAt: LocalDateTime
```
```
QuestionItem
- id: Long
- surveyVersionId: Long
- required: Boolean
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
- surveyId: Long
- surveyVersionId: Long
- content: Map<String, Object>
- submittedAt: LocalDateTime
```
```
SurveySubmissionSearch
- id: Long
- surveyId: Long
- surveyVersionId: Long
- surveySubmissionId: Long
- contentOrigin: String(Enum: QEUSTION)
- contentValue: String
```