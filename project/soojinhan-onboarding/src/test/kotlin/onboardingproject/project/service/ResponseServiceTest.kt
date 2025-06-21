package onboardingproject.project.service

import onboardingproject.project.*
import onboardingproject.project.common.domain.ErrorMessage
import onboardingproject.project.common.exception.BadRequestException
import onboardingproject.project.domain.*
import onboardingproject.project.dto.SaveFieldResponseRequest
import onboardingproject.project.dto.SaveSurveyFieldRequest
import onboardingproject.project.dto.SaveSurveyRequest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * packageName : onboardingproject.project.service
 * fileName    : ResponseServiceTest
 * author      : hsj
 * date        : 2025. 6. 21.
 * description :
 */
class ResponseServiceTest {
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

    private val responseService = ResponseService(responseRepository, surveyService)
    private lateinit var survey: Survey
    private lateinit var surveyVersion: SurveyVersion
    private lateinit var singleField: SurveyField
    private lateinit var multiField: SurveyField
    private lateinit var fieldOptions: List<FieldOption>


    // 공통 테스트 데이터
    @BeforeEach
    fun setup() {
        // 공통 데이터 초기화
        survey = surveyRepository.save(Survey(id = "survey-1", title = "제목", description = "설명"))
        surveyVersion = surveyVersionRepository.save(SurveyVersion(title = "버전1 제목", description = "버전1 설명", survey = survey))

        fieldOptions = listOf(
            FieldOption(id = "opt-1", optionValue = "남자"),
            FieldOption(id = "opt-2", optionValue = "여자"),
            FieldOption(id = "opt-3", optionValue = "운동"),
            FieldOption(id = "opt-4", optionValue = "독서"),
            FieldOption(id = "opt-5", optionValue = "개발")
        )
        fieldOptionRepository.saveAll(fieldOptions)

        singleField = surveyFieldRepository.save(
            SurveyField(
                id = "field-1",
                fieldName = "성별",
                description = "성별을 입력하세요",
                fieldType = FieldType.SINGLE_OPTION,
                isRequired = true,
                fieldOrder = 1,
                surveyVersion = surveyVersion,
                fieldOptions = fieldOptions
            )
        )

        multiField = surveyFieldRepository.save(
            SurveyField(
                id = "field-2",
                fieldName = "취미",
                description = "취미를 입력하세요",
                fieldType = FieldType.MULTI_OPTION,
                isRequired = true,
                fieldOrder = 2,
                surveyVersion = surveyVersion,
                fieldOptions = fieldOptions
            )
        )
    }

    @Test
    fun `createResponse는 선택형 응답을 저장한다`() {

        // given
        val request = SaveFieldResponseRequest(
            fieldId = "field-1",
            fieldOptionIdList = listOf("opt-1"),
            textValue = null
        )

        // when
        responseService.createResponse(listOf(request))

        // then
        assertThat(responseRepository.saved).hasSize(1)
        val saved = (responseRepository.saved).first()
        assertThat(saved.surveyField.fieldName).isEqualTo("성별")
        assertThat(saved.fieldOptions?.map { it.optionValue }).containsExactly("남자")
    }

    @Test
    fun `선택형인데 옵션이 없으면 예외가 발생한다`() {
        // given
        val request = SaveFieldResponseRequest(
            fieldId = "field-1",
            fieldOptionIdList = null,
            textValue = null
        )

        // expect
        assertThatThrownBy { responseService.createResponse(listOf(request)) }
            .isInstanceOf(BadRequestException::class.java)
            .hasMessage(ErrorMessage.REQUIRED_FIELD_MISSING.message)
    }

    @Test
    fun `단일 선택 항목인데 여러개를 선택하면 예외가 발생한다`() {
        // given
        val request = SaveFieldResponseRequest(
            fieldId = "field-1",
            fieldOptionIdList = listOf("opt-1", "opt-2"),
            textValue = null
        )

        // expect
        assertThatThrownBy { responseService.createResponse(listOf(request)) }
            .isInstanceOf(BadRequestException::class.java)
            .hasMessage(ErrorMessage.ONLY_ONE_OPTION_ALLOWED.message)
    }

    @Test
    fun `getSurveyResponse는 설문 응답 결과를 반환한다`() {
        // given
        val request = listOf(
            SaveFieldResponseRequest(
                fieldId = "field-1",
                fieldOptionIdList = listOf("opt-1"),
                textValue = null
            ), SaveFieldResponseRequest(
                fieldId = "field-2",
                fieldOptionIdList = listOf("opt-3", "opt-4"),
                textValue = null
            )
        )
        responseService.createResponse(request)

        // when
        val result = responseService.getSurveyResponse(survey.id)

        // then
        assertThat(result).hasSize(1)
        val surveyResponse = result.first()
        assertThat(surveyResponse.title).isEqualTo("버전1 제목")
        assertThat(surveyResponse.response).hasSize(2)
        val fieldResponse = surveyResponse.response.first()
        assertThat(fieldResponse.fieldId).isEqualTo("field-1")
        assertThat(fieldResponse.fieldResponse).containsExactly("남자")
    }


    @Test
    fun `설문이 수정되어 버전이 추가된 경우 기존 응답 포함 모든 응답을 조회한다`() {
        // given
        // 첫 번째 버전에 응답 저장
        val requestV1 = listOf(
            SaveFieldResponseRequest(
                fieldId = "field-2",
                fieldOptionIdList = listOf("opt-3", "opt-4"),
                textValue = null
            )
        )
        responseService.createResponse(requestV1)

        // 버전2 추가
        val updateRequest = SaveSurveyRequest(
            title = "버전2 제목",
            description = "수정된 설명",
            surveyFields = listOf(
                SaveSurveyFieldRequest(
                    fieldName = "성별",
                    fieldDescription = "성별을 선택하세요",
                    type = FieldType.SINGLE_OPTION,
                    isRequired = true,
                    order = 1,
                    fieldOptions = listOf("남자", "여자")
                ),
                SaveSurveyFieldRequest(
                    fieldName = "지역",
                    fieldDescription = "사는 지역을 입력하세요",
                    type = FieldType.SHORT,
                    isRequired = true,
                    order = 2,
                    fieldOptions = null
                )
            )
        )
        surveyService.updateSurvey(survey.id, updateRequest)

        val newSurveyVersion = surveyVersionRepository.findFirstBySurveyOrderByVersionDesc(survey)
        val fieldIdV2 = surveyFieldRepository.findAllBySurveyVersionId(newSurveyVersion.id)

        // 두번째 버전의 응답 저장
        val requestV2 = listOf(
            SaveFieldResponseRequest(
                fieldId = fieldIdV2.sortedBy { it.fieldOrder }[1].id,
                fieldOptionIdList = null,
                textValue = "서울"
            )
        )
        responseService.createResponse(requestV2)

        // when
        val result = responseService.getSurveyResponse(survey.id)

        // then
        assertThat(result).hasSize(2) // 두 개의 버전 응답
            .extracting({ it.title }, { it.version })
            .containsExactlyInAnyOrder(
                tuple("버전1 제목", 1),
                tuple("버전2 제목", 2)
            )

        val firstResponse = result.first().response
        val secondResponse = result.last().response
        assertThat(firstResponse).hasSize(1)
        assertThat(firstResponse)
            .extracting({ it.fieldName }, { it.fieldResponse })
            .containsExactlyInAnyOrder(
                tuple("취미", listOf("운동", "독서"))
            )
        assertThat(secondResponse).hasSize(1)
        assertThat(secondResponse)
            .extracting({ it.fieldName }, { it.fieldResponse })
            .containsExactlyInAnyOrder(
                tuple("지역", listOf("서울")),
            )
    }
}

