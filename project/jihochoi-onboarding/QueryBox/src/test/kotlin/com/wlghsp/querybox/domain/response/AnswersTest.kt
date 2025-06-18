package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.QuestionType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AnswersTest {

    @DisplayName("중복 없는 유효한 응답들로 Answer 생성 - Success")
    @Test
    fun createValidAnswers() {
        val answer1 = Answer.of(
            questionId = 1L,
            questionName = "취미?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "독서",
        )

        val answer2 = Answer.of(
            questionId = 2L,
            questionName = "선호하는 언어?",
            questionType = QuestionType.SINGLE_CHOICE,
            selectedOptionIds = listOf(1L)
        )
    }
}