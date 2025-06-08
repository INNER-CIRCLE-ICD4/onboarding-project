package fc.innercircle.sanghyukonboarding.survey.domain.validator

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode

/**
 * [설문 입력값 유효성 검증]
 *
 * 설문 정보는 필수 입력값이며, 다음과 같은 유효성 검사를 수행합니다:
 * - title: 빈 문자열이 아니어야 하며, 최대 255자 이내
 * - description: 빈 문자열이 아니어야 하며, 최대 1000자 이내
 */
object SurveyValidator {
    // 설문 제목은 빈 문자열이 아니어야 하며, 최대 255자 이내여야 한다.
    fun validateTitle(title: String) {
        if (title.isBlank() || title.length > 255) {
            throw CustomException(ErrorCode.INVALID_SURVEY_TITLE.withArgs(title))
        }
    }

    // 설문 설명은 빈 문자열이 아니어야 하며, 최대 1000자 이내여야 한다.
    fun validateDescription(description: String) {
        if (description.isBlank() || description.length > 1000) {
            throw CustomException(ErrorCode.INVALID_SURVEY_DESCRIPTION.withArgs(description))
        }
    }
}
