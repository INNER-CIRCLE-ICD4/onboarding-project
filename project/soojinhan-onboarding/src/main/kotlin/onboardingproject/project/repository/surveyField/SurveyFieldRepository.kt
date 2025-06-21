package onboardingproject.project.repository.surveyField

import onboardingproject.project.domain.SurveyField

/**
 * packageName : onboardingproject.project.repository.surveyField
 * fileName    : SurveyFieldRepository
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */

interface SurveyFieldRepository {
    fun save(surveyField: SurveyField): SurveyField
    fun deleteAll()
    fun findByIdOrNull(fieldId: String): SurveyField?
    fun findAllBySurveyVersionId(versionId: String): List<SurveyField>
}