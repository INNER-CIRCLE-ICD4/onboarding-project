package kr.innercircle.onboarding.survey.unit.fake.repository

import kr.innercircle.onboarding.survey.domain.SurveyItemOption
import kr.innercircle.onboarding.survey.repository.SurveyItemOptionRepository

/**
 * packageName : kr.innercircle.onboarding.survey.unit.fake.repository
 * fileName    : FakeSurveyItemOptionRepository
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */
class FakeSurveyItemOptionRepository: SurveyItemOptionRepository {
    private val storedSurveyItemOptions = mutableListOf<SurveyItemOption>()

    private fun save(surveyItemOption: SurveyItemOption): SurveyItemOption {
        return if(surveyItemOption.id == null) {
            val newId = storedSurveyItemOptions.mapNotNull { it.id }.maxOrNull()?.plus(1) ?: 1

            val newSurveyItemOption = SurveyItemOption(
                surveyItem = surveyItemOption.surveyItem,
                id = newId,
                option = surveyItemOption.option,
                orderNumber = surveyItemOption.orderNumber
            )
            storedSurveyItemOptions.add(newSurveyItemOption)
            newSurveyItemOption
        } else {
            storedSurveyItemOptions.removeIf { it.id == surveyItemOption.id }
            storedSurveyItemOptions.add(surveyItemOption)
            surveyItemOption
        }
    }

    override fun findById(id: Long): SurveyItemOption? {
        return storedSurveyItemOptions.find { it.id == id }
    }

    override fun findAll(): List<SurveyItemOption> {
        return storedSurveyItemOptions.toList()
    }

    override fun saveAll(surveyItemOptions: List<SurveyItemOption>): List<SurveyItemOption> {
        return surveyItemOptions.map { save(it) }
    }
}