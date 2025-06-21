package onboardingproject.project.dto

/**
 * packageName : onboardingproject.project.dto
 * fileName    : SaveFieldResponseRequest
 * author      : hsj
 * date        : 2025. 6. 21.
 * description :
 */
data class SaveFieldResponseRequest(
    val fieldId: String,
    val fieldOptionIdList: List<String>?,
    val textValue: String?
)