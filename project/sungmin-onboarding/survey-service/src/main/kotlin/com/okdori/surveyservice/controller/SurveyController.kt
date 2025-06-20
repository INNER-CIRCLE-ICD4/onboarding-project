package com.okdori.surveyservice.controller

import com.okdori.surveyservice.dto.PagedResponse
import com.okdori.surveyservice.dto.Payload
import com.okdori.surveyservice.dto.ResponseSearchDto
import com.okdori.surveyservice.dto.SurveyCreateDto
import com.okdori.surveyservice.dto.SurveyAnswerCreateDto
import com.okdori.surveyservice.dto.SurveyAnswerResponseDto
import com.okdori.surveyservice.dto.SurveyResponseDto
import com.okdori.surveyservice.service.SurveyService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * packageName    : com.okdori.surveyservice.controller
 * fileName       : SurveyController
 * author         : okdori
 * date           : 2025. 6. 14.
 * description    :
 */

@RestController
@RequestMapping("/api/surveys")
class SurveyController(
    private val surveyService: SurveyService,
) {
    @PostMapping
    fun createSurvey(
        @RequestBody surveyCreateDto: SurveyCreateDto,
        request: HttpServletRequest,
    ): Payload<SurveyResponseDto> {
        return Payload(
            HttpStatus.CREATED,
            "설문조사가 생성되었습니다.",
            request.servletPath,
            surveyService.createSurvey(surveyCreateDto)
        )
    }

    @GetMapping("/{surveyId}")
    fun getSurvey(
        @PathVariable surveyId: String,
        request: HttpServletRequest,
    ): Payload<SurveyResponseDto> {
        return Payload(
            HttpStatus.OK,
            "설문조사 항목 조회가 완료되었습니다.",
            request.servletPath,
            surveyService.getSurvey(surveyId)
        )
    }

    @PostMapping("/{surveyId}/responses")
    fun createSurveyResponse(
        @PathVariable surveyId: String,
        @RequestBody surveyAnswerCreateDto: SurveyAnswerCreateDto,
        request: HttpServletRequest,
    ): Payload<SurveyAnswerResponseDto> {
        return Payload(
            HttpStatus.CREATED,
            "응답이 제출되었습니다.",
            request.servletPath,
            surveyService.createSurveyResponse(surveyId, surveyAnswerCreateDto)
        )
    }

    @GetMapping("/{surveyId}/responses")
    fun getSurveyResponse(
        @PathVariable surveyId: String,
        @RequestParam itemName: String? = null,
        @RequestParam answerValue: String? = null,
        @RequestParam responseUser: String? = null,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        request: HttpServletRequest,
    ): Payload<PagedResponse<ResponseSearchDto>> {
        return Payload(
            HttpStatus.OK,
            "응답 조회가 완료되었습니다.",
            request.servletPath,
            surveyService.getSurveyResponse(surveyId, itemName, answerValue, responseUser, page, size)
        )
    }
}
