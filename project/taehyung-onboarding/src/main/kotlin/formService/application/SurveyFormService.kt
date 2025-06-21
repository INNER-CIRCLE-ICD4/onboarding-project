package formService.application

import formService.application.port.inbound.CreateSurveyFormUseCase
import formService.application.port.inbound.ModifySurveyFormUseCase
import formService.application.port.inbound.RetrieveOneSurveyFormUseCase
import formService.application.port.outbound.SurveyFormRepository
import formService.domain.Question
import formService.domain.QuestionOption
import formService.domain.SurveyForm
import formService.exception.BadRequestException
import formService.util.getTsid
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SurveyFormService(
    private val repository: SurveyFormRepository,
) : CreateSurveyFormUseCase,
    RetrieveOneSurveyFormUseCase,
    ModifySurveyFormUseCase {
    @Transactional
    override fun createSurveyForm(command: CreateSurveyFormUseCase.CreateSurveyFormCommand): CreateSurveyFormUseCase.CreateSurveyFormId {
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

        return CreateSurveyFormUseCase.CreateSurveyFormId(surveyForm.id)
    }

    @Transactional(readOnly = true)
    override fun retrieveSurveyForm(id: String): RetrieveOneSurveyFormUseCase.RetrieveSurveyFormRead {
        try {
            val surveyForm = repository.getOneBy(id)
            return RetrieveOneSurveyFormUseCase.RetrieveSurveyFormRead(
                id = surveyForm.id,
                surveyName = surveyForm.surveyName,
                description = surveyForm.description,
                questions =
                    surveyForm.questions.map { q ->
                        RetrieveOneSurveyFormUseCase.RetrieveSurveyFormQuestion(
                            id = q.id!!,
                            name = q.name,
                            description = q.description,
                            inputType = q.inputType,
                            required = q.required,
                            isRemoved = q.isRemoved,
                            options =
                                q.options?.map { qo ->
                                    RetrieveOneSurveyFormUseCase.RetrieveSurveyFormQuestionOption(
                                        id = qo.id!!,
                                        value = qo.value,
                                    )
                                } ?: emptyList(),
                        )
                    },
            )
        } catch (e: EntityNotFoundException) {
            throw BadRequestException(message = "설문지를 찾지 못햇습니다. id: $id")
        }
    }

    @Transactional
    override fun modifySurveyForm(command: ModifySurveyFormUseCase.ModifySurveyFormCommand): ModifySurveyFormUseCase.ModifySurveyFormId {
        try {
            val surveyForm = repository.getOneBy(command.id)
            surveyForm.modify(surveyName = command.surveyName, description = command.description)
            surveyForm.modifyQuestion(questions = command.toQuestions())

            repository.update(surveyForm)

            return ModifySurveyFormUseCase.ModifySurveyFormId(surveyForm.id)
        } catch (e: EntityNotFoundException) {
            throw BadRequestException(message = "설문지를 찾지 못햇습니다. id: ${command.id}")
        }
    }
}
