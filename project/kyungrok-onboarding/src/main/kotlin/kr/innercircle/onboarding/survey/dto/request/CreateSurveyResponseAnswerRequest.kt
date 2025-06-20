package kr.innercircle.onboarding.survey.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * packageName : kr.innercircle.onboarding.survey.dto.request
 * fileName    : CreateSurveyResponseAnswerRequest
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class CreateSurveyResponseAnswerRequest(
    @field:NotNull(message = "설문조사 항목 id는 필수값입니다.")
    val surveyItemId: Long,

    @field:NotBlank(message = "설문조사 항목에 대한 답변은 필수값입니다.")
    val answer: String,
)
