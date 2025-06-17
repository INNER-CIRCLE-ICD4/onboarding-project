package onboardingproject.project.repository

import onboardingproject.project.domain.Survey
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : SurveyRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface SurveyRepository : JpaRepository<Survey, String>