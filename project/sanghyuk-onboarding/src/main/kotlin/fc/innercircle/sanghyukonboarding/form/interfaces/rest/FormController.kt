package fc.innercircle.sanghyukonboarding.form.interfaces.rest

import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.dto.response.FormResponse
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.FormService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import java.net.URI

@RestController
class FormController(private val formService: FormService) {

    @PostMapping("/api/v1/forms", consumes = ["application/json"], produces = ["application/json"])
    fun createForm(@RequestBody command: FormCommand): ResponseEntity<FormResponse> {
        val formId: String = formService.create(command)
        val form: Form = formService.getById(formId)
        val response: FormResponse = FormResponse.from(form)
        val location: URI = fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(form.id)
            .toUri()
        return ResponseEntity
            .created(location)
            .body(response)
    }

    @GetMapping("/api/v1/forms/{formId}")
    fun getById(@PathVariable formId: String): ResponseEntity<FormResponse> {
        val form: Form = formService.getById(formId)
        val response: FormResponse = FormResponse.from(form)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/api/v1/forms/{formId}", consumes = ["application/json"], produces = ["application/json"])
    fun updateForm(
        @PathVariable formId: String,
        @RequestBody command: FormCommand
    ): ResponseEntity<FormResponse> {
        val formId: String = formService.update(formId, command)
        val form: Form = formService.getById(formId)
        val response: FormResponse = FormResponse.from(form)
        return ResponseEntity.ok(response)
    }
}
