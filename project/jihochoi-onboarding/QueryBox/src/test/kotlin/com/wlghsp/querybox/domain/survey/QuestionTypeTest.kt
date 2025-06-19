package com.wlghsp.querybox.domain.survey

import org.assertj.core.api.Assertions.assertThatThrownBy
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

    @DisplayName("주관식 항목에 옵션이 존재하면 예외 발생")
    @Test
    fun validateQuestionTextTypeWithOptionsThrowsException() {
        val exceptionMessage = "주관식 항목은 옵션을 가질 수 없습니다."
        val options = Options.of(listOf(Option("옵션1")))

        assertThatThrownBy {
            QuestionType.SHORT_TEXT.validateQuestion(options)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)

        assertThatThrownBy {
            QuestionType.LONG_TEXT.validateQuestion(options)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)
    }

    @DisplayName("선택형 항목에 옵션이 없으면 예외 발생")
    @Test
    fun validateQuestionChoiceTypeWithNoOptionsThrowsException() {
        val exceptionMessage = "선택형 항목에는 옵션이 반드시 있어야 합니다."

        assertThatThrownBy {
            QuestionType.SINGLE_CHOICE.validateQuestion(null)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)

        assertThatThrownBy {
            QuestionType.MULTIPLE_CHOICE.validateQuestion(null)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)
    }

    @DisplayName("주관식 응답이 비어 있으면 예외 발생")
    @Test
    fun validateAnswerTextTypeWithBlankValueThrowsException() {
        val exceptionMessage = "주관식 응답은 비어 있을 수 없습니다."

        assertThatThrownBy {
            QuestionType.SHORT_TEXT.validateAnswer(null, null)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)

        assertThatThrownBy {
            QuestionType.LONG_TEXT.validateAnswer(null, null)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)
    }

    @DisplayName("선택형 응답에서 선택이 없으면 예외 발생")
    @Test
    fun validateAnswerChoiceTypeWithNoSelectionThrowsException() {
        val exceptionMessage = "선택형 응답은 하나 이상 선택해야 합니다."

        assertThatThrownBy {
            QuestionType.SINGLE_CHOICE.validateAnswer(null, null)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)

        assertThatThrownBy {
            QuestionType.MULTIPLE_CHOICE.validateAnswer(null, null)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(exceptionMessage)
    }

}