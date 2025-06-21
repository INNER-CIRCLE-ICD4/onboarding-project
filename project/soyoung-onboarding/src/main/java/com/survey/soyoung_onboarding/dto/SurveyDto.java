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

    public void validate(BindingResult bindingResult) {

        ValidationUtils.rejectIfEmpty(bindingResult, "title", "survey.title.empty");

        if (questions == null || questions.size() < 1 || questions.size() > 10) {
            bindingResult.rejectValue("questions", "survey.questions.invalidSize", "설문 항목은 1~10개 사이여야 합니다.");
            return;
        }

        for (QuestionDto questionDto : questions) {
            questionDto.validate_add(bindingResult);
        }
    }
}
