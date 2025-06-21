package com.wlghsp.querybox.domain.survey

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class QuestionsTest {

    @DisplayName("Questions 생성 - 유효한 값일 경우 성공")
    @Test
    fun createQuestions_success() {
        assertDoesNotThrow {
            Questions.of(mutableListOf(
                    Question("Q1", "desc", QuestionType.SHORT_TEXT, true, null),
                    Question("Q2", "desc", QuestionType.SHORT_TEXT, true, null),
                )
            )
        }
    }

    @DisplayName("Questions 생성 - 항목 수가 1개 미만일 경우 예외 발생")
    @Test
    fun createQuestions_emptyList_shouldFail() {
        assertThatThrownBy { Questions.of(mutableListOf()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("항목 수는 1~10개 사이여야 합니다.")
    }

    @DisplayName("Questions 생성 - 항목 수가 1개 미만일 경우 예외 발생")
    @Test
    fun createQuestions_questionsOverTen_shouldFail() {
        assertThatThrownBy {
            Questions.of(mutableListOf(
                Question("Q1", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q2", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q3", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q4", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q5", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q6", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q7", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q8", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q9", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q10", "desc", QuestionType.LONG_TEXT, true, null),
                Question("Q11", "desc", QuestionType.LONG_TEXT, true, null),
            )) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("항목 수는 1~10개 사이여야 합니다.")
    }


    @DisplayName("Questions 생성 - 항목 이름 중복일 경우 예외 발생")
    @Test
    fun createQuestions_duplicateName_shouldFail() {
        assertThatThrownBy {
            Questions.of(mutableListOf(
                Question("Q1", "desc", QuestionType.SHORT_TEXT, true, null),
                Question("Q1", "desc", QuestionType.SHORT_TEXT, true, null),
            )) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("항목 이름은 중복될 수 없습니다.")
    }

    @DisplayName("Questions에 Question 추가 - 성공")
    @Test
    fun addQuestion_success() {
        val questions = Questions.of(mutableListOf(
            Question("Q1", "desc", QuestionType.SHORT_TEXT, true, null)
        ))

        val newQuestion = Question("Q2", "desc", QuestionType.LONG_TEXT, true, null)

        assertDoesNotThrow {
            questions.add(newQuestion)
        }
    }

}
