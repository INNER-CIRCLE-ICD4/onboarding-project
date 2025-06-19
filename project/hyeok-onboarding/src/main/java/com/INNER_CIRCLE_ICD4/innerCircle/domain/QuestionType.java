package com.INNER_CIRCLE_ICD4.innerCircle.domain;

public enum QuestionType {
    SHORT,
    LONG,
    SINGLE_CHOICE,
    MULTIPLE_CHOICE;

    public boolean isChoiceType() {
        return this == SINGLE_CHOICE || this == MULTIPLE_CHOICE;
    }

    public boolean isTextType() {
        return this == SHORT || this == LONG;
    }
}