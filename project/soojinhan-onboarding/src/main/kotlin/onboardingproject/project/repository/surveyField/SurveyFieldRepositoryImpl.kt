package onboardingproject.project.repository.surveyField

import onboardingproject.project.domain.SurveyField
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

/**
 * packageName : onboardingproject.project.repository.surveyField
 * fileName    : SurveyFieldRepositoryImpl
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
@Repository
class SurveyFieldRepositoryImpl(
    private val surveyFieldJpaRepository: SurveyFieldJpaRepository
) : SurveyFieldRepository {
    override fun save(surveyField: SurveyField): SurveyField {
        return surveyFieldJpaRepository.save(surveyField)
    }

    override fun deleteAll() {
        surveyFieldJpaRepository.deleteAll()
    }

    override fun findByIdOrNull(fieldId: String): SurveyField? {
        return surveyFieldJpaRepository.findByIdOrNull(fieldId)
    }

    override fun findAllBySurveyVersionId(versionId: String): List<SurveyField> {
        return surveyFieldJpaRepository.findAllBySurveyVersionId(versionId)
    }
}