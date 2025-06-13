package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class QuestionSnapshotTest : DescribeSpec({
    describe("QuestionSnapshot 클래스 테스트") {
        context("QuestionSnapshot 생성") {
            it("유효한 파라미터로 QuestionSnapshot을 생성할 수 있다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test-user"
                )
                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test-user"
                )

                // when & then
                val questionSnapshot = QuestionSnapshot(
                    title = "테스트 질문",
                    description = "테스트 질문 설명",
                    type = InputType.TEXT,
                    required = true,
                    displayOrder = 1,
                    version = 1L,
                    createdBy = "test-user",
                    questionTemplate = questionTemplate
                )

                // then
                questionSnapshot.title shouldBe "테스트 질문"
                questionSnapshot.description shouldBe "테스트 질문 설명"
                questionSnapshot.type shouldBe InputType.TEXT.name
                questionSnapshot.required shouldBe true
                questionSnapshot.displayOrder shouldBe 1
                questionSnapshot.version shouldBe 1L
                questionSnapshot.questionTemplate shouldBe questionTemplate
            }

            it("빈 제목으로 QuestionSnapshot을 생성하면 예외가 발생한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test-user"
                )
                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test-user"
                )

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshot(
                        title = "",
                        description = "테스트 질문 설명",
                        type = InputType.TEXT,
                        required = true,
                        displayOrder = 1,
                        version = 1L,
                        createdBy = "test-user",
                        questionTemplate = questionTemplate
                    )
                }

                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_TITLE.status
            }

            it("빈 설명으로 QuestionSnapshot을 생성하면 예외가 발생한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test-user"
                )
                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test-user"
                )

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshot(
                        title = "테스트 질문",
                        description = "",
                        type = InputType.TEXT,
                        required = true,
                        displayOrder = 1,
                        version = 1L,
                        createdBy = "test-user",
                        questionTemplate = questionTemplate
                    )
                }

                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_DESCRIPTION.status
            }

            it("음수 displayOrder로 QuestionSnapshot을 생성하면 예외가 발생한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test-user"
                )
                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test-user"
                )

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshot(
                        title = "테스트 질문",
                        description = "테스트 질문 설명",
                        type = InputType.TEXT,
                        required = true,
                        displayOrder = -1,
                        version = 1L,
                        createdBy = "test-user",
                        questionTemplate = questionTemplate
                    )
                }

                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_ORDER.status
            }

            it("음수 version으로 QuestionSnapshot을 생성하면 예외가 발생한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test-user"
                )
                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test-user"
                )

                // when & then
                val exception = shouldThrow<CustomException> {
                    QuestionSnapshot(
                        title = "테스트 질문",
                        description = "테스트 질문 설명",
                        type = InputType.TEXT,
                        required = true,
                        displayOrder = 1,
                        version = -1L,
                        createdBy = "test-user",
                        questionTemplate = questionTemplate
                    )
                }

                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_QUESTION_SNAPSHOT_VERSION.status
            }
        }

        context("다양한 InputType으로 QuestionSnapshot 생성") {
            it("모든 InputType으로 QuestionSnapshot을 생성할 수 있다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test-user"
                )
                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test-user"
                )

                // when & then
                InputType.entries.forEach { inputType ->
                    val questionSnapshot = QuestionSnapshot(
                        title = "테스트 질문",
                        description = "테스트 질문 설명",
                        type = inputType,
                        required = true,
                        displayOrder = 1,
                        version = 1L,
                        createdBy = "test-user",
                        questionTemplate = questionTemplate
                    )

                    questionSnapshot.type shouldBe inputType.name
                }
            }
        }
    }
})
