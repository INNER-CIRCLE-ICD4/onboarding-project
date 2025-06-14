package formService.adapter.port.outbound.persistence.jpa

import formService.application.port.outbound.SurveyFormRepository
import formService.common.Adapter
import formService.domain.Question
import formService.domain.QuestionOption
import formService.domain.SurveyForm
import jakarta.persistence.EntityNotFoundException

@Adapter
class SurveyFormPersistenceAdapter(
    private val surveyFormJpaRepository: SurveyFormJpaRepository,
) : SurveyFormRepository {
    override fun save(survey: SurveyForm) {
        val entity =
            SurveyFormJpaEntity(
                id = survey.id,
                surveyName = survey.surveyName,
                description = survey.description,
            )

        entity.addQuestions(
            question =
                survey.questions.map { q ->
                    val questionJpaEntity =
                        QuestionJpaEntity(
                            name = q.name,
                            description = q.description,
                            inputType = q.inputType,
                            required = q.required,
                        )

                    questionJpaEntity.addOptions(
                        options =
                            q.options?.map { qo ->
                                QuestionOptionJpaEntity(option = qo.value)
                            },
                    )
                    questionJpaEntity
                },
        )

        surveyFormJpaRepository.save(entity)
    }

    override fun getOneBy(id: String): SurveyForm {
        val entity =
            surveyFormJpaRepository
                .findById(
                    id,
                ).orElseThrow { throw EntityNotFoundException("surveyForm entity is not found by $id") }

        return SurveyForm(
            id = entity.id,
            surveyName = entity.surveyName,
            description = entity.description,
            questions =
                entity.questions.map { q ->
                    Question(
                        id = q.id,
                        name = q.name,
                        description = q.description,
                        required = q.required,
                        inputType = q.inputType,
                        options =
                            q.options.map { qo ->
                                QuestionOption(
                                    id = qo.id,
                                    value = qo.option,
                                )
                            },
                    )
                },
        )
    }
}
