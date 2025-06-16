package com.example.hyeongwononboarding.domain.survey.controller;

import com.example.hyeongwononboarding.domain.survey.dto.request.CreateSurveyRequest;
import com.example.hyeongwononboarding.domain.survey.dto.response.SurveyResponse;
import com.example.hyeongwononboarding.domain.survey.service.SurveyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 설문조사 관련 HTTP API 엔드포인트를 제공하는 컨트롤러입니다.
 * 설문 생성 등 RESTful 엔드포인트를 정의합니다.
 */
@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {
    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * 설문조사 생성 API
     * @param request 설문 생성 요청 DTO
     * @return 생성 결과(success, data)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createSurvey(@Valid @RequestBody CreateSurveyRequest request) {
        SurveyResponse response = surveyService.createSurvey(request);
        Map<String, Object> result = Map.of(
                "success", true,
                "data", response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 모든 설문조사 목록 조회 API
     * @return 설문조사 목록 응답
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSurveys() {
        List<SurveyResponse> responses = surveyService.getAllSurveys();
        Map<String, Object> result = Map.of(
                "success", true,
                "data", responses
        );
        return ResponseEntity.ok(result);
    }

    /**
     * ID로 설문조사 단건 조회 API
     * @param surveyId 조회할 설문조사 ID
     * @return 설문조사 상세 응답
     */
    @GetMapping("/{surveyId}")
    public ResponseEntity<Map<String, Object>> getSurveyById(
            @PathVariable @NotBlank(message = "설문조사 ID는 필수입니다.") String surveyId) {
        SurveyResponse response = surveyService.getSurveyById(surveyId);
        Map<String, Object> result = Map.of(
                "success", true,
                "data", response
        );
        return ResponseEntity.ok(result);
    }
}
