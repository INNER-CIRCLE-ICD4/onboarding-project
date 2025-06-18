package com.wlghsp.querybox.ui.api

import com.wlghsp.querybox.application.ResponseService
import com.wlghsp.querybox.ui.dto.ApiResponse
import com.wlghsp.querybox.ui.dto.ResponseCreateRequest
import com.wlghsp.querybox.ui.dto.ResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/response")
class ResponseRestController(
    private val responseService: ResponseService,
) {
    @PostMapping("/{surveyId}")
    fun submit(
        @PathVariable("surveyId") surveyId: Long,
        @RequestBody request: ResponseCreateRequest
    ): ResponseEntity<Unit> {
        responseService.submit(surveyId, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{surveyId}")
    fun getAll(@PathVariable("surveyId") surveyId: Long): ResponseEntity<ApiResponse<List<ResponseDto>>> {
        val responseDto = responseService.findAllBySurveyId(surveyId)
        return ResponseEntity.ok(ApiResponse.success(responseDto))
    }
}