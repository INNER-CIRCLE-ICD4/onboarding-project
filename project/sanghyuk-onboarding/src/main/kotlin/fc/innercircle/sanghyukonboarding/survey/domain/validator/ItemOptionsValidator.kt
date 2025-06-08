package fc.innercircle.sanghyukonboarding.survey.domain.validator

import fc.innercircle.sanghyukonboarding.survey.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.survey.common.domain.exception.ErrorCode

/**
 * [설문 항목 옵션 입력값 유효성 검증]
 *
 * 설문 항목 옵션 정보는 필수 입력값이며, 다음과 같은 유효성 검사를 수행합니다:
 * - optionText: 빈 문자열이 아니어야 하며, 최대 255자 이내
 * - displayOrder: 0 이상의 정수여야 하며, 기본값은 0
 */
object ItemOptionsValidator {

    // 설문 항목 옵션 텍스트는 빈 문자열이 아니어야 하며, 최대 50자 이내여야 한다.
    fun validateOptionText(optionText: String) {
        if (optionText.isBlank() || optionText.length > 50) {
            throw CustomException(ErrorCode.INVALID_ITEM_OPTION_TEXT.withArgs(optionText))
        }
    }

    // 항목 순서는 0 이상의 정수여야 하며, 기본값은 0
    fun validateDisplayOrder(displayOrder: Int) {
        if (displayOrder < 0) {
            throw CustomException(ErrorCode.INVALID_ITEM_OPTION_DISPLAY_ORDER.withArgs(displayOrder.toString()))
        }
    }
}
