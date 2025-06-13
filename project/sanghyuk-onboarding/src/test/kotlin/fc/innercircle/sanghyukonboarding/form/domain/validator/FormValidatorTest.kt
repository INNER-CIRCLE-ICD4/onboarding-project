package fc.innercircle.sanghyukonboarding.form.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class FormValidatorTest : DescribeSpec({
    describe("FormValidator 클래스 테스트") {
        context("validateTitle 메서드") {
            it("유효한 title은 예외가 발생하지 않아야 한다") {
                // given
                val title = "유효한 설문 제목"

                // when & then
                shouldNotThrowAny {
                    FormValidator.validateTitle(title)
                }
            }

            it("빈 문자열인 title은 예외가 발생해야 한다") {
                // given
                val title = ""

                // when & then
                val exception = shouldThrow<CustomException> {
                    FormValidator.validateTitle(title)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_TITLE.status
            }

            it("공백 문자만 있는 title은 예외가 발생해야 한다") {
                // given
                val title = "   "

                // when & then
                val exception = shouldThrow<CustomException> {
                    FormValidator.validateTitle(title)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_TITLE.status
            }

            it("255자를 초과하는 title은 예외가 발생해야 한다") {
                // given
                val title = "a".repeat(256)

                // when & then
                val exception = shouldThrow<CustomException> {
                    FormValidator.validateTitle(title)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_TITLE.status
            }

            it("255자 이하의 title은 예외가 발생하지 않아야 한다") {
                // given
                val title = "a".repeat(255)

                // when & then
                shouldNotThrowAny {
                    FormValidator.validateTitle(title)
                }
            }
        }

        context("validateDescription 메서드") {
            it("유효한 description은 예외가 발생하지 않아야 한다") {
                // given
                val description = "유효한 설문 설명"

                // when & then
                shouldNotThrowAny {
                    FormValidator.validateDescription(description)
                }
            }

            it("빈 문자열인 description은 예외가 발생하지 않아야 한다") {
                // given
                val description = ""

                // when & then
                shouldNotThrowAny {
                    FormValidator.validateDescription(description)
                }
            }

            it("공백 문자만 있는 description은 예외가 발생하지 않아야 한다") {
                // given
                val description = "   "

                // when & then
                shouldNotThrowAny {
                    FormValidator.validateDescription(description)
                }
            }

            it("1000자를 초과하는 description은 예외가 발생해야 한다") {
                // given
                val description = "a".repeat(1001)

                // when & then
                val exception = shouldThrow<CustomException> {
                    FormValidator.validateDescription(description)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_DESCRIPTION.status
            }

            it("1000자 이하의 description은 예외가 발생하지 않아야 한다") {
                // given
                val description = "a".repeat(1000)

                // when & then
                shouldNotThrowAny {
                    FormValidator.validateDescription(description)
                }
            }
        }
    }
})
