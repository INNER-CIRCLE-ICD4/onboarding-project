package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyResponseRequest

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
    var respondent: String
): BaseTimeEntity() {
    companion object {
        fun from(
            survey: Survey,
            createSurveyResponseRequest: CreateSurveyResponseRequest
        ): SurveyResponse {
            return SurveyResponse(
                survey = survey,
                respondent = createSurveyResponseRequest.respondent
            )
        }
    }
}