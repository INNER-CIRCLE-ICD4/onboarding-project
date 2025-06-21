package com.onboarding.api;

import com.onboarding.api.dto.request.CreateSurveyReq;
import com.onboarding.api.dto.response.CreateSurveyRes;
import com.onboarding.api.dto.response.SearchSurvey;
import com.onboarding.api.service.SurveyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
    private SurveyService surveyService;

    @GetMapping
    public String getSurveys() {
        return "Hello World";
    }

    @PostMapping
    public ResponseEntity<CreateSurveyRes> createSurvey(@RequestBody CreateSurveyReq survey) {
        return ResponseEntity.ok(surveyService.createServey(survey));
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SearchSurvey> searchSurveyById(@PathVariable("surveyId") String surveyId) {
        return ResponseEntity.ok(surveyService.searchSurveyById(surveyId));
    }
}
