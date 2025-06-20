package onboardingproject.project.dto

import jakarta.validation.constraints.Size
import onboardingproject.project.domain.FieldType
import onboardingproject.project.domain.SurveyField

/**
 * packageName : onboardingproject.project.dto
 * fileName    : SaveSurveyRequest
 * author      : hsj
 * date        : 2025. 6. 17.
 * description :
 */
data class SaveSurveyRequest(
    val title: String,
    val description: String,
    @field:Size(min = 1, max = 10) // 항목은 최대 10개로 제한
    val surveyFields: List<SaveSurveyFieldRequest>
)

data class SaveSurveyFieldRequest (
    val fieldName: String,
    val fieldDescription: String?,
    val type: FieldType,
    val isRequired: Boolean,
    val order: Int,
    @field:Size(min = 2, max = 10) // 선택 항목 리스트 최대 10개로 제한
    val fieldOptions: List<String>?
)