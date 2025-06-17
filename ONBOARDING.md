# 설문조사 백엔드 설계 및 API 명세

## 1. 기본정보 및 특징

- **DB:** H2 (개발/테스트), 확장 고려
- **ORM:** JPA (Hibernate)
- **API 명세:** Swagger(OpenAPI)
- **특징:**
  - 익명 응답 (로그인 불필요, UUID 기반)
  - 설문 그룹화/버전 관리/중복응답 제한
  - 문항/옵션 자유 설계
  - 대용량/확장성 고려
  - Swagger UI로 API 테스트 지원


## 2. 기능 요구사항

### 2.1 설문 생성/수정/버전 관리

- 설문 그룹 내 설문 생성 (`title`, `description`, `items`, `startDate`, `endDate`, `isOpen`)
- 문항 최대 10개
- 문항 구조 변경 시 새 버전 생성 (Deep Copy, 기존 응답/이력 보존)
- 단순 설명/오타 수정은 정책에 따라 버전업 없이 반영
- 만료/비공개(`startDate`, `endDate`, `isOpen`) 시 응답 불가

### 2.2 문항 관리

- 설문 내 1~10개 문항
- 필드: `question`, `type(SHORT_TEXT/LONG_TEXT/SINGLE_CHOICE/MULTI_CHOICE)`, `required`, `options`
- 객관식일 경우 `options` 필수
- 소프트 삭제(`isDeleted`) 지원

### 2.3 설문 응답 관리

- 누구나(익명, UUID) 1회만 응답
- 응답은 설문 버전별 관리, 필수/유효성 체크
- 만료/비공개 설문은 응답 불가
- 설문/버전별 전체 응답/답변 결과 조회 지원

---

## 3. 확장/운영 고려사항

- **통계/조회:** 관리자 별도 화면(?), 필터, 통계
- **Rate Limiting:** 봇/어뷰징 방지
- **멀티테넌시:** 여러 조직/고객 독립 운영
- **대용량/성능:** 인덱싱, 파티셔닝, 캐싱등

### 기능
- **설문조사화면:**
- **섫문조사 질문 선택:** 숫자, 날짜, 체크박스, 라디오 박스 등
- **섫문조사 질문 선택:** 예외처리방안

---

## 4. 엔티티 구조 및 매핑

- 모든 연관관계는 **id(Long) FK로만 단방향 참조**
- 컬렉션/양방향/객체 참조 매핑 전부 제거
- 서비스/쿼리 레이어에서 필요한 Join/DTO 조합

### 4.1 엔티티 



#### SurveySeries (설문 그룹)
| 필드         | 설명                          |
|--------------|-------------------------------|
| id           | PK                            |
| code         | 비즈니스 키 (유니크)          |
| name         | 그룹명                        |
| description  | 설명                          |
| createdAt    | 등록일                        |

#### Survey (설문)
| 필드         | 설명                              |
|--------------|-----------------------------------|
| id           | PK                                |
| seriesId     | 설문 그룹 FK                      |
| version      | 버전 (int)                        |
| title        | 설문 제목                         |
| description  | 설명                              |
| createdAt    | 생성일                            |
| updatedAt    | 수정일                            |
| startDate    | 시작일                            |
| endDate      | 종료일                            |
| isOpen       | 공개여부                          |

#### SurveyItem (문항)
| 필드         | 설명                              |
|--------------|-----------------------------------|
| id           | PK                                |
| surveyId     | 설문 FK                           |
| question     | 질문 텍스트                       |
| description  | 질문 설명                         |
| type         | QuestionType enum                 |
| required     | 필수 여부                         |
| options      | 객관식 선택지 (List<String>)      |
| isDeleted    | 삭제 여부(soft delete)            |

#### SurveyResponse (응답)
| 필드         | 설명                              |
|--------------|-----------------------------------|
| id           | PK                                |
| surveyId     | 설문 FK                           |
| uuid         | 응답자 UUID (브라우저별)          |
| submittedAt  | 응답시각                          |

#### SurveyResponseItem (문항별 응답)
| 필드         | 설명                              |
|--------------|-----------------------------------|
| id           | PK                                |
| responseId   | 응답 FK                           |
| surveyItemId | 문항 FK                           |
| questionText | 질문 스냅샷(변경 이력 대비)        |
| answer       | 실제 응답                         |

#### QuestionType (enum)
- SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTI_CHOICE

---

## 5. API/DTO 예시
### 5.1 설문 생성 API

- **요청**

POST /api/surveys

```json
{
  "seriesCode": "commuteSurvey",
  "title": "2024 출퇴근 설문",
  "description": "임직원 출퇴근 현황조사",
  "items": [
    {
      "question": "출근 시간은 언제입니까?",
      "type": "SINGLE_CHOICE",
      "required": true,
      "options": ["8시", "9시", "10시", "기타"]
    }
  ],
  "startDate": "2024-07-01T00:00:00",
  "endDate": "2024-07-10T23:59:59",
  "isOpen": true
}
```

- **응답**

```json
{
  "surveyId": 1001,
  "seriesCode": "commuteSurvey",
  "version": 1,
  "title": "2024 출퇴근 설문",
  "createdAt": "2024-06-15T13:22:11"
}
```

---
### 5.2 설문 응답 제출 API

- **요청**
POST /api/surveys/1001/responses

```json
{
  "uuid": "eb5a5b85-9932-4fa6-8430-123456789abc",
  "answers": [
    {
      "itemId": 201,
      "answer": "9시"
    }
  ]
}
```

- **응답**

```json
{
  "responseId": 5001,
  "submittedAt": "2024-07-03T14:32:00"
}
```

---
### 5.3 설문 응답 조회 API

- **요청**
GET /api/surveys/101/responses

```json
[]
```

- **응답**

```json
[
  {
    "responseId": 5002,
    "surveyId": 101,
    "uuid": "7a02f9e4-3ee5-4e0a-b0b5-2a13a9ac23ee",
    "submittedAt": "2024-07-06T10:33:10",
    "answers": [
      {
        "itemId": 2001,
        "question": "워크숍 장소는 만족스러웠나요?",
        "answer": "매우 만족"
      },
      {
        "itemId": 2002,
        "question": "가장 기억에 남는 세션을 적어주세요.",
        "answer": "AI 기초 강연"
      },
      {
        "itemId": 2003,
        "question": "추가로 바라는 점이 있다면 적어주세요.",
        "answer": "점심시간이 더 길었으면 좋겠습니다."
      }
    ]
  }
]
```
