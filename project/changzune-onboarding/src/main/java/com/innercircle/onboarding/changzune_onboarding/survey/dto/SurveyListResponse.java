package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SurveyListResponse {
    private Long id;
    private String title;
    private String description;
    private int questionCount;
}