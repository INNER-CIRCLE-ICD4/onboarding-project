package formService.adapter.port.inbound.web.dto

import formService.adapter.port.inbound.web.validator.IsEnum
import formService.application.port.inbound.SubmitSurveyFormUseCase
import formService.domain.InputType
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class RequestSubmitSurveyFormDto(
    @field:NotNull(message = "응답 값 목록은 필수 입니다.")
    @field:Size(min = 1, message = "응답 항목은 최소 1개 이상입니다.")
    @field:Valid
    val answers: List<@Valid RequestSubmitSurveyFormAnswerDto>,
) {
    data class RequestSubmitSurveyFormAnswerDto(
        @field:Min(value = 1, message = "설문 응답 ID 는 필수 또는 잘못된 값입니다.")
        val questionId: Long,
        @field:NotBlank(message = "설문 응답 값은 필수 입니다.")
        val answerValue: String,
        @field:IsEnum(enumClass = RequestQuestionInputType::class)
        val answerType: String,
    )

    fun toCommand(
        id: String,
        userId: String,
    ): SubmitSurveyFormUseCase.SubmitSurveyFormCommand =
        SubmitSurveyFormUseCase.SubmitSurveyFormCommand(
            surveyFormId = id,
            userId = userId,
            answers =
                this.answers.map {
                    SubmitSurveyFormUseCase.SubmitSurveyFormAnswer(
                        questionId = it.questionId,
                        answerValue = it.answerValue,
                        answerType = InputType.entries.find { t -> t.name == it.answerType }!!,
                    )
                },
        )
}
