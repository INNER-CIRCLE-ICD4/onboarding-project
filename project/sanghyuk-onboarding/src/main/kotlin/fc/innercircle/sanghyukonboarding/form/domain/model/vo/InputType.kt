package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode

enum class InputType {
    TEXT, // 단답형
    LONG_TEXT, // 장문형
    RADIO, // 라디오 버튼
    CHECKBOX, // 체크박스
    SELECT, // 드롭다운,
    ;

    companion object {
        fun isValidTypeIgnoreCase(type: String): Boolean = InputType.entries.any { it.name == type.uppercase() }
        fun validateOrThrows(type: String) {
            if (!isValidTypeIgnoreCase(type)) {
                throw CustomException(ErrorCode.INVALID_QUESTION_INPUT_TYPE.withArgs(type))
            }
        }

        fun valueOrThrows(type: String): InputType {
            this.validateOrThrows(type)
            return InputType.valueOf(
                type.uppercase()
            )
        }
    }
}
