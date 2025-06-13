package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SelectableOptionTest : DescribeSpec({
    describe("SelectableOption 엔티티 테스트") {
        context("생성자") {
            it("유효한 파라미터로 생성 시 예외가 발생하지 않아야 한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test"
                )

                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test"
                )

                val questionSnapshot = QuestionSnapshot(
                    title = "테스트 질문",
                    description = "테스트 질문 설명",
                    type = InputType.RADIO,
                    required = true,
                    displayOrder = 1,
                    version = 1L,
                    createdBy = "test",
                    questionTemplate = questionTemplate
                )

                // when & then
                shouldNotThrowAny {
                    SelectableOption(
                        value = "옵션 1",
                        displayOrder = 1,
                        questionSnapshot = questionSnapshot,
                        createdBy = "test"
                    )
                }
            }

            it("빈 옵션 텍스트로 생성 시 예외가 발생해야 한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test"
                )

                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test"
                )

                val questionSnapshot = QuestionSnapshot(
                    title = "테스트 질문",
                    description = "테스트 질문 설명",
                    type = InputType.RADIO,
                    required = true,
                    displayOrder = 1,
                    version = 1L,
                    createdBy = "test",
                    questionTemplate = questionTemplate
                )

                // when & then
                val exception = shouldThrow<CustomException> {
                    SelectableOption(
                        value = "",
                        displayOrder = 1,
                        questionSnapshot = questionSnapshot,
                        createdBy = "test"
                    )
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.status
            }

            it("50자를 초과하는 옵션 텍스트로 생성 시 예외가 발생해야 한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test"
                )

                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test"
                )

                val questionSnapshot = QuestionSnapshot(
                    title = "테스트 질문",
                    description = "테스트 질문 설명",
                    type = InputType.RADIO,
                    required = true,
                    displayOrder = 1,
                    version = 1L,
                    createdBy = "test",
                    questionTemplate = questionTemplate
                )

                // when & then
                val exception = shouldThrow<CustomException> {
                    SelectableOption(
                        value = "a".repeat(51),
                        displayOrder = 1,
                        questionSnapshot = questionSnapshot,
                        createdBy = "test"
                    )
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.status
            }

            it("음수 순서로 생성 시 예외가 발생해야 한다") {
                // given
                val form = Form(
                    title = "테스트 폼",
                    description = "테스트 폼 설명",
                    createdBy = "test"
                )

                val questionTemplate = QuestionTemplate(
                    form = form,
                    version = 1L,
                    createdBy = "test"
                )

                val questionSnapshot = QuestionSnapshot(
                    title = "테스트 질문",
                    description = "테스트 질문 설명",
                    type = InputType.RADIO,
                    required = true,
                    displayOrder = 1,
                    version = 1L,
                    createdBy = "test",
                    questionTemplate = questionTemplate
                )

                // when & then
                val exception = shouldThrow<CustomException> {
                    SelectableOption(
                        value = "옵션 1",
                        displayOrder = -1,
                        questionSnapshot = questionSnapshot,
                        createdBy = "test"
                    )
                }
                exception.formattedErrorCode.status shouldBe ErrorCode.INVALID_ITEM_OPTION_DISPLAY_ORDER.status
            }
        }
    }
})
