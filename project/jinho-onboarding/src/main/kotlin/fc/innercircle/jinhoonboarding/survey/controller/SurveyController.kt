package fc.innercircle.jinhoonboarding.survey.controller

import fc.innercircle.jinhoonboarding.survey.dto.request.CreateSurveyRequest
import fc.innercircle.jinhoonboarding.survey.domain.Survey
import fc.innercircle.jinhoonboarding.survey.dto.response.SurveyResponse
import fc.innercircle.jinhoonboarding.survey.service.SurveyService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/survey/{surveyId}")
    fun findBySurveyId(@PathVariable surveyId: Long): ResponseEntity<SurveyResponse> {
        return ResponseEntity.ok(
            surveyService.getSurvey(surveyId)
        )
    }

    @PostMapping("/survey")
    fun createSurvey(@Valid @RequestBody request: CreateSurveyRequest): ResponseEntity<SurveyResponse> {
        return ResponseEntity.ok(
            surveyService.createSurvey(request)
        )
    }

    @PutMapping("/survey/{surveyId}")
    fun modifySurvey(request: CreateSurveyRequest): ResponseEntity<SurveyResponse> {
        return ResponseEntity.ok(
            surveyService.createSurvey(request)
        )
    }


}