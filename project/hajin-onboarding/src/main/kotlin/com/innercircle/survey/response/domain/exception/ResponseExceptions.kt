package com.innercircle.survey.response.domain.exception

import com.innercircle.survey.common.exception.BusinessException
import com.innercircle.survey.common.exception.ErrorCode

class ResponseNotFoundException(
    message: String = "응답을 찾을 수 없습니다.",
) : BusinessException(ErrorCode.RESPONSE_NOT_FOUND, message)

class InvalidAnswerException(
    message: String,
) : BusinessException(ErrorCode.RESPONSE_INVALID_ANSWER, message)

class MissingRequiredAnswerException(
    message: String = "모든 필수 항목에 답변해야 합니다.",
) : BusinessException(ErrorCode.RESPONSE_MISSING_REQUIRED, message)

class TextTooLongException(
    maxLength: Int,
    message: String = "텍스트 응답은 최대 ${maxLength}자까지 입력할 수 있습니다.",
) : BusinessException(ErrorCode.RESPONSE_TEXT_TOO_LONG, message)

class InvalidChoiceException(
    message: String,
) : BusinessException(ErrorCode.RESPONSE_INVALID_CHOICE, message)

class MultipleChoiceNotAllowedException(
    message: String = "단일 선택 항목에는 하나의 선택지만 선택할 수 있습니다.",
) : BusinessException(ErrorCode.RESPONSE_MULTIPLE_CHOICE_NOT_ALLOWED, message)

class ResponseSurveyMismatchException(
    message: String,
) : BusinessException(ErrorCode.RESPONSE_SURVEY_MISMATCH, message)
