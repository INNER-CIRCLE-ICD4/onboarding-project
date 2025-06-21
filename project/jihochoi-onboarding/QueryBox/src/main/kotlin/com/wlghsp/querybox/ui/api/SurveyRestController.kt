package com.wlghsp.querybox.ui.api

import com.wlghsp.querybox.application.SurveyService
import com.wlghsp.querybox.domain.survey.Survey
import com.wlghsp.querybox.ui.dto.ApiResponse
import com.wlghsp.querybox.ui.dto.SurveyCreateRequest
import com.wlghsp.querybox.ui.dto.SurveyUpdateRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/surveys")
class SurveyRestController(
    private val surveyService: SurveyService,
) {

    @GetMapping("/{id}")
    fun getSurvey(@PathVariable id: Long): ResponseEntity<ApiResponse<Survey>> {
        val survey = surveyService.findSurveyWithQuestionsById(id)
        return ResponseEntity.ok(ApiResponse.success(survey))
    }

    @PostMapping
    fun createSurvey(@RequestBody request: SurveyCreateRequest): ResponseEntity<Long> {
        val id = surveyService.create(request)
        return ResponseEntity.ok(id)
    }

    @PatchMapping("{id}")
    fun updateSurvey(@PathVariable id: Long, @RequestBody request: SurveyUpdateRequest): ResponseEntity<Unit> {
        surveyService.update(id, request)
        return ResponseEntity.ok().build()
    }

}
