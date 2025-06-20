package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest

import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.SubmitReplyUseCase
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.SummarizeFormRepliesUseCase
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.request.AnswerRequest
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response.ReplySummaryResponse
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
    private val summarizeFormRepliesUseCase: SummarizeFormRepliesUseCase
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

    @GetMapping("/replies/summary", produces = ["application/json"])
    fun summarizeFormReplies(
        @PathVariable formId: String,
    ): ResponseEntity<ReplySummaryResponse> {
        val response: ReplySummaryResponse = summarizeFormRepliesUseCase.summarize(formId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/replies", produces = ["application/json"])
    fun getFormReplies(
        @PathVariable formId: String,
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam questionName: String = ""
    ): ResponseEntity<List<ReplySummaryResponse>> {
        val response: List<ReplySummaryResponse> = summarizeFormRepliesUseCase.summarize(formId).replies
        return ResponseEntity.ok(response)
    }

}
