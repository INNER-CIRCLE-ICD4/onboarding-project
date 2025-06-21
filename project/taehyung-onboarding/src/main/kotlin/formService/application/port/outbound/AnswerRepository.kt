package formService.application.port.outbound

import formService.domain.Answer

interface AnswerRepository {
    fun getOneBy(id: String): Answer

    fun save(answer: Answer)
}
