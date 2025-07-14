package kr.innercircle.onboarding.survey.service

import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.domain.SurveyResponse
import kr.innercircle.onboarding.survey.domain.SurveyResponseAnswer
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyResponseAnswerRequest
import kr.innercircle.onboarding.survey.exception.InvalidSurveyItemOptionAnswer
import kr.innercircle.onboarding.survey.repository.SurveyResponseAnswerRepository
import org.springframework.stereotype.Service

/**
 * packageName : kr.innercircle.onboarding.survey.service
 * fileName    : SurveyResponseAnswerService
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */

@Service
class SurveyResponseAnswerService(
    private val surveyItemService: SurveyItemService,
    private val surveyItemOptionService: SurveyItemOptionService,
    private val surveyResponseAnswerRepository: SurveyResponseAnswerRepository
) {
    fun createSurveyResponseAnswers(
        surveyResponse: SurveyResponse,
        createSurveyResponseAnswerRequests: List<CreateSurveyResponseAnswerRequest>
    ): List<SurveyResponseAnswer> {
        val surveyResponseAnswers = createSurveyResponseAnswerRequests.map { createSurveyResponseAnswerRequest ->
            val surveyItem = surveyItemService.getSurveyItemById(createSurveyResponseAnswerRequest.surveyItemId)
            if(surveyItem.inputType == SurveyItemInputType.SINGLE_CHOICE || surveyItem.inputType == SurveyItemInputType.MULTIPLE_CHOICE) {
                val surveyItemOptions = surveyItemOptionService.getSurveyItemOptionsBySurveyItem(surveyItem)
                if(!surveyItemOptions.map { it.option }.contains(createSurveyResponseAnswerRequest.answer)) {
                    throw InvalidSurveyItemOptionAnswer()
                }
            }

            SurveyResponseAnswer.from(surveyItem, surveyResponse, createSurveyResponseAnswerRequest)
        }

        return surveyResponseAnswerRepository.saveAll(surveyResponseAnswers)
    }

    fun getSurveyResponseAnswersBySurveyResponse(surveyResponse: SurveyResponse): List<SurveyResponseAnswer> {
        return surveyResponseAnswerRepository.findAllBySurveyResponse(surveyResponse)
    }
}