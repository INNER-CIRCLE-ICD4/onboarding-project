package com.innercircle.onboarding.changzune_onboarding.survey.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponse {
    private Long questionId;
    private String questionName;
    private String answerValue;

    public AnswerResponse(String questionName, String answerValue) {
        this.questionId = questionId;
        this.questionName = questionName;
        this.answerValue = answerValue;
    }
}