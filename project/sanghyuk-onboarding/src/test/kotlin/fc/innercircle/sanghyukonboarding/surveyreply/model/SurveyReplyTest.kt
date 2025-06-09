package fc.innercircle.sanghyukonboarding.surveyreply.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class SurveyReplyTest : DescribeSpec({
    describe("SurveyReply 클래스 테스트") {
        context("SurveyReply 생성") {
            it("유효한 파라미터로 SurveyReply를 생성할 수 있어야 한다") {
                // given
                val surveyId = 1L
                val responseDate = LocalDateTime.now()
                val createdBy = "테스트 사용자"

                // when
                val surveyReply = SurveyReply(
                    surveyId = surveyId,
                    responseDate = responseDate,
                    createdBy = createdBy
                )

                // then
                surveyReply.surveyId shouldBe surveyId
                surveyReply.responseDate shouldBe responseDate
                surveyReply.id shouldBe 0L
            }

            it("responseDate가 null인 SurveyReply를 생성할 수 있어야 한다") {
                // given
                val surveyId = 1L
                val responseDate = null
                val createdBy = "테스트 사용자"

                // when
                val surveyReply = SurveyReply(
                    surveyId = surveyId,
                    responseDate = responseDate,
                    createdBy = createdBy
                )

                // then
                surveyReply.surveyId shouldBe surveyId
                surveyReply.responseDate shouldBe responseDate
                surveyReply.id shouldBe 0L
            }
        }
    }
})
