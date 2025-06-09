package fc.innercircle.sanghyukonboarding.surveyreply.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.survey.domain.model.vo.InputType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SurveyItemAnswerValidatorTest :
    DescribeSpec({
        describe("SurveyItemAnswerValidator 테스트") {
            context("질문(question) 유효성 검증") {
                it("유효한 질문은 예외가 발생하지 않아야 한다") {
                    // given
                    val validQuestion = "유효한 질문"

                    // when & then
                    SurveyItemAnswerValidator.validateQuestion(validQuestion)
                }

                it("빈 질문은 예외가 발생해야 한다") {
                    // given
                    val emptyQuestion = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswerValidator.validateQuestion(emptyQuestion)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_QUESTION.withArgs(emptyQuestion).message
                }

                it("500자를 초과하는 질문은 예외가 발생해야 한다") {
                    // given
                    val longQuestion = "a".repeat(501)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswerValidator.validateQuestion(longQuestion)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_QUESTION.withArgs(longQuestion).message
                }
            }

            context("설명(description) 유효성 검증") {
                it("유효한 설명은 예외가 발생하지 않아야 한다") {
                    // given
                    val validDescription = "유효한 설명"

                    // when & then
                    SurveyItemAnswerValidator.validateDescription(validDescription)
                }

                it("빈 설명은 예외가 발생해야 한다") {
                    // given
                    val emptyDescription = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswerValidator.validateDescription(emptyDescription)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_DESCRIPTION.withArgs(emptyDescription).message
                }

                it("1000자를 초과하는 설명은 예외가 발생해야 한다") {
                    // given
                    val longDescription = "a".repeat(1001)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswerValidator.validateDescription(longDescription)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_DESCRIPTION.withArgs(longDescription).message
                }
            }

            context("입력 타입(surveyInputType) 유효성 검증") {
                it("유효한 입력 타입은 예외가 발생하지 않아야 한다") {
                    // given
                    val validInputTypes = InputType.entries.map { it.name }

                    // when & then
                    validInputTypes.forEach { inputType ->
                        SurveyItemAnswerValidator.validateSurveyInputType(inputType)
                    }
                }

                it("빈 입력 타입은 예외가 발생해야 한다") {
                    // given
                    val emptyInputType = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswerValidator.validateSurveyInputType(emptyInputType)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_SURVEY_INPUT_TYPE
                            .withArgs(
                                emptyInputType
                            ).message
                }

                it("유효하지 않은 입력 타입은 예외가 발생해야 한다") {
                    // given
                    val invalidInputType = "INVALID_TYPE"

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswerValidator.validateSurveyInputType(invalidInputType)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_SURVEY_INPUT_TYPE
                            .withArgs(
                                invalidInputType
                            ).message
                }
            }

            context("답변(answer) 유효성 검증") {
                it("유효한 답변은 예외가 발생하지 않아야 한다") {
                    // given
                    val validAnswer = "유효한 답변"

                    // when & then
                    SurveyItemAnswerValidator.validateAnswer(validAnswer)
                }

                it("빈 답변은 예외가 발생하지 않아야 한다") {
                    // given
                    val emptyAnswer = ""

                    // when & then
                    SurveyItemAnswerValidator.validateAnswer(emptyAnswer)
                }

                it("50000자를 초과하는 답변은 예외가 발생해야 한다") {
                    // given
                    val longAnswer = "a".repeat(50001)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemAnswerValidator.validateAnswer(longAnswer)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ANSWER_QUESTION.withArgs(longAnswer).message
                }
            }
        }
    })
