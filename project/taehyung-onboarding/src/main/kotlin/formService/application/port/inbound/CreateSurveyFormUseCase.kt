package formService.application.port.inbound

import formService.domain.InputType

interface CreateSurveyFormUseCase {
    fun createSurveyForm(command: CreateSurveyFormCommand): CreateSurveyFormId

    data class CreateSurveyFormCommand(
        var surveyName: String,
        var description: String,
        val questions: List<CreateSurveyFormQuestion>,
    )

    data class CreateSurveyFormQuestion(
        var name: String,
        var description: String,
        var inputType: InputType,
        var required: Boolean,
        val options: List<CreateSurveyFormQuestionOption>?,
    )

    data class CreateSurveyFormQuestionOption(
        var value: String,
    )

    data class CreateSurveyFormId(
        val id: String,
    )
}
