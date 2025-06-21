package formService.adapter.port.outbound.persistence.jpa

import formService.application.port.outbound.AnswerRepository
import formService.common.Adapter
import formService.domain.Answer
import formService.domain.QuestionAnswer
import jakarta.persistence.EntityNotFoundException

@Adapter
class AnswerPersistenceAdapter(
    private val answerJpaRepository: AnswerJpaRepository,
) : AnswerRepository {
    override fun save(answer: Answer) {
        val answerJpaEntity =
            AnswerJpaEntity(
                id = answer.id,
                userId = answer.userId,
                submittedAt = answer.submittedAt,
            )

        answerJpaEntity.addQuestionAnswers(
            questionAnswers =
                answer.values.map {
                    QuestionAnswerJpaEntity(
                        answerType = it.answerType,
                        answerValue = it.answerValue,
                    )
                },
        )

        answerJpaRepository.save(answerJpaEntity)
    }

    override fun getOneBy(id: String): Answer {
        val answerJpaEntity =
            answerJpaRepository
                .findById(
                    id,
                ).orElseThrow { throw EntityNotFoundException("answer entity is not found by $id") }

        return Answer(
            id = answerJpaEntity.id,
            userId = answerJpaEntity.userId,
            submittedAt = answerJpaEntity.submittedAt,
            values =
                answerJpaEntity.questionAnswers.map {
                    QuestionAnswer(
                        id = it.id,
                        answerValue = it.answerValue,
                        answerType = it.answerType,
                    )
                },
        )
    }
}
