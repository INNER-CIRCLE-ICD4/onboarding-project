package onboardingproject.project.domain

import jakarta.persistence.*
import onboardingproject.project.common.domain.BaseEntity
import java.util.*

/**
 * packageName : onboardingproject.project.domain
 * fileName    : SurveyField
 * author      : hsj
 * date        : 2025. 6. 14.
 * description : 설문 받을 항목
 */
@Entity
class SurveyField(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val fieldKey: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var version: Int = 1,

    @Column(nullable = false)
    var fieldName: String,

    @Column
    var description: String?,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var fieldType: FieldType,

    @Column(nullable = false)
    var isRequired: Boolean,

    @Column(nullable = false)
    var isDeleted: Boolean = false,

    @Column(nullable = false)
    var fieldOrder: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_version_id", nullable = false)
    val surveyVersion: SurveyVersion,

    @OneToMany(fetch = FetchType.LAZY)
    val fieldOptions: List<FieldOption>?
) : BaseEntity()