package fc.innercircle.jinhoonboarding.answer.controller

import fc.innercircle.jinhoonboarding.answer.dto.request.SubmitAnswerSetRequest
import fc.innercircle.jinhoonboarding.answer.service.AnswerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AnswerController(
    private val answerService: AnswerService
) {

    @PostMapping("/api/answer")
    fun submitAnswer(
        @RequestBody answerSet: SubmitAnswerSetRequest,
    ): AnswerSetResponse {

    }
}