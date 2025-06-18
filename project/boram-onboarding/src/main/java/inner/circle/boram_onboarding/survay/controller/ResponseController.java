package inner.circle.boram_onboarding.survay.controller;

import inner.circle.boram_onboarding.survay.dto.SurveyResponseSubmitRequest;
import inner.circle.boram_onboarding.survay.dto.SurveyAnswerResponse;
import inner.circle.boram_onboarding.survay.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Survey Response", description = "설문 응답 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/surveys/{surveyId}/responses")
public class ResponseController {

    private final ResponseService responseService;

    @Operation(summary = "설문 응답 제출", description = "설문에 대한 응답을 제출합니다.")
    @PostMapping
    public ResponseEntity<Long> submitResponse(
            @Parameter(description = "응답할 설문 ID") @PathVariable Long surveyId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "응답 데이터")
            @RequestBody SurveyResponseSubmitRequest request) {
        Long responseId = responseService.submitResponse(surveyId, request);
        return ResponseEntity.ok(responseId);
    }

    @Operation(summary = "설문 응답 조회", description = "설문에 대한 모든 응답을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SurveyAnswerResponse>> getResponses(@PathVariable Long surveyId) {
        List<SurveyAnswerResponse> responses = responseService.getResponses(surveyId);
        return ResponseEntity.ok(responses);
    }

}
