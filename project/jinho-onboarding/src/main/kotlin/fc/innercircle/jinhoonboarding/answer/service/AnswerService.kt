package fc.innercircle.jinhoonboarding.answer.service

import fc.innercircle.jinhoonboarding.answer.dto.request.SubmitAnswerSetRequest
import fc.innercircle.jinhoonboarding.answer.repository.AnswerRepository
import org.springframework.stereotype.Service

@Service
class AnswerService(
    private val answerRepository: AnswerRepository
) {
    fun submitAnswer(request: SubmitAnswerSetRequest, surveyId: Long) {

    }

}
