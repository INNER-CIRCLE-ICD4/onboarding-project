package onboardingproject.project.domain

import jakarta.persistence.*
import java.util.*

/**
 * packageName : onboardingproject.project.domain
 * fileName    : FieldOption
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
@Entity
class FieldOption(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column
    var optionValue: String,
) : BaseEntity()
