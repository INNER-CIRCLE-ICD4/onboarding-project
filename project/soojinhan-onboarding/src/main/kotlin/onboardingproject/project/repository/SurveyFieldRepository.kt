package onboardingproject.project.repository

import onboardingproject.project.domain.SurveyField
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : SurveyFieldRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface SurveyFieldRepository : JpaRepository<SurveyField, String>