package onboardingproject.project.domain

import jakarta.persistence.*
import java.util.*

/**
 * packageName : onboardingproject.project.domain
 * fileName    : SurveyVersion
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
@Entity
class SurveyVersion(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val version: Int = 1,

    @Column(nullable = false)
    val title: String,

    @Column
    val description: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    val survey: Survey
) : BaseEntity()