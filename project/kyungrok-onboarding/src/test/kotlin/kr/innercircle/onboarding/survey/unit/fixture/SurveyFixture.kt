package kr.innercircle.onboarding.survey.unit.fixture

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyRequest

/**
 * packageName : kr.innercircle.onboarding.survey.unit.fixture
 * fileName    : SurveyFixture
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */
object SurveyFixture {
    fun 설문조사_생성(
        name: String = "설문조사1",
        description: String? = "설문조사에 대한 설명"
    ): Survey {
        return Survey(
            name = name,
            description = description,
        )
    }
    
    fun 설문조사_생성_요청(
        name: String = "설문조사1",
        description: String? = "설문조사에 대한 설명",
        surveyItems: List<CreateSurveyItemRequest> = listOf(SurveyItemFixture.설문조사_항목_생성_요청())
    ): CreateSurveyRequest {
        return CreateSurveyRequest(
            name = name,
            description = description,
            surveyItems = surveyItems
        )
    }
}