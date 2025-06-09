package fc.innercircle.sanghyukonboarding.survey.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SurveyTest : DescribeSpec({
    describe("Survey 클래스 테스트") {
        context("Survey 생성") {
            it("유효한 파라미터로 Survey를 생성할 수 있어야 한다") {
                // given
                val title = "테스트 설문"
                val description = "테스트 설문 설명"
                val createdBy = "테스트 사용자"

                // when
                val survey = Survey(
                    title = title,
                    description = description,
                    createdBy = createdBy
                )

                // then
                survey.title shouldBe title
                survey.description shouldBe description
                survey.id shouldBe 0L
            }

            it("빈 제목으로 Survey를 생성하면 예외가 발생해야 한다") {
                // given
                val emptyTitle = ""

                // when
                val exception = shouldThrow<CustomException> {
                    Survey(
                        title = emptyTitle,
                        description = "테스트 설문 설명",
                        createdBy = "테스트 사용자"
                    )
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_TITLE.withArgs(emptyTitle).message
            }

            it("255자를 초과하는 제목으로 Survey를 생성하면 예외가 발생해야 한다") {
                // given
                val longTitle = "a".repeat(256)

                // when
                val exception = shouldThrow<CustomException> {
                    Survey(
                        title = longTitle,
                        description = "테스트 설문 설명",
                        createdBy = "테스트 사용자"
                    )
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_TITLE.withArgs(longTitle).message
            }

            it("빈 설명으로 Survey를 생성하면 예외가 발생해야 한다") {
                // given
                val emptyDescription = ""

                // when
                val exception = shouldThrow<CustomException> {
                    Survey(
                        title = "테스트 설문",
                        description = emptyDescription,
                        createdBy = "테스트 사용자"
                    )
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_DESCRIPTION.withArgs(emptyDescription).message
            }

            it("1000자를 초과하는 설명으로 Survey를 생성하면 예외가 발생해야 한다") {
                // given
                val longDescription = "a".repeat(1001)

                // when
                val exception = shouldThrow<CustomException> {
                    Survey(
                        title = "테스트 설문",
                        description = longDescription,
                        createdBy = "테스트 사용자"
                    )
                }

                // then
                exception.message shouldBe ErrorCode.INVALID_SURVEY_DESCRIPTION.withArgs(longDescription).message
            }
        }
    }
})
