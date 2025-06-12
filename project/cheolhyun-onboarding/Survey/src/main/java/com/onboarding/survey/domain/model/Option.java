package com.onboarding.survey.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Option {
    private static final String TEXT_EMPTY = "옵션에 빈값을 넣을 수 없습니다.";

    private final String text;

    public Option(String text) {
        validText(text);

        this.text = text;
    }

    private void validText(String text) {
        if(text == null || text.isEmpty()) {
            throw new IllegalArgumentException(TEXT_EMPTY);
        }
    }

    public static Option of(String text) {
        return new Option(text);
    }
}
