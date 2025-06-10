package com.innercircle.survey.survey.application.service

import com.innercircle.survey.survey.application.port.`in`.CreateSurveyUseCase.CreateSurveyCommand
import com.innercircle.survey.survey.application.port.out.SaveSurveyPort
import com.innercircle.survey.survey.domain.exception.InvalidQuestionTypeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CreateSurveyServiceTest : DescribeSpec({
    describe("CreateSurveyService") {
        val saveSurveyPort = mockk<SaveSurveyPort>()
        val service = CreateSurveyService(saveSurveyPort)

        context("createSurvey") {
            context("유효한 설문조사 생성 요청이 주어졌을 때") {
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
                    every { saveSurveyPort.save(any()) } answers { firstArg() }

                    val result = service.createSurvey(command)

                    result shouldNotBe null
                    result.title shouldBe command.title
                    result.description shouldBe command.description
                    result.questions.size shouldBe 2
                    result.version shouldBe 1

                    verify(exactly = 1) { saveSurveyPort.save(any()) }
                }
            }

            context("잘못된 질문 타입이 포함된 요청이 주어졌을 때") {
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

                    verify(exactly = 0) { saveSurveyPort.save(any()) }
                }
            }

            context("텍스트 타입 질문만 포함된 설문조사") {
                val command =
                    CreateSurveyCommand(
                        title = "텍스트 설문조사",
                        description = "텍스트 입력만 받는 설문조사",
                        questions =
                            listOf(
                                CreateSurveyCommand.QuestionCommand(
                                    title = "이름",
                                    description = "이름을 입력해주세요.",
                                    type = "SHORT_TEXT",
                                    required = true,
                                ),
                                CreateSurveyCommand.QuestionCommand(
                                    title = "의견",
                                    description = "의견을 자유롭게 작성해주세요.",
                                    type = "LONG_TEXT",
                                    required = false,
                                ),
                            ),
                    )

                it("선택지 없이 설문조사가 생성되어야 한다") {
                    every { saveSurveyPort.save(any()) } answers { firstArg() }

                    val result = service.createSurvey(command)

                    result.questions.forEach { question ->
                        question.choices shouldBe emptyList()
                    }
                }
            }

            context("선택형 질문이 포함된 설문조사") {
                val command =
                    CreateSurveyCommand(
                        title = "선택형 설문조사",
                        description = "선택지가 있는 설문조사",
                        questions =
                            listOf(
                                CreateSurveyCommand.QuestionCommand(
                                    title = "복수 선택",
                                    description = "여러 개를 선택할 수 있습니다.",
                                    type = "MULTIPLE_CHOICE",
                                    required = true,
                                    choices = listOf("옵션1", "옵션2", "옵션3"),
                                ),
                            ),
                    )

                it("선택지와 함께 설문조사가 생성되어야 한다") {
                    every { saveSurveyPort.save(any()) } answers { firstArg() }

                    val result = service.createSurvey(command)

                    val question = result.questions.first()
                    question.choices.size shouldBe 3
                    question.choices.map { it.text } shouldBe listOf("옵션1", "옵션2", "옵션3")
                }
            }

            context("최대 질문 개수(10개)의 설문조사") {
                val questions =
                    (1..10).map { index ->
                        CreateSurveyCommand.QuestionCommand(
                            title = "질문 $index",
                            description = "설명 $index",
                            type = "SHORT_TEXT",
                            required = false,
                        )
                    }

                val command =
                    CreateSurveyCommand(
                        title = "최대 질문 설문조사",
                        description = "10개 질문을 가진 설문조사",
                        questions = questions,
                    )

                it("10개의 질문으로 설문조사가 생성되어야 한다") {
                    every { saveSurveyPort.save(any()) } answers { firstArg() }

                    val result = service.createSurvey(command)

                    result.questions.size shouldBe 10
                }
            }
        }
    }
})
