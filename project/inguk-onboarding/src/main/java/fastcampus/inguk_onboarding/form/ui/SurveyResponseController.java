package fastcampus.inguk_onboarding.form.ui;

import fastcampus.inguk_onboarding.common.response.ApiResponse;
import fastcampus.inguk_onboarding.form.post.application.dto.SurveyResponseDto;
import fastcampus.inguk_onboarding.form.response.Response;
import fastcampus.inguk_onboarding.form.response.application.SurveyResponseService;
import fastcampus.inguk_onboarding.form.response.application.dto.ResponseSurveyRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;

    /**
     * 설문 응답 등록
     */
    @PostMapping("/{surveyVersionId}")
    public ResponseEntity<ApiResponse<Response>> answerSurvey(
            @PathVariable Long surveyVersionId,
            @RequestBody ResponseSurveyRequestDto requestDto) {
        
        try {
            log.info("설문 응답 등록 요청 - surveyVersionId: {}, requestDto: {}", surveyVersionId, requestDto);
            
            Response response = surveyResponseService.answerSurvey(surveyVersionId, requestDto);
            
            log.info("설문 응답 등록 성공 - responseId: {}", response.getId());
            
            return ResponseEntity.ok(ApiResponse.success("설문 응답이 등록되었습니다.", response));
            
        } catch (Exception e) {
            log.error("설문 응답 등록 실패", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("설문 응답 등록에 실패했습니다: " + e.getMessage()));
        }
    }

    /**
     * 설문 응답 조회
     */
    @GetMapping("/response/{responseId}")
    public ApiResponse<Response> getResponse(@PathVariable Long responseId) {
        log.info("설문 응답 조회 요청 - responseId: {}", responseId);
        
        Response response = surveyResponseService.getResponse(responseId);
        
        log.info("설문 응답 조회 성공 - responseId: {}", responseId);
        
        return ApiResponse.success("설문 응답 조회 성공", response);
    }
}
