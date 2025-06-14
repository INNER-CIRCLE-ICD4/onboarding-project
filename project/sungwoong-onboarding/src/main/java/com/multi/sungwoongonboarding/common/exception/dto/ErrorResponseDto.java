package com.multi.sungwoongonboarding.common.exception.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponseDto<T> {
    private final String errorCode;
    private final String errorMessage;
    private final List<T> errorDetails;
    private final LocalDateTime timestamp = LocalDateTime.now();

}
