package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : SurveyItemArchive
 * author      : ckr
 * date        : 25. 6. 11.
 * description :
 */

@Table(name = "survey_item_archive")
@Entity
class SurveyItemArchive(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    var survey: Survey,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var surveyItemId: Long,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var inputType: SurveyItemInputType,

    @Column(nullable = false)
    var isRequired: Boolean,

    @Column(nullable = false)
    var orderNumber: Int,

    @Column(length = 1000)
    var options: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var archiveReason: SurveyItemArchiveReason,
): BaseTimeEntity()

enum class SurveyItemArchiveReason {
    MODIFIED,
    DELETED
}