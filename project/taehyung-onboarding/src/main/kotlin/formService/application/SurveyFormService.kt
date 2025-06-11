package formService.application

import formService.application.port.inbound.CreateSurveyForm
import formService.application.port.outbound.SurveyFormRepository
import formService.domain.Question
import formService.domain.QuestionOption
import formService.domain.SurveyForm
import formService.util.getTsid

class SurveyFormService(
    private val repository: SurveyFormRepository,
) : CreateSurveyForm {
    override fun createSurveyForm(command: CreateSurveyForm.CreateSurveyFormCommand) {
        // command convert to domain
        val surveyForm =
            SurveyForm(
                id = getTsid(),
                surveyName = command.surveyName,
                description = command.description,
                questions =
                    command.questions.map {
                        Question(
                            name = it.name,
                            description = it.description,
                            inputType = it.inputType,
                            required = it.required,
                            options =
                                it.options?.map { option ->
                                    QuestionOption(
                                        value = option.value,
                                    )
                                },
                        )
                    },
            )

        // save persist
        repository.save(surveyForm)
    }
}
