package formService.adapter.port.inbound.web

import formService.adapter.port.inbound.web.dto.Success
import formService.application.port.inbound.RetrieveOneSurveyFormUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RetrieveSurveyFormController(
    private val retrieveOneSurveyFormUseCase: RetrieveOneSurveyFormUseCase,
) {
    @GetMapping("/api/v1/survey/{id}")
    fun execute(
        @PathVariable id: String,
    ) = Success(
        message = "success",
        status = HttpStatus.OK,
        data = retrieveOneSurveyFormUseCase.retrieveSurveyForm(id),
    )
}
