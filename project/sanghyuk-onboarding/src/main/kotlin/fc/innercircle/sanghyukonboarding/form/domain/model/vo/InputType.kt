package fc.innercircle.sanghyukonboarding.form.domain.model.vo

enum class InputType {
    TEXT, // 단답형
    LONG_TEXT, // 장문형
    RADIO, // 라디오 버튼
    CHECKBOX, // 체크박스
    SELECT, // 드롭다운,
    ;

    companion object {
        fun fromString(type: String): InputType = valueOf(type.uppercase())

        fun isValidType(type: String): Boolean = InputType.entries.any { it.name == type.uppercase() }
    }
}
