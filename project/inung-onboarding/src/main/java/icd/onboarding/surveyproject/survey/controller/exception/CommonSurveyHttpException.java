package icd.onboarding.surveyproject.survey.controller.exception;

import icd.onboarding.surveyproject.survey.controller.consts.ErrorCodes;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class CommonSurveyHttpException extends RuntimeException {
	@NotNull
	private final ErrorCodes errorCodes;

	@NotNull
	private final HttpStatus httpStatus;

	public CommonSurveyHttpException (
			@NotNull ErrorCodes errorCodes,
			@NotNull HttpStatus httpStatus
	) {
		this.errorCodes = errorCodes;
		this.httpStatus = httpStatus;
	}

	@NotNull
	public ErrorCodes getErrorCodes () {
		return errorCodes;
	}

	@NotNull
	public HttpStatus getHttpStatus () {
		return httpStatus;
	}
}
