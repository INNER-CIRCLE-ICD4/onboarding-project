package fc.innercircle.sanghyukonboarding.survey.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.survey.domain.model.vo.InputType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SurveyItemTest :
    DescribeSpec({
        describe("SurveyItem 클래스 테스트") {
            context("SurveyItem 생성") {
                it("유효한 파라미터로 SurveyItem을 생성할 수 있어야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자"
                        )
                    val question = "테스트 질문"
                    val description = "테스트 설명"
                    val type = InputType.TEXT
                    val required = true
                    val displayOrder = 1
                    val createdBy = "테스트 사용자"

                    // when
                    val surveyItem =
                        SurveyItem(
                            question = question,
                            description = description,
                            type = type,
                            required = required,
                            displayOrder = displayOrder,
                            survey = survey,
                            createdBy = createdBy
                        )

                    // then
                    surveyItem.question shouldBe question
                    surveyItem.description shouldBe description
                    surveyItem.type shouldBe type.name
                    surveyItem.required shouldBe required
                    surveyItem.displayOrder shouldBe displayOrder
                    surveyItem.survey shouldBe survey
                }

                it("빈 질문으로 SurveyItem을 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자"
                        )
                    val emptyQuestion = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItem(
                                question = emptyQuestion,
                                description = "테스트 설명",
                                type = InputType.TEXT,
                                required = true,
                                displayOrder = 1,
                                survey = survey,
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe ErrorCode.INVALID_SURVEY_ITEM_QUESTION.withArgs(emptyQuestion).message
                }

                it("500자를 초과하는 질문으로 SurveyItem을 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자"
                        )
                    val longQuestion = "a".repeat(501)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItem(
                                question = longQuestion,
                                description = "테스트 설명",
                                type = InputType.TEXT,
                                required = true,
                                displayOrder = 1,
                                survey = survey,
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe ErrorCode.INVALID_SURVEY_ITEM_QUESTION.withArgs(longQuestion).message
                }

                it("빈 설명으로 SurveyItem을 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자"
                        )
                    val emptyDescription = ""

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItem(
                                question = "테스트 질문",
                                description = emptyDescription,
                                type = InputType.TEXT,
                                required = true,
                                displayOrder = 1,
                                survey = survey,
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_DESCRIPTION.withArgs(emptyDescription).message
                }

                it("1000자를 초과하는 설명으로 SurveyItem을 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자"
                        )
                    val longDescription = "a".repeat(1001)

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItem(
                                question = "테스트 질문",
                                description = longDescription,
                                type = InputType.TEXT,
                                required = true,
                                displayOrder = 1,
                                survey = survey,
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_DESCRIPTION.withArgs(longDescription).message
                }

                it("음수 표시 순서로 SurveyItem을 생성하면 예외가 발생해야 한다") {
                    // given
                    val survey =
                        Survey(
                            title = "테스트 설문",
                            description = "테스트 설문 설명",
                            createdBy = "테스트 사용자"
                        )
                    val negativeDisplayOrder = -1

                    // when
                    val exception =
                        shouldThrow<CustomException> {
                            SurveyItem(
                                question = "테스트 질문",
                                description = "테스트 설명",
                                type = InputType.TEXT,
                                required = true,
                                displayOrder = negativeDisplayOrder,
                                survey = survey,
                                createdBy = "테스트 사용자"
                            )
                        }

                    // then
                    exception.message shouldBe
                        ErrorCode.INVALID_SURVEY_ITEM_ORDER.withArgs(negativeDisplayOrder.toString()).message
                }
            }
        }
    })
