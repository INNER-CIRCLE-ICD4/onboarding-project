
| 필드명         | 타입                                               | 필수         | 제약조건                                                                          |
|-------------|--------------------------------------------------|-------------|---------------------------------------------------------------------------------|
| title       | String                                           | 필수         | 최대 50자, 한글/영문/숫자/공백만 허용                                                   |
| description | String                                           | 선택         | 최대 500자, 한글/영문/숫자/공백만 허용                                                  |
| type        | String                                           | 필수         | • SHORT_ANSWER<br>• LONG_ANSWER<br>• SINGLE_CHOICE<br>• MULTIPLE_CHOICE         |
| order       | Integer                                          | 필수         | -                                                                               |
| required    | Boolean                                          | 필수         | -                                                                               |
| options     | [List[Object]](SurveyQuestionSelectOptionDto.md) | 조건부        | 선택형 질문의 경우 필수                                                              |

### 추가 제약사항
- 선택형 질문(SINGLE_CHOICE, MULTIPLE_CHOICE)의 경우
    - options는 최소 2개, 최대 5개까지 가능
- SHORT_ANSWER 타입의 경우 답변은 최대 100자까지 가능
- LONG_ANSWER 타입의 경우 답변은 최대 1000자까지 가능