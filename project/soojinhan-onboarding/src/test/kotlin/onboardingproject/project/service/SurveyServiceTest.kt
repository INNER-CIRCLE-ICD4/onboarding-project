package onboardingproject.project.service

import onboardingproject.project.*
import onboardingproject.project.common.domain.ErrorMessage
import onboardingproject.project.common.exception.BadRequestException
import onboardingproject.project.domain.FieldType
import onboardingproject.project.dto.SaveSurveyFieldRequest
import onboardingproject.project.dto.SaveSurveyRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


/**
 * packageName : onboardingproject.project.service
 * fileName    : SurveyServiceTest
 * author      : hsj
 * date        : 2025. 6. 19.
 * description :
 */
class SurveyServiceTest(
) {
    private val surveyRepository = FakeSurveyRepository()
    private val surveyVersionRepository = FakeSurveyVersionRepository()
    private val surveyFieldRepository = FakeSurveyFieldRepository()
    private val fieldOptionRepository = FakeFieldOptionRepository()
    private val responseRepository = FakeResponseRepository()
    private val surveyService = SurveyService(
        surveyRepository,
        surveyVersionRepository,
        surveyFieldRepository,
        fieldOptionRepository,
    )

    @AfterEach
    fun tearDown() {
        responseRepository.deleteAll()
        fieldOptionRepository.deleteAll()
        surveyFieldRepository.deleteAll()
        surveyVersionRepository.deleteAll()
        surveyRepository.deleteAll()
    }

    @Test
    fun `단일선택_옵션_항목이_있는_설문을_생성한다`() {

        // given
        val request = SaveSurveyRequest(
            title = "서비스 만족도 조사",
            description = "서비스에 대한 만족도를 조사합니다",
            surveyFields = listOf(
                SaveSurveyFieldRequest(
                    fieldName = "만족도",
                    fieldDescription = "당신의 만족도를 선택해주세요",
                    type = FieldType.SINGLE_OPTION,
                    isRequired = true,
                    order = 1,
                    fieldOptions = listOf("매우 만족", "보통", "불만족")
                )
            )
        )

        // when
        surveyService.createSurvey(request)

        // then
        assertThat(surveyRepository.saved).hasSize(1)
        assertThat(surveyVersionRepository.saved).hasSize(1)
        assertThat(surveyVersionRepository.saved.first().version).isEqualTo(1)

        assertThat(surveyFieldRepository.saved).hasSize(1)
        assertThat(fieldOptionRepository.saved).hasSize(3)

        val savedField = surveyFieldRepository.saved.first()
        assertThat(savedField.fieldName).isEqualTo("만족도")
        assertThat(savedField.fieldOptions)
            .extracting("optionValue")
            .containsExactlyInAnyOrder("매우 만족", "보통", "불만족")
    }

    @Test
    fun `선택형인데 옵션이 null이면 BadRequestException이 발생한다`() {
        // given
        val request = SaveSurveyRequest(
            title = "옵션 없음 테스트",
            description = "선택형인데 옵션이 없는 경우",
            surveyFields = listOf(
                SaveSurveyFieldRequest(
                    fieldName = "성별",
                    fieldDescription = "성별을 선택해주세요",
                    type = FieldType.SINGLE_OPTION, // 선택형
                    isRequired = true,
                    order = 1,
                    fieldOptions = null // ❗ 옵션 없음
                )
            )
        )

        // expect
        assertThatThrownBy { surveyService.createSurvey(request) }
            .isInstanceOf(BadRequestException::class.java)
            .hasMessage(ErrorMessage.OPTION_REQUIRED.message)
    }

    @Test
    fun `updateSurvey는 설문 제목과 설명을 수정하고 새 버전을 생성한다`() {
        // given
        val surveyRepository = FakeSurveyRepository()
        val surveyVersionRepository = FakeSurveyVersionRepository()
        val surveyFieldRepository = FakeSurveyFieldRepository()
        val fieldOptionRepository = FakeFieldOptionRepository()

        val surveyService = SurveyService(
            surveyRepository,
            surveyVersionRepository,
            surveyFieldRepository,
            fieldOptionRepository
        )

        // 기존 설문 및 1번 버전 저장
        val request = SaveSurveyRequest(
            title = "서비스 만족도 조사",
            description = "서비스에 대한 만족도를 조사합니다",
            surveyFields = listOf(
                SaveSurveyFieldRequest(
                    fieldName = "만족도",
                    fieldDescription = "당신의 만족도를 선택해주세요",
                    type = FieldType.SINGLE_OPTION,
                    isRequired = true,
                    order = 1,
                    fieldOptions = listOf("매우 만족", "보통", "불만족")
                )
            )
        )

        val surveyId = surveyService.createSurvey(request)

        // 수정 요청
        val updateRequest = SaveSurveyRequest(
            title = "수정된 제목",
            description = "수정된 설명",
            surveyFields = listOf(
                SaveSurveyFieldRequest(
                    fieldName = "성별",
                    fieldDescription = "성별을 선택하세요",
                    type = FieldType.SINGLE_OPTION,
                    isRequired = true,
                    order = 1,
                    fieldOptions = listOf("남자", "여자")
                )
            )
        )

        // when
        surveyService.updateSurvey(surveyId, updateRequest)

        // then
        val updatedSurvey = surveyRepository.saved.first()
        assertThat(updatedSurvey.title).isEqualTo("수정된 제목")
        assertThat(updatedSurvey.description).isEqualTo("수정된 설명")

        val versions = surveyVersionRepository.saved
        assertThat(versions).hasSize(2)
        assertThat(versions.maxOf { it.version }).isEqualTo(2)

        val fields = surveyFieldRepository.saved.filter { it.surveyVersion.version == 2 }
        assertThat(fields).hasSize(1)
        assertThat(fields.first().fieldOptions).hasSize(2)
    }
}