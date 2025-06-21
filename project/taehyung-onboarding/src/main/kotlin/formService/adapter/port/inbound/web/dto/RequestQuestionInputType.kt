package formService.adapter.port.inbound.web.dto

enum class RequestQuestionInputType {
    SHORT_TEXT,
    LONG_TEXT,
    SINGLE_CHOICE,
    MULTI_CHOICE,
    ;

    companion object {
        fun isShortText(value: String) = SHORT_TEXT.name == value

        fun isLongText(value: String) = LONG_TEXT.name == value

        fun isSingleChoice(value: String) = SINGLE_CHOICE.name == value

        fun isMultiChoice(value: String) = MULTI_CHOICE.name == value
    }
}
