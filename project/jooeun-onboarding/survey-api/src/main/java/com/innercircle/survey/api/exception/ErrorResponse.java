package com.innercircle.survey.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 에러 응답 DTO
 * 
 * API 에러 발생 시 일관된 형태의 응답을 제공합니다.
 */
@Schema(description = "에러 응답")
@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "VALIDATION_FAILED")
    private final String error;

    @Schema(description = "에러 메시지", example = "입력 데이터 검증에 실패했습니다.")
    private final String message;

    @Schema(description = "상세 에러 정보")
    private final Map<String, String> details;

    @Schema(description = "에러 발생 시각", example = "2024-01-15T10:30:00")
    private final LocalDateTime timestamp;
}
