package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SurveyResponse {
    private Long id;
    private String title;
    private String description;
    private List<QuestionDto> questions;

    @Getter
    @AllArgsConstructor
    public static class QuestionDto {
        private Long id;

        private String name;
        private String description;
        private String type;
        private boolean required;
        private List<String> options;
    }
}
