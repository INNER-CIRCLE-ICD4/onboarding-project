package kr.innercircle.onboarding.survey.service

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyRequest
import kr.innercircle.onboarding.survey.dto.request.UpdateSurveyRequest
import kr.innercircle.onboarding.survey.dto.response.GetSurveysResponse
import kr.innercircle.onboarding.survey.exception.SurveyNotFoundException
import kr.innercircle.onboarding.survey.repository.SurveyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * packageName : kr.innercircle.onboarding.survey.service
 * fileName    : SurveyService
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
    private val surveyItemService: SurveyItemService,
) {
    @Transactional
    fun createSurvey(createSurveyRequest: CreateSurveyRequest): Survey {
        val survey = surveyRepository.save(
            Survey.from(createSurveyRequest)
        )

        surveyItemService.createSurveyItems(survey, createSurveyRequest.surveyItems)
        return survey
    }

    fun getSurveysResponse(): List<GetSurveysResponse> {
        val surveys = surveyRepository.findAll()
        return surveys.map { survey ->
            val surveyItemsResponses = surveyItemService.getSurveyItemsResponse(survey)
            GetSurveysResponse(survey, surveyItemsResponses)
        }
    }

    @Transactional
    fun updateSurvey(surveyId: Long, updateSurveyRequest: UpdateSurveyRequest) {
        val survey = getSurveyById(surveyId)
        survey.update(updateSurveyRequest)

        surveyItemService.updateSurveyItems(updateSurveyRequest.surveyItems)
    }

    fun getSurveyById(surveyId: Long): Survey {
        return surveyRepository.findById(surveyId) ?: throw SurveyNotFoundException()
    }
}