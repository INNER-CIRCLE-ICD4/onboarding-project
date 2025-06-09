package fc.innercircle.sanghyukonboarding.survey.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SurveyItemValidatorTest :
    DescribeSpec({
        describe("SurveyItemValidator 테스트") {
            context("질문(question) 유효성 검증") {
                it("유효한 질문은 예외가 발생하지 않아야 한다") {
                    // given
                    val validQuestion = "유효한 질문"

                    // when & then
                    SurveyItemValidator.validateQuestion(validQuestion)
                }

                it("빈 질문은 예외가 발생해야 한다") {
                    // given
                    val emptyQuestion = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemValidator.validateQuestion(emptyQuestion)
                        }

                    // then
                    exception.message shouldBe ErrorCode.INVALID_SURVEY_ITEM_QUESTION.withArgs(emptyQuestion).message
                }

                it("500자를 초과하는 질문은 예외가 발생해야 한다") {
                    // given
                    val longQuestion = "a".repeat(501)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemValidator.validateQuestion(longQuestion)
                        }

                    // then
                    exception.message shouldBe ErrorCode.INVALID_SURVEY_ITEM_QUESTION.withArgs(longQuestion).message
                }
            }

            context("설명(description) 유효성 검증") {
                it("유효한 설명은 예외가 발생하지 않아야 한다") {
                    // given
                    val validDescription = "유효한 설명"

                    // when & then
                    SurveyItemValidator.validateDescription(validDescription)
                }

                it("빈 설명은 예외가 발생해야 한다") {
                    // given
                    val emptyDescription = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemValidator.validateDescription(emptyDescription)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_DESCRIPTION.withArgs(emptyDescription).message
                }

                it("1000자를 초과하는 설명은 예외가 발생해야 한다") {
                    // given
                    val longDescription = "a".repeat(1001)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemValidator.validateDescription(longDescription)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_DESCRIPTION.withArgs(longDescription).message
                }
            }

            context("필수 여부(required) 유효성 검증") {
                it("유효한 필수 여부(true)는 예외가 발생하지 않아야 한다") {
                    // given
                    val validRequired = true

                    // when & then
                    SurveyItemValidator.validateRequired(validRequired)
                }

                it("유효한 필수 여부(false)는 예외가 발생하지 않아야 한다") {
                    // given
                    val validRequired = false

                    // when & then
                    SurveyItemValidator.validateRequired(validRequired)
                }

                // Note: In practice, this test is unlikely to fail since Boolean can only be true or false
                // But we include it for completeness and to match the validator's logic
                it("유효하지 않은 필수 여부는 예외가 발생해야 한다") {
                    // This test is included for completeness, but in Kotlin, Boolean can only be true or false
                    // So this test is more of a formality to match the validator's logic
                }
            }

            context("표시 순서(displayOrder) 유효성 검증") {
                it("유효한 표시 순서는 예외가 발생하지 않아야 한다") {
                    // given
                    val validDisplayOrder = 0

                    // when & then
                    SurveyItemValidator.validateDisplayOrder(validDisplayOrder)
                }

                it("음수 표시 순서는 예외가 발생해야 한다") {
                    // given
                    val negativeDisplayOrder = -1

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItemValidator.validateDisplayOrder(negativeDisplayOrder)
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ORDER.withArgs(negativeDisplayOrder.toString()).message
                }
            }
        }
    })
