package onboardingproject.project.service

import onboardingproject.project.common.domain.ErrorMessage
import onboardingproject.project.common.exception.BadRequestException
import onboardingproject.project.domain.*
import onboardingproject.project.dto.SaveSurveyRequest
import onboardingproject.project.repository.fieldOption.FieldOptionRepository
import onboardingproject.project.repository.survey.SurveyRepository
import onboardingproject.project.repository.surveyField.SurveyFieldRepository
import onboardingproject.project.repository.surveyVersion.SurveyVersionRepository
import org.springframework.stereotype.Service

/**
 * packageName : onboardingproject.project.service
 * fileName    : SurveyService
 * author      : hsj
 * date        : 2025. 6. 17.
 * description :
 */
@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
    private val surveyVersionRepository: SurveyVersionRepository,
    private val surveyFieldRepository: SurveyFieldRepository,
    private val fieldOptionRepository: FieldOptionRepository
) {
    fun createSurvey(surveyRequest: SaveSurveyRequest) {
        // 설문조사 저장
        val survey = surveyRepository.save(
            Survey(
                title = surveyRequest.title,
                description = surveyRequest.description
            )
        )

        // 설문조사 버전 저장
        val surveyVersion = surveyVersionRepository.save(
            SurveyVersion(
                title = survey.title,
                description = survey.description,
                survey = survey
            )
        )

        // 선택 항목 리스트 저장
        surveyRequest.surveyFields.forEach { fieldRequest ->
            val fieldOptions: List<FieldOption>? =
                // 선택형인 경우 선택 옵션 리스트 저장
                if (fieldRequest.type == FieldType.SINGLE_OPTION || fieldRequest.type == FieldType.MULTI_OPTION) {
                    fieldRequest.fieldOptions?.map {
                        fieldOptionRepository.save(FieldOption(optionValue = it))
                    } ?: throw BadRequestException(ErrorMessage.OPTION_REQUIRED.message)
                } else null

            surveyFieldRepository.save(
                SurveyField(
                    fieldName = fieldRequest.fieldName,
                    description = fieldRequest.fieldDescription,
                    fieldType = fieldRequest.type,
                    isRequired = fieldRequest.isRequired,
                    fieldOrder = fieldRequest.order,
                    surveyVersion = surveyVersion,
                    fieldOptions = fieldOptions
                )
            )
        }
    }
}