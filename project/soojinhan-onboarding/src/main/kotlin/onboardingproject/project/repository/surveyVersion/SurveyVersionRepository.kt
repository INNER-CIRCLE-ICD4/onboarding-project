package onboardingproject.project.repository.surveyVersion

import onboardingproject.project.domain.Survey
import onboardingproject.project.domain.SurveyVersion

/**
 * packageName : onboardingproject.project.repository.surveyVersion
 * fileName    : SurveyVersionRepository
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
interface SurveyVersionRepository {
    fun save(surveyVersion: SurveyVersion): SurveyVersion
    fun deleteAll()
    fun findFirstBySurveyOrderByVersionDesc(survey: Survey): SurveyVersion
    fun findAllBySurveyId(surveyId: String): List<SurveyVersion>
}