package fastcampus.inguk_onboarding.form.ui;

import fastcampus.inguk_onboarding.common.response.ApiResponse;
import fastcampus.inguk_onboarding.form.post.application.SurveyService;
import fastcampus.inguk_onboarding.form.post.application.dto.CreateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.post.application.dto.SurveyResponseDto;
import fastcampus.inguk_onboarding.form.post.application.dto.UpdateSurveyRequestDto;
import fastcampus.inguk_onboarding.form.response.application.interfaces.ResponseRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final ResponseRepository responseRepository;

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
    
    /**
     * 설문조사의 기존 응답 수 조회 (변경 전 영향도 확인용)
     */
    @GetMapping("/{surveyId}/response-count")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getResponseCount(@PathVariable Long surveyId) {
        try {
            int responseCount = responseRepository.findBySurveyId(surveyId).size();
            
            Map<String, Object> result = new HashMap<>();
            result.put("surveyId", surveyId);
            result.put("existingResponseCount", responseCount);
            result.put("canModifyFreely", responseCount == 0);
            
            if (responseCount > 0) {
                result.put("warning", "설문 항목을 변경하면 새로운 버전이 생성되며, 기존 " + responseCount + "개의 응답은 유지됩니다.");
            } else {
                result.put("message", "기존 응답이 없으므로 자유롭게 수정할 수 있습니다.");
            }
            
            return ResponseEntity.ok(ApiResponse.success("응답 수 조회가 완료되었습니다.", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("응답 수 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
