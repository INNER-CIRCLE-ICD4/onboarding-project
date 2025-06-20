package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyRequest

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : Survey
 * author      : ckr
 * date        : 25. 6. 11.
 * description :
 */

@Table(name = "survey")
@Entity
class Survey (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null
): BaseTimeEntity() {
    companion object {
        fun from(createSurveyRequest: CreateSurveyRequest): Survey {
            return Survey(
                name = createSurveyRequest.name,
                description = createSurveyRequest.description
            )
        }
    }
}