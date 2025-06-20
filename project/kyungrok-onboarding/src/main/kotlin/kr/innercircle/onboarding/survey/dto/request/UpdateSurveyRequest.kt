package kr.innercircle.onboarding.survey.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * packageName : kr.innercircle.onboarding.survey.dto.request
 * fileName    : UpdateSurveyRequest
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class UpdateSurveyRequest(
    @field:NotBlank(message = "설문조사 이름은 필수값 입니다.")
    val name: String,

    val description: String?,

    @field:Valid
    @field:NotNull
    @field:Size(min = 1, max = 10, message = "설문조사 항목은 최소 1개, 최대 10개까지 가능합니다.")
    val surveyItems: List<UpdateSurveyItemRequest>
)
