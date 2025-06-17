package onboardingproject.project.repository

import onboardingproject.project.domain.SurveyVersion
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : SurveyVersionRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface SurveyVersionRepository : JpaRepository<SurveyVersion, String>