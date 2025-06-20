package kr.innercircle.onboarding.survey.service

import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.domain.SurveyItemOption
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest
import kr.innercircle.onboarding.survey.exception.InsufficientSurveyItemOptionsException
import kr.innercircle.onboarding.survey.repository.SurveyItemOptionRepository
import org.springframework.stereotype.Service

/**
 * packageName : kr.innercircle.onboarding.survey.service
 * fileName    : SurveyItemOptionService
 * author      : ckr
 * date        : 25. 6. 17.
 * description :
 */

@Service
class SurveyItemOptionService(
    private val surveyItemOptionRepository: SurveyItemOptionRepository
) {
    fun createSurveyItemOptions(
        surveyItem: SurveyItem,
        createSurveyItemOptionRequests: List<CreateSurveyItemOptionRequest>
    ): List<SurveyItemOption> {
        if(
            (surveyItem.inputType == SurveyItemInputType.SINGLE_CHOICE || surveyItem.inputType == SurveyItemInputType.MULTIPLE_CHOICE)
                && createSurveyItemOptionRequests.size < 2
        ) {
            throw InsufficientSurveyItemOptionsException()
        }

        var orderNumber = 1
        val surveyItemOptions = createSurveyItemOptionRequests.map {
            SurveyItemOption.of(surveyItem, it, orderNumber++)
        }
        return surveyItemOptionRepository.saveAll(surveyItemOptions)
    }
}