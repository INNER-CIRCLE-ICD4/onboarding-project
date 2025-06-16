package fc.innercircle.jinhoonboarding.survey.controller

import fc.innercircle.jinhoonboarding.common.dto.CommonResponse
import fc.innercircle.jinhoonboarding.survey.dto.request.SurveyRequest
import fc.innercircle.jinhoonboarding.survey.dto.response.SurveyResponse
import fc.innercircle.jinhoonboarding.survey.service.SurveyService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SurveyController(
    private val surveyService: SurveyService
) {

    // 설문조사 조회
    @GetMapping("/survey/{surveyId}")
    fun findBySurveyId(@PathVariable surveyId: Long): ResponseEntity<CommonResponse<SurveyResponse>> {
        return ResponseEntity.ok(CommonResponse(
            message = "설문 조회에 성공하였습니다.",
            data = surveyService.getSurvey(surveyId)
        ))
    }

    // 설문조사 생성
    @PostMapping("/survey")
    fun createSurvey(@Valid @RequestBody request: SurveyRequest): ResponseEntity<CommonResponse<SurveyResponse>> {
        return ResponseEntity.ok(CommonResponse(
            message = "설문조사를 생성 완료하였습니다.",
            data = surveyService.createSurvey(request)
        ))
    }

    // 설문조사 수정
    @PutMapping("/survey/{surveyId}")
    fun updateSurvey(@Valid @RequestBody request: SurveyRequest, @PathVariable surveyId: Long): ResponseEntity<CommonResponse<SurveyResponse>> {
        return ResponseEntity.ok(CommonResponse(
            message = "설문조사 수정을 완료하였습니다.",
            data = surveyService.updateSurvey(request, surveyId)
        ))
    }

    // 설문조사 삭제
    @DeleteMapping("/survey/{surveyId}")
    fun deleteSurvey(@PathVariable surveyId: Long): ResponseEntity<Void> {
        surveyService.deleteById(surveyId)
        return ResponseEntity.noContent().build()
    }


}