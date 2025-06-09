# 설문조사 서비스 (Survey Service)

## 프로젝트 소개
이 프로젝트는 설문조사를 생성하고 관리할 수 있는 서비스입니다. 사용자는 다양한 유형의 질문(단답형, 장문형, 라디오 버튼, 체크박스, 드롭다운)을 포함한 설문조사를 만들 수 있습니다.

[메인 애플리케이션](src/main/kotlin/fc/innercircle/sanghyukonboarding/SanghyukOnboardingApplication.kt)

## 개발 환경 및 기술
- **언어**: Kotlin 1.9.25
  - **사용 목적**: 간결한 문법과 null 안전성, 함수형 프로그래밍 지원으로 안정적이고 유지보수가 용이한 코드 작성
  - **주요 라이브러리**: kotlin-reflect (리플렉션 지원), jackson-module-kotlin (JSON 직렬화/역직렬화)

- **JDK**: Java 21
  - **사용 목적**: 최신 Java 기능 및 성능 개선 활용

- **프레임워크**: Spring Boot 3.5.0
  - **사용 목적**: 빠른 개발 환경 구성과 다양한 스프링 기능 통합
  - **주요 모듈**:
    - Spring Boot Starter Web: RESTful API 개발
    - Spring Boot Starter Data JPA: 데이터 액세스 계층 구현

- **빌드 도구**: Gradle (Kotlin DSL)
  - **사용 목적**: 유연한 빌드 스크립트 작성과 의존성 관리
  - **주요 플러그인**:
    - kotlin("jvm"), kotlin("plugin.spring"), kotlin("plugin.jpa"): Kotlin 지원
    - org.springframework.boot: Spring Boot 애플리케이션 빌드
    - io.spring.dependency-management: 스프링 의존성 관리
    - org.jlleitschuh.gradle.ktlint: 코드 스타일 검사

- **데이터베이스**: H2 Database (인메모리)
  - **사용 목적**: 개발 및 테스트 환경에서 빠른 설정과 실행 가능

- **ORM**: Spring Data JPA, Hibernate
  - **사용 목적**: 객체 지향적 데이터 접근과 SQL 추상화로 개발 생산성 향상
  - **주요 기능**: 엔티티 매핑, 트랜잭션 관리, 쿼리 생성

- **테스트 프레임워크**:
  - **JUnit5**: 기본 테스트 프레임워크
  - **Kotest**: 더 표현력 있는 테스트 작성을 위한 Kotlin 특화 테스트 프레임워크
    - kotest-runner-junit5: JUnit5 기반 실행
    - kotest-assertions-core: 강력한 assertion 라이브러리
    - kotest-extensions-spring: 스프링 통합 테스트 지원
  - **사용 목적**: 다양한 테스트 스타일 지원과 가독성 높은 테스트 코드 작성

- **코드 스타일 검사**: Ktlint
  - **사용 목적**: 일관된 코드 스타일 유지와 코드 품질 향상

## 주요 기능
- 설문조사 생성 및 관리
- 다양한 유형의 설문 항목 지원 (단답형, 장문형, 라디오 버튼, 체크박스, 드롭다운)
- 설문 항목에 대한 옵션 관리

## 아키텍처

- **도메인**: 핵심 비즈니스 로직과 규칙
- **인프라**: 데이터베이스 접근 및 외부 서비스 통합
- **애플리케이션**: 도메인 서비스 조정 및 트랜잭션 관리
- **인터페이스**: 사용자 인터페이스 및 API 엔드포인트

## 도메인 설계

- **Survey(설문)**: 설문조사 폼 메타데이터
- **SurveyItem(설문 항목):** 개별 문항(Question)
- **SurveyOption(선택 옵션)**: 선택형(단일/다중) 문항의 후보 옵션
- **SurveyReply(설문 응답)**: 사용자의 한 번의 “회신”(응답 묶음)
- **SurveyAnswer(설문 답변)**: 각 문항에 대한 응답(메타데이터 스냅샷 + 실제 답변)

비즈니스 규칙에 따라 `함께 생성되고 변경되는 객체`들을 각각의 애그리거트로 묶어 다음과 같이 구분하였습니다.

`[Survey - SurveyItem - SurveyOption]` 객체들은 “한 설문조사에는 최대 10개의 문항”과 같은 비즈니스 규칙을 한 트랜잭션 내에서 처리하여야 하기 때문에 하나의 애그리거트로 묶었고, 이에 대한 루트 엔티티(단일 진입점)으로 하위 객체들을 아우를 수 있는 Survey로 선정하였습니다.

`[SurveyReply - SurveyAnswer]` 객체들 또한, 설문 조사에 대한 응답이 이루어질 때, 공통으로 생성되고 변경되므로 하나의 애그리거트로 묶었으며, 마찬가지로 SurveyReply 객체가 하위 객체들의 생명 주기를 관리하므로 루트 앤티티로 설정하였습니다.

| 애그리거트              | 루트 엔티티        | 내부 엔티티                       | 주요 불변식／트랜잭션 경계                                                        |
|--------------------|---------------|------------------------------|-----------------------------------------------------------------------|
| **Survey 애그리거트**   | `Survey`      | `SurveyItem`, `SurveyOption` | - `Survey`당 `SurveyItem` ≤ 10- 문항·옵션 CRUD는 한 트랜잭션                     |
| **Response 애그리거트** | `SurveyReply` | `SurveyAnswer`               | - 한 번의 제출 시 `SurveyReply` + 다수 `SurveyAnswer` 저장- 응답 검증(필수 여부, 타입 일치) |

### 주요 설계 포인트

- **양방향 연관관계 사용 금지 :** `순환 참조`는 소프트웨어 설계에서 자주 볼 수 있는 대표적인 안티 패턴 중에 하나이다. 순환 참조로 인해 발생하는 문제점은 무한 루프, 시스템의 복잡도가 늘어날 수 있다. 또한, 대부분의 순환 참조는 단방향 관계로 풀 수 있다. `JPA의 양방향 연관관계는 순환 참조` 중에 하나이다. 도메인을 설계할 때 순환 참조로 인해 발생하는 문제들을 없애기 위해 단방향 연관관계만 사용하도록 설계 원칙을 잡았다. [OMY 개발팀 기술 블로그](https://medium.com/way-tech/%EC%96%91%EB%B0%A9%ED%96%A5-%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84-%EB%A7%A4%ED%95%91-feat-%EC%88%9C%ED%99%98-%EC%B0%B8%EC%A1%B0-b5f55ff3c601)
- **@ManyToOne만 사용 :** `@OneToMany는 One 테이블에 변경을 하였는데 전혀 다른 Many 쪽 테이블의 데이터도 수정될 수가 있다.` 물론 이 부분은 Many 쪽에도 @ManyToOne 연관관계를 설정하여 양방향 연관관계를 만들면 되지만, 앞서 언급했단 순환 참조 문제가 생기게 된다. 이에외도 @OneToMany 단방향 연관관계는 N+1 문제 등 몇 가지 문제가 발생할 수 있고 이런 문제들이 복잡성을 늘리게 되므로 사용하지 않는 편으로 결정하였다. [인프런 김영한님 답변](https://www.inflearn.com/community/questions/84572/onetomany-%EB%8B%A8%EB%B0%A9%ED%96%A5-%EA%B4%80%EB%A0%A8%ED%95%B4%EC%84%9C-%EA%B6%81%EA%B8%88%ED%95%A9%EB%8B%88%EB%8B%A4?srsltid=AfmBOoqQPgss8w-DOV6cLopZgzlVARakGexF1EhbEFRRPN2ttH0agy-6)

## 패키지 구조
```
fc.innercircle.sanghyukonboarding
├── common
│   └── domain
│       ├── exception
│       └── model
├── survey
│   └── domain
│       ├── model
│       ├── validator
│       └── value
└── surveyreply
    ├── model
    │   └── vo
    ├── validator
    └── value
```

- **common/domain/exception**: 공통 예외 처리 관련 클래스
  - 도메인 규칙 위반 시 발생하는 예외 정의
  - 일관된 오류 코드 및 메시지 관리 ([ErrorCode](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/domain/exception/ErrorCode.kt))

- **common/domain/model**: 공통 도메인 모델
  - [BaseEntity](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/domain/model/BaseEntity.kt): 모든 엔티티의 기본 클래스로 생성/수정 정보 관리

- **domain/model**: 설문조사 관련 도메인 모델
  - [Survey](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/Survey.kt): 설문조사 정보
  - [SurveyItem](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/SurveyItem.kt): 설문 항목 정보
  - [ItemOptions](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/ItemOptions.kt): 선택형 항목의 옵션 정보

- **domain/validator**: 도메인 모델 유효성 검사 클래스
  - 각 도메인 모델별 전용 검증 로직 구현 ([SurveyValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/validator/SurveyValidator.kt), [SurveyItemValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/validator/SurveyItemValidator.kt), [ItemOptionsValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/validator/ItemOptionsValidator.kt))
  - 비즈니스 규칙 강제

- **domain/value**: 도메인 값 객체
  - 불변성을 가진 값 표현 객체 정의

- **surveyreply/model**: 설문 응답 관련 도메인 모델
  - [SurveyReply](src/main/kotlin/fc/innercircle/sanghyukonboarding/surveyreply/model/SurveyReply.kt): 설문 응답 정보
  - [SurveyItemAnswer](src/main/kotlin/fc/innercircle/sanghyukonboarding/surveyreply/model/SurveyItemAnswer.kt): 설문 항목 응답 정보

- **surveyreply/model/vo**: 설문 응답 관련 값 객체
  - [SelectedItemOptions](src/main/kotlin/fc/innercircle/sanghyukonboarding/surveyreply/model/vo/SelectedItemOptions.kt): 선택형 항목의 선택된 옵션 정보

## 엔티티 설계

### [BaseEntity](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/domain/model/BaseEntity.kt)
모든 엔티티의 기본이 되는 추상 클래스입니다.
- **createdBy**: 생성자 (최대 50자)
- **createdAt**: 생성 시간
- **updatedBy**: 수정자 (nullable, 최대 50자)
- **updatedAt**: 수정 시간 (nullable)

### [Survey](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/Survey.kt) (설문 조사)
설문조사의 기본 정보를 담고 있는 엔티티입니다.
- **id**: 설문조사 ID (PK)
- **title**: 설문조사 제목 (최대 255자)
- **description**: 설문조사 설명 (최대 1000자, 기본값: 빈 문자열)
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [SurveyItem](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/SurveyItem.kt) (설문 항목)
설문조사의 각 항목을 나타내는 엔티티입니다.
- **id**: 설문 항목 ID (PK)
- **question**: 질문 내용 (최대 500자)
- **description**: 질문 설명 (최대 1000자, 기본값: 빈 문자열)
- **type**: 항목 유형 (TEXT, LONG_TEXT, RADIO, CHECKBOX, SELECT)
- **required**: 필수 응답 여부 (기본값: false)
- **displayOrder**: 표시 순서 (기본값: 0)
- **survey**: 연관된 설문조사 (FK)
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [ItemOptions](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/ItemOptions.kt) (항목 옵션)
라디오 버튼, 체크박스, 드롭다운과 같은 선택형 항목의 옵션을 나타내는 엔티티입니다.
- **id**: 옵션 ID (PK)
- **optionText**: 옵션 텍스트 (최대 50자)
- **displayOrder**: 표시 순서 (기본값: 0)
- **surveyItem**: 연관된 설문 항목 (FK)
- BaseEntity를 상속하지 않음 (createdBy, createdAt, updatedBy, updatedAt 필드 없음)

### [SurveyReply](src/main/kotlin/fc/innercircle/sanghyukonboarding/surveyreply/model/SurveyReply.kt) (설문 응답)
설문조사에 대한 응답 정보를 담고 있는 엔티티입니다.
- **id**: 설문 응답 ID (PK)
- **surveyId**: 연관된 설문조사 ID
- **responseDate**: 응답 일시
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [SurveyItemAnswer](src/main/kotlin/fc/innercircle/sanghyukonboarding/surveyreply/model/SurveyItemAnswer.kt) (설문 항목 응답)
설문 항목에 대한 응답 정보를 담고 있는 엔티티입니다.
- **id**: 설문 항목 응답 ID (PK)
- **surveyReply**: 연관된 설문 응답 (FK)
- **surveyItemId**: 연관된 설문 항목 ID
- **question**: (snapshot) 설문 당시의 질문 내용 (최대 500자)
- **description**: (snapshot) 설문 당시의 질문 설명 (최대 1000자)
- **required**: (snapshot) 설문 당시의 필수 응답 여부
- **type**: (snapshot) 설문 당시의 항목 유형
- **answer**: (snapshot) 설문 당시의 응답 내용 (단답형, 장문형)
- **selectedItemOptions**: (snapshot) 설문 당시의 선택된 항목 옵션 (선택형)
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

## RDBMS 스키마

### survey 테이블
```sql
CREATE TABLE survey (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '설문 제목',
    description VARCHAR(1000) NOT NULL COMMENT '설문 설명',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
);
```

### survey_item 테이블
```sql
CREATE TABLE survey_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(500) NOT NULL COMMENT '설문 항목 제목',
    description VARCHAR(1000) NOT NULL COMMENT '설문 항목 설명',
    type VARCHAR(20) NOT NULL COMMENT '설문 항목 타입',
    required BOOLEAN DEFAULT FALSE NOT NULL COMMENT '필수 여부',
    display_order INT DEFAULT 0 NOT NULL COMMENT '항목 순서',
    survey_id BIGINT NOT NULL,
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (survey_id) REFERENCES survey(id)
);
```

### item_options 테이블
```sql
CREATE TABLE item_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_text VARCHAR(50) NOT NULL COMMENT '설문 선택 옵션',
    display_order INT DEFAULT 0 NOT NULL COMMENT '항목 순서',
    survey_item_id BIGINT NOT NULL COMMENT '설문 항목 ID',
    FOREIGN KEY (survey_item_id) REFERENCES survey_item(id)
);
```

### survey_reply 테이블
```sql
CREATE TABLE survey_reply (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_id BIGINT NOT NULL COMMENT '설문 ID',
    response_date DATETIME NOT NULL COMMENT '설문 응답일자',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
);
```

### survey_item_answer 테이블
```sql
CREATE TABLE survey_item_answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_reply_id BIGINT NOT NULL COMMENT '설문 응답 ID',
    survey_item_id BIGINT NOT NULL COMMENT '설문 항목 ID',
    question VARCHAR(500) NOT NULL COMMENT '설문 항목 제목',
    description VARCHAR(1000) NOT NULL COMMENT '설문 항목 설명',
    required TINYINT(1) NOT NULL COMMENT '설문 항목 입력 필수 여부',
    type VARCHAR(20) NOT NULL COMMENT '설문 항목 입력 타입',
    answer TEXT NOT NULL COMMENT '설문 항목 응답 내용(단답형, 장문형)',
    selected_item_option_text VARCHAR(50) NOT NULL COMMENT '선택된 항목 옵션 텍스트(단일/다중 선택 리스트)',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (survey_reply_id) REFERENCES survey_reply(id)
);
```

## 데이터베이스 설정
[application.yml](src/main/resources/application.yml)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        show_sql: true
```
