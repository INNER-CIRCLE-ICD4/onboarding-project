package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest

import fc.innercircle.sanghyukonboarding.formreply.domain.service.port.FormReplyQueryRepository
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.SubmitReplyUseCase
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.request.AnswerRequest
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response.FormReplyResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RequestMapping(path = ["/api/v1/forms/{formId}"])
@RestController
class FormReplyController(
    private val submitReplyUseCase: SubmitReplyUseCase,
    private val replyReader: FormReplyQueryRepository
) {

    @PostMapping("/replies",consumes = ["application/json"], produces = ["application/json"])
    fun submitReply(
        @PathVariable formId: String,
        @RequestBody commands: List<AnswerRequest>,
    ): ResponseEntity<Unit> {
        val replyId: String = submitReplyUseCase.submit(formId, commands)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{replyId}")
            .buildAndExpand(replyId)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/replies", produces = ["application/json"])
    fun getAllReplies(
        @PathVariable formId: String,
        @RequestParam(required = false) version: Int? = null,
    ): ResponseEntity<List<FormReplyResponse>> {
        val response = replyReader.getAllByFormId(formId).map { reply ->
            FormReplyResponse.from(reply)
        }
        return ResponseEntity.ok(response)
    }

}
