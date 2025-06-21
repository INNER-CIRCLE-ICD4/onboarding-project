package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class SurveyResponseRequest {

    @NotNull
    private Long surveyId;

    @Size(min = 1)
    private List<AnswerRequest> answers;
}