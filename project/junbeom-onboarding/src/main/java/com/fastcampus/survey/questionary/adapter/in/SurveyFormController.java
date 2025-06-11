package com.fastcampus.survey.questionary.adapter.in;

import com.fastcampus.survey.questionary.domain.model.SurveyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.survey.questionary.adapter.in.dto.InsertFormRequest;
import com.fastcampus.survey.questionary.application.SurveyFormService;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyFormController {
    private final SurveyFormService surveyFormService;

    @PostMapping
    public SurveyForm createSurvey(@RequestBody InsertFormRequest insertFormRequest) {
        return surveyFormService.createSurveyForm(insertFormRequest);
    }

} 