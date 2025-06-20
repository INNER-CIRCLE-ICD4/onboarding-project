package kr.innercircle.onboarding.survey.unit.service

import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.exception.InvalidSurveyItemTypeException
import kr.innercircle.onboarding.survey.unit.TestDiContainer
import kr.innercircle.onboarding.survey.unit.fixture.SurveyFixture
import kr.innercircle.onboarding.survey.unit.fixture.SurveyItemFixture
import kr.innercircle.onboarding.survey.unit.fixture.SurveyItemOptionFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * packageName : kr.innercircle.onboarding.survey.unit.service
 * fileName    : SurveyServiceTest
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */

class SurveyServiceTest {
    private val testDiContainer = TestDiContainer()
    private val surveyRepository = testDiContainer.surveyRepository
    private val surveyItemRepository = testDiContainer.surveyItemRepository
    private val surveyItemOptionRepository = testDiContainer.surveyItemOptionRepository
    private val surveyService = testDiContainer.surveyService

    @Test
    fun createSurvey_단일_항목을_가진_설문조사를_생성한다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "고객 만족도 조사",
            description = "서비스 개선을 위한 설문조사입니다",
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "이름을 입력해주세요",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = emptyList(),
                    isRequired = true
                )
            )
        )

        // when
        val survey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(survey.id)
        val savedSurvey = surveyRepository.findById(survey.id!!)
        assertNotNull(savedSurvey)
        assertEquals("고객 만족도 조사", savedSurvey.name)
        assertEquals("서비스 개선을 위한 설문조사입니다", savedSurvey.description)

        // 설문 항목이 생성되었는지 확인
        val surveyItems = surveyItemRepository.findAll().filter { it.survey.id == survey.id }
        assertEquals(1, surveyItems.size)
        assertEquals("이름을 입력해주세요", surveyItems[0].name)
        assertEquals(SurveyItemInputType.SHORT_TEXT, surveyItems[0].inputType)
    }

    @Test
    fun createSurvey_선택_옵션이_있는_설문조사를_생성한다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "좋아하는 색깔 조사",
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "좋아하는 색깔을 선택해주세요",
                    inputType = SurveyItemInputType.SINGLE_CHOICE,
                    options = listOf(
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("빨간색"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("파란색"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("초록색")
                    )
                )
            )
        )

        // when
        val survey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(survey.id)
        val savedSurvey = surveyRepository.findById(survey.id!!)
        assertNotNull(savedSurvey)

        // 설문 항목이 생성되었는지 확인
        val surveyItems = surveyItemRepository.findAll().filter { it.survey.id == survey.id }
        assertEquals(1, surveyItems.size)

        val surveyItem = surveyItems[0]
        assertEquals("좋아하는 색깔을 선택해주세요", surveyItem.name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, surveyItem.inputType)

        // 선택 옵션들이 생성되었는지 확인
        val options = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItem.id }
        assertEquals(3, options.size)

        val sortedOptions = options.sortedBy { it.orderNumber }
        assertEquals("빨간색", sortedOptions[0].option)
        assertEquals(1, sortedOptions[0].orderNumber)
        assertEquals("파란색", sortedOptions[1].option)
        assertEquals(2, sortedOptions[1].orderNumber)
        assertEquals("초록색", sortedOptions[2].option)
        assertEquals(3, sortedOptions[2].orderNumber)
    }

    @Test
    fun createSurvey_SHORT_TEXT_타입에_옵션이_있으면_예외를_발생시킨다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "잘못된 설문조사",
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "이름을 입력해주세요",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = listOf(
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("잘못된 옵션")
                    )
                )
            )
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyService.createSurvey(createSurveyRequest)
        }
    }

    @Test
    fun createSurvey_LONG_TEXT_타입에_옵션이_있으면_예외를_발생시킨다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "잘못된 설문조사",
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "의견을 작성해주세요",
                    inputType = SurveyItemInputType.LONG_TEXT,
                    options = listOf(
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("잘못된 옵션")
                    )
                )
            )
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyService.createSurvey(createSurveyRequest)
        }
    }

    @Test
    fun createSurvey_최소_옵션이_있는_선택형_설문_항목을_생성한다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "최소 옵션 선택형 테스트",
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "선택해주세요",
                    inputType = SurveyItemInputType.SINGLE_CHOICE,
                    options = listOf(
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("옵션1"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("옵션2")
                    )
                )
            )
        )

        // when
        val survey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(survey.id)
        val surveyItems = surveyItemRepository.findAll().filter { it.survey.id == survey.id }
        assertEquals(1, surveyItems.size)
        assertEquals("선택해주세요", surveyItems[0].name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, surveyItems[0].inputType)

        // 옵션이 정확히 2개 생성되었는지 확인
        val options = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[0].id }
        assertEquals(2, options.size)
    }

    @Test
    fun createSurvey_설명이_null인_설문조사를_생성한다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "간단한 설문",
            description = null,
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "의견을 입력해주세요",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = emptyList()
                )
            )
        )

        // when
        val survey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(survey.id)
        val savedSurvey = surveyRepository.findById(survey.id!!)
        assertNotNull(savedSurvey)
        assertEquals("간단한 설문", savedSurvey.name)
        assertEquals(null, savedSurvey.description)
    }

    @Test
    fun createSurvey_혼합_타입_항목들을_가진_복합_설문조사를_생성한다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "종합 설문조사",
            description = "다양한 형태의 질문이 포함된 설문조사",
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "이름을 입력해주세요",
                    inputType = SurveyItemInputType.SHORT_TEXT,
                    options = emptyList(),
                    isRequired = true
                ),
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "성별을 선택해주세요",
                    inputType = SurveyItemInputType.SINGLE_CHOICE,
                    options = listOf(
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("남성"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("여성"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("기타")
                    ),
                    isRequired = true
                ),
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "취미를 모두 선택해주세요",
                    inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                    options = listOf(
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("독서"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("운동"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("음악감상")
                    ),
                    isRequired = false
                ),
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "추가 의견이 있으시면 작성해주세요",
                    inputType = SurveyItemInputType.LONG_TEXT,
                    options = emptyList(),
                    isRequired = false
                )
            )
        )

        // when
        val survey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(survey.id)
        val savedSurvey = surveyRepository.findById(survey.id!!)
        assertNotNull(savedSurvey)
        assertEquals("종합 설문조사", savedSurvey.name)
        assertEquals("다양한 형태의 질문이 포함된 설문조사", savedSurvey.description)

        // 모든 설문 항목이 생성되었는지 확인
        val surveyItems = surveyItemRepository.findAll().filter { it.survey.id == survey.id }
        assertEquals(4, surveyItems.size)

        val sortedItems = surveyItems.sortedBy { it.orderNumber }

        // 첫 번째 항목 (SHORT_TEXT)
        assertEquals("이름을 입력해주세요", sortedItems[0].name)
        assertEquals(SurveyItemInputType.SHORT_TEXT, sortedItems[0].inputType)
        assertEquals(true, sortedItems[0].isRequired)

        // 두 번째 항목 (SINGLE_CHOICE)
        assertEquals("성별을 선택해주세요", sortedItems[1].name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, sortedItems[1].inputType)
        assertEquals(true, sortedItems[1].isRequired)

        // 세 번째 항목 (MULTIPLE_CHOICE)
        assertEquals("취미를 모두 선택해주세요", sortedItems[2].name)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, sortedItems[2].inputType)
        assertEquals(false, sortedItems[2].isRequired)

        // 네 번째 항목 (LONG_TEXT)
        assertEquals("추가 의견이 있으시면 작성해주세요", sortedItems[3].name)
        assertEquals(SurveyItemInputType.LONG_TEXT, sortedItems[3].inputType)
        assertEquals(false, sortedItems[3].isRequired)

        // 선택 옵션들이 올바르게 생성되었는지 확인
        val allOptions = surveyItemOptionRepository.findAll()

        // 성별 선택 옵션 확인
        val genderOptions = allOptions.filter { it.surveyItem.id == sortedItems[1].id }.sortedBy { it.orderNumber }
        assertEquals(3, genderOptions.size)
        assertEquals("남성", genderOptions[0].option)
        assertEquals("여성", genderOptions[1].option)
        assertEquals("기타", genderOptions[2].option)

        // 취미 선택 옵션 확인
        val hobbyOptions = allOptions.filter { it.surveyItem.id == sortedItems[2].id }.sortedBy { it.orderNumber }
        assertEquals(3, hobbyOptions.size)
        assertEquals("독서", hobbyOptions[0].option)
        assertEquals("운동", hobbyOptions[1].option)
        assertEquals("음악감상", hobbyOptions[2].option)
    }

    @Test
    fun createSurvey_다중_선택_옵션이_있는_설문조사를_생성한다() {
        // given
        val createSurveyRequest = SurveyFixture.설문조사_생성_요청(
            name = "관심사 조사",
            surveyItems = listOf(
                SurveyItemFixture.설문조사_항목_생성_요청(
                    name = "관심사를 모두 선택해주세요",
                    inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                    options = listOf(
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("영화"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("음악"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("독서"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("운동"),
                        SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("게임")
                    )
                )
            )
        )

        // when
        val survey = surveyService.createSurvey(createSurveyRequest)

        // then
        assertNotNull(survey.id)

        // 설문 항목이 생성되었는지 확인
        val surveyItems = surveyItemRepository.findAll().filter { it.survey.id == survey.id }
        assertEquals(1, surveyItems.size)

        val surveyItem = surveyItems[0]
        assertEquals("관심사를 모두 선택해주세요", surveyItem.name)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, surveyItem.inputType)

        // 선택 옵션들이 생성되었는지 확인
        val options = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItem.id }
        assertEquals(5, options.size)

        val sortedOptions = options.sortedBy { it.orderNumber }
        assertEquals("영화", sortedOptions[0].option)
        assertEquals("음악", sortedOptions[1].option)
        assertEquals("독서", sortedOptions[2].option)
        assertEquals("운동", sortedOptions[3].option)
        assertEquals("게임", sortedOptions[4].option)
    }
}
