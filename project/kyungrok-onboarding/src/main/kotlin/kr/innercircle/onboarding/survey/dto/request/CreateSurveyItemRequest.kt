package kr.innercircle.onboarding.survey.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CreateSurveyItemRequest(
    @field:NotBlank(message = "설문조사 항목명은 필수값 입니다.")
    val name: String,
    val description: String?,

    @field:NotNull(message = "설문조사 항목 입력 형태는 필수값 입니다. (SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE)")
    val inputType: SurveyItemInputType,

    @field:Valid
    val options: List<CreateSurveyItemOptionRequest>?,

    @field:NotNull(message = "설문조사 항목 필수 여부는 필수값 입니다.")
    val isRequired: Boolean
)