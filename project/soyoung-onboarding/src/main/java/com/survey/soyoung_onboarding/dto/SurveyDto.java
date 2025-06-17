package com.survey.soyoung_onboarding.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;

import java.util.List;

@Getter
@Setter
public class SurveyDto {

    private String id;
    private String title;
    private String description;
    private List<QuestionDto> questions;

    public void validate_add(BindingResult bindingResult) {
        ValidationUtils.rejectIfEmpty(bindingResult, "title", "survey.title.empty");
        for (QuestionDto questionDto : questions) {
            questionDto.validate_add(bindingResult);
        }
    }
}
