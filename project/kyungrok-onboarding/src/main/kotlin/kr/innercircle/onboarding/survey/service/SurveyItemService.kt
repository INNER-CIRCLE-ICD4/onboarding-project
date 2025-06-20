package kr.innercircle.onboarding.survey.service

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest
import kr.innercircle.onboarding.survey.dto.request.UpdateSurveyItemRequest
import kr.innercircle.onboarding.survey.dto.response.GetSurveyItemsResponse
import kr.innercircle.onboarding.survey.exception.SurveyItemNotFoundException
import kr.innercircle.onboarding.survey.repository.SurveyItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * packageName : kr.innercircle.onboarding.survey.service
 * fileName    : SurveyItemService
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */

@Service
class SurveyItemService(
    private val surveyItemRepository: SurveyItemRepository,
    private val surveyItemOptionService: SurveyItemOptionService
) {
    @Transactional
    fun createSurveyItems(
        survey: Survey,
        createSurveyItemRequests: List<CreateSurveyItemRequest>
    ): List<SurveyItem> {
        var orderNumber = 1
        return createSurveyItemRequests.map { createSurveyItemRequest ->
            val surveyItem = surveyItemRepository.save(
                SurveyItem.from(survey, createSurveyItemRequest, orderNumber++)
            )

            surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemRequest.options)
            surveyItem
        }
    }

    fun getSurveyItemsResponse(survey: Survey): List<GetSurveyItemsResponse> {
        val surveyItems = surveyItemRepository.findAllBySurveyAndIsDeleted(survey, false)
        return surveyItems.map { surveyItem ->
            val surveyItemOptionsResponses = surveyItemOptionService.getSurveyItemOptionsResponse(surveyItem)
            GetSurveyItemsResponse(surveyItem, surveyItemOptionsResponses)
        }
    }

    @Transactional
    fun updateSurveyItems(updateSurveyItemRequests: List<UpdateSurveyItemRequest>) {
        var orderNumber = 1
        updateSurveyItemRequests.forEach { updateSurveyItemRequest ->
            val surveyItem = getSurveyItemById(updateSurveyItemRequest.surveyItemId)
            surveyItem.hasDeleted()

            val newSurveyItem = surveyItemRepository.save(
                SurveyItem.from(surveyItem.survey, updateSurveyItemRequest, orderNumber++)
            )
            surveyItemOptionService.createSurveyItemOptions(newSurveyItem, updateSurveyItemRequest.options)
        }
    }

    fun getSurveyItemById(surveyItemId: Long): SurveyItem {
        return surveyItemRepository.findById(surveyItemId) ?: throw SurveyItemNotFoundException()
    }
}