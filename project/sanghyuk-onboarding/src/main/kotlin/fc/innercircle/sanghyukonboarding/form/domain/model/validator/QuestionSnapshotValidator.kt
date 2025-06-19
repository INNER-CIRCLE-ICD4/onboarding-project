package fc.innercircle.sanghyukonboarding.form.domain.model.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode

/**
 * [질문 스냅샷 입력값 유효성 검증]
 *
 * 질문 스냅샷 정보는 필수 입력값이며, 다음과 같은 유효성 검사를 수행합니다:
 * - title: 빈 문자열이 아니어야 하며, 최대 500자 이내
 * - description: 빈 문자열이 아니어야 하며, 최대 1000자 이내
 * - displayOrder: 0 이상의 정수여야 하며, 기본값은 0
 * - version: 0 이상의 정수여야 하며, 기본값은 0
 */
object QuestionSnapshotValidator {
    // 질문 스냅샷 제목은 빈 문자열이 아니어야 하며, 최대 500자 이내여야 한다.
    fun validateTitle(title: String) {
        if (title.isBlank() || title.length > 500) {
            throw CustomException(ErrorCode.INVALID_QUESTION_TITLE.withArgs(title))
        }
    }

    // 질문 스냅샷 설명은 빈 문자열이 아니어야 하며, 최대 1000자 이내여야 한다.
    fun validateDescription(description: String) {
        if (description.isNotBlank() && description.length > 1000) {
            throw CustomException(ErrorCode.INVALID_QUESTION_DESCRIPTION.withArgs(description))
        }
    }

    // 질문 스냅샷 버전은 0 이상의 정수여야 하며, 기본값은 0
    fun validateVersion(version: Long) {
        if (version < 0) {
            throw CustomException(ErrorCode.INVALID_QUESTION_SNAPSHOT_VERSION.withArgs(version.toString()))
        }
    }
}
