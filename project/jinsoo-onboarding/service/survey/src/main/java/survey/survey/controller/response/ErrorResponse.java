package survey.survey.controller.response;


import survey.common.exception.ErrorType;

public record ErrorResponse(
        String errorMessage,
        ErrorType errorType) {
}
