package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyResponseAnswerRequest

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
    @JoinColumn(name = "survey_item_id", nullable = false)
    var surveyItem: SurveyItem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_response_id", nullable = false)
    var surveyResponse: SurveyResponse,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 1000)
    var answer: String? = null
): BaseTimeEntity() {
    companion object {
        fun from(
            surveyItem: SurveyItem,
            surveyResponse: SurveyResponse,
            createSurveyResponseAnswerRequest: CreateSurveyResponseAnswerRequest
        ): SurveyResponseAnswer {
            return SurveyResponseAnswer(
                surveyItem = surveyItem,
                surveyResponse = surveyResponse,
                answer = createSurveyResponseAnswerRequest.answer
            )
        }
    }
}