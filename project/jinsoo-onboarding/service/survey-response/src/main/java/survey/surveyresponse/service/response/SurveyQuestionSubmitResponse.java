package survey.surveyresponse.service.response;

import survey.surveyresponse.entity.SurveyQuestionSubmit;

public record SurveyQuestionSubmitResponse(
        Long questionId,
        String answer) {
    public static SurveyQuestionSubmitResponse of(SurveyQuestionSubmit surveyQuestionSubmits) {
        return new SurveyQuestionSubmitResponse(
                surveyQuestionSubmits.getQuestionId(),
                surveyQuestionSubmits.getAnswer());
    }
}
