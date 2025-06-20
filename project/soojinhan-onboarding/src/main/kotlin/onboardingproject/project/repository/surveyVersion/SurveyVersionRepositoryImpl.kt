package onboardingproject.project.repository.surveyVersion

import onboardingproject.project.domain.SurveyVersion
import org.springframework.stereotype.Repository

/**
 * packageName : onboardingproject.project.repository.surveyVersion
 * fileName    : SurveyVersionRepositoryImpl
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
@Repository
class SurveyVersionRepositoryImpl(
    private val surveyVersionJpaRepository: SurveyVersionJpaRepository
) : SurveyVersionRepository {
    override fun save(surveyVersion: SurveyVersion): SurveyVersion {
        return surveyVersionJpaRepository.save(surveyVersion)
    }

    override fun deleteAll() {
        surveyVersionJpaRepository.deleteAll()
    }
}