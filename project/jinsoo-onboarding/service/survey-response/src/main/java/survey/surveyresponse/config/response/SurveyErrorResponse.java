package survey.surveyresponse.config.response;


import survey.surveyresponse.config.ErrorType;

public record SurveyErrorResponse(
        String errorMessage,
        ErrorType errorType) {
}
