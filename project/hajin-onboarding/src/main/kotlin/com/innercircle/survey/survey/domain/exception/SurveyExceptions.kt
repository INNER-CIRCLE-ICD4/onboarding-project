package com.innercircle.survey.survey.exception

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

class InvalidQuestionTypeException(message: String) : BusinessException(
    errorCode = ErrorCode.SURVEY_INVALID_ITEM_TYPE,
    message = message,
)

class MissingChoicesException(questionTitle: String) : BusinessException(
    errorCode = ErrorCode.SURVEY_MISSING_CHOICES,
    message = "선택형 항목에는 선택지가 필요합니다. (항목: $questionTitle)",
)

class DuplicateChoiceException(duplicates: Set<String>) : BusinessException(
    errorCode = ErrorCode.SURVEY_DUPLICATE_CHOICE,
    message = "중복된 선택지가 있습니다: ${duplicates.joinToString(", ")}",
)
