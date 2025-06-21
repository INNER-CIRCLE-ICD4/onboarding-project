package com.onboarding.model.survey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Option {
    private static final String TEXT_EMPTY = "옵션에 빈값을 넣을 수 없습니다.";

    private String id;
    private String text;

    public Option(String text) {
        this(null, text);
    }

    public Option(String id, String text) {
        validText(text);

        this.id = id;
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
