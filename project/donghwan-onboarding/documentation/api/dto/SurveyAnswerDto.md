| 필드명               | 타입                          | 필수 | 제약조건                                                                    |
|-------------------|-----------------------------|----|-------------------------------------------------------------------------|
| questionItemId    | Long                        | 필수 | -                                                                       |
| questionItemType  | String                      | 필수 | • SHORT_ANSWER<br>• LONG_ANSWER<br>• SINGLE_CHOICE<br>• MULTIPLE_CHOICE |
| questionItemValue | • String<br> • List[String] | 필수 | 추가 제약사항 확인                                                              |

### 추가 제약사항

- 선택형 질문(SINGLE_CHOICE, MULTIPLE_CHOICE)의 경우
    - options는 최소 2개, 최대 5개까지 가능
- SHORT_ANSWER 타입의 경우 답변은 최대 100자까지 가능
- LONG_ANSWER 타입의 경우 답변은 최대 1000자까지 가능