package onboardingproject.project.repository.fieldOption

import onboardingproject.project.domain.FieldOption
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : FieldOptionJpaRepository
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
interface FieldOptionJpaRepository: JpaRepository<FieldOption, String> {
    fun findByIdIn(optionIds: List<String>): List<FieldOption>
}