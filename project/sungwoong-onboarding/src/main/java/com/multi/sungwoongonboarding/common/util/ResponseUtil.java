package com.multi.sungwoongonboarding.common.util;


import com.multi.sungwoongonboarding.common.dto.ResponseDto;
import com.multi.sungwoongonboarding.common.exception.ErrorCode;

import static com.multi.sungwoongonboarding.common.util.ResponseUtil.ResponseStatus.ERROR;
import static com.multi.sungwoongonboarding.common.util.ResponseUtil.ResponseStatus.SUCCESS;

public class ResponseUtil {

    public enum ResponseStatus {SUCCESS, ERROR}

    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.<T>builder()
                .status(SUCCESS.name())
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> error(ErrorCode errorCode) {
        return ResponseDto.<T>builder()
                .status(ERROR.name())
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static <T> ResponseDto<T> error(ErrorCode errorCode, T errorData) {
        return ResponseDto.<T>builder()
                .status(ERROR.name())
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .errorDetail(errorData)
                .build();
    }
}
