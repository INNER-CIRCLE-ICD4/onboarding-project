package fc.innercircle.jinhoonboarding.service

import fc.innercircle.jinhoonboarding.survey.dto.request.CreateSurveyRequest
import fc.innercircle.jinhoonboarding.survey.domain.Question
import fc.innercircle.jinhoonboarding.survey.domain.Survey
import fc.innercircle.jinhoonboarding.survey.domain.QuestionType
import fc.innercircle.jinhoonboarding.survey.repository.SurveyRepository
import fc.innercircle.jinhoonboarding.survey.service.SurveyService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.Test

class SurveyServiceTest {

    lateinit var sut: SurveyService
    lateinit var surveyRepository: SurveyRepository

    @BeforeEach
    fun setUp() {
        surveyRepository = mock(SurveyRepository::class.java)
        sut = SurveyService(surveyRepository)
    }

    @Nested
    inner class CreateSurvey {
        @Test
        @DisplayName("객관식일 경우 options가 null일 경우 exception이 발생한다")
        fun test1() {
            // Given
            val request1 = CreateSurveyRequest(
                title = "test",
                description = "test",
                questions = listOf(
                    CreateSurveyRequest.QuestionDTO(
                        title = "test question",
                        description = "test question",
                        questionType = "SINGLE_SELECT",
                        required = true,
                        options = null
                    )
                )
            )
            val request2 = CreateSurveyRequest(
                title = "test",
                description = "test",
                questions = listOf(
                    CreateSurveyRequest.QuestionDTO(
                        title = "test question",
                        description = "test question",
                        questionType = "SINGLE_SELECT",
                        required = true,
                        options = null
                    )
                )
            )
            // When & Then
            val newSurvey = Survey(
                title = "test question",
                description = "test question",
                questions = mutableListOf()
            )
            val newQuestion1 = Question (
                title = "test question",
                description = "test question",
                questionType = QuestionType.MULTI_SELECT,
                required = true,
                options = mutableListOf("A", "B"),
                survey = newSurvey
            )
            val newQuestion2 = Question (
                title = "test question",
                description = "test question",
                questionType = QuestionType.SHORT_TEXT,
                required = true,
                options = null,
                survey = newSurvey
            )
            newSurvey.questions.add(newQuestion1)
            newSurvey.questions.add(newQuestion2)
            `when`(surveyRepository.save(any())).thenReturn(newSurvey)

            val exception = assertThrows<RuntimeException>{
                sut.createSurvey(request1)
                sut.createSurvey(request2)
            }
            assertEquals(exception.message, "선택형 질문은 옵션값이 필수입니다.")
        }
        @Test
        @DisplayName("질문이 없으면 Exception 발생")
        fun test2() {
            // Given
            // When
            // Then
        }
        @Test
        @DisplayName("")
        fun test3() {
            // Given
            // When
            // Then
        }

    }

}