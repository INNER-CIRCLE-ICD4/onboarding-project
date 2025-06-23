package com.innercircle.survey.survey.domain.exception

import com.innercircle.survey.common.exception.BusinessException
import com.innercircle.survey.common.exception.ErrorCode
import java.util.UUID

class SurveyNotFoundException(surveyId: UUID) : BusinessException(
    errorCode = ErrorCode.SURVEY_NOT_FOUND,
    message = "설문조사를 찾을 수 없습니다. (ID: $surveyId)",
)

class SurveyItemLimitExceededException(currentCount: Int, maxCount: Int) : BusinessException(
    errorCode = ErrorCode.SURVEY_ITEM_LIMIT_EXCEEDED,
    message = "설문 항목 수가 제한을 초과했습니다. (현재: $currentCount, 최대: $maxCount)",
)

class SurveyChoiceLimitExceededException(currentCount: Int, maxCount: Int) : BusinessException(
    errorCode = ErrorCode.SURVEY_CHOICE_LIMIT_EXCEEDED,
    message = "선택지 수가 제한을 초과했습니다. (현재: $currentCount, 최대: $maxCount)",
)

class InvalidQuestionTypeException(type: String) : BusinessException(
    errorCode = ErrorCode.SURVEY_INVALID_ITEM_TYPE,
    message = "유효하지 않은 항목 타입입니다: $type",
)

class MissingChoicesException(questionTitle: String) : BusinessException(
    errorCode = ErrorCode.SURVEY_MISSING_CHOICES,
    message = "선택형 항목에는 선택지가 필요합니다. (항목: $questionTitle)",
)
