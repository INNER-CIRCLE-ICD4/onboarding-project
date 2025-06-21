package com.wlghsp.querybox.domain.survey

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class QuestionTypeTest {

    @DisplayName("단일 또는 다중 선택 리스트인지 확인")
    @Test
    fun isChoiceMethodTest() {
        assertThat(QuestionType.SHORT_TEXT.isChoice()).isFalse()
        assertThat(QuestionType.LONG_TEXT.isChoice()).isFalse()
        assertThat(QuestionType.SINGLE_CHOICE.isChoice()).isTrue()
        assertThat(QuestionType.MULTIPLE_CHOICE.isChoice()).isTrue()
    }
}