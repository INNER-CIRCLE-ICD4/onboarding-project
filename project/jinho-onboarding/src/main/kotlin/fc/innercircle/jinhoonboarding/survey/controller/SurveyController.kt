package fc.innercircle.jinhoonboarding.survey.controller

import fc.innercircle.jinhoonboarding.survey.dto.CreateSurveyRequest
import fc.innercircle.jinhoonboarding.survey.domain.Survey
import fc.innercircle.jinhoonboarding.survey.service.SurveyService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SurveyController(
    private val surveyService: SurveyService
) {

    @PostMapping("/survey")
    fun createSurvey(@Valid @RequestBody request: CreateSurveyRequest): ResponseEntity<Survey> {
        return ResponseEntity.ok(
            surveyService.createSurvey(request)
        )
    }

    @PutMapping
    fun modifySurvey(request: CreateSurveyRequest): ResponseEntity<Survey> {
        return ResponseEntity.ok(
            surveyService.createSurvey(request)
        )
    }


}