package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AnswerRequest {

    private List<AnswerRequest> answers;

    @NotNull
    private Long questionId;

    @NotBlank
    private String questionName;

    @NotBlank
    private String answerValue;

    public Long surveyId;
}