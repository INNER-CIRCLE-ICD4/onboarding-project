package kr.innercircle.onboarding.survey.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType

/**
 * packageName : kr.innercircle.onboarding.survey.dto.request
 * fileName    : UpdateSurveyItemRequest
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class UpdateSurveyItemRequest(
    @field:NotNull(message = "설문조사 항목 id는 필수값 입니다.")
    val surveyItemId: Long,

    @field:NotBlank(message = "설문조사 항목명은 필수값 입니다.")
    val name: String,

    val description: String?,

    @field:NotNull(message = "설문조사 항목 입력 형태는 필수값 입니다. (SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE)")
    val inputType: SurveyItemInputType,

    @field:Valid
    @field:NotNull(message = "설문조사 항목 옵션은 필수값 입니다.")
    val options: List<CreateSurveyItemOptionRequest>,

    @field:NotNull(message = "설문조사 항목 필수 여부는 필수값 입니다.")
    val isRequired: Boolean
)
