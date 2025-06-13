package fc.innercircle.sanghyukonboarding.formreply.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class AnswerTest : DescribeSpec({
    describe("Answer 클래스 테스트") {
        context("Answer 생성") {
            it("유효한 파라미터로 Answer를 생성할 수 있어야 한다") {
                // given
                val formReply = FormReply("form-id-123", LocalDateTime.now(), "test-user")
                val questionSnapshotId = "question-snapshot-id-123"
                val answer = "This is a test answer"
                val createdBy = "test-user"

                // when
                val answerEntity = Answer(formReply, questionSnapshotId, answer, createdBy = createdBy)

                // then
                answerEntity.formReply shouldBe formReply
                answerEntity.questionSnapshotId shouldBe questionSnapshotId
                answerEntity.answer shouldBe answer
                answerEntity.selectableOptionId shouldBe ""
                answerEntity.createdBy shouldBe createdBy
            }

            it("주관식 답변 없이 Answer를 생성하면 빈 문자열로 설정되어야 한다") {
                // given
                val formReply = FormReply("form-id-123", LocalDateTime.now(), "test-user")
                val questionSnapshotId = "question-snapshot-id-123"
                val createdBy = "test-user"

                // when
                val answerEntity = Answer(formReply, questionSnapshotId, createdBy = createdBy)

                // then
                answerEntity.formReply shouldBe formReply
                answerEntity.questionSnapshotId shouldBe questionSnapshotId
                answerEntity.answer shouldBe ""
                answerEntity.selectableOptionId shouldBe ""
                answerEntity.createdBy shouldBe createdBy
            }

            it("선택형 답변으로 Answer를 생성할 수 있어야 한다") {
                // given
                val formReply = FormReply("form-id-123", LocalDateTime.now(), "test-user")
                val questionSnapshotId = "question-snapshot-id-123"
                val selectableOptionId = "option-id-123"
                val createdBy = "test-user"

                // when
                val answerEntity = Answer(
                    formReply,
                    questionSnapshotId,
                    selectableOptionId = selectableOptionId,
                    createdBy = createdBy
                )

                // then
                answerEntity.formReply shouldBe formReply
                answerEntity.questionSnapshotId shouldBe questionSnapshotId
                answerEntity.answer shouldBe ""
                answerEntity.selectableOptionId shouldBe selectableOptionId
                answerEntity.createdBy shouldBe createdBy
            }

            it("주관식과 선택형 답변을 모두 포함하여 Answer를 생성할 수 있어야 한다") {
                // given
                val formReply = FormReply("form-id-123", LocalDateTime.now(), "test-user")
                val questionSnapshotId = "question-snapshot-id-123"
                val answer = "This is a test answer"
                val selectableOptionId = "option-id-123"
                val createdBy = "test-user"

                // when
                val answerEntity = Answer(
                    formReply,
                    questionSnapshotId,
                    answer,
                    selectableOptionId,
                    createdBy
                )

                // then
                answerEntity.formReply shouldBe formReply
                answerEntity.questionSnapshotId shouldBe questionSnapshotId
                answerEntity.answer shouldBe answer
                answerEntity.selectableOptionId shouldBe selectableOptionId
                answerEntity.createdBy shouldBe createdBy
            }
        }
    }
})
