package onboardingproject.project.common.domain

/**
 * packageName : onboardingproject.project.domain
 * fileName    : ErrorMessage
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
enum class ErrorMessage(
    val message: String
) {
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
    OPTION_REQUIRED("선택형 항목일 경우, 선택 리스트를 입력해야 합니다."),
    SURVEY_ID_NOT_FOUND("설문 정보를 찾을 수 없습니다."),
    SURVEY_FIELD_ID_NOT_FOUND("설문 항목 정보를 찾을 수 없습니다."),
    REQUIRED_FIELD_MISSING("필수 응답 항목이 누락되었습니다."),
    ONLY_ONE_OPTION_ALLOWED("단일 선택 항목은 하나의 옵션만 선택 가능합니다.")
    ;
}