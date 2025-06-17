package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.controller;


import jakarta.validation.Valid;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitSurveyAnswersRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyUpdateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyWithAnswersResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service.ISurveyService;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request.BaseRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final ISurveyService surveyService;

    // 1. 설문조사 생성
    @ResponseBody
    @PostMapping("/createSurvey")
    public BaseResponse<SurveyResponseDto> createSurvey(
            @Valid @RequestBody BaseRequest<SurveyCreateRequest> request
    ) {
        return BaseResponse.OK(this.surveyService.createSurvey(request));
    }


    // 2. 설문조사 수정
    @PutMapping("/{surveyId}")
    public BaseResponse<SurveyResponseDto> updateSurvey(
            @PathVariable Long surveyId,
            @Valid @RequestBody BaseRequest<SurveyUpdateRequest> request
    ) {
        return BaseResponse.OK(this.surveyService.updateSurvey(surveyId, request));
    }

    // 3. 설문조사 응답 제출
    @PostMapping("/{surveyId}/submit")
    public BaseResponse<Void> submitSurveyAnswers(
            @PathVariable Long surveyId,
            @Valid @RequestBody BaseRequest<SubmitSurveyAnswersRequest> request
    ) {
        this.surveyService.submitSurveyAnswer(surveyId, request);
        return BaseResponse.OK();
    }

    // 4. 설문조사 응답 조회
    @GetMapping("/{surveyId}/responses")
    public BaseResponse<SurveyWithAnswersResponseDto> getSurveyWithAnswerResponses(
            @PathVariable Long surveyId
    ) {
        return BaseResponse.OK(this.surveyService.getSurveyWithAnswerResponses(surveyId));
    }


    // 5. 설문조사 상세
    @GetMapping("/{surveyId}")
    public BaseResponse<SurveyResponseDto> getSurveyResponse(
            @PathVariable Long surveyId
    ) {
        return BaseResponse.OK(this.surveyService.getSurveyResponse(surveyId));
    }


}
