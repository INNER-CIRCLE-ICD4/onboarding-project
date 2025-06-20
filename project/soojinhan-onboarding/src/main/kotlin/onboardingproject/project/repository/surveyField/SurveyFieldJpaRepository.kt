package onboardingproject.project.repository.surveyField

import onboardingproject.project.domain.SurveyField
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : SurveyFieldRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface SurveyFieldJpaRepository : JpaRepository<SurveyField, String> {
    fun findAllBySurveyVersionId(surveyVersionId: String): List<SurveyField>
}