package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.QuestionType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ResponseTest {

    @DisplayName("응답 엔티티를 정상적으로 생성한다")
    @Test
    fun createResponse_success() {
        val answer = Answer.of(
            questionId = 1L,
            questionName = "취미는?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "등산"
        )

        val answers = Answers.of(listOf(answer))

        val response = Response.of(
            surveyId = 100L,
            answers = answers,
            snapshot = """{"survey": {}, "response": {}}"""
        )

        assertThat(response.surveyId).isEqualTo(100L)
        assertThat(response.answers?.values()).hasSize(1)
        assertThat(response.snapshot).contains("survey")
    }

    @DisplayName("항목 이름 키워드와 응답값 키워드로 필터링하여 DTO 생성 반환")
    @Test
    fun toFilteredDtos_filtersCorrectly() {
        val answer1 = Answer.of(
            questionId = 1L,
            questionName = "취미는?",
            questionType = QuestionType.LONG_TEXT,
            answerValue = "등산"
        )
        val answer2 = Answer.of(
            questionId = 2L,
            questionName = "좋아하는 색상은?",
            questionType = QuestionType.SHORT_TEXT,
            answerValue = "파랑"
        )

        val response = Response.of(
            surveyId = 1L,
            answers = Answers.of(listOf(answer1, answer2)),
            snapshot = "{}"
        )

        val dtos = response.toFilteredDtos("취미","등산")

        assertThat(dtos).hasSize(1)
        assertThat(dtos[0].questionName).isEqualTo("취미는?")
    }

    @DisplayName("Answers가 null일 경우 필터 결과는 비어 있어야 한다")
    @Test
    fun toFilteredDtos_nullAnswersSafeHandling() {
        val response = Response(
            surveyId = 1L,
            answers = null,
            snapshot = "{}"
        )

        val dtos = response.toFilteredDtos("질문", "응답")

        assertThat(dtos).isEmpty()
    }
}
