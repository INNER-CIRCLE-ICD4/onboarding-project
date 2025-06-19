// com.INNER_CIRCLE_ICD4.innerCircle.dto.ErrorResponse.java
package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        int status,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(String message, int status) {
        return new ErrorResponse(message, status, LocalDateTime.now());
    }
}
