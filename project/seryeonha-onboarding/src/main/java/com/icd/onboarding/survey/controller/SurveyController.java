package com.icd.onboarding.survey.controller;

import com.icd.onboarding.survey.dto.SurveyDto;
import com.icd.onboarding.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping("")
    public void createSurvey(@RequestBody SurveyDto.Create req) {
        surveyService.createSurvey(req);
    }

    @PutMapping("/{id}")
    public void updateSurvey(@PathVariable Long id, @RequestBody SurveyDto.Update req) {
        surveyService.updateSurvey(id, req);
    }

    @GetMapping("/{id}")
    public SurveyDto.Read getSurvey(@PathVariable Long id) {
        return surveyService.getSurvey(id);
    }
}
