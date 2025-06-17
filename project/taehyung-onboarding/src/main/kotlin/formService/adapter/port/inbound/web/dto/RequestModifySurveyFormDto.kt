package formService.adapter.port.inbound.web.dto

import formService.adapter.port.inbound.web.validator.IsEnum
import formService.application.port.inbound.ModifySurveyFormUseCase
import formService.domain.Question
import formService.exception.BadRequestException
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class RequestModifySurveyFormDto(
    @field:NotBlank(message = "설문 이름은 필수입니다.")
    val surveyName: String,
    @field:NotBlank(message = "설문 설명은 필수입니다.")
    val description: String,
    @field:NotNull(message = "설문 항목은 필수입니다.")
    @field:Size(min = 1, message = "설문 항목은 최소 1개 이상입니다.")
    @field:Valid
    val questions: List<@Valid RequestModifySurveyFormQuestionDto>,
) {
    data class RequestModifySurveyFormQuestionDto(
        @field:NotNull(message = "설문 항목 id는 필수 입니다.")
        val id: Long,
        @field:NotBlank(message = "설문 항목 이름은 필수입니다.")
        val name: String,
        @field:NotBlank(message = "설문 항목 설명은 필수입니다.")
        val description: String,
        @field:IsEnum(enumClass = RequestQuestionInputType::class)
        val inputType: String,
        @field:NotNull(message = "설문 항목 필수여부는 필수입니다.")
        val required: Boolean,
        @field:NotNull(message = "설문 항목 삭제 여부는 필수입니다.")
        val isRemoved: Boolean,
        @field:Valid
        val options: List<RequestModifySurveyFormQuestionOptionDto>?,
    )

    data class RequestModifySurveyFormQuestionOptionDto(
        @field:NotNull(message = "설문 선택지 id는 필수 입니다.")
        val id: Long,
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

    fun toCommand(id: String): ModifySurveyFormUseCase.ModifySurveyFormCommand =
        ModifySurveyFormUseCase.ModifySurveyFormCommand(
            id = id,
            surveyName = this.surveyName,
            description = this.description,
            questions =
                this.questions.map {
                    ModifySurveyFormUseCase.ModifySurveyFormQuestion(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        inputType = Question.QuestionInputType.entries.find { qt -> qt.name == it.inputType }!!,
                        required = it.required,
                        isRemoved = it.isRemoved,
                        options =
                            it.options?.map { qo ->
                                ModifySurveyFormUseCase.ModifySurveyFormQuestionOption(
                                    id = qo.id,
                                    value = qo.value,
                                )
                            },
                    )
                },
        )
}
