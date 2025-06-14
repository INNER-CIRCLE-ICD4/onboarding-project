package formService.application

import formService.application.port.inbound.CreateSurveyFormUseCase
import formService.application.port.outbound.SurveyFormRepository
import formService.domain.Question
import formService.domain.QuestionOption
import formService.domain.SurveyForm
import formService.util.getTsid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SurveyFormService(
    private val repository: SurveyFormRepository,
) : CreateSurveyFormUseCase {
    @Transactional
    override fun createSurveyForm(command: CreateSurveyFormUseCase.CreateSurveyFormCommand) {
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
