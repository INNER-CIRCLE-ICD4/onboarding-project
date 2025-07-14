package kr.innercircle.onboarding.survey.unit.fixture

import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.domain.SurveyItemOption
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest

/**
 * packageName : kr.innercircle.onboarding.survey.unit.fixture
 * fileName    : SurveyItemOptionFixture
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */

object SurveyItemOptionFixture {

    fun 설문조사_항목_옵션_생성(
        surveyItem: SurveyItem,
        option: String = "옵션1",
        orderNumber: Int = 1
    ): SurveyItemOption {
        return SurveyItemOption(
            surveyItem = surveyItem,
            option = option,
            orderNumber = orderNumber,
        )
    }

    fun 설문조사_항목_옵션_생성_요청(
        option: String = "옵션1",
    ): CreateSurveyItemOptionRequest {
        return CreateSurveyItemOptionRequest(option)
    }
}