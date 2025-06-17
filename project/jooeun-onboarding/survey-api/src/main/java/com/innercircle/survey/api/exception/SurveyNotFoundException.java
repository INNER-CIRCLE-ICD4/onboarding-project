package com.innercircle.survey.api.exception;

import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;

import java.util.Map;

/**
 * 설문조사를 찾을 수 없을 때 발생하는 예외
 */
public class SurveyNotFoundException extends BusinessException {
    
    public SurveyNotFoundException(String surveyId) {
        super(ErrorCode.SURVEY_NOT_FOUND, 
              String.format("설문조사를 찾을 수 없습니다: %s", surveyId),
              Map.of("surveyId", surveyId));
    }
}
