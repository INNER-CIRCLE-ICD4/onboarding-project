package com.survey.soyoung_onboarding.common.response;

import com.survey.soyoung_onboarding.common.exception.ApiError;
import com.survey.soyoung_onboarding.common.exception.ApiErrorDetail;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    public static <T> ResponseEntity<ApiResponseDto<T>> success(T body) {
        return ResponseEntity.ok(new ApiResponseDto<>(body));
    }

    public static ResponseEntity<ApiResponseDto<Object>> success() {
        return ResponseEntity.ok(new ApiResponseDto<>(null));
    }

    public static ResponseEntity<ApiResponseDto<Object>> error(int code, String message, int status) {
        ApiResponseDto<Object> response = new ApiResponseDto<>(code, message);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<ApiResponseDto<Object>> error(ApiError error) {
        return error(error.getCode(), error.getMessage(), error.getStatus());
    }

    public static ResponseEntity<ApiResponseDto<Object>> error(ApiErrorDetail error) {
        return error(error.getCode(), error.getMessage(), error.getStatus());
    }
}

