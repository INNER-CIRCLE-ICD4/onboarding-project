package fc.innercircle.sanghyukonboarding.form.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class QuestionTemplateValidatorTest : DescribeSpec({
    describe("QuestionTemplateValidator 클래스 테스트") {
        context("validateVersion 메서드") {
            it("0 이상의 버전은 예외가 발생하지 않아야 한다") {
                // given
                val version = 0L

                // when & then
                shouldNotThrowAny {
                    QuestionTemplateValidator.validateVersion(version)
                }
            }

            it("양수 버전은 예외가 발생하지 않아야 한다") {
                // given
                val version = 1L

                // when & then
                shouldNotThrowAny {
                    QuestionTemplateValidator.validateVersion(version)
                }
            }

            it("음수 버전은 예외가 발생해야 한다") {
                // given
                val version = -1L

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionTemplateValidator.validateVersion(version)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_VERSION.status
            }
        }
    }
})
