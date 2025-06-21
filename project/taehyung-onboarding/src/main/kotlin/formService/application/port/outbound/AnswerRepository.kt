package formService.application.port.outbound

import formService.domain.Answer

interface AnswerRepository {
    fun save(answer: Answer)
}
