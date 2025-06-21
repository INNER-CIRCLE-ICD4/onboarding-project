package com.innercircle.survey.api.exception;

import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;

import java.util.Map;

/**
 * 중복된 응답 제출 시 발생하는 예외
 */
public class DuplicateResponseException extends BusinessException {
    
    public DuplicateResponseException(String surveyId, String respondentInfo) {
        super(ErrorCode.RESPONSE_DUPLICATE,
              String.format("이미 응답을 제출한 응답자입니다: %s", respondentInfo),
              Map.of("surveyId", surveyId, "respondentInfo", respondentInfo));
    }
}
