package com.fastcampus.survey.common.exception;

public class ExceptionConstants {
    public static final int VALIDATION_ERROR = 400;
    public static final int DUPLICATE_ERROR = 409;
    public static final int INTERNAL_ERROR = 500;

    public static final String VALIDATION_MESSAGE = "입력값이 유효하지 않습니다.";
    public static final String DUPLICATE_MESSAGE = "중복된 데이터가 존재합니다.";
    public static final String INTERNAL_MESSAGE = "서버 오류가 발생했습니다.";
}
