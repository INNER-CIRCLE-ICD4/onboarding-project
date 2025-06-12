package com.innercircle.survey.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 설문 항목의 입력 형태를 정의하는 열거형
 */
@Getter
@RequiredArgsConstructor
public enum QuestionType {

    /**
     * 단답형 - 한 줄 텍스트 입력
     */
    SHORT_TEXT("단답형", "한 줄 텍스트를 입력받습니다.", false),

    /**
     * 장문형 - 여러 줄 텍스트 입력
     */
    LONG_TEXT("장문형", "여러 줄 텍스트를 입력받습니다.", false),

    /**
     * 단일 선택 리스트 - 여러 옵션 중 하나만 선택
     */
    SINGLE_CHOICE("단일 선택 리스트", "여러 옵션 중 하나만 선택합니다.", true),

    /**
     * 다중 선택 리스트 - 여러 옵션 중 다수 선택 가능
     */
    MULTIPLE_CHOICE("다중 선택 리스트", "여러 옵션 중 다수를 선택합니다.", true);

    private final String displayName;
    private final String description;
    private final boolean requiresOptions;

    /**
     * 선택 옵션이 필요한 타입인지 확인
     *
     * @return 선택 옵션 필요 여부
     */
    public boolean isChoiceType() {
        return requiresOptions;
    }

    /**
     * 텍스트 입력 타입인지 확인
     *
     * @return 텍스트 입력 타입 여부
     */
    public boolean isTextType() {
        return this == SHORT_TEXT || this == LONG_TEXT;
    }

    /**
     * 단일 선택 타입인지 확인
     *
     * @return 단일 선택 타입 여부
     */
    public boolean isSingleChoice() {
        return this == SINGLE_CHOICE;
    }

    /**
     * 다중 선택 타입인지 확인
     *
     * @return 다중 선택 타입 여부
     */
    public boolean isMultipleChoice() {
        return this == MULTIPLE_CHOICE;
    }
}
