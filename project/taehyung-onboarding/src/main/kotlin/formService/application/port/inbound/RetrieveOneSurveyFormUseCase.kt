package formService.application.port.inbound

import formService.domain.InputType

interface RetrieveOneSurveyFormUseCase {
    fun retrieveSurveyForm(id: String): RetrieveSurveyFormRead

    data class RetrieveSurveyFormRead(
        val id: String,
        val surveyName: String,
        val description: String,
        val questions: List<RetrieveSurveyFormQuestion>,
    )

    data class RetrieveSurveyFormQuestion(
        val id: Long,
        val name: String,
        val description: String,
        val inputType: InputType,
        val required: Boolean,
        val isRemoved: Boolean,
        val options: List<RetrieveSurveyFormQuestionOption>,
    )

    data class RetrieveSurveyFormQuestionOption(
        val id: Long,
        val value: String,
    )
}
