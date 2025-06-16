package com.innercircle.onboarding.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseStatus {

    SUCCESS(HttpStatus.OK.value(), "SUCCESS_CODE_0000", "SUCCESS")

    // TODO. 나중에 code 값 개선필요.

    // JsonUtils Exception
    , JSON_CONVERT_FAIL(HttpStatus.BAD_REQUEST.value(), "FAIL_0001", "Json Convert Failed.")

    // MethodArgumentNotValidException - Dto Validation
    , VALIDATION_FAIL(HttpStatus.BAD_REQUEST.value(), "FAIL_0002", "Dto Validation Failed.")

    // HttpRequestMethodNotSupportedException - Method Not Supported
    , METHOD_NOT_SUPPORT(HttpStatus.BAD_REQUEST.value(), "FAIL_0003", "Method Not Supported.")

    // NotFoundException - URL Not Found
    , NOT_FOUND_URL(HttpStatus.BAD_REQUEST.value(), "FAIL_0004", "URL Not Found.")

    // NotFoundDataException - No Data Found
    , NOT_FOUND_DATA(HttpStatus.BAD_REQUEST.value(), "FAIL_0005", "No Data Found.");

    private int httpsStatus;
    private String code;
    private String message;

    ResponseStatus(int httpsStatus, String code, String message) {
        this.httpsStatus = httpsStatus;
        this.code = code;
        this.message = message;
    }

}
