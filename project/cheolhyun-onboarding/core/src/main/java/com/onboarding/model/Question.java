package com.onboarding.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Question {
    private static final String INVALID_TITLE = "항목명이 유효하지 않습니다.";

    private String title;               // 항목 이름
    private String description;         // 항목 설명
    private QuestionType type;          // 항목 입력 형태
    private Options options;
    private boolean required;           // 항목 필수 여부
    private boolean isDeleted;

    public Question(String title) {
        this(title, "");
    }

    public Question(String title, String description) {
        this(title, description, QuestionType.SHORT_TEXT);
    }

    public Question(String title, String description, QuestionType type) {
        this(title, description, type, new Options(), false);
    }

    public Question(String title, String description, QuestionType type, Options options) {
        this(title, description, type, options, false);
    }

    public Question(String title, String description, QuestionType type, Options options, boolean required) {
        validTitle(title);

        this.title = title;
        this.description = description;
        this.type = type;
        this.options = options;
        this.required = required;
        this.isDeleted = false;
    }

    public static Question of(String title, String description) {
        return new Question(title, description);
    }

    public static Question of(String title, String description, QuestionType type) {
        return new Question(title, description, type);
    }

    public static Question of(String title, String description, QuestionType type, Options options) {
        return new Question(title, description, type, options);
    }

    public static Question of(String title, String description, QuestionType type, Options options, boolean required) {
        return new Question(title, description, type, options, required);
    }

    private void validTitle(String title) {
        if(title == null || title.isEmpty()) {
            throw new IllegalArgumentException(INVALID_TITLE);
        }
    }

    public void addOption(String text) {
        options.add(text);
    }

    public void updateOption() {
    }
}
