package com.wlghsp.querybox.application

import com.wlghsp.querybox.domain.survey.*
import com.wlghsp.querybox.ui.dto.AnswerRequest
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ResponseValidatorServiceTest {

    private val validator = ResponseValidatorService()

    @DisplayName("필수 항목인데 응답이 없으면 예외 발생")
    @Test
    fun throwsException_whenRequiredTrueQuestion_notAnswered() {
        val questions = createQuestions()
        val answers = listOf(
            AnswerRequest(
                questionId = questions.values().first().id,
                questionName = "언어?",
                questionType = QuestionType.SHORT_TEXT,
                answerValue = "",
                selectedOptionIds = emptyList(),
                selectedOptionTexts = emptyList()
            )
        )

        assertThatThrownBy {
            validator.validateAnswers(questions, answers)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("필수 항목에는 반드시 응답해야 합니다")
    }

    @DisplayName("질문 수와 응답 수가 다르면 예외 발생")
    @Test
    fun throwsException_whenQuestionCountAnswerCountNotMatched() {
        val questions = createQuestions()
        val answers = emptyList<AnswerRequest>()

        assertThatThrownBy {
            validator.validateAnswers(questions, answers)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("질문 수와 응답 수가 일치하지 않습니다")
    }

    @DisplayName("응답 대상 항목이 존재하지 않으면 예외 발생")
    @Test
    fun throwsException_whenQuestionCountAnswerNotMatched() {
        val questions = createQuestions()
        val answers = listOf(
            AnswerRequest(
                questionId = 999L,
                questionName = "없는 항목",
                questionType = QuestionType.SHORT_TEXT,
                answerValue = "Kotlin",
                selectedOptionIds = emptyList(),
                selectedOptionTexts = emptyList()
            )
        )

        assertThatThrownBy {
            validator.validateAnswers(questions, answers)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("응답 대상 항목이 설문에 존재하지 않습니다")
    }

    private fun createQuestions(): Questions {
        val question = Question(
            name = "언어?",
            description = "좋아하는 언어",
            type = QuestionType.SHORT_TEXT,
            required = true,
            options = null
        )
        return Questions.of(listOf(question))
    }
}