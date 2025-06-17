package com.innercircle.survey.api.exception;

import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;

import java.util.Map;

/**
 * 설문조사 응답을 찾을 수 없을 때 발생하는 예외
 */
public class ResponseNotFoundException extends BusinessException {
    
    public ResponseNotFoundException(String responseId) {
        super(ErrorCode.RESPONSE_NOT_FOUND,
              String.format("설문조사 응답을 찾을 수 없습니다: %s", responseId),
              Map.of("responseId", responseId));
    }
}
