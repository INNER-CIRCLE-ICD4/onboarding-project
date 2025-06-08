package fc.innercircle.sanghyukonboarding.survey.domain.validator

import fc.innercircle.sanghyukonboarding.survey.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.survey.common.domain.exception.ErrorCode

/**
 * [설문 항목 입력값 유효성 검증]
 *
 * 설문 항목 정보는 필수 입력값이며, 다음과 같은 유효성 검사를 수행합니다:
 * - question: 빈 문자열이 아니어야 하며, 최대 500자 이내
 * - description: 빈 문자열이 아니어야 하며, 최대 1000자 이내
 * - required: true/false 값이어야 하며, 기본값은 false
 * - displayOrder: 0 이상의 정수여야 하며, 기본값은 0
 */
object SurveyItemValidator {
    // 설문 항목 제목은 빈 문자열이 아니어야 하며, 최대 500자 이내여야 한다.
    fun validateQuestion(question: String) {
        if (question.isBlank() || question.length > 500) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_QUESTION.withArgs(question))
        }
    }

    // 설문 항목 설명은 빈 문자열이 아니어야 하며, 최대 1000자 이내여야 한다.
    fun validateDescription(description: String) {
        if (description.isBlank() || description.length > 1000) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_DESCRIPTION.withArgs(description))
        }
    }

    // 필수 여부는 true/false 값이어야 하며, 기본값은 false
    fun validateRequired(required: Boolean) {
        if (required !in listOf(true, false)) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_REQUIRED.withArgs(required.toString()))
        }
    }

    // 항목 순서는 0 이상의 정수여야 하며, 기본값은 0
    fun validateDisplayOrder(displayOrder: Int) {
        if (displayOrder < 0) {
            throw CustomException(ErrorCode.INVALID_SURVEY_ITEM_ORDER.withArgs(displayOrder.toString()))
        }
    }
}
