package com.wlghsp.querybox.domain.survey

import com.wlghsp.querybox.domain.response.Answer
import com.wlghsp.querybox.domain.response.Answers
import com.wlghsp.querybox.ui.dto.QuestionUpdateRequest
import com.wlghsp.querybox.ui.dto.SurveyUpdateRequest
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SurveyTest {

    @DisplayName("설문 생성 성공")
    @Test
    fun createSurvey_success() {
        val questions = Questions.of(
            listOf(
                Question("이름", "당신의 이름은?", QuestionType.SHORT_TEXT, true),
                Question("나이", "당신의 나이는?", QuestionType.SHORT_TEXT, false)
            )
        )
        val survey = Survey.of(title = "기본 설문", description = "간단한 설문입니다.", questions = questions,)

        assertThat(survey.title).isEqualTo("기본 설문")
        assertThat(survey.description).isEqualTo("간단한 설문입니다.")
        assertThat(survey.getQuestions()).hasSize(2)
    }

    @DisplayName("설문 응답 생성 성공")
    @Test
    fun createResponse_success() {
        // given
        val survey = Survey.of(
            title = "언어 선호도 조사",
            description = "개발 언어에 대한 선호도 조사입니다.",
            questions = Questions.of(
                listOf(
                    Question("언어", "가장 좋아하는 언어는?", QuestionType.SHORT_TEXT, true)
                )
            )
        )

        val answers = Answers.of(
            listOf(
                Answer.of(
                    questionId = survey.getQuestions().first().id,
                    questionName = "언어",
                    questionType = QuestionType.SHORT_TEXT,
                    answerValue = "Kotlin"
                )
            )
        )

        // when
        val response = survey.createResponse(answers)

        // then
        assertThat(response.surveyId).isEqualTo(survey.id)
        assertThat(response.answers!!.values()).hasSize(1)
    }

    @DisplayName("설문 제목이 비어 있으면 예외 발생")
    @Test
    fun createSurvey_withoutTitle_shouldFail() {
        assertThatThrownBy {
            Survey.of(
                title = "",
                description = "빈 제목 테스트",
                questions = Questions.of(mutableListOf(
                    Question("Q1", "desc", QuestionType.LONG_TEXT, true, null)
                ))
            ) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("설문 제목은 비어 있을 수 없습니다.")
    }


    @DisplayName("설문 제목과 설문 설명 수정")
    @Test
    fun updateSurvey_basicFields_updated() {
        val originalQuestion = Question("Q1", "desc", QuestionType.LONG_TEXT, true, null)
        val survey = Survey.of("기존 제목", "기존 설명", Questions.of(mutableListOf(originalQuestion)))
        val updateRequest = SurveyUpdateRequest(
            title = "새 제목", description = "새 설명", questions = mutableListOf(
                QuestionUpdateRequest(
                    id = survey.getQuestions().first().id, name = "변경된 질문", description = "변경된 설명",
                    type = QuestionType.LONG_TEXT, required = true, options = null
                )
            )
        )

        survey.update(updateRequest)

        assertThat(survey.title).isEqualTo("새 제목")
        assertThat(survey.description).isEqualTo("새 설명")
    }

}
