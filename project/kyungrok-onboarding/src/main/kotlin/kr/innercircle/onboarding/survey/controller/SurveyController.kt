package kr.innercircle.onboarding.survey.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyResponseRequest
import kr.innercircle.onboarding.survey.dto.request.UpdateSurveyRequest
import kr.innercircle.onboarding.survey.dto.response.ApiResponse
import kr.innercircle.onboarding.survey.dto.response.GetSurveysAnswerResponse
import kr.innercircle.onboarding.survey.service.SurveyItemService
import kr.innercircle.onboarding.survey.service.SurveyResponseService
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

@Tag(name = "설문조사")
@RestController
@RequestMapping("/surveys")
class SurveyController(
    private val surveyService: SurveyService,
    private val surveyResponseService: SurveyResponseService,
    private val surveyItemService: SurveyItemService,
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

    @PutMapping("/{surveyId}")
    fun putSurveys(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @PathVariable surveyId: Long,
        @RequestBody @Valid updateSurveyRequest: UpdateSurveyRequest,
    ): ApiResponse {
        surveyService.updateSurvey(surveyId, updateSurveyRequest)
        response.status = HttpStatus.OK.value()
        return ApiResponse(message = "설문조사를 수정했습니다.")
    }

    @PostMapping("/{surveyId}/responses")
    fun postSurveyResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @PathVariable surveyId: Long,
        @RequestBody @Valid createSurveyResponseRequest: CreateSurveyResponseRequest
    ): ApiResponse {
        val survey = surveyService.getSurveyById(surveyId)
        surveyResponseService.createSurveyResponses(survey, createSurveyResponseRequest)
        response.status = HttpStatus.CREATED.value()
        return ApiResponse(message = "설문조사 항목에 대한 응답이 생성되었습니다.")
    }

    @GetMapping("/{surveyId}/responses")
    fun getSurveyResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @PathVariable surveyId: Long
    ): ApiResponse {
        val survey = surveyService.getSurveyById(surveyId)
        val surveyItemsResponses = surveyItemService.getSurveyItemsResponse(survey)
        val surveyResponse = surveyResponseService.getSurveyItemsAnswerResponse(survey)
        val result = GetSurveysAnswerResponse(survey, surveyItemsResponses, surveyResponse)
        response.status = HttpStatus.OK.value()
        return ApiResponse(
            message = "설문조사에 대한 전체 응답을 조회했습니다.",
            data = result
        )
    }
}