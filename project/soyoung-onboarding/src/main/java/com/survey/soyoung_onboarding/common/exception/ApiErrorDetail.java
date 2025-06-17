package com.survey.soyoung_onboarding.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorDetail {
    private Integer code;
    private String message;
    private Integer status;
}
