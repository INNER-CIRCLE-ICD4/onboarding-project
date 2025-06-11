package fc.innercircle.sanghyukonboarding.surveyreply.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.survey.domain.model.vo.InputType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class SurveyItemAnswerTest :
    DescribeSpec({
        describe("SurveyItemAnswer 클래스 테스트") {
            context("SurveyItemAnswer 생성") {
                it("유효한 파라미터로 SurveyItemAnswer를 생성할 수 있어야 한다") {
                    // given
                    val surveyReply =
                        SurveyReply(
                            surveyId = 1L,
                            responseDate = LocalDateTime.now(),
                            createdBy = "테스트 사용자"
                        )
                    val surveyItemId = 1L
                    val question = "설문 질문"
                    val description = "설문 설명"
                    val required = true
                    val surveyInputType = InputType.TEXT.name
                    val answer = "답변 내용"
                    val selectedItemOptions = listOf("옵션1", "옵션2")
                    val createdBy = "테스트 사용자"

                    // when
                    val surveyItemAnswer =
                        SurveyItemAnswer(
                            surveyReply = surveyReply,
                            surveyItemId = surveyItemId,
                            question = question,
                            description = description,
                            required = required,
                            surveyInputType = surveyInputType,
                            answer = answer,
                            selectedItemOptions = selectedItemOptions,
                            createdBy = createdBy
                        )

                    // then
                    surveyItemAnswer.surveyReply shouldBe surveyReply
                    surveyItemAnswer.surveyItemId shouldBe surveyItemId
                    surveyItemAnswer.question shouldBe question
                    surveyItemAnswer.description shouldBe description
                    surveyItemAnswer.required shouldBe required
                    surveyItemAnswer.type shouldBe surveyInputType
                    surveyItemAnswer.answer shouldBe answer
                    surveyItemAnswer.selectedItemOptions.texts shouldBe selectedItemOptions
                }

                it("빈 질문으로 SurveyItemAnswer를 생성하면 예외가 발생해야 한다") {
                    // given
                    val surveyReply =
                        SurveyReply(
                            surveyId = 1L,
                            responseDate = LocalDateTime.now(),
                            createdBy = "테스트 사용자"
                        )
                    val emptyQuestion = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswer(
                                surveyReply = surveyReply,
                                surveyItemId = 1L,
                                question = emptyQuestion,
                                description = "설문 설명",
                                required = true,
                                surveyInputType = InputType.TEXT.name,
                                answer = "답변 내용",
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_QUESTION.withArgs(emptyQuestion).message
                }

                it("빈 설명으로 SurveyItemAnswer를 생성하면 예외가 발생해야 한다") {
                    // given
                    val surveyReply =
                        SurveyReply(
                            surveyId = 1L,
                            responseDate = LocalDateTime.now(),
                            createdBy = "테스트 사용자"
                        )
                    val emptyDescription = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswer(
                                surveyReply = surveyReply,
                                surveyItemId = 1L,
                                question = "설문 질문",
                                description = emptyDescription,
                                required = true,
                                surveyInputType = InputType.TEXT.name,
                                answer = "답변 내용",
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_DESCRIPTION.withArgs(emptyDescription).message
                }

                it("유효하지 않은 입력 타입으로 SurveyItemAnswer를 생성하면 예외가 발생해야 한다") {
                    // given
                    val surveyReply =
                        SurveyReply(
                            surveyId = 1L,
                            responseDate = LocalDateTime.now(),
                            createdBy = "테스트 사용자"
                        )
                    val invalidInputType = "INVALID_TYPE"

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswer(
                                surveyReply = surveyReply,
                                surveyItemId = 1L,
                                question = "설문 질문",
                                description = "설문 설명",
                                required = true,
                                surveyInputType = invalidInputType,
                                answer = "답변 내용",
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_SURVEY_INPUT_TYPE
                            .withArgs(
                                invalidInputType
                            ).message
                }

                it("50000자를 초과하는 답변으로 SurveyItemAnswer를 생성하면 예외가 발생해야 한다") {
                    // given
                    val surveyReply =
                        SurveyReply(
                            surveyId = 1L,
                            responseDate = LocalDateTime.now(),
                            createdBy = "테스트 사용자"
                        )
                    val longAnswer = "a".repeat(50001)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswer(
                                surveyReply = surveyReply,
                                surveyItemId = 1L,
                                question = "설문 질문",
                                description = "설문 설명",
                                required = true,
                                surveyInputType = InputType.TEXT.name,
                                answer = longAnswer,
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_QUESTION.withArgs(longAnswer).message
                }
            }
        }
    })
