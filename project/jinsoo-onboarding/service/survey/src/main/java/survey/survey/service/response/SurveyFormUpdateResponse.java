package survey.survey.service.response;


public record SurveyFormUpdateResponse(
        Long surveyFormId,
        boolean changed
) {
}

