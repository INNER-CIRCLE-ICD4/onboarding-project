package onboardingproject.project.service

import onboardingproject.project.common.domain.ErrorMessage
import onboardingproject.project.common.exception.BadRequestException
import onboardingproject.project.common.exception.NotFoundException
import onboardingproject.project.domain.*
import onboardingproject.project.dto.SaveSurveyFieldRequest
import onboardingproject.project.dto.SaveSurveyRequest
import onboardingproject.project.repository.fieldOption.FieldOptionRepository
import onboardingproject.project.repository.survey.SurveyRepository
import onboardingproject.project.repository.surveyField.SurveyFieldRepository
import onboardingproject.project.repository.surveyVersion.SurveyVersionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
    fun createSurvey(surveyRequest: SaveSurveyRequest): String {
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
        saveSurveyField(surveyRequest.surveyFields, surveyVersion)

        return survey.id
    }


    @Transactional
    fun updateSurvey(surveyId: String, surveyRequest: SaveSurveyRequest) {
        // 설문조사 조회
        val survey = findSurveyById(surveyId)
        val lastSurveyVersion = surveyVersionRepository.findFirstBySurveyOrderByVersionDesc(survey).version

        // 업데이트
        survey.apply {
            this.title = surveyRequest.title
            this.description = surveyRequest.description
        }

        // 새로운 설문조사 버전 저장
        val newSurveyVersion = surveyVersionRepository.save(
            SurveyVersion(
                title = surveyRequest.title,
                description = surveyRequest.description,
                survey = survey,
                version = lastSurveyVersion + 1

            )
        )

        // 선택 항목 리스트 저장
        saveSurveyField(surveyRequest.surveyFields, newSurveyVersion)
    }

    private fun saveSurveyField(surveyFields: List<SaveSurveyFieldRequest>, newSurveyVersion: SurveyVersion) {
        surveyFields.forEach { fieldRequest ->
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
                    surveyVersion = newSurveyVersion,
                    fieldOptions = fieldOptions
                )
            )
        }
    }

    fun findSurveyById(surveyId: String): Survey {
        return surveyRepository.findByIdOrNull(surveyId) ?: throw NotFoundException(ErrorMessage.SURVEY_ID_NOT_FOUND.message)
    }

    fun findSurveyFieldById(fieldId: String): SurveyField {
        return surveyFieldRepository.findByIdOrNull(fieldId) ?: throw NotFoundException(ErrorMessage.SURVEY_FIELD_ID_NOT_FOUND.message)
    }

    fun findSurveyFieldBySurveyVersionId(versionId: String): List<SurveyField> {
        return surveyFieldRepository.findAllBySurveyVersionId(versionId)
    }

    fun findFieldOptionsByIds(optionIds: List<String>): List<FieldOption> {
        return fieldOptionRepository.findByIdIn(optionIds)

    }

    fun findSurveyVersionBySurveyId(surveyId: String): List<SurveyVersion> {
        return surveyVersionRepository.findAllBySurveyId(surveyId)
    }
}