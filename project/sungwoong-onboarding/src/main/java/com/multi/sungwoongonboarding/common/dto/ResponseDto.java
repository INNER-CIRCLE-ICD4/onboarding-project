package com.multi.sungwoongonboarding.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ResponseDto<T> {
    private final String status;
    private final String message;
    private final String errorCode;
    private final T data;
    private final T errorDetail;
}
