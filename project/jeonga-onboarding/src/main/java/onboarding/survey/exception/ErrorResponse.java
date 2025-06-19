package onboarding.survey.exception;

public record ErrorResponse(
        String code,
        String message) {
}