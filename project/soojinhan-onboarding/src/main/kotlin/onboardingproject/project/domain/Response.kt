package onboardingproject.project.domain

import jakarta.persistence.*
import java.util.*

/**
 * packageName : onboardingproject.project.domain
 * fileName    : Response
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
@Entity
class Response(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column
    val textValue: String?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    val surveyField: SurveyField,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    val fieldOptions: List<FieldOption>?
) : BaseEntity()