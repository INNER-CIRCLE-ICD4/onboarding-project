package fastcampus.inguk_onboarding.common.exception;

public class InvalidSurveyItemCountException extends SurveyException {
    
    public InvalidSurveyItemCountException(int count) {
        super("설문 받을 항목은 1개 ~ 10개까지 포함할 수 있습니다. 현재 항목 수: " + count);
    }
} 