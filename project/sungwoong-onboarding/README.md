# 📝 Form Management System

> **온보딩 프로젝트**: 설문조사 생성, 관리, 응답 및 검색 시스템

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.12-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![H2 Database](https://img.shields.io/badge/Database-H2-blue.svg)](https://www.h2database.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [아키텍처](#-아키텍처)
- [프로젝트 구조](#-프로젝트-구조)
- [설치 및 실행](#-설치-및-실행)
- [API 문서](#-api-문서)
- [데이터베이스 설계](#-데이터베이스-설계)
- [주요 구현 사항](#-주요-구현-사항)
- [개발 과정](#-개발-과정)

## 🎯 프로젝트 개요

설문조사를 **생성, 관리, 응답, 검색**할 수 있는 종합적인 시스템입니다.

### 핵심 기능
- **📋 설문지 관리**: 설문지 생성, 수정, 조회
- **❓ 다양한 질문 유형**: 단일선택, 복수선택, 단답형, 장문형 지원
- **✅ 응답 시스템**: 설문 응답 제출 및 관리
- **🔍 검색**: 질문 내용, 답변 내용 기반 검색

## 🛠 기술 스택

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.3.12
- **Database**: H2 Database (In-Memory)
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Spring Validation
- **Build Tool**: Gradle (Kotlin DSL)

### Libraries & Tools
- **Lombok**: 보일러플레이트 코드 제거
- **JUnit 5**: 단위 테스트
- **AssertJ**: 테스트 검증
- **Spring Boot DevTools**: 개발 생산성 향상

## 🚀 주요 기능

### 1. 설문지 관리
```http
POST /api/v1/forms
PATCH /api/v1/forms/{formId}
```

### 2. 질문 관리
- **단일 선택 (SINGLE_CHOICE)**: 하나만 선택 가능
- **복수 선택 (MULTIPLE_CHOICE)**: 여러 개 선택 가능
- **단답형 (SHORT_ANSWER)**: 짧은 텍스트 입력
- **장문형 (LONG_ANSWER)**: 긴 텍스트 입력

### 3. 응답 시스템
```http
POST /api/v1/submission
GET /api/v1/submission/{formId}
```

### 4. 고급 검색
```http
GET /api/v1/submission/{formId}?questionText={검색어}&answerText={검색어}
```
- **질문 내용 검색**: 설문지에 특정 텍스트가 포함된 질문이 있는 응답 조회
- **답변 내용 검색**: 텍스트 답변이나 선택 옵션에서 검색

## 🏗 아키텍처

### Domain-Driven Design (DDD) 적용
```
┌─ Presentation Layer ─┐
│   Controllers        │
├─ Application Layer ──┤
│   Services, DTOs     │
├─ Domain Layer ───────┤
│   Entities, VOs      │
└─ Infrastructure ─────┘
    Repositories, JPA
```

### 핵심 도메인 모델
- **Forms**: 설문지 정보
  - **Questions**: 질문 정보 및 유형
  - **Options**: 선택형 질문의 옵션
- **Submission**: 설문 응답 제출 정보
  - **Answers**: 개별 질문에 대한 답변

## 📁 프로젝트 구조

```
src/main/java/com/multi/sungwoongonboarding/
├── common/                    # 공통 유틸리티
│   ├── dto/                  # 공통 DTO
│   ├── util/                 # 유틸리티 클래스
│   └── valid/                # 커스텀 검증
├── forms/                    # 설문지 도메인
│   ├── presentation/         # 컨트롤러
│   ├── application/          # 서비스, DTO
│   ├── domain/              # 도메인 엔티티
│   └── infrastructure/       # 리포지토리 구현
├── questions/               # 질문 도메인
├── options/                 # 옵션 도메인
└── submission/              # 응답 도메인
    ├── presentation/
    ├── application/
    │   └── factory/         # SubmissionFactory
    ├── domain/
    └── infrastructure/
```

## 🚀 설치 및 실행

### 요구사항
- **Java 17** 이상
- **Gradle 7.0** 이상

## 📖 API 문서

### 1. 설문지 작성 
```http request
### POST 설문지 등록 api - 성공
POST /api/v1/forms
Content-Type: application/json

{
  "title": "햄버거 설문조사",
  "description": "햄버거를 다들 좋아하는지 조사한다",
  "questionCreateRequests": [
    {
      "questionText": "나는 햄버거를 좋아한다.",
      "questionType": "SINGLE_CHOICE",
      "isRequired": true,
      "optionCreateRequests": [
        {
          "optionText": "좋아한다"
        },
        ...
      ]
    },
    {
      "questionText": "브랜드",
      "questionType": "MULTIPLE_CHOICE",
      "optionCreateRequests": [
        {
          "optionText": "맥도날드"
        },
        ...
        }
      ]
    },
    {
      "questionText": "자세히 입력해주세요",
      "questionType": "SHORT_ANSWER"
    }
  ]
}
```

### 2. 설문지 수정 기능 
```http request
### PATCH 설문지 수정
PATCH /api/v1/forms/{formId}
Content-Type: application/json

{
  "id": 1,
  "title": "피자 설문조사",
  "description": "피자를 좋아하는지 조사",
  "questions": [
    {
      "id": 1,
      "questionText": "나는 피자를 좋아한다.",
      "questionType": "SINGLE_CHOICE",
      "isRequired": false,
      "deleted": false,
      "options": [
        {
          "id": 1,
          "optionText": "좋아한다"
        },
        ...
      ]
    },
    {
      "id": 2,
      "questionText": "씬 도우 vs 일반 도우",
      "questionType": "MULTIPLE_CHOICE",
      "isRequired": false,
      "deleted": false,
      "options": [
        {
          "id": 4,
          "optionText": "치킨버거"
        },
        ...
      ]
    },
    {
      "questionText": "자세히 알려주세요",
      "questionType": "SHORT_ANSWER",
      "isRequired": true,
      "deleted": false,
      "options": []
    }
  ]
}
```


### 3. 설문 응답 제출
```http request
### POST 응답지 제출
POST /api/v1/submission
Content-Type: application/json

{
  "formId": 1,
  "userId": "user123",
  "answerCreateRequests": [
    {
      "questionId": 1,
      "optionIds": [1, 2],      // 복수선택인 경우
      "answerText": null
    },
    {
      "questionId": 2,
      "optionIds": [],
      "answerText": "답변 내용"  // 텍스트형인 경우
    }
  ]
}
```

### 응답 검색
```http request
### GET 설문지 응답 검색 기능  
GET /api/v1/submission/{forId}?questionText={질문내용 검색어}&answerText={답변 or 옵션 내용 검색어}

Respoonse:  
{
  "status": "SUCCESS",
  "message": null,
  "errorCode": null,
  "data": [
    {
      "id": 2,
      "formId": 1,
      "userId": "sungwoong_test",
      "formTitle": "햄버거 설문조사",
      "formDescription": "햄버거를 다들 좋아하는지 조사한다",
      "formVersion": 0,
      "questionAnswer": [
        {
          "questionId": 1,
          "questionText": "나는 햄버거를 좋아한다.",
          "questionType": "SINGLE_CHOICE",
          "selectedOptions": [
            {
              "optionId": 1,
              "optionText": "좋아한다"
            }
          ],
          "answerText": null,
          "required": false
        },
        ...
  ],
  "errorDetail": null
}
```

## 🗄 데이터베이스 설계

### 주요 테이블
- **forms**: 설문지 기본 정보
  - **form_history**: 설문지 버전 정보
- **questions**: 질문 정보 (form과 1:N)
- **options**: 선택 옵션 (question과 1:N)
- **submission**: 응답 제출 정보 (form과 1:N)
- **answers**: 개별 답변 (submission과 1:N)


## 🔧 주요 구현 사항

### 1. Factory Pattern 적용
```java
@Component
public class SubmissionFactory {
    public Submission createSubmission(SubmissionCreateRequest request) {
        // 복잡한 도메인 객체 생성 로직
        // 비즈니스 검증 포함
    }
}
```

### 2. 복수 선택 질문 처리
- **요청**: 하나의 질문에 여러 옵션 ID 배열
- **저장**: questionId별로 그룹핑하여 여러 Answer 행 생성
- **조회**: questionId로 그룹핑하여 하나의 Answers 도메인으로 변환


## 🧪 테스트 전략

### 계층별 테스트
- **@DataJpaTest**: JPA Repository 계층
- **@SpringBootTest**: 통합 테스트
- **@ExtendWith(MockitoExtension.class)**: 단위 테스트

### 주요 테스트 케이스
- ✅ 설문 응답 제출 및 조회
- ✅ 복수 선택 질문 처리
- ✅ 검색 기능 (질문/답변 내용)
- ✅ 유효성 검증 (필수 질문, 옵션 유효성)

## 📚 개발 과정

### 고민해본 내용
1. 설문지 등록에 대한 유효성 검증
   - 설문지는 여러 질문들을 가지고, 질문들은 질문 유형에 따라 옵션을 갖거나, 텍스트 형식의 답변이 있어야 한다.
2. 설문지 응답이 제출된 상태에서 설문지가 수정되어도 응답은 유지되어야 한다.
   - 설문지의 내용과 질문이 응답 시점의 상태가 유지되어야 하므로 수정이 된다면 수정 전 내용도 필요했다. 
   - 처음에는 PK를 통해 모두 수정하는 방식을 사용했다가 질문 순서, 질문 스냅샷이 조금 까다로워 방식을 변경.
   - 수정 -> 논리 삭제 후 다시 적재하는 방식으로 변경하여 데이터는 더 많이 쌓이지만 순서나 질문 이력을 관리하기가 용이해졌다.
3. 응답지 조회 쿼리 
   - 질문 내용과 답변 내용으로 검색을 하려면 (설문지 1 : N 질문) 1:N 구조를 가지다 보니 나중에 페이징에 문제가 될것 같아 서브쿼리로 일단 구현하였다. 
   - 답변 내용 검색은 선택형 질문도 있기 때문에 답변내용이 없다면 선택한 옵션의 내용으로 검색되도록 하였다.

### 주요 학습 포인트
1. **DDD 아키텍처** 도메인 중심으로 개발해보기
   - 기존 사내 프로젝트는 테이블 중심으로 개발을 진행해옴.
   - 도메인이라는 개념을 고려해보고, 도메인 중심으로 개발을 하도록 함.
2. **Factory Pattern**으로 복잡한 객체 생성 로직 분리
3**테스트 전략** 
   - 테스트 코드 작성 및 속도 개선해보기

### 성능 최적화
- **쿼리 최적화**: EXISTS 서브쿼리로 효율적인 검색
- **메모리 최적화**: 필요한 데이터만 선별적 로딩
- **캐시 전략**: H2 Database 특성 활용


## 🔍 추가 개선 사항

### 도메인 분석 능력 
- 요구사항 문서를 좀 더 상세히 파악하여 설계하는 능력 기르기
- 확장성에 대한 고려해보기

### 향후 개발 계획
- **페이징 처리**: 대용량 데이터 대응
- **통계 기능**: 응답 분석 및 시각화


## 🤔 프로젝트 회고
### 🚧 아쉬운점
- **분석, 설계** 도메인 분석 과정에서 잡고가야할 부분을 놓치고 시작하여 설계가 계속 바뀐점이 아쉬웠다. 다음부터는 요구사항과 구현시 발생할 문제들을 조금 더 상세히 추출하여 구현을 해야겠다고 느꼈다.
- **테스트** 구현할 기능들에 대한 명세를 단위 테스트로 테스트 후 진행했하지 못하고 구현 후 테스트를 작성. 
  - 그러다보니 테스트가 통과되어도 불안함. 다음부터는 코드 작성 전 기능을 명확히 하고 테스트부터 해야된다고 느꼈다.
- **일관성** 한가지 방법으로 꾸준히 진행했어야 됐는데 중간중간 더 좋은 방법이 생각나면 바꾸던 문제. 
  - 이걸 설계 단계에서 정하고 시작했어야 됐다고 보는데, 어떤 방법이 좋은지 판단이 서지 않아 어려웠다. 다음부턴 이런 부분을 좀 테스트 하면서 정해야겠다고 느꼈다. 
- **확장성 고려** 멘토님 리뷰를 듣고나니 많이 부족하다 느꼈음. 코드의 퀄리티도 좋요하지만 시스템의 확장성과 성능, 운영에 대한 고민이 더 중요하다는 생각이 든다. 
  - 이 부분은 이너써클 프로젝트를 통해 더 생각해보고 배우는 시간이 필요하다고 느꼈다.

### 💡좋았던점
- 잘하시는 분들이 많아 많이 참고하고 배울 수 있는 시간이 된것 같았다. 
  - 멀티모듈을 어떻게 활용하는지
  - 개발을 할 때 어떤 점을 고려하면서 개발해야 하는지
  - 문서화 하는 내용
- 그동안 업무를하며 생각하지 못했던 부분들을 고민하게 해준 점
  - 트래픽이 많은 서비스를 하는 회사가 아니다보니 개발자가 항상 고려해야 할 중요한 점(성능, 동시성, 트래픽 등)에 대해 크게 고민해본적이 없는데 이번 기회에 많이 고민해보게 된 것 같다.


