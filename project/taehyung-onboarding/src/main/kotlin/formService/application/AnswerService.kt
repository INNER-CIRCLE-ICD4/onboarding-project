package formService.application

import formService.application.port.inbound.RetrieveAnswerUseCase
import formService.application.port.inbound.SubmitSurveyFormUseCase
import formService.application.port.outbound.AnswerRepository
import formService.application.port.outbound.SurveyFormRepository
import formService.exception.BadRequestException
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AnswerService(
    private val repository: SurveyFormRepository,
    private val answerRepository: AnswerRepository,
) : SubmitSurveyFormUseCase,
    RetrieveAnswerUseCase {
    @Transactional
    override fun submitSurveyForm(command: SubmitSurveyFormUseCase.SubmitSurveyFormCommand): SubmitSurveyFormUseCase.AnswerId {
        try {
            val surveyForm = repository.getOneBy(command.surveyFormId)

            val answer = command.toDomain()

            answer.validateAnswer(surveyForm.questions)

            answerRepository.save(answer)

            return SubmitSurveyFormUseCase.AnswerId(answer.id)
        } catch (e: EntityNotFoundException) {
            throw BadRequestException(message = "설문지를 찾지 못햇습니다. id: ${command.surveyFormId}")
        }
    }

    @Transactional(readOnly = true)
    override fun retrieveAnswer(answerId: String): RetrieveAnswerUseCase.RetrieveAnswerRead {
        val answer = answerRepository.getOneBy(answerId)

        return RetrieveAnswerUseCase.RetrieveAnswerRead(
            answerId = answer.id,
            userId = answer.userId,
            submittedAt = answer.submittedAt,
            values =
                answer.values.map {
                    RetrieveAnswerUseCase.RetrieveQuestionAnswer(
                        answerValue = it.answerValue,
                    )
                },
        )
    }
}
