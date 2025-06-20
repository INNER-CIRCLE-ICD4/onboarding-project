package kr.innercircle.onboarding.survey.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyRequest
import kr.innercircle.onboarding.survey.dto.response.ApiResponse
import kr.innercircle.onboarding.survey.service.SurveyService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * packageName : kr.innercircle.onboarding.survey.controller
 * fileName    : SurveyController
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */

@RestController
@RequestMapping("/surveys")
class SurveyController(
    private val surveyService: SurveyService,
) {
    @PostMapping
    fun postSurvey(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestBody @Valid createSurveyRequest: CreateSurveyRequest
    ): ApiResponse {
        surveyService.createSurvey(createSurveyRequest)
        response.status = HttpStatus.CREATED.value()
        return ApiResponse(message = "설문조사가 생성되었습니다.")
    }

    @GetMapping
    fun getSurveys(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ApiResponse {
        val result = surveyService.getSurveysResponse()
        response.status = HttpStatus.OK.value()
        return ApiResponse(
            message = "설문조사 목록을 조회했습니다.",
            data = result
        )
    }
}