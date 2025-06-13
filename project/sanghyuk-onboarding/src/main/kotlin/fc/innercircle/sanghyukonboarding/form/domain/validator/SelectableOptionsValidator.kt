package fc.innercircle.sanghyukonboarding.form.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode

/**
 * [질문 옵션 입력값 유효성 검증]
 *
 * 질문 옵션 정보는 필수 입력값이며, 다음과 같은 유효성 검사를 수행합니다:
 * - value: 빈 문자열이 아니어야 하며, 최대 255자 이내
 * - displayOrder: 0 이상의 정수여야 하며, 기본값은 0
 */
object SelectableOptionsValidator {
    // 질문 옵션 텍스트는 빈 문자열이 아니어야 하며, 최대 50자 이내여야 한다.
    fun validateValue(value: String) {
        if (value.isBlank() || value.length > 50) {
            throw CustomException(ErrorCode.INVALID_ITEM_OPTION_TEXT.withArgs(value))
        }
    }

    // 항목 순서는 0 이상의 정수여야 하며, 기본값은 0
    fun validateDisplayOrder(displayOrder: Int) {
        if (displayOrder < 0) {
            throw CustomException(ErrorCode.INVALID_ITEM_OPTION_DISPLAY_ORDER.withArgs(displayOrder.toString()))
        }
    }
}
