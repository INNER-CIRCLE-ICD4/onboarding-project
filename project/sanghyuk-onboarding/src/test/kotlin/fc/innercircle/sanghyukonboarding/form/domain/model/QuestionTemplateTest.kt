package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class QuestionTemplateTest : DescribeSpec({
    describe("QuestionTemplate 클래스 테스트") {
        context("QuestionTemplate 생성") {
            it("유효한 값으로 QuestionTemplate을 생성할 수 있어야 한다") {
                // given
                val form = Form("테스트 설문", "테스트 설문 설명", "testUser")
                val version = 0L
                val createdBy = "testUser"

                // when
                val questionTemplate = QuestionTemplate(form, version, createdBy)

                // then
                questionTemplate.id shouldNotBe null
                questionTemplate.form shouldBe form
                questionTemplate.version shouldBe version
                questionTemplate.createdBy shouldBe createdBy
                questionTemplate.createdAt shouldNotBe null
                questionTemplate.updatedBy shouldBe null
                questionTemplate.updatedAt shouldBe null
            }

            it("기본 버전 값(0L)으로 QuestionTemplate을 생성할 수 있어야 한다") {
                // given
                val form = Form("테스트 설문", "테스트 설문 설명", "testUser")
                val createdBy = "testUser"

                // when
                val questionTemplate = QuestionTemplate(form, createdBy = createdBy)

                // then
                questionTemplate.id shouldNotBe null
                questionTemplate.form shouldBe form
                questionTemplate.version shouldBe 0L
                questionTemplate.createdBy shouldBe createdBy
                questionTemplate.createdAt shouldNotBe null
                questionTemplate.updatedBy shouldBe null
                questionTemplate.updatedAt shouldBe null
            }

            it("음수 버전으로 QuestionTemplate을 생성하면 예외가 발생해야 한다") {
                // given
                val form = Form("테스트 설문", "테스트 설문 설명", "testUser")
                val version = -1L
                val createdBy = "testUser"

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionTemplate(form, version, createdBy)
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_VERSION.status
            }
        }
    }
})
