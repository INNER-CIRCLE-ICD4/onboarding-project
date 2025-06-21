package com.wlghsp.querybox.domain.survey

enum class QuestionType {
    SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE;

    // 단일 or 다중 선택 리스트 인지 확인
    fun isChoice(): Boolean = this == SINGLE_CHOICE || this == MULTIPLE_CHOICE

    fun validateQuestion(options: Options?) {
        when (this) {
            SHORT_TEXT, LONG_TEXT -> {
                require(isNullAndEmpty(options)) { "주관식 항목은 옵션을 가질 수 없습니다." }
            }
            SINGLE_CHOICE, MULTIPLE_CHOICE -> {
                require(isNotNullAndNotEmpty(options)) { "선택형 항목에는 옵션이 반드시 있어야 합니다." }
            }
        }
    }

    fun validateAnswer(answerValue: String?, selectedOptionIds: List<Long>?) {
        when (this) {
            SHORT_TEXT, LONG_TEXT -> {
                require(!answerValue.isNullOrBlank()) { "주관식 응답은 비어 있을 수 없습니다." }
            }
            SINGLE_CHOICE, MULTIPLE_CHOICE -> {
                require(!selectedOptionIds.isNullOrEmpty()) { "선택형 응답은 하나 이상 선택해야 합니다." }
            }
        }
    }

    private fun isNullAndEmpty(options: Options?): Boolean = options == null || options.isEmpty()

    private fun isNotNullAndNotEmpty(options: Options?): Boolean = options != null && options.isNotEmpty()

}
