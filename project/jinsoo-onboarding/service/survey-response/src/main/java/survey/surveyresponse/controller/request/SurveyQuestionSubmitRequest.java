package survey.surveyresponse.controller.request;

public record SurveyQuestionSubmitRequest(
        Long surveyQuestionId,
        String answer
) {
}
