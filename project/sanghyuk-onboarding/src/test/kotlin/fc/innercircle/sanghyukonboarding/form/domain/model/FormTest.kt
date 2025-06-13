package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class FormTest : DescribeSpec({
    describe("Form 클래스 테스트") {
        context("Form 생성") {
            it("유효한 파라미터로 Form을 생성할 수 있어야 한다") {
                // given
                val title = "유효한 설문 제목"
                val description = "유효한 설문 설명"
                val createdBy = "test-user"

                // when & then
                shouldNotThrowAny {
                    Form(title, description, createdBy)
                }
            }

            it("유효한 파라미터로 Form을 생성하면 필드가 올바르게 설정되어야 한다") {
                // given
                val title = "유효한 설문 제목"
                val description = "유효한 설문 설명"
                val createdBy = "test-user"

                // when
                val form = Form(title, description, createdBy)

                // then
                form.title shouldBe title
                form.description shouldBe description
                form.createdBy shouldBe createdBy
            }

            it("description 파라미터를 생략하면 빈 문자열로 설정되어야 한다") {
                // given
                val title = "유효한 설문 제목"
                val createdBy = "test-user"

                // when
                val form = Form(title, createdBy = createdBy)

                // then
                form.title shouldBe title
                form.description shouldBe ""
                form.createdBy shouldBe createdBy
            }

            it("빈 title로 Form을 생성하면 예외가 발생해야 한다") {
                // given
                val title = ""
                val description = "유효한 설문 설명"
                val createdBy = "test-user"

                // when & then
                val exception = shouldThrow<CustomException> {
                    Form(title, description, createdBy)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_TITLE.status
            }

            it("공백만 있는 title로 Form을 생성하면 예외가 발생해야 한다") {
                // given
                val title = "   "
                val description = "유효한 설문 설명"
                val createdBy = "test-user"

                // when & then
                val exception = shouldThrow<CustomException> {
                    Form(title, description, createdBy)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_TITLE.status
            }

            it("255자를 초과하는 title로 Form을 생성하면 예외가 발생해야 한다") {
                // given
                val title = "a".repeat(256)
                val description = "유효한 설문 설명"
                val createdBy = "test-user"

                // when & then
                val exception = shouldThrow<CustomException> {
                    Form(title, description, createdBy)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_TITLE.status
            }

            it("1000자를 초과하는 description으로 Form을 생성하면 예외가 발생해야 한다") {
                // given
                val title = "유효한 설문 제목"
                val description = "a".repeat(1001)
                val createdBy = "test-user"

                // when & then
                val exception = shouldThrow<CustomException> {
                    Form(title, description, createdBy)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_FORM_DESCRIPTION.status
            }
        }
    }
})
