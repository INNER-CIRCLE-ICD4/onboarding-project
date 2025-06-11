package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.controller;


import jakarta.validation.Valid;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyCreateResponse;
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

    // Create
    @ResponseBody
    @PostMapping("/createSurvey")
    public BaseResponse<SurveyCreateResponse> createSurvey(
            @Valid @RequestBody BaseRequest<SurveyCreateRequest> request
    ) {
        return BaseResponse.OK(this.surveyService.createSurvey(request));
    }


}
