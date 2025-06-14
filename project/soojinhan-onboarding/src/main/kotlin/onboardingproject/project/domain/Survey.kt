package onboardingproject.project.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

/**
 * packageName : onboardingproject.project.domain
 * fileName    : Survey
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
@Entity
class Survey(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var title: String,

    @Column
    var description: String?,
) : BaseEntity()