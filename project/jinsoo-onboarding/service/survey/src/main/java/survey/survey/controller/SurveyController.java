package survey.survey.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import survey.survey.controller.request.SurveyFormUpdateRequest;
import survey.survey.service.SurveyService;
import survey.survey.controller.request.SurveyFormCreateRequest;
import survey.survey.service.response.SurveyFormResponse;
import survey.survey.service.response.SurveyFormUpdateResponse;

@RestController
@RequestMapping("/api/v1/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<SurveyFormResponse> create(@Valid @RequestBody SurveyFormCreateRequest request) {
        SurveyFormResponse response = surveyService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{surveyFormId}")
    public ResponseEntity<SurveyFormUpdateResponse> update(@Valid @RequestBody SurveyFormUpdateRequest request,
                                                                    @PathVariable Long surveyFormId) {
        SurveyFormUpdateResponse response = surveyService.update(request, surveyFormId);
        return ResponseEntity.ok(response);

    }
}
