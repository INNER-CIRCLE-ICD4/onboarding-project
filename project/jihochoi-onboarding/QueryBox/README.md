# 📋 설문조사 서비스 (Survey Service)

설문조사 생성 및 응답 수집 기능을 제공하는 도메인 기반 설문 서비스입니다.  
Google Forms, Tally 등과 유사한 기능을 목표로 하며, DDD 스타일로 작성합니다.

---

## ✅ 주요 도메인 모델

### 📦 Entity 목록

| Entity             | 설명                               |
|--------------------|----------------------------------|
| `Survey`           | 설문 전체. 항목 목록을 포함한 루트 애그리거트       |
| `Question`         | 하나의 항목. 입력 형태, 필수 여부 등 포함        |
| `Response`         | 사용자의 응답. 여러 Answer를 포함, 루트 애그리거트 |
| `ResponseSnapshot` | 사용자의 응답과 설문 항목 스냅샷               |


### 🧱 Value Object 목록

| Value Object | 설명 |
|----------|------|
| `Option` | 선택형 항목의 선택지. 값만으로 동일성 판단 |
| `Answer` | 텍스트 or 선택지 응답값을 캡슐화할 경우 |
| `QuestionType` | 단답형, 장문형, 단일/다중 선택형 등 |

---

## 🔒 도메인 불변식 (Invariant)

### Survey
- [x] 설문 제목은 공백일 수 없음

### Questions
- [x] 항목은 1~10개 사이여야 함
- [x] 항목 이름은 중복되면 안 됨

### Question
- [x] 항목 이름은 공백일 수 없음
- [x] 선택형 항목(SINGLE, MULTIPLE)은 옵션이 1개 이상 존재해야 함
- [x] 주관식 항목(SHORT_TEXT, LONG_TEXT)은 옵션이 없어야 함

### Answers
- [x] 응답은 최소 하나 이상의 Answer를 포함해야 함
- [x] 동일한 Question에 대해 중복 응답이 존재하면 안 됨

### Answer
- [x] 주관식인 경우: text 값이 반드시 존재해야 함
- [x] 선택형인 경우: 최소 하나 이상의 option ID가 포함되어야 함

---

## 🧩 기능 목록 (Feature To-do)

### 설문 관리

- [x] 설문 생성 API
- [x] 설문 수정 API
- [x] 설문 조회 API (생성/수정 결과 확인용)

### 응답 관리

- [x] 설문 응답 제출 API
- [x] 설문 응답 전체 조회 API (항목 이름 + 응답 값 기준)

---

### 도메인 이벤트

- [x] `ResponseSubmittedEvent` 등록 및 발행
  - 응답 제출 후 스냅샷 저장

---


## 📡 API 명세

### 🔹 설문 생성 API

- **URL:** `POST /surveys`
- **요청 바디:**
```json
{
  "title": "설문 제목",
  "description": "설문 설명",
  "questions": [
    {
      "name": "질문 1",
      "description": "질문 설명",
      "type": "SHORT_TEXT",
      "required": true,
      "options": []
    }
  ]
}
```
- **응답:**
```json
{
  "message": "",
  "body": 1 
}
```

---

### 🔹 설문 수정 API

- **URL:** `PUT /surveys/{surveyId}`
- **요청 바디:** (생성과 동일)
- **응답:**
```json
{
  "message": "",
  "body": null
}
```

### 🔹 설문 조회 API

- **URL:** `GET /surveys/{surveyId}`
- **응답:**
```json
{
  "data": {
    "id": 1,
    "title": "설문 제목",
    "description": "설문 설명",
    "questions": [
      {
        "id": 1,
        "name": "질문 1",
        "description": "질문 설명",
        "type": "SHORT_TEXT",
        "required": true,
        "options": []
      }
    ]
  }
}
```

---
### 🔹 설문 응답 제출 API

- **URL:** `POST /surveys/{surveyId}/responses`
- **요청 바디:**
```json
{
  "answers": [
    {
      "questionId": 1,
      "answerValue": "응답 텍스트",
      "selectedOptionIds": [1],
      "selectedOptionTexts": ["선택지1"]
    }
  ]
}
```
- **응답:**
```json
{
  "message": "",
  "body": null
}
```

---

---

### 🔹 설문 응답 전체 조회 API

- **URL:** `GET /surveys/{surveyId}/responses`
- **Query Parameter (Optional):**
    - `questionKeyword` (질문 텍스트 필터링)
    - `answerKeyword` (답변 텍스트 필터링)
- **응답:**
```json
{
  "data": [
    {
      "responseId": 1,
      "surveyId": 1,
      "questionId": 1,
      "questionName": "질문 1",
      "questionType": "SHORT_TEXT",
      "answerValue": "응답 텍스트",
      "selectedOptionIds": [],
      "selectedOptionTexts": []
    }
  ]
}
```


---
## 🏗️ 기술 스택

- Kotlin + Spring Boot
- JPA + Hibernate
- H2
- Test: JUnit5
---

