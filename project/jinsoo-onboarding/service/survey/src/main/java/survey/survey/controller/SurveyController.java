package survey.survey.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import survey.survey.controller.request.survey.update.SurveyFormUpdateRequest;
import survey.survey.controller.request.survey.create.SurveyCreateRequest;
import survey.survey.controller.request.survey.update.SurveyUpdateRequest;
import survey.survey.service.SurveyService;
import survey.survey.service.response.SurveyResponse;

@RestController
@RequestMapping("/api/v1/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<SurveyResponse> create(@Valid @RequestBody SurveyCreateRequest request) {
        SurveyResponse response = surveyService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SurveyResponse> update(@Valid @RequestBody SurveyUpdateRequest request) {
        SurveyResponse response = surveyService.update(request);
        return ResponseEntity.ok(response);
    }
}
