package kr.innercircle.onboarding.survey.unit.service

import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.exception.InsufficientSurveyItemOptionsException
import kr.innercircle.onboarding.survey.exception.InvalidSurveyItemTypeException
import kr.innercircle.onboarding.survey.unit.TestDiContainer
import kr.innercircle.onboarding.survey.unit.fixture.SurveyFixture
import kr.innercircle.onboarding.survey.unit.fixture.SurveyItemFixture
import kr.innercircle.onboarding.survey.unit.fixture.SurveyItemOptionFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

/**
 * packageName : kr.innercircle.onboarding.survey.unit.service
 * fileName    : SurveyItemOptionServiceTest
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */

class SurveyItemOptionServiceTest {
    private val testDiContainer = TestDiContainer()
    private val surveyRepository = testDiContainer.surveyRepository
    private val surveyItemRepository = testDiContainer.surveyItemRepository
    private val surveyItemOptionRepository = testDiContainer.surveyItemOptionRepository
    private val surveyItemOptionService = testDiContainer.surveyItemOptionService

    @Test
    fun createSurveyItemOptions_SINGLE_CHOICE_타입의_설문_항목에_여러_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("만족도 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "서비스 만족도를 선택해주세요",
                surveyItemInputType = SurveyItemInputType.SINGLE_CHOICE
            )
        )
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("매우 만족"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("만족"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("보통")
        )

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(3, surveyItemOptions.size)
        
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        assertEquals("매우 만족", sortedOptions[0].option)
        assertEquals(1, sortedOptions[0].orderNumber)
        assertEquals("만족", sortedOptions[1].option)
        assertEquals(2, sortedOptions[1].orderNumber)
        assertEquals("보통", sortedOptions[2].option)
        assertEquals(3, sortedOptions[2].orderNumber)

        // 저장소에 저장되었는지 확인
        val savedOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItem.id }
        assertEquals(3, savedOptions.size)
    }

    @Test
    fun createSurveyItemOptions_MULTIPLE_CHOICE_타입의_설문_항목에_여러_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("취미 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "취미를 모두 선택해주세요",
                surveyItemInputType = SurveyItemInputType.MULTIPLE_CHOICE
            )
        )
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("독서"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("운동"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("영화감상"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("음악감상")
        )

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(4, surveyItemOptions.size)
        
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        assertEquals("독서", sortedOptions[0].option)
        assertEquals("운동", sortedOptions[1].option)
        assertEquals("영화감상", sortedOptions[2].option)
        assertEquals("음악감상", sortedOptions[3].option)

        // 저장소에 저장되었는지 확인
        val savedOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItem.id }
        assertEquals(4, savedOptions.size)
    }

    @Test
    fun createSurveyItemOptions_SHORT_TEXT_타입의_설문_항목에_옵션을_생성하면_예외를_발생시킨다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("기본 정보 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "이름을 입력해주세요",
                surveyItemInputType = SurveyItemInputType.SHORT_TEXT
            )
        )
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("잘못된 옵션1"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("잘못된 옵션2")
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)
        }
    }

    @Test
    fun createSurveyItemOptions_LONG_TEXT_타입의_설문_항목에_옵션을_생성하면_예외를_발생시킨다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("의견 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "의견을 자세히 작성해주세요",
                surveyItemInputType = SurveyItemInputType.LONG_TEXT
            )
        )
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("잘못된 옵션1"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("잘못된 옵션2")
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)
        }
    }

    @Test
    fun createSurveyItemOptions_선택형_항목에_2개_미만의_옵션을_생성하면_예외를_발생시킨다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("테스트 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "선택해주세요",
                surveyItemInputType = SurveyItemInputType.SINGLE_CHOICE
            )
        )
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("단일 옵션")
        )

        // when & then
        assertThrows<InsufficientSurveyItemOptionsException> {
            surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)
        }
    }

    @Test
    fun createSurveyItemOptions_빈_리스트로_선택형_항목에_옵션을_생성하면_예외를_발생시킨다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("테스트 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "선택해주세요",
                surveyItemInputType = SurveyItemInputType.MULTIPLE_CHOICE
            )
        )
        val createSurveyItemOptionRequests = emptyList<kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest>()

        // when & then
        assertThrows<InsufficientSurveyItemOptionsException> {
            surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)
        }
    }

    @Test
    fun createSurveyItemOptions_긴_텍스트_옵션들로_선택형_항목에_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("상세 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "상세 옵션을 선택해주세요",
                surveyItemInputType = SurveyItemInputType.MULTIPLE_CHOICE
            )
        )
        val longOption1 = "매우 긴 옵션 텍스트입니다. 이 옵션은 200자 제한까지 테스트하기 위한 첫 번째 옵션입니다."
        val longOption2 = "또 다른 긴 옵션 텍스트입니다. 이 옵션은 200자 제한까지 테스트하기 위한 두 번째 옵션입니다."
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(longOption1),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(longOption2)
        )

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(2, surveyItemOptions.size)
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        assertEquals(longOption1, sortedOptions[0].option)
        assertEquals(longOption2, sortedOptions[1].option)
    }

    @Test
    fun createSurveyItemOptions_특수문자가_포함된_옵션들로_선택형_항목에_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("특수문자 테스트 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "특수문자 테스트",
                surveyItemInputType = SurveyItemInputType.MULTIPLE_CHOICE
            )
        )
        val specialCharOption1 = "특수문자!@#$%^&*()_+-=[]{}|옵션1"
        val specialCharOption2 = "특수문자;':\",./<>?~`옵션2"
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(specialCharOption1),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(specialCharOption2)
        )

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(2, surveyItemOptions.size)
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        assertEquals(specialCharOption1, sortedOptions[0].option)
        assertEquals(specialCharOption2, sortedOptions[1].option)
    }

    @Test
    fun createSurveyItemOptions_유니코드_문자가_포함된_옵션들로_선택형_항목에_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("다국어 테스트 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "다국어 테스트",
                surveyItemInputType = SurveyItemInputType.SINGLE_CHOICE
            )
        )
        val unicodeOption1 = "한국어 🇰🇷 English 日本語"
        val unicodeOption2 = "中文 Español Français Русский العربية हिन्दी"
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(unicodeOption1),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(unicodeOption2)
        )

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(2, surveyItemOptions.size)
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        assertEquals(unicodeOption1, sortedOptions[0].option)
        assertEquals(unicodeOption2, sortedOptions[1].option)
    }

    @Test
    fun createSurveyItemOptions_공백이_포함된_옵션들로_선택형_항목에_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("공백 테스트 조사"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "공백 테스트",
                surveyItemInputType = SurveyItemInputType.MULTIPLE_CHOICE
            )
        )
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청(""),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("   "),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("  앞뒤 공백 옵션  ")
        )

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(3, surveyItemOptions.size)
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        assertEquals("", sortedOptions[0].option)
        assertEquals("   ", sortedOptions[1].option)
        assertEquals("  앞뒤 공백 옵션  ", sortedOptions[2].option)
    }

    @Test
    fun createSurveyItemOptions_정확히_2개의_옵션으로_선택형_항목에_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("최소 옵션 테스트"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "최소 옵션 테스트",
                surveyItemInputType = SurveyItemInputType.SINGLE_CHOICE
            )
        )
        val createSurveyItemOptionRequests = listOf(
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("첫 번째 옵션"),
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("두 번째 옵션")
        )

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(2, surveyItemOptions.size)
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        assertEquals("첫 번째 옵션", sortedOptions[0].option)
        assertEquals("두 번째 옵션", sortedOptions[1].option)
    }

    @Test
    fun createSurveyItemOptions_대량의_옵션으로_선택형_항목에_옵션을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("대량 옵션 테스트"))
        val surveyItem = surveyItemRepository.save(
            SurveyItemFixture.설문조사_항목_생성(
                survey = survey,
                name = "대량 옵션 테스트",
                surveyItemInputType = SurveyItemInputType.MULTIPLE_CHOICE
            )
        )
        val createSurveyItemOptionRequests = (1..10).map { index ->
            SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("옵션 $index")
        }

        // when
        val surveyItemOptions = surveyItemOptionService.createSurveyItemOptions(surveyItem, createSurveyItemOptionRequests)

        // then
        assertEquals(10, surveyItemOptions.size)
        val sortedOptions = surveyItemOptions.sortedBy { it.orderNumber }
        for (i in 0..9) {
            assertEquals("옵션 ${i + 1}", sortedOptions[i].option)
            assertEquals(i + 1, sortedOptions[i].orderNumber)
        }
    }
}
