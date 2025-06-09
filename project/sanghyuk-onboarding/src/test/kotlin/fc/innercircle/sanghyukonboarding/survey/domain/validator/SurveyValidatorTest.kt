package fc.innercircle.sanghyukonboarding.survey.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SurveyValidatorTest : DescribeSpec({
    describe("SurveyValidator 테스트") {
        context("제목(title) 유효성 검증") {
            it("유효한 제목은 예외가 발생하지 않아야 한다") {
                // given
                val validTitle = "유효한 제목"

                // when & then
                SurveyValidator.validateTitle(validTitle)
            }

            it("빈 제목은 예외가 발생해야 한다") {
                // given
                val emptyTitle = ""

                // when
                val exception = shouldThrow<CustomException> {
                    SurveyValidator.validateTitle(emptyTitle)
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_TITLE.withArgs(emptyTitle).message
            }

            it("255자를 초과하는 제목은 예외가 발생해야 한다") {
                // given
                val longTitle = "a".repeat(256)

                // when
                val exception = shouldThrow<CustomException> {
                    SurveyValidator.validateTitle(longTitle)
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_TITLE.withArgs(longTitle).message
            }
        }

        context("설명(description) 유효성 검증") {
            it("유효한 설명은 예외가 발생하지 않아야 한다") {
                // given
                val validDescription = "유효한 설명"

                // when & then
                SurveyValidator.validateDescription(validDescription)
            }

            it("빈 설명은 예외가 발생해야 한다") {
                // given
                val emptyDescription = ""

                // when
                val exception = shouldThrow<CustomException> {
                    SurveyValidator.validateDescription(emptyDescription)
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_DESCRIPTION.withArgs(emptyDescription).message
            }

            it("1000자를 초과하는 설명은 예외가 발생해야 한다") {
                // given
                val longDescription = "a".repeat(1001)

                // when
                val exception = shouldThrow<CustomException> {
                    SurveyValidator.validateDescription(longDescription)
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_DESCRIPTION.withArgs(longDescription).message
            }
        }
    }
})
