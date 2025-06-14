package onboardingproject.project.domain

/**
 * packageName : onboardingproject.project.domain
 * fileName    : FieldType
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
enum class FieldType(
    val type: String
) {
    SHORT("단답형"),
    LONG("장문형"),
    OPTION("단일 선택 옵션"),
    MULTI_OPTION("다중 선택 옵선")
}