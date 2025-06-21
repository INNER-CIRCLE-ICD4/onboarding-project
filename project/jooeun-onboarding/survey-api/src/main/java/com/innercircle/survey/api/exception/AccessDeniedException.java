package com.innercircle.survey.api.exception;

import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;

import java.util.Map;

/**
 * 접근 권한이 없을 때 발생하는 예외
 */
public class AccessDeniedException extends BusinessException {
    
    public AccessDeniedException(String surveyId, String userId) {
        super(ErrorCode.SURVEY_ACCESS_DENIED,
              String.format("설문조사에 대한 접근 권한이 없습니다: %s", surveyId),
              Map.of("surveyId", surveyId, "userId", userId));
    }

    public AccessDeniedException(String message) {
        super(ErrorCode.ACCESS_DENIED, message);
    }
}
