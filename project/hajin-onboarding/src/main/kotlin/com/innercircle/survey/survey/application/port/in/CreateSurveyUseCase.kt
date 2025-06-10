package com.innercircle.survey.survey.application.port.`in`

import com.innercircle.survey.survey.domain.Survey

interface CreateSurveyUseCase {
    fun createSurvey(command: CreateSurveyCommand): Survey

    data class CreateSurveyCommand(
        val title: String,
        val description: String,
        val questions: List<QuestionCommand>,
    ) {
        data class QuestionCommand(
            val title: String,
            val description: String,
            val type: String,
            val required: Boolean,
            val choices: List<String> = emptyList(),
        )
    }
}
