package com.innercircle.onboarding.changzune_onboarding.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalExceptionResponse { //예외처리 공통화 하기
    private String message;
    private int status;
}