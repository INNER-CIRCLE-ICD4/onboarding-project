package onboardingproject.project.repository

import onboardingproject.project.domain.FieldOption
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : FieldOptionRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface FieldOptionRepository : JpaRepository<FieldOption, String>