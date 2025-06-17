package onboardingproject.project.domain

/**
 * packageName : onboardingproject.project.domain
 * fileName    : FieldType
 * author      : hsj
 * date        : 2025. 6. 14.
 * description : 설문 받을 항목의 입력 형태
 */
enum class FieldType(
    val value: String
) {
    SHORT("단답형"),
    LONG("장문형"),
    SINGLE_OPTION("단일 선택 옵션"),
    MULTI_OPTION("다중 선택 옵선")
}