package com.innercircle.survey.api.controller;

import com.innercircle.survey.application.service.SurveyResponseService;
import com.innercircle.survey.common.dto.SurveyResponseDetailDto;
import com.innercircle.survey.common.dto.SurveyResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;

    //응답 제출
    @PostMapping("/{surveyId}/responses")
    public ResponseEntity<Void> submitSurveyResponse(
            @PathVariable UUID surveyId,
            @Valid @RequestBody SurveyResponseDto request
    ) {
        request.setSurveyId(surveyId);

        surveyResponseService.submitResponse(request);
        return ResponseEntity.ok().build();
    }

    //응답 조회
    @GetMapping("/responses/{responseId}")
    public ResponseEntity<SurveyResponseDetailDto> getResponse(@PathVariable UUID responseId) {
        SurveyResponseDetailDto result = surveyResponseService.getSurveyResponse(responseId);
        return ResponseEntity.ok(result);
    }
}
