package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service.exception.ContentValidationException;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.InsertFormRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service.SurveyFormService;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyFormController {
    private final SurveyFormService surveyFormService;

    @PostMapping
    public ResponseEntity<?> createSurvey(@Valid @RequestBody InsertFormRequest requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // @Size 위반 등 Bean Validation 오류를 ContentValidationException으로 변환
            bindingResult.getFieldErrors().forEach(error -> {
                throw new ContentValidationException(error.getField(), error.getDefaultMessage());
            });
        }

        SurveyForm surveyForm = surveyFormService.createSurveyForm(requestDto);
        return ResponseEntity.ok(surveyForm);
    }

} 
