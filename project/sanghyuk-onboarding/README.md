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
    - org.jlleitschuh.gradle.ktlint: 코드 스타일 검사 (버전 11.6.1)

- **데이터베이스**: H2 Database (인메모리)
  - **사용 목적**: 개발 및 테스트 환경에서 빠른 설정과 실행 가능

- **ORM**: Spring Data JPA, Hibernate
  - **사용 목적**: 객체 지향적 데이터 접근과 SQL 추상화로 개발 생산성 향상
  - **주요 기능**: 엔티티 매핑, 트랜잭션 관리, 쿼리 생성

- **테스트 프레임워크**:
  - **JUnit5**: 기본 테스트 프레임워크
  - **Kotest**: 더 표현력 있는 테스트 작성을 위한 Kotlin 특화 테스트 프레임워크
    - kotest-runner-junit5: JUnit5 기반 실행 (버전 5.8.1)
    - kotest-assertions-core: 강력한 assertion 라이브러리 (버전 5.8.1)
    - kotest-extensions-spring: 스프링 통합 테스트 지원 (버전 4.4.3)
  - **사용 목적**: 다양한 테스트 스타일 지원과 가독성 높은 테스트 코드 작성

- **코드 스타일 검사**: Ktlint (버전 11.6.1)
  - **사용 목적**: 일관된 코드 스타일 유지와 코드 품질 향상

- **ID 생성 라이브러리**: ULID Creator (버전 5.2.3)
  - **사용 목적**: 분산 환경에서 고유한 ID 생성을 위한 ULID(Universally Unique Lexicographically Sortable Identifier) 구현

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

### 주요 도메인 엔티티

- **Form(설문)**: 설문조사 폼의 기본 정보를 담고 있는 엔티티
  - 제목, 설명 등의 메타데이터 포함
  - 여러 QuestionTemplate과 연결됨
  - createQuestionTemplate 메서드를 통해 QuestionTemplate 생성 가능

- **QuestionTemplate(질문 템플릿)**: 질문 템플릿 정보를 담고 있는 엔티티
  - Form과 연결되어 있으며, 하나의 Form에 여러 QuestionTemplate이 존재할 수 있음
  - 버전 관리를 통해 질문 템플릿의 변경 이력 관리

- **QuestionSnapshot(질문 스냅샷)**: 설문조사의 각 질문을 나타내는 엔티티
  - 질문 내용, 설명, 입력 타입, 필수 여부, 표시 순서 등의 정보 포함
  - QuestionTemplate과 연결되어 있으며, 하나의 QuestionTemplate에 여러 QuestionSnapshot이 존재할 수 있음
  - 버전 관리를 통해 질문의 변경 이력 관리

- **SelectableOption(선택 옵션)**: 선택형 항목(라디오 버튼, 체크박스, 드롭다운)의 옵션을 나타내는 엔티티
  - 옵션 값, 표시 순서 등의 정보 포함
  - QuestionSnapshot과 연결되어 있으며, 하나의 QuestionSnapshot에 여러 SelectableOption이 존재할 수 있음

- **InputType(입력 타입)**: 질문의 입력 타입을 나타내는 열거형
  - TEXT(단답형), LONG_TEXT(장문형), RADIO(라디오 버튼), CHECKBOX(체크박스), SELECT(드롭다운) 등의 타입 제공

- **FormReply(설문 응답)**: 설문조사에 대한 응답 정보를 담고 있는 엔티티
  - Form의 ID, 제출 일시 등의 정보 포함
  - 여러 Answer와 연결됨

- **Answer(설문 답변)**: 설문 항목에 대한 응답 정보를 담고 있는 엔티티
  - FormReply, QuestionSnapshot과 연결되어 있음
  - 주관식 답변(answer)과 선택형 답변(selectableOptionId)을 모두 지원

비즈니스 규칙에 따라 `함께 생성되고 변경되는 객체`들을 각각의 애그리거트로 묶어 다음과 같이 구분하였습니다.

`[Form - QuestionTemplate - QuestionSnapshot - SelectableOption]` 객체들은 "한 설문조사에는 최대 10개의 문항"과 같은 비즈니스 규칙을 한 트랜잭션 내에서 처리하여야 하기 때문에 하나의 애그리거트로 묶었고, 이에 대한 루트 엔티티(단일 진입점)으로 하위 객체들을 아우를 수 있는 Form으로 선정하였습니다.

`[FormReply - Answer]` 객체들 또한, 설문 조사에 대한 응답이 이루어질 때, 공통으로 생성되고 변경되므로 하나의 애그리거트로 묶었으며, 마찬가지로 FormReply 객체가 하위 객체들의 생명 주기를 관리하므로 루트 엔티티로 설정하였습니다.

| 애그리거트              | 루트 엔티티        | 내부 엔티티                                                   | 주요 불변식／트랜잭션 경계                                                        |
|--------------------|---------------|----------------------------------------------------------|-----------------------------------------------------------------------|
| **Form 애그리거트**    | `Form`        | `QuestionTemplate`, `QuestionSnapshot`, `SelectableOption` | - `Form`당 `QuestionSnapshot` ≤ 10- 문항·옵션 CRUD는 한 트랜잭션                 |
| **Response 애그리거트** | `FormReply`   | `Answer`                                                  | - 한 번의 제출 시 `FormReply` + 다수 `Answer` 저장- 응답 검증(필수 여부, 타입 일치) |

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
├── form
│   └── domain
│       ├── model
│       │   └── vo
│       └── validator
└── formreply
    └── model
```

- **common/domain/exception**: 공통 예외 처리 관련 클래스
  - 도메인 규칙 위반 시 발생하는 예외 정의
  - 일관된 오류 코드 및 메시지 관리 ([ErrorCode](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/domain/exception/ErrorCode.kt))

- **common/domain/model**: 공통 도메인 모델
  - [BaseEntity](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/domain/model/BaseEntity.kt): 모든 엔티티의 기본 클래스로 생성/수정 정보 관리

- **form/domain/model**: 설문조사 관련 도메인 모델
  - [Form](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/Form.kt): 설문조사 정보
  - [QuestionTemplate](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/QuestionTemplate.kt): 질문 템플릿 정보
  - [QuestionSnapshot](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/QuestionSnapshot.kt): 질문 스냅샷 정보
  - [SelectableOption](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/SelectableOption.kt): 선택형 항목의 옵션 정보

- **form/domain/model/vo**: 설문조사 관련 값 객체
  - [InputType](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/vo/InputType.kt): 질문 입력 타입 (TEXT, LONG_TEXT, RADIO, CHECKBOX, SELECT)

- **form/domain/validator**: 도메인 모델 유효성 검사 클래스
  - 각 도메인 모델별 전용 검증 로직 구현 ([FormValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/validator/FormValidator.kt), [QuestionTemplateValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/validator/QuestionTemplateValidator.kt), [QuestionSnapshotValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/validator/QuestionSnapshotValidator.kt), [SelectableOptionsValidator](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/validator/SelectableOptionsValidator.kt))
  - 비즈니스 규칙 강제

- **formreply/model**: 설문 응답 관련 도메인 모델
  - [FormReply](src/main/kotlin/fc/innercircle/sanghyukonboarding/formreply/model/FormReply.kt): 설문 응답 정보
  - [Answer](src/main/kotlin/fc/innercircle/sanghyukonboarding/formreply/model/Answer.kt): 설문 항목 응답 정보

## 엔티티 설계

### [BaseEntity](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/domain/model/BaseEntity.kt)
모든 엔티티의 기본이 되는 추상 클래스입니다.
- **createdBy**: 생성자 (최대 50자)
- **createdAt**: 생성 시간
- **updatedBy**: 수정자 (nullable, 최대 50자)
- **updatedAt**: 수정 시간 (nullable)

### [Form](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/Form.kt) (설문 조사)
설문조사의 기본 정보를 담고 있는 엔티티입니다.
- **id**: 설문조사 ID (PK)
- **title**: 설문조사 제목 (최대 255자)
- **description**: 설문조사 설명 (최대 1000자, 기본값: 빈 문자열)
- **createQuestionTemplate**: QuestionTemplate을 생성하는 메서드
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [QuestionTemplate](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/QuestionTemplate.kt) (질문 템플릿)
질문 템플릿 정보를 담고 있는 엔티티입니다.
- **id**: 질문 템플릿 ID (PK)
- **version**: 버전 (기본값: 0)
- **form**: 연관된 설문조사 (FK)
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [QuestionSnapshot](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/QuestionSnapshot.kt) (질문 스냅샷)
설문조사의 각 질문을 나타내는 엔티티입니다.
- **id**: 질문 스냅샷 ID (PK)
- **title**: 질문 내용 (최대 500자)
- **description**: 질문 설명 (최대 1000자, 기본값: 빈 문자열)
- **type**: 항목 유형 (TEXT, LONG_TEXT, RADIO, CHECKBOX, SELECT)
- **required**: 필수 응답 여부 (기본값: false)
- **displayOrder**: 표시 순서 (기본값: 0)
- **version**: 버전 (기본값: 0)
- **questionTemplate**: 연관된 질문 템플릿 (FK)
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [SelectableOption](src/main/kotlin/fc/innercircle/sanghyukonboarding/form/domain/model/SelectableOption.kt) (선택 옵션)
라디오 버튼, 체크박스, 드롭다운과 같은 선택형 항목의 옵션을 나타내는 엔티티입니다.
- **id**: 옵션 ID (PK)
- **value**: 옵션 값 (최대 50자)
- **displayOrder**: 표시 순서 (기본값: 0)
- **questionSnapshot**: 연관된 질문 스냅샷 (FK)
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [FormReply](src/main/kotlin/fc/innercircle/sanghyukonboarding/formreply/model/FormReply.kt) (설문 응답)
설문조사에 대한 응답 정보를 담고 있는 엔티티입니다.
- **id**: 설문 응답 ID (PK)
- **formId**: 연관된 설문조사 ID (String)
- **submittedAt**: 제출 일시 (nullable)
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)

### [Answer](src/main/kotlin/fc/innercircle/sanghyukonboarding/formreply/model/Answer.kt) (설문 항목 응답)
설문 항목에 대한 응답 정보를 담고 있는 엔티티입니다.
- **id**: 설문 항목 응답 ID (PK)
- **formReply**: 연관된 설문 응답 (FK)
- **questionSnapshotId**: 연관된 질문 스냅샷 ID (String)
- **answer**: 주관식 답변 내용 (단답형, 장문형)
- **selectableOptionId**: 선택형 답변용 선택 옵션 ID
- BaseEntity 상속 (createdBy, createdAt, updatedBy, updatedAt)


## RDBMS 스키마

### form 테이블
```sql
CREATE TABLE form (
    id CHAR(26) PRIMARY KEY COMMENT 'ULID 형식의 ID',
    title VARCHAR(255) NOT NULL COMMENT '설문 제목',
    description VARCHAR(1000) NOT NULL COMMENT '설문 설명',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
);
```

### question_template 테이블
```sql
CREATE TABLE question_template (
    id CHAR(26) PRIMARY KEY COMMENT 'ULID 형식의 ID',
    version BIGINT DEFAULT 0 NOT NULL COMMENT '버전',
    form_id CHAR(26) NOT NULL COMMENT '설문 ID',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
    -- 외래 키: form_id -> form.id
);
```

### question_snapshot 테이블
```sql
CREATE TABLE question_snapshot (
    id CHAR(26) PRIMARY KEY COMMENT 'ULID 형식의 ID',
    title VARCHAR(500) NOT NULL COMMENT '질문 제목',
    description VARCHAR(1000) NOT NULL COMMENT '질문 설명',
    type VARCHAR(20) NOT NULL COMMENT '질문 입력 타입',
    required BOOLEAN DEFAULT FALSE NOT NULL COMMENT '필수 여부',
    display_order INT DEFAULT 0 NOT NULL COMMENT '항목 순서',
    version BIGINT DEFAULT 0 NOT NULL COMMENT '버전',
    question_template_id CHAR(26) NOT NULL COMMENT '질문 템플릿 ID',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
    -- 외래 키: question_template_id -> question_template.id
);
```

### selectable_option 테이블
```sql
CREATE TABLE selectable_option (
    id CHAR(26) PRIMARY KEY COMMENT 'ULID 형식의 ID',
    value VARCHAR(50) NOT NULL COMMENT '질문 선택 옵션',
    display_order INT DEFAULT 0 NOT NULL COMMENT '항목 순서',
    question_snapshot_id CHAR(26) NOT NULL COMMENT '질문 스냅샷 ID',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
    -- 외래 키: question_snapshot_id -> question_snapshot.id
);
```

### form_reply 테이블
```sql
CREATE TABLE form_reply (
    id CHAR(26) PRIMARY KEY COMMENT 'ULID 형식의 ID',
    form_id VARCHAR(255) NOT NULL COMMENT '설문 ID',
    submitted_at DATETIME COMMENT '제출 일시',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
    -- 외래 키: form_id -> form.id
);
```

### answer 테이블
```sql
CREATE TABLE answer (
    id CHAR(26) PRIMARY KEY COMMENT 'ULID 형식의 ID',
    form_reply_id CHAR(26) NOT NULL COMMENT '설문 응답 ID',
    question_snapshot_id VARCHAR(255) NOT NULL COMMENT '질문 스냅샷 ID',
    answer TEXT NOT NULL COMMENT '주관식 답변 내용',
    selectable_option_id VARCHAR(255) NOT NULL COMMENT '선택형 답변용 선택 옵션 ID',
    created_by VARCHAR(50) NOT NULL COMMENT '생성자',
    created_at TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_by VARCHAR(50) COMMENT '수정자',
    updated_at TIMESTAMP COMMENT '수정 일시'
    -- 외래 키: form_reply_id -> form_reply.id
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

## ID 채번 전략

본 프로젝트에서는 [대용량 트래픽, 분산 환경]에 대한 엔티티의 안전한 ID 생성을 보장하기 위해 ULID(Universally Unique Lexicographically Sortable Identifier) 전략을 채택하였습니다.

### ULID 구조
- **26자리 문자열** (Crockford's Base32 알파벳 사용: 0123456789ABCDEFGHJKMNPQRSTVWXYZ)
- **첫 10자리**: 타임스탬프 (밀리초)
- **나머지 16자리**: 무작위 값

### ULID 특징
- **시간 정렬 가능**: 타임스탬프가 포함되어 생성 시간 순으로 정렬 가능
- **고유성 보장**: 충돌 가능성이 극히 낮음 (무작위 부분이 16자리)
- **분산 환경 적합**: 중앙 조정 없이 여러 노드에서 독립적으로 생성 가능
- **성능**: 초당 수십만 개의 ID 생성 가능
- **URL 안전**: URL에 사용해도 안전한 문자만 사용
- **가독성**: 숫자와 대문자만 사용하여 가독성이 좋음 (I, L, O, U 제외)

### 왜 ULID를 선택했는가?
기존에는 Snowflake ID 전략을 사용할 계획이었으나, 다음과 같은 이유로 ULID로 변경하였습니다:

1. **분산 시스템 지원**: ULID는 중앙 조정 없이 여러 서버에서 독립적으로 ID를 생성할 수 있어, 분산 시스템에 더 적합합니다.
2. **시간 정렬 가능**: ULID는 Snowflake ID와 마찬가지로 시간 정렬이 가능하지만, 문자열 기반이라 정렬이 더 직관적입니다.
3. **가독성과 디버깅**: 문자열 형식으로 가독성이 좋고, 디버깅이 용이합니다.
4. **충돌 가능성 최소화**: 128비트(16바이트) 크기로 충돌 가능성이 극히 낮습니다.
5. **구현 단순성**: Snowflake ID는 워커 ID와 데이터센터 ID 관리가 필요하지만, ULID는 그런 복잡성이 없습니다.

### 구현 방식
- [UlidGenerator](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/infrastructure/numbering/UlidGenerator.kt): ULID 생성 구현체
- [IdGeneratingEntityListener](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/infrastructure/numbering/IdGeneratingEntityListener.kt): JPA 엔티티 저장 전 ID 자동 생성
- [BaseEntity](src/main/kotlin/fc/innercircle/sanghyukonboarding/common/domain/model/BaseEntity.kt): 모든 엔티티의 기본 클래스로 ID 필드 정의

### 테스트
- [UlidGeneratorTest](src/test/kotlin/fc/innercircle/sanghyukonboarding/common/infrastructure/numbering/UlidGeneratorTest.kt): 대용량 트래픽, 동시성, 분산 환경에서의 안전성 검증
- [UlidGeneratorStructureTest](src/test/kotlin/fc/innercircle/sanghyukonboarding/common/infrastructure/numbering/UlidGeneratorStructureTest.kt): ID 구조 검증
