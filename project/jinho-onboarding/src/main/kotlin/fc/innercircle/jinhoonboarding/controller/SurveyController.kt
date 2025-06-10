package fc.innercircle.jinhoonboarding.controller

import fc.innercircle.jinhoonboarding.dto.CreateSurveyRequest
import fc.innercircle.jinhoonboarding.entity.Survey
import fc.innercircle.jinhoonboarding.service.SurveyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SurveyController(
    private val surveyService: SurveyService
) {

    @PostMapping("/survey")
    fun createSurvey(@RequestBody request: CreateSurveyRequest): ResponseEntity<Survey> {

        return ResponseEntity.ok(
            surveyService.createSurvey(request)
        )
    }

}