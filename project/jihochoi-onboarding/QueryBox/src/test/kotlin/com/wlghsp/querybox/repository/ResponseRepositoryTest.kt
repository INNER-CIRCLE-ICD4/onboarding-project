package com.wlghsp.querybox.repository

import com.wlghsp.querybox.domain.response.Answer
import com.wlghsp.querybox.domain.response.Answers
import com.wlghsp.querybox.domain.response.Response
import com.wlghsp.querybox.domain.survey.QuestionType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ResponseRepositoryTest @Autowired constructor(
    val responseRepository: ResponseRepository,
) {

    @DisplayName("surveyId로 응답을 조회 - 성공")
    @Test
    fun getResponse_withSurveyId() {
        val answers1 = Answers.of(
            listOf(
                Answer.of(
                    questionId = 1L,
                    questionName = "가장 좋아하는 언어는?",
                    questionType = QuestionType.SHORT_TEXT,
                    answerValue = "Kotlin"
                )
            )
        )

        val answers2 = Answers.of(
            listOf(
                Answer.of(
                    questionId = 1L,
                    questionName = "가장 좋아하는 언어는?",
                    questionType = QuestionType.SHORT_TEXT,
                    answerValue = "Kotlin"
                )
            )
        )

        val response1 = Response.of(surveyId = 1L, answers = answers1)
        val response2 = Response.of(surveyId = 2L, answers = answers2)

        responseRepository.save(response1)
        responseRepository.save(response2)

        val result = responseRepository.findAllBySurveyId(1L)

        assertThat(result).hasSize(1)
        assertThat(result[0].surveyId).isEqualTo(1L)
        assertThat(result[0].answers!!.values()[0].answerValue).isEqualTo("Kotlin")
    }
}