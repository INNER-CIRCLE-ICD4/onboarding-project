package com.wlghsp.querybox.domain.survey

enum class QuestionType {
    SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE;

    // 단일 or 다중 선택 리스트 인지 확인
    fun isChoice(): Boolean = this == SINGLE_CHOICE || this == MULTIPLE_CHOICE
}
