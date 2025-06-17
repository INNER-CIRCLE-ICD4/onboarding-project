package onboardingproject.project.domain

import jakarta.persistence.*
import onboardingproject.project.common.domain.BaseEntity
import java.util.*

/**
 * packageName : onboardingproject.project.domain
 * fileName    : FieldOption
 * author      : hsj
 * date        : 2025. 6. 14.
 * description : 선택형 항목의 후보
 */
@Entity
class FieldOption(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column
    var optionValue: String,
) : BaseEntity()
