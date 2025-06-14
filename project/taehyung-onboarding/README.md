# 설문 조사 애플리케이션 - 온보딩 프로젝트

## Open Source Library

| name           | link                                                       | 사용 이유          |
|----------------|------------------------------------------------------------|----------------|
| tsid-creator   | https://github.com/f4b6a3/tsid-creator                     | 엔티티 식별자로 사용    |
| fixture-monkey | https://github.com/naver/fixture-monkey?tab=readme-ov-file | Fixture 데이터 사용 |

---

## Application Architecture

- adapter
- application
- domain

의존 관계 방향
- application > domain
- adapter > application
  - application 의 UseCase 에 의존
  - persistence: DB Access 관련 패키지
  - web: API 요청을 처리하는 패키지

---

## Domain

- SurveyForm: 설문지 폼
  - 이름
  - 설명
  - 설문 받을 항목 목록 <br/>


- SurveyFormQuestion: 설문 받을 항목
  - 이름
  - 설명
  - 입력 받을 유형(단답, 장문, 단일 리스트, 다중 리스트)
  - 필수 여부
  - 옵션 값 목록


- SurveyFormQuestionOption: 단일 리스트, 다중 리스트용 옵션값
  - value: 선택해야될 값


- Answer: 설문지 응답
  - 유저 이메일
  - 제출 일자
  - 응답값 목록
 
- QuestionAnswer
  - 응답값

---

## API Spec


| API명   | url            | content-type     | 
|--------|----------------|------------------| 
| 설문지 생성 | /api/v1/survey | application/json |

### 설문지 생성

| name                          | type                                                     | nullable | 설명           | 
|-------------------------------|----------------------------------------------------------|----------|--------------| 
| surveyName                    | String                                                   | false    | 설문지 이름       |
| description                   | String                                                   | false    | 설문지 설명       |
| questions                     | List                                                     | false    | 설문 항목 목록     |
| questions[*].name             | String                                                   | false    | 설문 항목 이름     |
| questions[*].description      | String                                                   | false    | 설문 항목 설명     |
| questions[*].inputType        | Enum(SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTI_CHOICE) | false    | 설문 항목 유형     |
| questions[*].required         | Boolean                                                  | false    | 설문 항목 필수 여부  |
| questions[*].options          | List                                                     | true     | 설문 항목 선택지 목록 |
| questions[*].options[*].value | String                                                   | false    | 설문 항목 선택지 값  |