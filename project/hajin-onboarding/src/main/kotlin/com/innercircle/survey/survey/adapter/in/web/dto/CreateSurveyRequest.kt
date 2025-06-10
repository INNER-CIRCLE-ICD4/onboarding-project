package com.innercircle.survey.survey.adapter.`in`.web.dto

import com.innercircle.survey.survey.application.port.`in`.CreateSurveyUseCase.CreateSurveyCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CreateSurveyRequest(
    @field:NotBlank(message = "설문조사 제목은 필수입니다.")
    @field:Size(max = 200, message = "설문조사 제목은 200자를 초과할 수 없습니다.")
    val title: String,
    @field:NotBlank(message = "설문조사 설명은 필수입니다.")
    @field:Size(max = 1000, message = "설문조사 설명은 1000자를 초과할 수 없습니다.")
    val description: String,
    @field:Valid
    @field:NotEmpty(message = "최소 1개 이상의 설문 항목이 필요합니다.")
    @field:Size(min = 1, max = 10, message = "설문 항목은 1개에서 10개 사이여야 합니다.")
    val questions: List<QuestionRequest>,
) {
    data class QuestionRequest(
        @field:NotBlank(message = "항목 제목은 필수입니다.")
        @field:Size(max = 200, message = "항목 제목은 200자를 초과할 수 없습니다.")
        val title: String,
        @field:NotBlank(message = "항목 설명은 필수입니다.")
        @field:Size(max = 500, message = "항목 설명은 500자를 초과할 수 없습니다.")
        val description: String,
        @field:NotBlank(message = "항목 타입은 필수입니다.")
        val type: String,
        val required: Boolean = false,
        @field:Size(max = 20, message = "선택지는 최대 20개까지만 가능합니다.")
        val choices: List<String> = emptyList(),
    )

    fun toCommand(): CreateSurveyCommand =
        CreateSurveyCommand(
            title = title,
            description = description,
            questions =
                questions.map { question ->
                    CreateSurveyCommand.QuestionCommand(
                        title = question.title,
                        description = question.description,
                        type = question.type,
                        required = question.required,
                        choices = question.choices,
                    )
                },
        )
}
