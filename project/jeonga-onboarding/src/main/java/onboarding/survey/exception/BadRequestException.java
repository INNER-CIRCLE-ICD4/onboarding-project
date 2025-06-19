package onboarding.survey.exception;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(ErrorCode.INVALID_INPUT_VALUE, message);
    }
}