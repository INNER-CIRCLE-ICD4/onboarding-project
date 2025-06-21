package formService.adapter.port.inbound.web

import formService.adapter.port.inbound.web.dto.RequestSubmitSurveyFormDto
import formService.adapter.port.inbound.web.dto.Success
import formService.application.port.inbound.SubmitSurveyFormUseCase
import formService.exception.BadRequestException
import formService.util.checkUUID
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SubmitSurveyFormController(
    private val useCase: SubmitSurveyFormUseCase,
) {
    @PostMapping("/api/v1/survey/{id}/submit")
    fun execute(
        @PathVariable id: String,
        @RequestParam("userId") userId: String,
        @Valid @RequestBody request: RequestSubmitSurveyFormDto,
    ): Success<SubmitSurveyFormUseCase.AnswerId> {
        if (checkUUID(userId).not()) {
            throw BadRequestException("userId 형식은 UUID 여야됩니다.")
        }

        return Success(message = "success", status = HttpStatus.CREATED, data = useCase.submitSurveyForm(request.toCommand(id, userId)))
    }
}
