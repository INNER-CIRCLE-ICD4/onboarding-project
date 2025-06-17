package com.innercircle.survey.api.exception;

import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;

import java.util.List;
import java.util.Map;

/**
 * 필수 질문 응답 누락 시 발생하는 예외
 */
public class RequiredAnswerMissingException extends BusinessException {
    
    public RequiredAnswerMissingException(List<String> missingQuestionTitles) {
        super(ErrorCode.RESPONSE_REQUIRED_MISSING,
              String.format("필수 질문에 대한 응답이 누락되었습니다: %s", String.join(", ", missingQuestionTitles)),
              Map.of("missingQuestions", missingQuestionTitles));
    }
}
