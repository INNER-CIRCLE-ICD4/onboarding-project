package fc.innercircle.sanghyukonboarding.form.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class QuestionSnapshotValidatorTest : DescribeSpec({
    describe("QuestionSnapshotValidator 클래스 테스트") {
        context("validateTitle 메서드") {
            it("유효한 제목은 예외가 발생하지 않아야 한다") {
                // given
                val title = "유효한 제목"

                // when & then
                shouldNotThrowAny {
                    QuestionSnapshotValidator.validateTitle(title)
                }
            }

            it("빈 제목은 예외가 발생해야 한다") {
                // given
                val title = ""

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateTitle(title)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_TITLE.status
            }

            it("공백만 있는 제목은 예외가 발생해야 한다") {
                // given
                val title = "   "

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateTitle(title)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_TITLE.status
            }

            it("500자를 초과하는 제목은 예외가 발생해야 한다") {
                // given
                val title = "a".repeat(501)

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateTitle(title)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_TITLE.status
            }
        }

        context("validateDescription 메서드") {
            it("유효한 설명은 예외가 발생하지 않아야 한다") {
                // given
                val description = "유효한 설명"

                // when & then
                shouldNotThrowAny {
                    QuestionSnapshotValidator.validateDescription(description)
                }
            }

            it("빈 설명은 예외가 발생해야 한다") {
                // given
                val description = ""

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateDescription(description)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_DESCRIPTION.status
            }

            it("공백만 있는 설명은 예외가 발생해야 한다") {
                // given
                val description = "   "

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateDescription(description)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_DESCRIPTION.status
            }

            it("1000자를 초과하는 설명은 예외가 발생해야 한다") {
                // given
                val description = "a".repeat(1001)

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateDescription(description)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_DESCRIPTION.status
            }
        }

        context("validateDisplayOrder 메서드") {
            it("0 이상의 순서는 예외가 발생하지 않아야 한다") {
                // given
                val displayOrder = 0

                // when & then
                shouldNotThrowAny {
                    QuestionSnapshotValidator.validateDisplayOrder(displayOrder)
                }
            }

            it("양수 순서는 예외가 발생하지 않아야 한다") {
                // given
                val displayOrder = 1

                // when & then
                shouldNotThrowAny {
                    QuestionSnapshotValidator.validateDisplayOrder(displayOrder)
                }
            }

            it("음수 순서는 예외가 발생해야 한다") {
                // given
                val displayOrder = -1

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateDisplayOrder(displayOrder)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_ORDER.status
            }
        }

        context("validateVersion 메서드") {
            it("0 이상의 버전은 예외가 발생하지 않아야 한다") {
                // given
                val version = 0L

                // when & then
                shouldNotThrowAny {
                    QuestionSnapshotValidator.validateVersion(version)
                }
            }

            it("양수 버전은 예외가 발생하지 않아야 한다") {
                // given
                val version = 1L

                // when & then
                shouldNotThrowAny {
                    QuestionSnapshotValidator.validateVersion(version)
                }
            }

            it("음수 버전은 예외가 발생해야 한다") {
                // given
                val version = -1L

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshotValidator.validateVersion(version)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_SNAPSHOT_VERSION.status
            }
        }
    }
})
