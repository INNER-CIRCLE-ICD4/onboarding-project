package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest

import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.FormService
import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.dto.response.FormReplyResponse
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.FormReplyUseCase
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/api/v1/forms"])
@RestController
class FormReplyController(
    private val formService: FormService,
    private val formReplyUseCase: FormReplyUseCase,
) {

    @PostMapping(path = ["/{formId}/submit"], consumes = ["application/json"], produces = ["application/json"])
    fun submitFormReply(
        @PathVariable formId: String,
        @RequestBody commands: List<FormReplyCommand>,
    ): FormReplyResponse {
        val formReplyId: String = formReplyUseCase.reply(formId, commands)
        val formReply = formReplyUseCase.getById(formReplyId)
        return FormReplyResponse.from(formReply)
    }

}
