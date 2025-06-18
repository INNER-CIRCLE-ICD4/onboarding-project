package icd.onboarding.surveyproject.survey.common.controller;

import icd.onboarding.surveyproject.survey.controller.consts.ErrorCodes;
import icd.onboarding.surveyproject.survey.controller.exception.CommonSurveyHttpException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;

@RestControllerAdvice
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidationExceptions (
			MethodArgumentNotValidException exception
	) {
		final String message = exception.getBindingResult()
										.getAllErrors()
										.get(0)
										.getDefaultMessage();
		final ApiResponse<Void> body = ApiResponse.fromMessageAndCode(message, "METHOD_ARGS_INVALID");
		final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

		return ResponseEntity.badRequest()
							 .contentType(contentType)
							 .body(body);
	}
}
