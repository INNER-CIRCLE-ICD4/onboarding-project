package onboardingproject.project.controller

import jakarta.servlet.http.HttpServletRequest
import onboardingproject.project.common.dto.ResponseDto
import onboardingproject.project.dto.SaveFieldResponseRequest
import onboardingproject.project.dto.SurveyResponse
import onboardingproject.project.service.ResponseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * packageName : onboardingproject.project.controller
 * fileName    : ResponseController
 * author      : hsj
 * date        : 2025. 6. 21.
 * description :
 */
@RestController
class ResponseController(
    private val responseService: ResponseService
) {

    /**
     * 응답 생성 API
     */
    @PostMapping("/surveys/{surveyId}/responses")
    fun createResponse(
        request: HttpServletRequest,
        @PathVariable surveyId: String,
        @RequestBody saveResponseRequest: List<SaveFieldResponseRequest>,
    ): ResponseDto<Unit> {
        return ResponseDto(
            path = request.servletPath,
            data = responseService.createResponse(saveResponseRequest)
        )
    }


    /**
     * 응답 조회 API
     */
    @GetMapping("/surveys/{surveyId}/responses")
    fun getResponse(
        request: HttpServletRequest,
        @PathVariable surveyId: String
    ): ResponseDto<List<SurveyResponse>> {
        return ResponseDto(
            path = request.servletPath,
            data = responseService.getSurveyResponse(surveyId)
        )
    }
}