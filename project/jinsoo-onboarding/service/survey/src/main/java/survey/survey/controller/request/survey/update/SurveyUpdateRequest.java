package survey.survey.controller.request.survey.update;

public record SurveyUpdateRequest(
        Long surveyId,
        SurveyFormUpdateRequest surveyFormUpdateRequest
) {
}
