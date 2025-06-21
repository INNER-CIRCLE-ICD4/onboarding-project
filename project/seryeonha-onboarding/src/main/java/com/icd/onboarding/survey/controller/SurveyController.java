package com.icd.onboarding.survey.controller;

import com.icd.onboarding.survey.dto.AnswerDto;
import com.icd.onboarding.survey.dto.SurveyDto;
import com.icd.onboarding.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{id}/answers")
    public void answer(@PathVariable Long id, @RequestBody AnswerDto.Create req) {
        surveyService.createAnswer(id, req);
    }

    // todo 작업완료해야함
    @GetMapping("/{id}/answers")
    public List<AnswerDto.Read> getAnswers(@PathVariable Long id) {
        return surveyService.getAnswers(id);
    }
}
