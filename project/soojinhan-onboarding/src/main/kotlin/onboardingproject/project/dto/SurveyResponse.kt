package onboardingproject.project.dto

/**
 * packageName : onboardingproject.project.dto
 * fileName    : SurveyResponse
 * author      : hsj
 * date        : 2025. 6. 21.
 * description :
 */
data class SurveyResponse(
    val title: String,
    val version: Int,
    val description: String?,
    val response: List<FieldResponseDto>
)

data class FieldResponseDto(
    val fieldId: String,
    val fieldName: String,
    val fieldResponse: List<String>
)