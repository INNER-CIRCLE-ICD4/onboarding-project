package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : SurveyResponse
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

@Table(name = "survey_response")
@Entity
class SurveyResponse(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    var survey: Survey,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false , length = 100)
    var respondentId: String
): BaseTimeEntity()