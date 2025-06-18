package com.okdori.surveyservice.controller

import com.okdori.surveyservice.dto.Payload
import com.okdori.surveyservice.dto.SurveyCreateDto
import com.okdori.surveyservice.dto.SurveyResponseDto
import com.okdori.surveyservice.service.SurveyService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
}
