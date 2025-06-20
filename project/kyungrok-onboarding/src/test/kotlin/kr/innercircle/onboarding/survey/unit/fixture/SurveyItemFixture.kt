package kr.innercircle.onboarding.survey.unit.fixture

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest

/**
 * packageName : kr.innercircle.onboarding.survey.unit.fixture
 * fileName    : SurveyItemFixture
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */
object SurveyItemFixture {
    fun 설문조사_항목_생성(
        survey: Survey,
        name: String = "항목1",
        description: String? = "항목에 대한 설명",
        surveyItemInputType: SurveyItemInputType = SurveyItemInputType.SHORT_TEXT,
        isRequired: Boolean = true,
        orderNumber: Int = 1,
        isDeleted: Boolean = false
    ): SurveyItem {
        return SurveyItem(
            survey = survey,
            name = name,
            description = description,
            inputType = surveyItemInputType,
            isRequired = isRequired,
            orderNumber = orderNumber,
            isDeleted = isDeleted
        )
    }
    
    fun 설문조사_항목_생성_요청(
        name: String = "항목1",
        description: String? = "항목에 대한 설명",
        inputType: SurveyItemInputType = SurveyItemInputType.SHORT_TEXT,
        options: List<CreateSurveyItemOptionRequest> = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("옵션2")
        ),
        isRequired: Boolean = true,
    ): CreateSurveyItemRequest {
        return CreateSurveyItemRequest(
            name = name,
            description = description,
            inputType = inputType,
            options = options,
            isRequired = isRequired
        )
    }
}