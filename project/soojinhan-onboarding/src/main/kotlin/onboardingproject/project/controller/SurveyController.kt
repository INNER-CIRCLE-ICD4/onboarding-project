package onboardingproject.project.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import onboardingproject.project.common.dto.ResponseDto
import onboardingproject.project.dto.SaveSurveyRequest
import onboardingproject.project.service.SurveyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * packageName : onboardingproject.project.controller
 * fileName    : SurveyController
 * author      : hsj
 * date        : 2025. 6. 17.
 * description :
 */
@RestController
class SurveyController(
    private val surveyService: SurveyService

) {

    /**
     * 설문조사 생성 API
     */
    @PostMapping("/surveys")
    fun createSurvey(
        request: HttpServletRequest,
        @Valid @RequestBody surveyRequest: SaveSurveyRequest
    ): ResponseDto<Unit> {
        return ResponseDto(
            path = request.servletPath,
            data = surveyService.createSurvey(surveyRequest)
        )
    }


}