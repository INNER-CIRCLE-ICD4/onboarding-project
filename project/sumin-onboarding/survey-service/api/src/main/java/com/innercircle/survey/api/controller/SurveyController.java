package com.innercircle.survey.api.controller;

import com.innercircle.survey.application.service.SurveyService;
import com.innercircle.survey.common.dto.SurveyCreateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<?> createSurvey(@RequestBody @Valid SurveyCreateDto request) {
        UUID surveyId = surveyService.createSurvey(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("surveyId", surveyId, "message", "설문조사 생성 성공"));
    }
}
