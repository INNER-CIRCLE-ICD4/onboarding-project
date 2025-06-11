package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : SurveyResponseAnswer
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

@Table(name = "survey_response_answer")
@Entity
class SurveyResponseAnswer(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_item_id")
    var surveyItem: SurveyItem? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_item_archive_id")
    var surveyItemArchive: SurveyItemArchive? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    var surveyResponse: SurveyResponse,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 1000)
    var answer: String? = null
): BaseTimeEntity()