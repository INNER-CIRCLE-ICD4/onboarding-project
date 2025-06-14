-- 설문조사 테이블
CREATE TABLE surveys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 설문 항목 테이블
CREATE TABLE survey_items (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     survey_id BIGINT NOT NULL,
     name VARCHAR(100) NOT NULL,
     description VARCHAR(500),
     input_type VARCHAR(20) NOT NULL, -- SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTI_CHOICE
     is_required BOOLEAN DEFAULT FALSE,
     is_deleted BOOLEAN DEFAULT FALSE,
     ordering INT DEFAULT 0,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT fk_survey_items_survey_id FOREIGN KEY (survey_id) REFERENCES surveys(id) ON DELETE CASCADE
);

-- 선택형 항목 옵션 테이블
CREATE TABLE survey_item_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_item_id BIGINT NOT NULL,
    content VARCHAR(255) NOT NULL,
    ordering INT DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_survey_item_options_survey_item_id FOREIGN KEY (survey_item_id) REFERENCES survey_items(id) ON DELETE CASCADE
);

-- 설문 응답 테이블
CREATE TABLE survey_responses(
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     survey_id BIGINT NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT fk_survey_responses_survey_id FOREIGN KEY (survey_id) REFERENCES surveys(id) ON DELETE CASCADE
);

-- 응답 항목 테이블 (질문 스냅샷 포함)
CREATE TABLE survey_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    response_id BIGINT NOT NULL,
    survey_item_id BIGINT NOT NULL,

    question_text VARCHAR(255) NOT NULL,  -- 질문 스냅샷
    input_type VARCHAR(20) NOT NULL,      -- 질문 타입 스냅샷
    answer_text TEXT,                     -- 주관식 응답

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_survey_answers_response_id FOREIGN KEY (response_id) REFERENCES survey_responses(id) ON DELETE CASCADE,
    CONSTRAINT fk_survey_answers_survey_item_id FOREIGN KEY (survey_item_id) REFERENCES survey_items(id) ON DELETE CASCADE
);

-- 선택형 응답 테이블 (옵션 스냅샷 포함)
CREATE TABLE survey_answer_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    answer_id BIGINT NOT NULL,
    option_id BIGINT NOT NULL,
    option_text VARCHAR(255) NOT NULL, -- 옵션 스냅샷

    CONSTRAINT fk_survey_answer_options_answer_id FOREIGN KEY (answer_id) REFERENCES survey_answers(id) ON DELETE CASCADE,
    CONSTRAINT fk_survey_answer_options_option_id FOREIGN KEY (option_id) REFERENCES survey_item_options(id) ON DELETE CASCADE
);