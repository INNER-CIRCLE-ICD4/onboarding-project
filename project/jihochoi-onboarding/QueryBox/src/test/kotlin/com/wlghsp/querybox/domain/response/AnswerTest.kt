package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.QuestionType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AnswerTest {

    @DisplayName("주관식 응답을 정상적으로 생성")
    @Test
    fun createTextAnswer() {
        val answer = Answer.of(
            questionId = 1L,
            questionName = "당신의 취미는?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "등산"
        )

        assertThat(answer.questionId).isEqualTo(1L)
        assertThat(answer.questionName).isEqualTo("당신의 취미는?")
        assertThat(answer.questionType).isEqualTo(QuestionType.SHORT_TEXT)
        assertThat(answer.answerValue).isEqualTo("등산")
        assertThat(answer.selectedOptionIds).isNull()
    }

    @DisplayName("선택형 응답을 정상적으로 생성")
    @Test
    fun createChoiceAnswer() {
        val answer = Answer.of(
            questionId = 2L,
            questionName = "선호하는 언어는?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            selectedOptionIds = listOf(1L, 2L)
        )

        assertThat(answer.questionId).isEqualTo(2L)
        assertThat(answer.questionName).isEqualTo("선호하는 언어는?")
        assertThat(answer.questionType).isEqualTo(QuestionType.SINGLE_CHOICE)
        assertThat(answer.answerValue).isNull()
        assertThat(answer.selectedOptionIds).containsExactly(1L, 2L)
    }


}