package formService.adapter.port.inbound.web.dto

import formService.adapter.port.inbound.web.validator.IsEnum
import formService.application.port.inbound.CreateSurveyFormUseCase
import formService.domain.InputType
import formService.exception.BadRequestException
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class RequestCreateSurveyFormDto(
    @field:NotBlank(message = "설문 이름은 필수입니다.")
    val surveyName: String,
    @field:NotBlank(message = "설문 설명은 필수입니다.")
    val description: String,
    @field:NotNull(message = "설문 항목은 필수입니다.")
    @field:Size(min = 1, message = "설문 항목은 최소 1개 이상입니다.")
    @field:Valid
    val questions: List<@Valid RequestCreateSurveyFormQuestionDto>,
) {
    data class RequestCreateSurveyFormQuestionDto(
        @field:NotBlank(message = "설문 항목 이름은 필수입니다.")
        val name: String,
        @field:NotBlank(message = "설문 항목 설명은 필수입니다.")
        val description: String,
        @field:IsEnum(enumClass = RequestQuestionInputType::class)
        val inputType: String,
        @field:NotNull(message = "설문 항목 필수여부는 필수입니다.")
        val required: Boolean,
        @field:Valid
        val options: List<@Valid RequestCreateSurveyFormQuestionOptionDto>?,
    )

    data class RequestCreateSurveyFormQuestionOptionDto(
        @field:NotBlank(message = "설문 선택지는 필수 입니다.")
        val value: String,
    )

    fun validConditionOptions() {
        questions.forEach {
            if ((RequestQuestionInputType.isShortText(it.inputType) || RequestQuestionInputType.isLongText(it.inputType)) &&
                it.options != null
            ) {
                throw BadRequestException("단답, 장문 타입 인 경우 설문 선택지를 포함 할 수 없습니다.")
            } else if ((
                    RequestQuestionInputType.isSingleChoice(it.inputType) ||
                        RequestQuestionInputType.isMultiChoice(it.inputType)
                ) &&
                it.options.isNullOrEmpty()
            ) {
                throw BadRequestException("단일, 다중 선택 리스트는 설문 선택지가 최소 1개이상 포함되어야됩니다.")
            }
        }
    }

    fun toCommand() =
        CreateSurveyFormUseCase.CreateSurveyFormCommand(
            surveyName = surveyName,
            description = description,
            questions =
                questions.map {
                    CreateSurveyFormUseCase.CreateSurveyFormQuestion(
                        it.name,
                        it.description,
                        inputType = InputType.entries.find { t -> t.name == it.inputType }!!,
                        options =
                            it.options?.map { o ->
                                CreateSurveyFormUseCase.CreateSurveyFormQuestionOption(o.value)
                            },
                        required = it.required,
                    )
                },
        )
}
