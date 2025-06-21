package onboardingproject.project.repository.survey

import onboardingproject.project.domain.FieldOption
import onboardingproject.project.domain.Response
import onboardingproject.project.domain.Survey
import onboardingproject.project.domain.SurveyField
import onboardingproject.project.domain.SurveyVersion

/**
 * packageName : onboardingproject.project.repository
 * fileName    : SurveyRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface SurveyRepository {
    fun save(survey: Survey): Survey
    fun deleteAll()
    fun findByIdOrNull(id: String): Survey?
}