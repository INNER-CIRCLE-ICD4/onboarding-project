package formService.adapter.port.outbound.persistence.jpa

import formService.application.port.outbound.SurveyFormRepository
import formService.domain.SurveyForm

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
}
