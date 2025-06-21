package survey.surveyresponse.config.response;


import survey.common.exception.ErrorType;

public record ErrorResponse(
        String errorMessage,
        ErrorType errorType) {
}
