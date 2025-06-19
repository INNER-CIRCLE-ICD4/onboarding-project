package com.survey.choheeonboarding.api.controller;

import com.survey.choheeonboarding.api.dto.SurveyDto;
import com.survey.choheeonboarding.api.service.SurveySvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurveyCtrl {

    private final SurveySvc surveySvc;

    public SurveyCtrl(SurveySvc surveySvc) {
        this.surveySvc = surveySvc;
    }

    @PostMapping("/api/survey")
    public ResponseEntity<?> createSurvey(@RequestBody SurveyDto.CreateSurveyRequest survey) {
        surveySvc.createSurvey(survey);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
