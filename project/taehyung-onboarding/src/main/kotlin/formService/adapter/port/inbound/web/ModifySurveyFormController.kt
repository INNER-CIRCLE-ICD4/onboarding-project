package formService.adapter.port.inbound.web

import formService.adapter.port.inbound.web.dto.RequestModifySurveyFormDto
import formService.adapter.port.inbound.web.dto.Success
import formService.application.port.inbound.ModifySurveyFormUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ModifySurveyFormController(
    private val useCase: ModifySurveyFormUseCase,
) {
    @PutMapping("/api/v1/survey/{id}")
    fun execute(
        @PathVariable id: String,
        @Valid @RequestBody
        request: RequestModifySurveyFormDto,
    ): Success<ModifySurveyFormUseCase.ModifySurveyFormId> {
        request.validConditionOptions()
        println(request.toCommand(id))
        return Success(message = "success", status = HttpStatus.OK, data = useCase.modifySurveyForm(request.toCommand(id)))
    }
}
