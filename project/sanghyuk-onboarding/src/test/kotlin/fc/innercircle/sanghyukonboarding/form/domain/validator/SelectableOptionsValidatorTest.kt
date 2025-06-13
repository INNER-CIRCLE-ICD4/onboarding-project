package fc.innercircle.sanghyukonboarding.form.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SelectableOptionsValidatorTest : DescribeSpec({
    describe("SelectableOptionsValidator 클래스 테스트") {
        context("validateValue 메서드") {
            it("유효한 옵션 텍스트는 예외가 발생하지 않아야 한다") {
                // given
                val value = "유효한 옵션 텍스트"

                // when & then
                shouldNotThrowAny {
                    SelectableOptionsValidator.validateValue(value)
                }
            }

            it("빈 옵션 텍스트는 예외가 발생해야 한다") {
                // given
                val value = ""

                // when & then
                val exception = shouldThrow<CustomException> {
                    SelectableOptionsValidator.validateValue(value)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.status
            }

            it("공백만 있는 옵션 텍스트는 예외가 발생해야 한다") {
                // given
                val value = "   "

                // when & then
                val exception = shouldThrow<CustomException> {
                    SelectableOptionsValidator.validateValue(value)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.status
            }

            it("50자를 초과하는 옵션 텍스트는 예외가 발생해야 한다") {
                // given
                val value = "a".repeat(51)

                // when & then
                val exception = shouldThrow<CustomException> {
                    SelectableOptionsValidator.validateValue(value)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.status
            }
        }

        context("validateDisplayOrder 메서드") {
            it("0 이상의 순서는 예외가 발생하지 않아야 한다") {
                // given
                val displayOrder = 0

                // when & then
                shouldNotThrowAny {
                    SelectableOptionsValidator.validateDisplayOrder(displayOrder)
                }
            }

            it("양수 순서는 예외가 발생하지 않아야 한다") {
                // given
                val displayOrder = 1

                // when & then
                shouldNotThrowAny {
                    SelectableOptionsValidator.validateDisplayOrder(displayOrder)
                }
            }

            it("음수 순서는 예외가 발생해야 한다") {
                // given
                val displayOrder = -1

                // when & then
                val exception = shouldThrow<CustomException> {
                    SelectableOptionsValidator.validateDisplayOrder(displayOrder)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_ITEM_OPTION_DISPLAY_ORDER.status
            }
        }
    }
})
