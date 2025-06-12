package com.onboarding.survey.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Survey {
    private static final String INVALID_TITLE = "설문조사 명이 유효하지 않습니다.";

    private String title;
    private String description;
    private Questions questions;

    public Survey(String title) {
        this(title, "");
    }

    public Survey(String title, String description) {
        this(title, description, new Questions());
    }

    public Survey(String title, String description, Questions questions) {
        validTitle(title);

        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    private void validTitle(String title) {
        if(title == null || title.isEmpty()) {
            throw new IllegalArgumentException(INVALID_TITLE);
        }
    }
}
