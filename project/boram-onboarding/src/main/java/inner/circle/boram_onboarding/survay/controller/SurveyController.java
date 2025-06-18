package inner.circle.boram_onboarding.survay.controller;

import inner.circle.boram_onboarding.survay.dto.SurveyCreateRequest;
import inner.circle.boram_onboarding.survay.dto.SurveyCreateResponse;
import inner.circle.boram_onboarding.survay.dto.SurveyUpdateRequest;
import inner.circle.boram_onboarding.survay.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(summary = "설문 생성", description = "새로운 설문조사를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SurveyCreateResponse> createSurvey(@RequestBody SurveyCreateRequest request) {
        SurveyCreateResponse response = surveyService.createSurvey(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "설문 수정", description = "기존 설문조사의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{surveyId}")
    public ResponseEntity<Void> updateSurvey(@PathVariable Long surveyId, @RequestBody SurveyUpdateRequest request) {
        surveyService.updateSurvey(surveyId, request);
        return ResponseEntity.ok().build();
    }
}

