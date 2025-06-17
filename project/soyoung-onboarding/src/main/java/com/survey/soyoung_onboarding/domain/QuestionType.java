package com.survey.soyoung_onboarding.domain;

import lombok.Getter;

@Getter
public enum QuestionType {
    SHORT(""),
    LONG(""),
    SINGLE(""),
    MULTIPLE("");

    private final String key;

    QuestionType(String key) {
        this.key = key;
    }
}
