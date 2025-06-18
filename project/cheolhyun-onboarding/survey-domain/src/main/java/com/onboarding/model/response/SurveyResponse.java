package com.onboarding.model.response;

import java.util.List;

public class SurveyResponse {
    String surveyId;
    OptionAnswers answers;

    public SurveyResponse(String surveyId, OptionAnswers answers) {
        this.surveyId = surveyId;
        this.answers = answers;
    }

    public SurveyResponse(String surveyId, List<OptionAnswer> answers) {
        this.surveyId = surveyId;
        this.answers = new OptionAnswers(answers);
    }
}
