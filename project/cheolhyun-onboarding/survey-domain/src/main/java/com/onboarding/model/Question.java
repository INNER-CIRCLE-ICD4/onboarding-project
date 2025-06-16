package com.onboarding.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Question {
    private static final String INVALID_ID = "항목의 ID는 필수입니다.";
    private static final String INVALID_TITLE = "항목의 이름은 필수입니다.";

    private String id;

    private String title;               // 항목 이름
    private String description;         // 항목 설명
    private QuestionType type;          // 항목 입력 형태
    private Options options;
    private boolean required;           // 항목 필수 여부
    private boolean isDeleted;

    public Question(String id, String title) {
        this(id, title, "");
    }

    public Question(String id, String title, String description) {
        this(id, title, description, QuestionType.SHORT_TEXT);
    }

    public Question(String id, String title, String description, QuestionType type) {
        this(id, title, description, type, new Options(), false);
    }

    public Question(String id, String title, String description, QuestionType type, Options options) {
        this(id, title, description, type, options, false);
    }

    public Question(String id, String title, String description, QuestionType type, Options options, boolean required) {
        validTitle(title);

        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.options = options;
        this.required = required;
        this.isDeleted = false;
    }

    public static Question of(String id, String title, String description) {
        return new Question(id, title, description);
    }

    public static Question of(String id, String title, String description, QuestionType type) {
        return new Question(id, title, description, type);
    }

    private void validId(String id) {
        if(isNullOrBlank(id)) {
            throw new IllegalArgumentException(INVALID_ID);
        }
    }

    private void validTitle(String title) {
        if(isNullOrBlank(title)) {
            throw new IllegalArgumentException(INVALID_TITLE);
        }
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void addOption(String text) {
        options.add(text);
    }

    public void markDeleted() {
        this.isDeleted = true;
    }

    public boolean isEqualById(String id) {
        return this.id.equals(id);
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
