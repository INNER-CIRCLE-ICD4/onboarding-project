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
import kotlin.test.assertNotNull

/**
 * packageName : kr.innercircle.onboarding.survey.unit.service
 * fileName    : SurveyItemServiceTest
 * author      : ckr
 * date        : 25. 6. 20.
 * description :
 */

class SurveyItemServiceTest {
    private val testDiContainer = TestDiContainer()
    private val surveyRepository = testDiContainer.surveyRepository
    private val surveyItemRepository = testDiContainer.surveyItemRepository
    private val surveyItemOptionRepository = testDiContainer.surveyItemOptionRepository
    private val surveyItemService = testDiContainer.surveyItemService

    @Test
    fun createSurveyItems_단일_설문조사_항목을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "이름을 입력해주세요",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(1, surveyItems.size)
        val surveyItem = surveyItems[0]
        assertNotNull(surveyItem.id)
        assertEquals("이름을 입력해주세요", surveyItem.name)
        assertEquals(SurveyItemInputType.SHORT_TEXT, surveyItem.inputType)
        assertEquals(true, surveyItem.isRequired)
        assertEquals(1, surveyItem.orderNumber) // 자동으로 1부터 시작
        assertEquals(survey, surveyItem.survey)
        assertEquals(false, surveyItem.isDeleted)

        // 저장소에 저장되었는지 확인
        val savedItem = surveyItemRepository.findById(surveyItem.id!!)
        assertNotNull(savedItem)
        assertEquals("이름을 입력해주세요", savedItem.name)
    }

    @Test
    fun createSurveyItems_여러_설문조사_항목을_순서대로_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "첫 번째 질문",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "두 번째 질문",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList(),
                isRequired = false
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "세 번째 질문",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("옵션1"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("옵션2")
                ),
                isRequired = true
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(3, surveyItems.size)

        // 첫 번째 항목 검증
        assertEquals("첫 번째 질문", surveyItems[0].name)
        assertEquals(SurveyItemInputType.SHORT_TEXT, surveyItems[0].inputType)
        assertEquals(true, surveyItems[0].isRequired)
        assertEquals(1, surveyItems[0].orderNumber)

        // 두 번째 항목 검증
        assertEquals("두 번째 질문", surveyItems[1].name)
        assertEquals(SurveyItemInputType.LONG_TEXT, surveyItems[1].inputType)
        assertEquals(false, surveyItems[1].isRequired)
        assertEquals(2, surveyItems[1].orderNumber)

        // 세 번째 항목 검증
        assertEquals("세 번째 질문", surveyItems[2].name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, surveyItems[2].inputType)
        assertEquals(true, surveyItems[2].isRequired)
        assertEquals(3, surveyItems[2].orderNumber)

        // 모든 항목이 같은 설문조사에 속하는지 확인
        surveyItems.forEach { item ->
            assertEquals(survey, item.survey)
        }

        // 선택형 항목의 옵션이 생성되었는지 확인
        val options = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[2].id }
        assertEquals(2, options.size)
    }

    @Test
    fun createSurveyItems_모든_입력_타입의_항목을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "단답형 질문",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList()
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "장문형 질문",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList()
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "단일선택 질문",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("선택1"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("선택2")
                )
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "다중선택 질문",
                inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("다중1"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("다중2"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("다중3")
                )
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(4, surveyItems.size)
        assertEquals(SurveyItemInputType.SHORT_TEXT, surveyItems[0].inputType)
        assertEquals(SurveyItemInputType.LONG_TEXT, surveyItems[1].inputType)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, surveyItems[2].inputType)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, surveyItems[3].inputType)

        // 순서번호가 1, 2, 3, 4로 자동 설정되었는지 확인
        assertEquals(1, surveyItems[0].orderNumber)
        assertEquals(2, surveyItems[1].orderNumber)
        assertEquals(3, surveyItems[2].orderNumber)
        assertEquals(4, surveyItems[3].orderNumber)

        // 선택형 항목들의 옵션이 생성되었는지 확인
        val singleChoiceOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[2].id }
        assertEquals(2, singleChoiceOptions.size)

        val multipleChoiceOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[3].id }
        assertEquals(3, multipleChoiceOptions.size)
    }

    @Test
    fun createSurveyItems_빈_리스트로_호출하면_빈_리스트를_반환한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = emptyList<kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest>()

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(0, surveyItems.size)

        // 저장소에도 항목이 생성되지 않았는지 확인
        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == survey.id }
        assertEquals(0, savedItems.size)
    }

    @Test
    fun createSurveyItems_선택형_항목에_옵션이_부족하면_예외를_발생시킨다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "부족한 옵션 질문",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("단일 옵션")
                )
            )
        )

        // when & then
        assertThrows<InsufficientSurveyItemOptionsException> {
            surveyItemService.createSurveyItems(survey, createSurveyItemRequests)
        }
    }

    @Test
    fun createSurveyItems_텍스트형_항목에_옵션이_있으면_예외를_발생시킨다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "잘못된 텍스트 질문",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("잘못된 옵션")
                )
            )
        )

        // when & then
        assertThrows<InvalidSurveyItemTypeException> {
            surveyItemService.createSurveyItems(survey, createSurveyItemRequests)
        }
    }

    @Test
    fun createSurveyItems_긴_이름과_설명을_가진_항목들을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val longName = "매우 긴 설문조사 항목 이름입니다. ".repeat(5)
        val longDescription = "매우 긴 설명입니다. ".repeat(20)
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = longName,
                description = longDescription,
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList()
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(1, surveyItems.size)
        assertEquals(longName, surveyItems[0].name)
        assertEquals(longDescription, surveyItems[0].description)
        assertEquals(SurveyItemInputType.LONG_TEXT, surveyItems[0].inputType)
    }

    @Test
    fun createSurveyItems_특수문자가_포함된_항목들을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val specialCharName = "특수문자!@#$%^&*()질문"
        val unicodeName = "유니코드🌍질문"
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = specialCharName,
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList()
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = unicodeName,
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList()
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(2, surveyItems.size)
        assertEquals(specialCharName, surveyItems[0].name)
        assertEquals(unicodeName, surveyItems[1].name)
        assertEquals(1, surveyItems[0].orderNumber)
        assertEquals(2, surveyItems[1].orderNumber)
    }

    @Test
    fun createSurveyItems_null과_빈_설명을_가진_항목들을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "null 설명 질문",
                description = null,
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList()
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "빈 설명 질문",
                description = "",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList()
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "공백 설명 질문",
                description = "   ",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList()
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(3, surveyItems.size)
        assertEquals(null, surveyItems[0].description)
        assertEquals("", surveyItems[1].description)
        assertEquals("   ", surveyItems[2].description)
    }

    @Test
    fun createSurveyItems_필수와_선택_조합을_가진_항목들을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "필수 질문",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "선택 질문",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList(),
                isRequired = false
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "필수 선택형 질문",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("예"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("아니오")
                ),
                isRequired = true
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(3, surveyItems.size)
        assertEquals(true, surveyItems[0].isRequired)
        assertEquals(false, surveyItems[1].isRequired)
        assertEquals(true, surveyItems[2].isRequired)
    }

    @Test
    fun createSurveyItems_대량의_항목을_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성())
        val createSurveyItemRequests = (1..10).map { index ->
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "질문 $index",
                description = "질문 $index 에 대한 설명",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = index % 2 == 1 // 홀수번째는 필수, 짝수번째는 선택
            )
        }

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(10, surveyItems.size)

        // 순서번호가 1부터 10까지 자동으로 설정되었는지 확인
        surveyItems.forEachIndexed { index, item ->
            assertEquals("질문 ${index + 1}", item.name)
            assertEquals("질문 ${index + 1} 에 대한 설명", item.description)
            assertEquals(index + 1, item.orderNumber)
            assertEquals((index + 1) % 2 == 1, item.isRequired)
            assertEquals(survey, item.survey)
        }

        // 저장소에 모든 항목이 저장되었는지 확인
        val savedItems = surveyItemRepository.findAll().filter { it.survey.id == survey.id }
        assertEquals(10, savedItems.size)
    }

    @Test
    fun createSurveyItems_혼합된_타입과_옵션을_가진_복잡한_설문조사를_생성한다() {
        // given
        val survey = surveyRepository.save(SurveyFixture.설문조사_생성("복합 설문조사"))
        val createSurveyItemRequests = listOf(
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "개인정보 - 이름",
                description = "실명을 입력해주세요",
                inputType = SurveyItemInputType.SHORT_TEXT,
                options = emptyList(),
                isRequired = true
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "성별",
                description = "해당하는 성별을 선택해주세요",
                inputType = SurveyItemInputType.SINGLE_CHOICE,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("남성"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("여성"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("기타")
                ),
                isRequired = true
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "관심사",
                description = "관심있는 분야를 모두 선택해주세요",
                inputType = SurveyItemInputType.MULTIPLE_CHOICE,
                options = listOf(
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("기술"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("예술"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("스포츠"),
                    SurveyItemOptionFixture.설문조사_항목_옵션_생성_요청("여행")
                ),
                isRequired = false
            ),
            SurveyItemFixture.설문조사_항목_생성_요청(
                name = "추가 의견",
                description = "서비스 개선을 위한 의견을 자유롭게 작성해주세요",
                inputType = SurveyItemInputType.LONG_TEXT,
                options = emptyList(),
                isRequired = false
            )
        )

        // when
        val surveyItems = surveyItemService.createSurveyItems(survey, createSurveyItemRequests)

        // then
        assertEquals(4, surveyItems.size)

        // 각 항목의 세부 검증
        assertEquals("개인정보 - 이름", surveyItems[0].name)
        assertEquals(SurveyItemInputType.SHORT_TEXT, surveyItems[0].inputType)
        assertEquals(true, surveyItems[0].isRequired)
        assertEquals(1, surveyItems[0].orderNumber)

        assertEquals("성별", surveyItems[1].name)
        assertEquals(SurveyItemInputType.SINGLE_CHOICE, surveyItems[1].inputType)
        assertEquals(true, surveyItems[1].isRequired)
        assertEquals(2, surveyItems[1].orderNumber)

        assertEquals("관심사", surveyItems[2].name)
        assertEquals(SurveyItemInputType.MULTIPLE_CHOICE, surveyItems[2].inputType)
        assertEquals(false, surveyItems[2].isRequired)
        assertEquals(3, surveyItems[2].orderNumber)

        assertEquals("추가 의견", surveyItems[3].name)
        assertEquals(SurveyItemInputType.LONG_TEXT, surveyItems[3].inputType)
        assertEquals(false, surveyItems[3].isRequired)
        assertEquals(4, surveyItems[3].orderNumber)

        // 옵션들이 정확히 생성되었는지 확인
        val genderOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[1].id }
        assertEquals(3, genderOptions.size)

        val interestOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[2].id }
        assertEquals(4, interestOptions.size)

        // 텍스트형 항목들은 옵션이 없어야 함
        val nameOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[0].id }
        assertEquals(0, nameOptions.size)

        val commentOptions = surveyItemOptionRepository.findAll().filter { it.surveyItem.id == surveyItems[3].id }
        assertEquals(0, commentOptions.size)
    }
}
