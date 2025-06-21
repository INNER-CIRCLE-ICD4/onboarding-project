package survey.survey.config.response;


import survey.survey.config.ErrorType;

public record SurveyErrorResponse(
        String errorMessage,
        ErrorType errorType) {
}
