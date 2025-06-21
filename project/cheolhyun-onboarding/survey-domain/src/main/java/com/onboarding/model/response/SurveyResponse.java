package com.onboarding.model.response;

import java.util.List;

public class SurveyResponse {
    private static final String INVALID_QUESTION_ID = "설문 조사 ID가 존재하지 않습니다.";

    private String surveyId;
    private OptionAnswers answers;

    public SurveyResponse(String surveyId) {
        validate(surveyId);

        this.surveyId = surveyId;
        this.answers = new OptionAnswers();
    }

    public SurveyResponse(String surveyId, List<OptionAnswer> answers) {
        this(surveyId, new OptionAnswers(answers));
    }

    public SurveyResponse(String surveyId, OptionAnswers answers) {
        validate(surveyId);

        this.surveyId = surveyId;
        this.answers = answers;
    }

    private void validate(String surveyId) {
        if(surveyId == null || surveyId.isBlank()) {
            throw new IllegalArgumentException(INVALID_QUESTION_ID);
        }
    }
}
