package formService.adapter.port.inbound.web

import formService.adapter.port.inbound.web.dto.RequestCreateSurveyFormDto
import formService.adapter.port.inbound.web.dto.Success
import formService.application.port.inbound.CreateSurveyFormUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateSurveyFormController(
    private val useCase: CreateSurveyFormUseCase,
) {
    @PostMapping("/api/v1/survey")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun execute(
        @Valid @RequestBody
        request: RequestCreateSurveyFormDto,
    ): Success<CreateSurveyFormUseCase.CreateSurveyFormId> {
        request.validConditionOptions()
        return Success(message = "success", status = HttpStatus.CREATED, data = useCase.createSurveyForm(request.toCommand()))
    }
}
