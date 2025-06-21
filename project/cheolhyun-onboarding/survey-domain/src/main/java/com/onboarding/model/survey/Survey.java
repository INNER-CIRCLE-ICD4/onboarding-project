package com.onboarding.model.survey;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Survey {
    private static final String INVALID_TITLE = "설문조사 명이 유효하지 않습니다.";

    private String id;

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
        this(null, title, description, questions);
    }

    public Survey(String id, String title, String description, Questions questions) {
        validTitle(title);

        this.id = id;
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
