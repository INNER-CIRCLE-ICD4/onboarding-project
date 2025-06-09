package fc.innercircle.sanghyukonboarding.survey.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.survey.domain.model.vo.InputType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ItemOptionsTest :
    DescribeSpec({
        describe("ItemOptions 클래스 테스트") {
            context("ItemOptions 생성") {
                it("유효한 파라미터로 ItemOptions를 생성할 수 있어야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자",
                        )
                    val surveyItem =
                        SurveyItem(
                            question = "테스트 질문",
                            description = "테스트 설명",
                            type = InputType.RADIO,
                            required = true,
                            displayOrder = 1,
                            survey = survey,
                            createdBy = "테스트 사용자",
                        )
                    val optionText = "옵션 텍스트"
                    val displayOrder = 1

                    // when
                    val itemOptions =
                        ItemOptions(
                            optionText = optionText,
                            displayOrder = displayOrder,
                            surveyItem = surveyItem,
                        )

                    // then
                    itemOptions.optionText shouldBe optionText
                    itemOptions.displayOrder shouldBe displayOrder
                    itemOptions.surveyItem shouldBe surveyItem
                    itemOptions.id shouldBe 0L
                }

                it("빈 옵션 텍스트로 ItemOptions를 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자",
                        )
                    val surveyItem =
                        SurveyItem(
                            question = "테스트 질문",
                            description = "테스트 설명",
                            type = InputType.RADIO,
                            required = true,
                            displayOrder = 1,
                            survey = survey,
                            createdBy = "테스트 사용자",
                        )
                    val emptyOptionText = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            ItemOptions(
                                optionText = emptyOptionText,
                                displayOrder = 1,
                                surveyItem = surveyItem,
                            )
                        }

                    // then
                    exception.message shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.withArgs(emptyOptionText).message
                }

                it("50자를 초과하는 옵션 텍스트로 ItemOptions를 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자",
                        )
                    val surveyItem =
                        SurveyItem(
                            question = "테스트 질문",
                            description = "테스트 설명",
                            type = InputType.RADIO,
                            required = true,
                            displayOrder = 1,
                            survey = survey,
                            createdBy = "테스트 사용자",
                        )
                    val longOptionText = "a".repeat(51)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            ItemOptions(
                                optionText = longOptionText,
                                displayOrder = 1,
                                surveyItem = surveyItem,
                            )
                        }

                    // then
                    exception.message shouldBe ErrorCode.INVALID_ITEM_OPTION_TEXT.withArgs(longOptionText).message
                }

                it("음수 표시 순서로 ItemOptions를 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자",
                        )
                    val surveyItem =
                        SurveyItem(
                            question = "테스트 질문",
                            description = "테스트 설명",
                            type = InputType.RADIO,
                            required = true,
                            displayOrder = 1,
                            survey = survey,
                            createdBy = "테스트 사용자",
                        )
                    val negativeDisplayOrder = -1

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            ItemOptions(
                                optionText = "옵션 텍스트",
                                displayOrder = negativeDisplayOrder,
                                surveyItem = surveyItem,
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_ITEM_OPTION_DISPLAY_ORDER.withArgs(negativeDisplayOrder.toString()).message
                }
            }
        }
    })
