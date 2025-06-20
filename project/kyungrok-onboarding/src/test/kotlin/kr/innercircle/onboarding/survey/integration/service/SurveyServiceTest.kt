package kr.innercircle.onboarding.survey.integration.service

import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyRequest
import kr.innercircle.onboarding.survey.exception.InvalidSurveyItemTypeException
import kr.innercircle.onboarding.survey.repository.SurveyItemOptionRepository
import kr.innercircle.onboarding.survey.repository.SurveyItemRepository
import kr.innercircle.onboarding.survey.repository.SurveyRepository
import kr.innercircle.onboarding.survey.service.SurveyService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * packageName : kr.innercircle.onboarding.survey.integration.service
 * fileName    : SurveyServiceTest
 * author      : ckr
 * date        : 25. 6. 21.
 * description : SurveyService 통합 테스트
 */

@SpringBootTest
@Transactional
class SurveyServiceTest {

    @Autowired
    private lateinit var surveyService: SurveyService

    @Autowired
    private lateinit var surveyRepository: SurveyRepository

    @Autowired
    private lateinit var surveyItemRepository: SurveyItemRepository

    @Autowired
    private lateinit var surveyItemOptionRepository: SurveyItemOptionRepository

    @Test
    fun createSurvey_완전한_설문조사를_생성한다() {
        // given
        val createSurveyRequest = CreateSurveyRequest(
            name = "고객 만족도 조사",
            description = "서비스 개선을 위한 종합적인 설문조사입니다",
            surveyItems = listOf(
                CreateSurveyItemRequest(
                    name = "이름을 입력해주세요",
                    description = "실명으로 입력해주세요",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = emptyList(),
                    isRequired = true
                ),
                CreateSurveyItemRequest(
                    name = "성별을 선택해주세요",
                    description = "해당하는 성별을 선택해주세요",
                    inputType = SurveyItemInputType.SINGLE_CHOICE,
                    options = listOf(
                        CreateSurveyItemOptionRequest("남성"),
                        CreateSurveyItemOptionRequest("여성"),
                        CreateSurveyItemOptionRequest("선택안함")
                    ),
                    isRequired = true
                ),
                CreateSurveyItemRequest(
                    name = "관심사를 모두 선택해주세요",
                    description = "여러 개 선택 가능합니다",
                    inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                    options = listOf(
                        CreateSurveyItemOptionRequest("기술"),
                        CreateSurveyItemOptionRequest("예술"),
                        CreateSurveyItemOptionRequest("스포츠"),
                        CreateSurveyItemOptionRequest("여행")
                    ),
                    isRequired = false
                ),
                CreateSurveyItemRequest(
                    name = "추가 의견을 작성해주세요",
                    description = "자유롭게 의견을 작성해주세요",
                    inputType = SurveyItemInputType.LONG_TEXT,
                    options = emptyList(),
                    isRequired = false
                )
            )
        )

        // when
        val createdSurvey = surveyService.createSurvey(createSurveyRequest)

        // then
        // 설문조사 검증
        assertNotNull(createdSurvey.id)
        assertEquals("고객 만족도 조사", createdSurvey.name)
        assertEquals("서비스 개선을 위한 종합적인 설문조사입니다", createdSurvey.description)

        // 데이터베이스에서 실제 저장된 데이터 검증
        val savedSurvey = surveyRepository.findById(createdSurvey.id!!)
        assertNotNull(savedSurvey)
        assertEquals("고객 만족도 조사", savedSurvey.name)

        // 설문 항목들이 정확히 생성되었는지 검증
        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == createdSurvey.id }
        assertEquals(4, savedItems.size)

        val sortedItems = savedItems.sortedBy { it.orderNumber }

        // 첫 번째 항목 (SHORT_TEXT) 검증
        assertEquals("이름을 입력해주세요", sortedItems[0].name)
        assertEquals("실명으로 입력해주세요", sortedItems[0].description)
        assertEquals(SurveyItemInputType.SHORT_TEXT, sortedItems[0].inputType)
        assertEquals(true, sortedItems[0].isRequired)
        assertEquals(1, sortedItems[0].orderNumber)

        // 두 번째 항목 (SINGLE_CHOICE) 검증
        assertEquals("성별을 선택해주세요", sortedItems[1].name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, sortedItems[1].inputType)
        assertEquals(true, sortedItems[1].isRequired)
        assertEquals(2, sortedItems[1].orderNumber)

        // 세 번째 항목 (MULTIPLE_CHOICE) 검증
        assertEquals("관심사를 모두 선택해주세요", sortedItems[2].name)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, sortedItems[2].inputType)
        assertEquals(false, sortedItems[2].isRequired)
        assertEquals(3, sortedItems[2].orderNumber)

        // 네 번째 항목 (LONG_TEXT) 검증
        assertEquals("추가 의견을 작성해주세요", sortedItems[3].name)
        assertEquals(SurveyItemInputType.LONG_TEXT, sortedItems[3].inputType)
        assertEquals(false, sortedItems[3].isRequired)
        assertEquals(4, sortedItems[3].orderNumber)

        // 옵션들이 정확히 생성되었는지 검증
        val allOptions = surveyItemOptionRepository.findAll()

        // 성별 선택 옵션 검증
        val genderOptions = allOptions.filter { it.surveyItem.id == sortedItems[1].id }
            .sortedBy { it.orderNumber }
        assertEquals(3, genderOptions.size)
        assertEquals("남성", genderOptions[0].option)
        assertEquals("여성", genderOptions[1].option)
        assertEquals("선택안함", genderOptions[2].option)

        // 관심사 선택 옵션 검증
        val interestOptions = allOptions.filter { it.surveyItem.id == sortedItems[2].id }
            .sortedBy { it.orderNumber }
        assertEquals(4, interestOptions.size)
        assertEquals("기술", interestOptions[0].option)
        assertEquals("예술", interestOptions[1].option)
        assertEquals("스포츠", interestOptions[2].option)
        assertEquals("여행", interestOptions[3].option)

        // 텍스트형 항목들은 옵션이 없어야 함
        val shortTextOptions = allOptions.filter { it.surveyItem.id == sortedItems[0].id }
        assertEquals(0, shortTextOptions.size)

        val longTextOptions = allOptions.filter { it.surveyItem.id == sortedItems[3].id }
        assertEquals(0, longTextOptions.size)
    }

    @Test
    fun createSurvey_단순한_텍스트_설문조사를_생성한다() {
        // given
        val createSurveyRequest = CreateSurveyRequest(
            name = "간단한 피드백",
            description = null,
            surveyItems = listOf(
                CreateSurveyItemRequest(
                    name = "서비스에 대한 의견을 자유롭게 작성해주세요",
                    description = null,
                    inputType = SurveyItemInputType.LONG_TEXT,
                    options = emptyList(),
                    isRequired = true
                )
            )
        )

        // when
        val createdSurvey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(createdSurvey.id)
        assertEquals("간단한 피드백", createdSurvey.name)
        assertEquals(null, createdSurvey.description)

        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == createdSurvey.id }
        assertEquals(1, savedItems.size)
        assertEquals("서비스에 대한 의견을 자유롭게 작성해주세요", savedItems[0].name)
        assertEquals(null, savedItems[0].description)
        assertEquals(SurveyItemInputType.LONG_TEXT, savedItems[0].inputType)
        assertEquals(true, savedItems[0].isRequired)
        assertEquals(1, savedItems[0].orderNumber)

        // 옵션이 생성되지 않았는지 확인
        val options = surveyItemOptionRepository.findAll()
        assertEquals(0, options.size)
    }

    @Test
    fun createSurvey_선택형_항목만_있는_설문조사를_생성한다() {
        // given
        val createSurveyRequest = CreateSurveyRequest(
            name = "선호도 조사",
            description = "사용자 선호도 파악을 위한 설문",
            surveyItems = listOf(
                CreateSurveyItemRequest(
                    name = "선호하는 색상을 선택해주세요",
                    description = "가장 좋아하는 색상 하나를 선택해주세요",
                    inputType = SurveyItemInputType.SINGLE_CHOICE,
                    options = listOf(
                        CreateSurveyItemOptionRequest("빨간색"),
                        CreateSurveyItemOptionRequest("파란색"),
                        CreateSurveyItemOptionRequest("초록색"),
                        CreateSurveyItemOptionRequest("노란색"),
                        CreateSurveyItemOptionRequest("보라색")
                    ),
                    isRequired = true
                ),
                CreateSurveyItemRequest(
                    name = "관심있는 카테고리를 모두 선택해주세요",
                    description = "복수 선택 가능합니다",
                    inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                    options = listOf(
                        CreateSurveyItemOptionRequest("음식"),
                        CreateSurveyItemOptionRequest("패션"),
                        CreateSurveyItemOptionRequest("전자제품"),
                        CreateSurveyItemOptionRequest("도서"),
                        CreateSurveyItemOptionRequest("여행"),
                        CreateSurveyItemOptionRequest("스포츠")
                    ),
                    isRequired = false
                )
            )
        )

        // when
        val createdSurvey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(createdSurvey.id)
        assertEquals("선호도 조사", createdSurvey.name)

        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == createdSurvey.id }
        assertEquals(2, savedItems.size)

        val sortedItems = savedItems.sortedBy { it.orderNumber }

        // 첫 번째 항목 (SINGLE_CHOICE) 검증
        assertEquals("선호하는 색상을 선택해주세요", sortedItems[0].name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, sortedItems[0].inputType)
        assertEquals(true, sortedItems[0].isRequired)
        assertEquals(1, sortedItems[0].orderNumber)

        // 두 번째 항목 (MULTIPLE_CHOICE) 검증
        assertEquals("관심있는 카테고리를 모두 선택해주세요", sortedItems[1].name)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, sortedItems[1].inputType)
        assertEquals(false, sortedItems[1].isRequired)
        assertEquals(2, sortedItems[1].orderNumber)

        // 옵션들 검증
        val allOptions = surveyItemOptionRepository.findAll()

        val colorOptions = allOptions.filter { it.surveyItem.id == sortedItems[0].id }
            .sortedBy { it.orderNumber }
        assertEquals(5, colorOptions.size)
        assertEquals("빨간색", colorOptions[0].option)
        assertEquals("파란색", colorOptions[1].option)
        assertEquals("초록색", colorOptions[2].option)
        assertEquals("노란색", colorOptions[3].option)
        assertEquals("보라색", colorOptions[4].option)

        val categoryOptions = allOptions.filter { it.surveyItem.id == sortedItems[1].id }
            .sortedBy { it.orderNumber }
        assertEquals(6, categoryOptions.size)
        assertEquals("음식", categoryOptions[0].option)
        assertEquals("패션", categoryOptions[1].option)
        assertEquals("전자제품", categoryOptions[2].option)
        assertEquals("도서", categoryOptions[3].option)
        assertEquals("여행", categoryOptions[4].option)
        assertEquals("스포츠", categoryOptions[5].option)
    }

    @Test
    fun createSurvey_텍스트형_항목에_옵션이_있으면_예외를_발생시킨다() {
        // given
        val createSurveyRequest = CreateSurveyRequest(
            name = "잘못된 설문조사",
            description = "텍스트형 항목에 잘못된 옵션이 포함된 설문조사",
            surveyItems = listOf(
                CreateSurveyItemRequest(
                    name = "이름을 입력해주세요",
                    description = "실명을 입력해주세요",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = listOf(
                        CreateSurveyItemOptionRequest("잘못된 옵션")
                    ),
                    isRequired = true
                )
            )
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyService.createSurvey(createSurveyRequest)
        }
    }

    @Test
    fun createSurvey_대량의_항목과_옵션을_가진_설문조사를_생성한다() {
        // given
        val surveyItems = (1..10).map { index ->
            when (index % 4) {
                1 -> CreateSurveyItemRequest(
                    name = "단답형 질문 $index",
                    description = "단답형 질문 $index 에 대한 설명",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = emptyList(),
                    isRequired = index % 2 == 1
                )
                2 -> CreateSurveyItemRequest(
                    name = "장문형 질문 $index",
                    description = "장문형 질문 $index 에 대한 설명",
                    inputType = SurveyItemInputType.LONG_TEXT,
                    options = emptyList(),
                    isRequired = index % 2 == 1
                )
                3 -> CreateSurveyItemRequest(
                    name = "단일선택 질문 $index",
                    description = "단일선택 질문 $index 에 대한 설명",
                    inputType = SurveyItemInputType.SINGLE_CHOICE,
                    options = listOf(
                        CreateSurveyItemOptionRequest("옵션 ${index}A"),
                        CreateSurveyItemOptionRequest("옵션 ${index}B"),
                        CreateSurveyItemOptionRequest("옵션 ${index}C")
                    ),
                    isRequired = index % 2 == 1
                )
                else -> CreateSurveyItemRequest(
                    name = "다중선택 질문 $index",
                    description = "다중선택 질문 $index 에 대한 설명",
                    inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                    options = listOf(
                        CreateSurveyItemOptionRequest("다중옵션 ${index}A"),
                        CreateSurveyItemOptionRequest("다중옵션 ${index}B"),
                        CreateSurveyItemOptionRequest("다중옵션 ${index}C"),
                        CreateSurveyItemOptionRequest("다중옵션 ${index}D")
                    ),
                    isRequired = index % 2 == 1
                )
            }
        }

        val createSurveyRequest = CreateSurveyRequest(
            name = "대용량 종합 설문조사",
            description = "대량의 항목과 옵션을 포함한 종합 설문조사",
            surveyItems = surveyItems
        )

        // when
        val createdSurvey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(createdSurvey.id)
        assertEquals("대용량 종합 설문조사", createdSurvey.name)

        // 모든 항목이 생성되었는지 확인
        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == createdSurvey.id }
        assertEquals(10, savedItems.size)

        // 순서번호가 올바르게 설정되었는지 확인
        val sortedItems = savedItems.sortedBy { it.orderNumber }
        sortedItems.forEachIndexed { index, item ->
            assertEquals(index + 1, item.orderNumber)
        }

        // 선택형 항목들의 옵션이 모두 생성되었는지 확인
        val allOptions = surveyItemOptionRepository.findAll()
        val singleChoiceCount = sortedItems.count { it.inputType == SurveyItemInputType.SINGLE_CHOICE }
        val multipleChoiceCount = sortedItems.count { it.inputType == SurveyItemInputType.MULTIPLE_CHOICE }
        val expectedOptionCount = singleChoiceCount * 3 + multipleChoiceCount * 4
        assertEquals(expectedOptionCount, allOptions.size)
    }

    @Test
    fun createSurvey_트랜잭션_롤백_테스트() {
        // given - 마지막 항목에서 예외가 발생하는 설문조사
        val createSurveyRequest = CreateSurveyRequest(
            name = "트랜잭션 테스트 설문조사",
            description = "트랜잭션 롤백을 테스트하기 위한 설문조사",
            surveyItems = listOf(
                CreateSurveyItemRequest(
                    name = "정상적인 질문",
                    description = "이 질문은 정상적으로 처리됩니다",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = emptyList(),
                    isRequired = true
                ),
                CreateSurveyItemRequest(
                    name = "오류를 발생시킬 질문",
                    description = "이 질문은 오류를 발생시킵니다",
                    inputType = SurveyItemInputType.LONG_TEXT,
                    options = listOf(
                        CreateSurveyItemOptionRequest("잘못된 옵션") // LONG_TEXT에 옵션 추가로 예외 발생
                    ),
                    isRequired = true
                )
            )
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyService.createSurvey(createSurveyRequest)
        }
    }

    @Test
    fun createSurvey_동시에_여러_설문조사를_생성한다() {
        // given
        val surveyRequests = (1..5).map { index ->
            CreateSurveyRequest(
                name = "설문조사 $index",
                description = "설문조사 $index 에 대한 설명",
                surveyItems = listOf(
                    CreateSurveyItemRequest(
                        name = "질문 ${index}-1",
                        description = "질문 ${index}-1 설명",
                        inputType = SurveyItemInputType.SHORT_TEXT,
                        options = emptyList(),
                        isRequired = true
                    ),
                    CreateSurveyItemRequest(
                        name = "질문 ${index}-2",
                        description = "질문 ${index}-2 설명",
                        inputType = SurveyItemInputType.SINGLE_CHOICE,
                        options = listOf(
                            CreateSurveyItemOptionRequest("옵션A"),
                            CreateSurveyItemOptionRequest("옵션B")
                        ),
                        isRequired = false
                    )
                )
            )
        }

        // when
        val createdSurveys = surveyRequests.map { request ->
            surveyService.createSurvey(request)
        }

        // then
        assertEquals(5, createdSurveys.size)

        // 모든 설문조사가 고유한 ID를 가지는지 확인
        val surveyIds = createdSurveys.mapNotNull { it.id }
        assertEquals(5, surveyIds.size)
        assertEquals(5, surveyIds.toSet().size) // 중복 없이 5개

        // 데이터베이스에 모든 데이터가 저장되었는지 확인
        val allSurveys = surveyRepository.findAll()
        assertEquals(5, allSurveys.size)

        val allItems = surveyItemRepository.findAll()
        assertEquals(10, allItems.size) // 각 설문조사마다 2개씩 총 10개

        val allOptions = surveyItemOptionRepository.findAll()
        assertEquals(10, allOptions.size) // 각 설문조사의 두 번째 항목마다 2개씩 총 10개
    }
}
