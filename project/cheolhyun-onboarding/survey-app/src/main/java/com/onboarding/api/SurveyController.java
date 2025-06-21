package com.onboarding.api;

import com.onboarding.api.dto.CreateSurveyReq;
import com.onboarding.api.service.SurveyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
    private SurveyService serveyService;

    @GetMapping
    public String getSurveys() {
        return "Hello World";
    }

    @PostMapping
    public ResponseEntity<String> createSurvey(@RequestBody CreateSurveyReq survey) {
        serveyService.createServey(survey);
        return ResponseEntity.ok("설문지가 작성되었습니다.");
    }
}
