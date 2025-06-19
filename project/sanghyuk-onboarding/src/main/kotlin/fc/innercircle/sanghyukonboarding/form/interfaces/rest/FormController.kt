package fc.innercircle.sanghyukonboarding.form.interfaces.rest

import fc.innercircle.sanghyukonboarding.form.application.CreateFormFacade
import fc.innercircle.sanghyukonboarding.form.application.EditFormFacade
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request.FormRequest
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.response.FormResponse
import fc.innercircle.sanghyukonboarding.form.service.port.FormQueryRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import java.net.URI

@RequestMapping(path = ["/api/v1/forms"])
@RestController
class FormController(
    private val createFormFacade: CreateFormFacade,
    private val editFormFacade: EditFormFacade,
    private val formQueryRepository: FormQueryRepository
) {

    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    fun createForm(@RequestBody command: FormRequest): ResponseEntity<Unit> {
        val formId: String = createFormFacade.create(command)
        val location: URI = fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(formId)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/{formId}", produces = ["application/json"])
    fun getById(@PathVariable formId: String): ResponseEntity<FormResponse> {
        val form: Form = formQueryRepository.getById(formId)
        val response: FormResponse = FormResponse.from(form)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{formId}", consumes = ["application/json"], produces = ["application/json"])
    fun editForm(
        @PathVariable formId: String,
        @RequestBody command: FormRequest
    ): ResponseEntity<Unit> {
        editFormFacade.edit(formId, command)
        return ResponseEntity.noContent().build()
    }
}
