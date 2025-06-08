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

## 패키지 구조
```
fc.innercircle.sanghyukonboarding
├── survey
│   ├── common
│   │   └── domain
│   │       ├── exception
│   │       └── model
│   └── domain
│       ├── model
│       ├── validator
│       └── value
```

- **common/domain/exception**: 공통 예외 처리 관련 클래스
  - 도메인 규칙 위반 시 발생하는 예외 정의
  - 일관된 오류 코드 및 메시지 관리 ([ErrorCode](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/common/domain/exception/ErrorCode.kt))

- **common/domain/model**: 공통 도메인 모델
  - [BaseEntity](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/common/domain/model/BaseEntity.kt): 모든 엔티티의 기본 클래스로 생성/수정 정보 관리

- **domain/model**: 설문조사 관련 도메인 모델
  - [Survey](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/Survey.kt): 설문조사 정보
  - [SurveyItem](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/SurveyItem.kt): 설문 항목 정보
  - [ItemOptions](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/model/ItemOptions.kt): 선택형 항목의 옵션 정보

- **domain/validator**: 도메인 모델 유효성 검사 클래스
  - 각 도메인 모델별 전용 검증 로직 구현 ([SurveyValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/validator/SurveyValidator.kt), [SurveyItemValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/validator/SurveyItemValidator.kt), [ItemOptionsValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/domain/validator/ItemOptionsValidator.kt))
  - 비즈니스 규칙 강제

- **domain/value**: 도메인 값 객체
  - 불변성을 가진 값 표현 객체 정의

## 엔티티 설계

### [BaseEntity](src/main/kotlin/fc/innercircle/sanghyukonboarding/survey/common/domain/model/BaseEntity.kt)
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
