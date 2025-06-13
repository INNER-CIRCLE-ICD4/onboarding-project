package survey.survey.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import survey.survey.service.SurveyFormService;
import survey.survey.controller.request.SurveyFormCreateRequest;
import survey.survey.service.response.SurveyFormResponse;

@RestController
@RequestMapping("/api/v1/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyFormService surveyFormService;

    @PostMapping
    public SurveyFormResponse create(@Valid @RequestBody SurveyFormCreateRequest request) {
        return surveyFormService.create(request);
    }
}
