package com.INNER_CIRCLE_ICD4.innerCircle.controller;


import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping
    public List<SurveyResponse> getAllSurveys() {
        return surveyService.findAll();
    }

    @GetMapping("/{id}")
    public SurveyResponse getSurveyById(@PathVariable UUID id) {
        return surveyService.findById(id);
    }
}