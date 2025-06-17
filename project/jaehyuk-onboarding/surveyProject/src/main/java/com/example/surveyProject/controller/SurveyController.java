package com.example.surveyProject.controller;

import com.example.surveyProject.dto.SurveyCreateRequestDto;
import com.example.surveyProject.service.SurveyService;
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

    private final SurveyService surveyService;

    @PostMapping("/create")
    public ResponseEntity<Long> createSurvey(@RequestBody SurveyCreateRequestDto request) {
        Long id = surveyService.createSurvey(request);
        return ResponseEntity.ok(id);
    }

}
