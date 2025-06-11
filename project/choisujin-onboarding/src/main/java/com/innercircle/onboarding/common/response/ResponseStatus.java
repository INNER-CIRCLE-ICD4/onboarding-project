package com.innercircle.onboarding.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseStatus {

    SUCCESS(HttpStatus.OK.value(), "SUCCESS_CODE_0000", "SUCCESS"),

    // JsonUtils Exception
    JSON_CONVERT_FAIL(HttpStatus.BAD_REQUEST.value(), "FAIL_JSON_0001", "Json Convert Failed.");

    private int httpsStatus;
    private String code;
    private String message;

    ResponseStatus(int httpsStatus, String code, String message) {
        this.httpsStatus = httpsStatus;
        this.code = code;
        this.message = message;
    }

}
