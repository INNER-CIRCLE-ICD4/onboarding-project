package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubmitSurveyRequest {
    private Long surveyId;
    private List<AnswerRequest> answers;
}