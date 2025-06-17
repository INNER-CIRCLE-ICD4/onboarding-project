package icd.onboarding.surveyproject.survey.common.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import icd.onboarding.surveyproject.survey.controller.consts.ErrorCodes;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
		T data,
		ApiErrorResponse error
) {
	public static <T> ApiResponse<T> just (T data) {
		return new ApiResponse<>(data, null);
	}

	public static <T> ApiResponse<T> fromErrorCodes (ErrorCodes errorCodes) {
		final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(errorCodes.message, errorCodes.code);
		return new ApiResponse<>(null, apiErrorResponse);
	}
}
