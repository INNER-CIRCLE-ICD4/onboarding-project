package com.okdori.surveyservice.domain

/**
 * packageName    : com.okdori.surveyservice.domain
 * fileName       : ItemType
 * author         : okdori
 * date           : 2025. 6. 11.
 * description    :
 */
enum class ItemType(
    val displayName: String,
    val description: String,
    val allowMultiple: Boolean = false
) {
    SHORT_TEXT(
        displayName = "단답형",
        description = "짧은 텍스트 응답"
    ),

    LONG_TEXT(
        displayName = "장문형",
        description = "긴 텍스트 응답"
    ),

    SINGLE_SELECT(
        displayName = "단일 선택 리스트",
        description = "선택지 중 하나만 선택"
    ),

    MULTIPLE_SELECT(
        displayName = "다중 선택 리스트",
        description = "선택지 중 여러 개 선택 가능",
        allowMultiple = true
    );

    companion object {
        fun getTextTypes(): List<ItemType> =
            listOf(SHORT_TEXT, LONG_TEXT)

        fun getSelectTypes(): List<ItemType> =
            listOf(SINGLE_SELECT, MULTIPLE_SELECT)

        fun isTextType(type: ItemType): Boolean =
            type in getTextTypes()

        fun isSelectType(type: ItemType): Boolean =
            type in getSelectTypes()
    }
}
