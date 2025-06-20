package kr.innercircle.onboarding.survey.unit.fake.repository

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.repository.SurveyRepository

/**
 * packageName : kr.innercircle.onboarding.survey.unit.fake.repository
 * fileName    : FakeSurveyRepository
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */
class FakeSurveyRepository: SurveyRepository {
    private val storedSurveys = mutableListOf<Survey>()

    override fun save(survey: Survey): Survey {
        return if(survey.id == null) {
            val newId = storedSurveys.mapNotNull { it.id }.maxOrNull()?.plus(1) ?: 1

            val newSurvey = Survey(
                id = newId,
                name = survey.name,
                description = survey.description,
            )
            storedSurveys.add(newSurvey)
            newSurvey
        } else {
            storedSurveys.removeIf { it.id == survey.id }
            storedSurveys.add(survey)
            survey
        }
    }

    override fun findById(id: Long): Survey? {
        return storedSurveys.find { it.id == id }
    }

    override fun findAll(): List<Survey> {
        return storedSurveys.toList()
    }
}