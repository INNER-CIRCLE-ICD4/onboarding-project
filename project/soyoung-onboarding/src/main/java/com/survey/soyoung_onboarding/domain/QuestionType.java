package com.survey.soyoung_onboarding.domain;

import lombok.Getter;

@Getter
public enum QuestionType {
    SHORT("short"),
    LONG("long"),
    SINGLE("single"),
    MULTIPLE("multiple"),;

    private final String key;

    QuestionType(String key) {
        this.key = key;
    }
}
