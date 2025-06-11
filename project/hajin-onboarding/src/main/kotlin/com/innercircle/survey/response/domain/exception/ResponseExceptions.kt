package com.innercircle.survey.response.domain.exception

import com.innercircle.survey.common.exception.BusinessException
import com.innercircle.survey.common.exception.ErrorCode
import java.util.UUID

class ResponseNotFoundException(responseId: UUID) : BusinessException(
    errorCode = ErrorCode.RESPONSE_NOT_FOUND,
    message = "응답을 찾을 수 없습니다. (ID: $responseId)",
)

class InvalidAnswerException(reason: String) : BusinessException(
    errorCode = ErrorCode.RESPONSE_INVALID_ANSWER,
    message = "유효하지 않은 응답입니다: $reason",
)

class MissingRequiredAnswerException(missingQuestions: Set<UUID>) : BusinessException(
    errorCode = ErrorCode.RESPONSE_MISSING_REQUIRED,
    message = "필수 항목에 대한 응답이 누락되었습니다. 누락된 항목: ${missingQuestions.joinToString(", ")}",
)

class TextTooLongException(maxLength: Int, actualLength: Int) : BusinessException(
    errorCode = ErrorCode.RESPONSE_TEXT_TOO_LONG,
    message = "텍스트 응답이 너무 깁니다. (최대: ${maxLength}자, 입력: ${actualLength}자)",
)

class InvalidChoiceException(invalidChoices: Set<UUID>) : BusinessException(
    errorCode = ErrorCode.RESPONSE_INVALID_CHOICE,
    message = "유효하지 않은 선택지입니다: ${invalidChoices.joinToString(", ")}",
)

class MultipleChoiceNotAllowedException(questionId: UUID) : BusinessException(
    errorCode = ErrorCode.RESPONSE_MULTIPLE_CHOICE_NOT_ALLOWED,
    message = "단일 선택 항목에 여러 개의 선택지를 선택할 수 없습니다. (질문 ID: $questionId)",
)

class SurveyMismatchException(reason: String) : BusinessException(
    errorCode = ErrorCode.RESPONSE_SURVEY_MISMATCH,
    message = "응답이 설문조사 구조와 일치하지 않습니다: $reason",
)
