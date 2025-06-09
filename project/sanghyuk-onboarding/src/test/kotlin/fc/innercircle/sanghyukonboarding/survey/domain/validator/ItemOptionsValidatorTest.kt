package fc.innercircle.sanghyukonboarding.survey.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ItemOptionsValidatorTest : DescribeSpec({
    describe("ItemOptionsValidator 테스트") {
        context("옵션 텍스트(optionText) 유효성 검증") {
            it("유효한 옵션 텍스트는 예외가 발생하지 않아야 한다") {
                // given
                val validOptionText = "유효한 옵션 텍스트"

                // when & then
                ItemOptionsValidator.validateOptionText(validOptionText)
            }

            it("빈 옵션 텍스트는 예외가 발생해야 한다") {
                // given
                val emptyOptionText = ""

                // when
                val exception = shouldThrow<CustomException> {
                    ItemOptionsValidator.validateOptionText(emptyOptionText)
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.withArgs(emptyOptionText).message
            }

            it("50자를 초과하는 옵션 텍스트는 예외가 발생해야 한다") {
                // given
                val longOptionText = "a".repeat(51)

                // when
                val exception = shouldThrow<CustomException> {
                    ItemOptionsValidator.validateOptionText(longOptionText)
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.withArgs(longOptionText).message
            }
        }

        context("표시 순서(displayOrder) 유효성 검증") {
            it("유효한 표시 순서는 예외가 발생하지 않아야 한다") {
                // given
                val validDisplayOrder = 0

                // when & then
                ItemOptionsValidator.validateDisplayOrder(validDisplayOrder)
            }

            it("음수 표시 순서는 예외가 발생해야 한다") {
                // given
                val negativeDisplayOrder = -1

                // when
                val exception = shouldThrow<CustomException> {
                    ItemOptionsValidator.validateDisplayOrder(negativeDisplayOrder)
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_ITEM_OPTION_DISPLAY_ORDER.withArgs(
                    negativeDisplayOrder.toString()
                ).message
            }
        }
    }
})
