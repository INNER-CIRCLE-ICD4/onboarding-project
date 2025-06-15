package com.innercircle.survey.api.exception;

/**
 * 설문조사를 찾을 수 없을 때 발생하는 예외
 */
public class SurveyNotFoundException extends RuntimeException {
    
    public SurveyNotFoundException(String surveyId) {
        super("설문조사를 찾을 수 없습니다: " + surveyId);
    }
    
    public SurveyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
