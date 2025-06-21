package onboardingproject.project.repository.survey

import onboardingproject.project.domain.Survey
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

/**
 * packageName : onboardingproject.project.repository.survey
 * fileName    : SurveyRepositoryImpl
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
@Repository
class SurveyRepositoryImpl(
    private val surveyJpaRepository: SurveyJpaRepository
) : SurveyRepository {
    override fun save(survey: Survey): Survey {
        return surveyJpaRepository.save(survey)
    }

    override fun deleteAll() {
        surveyJpaRepository.deleteAll()
    }

    override fun findByIdOrNull(id: String): Survey? {
        return surveyJpaRepository.findByIdOrNull(id)
    }
}