package com.innercircle.survey.survey.domain

enum class QuestionType(
    val description: String,
    val requiresChoices: Boolean,
) {
    SHORT_TEXT("단답형", false),

    LONG_TEXT("장문형", false),

    SINGLE_CHOICE("단일 선택", true),

    MULTIPLE_CHOICE("다중 선택", true),
    ;

    fun isTextType(): Boolean = !requiresChoices

    fun isChoiceType(): Boolean = requiresChoices
}
