package onboardingproject.project.domain

import jakarta.persistence.*
import onboardingproject.project.common.domain.BaseEntity
import java.util.*

/**
 * packageName : onboardingproject.project.domain
 * fileName    : SurveyVersion
 * author      : hsj
 * date        : 2025. 6. 14.
 * description : 설문조사 버전
 */
@Entity
class SurveyVersion(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val version: Int = 1, // 수정될때마다 1씩 증가

    @Column(nullable = false)
    val title: String,

    @Column
    val description: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    val survey: Survey
) : BaseEntity()