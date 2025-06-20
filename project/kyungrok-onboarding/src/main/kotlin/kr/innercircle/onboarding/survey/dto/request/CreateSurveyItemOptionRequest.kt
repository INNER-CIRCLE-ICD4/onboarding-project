package kr.innercircle.onboarding.survey.dto.request

import jakarta.validation.constraints.NotBlank

/**
 * packageName : kr.innercircle.onboarding.survey.dto.request
 * fileName    : CreateSurveyItemOptionRequest
 * author      : ckr
 * date        : 25. 6. 17.
 * description :
 */


data class CreateSurveyItemOptionRequest(
    @field:NotBlank(message = "설문조사 항목 옵션은 필수값 입니다.")
    val option: String
)