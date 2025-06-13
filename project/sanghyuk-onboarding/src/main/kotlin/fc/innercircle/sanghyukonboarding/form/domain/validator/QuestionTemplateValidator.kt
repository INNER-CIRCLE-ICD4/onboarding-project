package fc.innercircle.sanghyukonboarding.form.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode

/**
 * [질문 입력값 유효성 검증]
 *
 * 설문 항목 정보는 필수 입력값이며, 다음과 같은 유효성 검사를 수행합니다:
 * - version: 0보다 큰 정수여야 하며, 기본값은 0
 */
object QuestionTemplateValidator {
    // 버전는 0보다 큰 정수여야 하며, 기본값은 0
    fun validateVersion(version: Long) {
        if (version < 0) {
            throw CustomException(ErrorCode.INVALID_QUESTION_VERSION.withArgs(version.toString()))
        }
    }
}
