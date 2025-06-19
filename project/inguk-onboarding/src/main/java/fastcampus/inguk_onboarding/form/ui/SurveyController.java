package fastcampus.inguk_onboarding.form.ui;

import fastcampus.inguk_onboarding.common.response.ApiResponse;
import fastcampus.inguk_onboarding.form.post.application.SurveyService;
import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.SurveyResponseDto;
import fastcampus.inguk_onboarding.form.post.application.dto.UpdateSurveyRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    //create
    @PostMapping
    public ResponseEntity<ApiResponse<SurveyResponseDto>> createSurvey(@Valid @RequestBody CreateSurveyRequestDto dto) {
        SurveyResponseDto surveyResponse = surveyService.createSurvey(dto);
        return ResponseEntity.ok(ApiResponse.success(surveyResponse));
    }

    //update
    @PatchMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<SurveyResponseDto>> updateSurvey(
            @PathVariable Long surveyId,
            @Valid @RequestBody UpdateSurveyRequestDto dto) {
        SurveyResponseDto surveyResponse = surveyService.updateSurvey(surveyId, dto);
        return ResponseEntity.ok(ApiResponse.success(surveyResponse));
    }




}
