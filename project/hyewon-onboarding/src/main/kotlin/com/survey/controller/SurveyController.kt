package com.survey.controller

import com.survey.dto.ResponseRequest
import com.survey.dto.SurveyCreateRequest
import com.survey.dto.SurveyItemRequest
import com.survey.dto.SurveyOptionRequest
import com.survey.model.Response
import com.survey.model.Survey
import com.survey.model.SurveyItem
import com.survey.model.SurveyOption
import com.survey.service.SurveyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/surveys")
class SurveyController(
    private val surveyService: SurveyService
) {

//    설문 생성
    @PostMapping("/create")
    fun createSurvey(@RequestBody request: SurveyCreateRequest): ResponseEntity<Any> {
        val created = surveyService.createSurvey(request.title, request.description)
        return ResponseEntity.ok(created)
    }

//    전체 설문 조회
    @GetMapping
    fun getAllSurveys(): List<Survey> = surveyService.getAllSurveys()

//    모든 설문 항목 조회
    @GetMapping("/items")
    fun getAllSurveyItems(): List<SurveyItem> = surveyService.getAllSurveyItems()

//    설문 항목 저장
    @PostMapping("/items")
    fun createSurveyItem(@RequestBody request: SurveyItemRequest): ResponseEntity<SurveyItem> {
        val saved = surveyService.saveSurveyItem(request)
        return ResponseEntity.ok(saved)
    }

//    설문 항목 수정
    @PutMapping("/items/{surveyOptionId}")
    fun updateSurveyItem(
        @PathVariable surveyOptionId: UUID,
        @RequestBody updated: SurveyItemRequest
    ): ResponseEntity<SurveyItem> {
        val updatedItem = surveyService.updateSurveyItem(surveyOptionId, updated)
        return ResponseEntity.ok(updatedItem)
    }

//    특정 항목의 옵션 목록 조회
    @GetMapping("/items/{itemId}/options")
    fun getOptionsByItem(@PathVariable itemId: UUID): List<SurveyOption> =
        surveyService.getOptionsByItemId(itemId)

////    옵션 저장
//    @PostMapping("/options")
//    fun createSurveyOption(@RequestBody option: SurveyOptionRequest): ResponseEntity<SurveyOption> {
//        val saved = surveyService.saveSurveyOption(option)
//        return ResponseEntity.ok(saved)
//    }

//    설문 응답 저장
    @PostMapping("/responses")
    fun submitResponse(@RequestBody response: ResponseRequest): ResponseEntity<Response> {
        val saved = surveyService.saveResponse(response)
        return ResponseEntity.ok(saved)
    }

//    전체 응답 목록 조회
    @GetMapping("/responses")
    fun getAllResponses(): List<Response> = surveyService.getAllResponses()
}
