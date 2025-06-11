# 데이터베이스 설계

## 데이터베이스 테이블 구조

### 핵심 설계 원칙
- **히스토리 추적**: 모든 변경사항을 추적 가능하도록 설계
- **UUID 7 사용**: 모든 기본키는 UUID 7 형식으로 생성
    - **시간 순서**: UUID 7의 시간 기반 정렬 지원
    - **성능**: 데이터베이스 인덱스 효율성 향상
    - **분산 시스템**: 중복 없는 고유 식별자 보장

### 1. 설문조사 기본 정보 (surveys)
```sql
CREATE TABLE surveys (
    id CHAR(36) PRIMARY KEY, -- UUID 7
    title VARCHAR(255) NOT NULL,
    description TEXT,
    current_version INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 2. 설문조사 버전별 스냅샷 (survey_versions)
```sql
CREATE TABLE survey_versions (
    id CHAR(36) PRIMARY KEY, -- UUID 7
    survey_id CHAR(36) NOT NULL,
    version INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- FOREIGN KEY (survey_id) REFERENCES surveys(id),
    UNIQUE KEY uk_survey_version (survey_id, version)
);
```

### 3. 설문 항목 (survey_questions)
```sql
CREATE TABLE survey_questions (
    id CHAR(36) PRIMARY KEY, -- UUID 7
    survey_version_id CHAR(36) NOT NULL,
    question_order INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    input_type ENUM('SHORT_TEXT', 'LONG_TEXT', 'SINGLE_CHOICE', 'MULTIPLE_CHOICE') NOT NULL,
    is_required BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- FOREIGN KEY (survey_version_id) REFERENCES survey_versions(id),
    INDEX idx_survey_version_order (survey_version_id, question_order)
);
```

### 4. 선택형 항목의 옵션 (question_options)
```sql
CREATE TABLE question_options (
    id CHAR(36) PRIMARY KEY, -- UUID 7
    question_id CHAR(36) NOT NULL,
    option_order INT NOT NULL,
    option_text VARCHAR(255) NOT NULL,
    -- FOREIGN KEY (question_id) REFERENCES survey_questions(id),
    INDEX idx_question_order (question_id, option_order)
);
```

### 5. 설문 응답 (survey_responses)
```sql
CREATE TABLE survey_responses (
    id CHAR(36) PRIMARY KEY, -- UUID 7
    survey_id CHAR(36) NOT NULL,
    survey_version_id CHAR(36) NOT NULL,
    respondent_email VARCHAR(255), -- 익명 응답 시 NULL 가능
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- FOREIGN KEY (survey_id) REFERENCES surveys(id),
    -- FOREIGN KEY (survey_version_id) REFERENCES survey_versions(id)
);
```

### 6. 응답 상세 데이터 (response_answers)
```sql
CREATE TABLE response_answers (
    id CHAR(36) PRIMARY KEY, -- UUID 7
    response_id CHAR(36) NOT NULL,
    question_id CHAR(36) NOT NULL,
    answer_text TEXT,
    selected_option_ids JSON, -- 선택형의 경우 선택된 옵션 ID 배열
    -- FOREIGN KEY (response_id) REFERENCES survey_responses(id),
    -- FOREIGN KEY (question_id) REFERENCES survey_questions(id)
);
```

---

## 데이터 타입 정의

### OptionType (항목 유형)
```java
public enum OptionType {
    SHORT_TEXT,    // 단답형
    LONG_TEXT,     // 장문형
    SINGLE_CHOICE,   // 단일 선택 리스트
    MULTIPLE_CHOICE  // 다중 선택 리스트
}
```