package kr.innercircle.onboarding.survey.unit

import kr.innercircle.onboarding.survey.service.SurveyItemOptionService
import kr.innercircle.onboarding.survey.service.SurveyItemService
import kr.innercircle.onboarding.survey.service.SurveyService
import kr.innercircle.onboarding.survey.unit.fake.repository.FakeSurveyItemOptionRepository
import kr.innercircle.onboarding.survey.unit.fake.repository.FakeSurveyItemRepository
import kr.innercircle.onboarding.survey.unit.fake.repository.FakeSurveyRepository

/**
 * packageName : kr.innercircle.onboarding.survey.unit.fake
 * fileName    : TestDiContainer
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */


class TestDiContainer {
    val surveyRepository = FakeSurveyRepository()
    val surveyItemRepository = FakeSurveyItemRepository()
    val surveyItemOptionRepository = FakeSurveyItemOptionRepository()

    val surveyItemOptionService = SurveyItemOptionService(surveyItemOptionRepository)
    val surveyItemService = SurveyItemService(surveyItemRepository, surveyItemOptionService)
    val surveyService = SurveyService(surveyRepository, surveyItemService)

}