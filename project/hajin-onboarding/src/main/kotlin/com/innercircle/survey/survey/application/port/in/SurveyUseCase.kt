package com.innercircle.survey.survey.application.port.`in`

import com.innercircle.survey.survey.domain.Survey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SurveyUseCase {
    fun createSurvey(command: CreateSurveyCommand): Survey

    fun getSurveyById(surveyId: UUID): Survey

    fun getSurveys(pageable: Pageable): Page<Survey>

    data class CreateSurveyCommand(
        val title: String,
        val description: String,
        val questions: List<QuestionCommand> = emptyList(),
    ) {
        data class QuestionCommand(
            val title: String,
            val description: String,
            val type: String,
            val required: Boolean = false,
            val choices: List<String> = emptyList(),
        )
    }
}
