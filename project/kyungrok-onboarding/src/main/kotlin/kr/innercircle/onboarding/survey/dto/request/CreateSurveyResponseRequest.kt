package kr.innercircle.onboarding.survey.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * packageName : kr.innercircle.onboarding.survey.dto.request
 * fileName    : CreateSurveyResponseRequest
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class CreateSurveyResponseRequest(
    @field:NotBlank(message = "설문조사 응답자 필수값입니다.")
    val respondent: String,

    @field:Valid
    @field:NotNull(message = "설문조사 답변은 필수값 입니다.")
    @field:Size(min = 1, message = "설문조사 답변은 최소 1개이상 필요합니다.")
    val answers: List<CreateSurveyResponseAnswerRequest>
)
