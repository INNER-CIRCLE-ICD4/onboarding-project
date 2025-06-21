package formService.application.port.inbound

import formService.domain.Question
import formService.domain.QuestionOption

interface ModifySurveyFormUseCase {
    fun modifySurveyForm(command: ModifySurveyFormCommand): ModifySurveyFormId

    data class ModifySurveyFormCommand(
        val id: String,
        val surveyName: String,
        val description: String,
        val questions: List<ModifySurveyFormQuestion>,
    ) {
        fun toQuestions(): List<Question> =
            this.questions.map {
                Question(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    inputType = it.inputType,
                    required = it.required,
                    isRemoved = it.isRemoved,
                    options =
                        it.options?.map { qo ->
                            QuestionOption(
                                id = qo.id,
                                value = qo.value,
                            )
                        },
                )
            }
    }

    data class ModifySurveyFormQuestion(
        val id: Long,
        val name: String,
        val description: String,
        val inputType: Question.QuestionInputType,
        val required: Boolean,
        val isRemoved: Boolean,
        val options: List<ModifySurveyFormQuestionOption>?,
    )

    data class ModifySurveyFormQuestionOption(
        val id: Long,
        val value: String,
    )

    data class ModifySurveyFormId(
        val id: String,
    )
}
