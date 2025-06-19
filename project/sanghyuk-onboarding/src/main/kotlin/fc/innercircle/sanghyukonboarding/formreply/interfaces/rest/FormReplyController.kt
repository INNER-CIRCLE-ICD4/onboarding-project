package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest

import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand
import fc.innercircle.sanghyukonboarding.formreply.domain.service.port.FormReplyReader
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.dto.response.FormReplyResponse
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.SubmitReplyUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RequestMapping(path = ["/api/v1/forms/{formId}"])
@RestController
class FormReplyController(
    private val submitReplyUseCase: SubmitReplyUseCase,
    private val replyReader: FormReplyReader
) {

    @PostMapping("/replies",consumes = ["application/json"], produces = ["application/json"])
    fun submitReply(
        @PathVariable formId: String,
        @RequestBody commands: List<FormReplyCommand>,
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
    ): ResponseEntity<List<FormReplyResponse>> {
        val response = replyReader.getAllByFormId(formId).map { reply ->
            FormReplyResponse.from(reply)
        }
        return ResponseEntity.ok(response)
    }

}
