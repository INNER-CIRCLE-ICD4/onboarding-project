package fastcampus.inguk_onboarding.form.ui;

import fastcampus.inguk_onboarding.common.response.ApiResponse;
import fastcampus.inguk_onboarding.form.post.application.SurveyService;
import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.SurveyResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<ApiResponse<SurveyResponseDto>> createSurvey(@Valid @RequestBody CreateSurveyRequestDto dto) {
        SurveyResponseDto surveyResponse = surveyService.createSurvey(dto);
        return ResponseEntity.ok(ApiResponse.success(surveyResponse));
    }
}
