package com.wlghsp.querybox.application

import com.wlghsp.querybox.domain.survey.QuestionType
import com.wlghsp.querybox.repository.ResponseRepository
import com.wlghsp.querybox.ui.dto.AnswerRequest
import com.wlghsp.querybox.ui.dto.QuestionRequest
import com.wlghsp.querybox.ui.dto.ResponseCreateRequest
import com.wlghsp.querybox.ui.dto.SurveyCreateRequest
import jakarta.transaction.Transactional
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class ResponseServiceTest @Autowired constructor(
    private val responseService: ResponseService,
    private val surveyService: SurveyService,
    private val responseRepository: ResponseRepository,
) {

    @DisplayName("submit() — 응답 저장")
    @Test
    fun submit_savesResponse() {
        // given
        val surveyId = createSurvey()
        val question = surveyService.findSurveyWithQuestionsById(surveyId).getQuestions().first()

        val request = ResponseCreateRequest(
            answers = listOf(
                AnswerRequest(
                    questionId = question.id,
                    questionName = question.name,
                    questionType = question.type,
                    answerValue = "Kotlin",
                    selectedOptionIds = emptyList(),
                    selectedOptionTexts = emptyList()
                )
            )
        )

        // when
        responseService.submit(surveyId, request)

        // then
        val responses = responseRepository.findAllBySurveyId(surveyId)
        assertThat(responses).hasSize(1)
        assertThat(responses.first().answers!!.values().first().answerValue).isEqualTo("Kotlin")
    }

    @DisplayName("searchResponses() — 키워드 필터링")
    @Test
    fun searchResponses_filtersByKeyword() {
        // given
        val surveyId = createSurvey()
        val question = surveyService.findSurveyWithQuestionsById(surveyId).getQuestions().first()

        val kotlinRequest = ResponseCreateRequest(
            answers = listOf(
                AnswerRequest(
                    questionId = question.id,
                    questionName = question.name,
                    questionType = question.type,
                    answerValue = "Kotlin",
                    selectedOptionIds = emptyList(),
                    selectedOptionTexts = emptyList()
                )
            )
        )
        val javaRequest = kotlinRequest.copy(
            answers = listOf(
                kotlinRequest.answers.first().copy(answerValue = "Java")
            )
        )

        responseService.submit(surveyId, kotlinRequest)
        responseService.submit(surveyId, javaRequest)

        // when
        val filtered = responseService.searchResponses(
            surveyId = surveyId,
            questionKeyword = null,
            answerKeyword = "Kotlin"
        )

        // then
        assertThat(filtered).hasSize(1)
        assertThat(filtered.first().answerValue).isEqualTo("Kotlin")
    }

    private fun createSurvey(): Long =
        surveyService.create(
            SurveyCreateRequest(
                title = "응답 테스트 설문",
                description = "설명",
                questions = listOf(
                    QuestionRequest(
                        name = "선호 언어?",
                        description = "당신이 가장 선호하는 언어",
                        type = QuestionType.SHORT_TEXT,
                        required = true,
                        options = null
                    )
                )
            )
        )
}