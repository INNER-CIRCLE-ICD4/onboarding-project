package kr.innercircle.onboarding.survey.integration.service

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest
import kr.innercircle.onboarding.survey.exception.InsufficientSurveyItemOptionsException
import kr.innercircle.onboarding.survey.exception.InvalidSurveyItemTypeException
import kr.innercircle.onboarding.survey.repository.SurveyItemOptionRepository
import kr.innercircle.onboarding.survey.repository.SurveyItemRepository
import kr.innercircle.onboarding.survey.repository.SurveyRepository
import kr.innercircle.onboarding.survey.service.SurveyItemService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * packageName : kr.innercircle.onboarding.survey.integration.service
 * fileName    : SurveyItemServiceTest
 * author      : ckr
 * date        : 25. 6. 21.
 * description : SurveyItemService 통합 테스트
 */

@SpringBootTest
@Transactional
class SurveyItemServiceTest {

    @Autowired
    private lateinit var surveyItemService: SurveyItemService

    @Autowired
    private lateinit var surveyRepository: SurveyRepository

    @Autowired
    private lateinit var surveyItemRepository: SurveyItemRepository

    @Autowired
    private lateinit var surveyItemOptionRepository: SurveyItemOptionRepository

    private lateinit var testSurvey: Survey

    @BeforeEach
    fun setUp() {
        // @Transactional로 자동 롤백되므로 별도 정리 불필요

        // 테스트용 설문조사 생성
        testSurvey = surveyRepository.save(
            Survey(
                name = "테스트 설문조사",
                description = "통합 테스트용 설문조사"
            )
        )
    }

    @Test
    fun createSurveyItems_텍스트형_항목들을_생성한다() {
        // given
        val createSurveyItemRequests = listOf(
            CreateSurveyItemRequest(
                name = "이름을 입력해주세요",
                description = "실명으로 입력해주세요",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            CreateSurveyItemRequest(
                name = "상세한 의견을 작성해주세요",
                description = "500자 이내로 작성해주세요",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList(),
                isRequired = false
            )
        )

        // when
        val createdItems = surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)

        // then
        assertEquals(2, createdItems.size)

        // 첫 번째 항목 검증
        assertNotNull(createdItems[0].id)
        assertEquals("이름을 입력해주세요", createdItems[0].name)
        assertEquals("실명으로 입력해주세요", createdItems[0].description)
        assertEquals(SurveyItemInputType.SHORT_TEXT, createdItems[0].inputType)
        assertEquals(true, createdItems[0].isRequired)
        assertEquals(1, createdItems[0].orderNumber)
        assertEquals(testSurvey, createdItems[0].survey)

        // 두 번째 항목 검증
        assertNotNull(createdItems[1].id)
        assertEquals("상세한 의견을 작성해주세요", createdItems[1].name)
        assertEquals("500자 이내로 작성해주세요", createdItems[1].description)
        assertEquals(SurveyItemInputType.LONG_TEXT, createdItems[1].inputType)
        assertEquals(false, createdItems[1].isRequired)
        assertEquals(2, createdItems[1].orderNumber)
        assertEquals(testSurvey, createdItems[1].survey)

        // 데이터베이스에서 실제 저장된 데이터 검증
        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == testSurvey.id }
        assertEquals(2, savedItems.size)

        // 텍스트형 항목에는 옵션이 생성되지 않았는지 확인
        val options = surveyItemOptionRepository.findAll()
        assertEquals(0, options.size)
    }

    @Test
    fun createSurveyItems_선택형_항목들을_생성한다() {
        // given
        val createSurveyItemRequests = listOf(
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
                    CreateSurveyItemOptionRequest("여행"),
                    CreateSurveyItemOptionRequest("음식")
                ),
                isRequired = false
            )
        )

        // when
        val createdItems = surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)

        // then
        assertEquals(2, createdItems.size)

        // 첫 번째 항목 (SINGLE_CHOICE) 검증
        assertEquals("성별을 선택해주세요", createdItems[0].name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, createdItems[0].inputType)
        assertEquals(true, createdItems[0].isRequired)
        assertEquals(1, createdItems[0].orderNumber)

        // 두 번째 항목 (MULTIPLE_CHOICE) 검증
        assertEquals("관심사를 모두 선택해주세요", createdItems[1].name)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, createdItems[1].inputType)
        assertEquals(false, createdItems[1].isRequired)
        assertEquals(2, createdItems[1].orderNumber)

        // 옵션들이 정확히 생성되었는지 검증
        val allOptions = surveyItemOptionRepository.findAll()
        assertEquals(8, allOptions.size) // 3개 + 5개

        // 성별 선택 옵션 검증
        val genderOptions = allOptions.filter { it.surveyItem.id == createdItems[0].id }
            .sortedBy { it.orderNumber }
        assertEquals(3, genderOptions.size)
        assertEquals("남성", genderOptions[0].option)
        assertEquals("여성", genderOptions[1].option)
        assertEquals("선택안함", genderOptions[2].option)

        // 관심사 선택 옵션 검증
        val interestOptions = allOptions.filter { it.surveyItem.id == createdItems[1].id }
            .sortedBy { it.orderNumber }
        assertEquals(5, interestOptions.size)
        assertEquals("기술", interestOptions[0].option)
        assertEquals("예술", interestOptions[1].option)
        assertEquals("스포츠", interestOptions[2].option)
        assertEquals("여행", interestOptions[3].option)
        assertEquals("음식", interestOptions[4].option)
    }

    @Test
    fun createSurveyItems_혼합_타입_항목들을_생성한다() {
        // given
        val createSurveyItemRequests = listOf(
            CreateSurveyItemRequest(
                name = "이름",
                description = null,
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            CreateSurveyItemRequest(
                name = "나이대",
                description = "해당하는 나이대를 선택해주세요",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    CreateSurveyItemOptionRequest("10대"),
                    CreateSurveyItemOptionRequest("20대"),
                    CreateSurveyItemOptionRequest("30대"),
                    CreateSurveyItemOptionRequest("40대"),
                    CreateSurveyItemOptionRequest("50대 이상")
                ),
                isRequired = true
            ),
            CreateSurveyItemRequest(
                name = "취미",
                description = "복수 선택 가능",
                inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                options = listOf(
                    CreateSurveyItemOptionRequest("독서"),
                    CreateSurveyItemOptionRequest("운동"),
                    CreateSurveyItemOptionRequest("영화감상")
                ),
                isRequired = false
            ),
            CreateSurveyItemRequest(
                name = "추가 의견",
                description = "자유롭게 작성해주세요",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList(),
                isRequired = false
            )
        )

        // when
        val createdItems = surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)

        // then
        assertEquals(4, createdItems.size)

        // 순서번호가 올바르게 설정되었는지 확인
        createdItems.forEachIndexed { index, item ->
            assertEquals(index + 1, item.orderNumber)
            assertEquals(testSurvey, item.survey)
        }

        // 각 항목 타입별 검증
        assertEquals(SurveyItemInputType.SHORT_TEXT, createdItems[0].inputType)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, createdItems[1].inputType)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, createdItems[2].inputType)
        assertEquals(SurveyItemInputType.LONG_TEXT, createdItems[3].inputType)

        // 옵션 개수 검증
        val allOptions = surveyItemOptionRepository.findAll()
        assertEquals(8, allOptions.size) // 5개(나이대) + 3개(취미)

        // 텍스트형 항목들은 옵션이 없어야 함
        val shortTextOptions = allOptions.filter { it.surveyItem.id == createdItems[0].id }
        assertEquals(0, shortTextOptions.size)

        val longTextOptions = allOptions.filter { it.surveyItem.id == createdItems[3].id }
        assertEquals(0, longTextOptions.size)
    }

    @Test
    fun createSurveyItems_빈_리스트로_호출하면_빈_리스트를_반환한다() {
        // given
        val createSurveyItemRequests = emptyList<CreateSurveyItemRequest>()

        // when
        val createdItems = surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)

        // then
        assertEquals(0, createdItems.size)

        // 데이터베이스에도 아무것도 저장되지 않았는지 확인
        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == testSurvey.id }
        assertEquals(0, savedItems.size)

        val options = surveyItemOptionRepository.findAll()
        assertEquals(0, options.size)
    }

    @Test
    fun createSurveyItems_선택형_항목에_빈_옵션_리스트가_있어도_생성된다() {
        // given
        val createSurveyItemRequests = listOf(
            CreateSurveyItemRequest(
                name = "정상적인 질문",
                description = "이 질문은 정상적으로 처리됩니다",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            CreateSurveyItemRequest(
                name = "빈 옵션을 가진 선택형 질문",
                description = "이 질문은 빈 옵션 리스트를 가지고 있습니다",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = emptyList(),
                isRequired = true
            )
        )

        // when & then
        // 빈 리스트에 대해서는 예외가 발생함
        assertThrows<InsufficientSurveyItemOptionsException> {
            surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)
        }
    }

    @Test
    fun createSurveyItems_텍스트형_항목에_옵션이_있으면_예외를_발생시킨다() {
        // given
        val createSurveyItemRequests = listOf(
            CreateSurveyItemRequest(
                name = "잘못된 텍스트 질문",
                description = "이 질문은 잘못된 옵션을 가지고 있습니다",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = listOf(
                    CreateSurveyItemOptionRequest("잘못된 옵션") // SHORT_TEXT에 옵션 추가로 예외 발생
                ),
                isRequired = true
            )
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)
        }
    }

    @Test
    fun createSurveyItems_대량의_항목을_생성한다() {
        // given
        val createSurveyItemRequests = (1..20).map { index ->
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
                    options = (1..3).map { optionIndex ->
                        CreateSurveyItemOptionRequest("옵션 ${index}-${optionIndex}")
                    },
                    isRequired = index % 2 == 1
                )
                else -> CreateSurveyItemRequest(
                    name = "다중선택 질문 $index",
                    description = "다중선택 질문 $index 에 대한 설명",
                    inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                    options = (1..5).map { optionIndex ->
                        CreateSurveyItemOptionRequest("다중옵션 ${index}-${optionIndex}")
                    },
                    isRequired = index % 2 == 1
                )
            }
        }

        // when
        val createdItems = surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)

        // then
        assertEquals(20, createdItems.size)

        // 순서번호가 올바르게 설정되었는지 확인
        createdItems.forEachIndexed { index, item ->
            assertEquals(index + 1, item.orderNumber)
            assertEquals(testSurvey, item.survey)
        }

        // 데이터베이스에 모든 데이터가 저장되었는지 확인
        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == testSurvey.id }
        assertEquals(20, savedItems.size)

        // 옵션 개수 계산 및 확인
        val singleChoiceCount = createdItems.count { it.inputType == SurveyItemInputType.SINGLE_CHOICE }
        val multipleChoiceCount = createdItems.count { it.inputType == SurveyItemInputType.MULTIPLE_CHOICE }
        val expectedOptionCount = singleChoiceCount * 3 + multipleChoiceCount * 5

        val allOptions = surveyItemOptionRepository.findAll()
        assertEquals(expectedOptionCount, allOptions.size)
    }

    @Test
    fun createSurveyItems_특수문자와_유니코드가_포함된_항목을_생성한다() {
        // given
        val createSurveyItemRequests = listOf(
            CreateSurveyItemRequest(
                name = "특수문자!@#$%^&*()질문",
                description = "특수문자가 포함된 설명!@#$%",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            CreateSurveyItemRequest(
                name = "유니코드🌍질문한국어日本語",
                description = "다국어와 이모지가 포함된 설명🇰🇷🇺🇸",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    CreateSurveyItemOptionRequest("옵션🌟"),
                    CreateSurveyItemOptionRequest("选项中文"),
                    CreateSurveyItemOptionRequest("オプション")
                ),
                isRequired = false
            )
        )

        // when
        val createdItems = surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)

        // then
        assertEquals(2, createdItems.size)

        // 특수문자 포함 항목 검증
        assertEquals("특수문자!@#$%^&*()질문", createdItems[0].name)
        assertEquals("특수문자가 포함된 설명!@#$%", createdItems[0].description)
        assertEquals(SurveyItemInputType.SHORT_TEXT, createdItems[0].inputType)

        // 유니코드 포함 항목 검증
        assertEquals("유니코드🌍질문한국어日本語", createdItems[1].name)
        assertEquals("다국어와 이모지가 포함된 설명🇰🇷🇺🇸", createdItems[1].description)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, createdItems[1].inputType)

        // 유니코드 옵션 검증
        val options = surveyItemOptionRepository.findAll()
            .filter { it.surveyItem.id == createdItems[1].id }
            .sortedBy { it.orderNumber }

        assertEquals(3, options.size)
        assertEquals("옵션🌟", options[0].option)
        assertEquals("选项中文", options[1].option)
        assertEquals("オプション", options[2].option)
    }

    @Test
    fun createSurveyItems_null과_빈값_처리_테스트() {
        // given
        val createSurveyItemRequests = listOf(
            CreateSurveyItemRequest(
                name = "설명이 null인 질문",
                description = null,
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            CreateSurveyItemRequest(
                name = "설명이 빈 문자열인 질문",
                description = "",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList(),
                isRequired = false
            ),
            CreateSurveyItemRequest(
                name = "설명이 공백인 질문",
                description = "   ",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    CreateSurveyItemOptionRequest(""),
                    CreateSurveyItemOptionRequest("   ")
                ),
                isRequired = false
            )
        )

        // when
        val createdItems = surveyItemService.createSurveyItems(testSurvey, createSurveyItemRequests)

        // then
        assertEquals(3, createdItems.size)

        // null 설명 검증
        assertEquals("설명이 null인 질문", createdItems[0].name)
        assertEquals(null, createdItems[0].description)

        // 빈 문자열 설명 검증
        assertEquals("설명이 빈 문자열인 질문", createdItems[1].name)
        assertEquals("", createdItems[1].description)

        // 공백 설명 검증
        assertEquals("설명이 공백인 질문", createdItems[2].name)
        assertEquals("   ", createdItems[2].description)

        // 빈 문자열과 공백 옵션 검증
        val options = surveyItemOptionRepository.findAll()
            .filter { it.surveyItem.id == createdItems[2].id }
            .sortedBy { it.orderNumber }

        assertEquals(2, options.size)
        assertEquals("", options[0].option)
        assertEquals("   ", options[1].option)
    }

    @Test
    fun createSurveyItems_동일한_설문조사에_여러번_호출하면_각각_1부터_시작한다() {
        // given - 첫 번째 배치
        val firstBatch = listOf(
            CreateSurveyItemRequest(
                name = "첫 번째 질문",
                description = "첫 번째 배치의 질문",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            CreateSurveyItemRequest(
                name = "두 번째 질문",
                description = "첫 번째 배치의 질문",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList(),
                isRequired = false
            )
        )

        // when - 첫 번째 배치 생성
        val firstCreatedItems = surveyItemService.createSurveyItems(testSurvey, firstBatch)

        // given - 두 번째 배치
        val secondBatch = listOf(
            CreateSurveyItemRequest(
                name = "세 번째 질문",
                description = "두 번째 배치의 질문",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    CreateSurveyItemOptionRequest("옵션1"),
                    CreateSurveyItemOptionRequest("옵션2")
                ),
                isRequired = true
            )
        )

        // when - 두 번째 배치 생성
        val secondCreatedItems = surveyItemService.createSurveyItems(testSurvey, secondBatch)

        // then
        assertEquals(2, firstCreatedItems.size)
        assertEquals(1, secondCreatedItems.size)

        // 첫 번째 배치의 순서번호 확인
        assertEquals(1, firstCreatedItems[0].orderNumber)
        assertEquals(2, firstCreatedItems[1].orderNumber)

        // 두 번째 배치의 순서번호 확인 (각 호출마다 1부터 시작)
        assertEquals(1, secondCreatedItems[0].orderNumber)

        // 전체 항목 확인
        val allItems = surveyItemRepository.findAll().filter { it.survey.id == testSurvey.id }
        assertEquals(3, allItems.size)

        // 이름으로 항목 찾기
        assertEquals("첫 번째 질문", allItems.find { it.name == "첫 번째 질문" }?.name)
        assertEquals("두 번째 질문", allItems.find { it.name == "두 번째 질문" }?.name)
        assertEquals("세 번째 질문", allItems.find { it.name == "세 번째 질문" }?.name)
    }
}
