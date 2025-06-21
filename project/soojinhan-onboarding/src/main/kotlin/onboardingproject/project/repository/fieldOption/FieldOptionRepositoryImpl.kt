package onboardingproject.project.repository.fieldOption

import onboardingproject.project.domain.FieldOption
import org.springframework.stereotype.Repository

/**
 * packageName : onboardingproject.project.repository.fieldOption
 * fileName    : FieldOptionRepositoryImpl
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
@Repository
class FieldOptionRepositoryImpl(
    private val fieldOptionJpaRepository: FieldOptionJpaRepository
) : FieldOptionRepository {
    override fun save(fieldOption: FieldOption): FieldOption {
        return fieldOptionJpaRepository.save(fieldOption)
    }

    override fun deleteAll() {
        fieldOptionJpaRepository.deleteAll()
    }

    override fun findByIdIn(optionIds: List<String>): List<FieldOption> {
        return fieldOptionJpaRepository.findByIdIn(optionIds)
    }

}