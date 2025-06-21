package com.innercircle.survey.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 표준화된 에러 응답 DTO
 * 
 * 모든 API 에러에 대해 일관된 형태의 응답을 제공합니다.
 */
@Schema(description = "표준화된 에러 응답")
@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardErrorResponse {

    @Schema(description = "에러 추적 ID", example = "01HK123ABC456DEF789GHI012J")
    private final String errorId;

    @Schema(description = "에러 코드", example = "ERR_3001")
    private final String errorCode;

    @Schema(description = "에러 메시지", example = "설문조사를 찾을 수 없습니다.")
    private final String message;

    @Schema(description = "상세 에러 정보")
    private final Map<String, Object> details;

    @Schema(description = "유효성 검증 실패 목록")
    private final List<ValidationErrorDetail> validationErrors;

    @Schema(description = "에러 발생 시각", example = "2024-01-15T10:30:00")
    private final LocalDateTime timestamp;

    @Schema(description = "API 경로", example = "/api/surveys/01HK123ABC456DEF789GHI012J")
    private final String path;

    @Schema(description = "HTTP 메서드", example = "GET")
    private final String method;

    /**
     * 유효성 검증 에러 상세 정보
     */
    @Schema(description = "유효성 검증 에러 상세")
    @Getter
    @Builder
    public static class ValidationErrorDetail {
        
        @Schema(description = "필드명", example = "title")
        private final String field;
        
        @Schema(description = "거부된 값", example = "")
        private final Object rejectedValue;
        
        @Schema(description = "에러 메시지", example = "설문조사 제목은 필수입니다.")
        private final String message;
        
        @Schema(description = "에러 코드", example = "NotBlank")
        private final String code;
    }
}
