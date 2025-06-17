package com.survey.daheeonboarding.api.controller;


import com.survey.common.dto.*;
import com.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    /**
     * 설문 생성 API
     */
    @PostMapping
    public ResponseEntity<SurveyResponseDto> createSurvey(
            @RequestBody SurveyRequest request) {
        SurveyResponseDto result = surveyService.createSurvey(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 설문 응답 제출 API
     */
    @PostMapping("/{surveyId}/responses")
    public ResponseEntity<ResponseDto> submitResponse(
            @PathVariable Long surveyId,
            @RequestBody ResponseRequest request) {
        ResponseDto dto = surveyService.submitResponse(surveyId, request);
        return ResponseEntity.ok(dto);
    }

    /**
     * 설문 조회 API
     */
    @GetMapping("/api/surveys/{surveyId}/responses")
    public List<SurveyAnswerResponseDto> getSurveyResponses(@PathVariable Long surveyId) {
        return surveyService.getSurveyResponses(surveyId);
    }
}
