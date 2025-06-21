package onboardingproject.project.repository.fieldOption

import onboardingproject.project.domain.FieldOption

/**
 * packageName : onboardingproject.project.repository.fieldOption
 * fileName    : FieldOptionRepository
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
interface FieldOptionRepository {
    fun save(fieldOption: FieldOption): FieldOption
    fun deleteAll()
}