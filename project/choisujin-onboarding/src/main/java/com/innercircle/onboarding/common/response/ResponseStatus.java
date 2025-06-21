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
    , NOT_FOUND_DATA(HttpStatus.BAD_REQUEST.value(), "FAIL_0005", "No Data Found.")

    // NotMatchQuestionAnswerCountException - Question and Answer Count Mismatch
    , NOT_MATCH_QUESTION_ANSWER_COUNT(HttpStatus.BAD_REQUEST.value(), "FAIL_0006", "The number of answers does not match the number of questions.")
    , ANSWER_COUNT_MISMATCH(HttpStatus.BAD_REQUEST.value(), "FAIL_0007", "Please check the number of answers.")
    , REQUIRED_QUESTION_NOT_ANSWERED(HttpStatus.BAD_REQUEST.value(), "FAIL_0008", "Required question was not answered.");
    private int httpsStatus;
    private String code;
    private String message;

    ResponseStatus(int httpsStatus, String code, String message) {
        this.httpsStatus = httpsStatus;
        this.code = code;
        this.message = message;
    }

}
