package onboarding.survey.controller;

import lombok.RequiredArgsConstructor;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.CreateSurveyResponse;
import onboarding.survey.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<CreateSurveyResponse> createSurvey(@RequestBody CreateSurveyRequest request) {
        Long id = surveyService.createSurvey(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateSurveyResponse(id));
    }
}