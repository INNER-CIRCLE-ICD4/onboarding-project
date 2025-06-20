package kr.innercircle.onboarding.survey.integration.service

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest
import kr.innercircle.onboarding.survey.exception.InsufficientSurveyItemOptionsException
import kr.innercircle.onboarding.survey.exception.InvalidSurveyItemTypeException
import kr.innercircle.onboarding.survey.repository.SurveyItemOptionRepository
import kr.innercircle.onboarding.survey.repository.SurveyItemRepository
import kr.innercircle.onboarding.survey.repository.SurveyRepository
import kr.innercircle.onboarding.survey.service.SurveyItemOptionService
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
 * fileName    : SurveyItemOptionServiceTest
 * author      : ckr
 * date        : 25. 6. 21.
 * description : SurveyItemOptionService 통합 테스트
 */

@SpringBootTest
@Transactional
class SurveyItemOptionServiceTest {

    @Autowired
    private lateinit var surveyItemOptionService: SurveyItemOptionService

    @Autowired
    private lateinit var surveyRepository: SurveyRepository

    @Autowired
    private lateinit var surveyItemRepository: SurveyItemRepository

    @Autowired
    private lateinit var surveyItemOptionRepository: SurveyItemOptionRepository

    private lateinit var testSurvey: Survey
    private lateinit var singleChoiceItem: SurveyItem
    private lateinit var multipleChoiceItem: SurveyItem
    private lateinit var shortTextItem: SurveyItem
    private lateinit var longTextItem: SurveyItem

    @BeforeEach
    fun setUp() {
        // 테스트용 설문조사 생성
        testSurvey = surveyRepository.save(
            Survey(
                name = "테스트 설문조사",
                description = "통합 테스트용 설문조사"
            )
        )

        // 테스트용 설문 항목들 생성
        singleChoiceItem = surveyItemRepository.save(
            SurveyItem(
                survey = testSurvey,
                name = "단일 선택 질문",
                description = "하나만 선택해주세요",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                isRequired = true,
                orderNumber = 1
            )
        )

        multipleChoiceItem = surveyItemRepository.save(
            SurveyItem(
                survey = testSurvey,
                name = "다중 선택 질문",
                description = "여러 개 선택 가능합니다",
                inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                isRequired = false,
                orderNumber = 2
            )
        )

        shortTextItem = surveyItemRepository.save(
            SurveyItem(
                survey = testSurvey,
                name = "단답형 질문",
                description = "간단히 답해주세요",
                inputType = SurveyItemInputType.SHORT_TEXT,
                isRequired = true,
                orderNumber = 3
            )
        )

        longTextItem = surveyItemRepository.save(
            SurveyItem(
                survey = testSurvey,
                name = "장문형 질문",
                description = "자세히 답해주세요",
                inputType = SurveyItemInputType.LONG_TEXT,
                isRequired = false,
                orderNumber = 4
            )
        )
    }

    @Test
    fun createSurveyItemOptions_SINGLE_CHOICE_항목에_옵션들을_생성한다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("매우 만족"),
            CreateSurveyItemOptionRequest("만족"),
            CreateSurveyItemOptionRequest("보통"),
            CreateSurveyItemOptionRequest("불만족"),
            CreateSurveyItemOptionRequest("매우 불만족")
        )

        // when
        val createdOptions = surveyItemOptionService.createSurveyItemOptions(
            singleChoiceItem,
            createSurveyItemOptionRequests
        )

        // then
        assertEquals(5, createdOptions.size)

        // 각 옵션이 올바르게 생성되었는지 확인
        createdOptions.forEachIndexed { index, option ->
            assertNotNull(option.id)
            assertEquals(singleChoiceItem, option.surveyItem)
            assertEquals(createSurveyItemOptionRequests[index].option, option.option)
            assertEquals(index + 1, option.orderNumber)
        }

        // 데이터베이스에서 실제 저장된 데이터 검증
        val savedOptions = surveyItemOptionRepository.findAll()
        assertEquals(5, savedOptions.size)

        val sortedOptions = savedOptions.sortedBy { it.orderNumber }
        assertEquals("매우 만족", sortedOptions[0].option)
        assertEquals("만족", sortedOptions[1].option)
        assertEquals("보통", sortedOptions[2].option)
        assertEquals("불만족", sortedOptions[3].option)
        assertEquals("매우 불만족", sortedOptions[4].option)
    }

    @Test
    fun createSurveyItemOptions_MULTIPLE_CHOICE_항목에_옵션들을_생성한다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("기술"),
            CreateSurveyItemOptionRequest("예술"),
            CreateSurveyItemOptionRequest("스포츠"),
            CreateSurveyItemOptionRequest("여행"),
            CreateSurveyItemOptionRequest("음식"),
            CreateSurveyItemOptionRequest("패션"),
            CreateSurveyItemOptionRequest("게임")
        )

        // when
        val createdOptions = surveyItemOptionService.createSurveyItemOptions(
            multipleChoiceItem,
            createSurveyItemOptionRequests
        )

        // then
        assertEquals(7, createdOptions.size)

        // 순서번호가 올바르게 설정되었는지 확인
        createdOptions.forEachIndexed { index, option ->
            assertNotNull(option.id)
            assertEquals(multipleChoiceItem, option.surveyItem)
            assertEquals(createSurveyItemOptionRequests[index].option, option.option)
            assertEquals(index + 1, option.orderNumber)
        }

        // 데이터베이스에서 실제 저장된 데이터 검증
        val savedOptions = surveyItemOptionRepository.findAll()
        assertEquals(7, savedOptions.size)

        val sortedOptions = savedOptions.sortedBy { it.orderNumber }
        assertEquals("기술", sortedOptions[0].option)
        assertEquals("예술", sortedOptions[1].option)
        assertEquals("스포츠", sortedOptions[2].option)
        assertEquals("여행", sortedOptions[3].option)
        assertEquals("음식", sortedOptions[4].option)
        assertEquals("패션", sortedOptions[5].option)
        assertEquals("게임", sortedOptions[6].option)
    }

    @Test
    fun createSurveyItemOptions_최소_2개_옵션으로_생성한다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("예"),
            CreateSurveyItemOptionRequest("아니오")
        )

        // when
        val createdOptions = surveyItemOptionService.createSurveyItemOptions(
            singleChoiceItem,
            createSurveyItemOptionRequests
        )

        // then
        assertEquals(2, createdOptions.size)
        assertEquals("예", createdOptions[0].option)
        assertEquals(1, createdOptions[0].orderNumber)
        assertEquals("아니오", createdOptions[1].option)
        assertEquals(2, createdOptions[1].orderNumber)
    }

    @Test
    fun createSurveyItemOptions_2개_미만_옵션으로_생성하면_예외를_발생시킨다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("단일 옵션")
        )

        // when & then
        assertThrows<InsufficientSurveyItemOptionsException> {
            surveyItemOptionService.createSurveyItemOptions(singleChoiceItem, createSurveyItemOptionRequests)
        }

        // 예외 발생으로 인해 옵션이 저장되지 않았는지 확인
        val savedOptions = surveyItemOptionRepository.findAll()
        assertEquals(0, savedOptions.size)
    }

    @Test
    fun createSurveyItemOptions_빈_리스트로_생성하면_예외를_발생시킨다() {
        // given
        val createSurveyItemOptionRequests = emptyList<CreateSurveyItemOptionRequest>()

        // when & then
        assertThrows<InsufficientSurveyItemOptionsException> {
            surveyItemOptionService.createSurveyItemOptions(multipleChoiceItem, createSurveyItemOptionRequests)
        }

        // 예외 발생으로 인해 옵션이 저장되지 않았는지 확인
        val savedOptions = surveyItemOptionRepository.findAll()
        assertEquals(0, savedOptions.size)
    }

    @Test
    fun createSurveyItemOptions_SHORT_TEXT_항목에_옵션을_생성하면_예외를_발생시킨다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("잘못된 옵션1"),
            CreateSurveyItemOptionRequest("잘못된 옵션2")
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyItemOptionService.createSurveyItemOptions(shortTextItem, createSurveyItemOptionRequests)
        }

        // 예외 발생으로 인해 옵션이 저장되지 않았는지 확인
        val savedOptions = surveyItemOptionRepository.findAll()
        assertEquals(0, savedOptions.size)
    }

    @Test
    fun createSurveyItemOptions_LONG_TEXT_항목에_옵션을_생성하면_예외를_발생시킨다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("잘못된 옵션1"),
            CreateSurveyItemOptionRequest("잘못된 옵션2")
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyItemOptionService.createSurveyItemOptions(longTextItem, createSurveyItemOptionRequests)
        }

        // 예외 발생으로 인해 옵션이 저장되지 않았는지 확인
        val savedOptions = surveyItemOptionRepository.findAll()
        assertEquals(0, savedOptions.size)
    }

    @Test
    fun createSurveyItemOptions_대량의_옵션을_생성한다() {
        // given
        val createSurveyItemOptionRequests = (1..50).map { index ->
            CreateSurveyItemOptionRequest("옵션 $index")
        }

        // when
        val createdOptions = surveyItemOptionService.createSurveyItemOptions(
            multipleChoiceItem,
            createSurveyItemOptionRequests
        )

        // then
        assertEquals(50, createdOptions.size)

        // 순서번호가 올바르게 설정되었는지 확인
        createdOptions.forEachIndexed { index, option ->
            assertNotNull(option.id)
            assertEquals(multipleChoiceItem, option.surveyItem)
            assertEquals("옵션 ${index + 1}", option.option)
            assertEquals(index + 1, option.orderNumber)
        }

        // 데이터베이스에서 실제 저장된 데이터 검증
        val savedOptions = surveyItemOptionRepository.findAll()
        assertEquals(50, savedOptions.size)
    }

    @Test
    fun createSurveyItemOptions_특수문자와_유니코드_옵션을_생성한다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("특수문자!@#$%^&*()옵션"),
            CreateSurveyItemOptionRequest("유니코드🌍옵션한국어"),
            CreateSurveyItemOptionRequest("中文选项"),
            CreateSurveyItemOptionRequest("日本語オプション"),
            CreateSurveyItemOptionRequest("Русский вариант"),
            CreateSurveyItemOptionRequest("العربية خيار")
        )

        // when
        val createdOptions = surveyItemOptionService.createSurveyItemOptions(
            singleChoiceItem,
            createSurveyItemOptionRequests
        )

        // then
        assertEquals(6, createdOptions.size)

        val sortedOptions = createdOptions.sortedBy { it.orderNumber }
        assertEquals("특수문자!@#$%^&*()옵션", sortedOptions[0].option)
        assertEquals("유니코드🌍옵션한국어", sortedOptions[1].option)
        assertEquals("中文选项", sortedOptions[2].option)
        assertEquals("日本語オプション", sortedOptions[3].option)
        assertEquals("Русский вариант", sortedOptions[4].option)
        assertEquals("العربية خيار", sortedOptions[5].option)

        // 데이터베이스에서 유니코드 데이터가 올바르게 저장되었는지 확인
        val savedOptions = surveyItemOptionRepository.findAll().sortedBy { it.orderNumber }
        assertEquals("특수문자!@#$%^&*()옵션", savedOptions[0].option)
        assertEquals("유니코드🌍옵션한국어", savedOptions[1].option)
        assertEquals("中文选项", savedOptions[2].option)
        assertEquals("日本語オプション", savedOptions[3].option)
        assertEquals("Русский вариант", savedOptions[4].option)
        assertEquals("العربية خيار", savedOptions[5].option)
    }

    @Test
    fun createSurveyItemOptions_빈_문자열과_공백_옵션을_생성한다() {
        // given
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest(""),
            CreateSurveyItemOptionRequest("   "),
            CreateSurveyItemOptionRequest("  앞뒤 공백  "),
            CreateSurveyItemOptionRequest("정상 옵션")
        )

        // when
        val createdOptions = surveyItemOptionService.createSurveyItemOptions(
            multipleChoiceItem,
            createSurveyItemOptionRequests
        )

        // then
        assertEquals(4, createdOptions.size)

        val sortedOptions = createdOptions.sortedBy { it.orderNumber }
        assertEquals("", sortedOptions[0].option)
        assertEquals("   ", sortedOptions[1].option)
        assertEquals("  앞뒤 공백  ", sortedOptions[2].option)
        assertEquals("정상 옵션", sortedOptions[3].option)
    }

    @Test
    fun createSurveyItemOptions_동일한_항목에_여러번_호출하면_각각_1부터_시작한다() {
        // given - 첫 번째 배치
        val firstBatch = listOf(
            CreateSurveyItemOptionRequest("첫 번째 옵션"),
            CreateSurveyItemOptionRequest("두 번째 옵션")
        )

        // when - 첫 번째 배치 생성
        val firstCreatedOptions = surveyItemOptionService.createSurveyItemOptions(
            singleChoiceItem,
            firstBatch
        )

        // given - 두 번째 배치
        val secondBatch = listOf(
            CreateSurveyItemOptionRequest("세 번째 옵션"),
            CreateSurveyItemOptionRequest("네 번째 옵션")
        )

        // when - 두 번째 배치 생성
        val secondCreatedOptions = surveyItemOptionService.createSurveyItemOptions(
            singleChoiceItem,
            secondBatch
        )

        // then
        assertEquals(2, firstCreatedOptions.size)
        assertEquals(2, secondCreatedOptions.size)

        // 첫 번째 배치의 순서번호 확인
        assertEquals(1, firstCreatedOptions[0].orderNumber)
        assertEquals(2, firstCreatedOptions[1].orderNumber)

        // 두 번째 배치의 순서번호 확인 (각 호출마다 1부터 시작)
        assertEquals(1, secondCreatedOptions[0].orderNumber)
        assertEquals(2, secondCreatedOptions[1].orderNumber)

        // 전체 옵션 확인 (총 4개)
        val allOptions = surveyItemOptionRepository.findAll()
        assertEquals(4, allOptions.size)
        assertEquals("첫 번째 옵션", allOptions.find { it.option == "첫 번째 옵션" }?.option)
        assertEquals("두 번째 옵션", allOptions.find { it.option == "두 번째 옵션" }?.option)
        assertEquals("세 번째 옵션", allOptions.find { it.option == "세 번째 옵션" }?.option)
        assertEquals("네 번째 옵션", allOptions.find { it.option == "네 번째 옵션" }?.option)
    }

    @Test
    fun createSurveyItemOptions_서로_다른_항목에_독립적으로_옵션을_생성한다() {
        // given
        val singleChoiceOptions = listOf(
            CreateSurveyItemOptionRequest("단일선택 옵션1"),
            CreateSurveyItemOptionRequest("단일선택 옵션2"),
            CreateSurveyItemOptionRequest("단일선택 옵션3")
        )

        val multipleChoiceOptions = listOf(
            CreateSurveyItemOptionRequest("다중선택 옵션1"),
            CreateSurveyItemOptionRequest("다중선택 옵션2"),
            CreateSurveyItemOptionRequest("다중선택 옵션3"),
            CreateSurveyItemOptionRequest("다중선택 옵션4")
        )

        // when
        val singleChoiceCreatedOptions = surveyItemOptionService.createSurveyItemOptions(
            singleChoiceItem,
            singleChoiceOptions
        )

        val multipleChoiceCreatedOptions = surveyItemOptionService.createSurveyItemOptions(
            multipleChoiceItem,
            multipleChoiceOptions
        )

        // then
        assertEquals(3, singleChoiceCreatedOptions.size)
        assertEquals(4, multipleChoiceCreatedOptions.size)

        // 각 항목의 옵션들이 독립적으로 순서번호를 가지는지 확인
        singleChoiceCreatedOptions.forEachIndexed { index, option ->
            assertEquals(singleChoiceItem, option.surveyItem)
            assertEquals(index + 1, option.orderNumber)
        }

        multipleChoiceCreatedOptions.forEachIndexed { index, option ->
            assertEquals(multipleChoiceItem, option.surveyItem)
            assertEquals(index + 1, option.orderNumber)
        }

        // 데이터베이스에서 각 항목별로 옵션이 올바르게 분리되어 저장되었는지 확인
        val allOptions = surveyItemOptionRepository.findAll()
        assertEquals(7, allOptions.size)

        val singleChoiceStoredOptions = allOptions.filter { it.surveyItem.id == singleChoiceItem.id }
            .sortedBy { it.orderNumber }
        assertEquals(3, singleChoiceStoredOptions.size)
        assertEquals("단일선택 옵션1", singleChoiceStoredOptions[0].option)
        assertEquals("단일선택 옵션2", singleChoiceStoredOptions[1].option)
        assertEquals("단일선택 옵션3", singleChoiceStoredOptions[2].option)

        val multipleChoiceStoredOptions = allOptions.filter { it.surveyItem.id == multipleChoiceItem.id }
            .sortedBy { it.orderNumber }
        assertEquals(4, multipleChoiceStoredOptions.size)
        assertEquals("다중선택 옵션1", multipleChoiceStoredOptions[0].option)
        assertEquals("다중선택 옵션2", multipleChoiceStoredOptions[1].option)
        assertEquals("다중선택 옵션3", multipleChoiceStoredOptions[2].option)
        assertEquals("다중선택 옵션4", multipleChoiceStoredOptions[3].option)
    }

    @Test
    fun createSurveyItemOptions_트랜잭션_롤백_테스트() {
        // given - 올바른 옵션과 잘못된 항목 타입을 혼합
        val validOptions = listOf(
            CreateSurveyItemOptionRequest("정상 옵션1"),
            CreateSurveyItemOptionRequest("정상 옵션2")
        )

        val invalidOptions = listOf(
            CreateSurveyItemOptionRequest("잘못된 옵션1"),
            CreateSurveyItemOptionRequest("잘못된 옵션2")
        )

        // when - 먼저 정상적인 옵션을 생성
        val validCreatedOptions = surveyItemOptionService.createSurveyItemOptions(
            singleChoiceItem,
            validOptions
        )

        // then - 정상적인 옵션이 생성되었는지 확인
        assertEquals(2, validCreatedOptions.size)
        val initialOptions = surveyItemOptionRepository.findAll()
        assertEquals(2, initialOptions.size)

        // when & then - 잘못된 항목 타입에 옵션 생성 시도 (예외 발생)
        assertThrows<InvalidSurveyItemTypeException> {
            surveyItemOptionService.createSurveyItemOptions(shortTextItem, invalidOptions)
        }

        // then - 이전에 생성된 정상적인 옵션들은 영향받지 않았는지 확인
        val finalOptions = surveyItemOptionRepository.findAll()
        assertEquals(2, finalOptions.size) // 여전히 2개만 있어야 함
        assertEquals("정상 옵션1", finalOptions.find { it.orderNumber == 1 }?.option)
        assertEquals("정상 옵션2", finalOptions.find { it.orderNumber == 2 }?.option)
    }

    @Test
    fun createSurveyItemOptions_긴_텍스트_옵션을_생성한다() {
        // given
        val longOptionText = "긴 옵션 텍스트입니다. 이 옵션은 길지만 200자 이내로 작성되었습니다. 데이터베이스 저장을 테스트합니다."
        val createSurveyItemOptionRequests = listOf(
            CreateSurveyItemOptionRequest("짧은 옵션"),
            CreateSurveyItemOptionRequest(longOptionText),
            CreateSurveyItemOptionRequest("다른 짧은 옵션")
        )

        // when
        val createdOptions = surveyItemOptionService.createSurveyItemOptions(
            multipleChoiceItem,
            createSurveyItemOptionRequests
        )

        // then
        assertEquals(3, createdOptions.size)
        assertEquals("짧은 옵션", createdOptions[0].option)
        assertEquals(longOptionText, createdOptions[1].option)
        assertEquals("다른 짧은 옵션", createdOptions[2].option)

        // 데이터베이스에서 긴 텍스트가 올바르게 저장되었는지 확인
        val savedOptions = surveyItemOptionRepository.findAll().sortedBy { it.orderNumber }
        assertEquals(longOptionText, savedOptions[1].option)
    }
}
