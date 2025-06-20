package kr.innercircle.onboarding.survey.unit.fake.repository

import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.repository.SurveyItemRepository

/**
 * packageName : kr.innercircle.onboarding.survey.unit.fake.repository
 * fileName    : FakeSurveyItemRepository
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */
class FakeSurveyItemRepository: SurveyItemRepository {
    private val storedSurveyItems = mutableListOf<SurveyItem>()

    override fun save(surveyItem: SurveyItem): SurveyItem {
        return if(surveyItem.id == null) {
            val newId = storedSurveyItems.mapNotNull { it.id }.maxOrNull()?.plus(1) ?: 1

            val newSurveyItem = SurveyItem(
                survey = surveyItem.survey,
                id = newId,
                name = surveyItem.name,
                description = surveyItem.description,
                inputType = surveyItem.inputType,
                isRequired = surveyItem.isRequired,
                orderNumber = surveyItem.orderNumber,
                isDeleted = surveyItem.isDeleted
            )
            storedSurveyItems.add(newSurveyItem)
            newSurveyItem
        } else {
            storedSurveyItems.removeIf { it.id == surveyItem.id }
            storedSurveyItems.add(surveyItem)
            surveyItem
        }
    }

    override fun findById(id: Long): SurveyItem? {
        return storedSurveyItems.find { it.id == id }
    }

    override fun findAll(): List<SurveyItem> {
        return storedSurveyItems.toList()
    }
}