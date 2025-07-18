package com.multi.sungwoongonboarding.common.exception.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class FieldErrorDto {
    private final String field;
    private final String message;
}
