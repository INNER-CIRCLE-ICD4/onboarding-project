package com.innercircle.onboarding.changzune_onboarding.survey.controller;

import com.innercircle.onboarding.changzune_onboarding.survey.domain.Survey;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyRequest;
import com.innercircle.onboarding.changzune_onboarding.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // 설문 생성 API: POST /api/surveys
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody @Valid SurveyRequest request) {
        Survey savedSurvey = surveyService.createSurvey(request);
        return new ResponseEntity<>(savedSurvey, HttpStatus.CREATED);
    }
}