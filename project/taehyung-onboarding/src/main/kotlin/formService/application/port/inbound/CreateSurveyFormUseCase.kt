package formService.application.port.inbound

import formService.domain.Question

interface CreateSurveyFormUseCase {
    fun createSurveyForm(command: CreateSurveyFormCommand)

    data class CreateSurveyFormCommand(
        var surveyName: String,
        var description: String,
        val questions: List<CreateSurveyFormQuestion>,
    )

    data class CreateSurveyFormQuestion(
        var name: String,
        var description: String,
        var inputType: Question.QuestionInputType,
        var required: Boolean,
        val options: List<CreateSurveyFormQuestionOption>?,
    )

    data class CreateSurveyFormQuestionOption(
        var value: String,
    )
}
