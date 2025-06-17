package icd.onboarding.surveyproject.survey.common.controller;

import icd.onboarding.surveyproject.survey.controller.exception.CommonSurveyHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.charset.StandardCharsets;

public class GlobalExceptionHandler {
	@ExceptionHandler(value = {CommonSurveyHttpException.class})
	public ResponseEntity<ApiResponse<Void>> handleCommonInventoryHttpException (
			CommonSurveyHttpException exception
	) {
		final ApiResponse<Void> body = ApiResponse.fromErrorCodes(exception.getErrorCodes());
		final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
		final HttpStatus status = exception.getHttpStatus();

		return ResponseEntity.status(status)
							 .contentType(contentType)
							 .body(body);
	}
}
