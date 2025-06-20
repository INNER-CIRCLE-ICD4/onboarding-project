package onboardingproject.project.repository.survey

import onboardingproject.project.domain.Survey
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : SurveyJpaRepository
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
interface SurveyJpaRepository: JpaRepository<Survey, String> {
}