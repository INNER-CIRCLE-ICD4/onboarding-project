package fc.innercircle.sanghyukonboarding.formreply.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class FormReplyTest : DescribeSpec({
    describe("FormReply 클래스 테스트") {
        context("FormReply 생성") {
            it("formId와 createdBy로 FormReply를 생성할 수 있어야 한다") {
                // given
                val formId = "form-id-123"
                val createdBy = "test-user"

                // when
                val formReply = FormReply(formId, createdBy = createdBy)

                // then
                formReply.formId shouldBe formId
                formReply.submittedAt shouldBe null
                formReply.createdBy shouldBe createdBy
            }

            it("formId, submittedAt, createdBy로 FormReply를 생성할 수 있어야 한다") {
                // given
                val formId = "form-id-123"
                val submittedAt = LocalDateTime.now()
                val createdBy = "test-user"

                // when
                val formReply = FormReply(formId, submittedAt, createdBy)

                // then
                formReply.formId shouldBe formId
                formReply.submittedAt shouldBe submittedAt
                formReply.createdBy shouldBe createdBy
            }
        }
    }
})
