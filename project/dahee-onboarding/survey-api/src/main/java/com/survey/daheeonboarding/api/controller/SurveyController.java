package com.survey.daheeonboarding.api.controller;


import com.survey.common.dto.ResponseDto;
import com.survey.common.dto.ResponseRequest;
import com.survey.common.dto.SurveyRequest;
import com.survey.common.dto.SurveyResponseDto;
import com.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody @Validated SurveyRequest request) {
        SurveyResponseDto result = surveyService.createSurvey(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 설문 응답 제출 API
     */
    @PostMapping("/{surveyId}/responses")
    public ResponseEntity<ResponseDto> submitResponse(
            @PathVariable Long surveyId,
            @RequestBody @Validated ResponseRequest request) {
        ResponseDto dto = surveyService.submitResponse(surveyId, request);
        return ResponseEntity.ok(dto);
    }
}
