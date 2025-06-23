package com.innercircle.survey.response.application.service

import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.application.port.out.ResponseRepository
import com.innercircle.survey.response.domain.Response
import com.innercircle.survey.response.domain.exception.ResponseNotFoundException
import com.innercircle.survey.survey.application.port.out.SurveyRepository
import com.innercircle.survey.survey.domain.Survey
import com.innercircle.survey.survey.domain.exception.SurveyNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.UUID

class ResponseServiceTest : DescribeSpec({
    describe("ResponseService") {
        context("getResponseById") {
            val responseRepository = mockk<ResponseRepository>()
            val surveyRepository = mockk<SurveyRepository>()
            val service = ResponseService(responseRepository, surveyRepository)

            context("존재하는 응답 ID가 주어졌을 때") {
                val responseId = UUID.randomUUID()
                val mockSurvey =
                    mockk<Survey> {
                        every { id } returns UUID.randomUUID()
                        every { title } returns "Test Survey"
                    }
                val mockResponse =
                    mockk<Response> {
                        every { id } returns responseId
                        every { survey } returns mockSurvey
                    }

                it("응답을 반환해야 한다") {
                    every { responseRepository.findById(responseId) } returns mockResponse

                    val result = service.getResponseById(responseId)

                    result shouldBe mockResponse
                    verify(exactly = 1) { responseRepository.findById(responseId) }
                }
            }

            context("존재하지 않는 응답 ID가 주어졌을 때") {
                val responseId = UUID.randomUUID()

                it("ResponseNotFoundException이 발생해야 한다") {
                    every { responseRepository.findById(responseId) } returns null

                    shouldThrow<ResponseNotFoundException> {
                        service.getResponseById(responseId)
                    }
                }
            }
        }

        context("getResponsesBySurveyId") {
            context("존재하는 설문조사 ID로 조회할 때") {
                val responseRepository = mockk<ResponseRepository>()
                val surveyRepository = mockk<SurveyRepository>()
                val service = ResponseService(responseRepository, surveyRepository)

                val surveyId = UUID.randomUUID()
                val pageable = PageRequest.of(0, 10)
                val mockSurvey =
                    mockk<Survey> {
                        every { id } returns surveyId
                    }
                val mockResponses =
                    listOf(
                        mockk<Response>(),
                        mockk<Response>(),
                    )
                val page = PageImpl(mockResponses, pageable, 2)

                it("페이징된 응답 목록을 반환해야 한다") {
                    every { surveyRepository.findById(surveyId) } returns mockSurvey
                    every { responseRepository.findBySurveyIdWithAnswers(surveyId, pageable) } returns page

                    val result = service.getResponsesBySurveyId(surveyId, pageable)

                    result.content shouldBe mockResponses
                    result.totalElements shouldBe 2
                    verify(exactly = 1) { surveyRepository.findById(surveyId) }
                    verify(exactly = 1) { responseRepository.findBySurveyIdWithAnswers(surveyId, pageable) }
                }
            }

            context("존재하지 않는 설문조사 ID로 조회할 때") {
                val responseRepository = mockk<ResponseRepository>(relaxed = true)
                val surveyRepository = mockk<SurveyRepository>()
                val service = ResponseService(responseRepository, surveyRepository)

                val surveyId = UUID.randomUUID()
                val pageable = PageRequest.of(0, 10)

                it("SurveyNotFoundException이 발생해야 한다") {
                    every { surveyRepository.findById(surveyId) } returns null

                    shouldThrow<SurveyNotFoundException> {
                        service.getResponsesBySurveyId(surveyId, pageable)
                    }

                    verify(exactly = 1) { surveyRepository.findById(surveyId) }
                    verify(exactly = 0) { responseRepository.findBySurveyIdWithAnswers(any(), any()) }
                }
            }
        }

        context("getResponseSummariesBySurveyId") {
            val responseRepository = mockk<ResponseRepository>()
            val surveyRepository = mockk<SurveyRepository>()
            val service = ResponseService(responseRepository, surveyRepository)

            context("존재하는 설문조사 ID로 요약 조회할 때") {
                val surveyId = UUID.randomUUID()
                val pageable = PageRequest.of(0, 10)
                val mockSurvey =
                    mockk<Survey> {
                        every { id } returns surveyId
                    }
                val mockSummaries =
                    listOf(
                        ResponseSummaryProjection(
                            id = UUID.randomUUID(),
                            surveyId = surveyId,
                            surveyVersion = 1,
                            respondentId = "user1",
                            createdAt = LocalDateTime.now(),
                            answerCount = 5,
                        ),
                        ResponseSummaryProjection(
                            id = UUID.randomUUID(),
                            surveyId = surveyId,
                            surveyVersion = 1,
                            respondentId = "user2",
                            createdAt = LocalDateTime.now(),
                            answerCount = 3,
                        ),
                    )
                val page = PageImpl(mockSummaries, pageable, 2)

                it("페이징된 응답 요약 목록을 반환해야 한다") {
                    every { surveyRepository.findById(surveyId) } returns mockSurvey
                    every { responseRepository.findResponseSummariesBySurveyId(surveyId, pageable) } returns page

                    val result = service.getResponseSummariesBySurveyId(surveyId, pageable)

                    result.content shouldBe mockSummaries
                    result.totalElements shouldBe 2
                    result.content[0].answerCount shouldBe 5
                    result.content[1].answerCount shouldBe 3
                    verify(exactly = 1) { surveyRepository.findById(surveyId) }
                    verify(exactly = 1) { responseRepository.findResponseSummariesBySurveyId(surveyId, pageable) }
                }
            }
        }
    }
})
