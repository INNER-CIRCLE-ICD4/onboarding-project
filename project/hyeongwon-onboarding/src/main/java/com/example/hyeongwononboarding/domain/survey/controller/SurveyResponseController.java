package com.example.hyeongwononboarding.domain.survey.controller;

import com.example.hyeongwononboarding.domain.survey.dto.request.SubmitSurveyResponseRequest;
import com.example.hyeongwononboarding.domain.survey.dto.response.SubmitSurveyResponseResponse;
import com.example.hyeongwononboarding.domain.survey.service.SurveyResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 설문 응답 제출 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/surveys/{surveyId}/responses")
@RequiredArgsConstructor
public class SurveyResponseController {
    private final SurveyResponseService surveyResponseService;

    /**
     * 설문 응답 제출
     */
    @PostMapping
    public ResponseEntity<SubmitSurveyResponseResponse> submitSurveyResponse(
            @PathVariable("surveyId") String surveyId,
            @Valid @RequestBody SubmitSurveyResponseRequest request
    ) {

        SubmitSurveyResponseResponse response = surveyResponseService.submitSurveyResponse(surveyId, request);
        return ResponseEntity.ok(response);
    }
}
