package survey.survey.config.response;


import survey.common.exception.ErrorType;

public record ErrorResponse(
        String errorMessage,
        ErrorType errorType) {
}
