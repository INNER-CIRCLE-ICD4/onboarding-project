package com.example.surveyProject.controller;

import com.example.surveyProject.dto.SurveyDto;
import com.example.surveyProject.service.SurveyCreateService;
import com.example.surveyProject.service.SurveyUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyCreateService surveyCreateService;
    private final SurveyUpdateService surveyUpdateService;

    @PostMapping("/create")
    public ResponseEntity<?> createSurvey(@RequestBody SurveyDto request) {
        Long id = surveyCreateService.createSurvey(request);
        return ResponseEntity.ok(id);
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateSurvey(@RequestBody SurveyDto request) {
        Long id = surveyUpdateService.updateSurvey(request);
        return ResponseEntity.ok(id);
    }

}
