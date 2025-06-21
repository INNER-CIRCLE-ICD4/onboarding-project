package onboardingproject.project.repository.surveyVersion

import onboardingproject.project.domain.Survey
import onboardingproject.project.domain.SurveyVersion
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : SurveyVersionRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface SurveyVersionJpaRepository : JpaRepository<SurveyVersion, String> {
    fun findFirstBySurveyOrderByVersionDesc(survey: Survey): SurveyVersion
    fun findAllBySurveyId(surveyId: String): List<SurveyVersion>
}