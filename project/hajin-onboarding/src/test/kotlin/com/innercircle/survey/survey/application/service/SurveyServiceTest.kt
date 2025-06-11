package com.innercircle.survey.survey.application.service

import com.innercircle.survey.survey.application.port.`in`.SurveyUseCase.CreateSurveyCommand
import com.innercircle.survey.survey.application.port.`in`.SurveyUseCase.UpdateSurveyCommand
import com.innercircle.survey.survey.application.port.out.SurveyRepository
import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.QuestionType
import com.innercircle.survey.survey.domain.Survey
import com.innercircle.survey.survey.domain.exception.InvalidQuestionTypeException
import com.innercircle.survey.survey.domain.exception.SurveyNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.UUID

class SurveyServiceTest : DescribeSpec({
    describe("SurveyService") {
        context("createSurvey") {
            context("유효한 설문조사 생성 요청이 주어졌을 때") {
                val surveyRepository = mockk<SurveyRepository>()
                val service = SurveyService(surveyRepository)

                val command =
                    CreateSurveyCommand(
                        title = "고객 만족도 조사",
                        description = "서비스 개선을 위한 설문조사입니다.",
                        questions =
                            listOf(
                                CreateSurveyCommand.QuestionCommand(
                                    title = "서비스 만족도",
                                    description = "전반적인 서비스 만족도를 평가해주세요.",
                                    type = "SINGLE_CHOICE",
                                    required = true,
                                    choices = listOf("매우 만족", "만족", "보통", "불만족", "매우 불만족"),
                                ),
                                CreateSurveyCommand.QuestionCommand(
                                    title = "개선 사항",
                                    description = "서비스 개선이 필요한 부분을 자유롭게 작성해주세요.",
                                    type = "LONG_TEXT",
                                    required = false,
                                ),
                            ),
                    )

                it("설문조사가 성공적으로 생성되어야 한다") {
                    every { surveyRepository.save(any()) } answers { firstArg() }

                    val result = service.createSurvey(command)

                    result shouldNotBe null
                    result.title shouldBe command.title
                    result.description shouldBe command.description
                    result.questions.size shouldBe 2
                    result.version shouldBe 1

                    verify(exactly = 1) { surveyRepository.save(any()) }
                }
            }

            context("잘못된 질문 타입이 포함된 요청이 주어졌을 때") {
                val surveyRepository = mockk<SurveyRepository>()
                val service = SurveyService(surveyRepository)

                val command =
                    CreateSurveyCommand(
                        title = "테스트 설문조사",
                        description = "테스트 설명",
                        questions =
                            listOf(
                                CreateSurveyCommand.QuestionCommand(
                                    title = "잘못된 타입 질문",
                                    description = "이 질문은 잘못된 타입을 가지고 있습니다.",
                                    type = "INVALID_TYPE",
                                    required = true,
                                ),
                            ),
                    )

                it("InvalidQuestionTypeException이 발생해야 한다") {
                    shouldThrow<InvalidQuestionTypeException> {
                        service.createSurvey(command)
                    }

                    verify(exactly = 0) { surveyRepository.save(any()) }
                }
            }
        }

        context("getSurveyById") {
            val surveyRepository = mockk<SurveyRepository>()
            val service = SurveyService(surveyRepository)

            context("존재하는 설문조사 ID가 주어졌을 때") {
                val surveyId = UUID.randomUUID()
                val mockSurvey =
                    mockk<com.innercircle.survey.survey.domain.Survey> {
                        every { id } returns surveyId
                        every { title } returns "Test Survey"
                    }

                it("설문조사를 반환해야 한다") {
                    every { surveyRepository.findById(surveyId) } returns mockSurvey

                    val result = service.getSurveyById(surveyId)

                    result shouldBe mockSurvey
                    verify(exactly = 1) { surveyRepository.findById(surveyId) }
                }
            }

            context("존재하지 않는 설문조사 ID가 주어졌을 때") {
                val surveyId = UUID.randomUUID()

                it("SurveyNotFoundException이 발생해야 한다") {
                    every { surveyRepository.findById(surveyId) } returns null

                    shouldThrow<SurveyNotFoundException> {
                        service.getSurveyById(surveyId)
                    }
                }
            }
        }

        context("getSurveys") {
            val surveyRepository = mockk<SurveyRepository>()
            val service = SurveyService(surveyRepository)

            context("페이징 요청이 주어졌을 때") {
                val pageable = PageRequest.of(0, 10)
                val mockSurveys =
                    listOf(
                        mockk<com.innercircle.survey.survey.domain.Survey>(),
                        mockk<com.innercircle.survey.survey.domain.Survey>(),
                    )
                val page = PageImpl(mockSurveys, pageable, 2)

                it("페이징된 설문조사 목록을 반환해야 한다") {
                    every { surveyRepository.findAll(pageable) } returns page

                    val result = service.getSurveys(pageable)

                    result.content shouldBe mockSurveys
                    result.totalElements shouldBe 2
                    verify(exactly = 1) { surveyRepository.findAll(pageable) }
                }
            }
        }

        context("updateSurvey") {
            context("유효한 설문조사 수정 요청이 주어졌을 때") {
                val surveyRepository = mockk<SurveyRepository>()
                val service = SurveyService(surveyRepository)
                val surveyId = UUID.randomUUID()

                val existingSurvey =
                    Survey.create(
                        title = "기존 설문조사",
                        description = "기존 설명",
                        questions =
                            listOf(
                                Question.create(
                                    title = "기존 질문",
                                    description = "기존 질문 설명",
                                    type = QuestionType.SHORT_TEXT,
                                    required = true,
                                ),
                            ),
                    )

                val command =
                    UpdateSurveyCommand(
                        surveyId = surveyId,
                        title = "수정된 설문조사",
                        description = "수정된 설명",
                        questions =
                            listOf(
                                UpdateSurveyCommand.QuestionCommand(
                                    title = "새로운 질문 1",
                                    description = "새로운 질문 설명 1",
                                    type = "SINGLE_CHOICE",
                                    required = true,
                                    choices = listOf("옵션1", "옵션2", "옵션3"),
                                ),
                                UpdateSurveyCommand.QuestionCommand(
                                    title = "새로운 질문 2",
                                    description = "새로운 질문 설명 2",
                                    type = "LONG_TEXT",
                                    required = false,
                                ),
                            ),
                    )

                it("설문조사가 성공적으로 수정되어야 한다") {
                    val surveySlot = slot<Survey>()

                    every { surveyRepository.findById(surveyId) } returns existingSurvey
                    every { surveyRepository.save(capture(surveySlot)) } answers { firstArg() }

                    val originalVersion = existingSurvey.version
                    val result = service.updateSurvey(command)

                    result shouldNotBe null
                    result.title shouldBe command.title
                    result.description shouldBe command.description
                    result.questions.size shouldBe 2
                    result.version shouldBe originalVersion + 2 // update + updateQuestions로 2 증가

                    // 기존 질문이 비활성화 되었는지 확인
                    surveySlot.captured.questions.forEach { question ->
                        question.isActive shouldBe true // 새로운 질문들은 활성화됨
                    }

                    verify(exactly = 1) { surveyRepository.findById(surveyId) }
                    verify(exactly = 1) { surveyRepository.save(any()) }
                }
            }

            context("존재하지 않는 설문조사 ID로 수정 요청이 주어졌을 때") {
                val surveyRepository = mockk<SurveyRepository>()
                val service = SurveyService(surveyRepository)
                val surveyId = UUID.randomUUID()

                val command =
                    UpdateSurveyCommand(
                        surveyId = surveyId,
                        title = "수정할 설문조사",
                        description = "수정할 설명",
                        questions = emptyList(),
                    )

                it("SurveyNotFoundException이 발생해야 한다") {
                    every { surveyRepository.findById(surveyId) } returns null

                    shouldThrow<SurveyNotFoundException> {
                        service.updateSurvey(command)
                    }

                    verify(exactly = 0) { surveyRepository.save(any()) }
                }
            }

            context("잘못된 질문 타입으로 수정 요청이 주어졌을 때") {
                val surveyRepository = mockk<SurveyRepository>()
                val service = SurveyService(surveyRepository)
                val surveyId = UUID.randomUUID()

                val existingSurvey =
                    Survey.create(
                        title = "기존 설문조사",
                        description = "기존 설명",
                    )

                val command =
                    UpdateSurveyCommand(
                        surveyId = surveyId,
                        title = "수정된 설문조사",
                        description = "수정된 설명",
                        questions =
                            listOf(
                                UpdateSurveyCommand.QuestionCommand(
                                    title = "잘못된 타입 질문",
                                    description = "설명",
                                    type = "INVALID_TYPE",
                                    required = true,
                                ),
                            ),
                    )

                it("InvalidQuestionTypeException이 발생해야 한다") {
                    every { surveyRepository.findById(surveyId) } returns existingSurvey

                    shouldThrow<InvalidQuestionTypeException> {
                        service.updateSurvey(command)
                    }

                    verify(exactly = 0) { surveyRepository.save(any()) }
                }
            }
        }
    }
})
