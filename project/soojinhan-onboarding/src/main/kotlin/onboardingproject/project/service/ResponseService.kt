package onboardingproject.project.service

import onboardingproject.project.common.domain.ErrorMessage
import onboardingproject.project.common.exception.BadRequestException
import onboardingproject.project.domain.FieldOption
import onboardingproject.project.domain.FieldType
import onboardingproject.project.domain.Response
import onboardingproject.project.domain.SurveyField
import onboardingproject.project.dto.FieldResponseDto
import onboardingproject.project.dto.SaveFieldResponseRequest
import onboardingproject.project.dto.SurveyResponse
import onboardingproject.project.repository.response.ResponseRepository
import org.springframework.stereotype.Service

/**
 * packageName : onboardingproject.project.service
 * fileName    : ResponseService
 * author      : hsj
 * date        : 2025. 6. 21.
 * description :
 */
@Service
class ResponseService(
    private val responseRepository: ResponseRepository,
    private val surveyService: SurveyService
) {
    fun createResponse(saveRequests: List<SaveFieldResponseRequest>) {
        saveRequests.forEach { request ->
            val surveyField = surveyService.findSurveyFieldById(request.fieldId)
            val fieldOptions = mutableListOf<FieldOption>()
            // 필수 항목인데 값이 없을 경우 오류
            // 필수가 아닌데 값이 없을 경우 저장 제외
            when (surveyField.fieldType) {
                FieldType.SINGLE_OPTION -> {
                    if (request.fieldOptionIdList.isNullOrEmpty()) {
                        if (checkRequiredField(surveyField)) return@forEach
                    } else if (request.fieldOptionIdList.size > 1) {
                        throw BadRequestException(ErrorMessage.ONLY_ONE_OPTION_ALLOWED.message)
                    } else {
                        fieldOptions.addAll(surveyService.findFieldOptionsByIds(request.fieldOptionIdList))
                    }
                }

                FieldType.MULTI_OPTION -> {
                    if (request.fieldOptionIdList.isNullOrEmpty()) {
                        if (checkRequiredField(surveyField)) return@forEach
                    } else {
                        fieldOptions.addAll(surveyService.findFieldOptionsByIds(request.fieldOptionIdList))
                    }
                }

                FieldType.SHORT, FieldType.LONG -> {
                    if (request.textValue.isNullOrBlank()) {
                        if (checkRequiredField(surveyField)) return@forEach
                    }
                }
            }


            Response(
                textValue = request.textValue,
                surveyField = surveyField,
                fieldOptions = fieldOptions
            ).let { responseRepository.save(it) }
        }
    }

    fun getSurveyResponse(surveyId: String): List<SurveyResponse> {
        val surveyVersions = surveyService.findSurveyVersionBySurveyId(surveyId).sortedBy { it.version }
        return surveyVersions.map { surveyVersion ->
            val surveyFields = surveyService.findSurveyFieldBySurveyVersionId(surveyVersion.id)
            val responses = responseRepository.findAllBySurveyFieldIdIn(surveyFields.map { it.id }).sortedBy { it.surveyField.fieldOrder }
            SurveyResponse(
                title = surveyVersion.title,
                version = surveyVersion.version,
                description = surveyVersion.description,
                response = responses.map {
                    FieldResponseDto(
                        fieldId = it.surveyField.id,
                        fieldName = it.surveyField.fieldName,
                        fieldResponse = when (it.surveyField.fieldType) {
                            FieldType.SHORT, FieldType.LONG -> listOf(it.textValue!!)
                            FieldType.SINGLE_OPTION, FieldType.MULTI_OPTION -> it.fieldOptions!!.map { option -> option.optionValue }
                        }
                    )
                }
            )
        }
    }

    private fun checkRequiredField(surveyField: SurveyField): Boolean {
        if (surveyField.isRequired) throw BadRequestException(ErrorMessage.REQUIRED_FIELD_MISSING.message)
        else return true
    }
}