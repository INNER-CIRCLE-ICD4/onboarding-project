package onboarding.survey.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.CreateSurveyResponse;
import onboarding.survey.dto.UpdateSurveyRequest;
import onboarding.survey.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<CreateSurveyResponse> createSurvey(@Valid @RequestBody CreateSurveyRequest request) {
        Long id = surveyService.createSurvey(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateSurveyResponse(id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateSurvey(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSurveyRequest request
    ) {
        surveyService.updateSurvey(id, request);

        return ResponseEntity.noContent().build();
    }
}